package com.hbm.ntm.common.network;

import com.hbm.ntm.common.block.entity.MachineBlockEntity;
import com.hbm.ntm.common.menu.MachineMenuBase;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

@SuppressWarnings("null")
public record MachineStateRequestPacket(BlockPos pos) {
    public static void encode(final MachineStateRequestPacket packet, final FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
    }

    public static MachineStateRequestPacket decode(final FriendlyByteBuf buffer) {
        return new MachineStateRequestPacket(buffer.readBlockPos());
    }

    public static void handle(final MachineStateRequestPacket packet, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            if (!player.level().isLoaded(packet.pos)) {
                return;
            }
            final BlockEntity blockEntity = player.level().getBlockEntity(packet.pos);
            if (!(blockEntity instanceof MachineBlockEntity machine)) {
                return;
            }
            if (!machine.canPlayerControl(player)) {
                return;
            }
            final AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof MachineMenuBase<?> machineMenu && machineMenu.machine() != null && machineMenu.machine().getBlockPos().equals(packet.pos)) {
                machine.syncMachineStateToPlayer(player, true);
            }
        });
        context.setPacketHandled(true);
    }
}

package com.hbm.ntm.common.network;

import com.hbm.ntm.common.machine.IMachineControlReceiver;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public record MachineControlPacket(BlockPos pos, CompoundTag data) {
    public static void encode(final MachineControlPacket packet, final FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeNbt(packet.data);
    }

    public static MachineControlPacket decode(final FriendlyByteBuf buffer) {
        return new MachineControlPacket(buffer.readBlockPos(), buffer.readNbt());
    }

    public static void handle(final MachineControlPacket packet, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            final BlockEntity blockEntity = player.level().getBlockEntity(packet.pos);
            if (!(blockEntity instanceof IMachineControlReceiver receiver)) {
                return;
            }
            if (!receiver.canPlayerControl(player)) {
                return;
            }
            receiver.receiveControl(player, packet.data.copy());
        });
        context.setPacketHandled(true);
    }
}

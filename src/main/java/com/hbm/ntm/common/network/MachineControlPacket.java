package com.hbm.ntm.common.network;

import com.hbm.ntm.common.machine.IMachineControlReceiver;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

@SuppressWarnings("null")
public record MachineControlPacket(BlockPos pos, CompoundTag data) {
    private static final String PAYLOAD_NAME = "machine_control";

    public static void encode(final MachineControlPacket packet, final FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        NbtPacketBufferHelper.writeCompound(buffer, packet.data, PAYLOAD_NAME);
    }

    public static MachineControlPacket decode(final FriendlyByteBuf buffer) {
        final BlockPos pos = buffer.readBlockPos();
        final CompoundTag data = NbtPacketBufferHelper.readCompound(buffer, PAYLOAD_NAME);
        return new MachineControlPacket(pos, data);
    }

    public static void handle(final MachineControlPacket packet, final Supplier<NetworkEvent.Context> contextSupplier) {
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
            if (!(blockEntity instanceof IMachineControlReceiver receiver)) {
                return;
            }
            if (!receiver.canPlayerControl(player)) {
                return;
            }
            if (!receiver.isControlDataAllowed(player, packet.data)) {
                return;
            }
            receiver.receiveControl(player, packet.data.copy());
        });
        context.setPacketHandled(true);
    }
}

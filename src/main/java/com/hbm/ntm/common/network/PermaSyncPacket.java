package com.hbm.ntm.common.network;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

@SuppressWarnings("null")
public record PermaSyncPacket(CompoundTag data) {
    private static final String PAYLOAD_NAME = "perma_sync";

    public static void encode(final PermaSyncPacket packet, final FriendlyByteBuf buffer) {
        NbtPacketBufferHelper.writeCompound(buffer, packet.data, PAYLOAD_NAME);
    }

    public static PermaSyncPacket decode(final FriendlyByteBuf buffer) {
        return new PermaSyncPacket(NbtPacketBufferHelper.readCompound(buffer, PAYLOAD_NAME));
    }

    public static void handle(final PermaSyncPacket packet, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> HbmPacketHandler.dispatchPermaSyncOnClient(packet));
        context.setPacketHandled(true);
    }
}

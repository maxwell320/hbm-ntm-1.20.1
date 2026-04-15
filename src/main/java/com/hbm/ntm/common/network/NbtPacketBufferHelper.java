package com.hbm.ntm.common.network;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.config.HbmCommonConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public final class NbtPacketBufferHelper {
    private static final String FALLBACK_PAYLOAD_NAME = "unknown";

    private NbtPacketBufferHelper() {
    }

    public static void writeCompound(final FriendlyByteBuf buffer, final CompoundTag tag, final String payloadName) {
        final int maxBytes = getConfiguredMaxBytes();
        final FriendlyByteBuf payload = new FriendlyByteBuf(Unpooled.buffer());
        try {
            payload.writeNbt(tag == null ? new CompoundTag() : tag);
        } catch (final Exception exception) {
            HbmNtmMod.LOGGER.warn("Failed to encode {} payload", safePayloadName(payloadName), exception);
            payload.release();
            buffer.writeVarInt(0);
            return;
        }

        final int payloadSize = payload.readableBytes();
        if (payloadSize > maxBytes) {
            HbmNtmMod.LOGGER.warn("Dropping oversized {} payload ({} bytes > {} bytes)", safePayloadName(payloadName), payloadSize, maxBytes);
            payload.release();
            buffer.writeVarInt(0);
            return;
        }

        buffer.writeVarInt(payloadSize);
        buffer.writeBytes(payload, payload.readerIndex(), payloadSize);
        payload.release();
    }

    public static CompoundTag readCompound(final FriendlyByteBuf buffer, final String payloadName) {
        final int maxBytes = getConfiguredMaxBytes();
        final int length = buffer.readVarInt();
        if (length <= 0) {
            return new CompoundTag();
        }
        if (length > maxBytes) {
            final int skip = Math.min(length, buffer.readableBytes());
            buffer.skipBytes(skip);
            HbmNtmMod.LOGGER.warn("Rejected oversized {} payload ({} bytes > {} bytes)", safePayloadName(payloadName), length, maxBytes);
            return new CompoundTag();
        }
        if (length > buffer.readableBytes()) {
            final int readable = buffer.readableBytes();
            buffer.skipBytes(readable);
            HbmNtmMod.LOGGER.warn("Rejected truncated {} payload (declared {} bytes, readable {})",
                safePayloadName(payloadName), length, readable);
            return new CompoundTag();
        }

        final FriendlyByteBuf payload = new FriendlyByteBuf(buffer.readBytes(length));
        try {
            final CompoundTag decoded = payload.readNbt();
            return decoded == null ? new CompoundTag() : decoded;
        } catch (final Exception exception) {
            HbmNtmMod.LOGGER.warn("Failed to decode {} payload", safePayloadName(payloadName), exception);
            return new CompoundTag();
        } finally {
            payload.release();
        }
    }

    private static int getConfiguredMaxBytes() {
        return Math.max(1_024, HbmCommonConfig.NETWORK_MAX_NBT_PAYLOAD_BYTES.get());
    }

    private static String safePayloadName(final String payloadName) {
        return payloadName == null || payloadName.isBlank() ? FALLBACK_PAYLOAD_NAME : payloadName;
    }
}

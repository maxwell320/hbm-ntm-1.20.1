package com.hbm.ntm.common.network;

import com.hbm.ntm.HbmNtmMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class HbmPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
        .named(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "main"))
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
        .serverAcceptedVersions(PROTOCOL_VERSION::equals)
        .simpleChannel();
    private static int nextId;

    private HbmPacketHandler() {
    }

    public static void register() {
        nextId = 0;
        CHANNEL.registerMessage(nextId++, MachineControlPacket.class, MachineControlPacket::encode, MachineControlPacket::decode, MachineControlPacket::handle);
    }
}

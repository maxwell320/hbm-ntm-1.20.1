package com.hbm.ntm.common.network;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.MachineBlockEntity;
import com.hbm.ntm.common.config.HbmCommonConfig;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class HbmPacketHandler {
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
        .named(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "main"))
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
        .serverAcceptedVersions(PROTOCOL_VERSION::equals)
        .simpleChannel();
    private static boolean registered;
    private static int nextId;
    private static Consumer<MachineStateSyncPacket> machineStateClientDispatcher = packet -> {
    };
    private static Consumer<PermaSyncPacket> permaSyncClientDispatcher = packet -> {
    };

    private HbmPacketHandler() {
    }

    public static void register() {
        if (registered) {
            trace("Network registration skipped (already registered)");
            return;
        }
        nextId = 0;
        registerMessage("machine_control", MachineControlPacket.class, MachineControlPacket::encode, MachineControlPacket::decode, MachineControlPacket::handle);
        registerMessage("machine_state_sync", MachineStateSyncPacket.class, MachineStateSyncPacket::encode, MachineStateSyncPacket::decode,
            MachineStateSyncPacket::handle);
        registerMessage("machine_state_request", MachineStateRequestPacket.class, MachineStateRequestPacket::encode, MachineStateRequestPacket::decode,
            MachineStateRequestPacket::handle);
        registerMessage("perma_sync", PermaSyncPacket.class, PermaSyncPacket::encode, PermaSyncPacket::decode, PermaSyncPacket::handle);
        registered = true;
        trace("Network channel initialized with protocol {} and {} packets", PROTOCOL_VERSION, nextId);
    }

    public static void syncMachineState(final MachineBlockEntity machine, final CompoundTag data) {
        final Level level = machine.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }
        final LevelChunk chunk = level.getChunkAt(machine.getBlockPos());
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new MachineStateSyncPacket(machine.getBlockPos(), data.copy()));
        trace("S2C machine_state_sync pos={} chunk={}", machine.getBlockPos(), chunk.getPos());
    }

    public static void syncMachineStateToPlayer(final MachineBlockEntity machine, final CompoundTag data, final ServerPlayer player) {
        final Level level = machine.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MachineStateSyncPacket(machine.getBlockPos(), data.copy()));
        trace("S2C machine_state_sync pos={} player={}", machine.getBlockPos(), player.getGameProfile().getName());
    }

    public static void sendPermaSyncToPlayer(final ServerPlayer player, final CompoundTag data) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PermaSyncPacket(data.copy()));
        trace("S2C perma_sync player={}", player.getGameProfile().getName());
    }

    public static void setMachineStateClientDispatcher(final Consumer<MachineStateSyncPacket> dispatcher) {
        machineStateClientDispatcher = dispatcher == null ? packet -> {
        } : dispatcher;
    }

    public static void dispatchMachineStateOnClient(final MachineStateSyncPacket packet) {
        machineStateClientDispatcher.accept(packet);
    }

    public static void setPermaSyncClientDispatcher(final Consumer<PermaSyncPacket> dispatcher) {
        permaSyncClientDispatcher = dispatcher == null ? packet -> {
        } : dispatcher;
    }

    public static void dispatchPermaSyncOnClient(final PermaSyncPacket packet) {
        permaSyncClientDispatcher.accept(packet);
    }

    private static <MSG> void registerMessage(final String name,
                                              final Class<MSG> packetType,
                                              final BiConsumer<MSG, FriendlyByteBuf> encoder,
                                              final Function<FriendlyByteBuf, MSG> decoder,
                                              final BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler) {
        final int packetId = nextId++;
        CHANNEL.registerMessage(packetId, packetType, encoder, decoder, handler);
        trace("Registered packet id={} name={} type={}", packetId, name, packetType.getSimpleName());
    }

    private static void trace(final String format, final Object... args) {
        if (!HbmCommonConfig.ENABLE_NETWORK_TRACE_LOGGING.get()) {
            return;
        }
        HbmNtmMod.LOGGER.info("[network] " + format, args);
    }
}

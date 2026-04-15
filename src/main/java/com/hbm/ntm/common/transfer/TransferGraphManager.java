package com.hbm.ntm.common.transfer;

import com.hbm.ntm.HbmNtmMod;
import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("null")
@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TransferGraphManager {
    private static final Map<ResourceKey<Level>, Map<TransferNetworkKind, Map<BlockPos, TransferNetwork>>> NETWORKS = new HashMap<>();
    private static final Map<ResourceKey<Level>, Map<TransferNetworkKind, Set<BlockPos>>> DIRTY_NODES = new HashMap<>();

    private TransferGraphManager() {
    }

    public static void markDirty(final Level level, final BlockPos pos, final TransferNetworkKind kind) {
        if (level.isClientSide()) {
            return;
        }
        final Map<TransferNetworkKind, Set<BlockPos>> byKind = DIRTY_NODES.computeIfAbsent(level.dimension(), key -> new EnumMap<>(TransferNetworkKind.class));
        final Set<BlockPos> dirty = byKind.computeIfAbsent(kind, key -> new HashSet<>());
        dirty.add(pos.immutable());
        for (final Direction direction : Direction.values()) {
            dirty.add(pos.relative(direction).immutable());
        }
    }

    public static TransferNetwork rebuildIfDirty(final Level level, final BlockPos origin, final TransferNetworkKind kind) {
        if (level.isClientSide()) {
            return new TransferNetwork(kind);
        }
        final Map<TransferNetworkKind, Set<BlockPos>> byKind = DIRTY_NODES.get(level.dimension());
        if (byKind == null) {
            return getNetwork(level, origin, kind);
        }
        final Set<BlockPos> dirty = byKind.get(kind);
        if (dirty == null || !dirty.remove(origin)) {
            return getNetwork(level, origin, kind);
        }
        return rebuildNetwork(level, origin, kind);
    }

    public static TransferNetwork rebuildNetwork(final Level level, final BlockPos origin, final TransferNetworkKind kind) {
        if (level.isClientSide()) {
            return new TransferNetwork(kind);
        }
        final BlockEntity startEntity = level.getBlockEntity(origin);
        if (!(startEntity instanceof final TransferNodeProvider startProvider) || startProvider.getTransferNetworkKind() != kind) {
            removeNode(level, origin, kind);
            return new TransferNetwork(kind);
        }

        final TransferNetwork network = new TransferNetwork(kind);
        final ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        final Set<BlockPos> visited = new HashSet<>();
        queue.add(origin.immutable());

        while (!queue.isEmpty()) {
            final BlockPos pos = queue.removeFirst();
            if (!visited.add(pos)) {
                continue;
            }

            final BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof final TransferNodeProvider provider) || provider.getTransferNetworkKind() != kind) {
                continue;
            }

            network.add(pos);
            for (final Direction direction : provider.getConnectionDirections()) {
                final BlockPos neighborPos = pos.relative(direction);
                final BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                if (!(neighborEntity instanceof final TransferNodeProvider neighborProvider) || neighborProvider.getTransferNetworkKind() != kind) {
                    continue;
                }
                if (!neighborProvider.getConnectionDirections().contains(direction.getOpposite())) {
                    continue;
                }
                if (!provider.canConnectTo(level, pos, direction, neighborEntity)) {
                    continue;
                }
                if (!neighborProvider.canConnectTo(level, neighborPos, direction.getOpposite(), blockEntity)) {
                    continue;
                }
                queue.add(neighborPos.immutable());
            }
        }

        storeNetwork(level, network);
        return network;
    }

    public static TransferNetwork getNetwork(final Level level, final BlockPos pos, final TransferNetworkKind kind) {
        if (level.isClientSide()) {
            return new TransferNetwork(kind);
        }
        final Map<BlockPos, TransferNetwork> byPos = NETWORKS
            .getOrDefault(level.dimension(), Map.of())
            .getOrDefault(kind, Map.of());
        final TransferNetwork network = byPos.get(pos);
        return network == null ? new TransferNetwork(kind) : network;
    }

    public static void recordTransfer(final Level level, final BlockPos pos, final TransferNetworkKind kind, final long amount) {
        if (level.isClientSide()) {
            return;
        }
        final TransferNetwork network = getNetwork(level, pos, kind);
        if (network.size() > 0) {
            network.recordTransfer(level.getGameTime(), amount);
        }
    }

    public static long getTransferThisTick(final Level level, final BlockPos pos, final TransferNetworkKind kind) {
        if (level.isClientSide()) {
            return 0L;
        }
        return getNetwork(level, pos, kind).getTransferThisTick(level.getGameTime());
    }

    public static void removeNode(final Level level, final BlockPos pos, final TransferNetworkKind kind) {
        if (level.isClientSide()) {
            return;
        }
        final Map<TransferNetworkKind, Map<BlockPos, TransferNetwork>> byKind = NETWORKS.get(level.dimension());
        if (byKind == null) {
            return;
        }
        final Map<BlockPos, TransferNetwork> byPos = byKind.get(kind);
        if (byPos == null) {
            return;
        }
        byPos.remove(pos);
        if (byPos.isEmpty()) {
            byKind.remove(kind);
            final Map<TransferNetworkKind, Set<BlockPos>> dirtyByKind = DIRTY_NODES.get(level.dimension());
            if (dirtyByKind != null) {
                dirtyByKind.remove(kind);
                if (dirtyByKind.isEmpty()) {
                    DIRTY_NODES.remove(level.dimension());
                }
            }
        }
        if (byKind.isEmpty()) {
            NETWORKS.remove(level.dimension());
        }
    }

    public static void clearLevel(final ResourceKey<Level> levelKey) {
        NETWORKS.remove(levelKey);
        DIRTY_NODES.remove(levelKey);
    }

    public static void clearAll() {
        NETWORKS.clear();
        DIRTY_NODES.clear();
    }

    @SubscribeEvent
    public static void onLevelUnload(final LevelEvent.Unload event) {
        if (!(event.getLevel() instanceof final Level level) || level.isClientSide()) {
            return;
        }
        clearLevel(level.dimension());
    }

    @SubscribeEvent
    public static void onServerStopped(final ServerStoppedEvent event) {
        clearAll();
    }

    private static void storeNetwork(final Level level, final TransferNetwork network) {
        final Map<TransferNetworkKind, Map<BlockPos, TransferNetwork>> byKind = NETWORKS.computeIfAbsent(level.dimension(), key -> new EnumMap<>(TransferNetworkKind.class));
        final Map<BlockPos, TransferNetwork> byPos = byKind.computeIfAbsent(network.kind(), key -> new HashMap<>());
        for (final BlockPos nodePos : network.nodes()) {
            byPos.put(nodePos, network);
        }
    }
}

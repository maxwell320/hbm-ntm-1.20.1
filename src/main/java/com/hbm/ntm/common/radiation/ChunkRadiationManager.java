package com.hbm.ntm.common.radiation;

import com.hbm.ntm.HbmNtmMod;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ChunkRadiationManager {
    private static final String NBT_KEY_CHUNK_RADIATION = "hbmntm_chunk_radiation";
    private static final float MAX_RADIATION = 100_000.0F;
    private static final Map<ServerLevel, Map<ChunkPos, Float>> PER_LEVEL = Collections.synchronizedMap(new IdentityHashMap<>());
    private static int tickCounter;

    private ChunkRadiationManager() {
    }

    public static float getRadiation(final Level level, final int x, final int y, final int z) {
        if (!(level instanceof final ServerLevel serverLevel)) {
            return 0.0F;
        }
        return getRadiation(serverLevel, new ChunkPos(x >> 4, z >> 4));
    }

    public static void setRadiation(final Level level, final int x, final int y, final int z, final float radiation) {
        if (!(level instanceof final ServerLevel serverLevel)) {
            return;
        }
        final ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
        final Map<ChunkPos, Float> radiationByChunk = radiationFor(serverLevel);
        final float clamped = clampRadiation(radiation);
        if (clamped <= 0.0F) {
            radiationByChunk.remove(chunkPos);
        } else {
            radiationByChunk.put(chunkPos, clamped);
        }
        markChunkUnsaved(serverLevel, chunkPos);
    }

    public static void incrementRad(final Level level, final int x, final int y, final int z, final float radiation) {
        setRadiation(level, x, y, z, getRadiation(level, x, y, z) + radiation);
    }

    public static void decrementRad(final Level level, final int x, final int y, final int z, final float radiation) {
        setRadiation(level, x, y, z, Math.max(getRadiation(level, x, y, z) - radiation, 0.0F));
    }

    @SubscribeEvent
    public static void onLevelLoad(final LevelEvent.Load event) {
        if (event.getLevel() instanceof final ServerLevel serverLevel) {
            radiationFor(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(final LevelEvent.Unload event) {
        if (event.getLevel() instanceof final ServerLevel serverLevel) {
            PER_LEVEL.remove(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(final ChunkDataEvent.Load event) {
        if (!(event.getLevel() instanceof final ServerLevel serverLevel)) {
            return;
        }
        final Map<ChunkPos, Float> radiationByChunk = radiationFor(serverLevel);
        final float radiation = clampRadiation(event.getData().getFloat(NBT_KEY_CHUNK_RADIATION));
        if (radiation > 0.0F) {
            radiationByChunk.put(event.getChunk().getPos(), radiation);
        } else {
            radiationByChunk.remove(event.getChunk().getPos());
        }
    }

    @SubscribeEvent
    public static void onChunkSave(final ChunkDataEvent.Save event) {
        if (!(event.getLevel() instanceof final ServerLevel serverLevel)) {
            return;
        }
        final float radiation = getRadiation(serverLevel, event.getChunk().getPos());
        event.getData().putFloat(NBT_KEY_CHUNK_RADIATION, radiation);
    }

    @SubscribeEvent
    public static void onChunkUnload(final ChunkEvent.Unload event) {
        if (!(event.getLevel() instanceof final ServerLevel serverLevel)) {
            return;
        }
        radiationFor(serverLevel).remove(event.getChunk().getPos());
    }

    @SubscribeEvent
    public static void onServerTick(final TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        tickCounter++;
        if (tickCounter >= 20) {
            updateSystem();
            tickCounter = 0;
        }
    }

    private static float getRadiation(final ServerLevel serverLevel, final ChunkPos chunkPos) {
        return clampRadiation(radiationFor(serverLevel).getOrDefault(chunkPos, 0.0F));
    }

    private static Map<ChunkPos, Float> radiationFor(final ServerLevel serverLevel) {
        synchronized (PER_LEVEL) {
            return PER_LEVEL.computeIfAbsent(serverLevel, key -> new ConcurrentHashMap<>());
        }
    }

    private static void updateSystem() {
        final Map<ServerLevel, Map<ChunkPos, Float>> perLevelSnapshot;
        synchronized (PER_LEVEL) {
            perLevelSnapshot = new IdentityHashMap<>(PER_LEVEL);
        }
        for (final Map.Entry<ServerLevel, Map<ChunkPos, Float>> perLevelEntry : perLevelSnapshot.entrySet()) {
            updateLevel(perLevelEntry.getKey(), perLevelEntry.getValue());
        }
    }

    private static void updateLevel(final ServerLevel serverLevel, final Map<ChunkPos, Float> radiationByChunk) {
        if (radiationByChunk.isEmpty()) {
            return;
        }
        final Map<ChunkPos, Float> previous = new HashMap<>(radiationByChunk);
        radiationByChunk.clear();

        for (final Map.Entry<ChunkPos, Float> chunkEntry : previous.entrySet()) {
            final float sourceRadiation = chunkEntry.getValue();
            if (sourceRadiation <= 0.0F) {
                continue;
            }

            final ChunkPos chunkPos = chunkEntry.getKey();
            for (int offsetX = -1; offsetX <= 1; offsetX++) {
                for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                    final int type = Math.abs(offsetX) + Math.abs(offsetZ);
                    final float percent = type == 0 ? 0.6F : type == 1 ? 0.075F : 0.025F;
                    final ChunkPos targetPos = new ChunkPos(chunkPos.x + offsetX, chunkPos.z + offsetZ);
                    final float updatedRadiation;

                    if (previous.containsKey(targetPos)) {
                        final float currentRadiation = radiationByChunk.getOrDefault(targetPos, 0.0F);
                        updatedRadiation = clampRadiation((currentRadiation + sourceRadiation * percent) * 0.99F - 0.05F);
                    } else {
                        updatedRadiation = clampRadiation(sourceRadiation * percent);
                    }

                    if (updatedRadiation > 0.0F) {
                        radiationByChunk.put(targetPos, updatedRadiation);
                        markChunkUnsaved(serverLevel, targetPos);
                    }
                }
            }
        }
    }

    private static void markChunkUnsaved(final ServerLevel serverLevel, final ChunkPos chunkPos) {
        final LevelChunk chunk = serverLevel.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
        if (chunk != null) {
            chunk.setUnsaved(true);
        }
    }

    private static float clampRadiation(final float radiation) {
        return Math.max(0.0F, Math.min(MAX_RADIATION, radiation));
    }
}

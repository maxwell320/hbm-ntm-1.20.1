package com.hbm.ntm.common.network;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.config.HbmCommonConfig;
import com.hbm.ntm.common.pollution.PollutionSavedData;
import com.hbm.ntm.common.pollution.PollutionType;
import com.hbm.ntm.common.radiation.ChunkRadiationManager;
import com.hbm.ntm.common.saveddata.TomImpactSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PermaSyncServerTicker {
    private PermaSyncServerTicker() {
    }

    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }
        if (!(event.player instanceof final ServerPlayer player)) {
            return;
        }

        final int interval = Math.max(5, HbmCommonConfig.NETWORK_PERMA_SYNC_INTERVAL_TICKS.get());
        if (player.tickCount % interval != 0) {
            return;
        }

        HbmPacketHandler.sendPermaSyncToPlayer(player, buildPayload(player));
    }

    private static CompoundTag buildPayload(final ServerPlayer player) {
        final BlockPos pos = player.blockPosition();
        final CompoundTag payload = new CompoundTag();

        payload.putFloat("chunkRadiation", ChunkRadiationManager.getRadiation(player.level(), pos.getX(), pos.getY(), pos.getZ()));
        payload.putFloat("pollutionSoot", PollutionSavedData.getPollution(player.level(), pos.getX(), pos.getY(), pos.getZ(), PollutionType.SOOT));
        payload.putFloat("pollutionHeavyMetal", PollutionSavedData.getPollution(player.level(), pos.getX(), pos.getY(), pos.getZ(), PollutionType.HEAVY_METAL));
        payload.putFloat("pollutionPoison", PollutionSavedData.getPollution(player.level(), pos.getX(), pos.getY(), pos.getZ(), PollutionType.POISON));

        final TomImpactSavedData tomImpact = TomImpactSavedData.get(player.level());
        payload.putFloat("tomDust", tomImpact.dust());
        payload.putFloat("tomFire", tomImpact.fire());
        payload.putBoolean("tomImpact", tomImpact.impact());

        return payload;
    }
}

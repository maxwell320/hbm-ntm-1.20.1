package com.hbm.ntm.common.radiation;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.damage.HbmDamageTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("null")
@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RadiationEventHandler {
    private RadiationEventHandler() {
    }

    @SubscribeEvent
    public static void onPlayerClone(final PlayerEvent.Clone event) {
        final CompoundTag tag = RadiationUtil.copyData(event.getOriginal());
        RadiationUtil.loadData(event.getEntity(), tag);
    }

    @SubscribeEvent
    public static void onLivingTick(final LivingEvent.LivingTickEvent event) {
        final LivingEntity entity = event.getEntity();
        if (entity.tickCount % 20 == 0) {
            RadiationUtil.setRadBuf(entity, RadiationUtil.getRadEnv(entity));
            RadiationUtil.setRadEnv(entity, 0.0F);
        }
        RadiationUtil.tickContaminationEffects(entity);
        handleRadiationEffects(entity);
        applyAmbientChunkRadiation(entity);
    }

    private static void handleRadiationEffects(final LivingEntity entity) {
        if (entity.level().isClientSide()) {
            return;
        }
        if (entity instanceof final Player player && player.isCreative()) {
            return;
        }

        float radiation = RadiationUtil.getRadiation(entity);
        if (handleRadiationTransformation(entity, radiation)) {
            return;
        }
        if (radiation < 200.0F || RadiationUtil.isRadImmune(entity)) {
            return;
        }
        if (radiation > 2500.0F) {
            radiation = 2500.0F;
            RadiationUtil.setRadiation(entity, radiation);
        }

        final RandomSource random = entity.getRandom();
        if (radiation >= 1000.0F) {
            RadiationUtil.setRadiation(entity, 0.0F);
            entity.hurt(HbmDamageTypes.radiation(entity.level()), 1000.0F);
            return;
        }
        if (radiation >= 800.0F) {
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0));
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2));
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2));
            maybeAddEffect(entity, random, 500, new MobEffectInstance(MobEffects.POISON, 3 * 20, 2));
            maybeAddEffect(entity, random, 700, new MobEffectInstance(MobEffects.WITHER, 3 * 20, 1));
            return;
        }
        if (radiation >= 600.0F) {
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0));
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2));
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2));
            maybeAddEffect(entity, random, 500, new MobEffectInstance(MobEffects.POISON, 3 * 20, 1));
            return;
        }
        if (radiation >= 400.0F) {
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0));
            maybeAddEffect(entity, random, 500, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 0));
            maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 1));
            return;
        }
        maybeAddEffect(entity, random, 300, new MobEffectInstance(MobEffects.CONFUSION, 5 * 20, 0));
        maybeAddEffect(entity, random, 500, new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 0));
    }

    private static void applyAmbientChunkRadiation(final LivingEntity entity) {
        if (entity.level().isClientSide() || RadiationUtil.isRadImmune(entity)) {
            return;
        }

        final int x = entity.blockPosition().getX();
        final int y = entity.blockPosition().getY();
        final int z = entity.blockPosition().getZ();
        final float chunkRadiation = ChunkRadiationManager.getRadiation(entity.level(), x, y, z);
        if (chunkRadiation > 0.0F) {
            RadiationUtil.contaminate(entity, RadiationUtil.HazardType.RADIATION, RadiationUtil.ContaminationType.CREATIVE, chunkRadiation / 20.0F);
        }
    }

    private static boolean handleRadiationTransformation(final LivingEntity entity, final float radiation) {
        if (!(entity.level() instanceof final ServerLevel serverLevel) || entity.isDeadOrDying()) {
            return false;
        }
        if (entity instanceof Cow && !(entity instanceof MushroomCow) && radiation >= 50.0F) {
            return replaceEntity(entity, EntityType.MOOSHROOM.create(serverLevel));
        }
        if (entity instanceof Villager && radiation >= 500.0F) {
            return replaceEntity(entity, EntityType.ZOMBIE.create(serverLevel));
        }
        return false;
    }

    private static boolean replaceEntity(final LivingEntity source, final Mob replacement) {
        if (replacement == null) {
            return false;
        }
        replacement.moveTo(source.getX(), source.getY(), source.getZ(), source.getYRot(), source.getXRot());
        source.level().addFreshEntity(replacement);
        source.discard();
        return true;
    }

    private static void maybeAddEffect(final LivingEntity entity, final RandomSource random, final int chance, final MobEffectInstance effect) {
        if (random.nextInt(chance) == 0) {
            entity.addEffect(effect);
        }
    }
}

package com.hbm.ntm.common.radiation;

import com.hbm.ntm.common.registration.HbmMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

public final class RadiationUtil {
    private static final String TAG_ROOT = "hbmntm_living";
    private static final String TAG_RADIATION = "radiation";
    private static final String TAG_RAD_ENV = "rad_env";
    private static final String TAG_RAD_BUF = "rad_buf";
    private static final String TAG_CONTAMINATION = "contamination";
    private static final float MAX_RADIATION = 2500.0F;

    private RadiationUtil() {
    }

    public static float calculateRadiationMod(final LivingEntity entity) {
        if (entity instanceof final Player player) {
            float resistance = RadiationResistanceRegistry.getResistance(player);
            if (player.hasEffect(HbmMobEffects.RAD_X.get())) {
                resistance += 0.2F;
            }
            return (float) Math.pow(10.0F, -resistance);
        }
        return 1.0F;
    }

    public static float getRadiation(final LivingEntity entity) {
        return data(entity).getFloat(TAG_RADIATION);
    }

    public static void setRadiation(final LivingEntity entity, final float radiation) {
        data(entity).putFloat(TAG_RADIATION, clampRadiation(radiation));
    }

    public static void incrementRadiation(final LivingEntity entity, final float radiation) {
        setRadiation(entity, getRadiation(entity) + radiation);
    }

    public static float getRadEnv(final LivingEntity entity) {
        return data(entity).getFloat(TAG_RAD_ENV);
    }

    public static void setRadEnv(final LivingEntity entity, final float radiation) {
        data(entity).putFloat(TAG_RAD_ENV, radiation);
    }

    public static float getRadBuf(final LivingEntity entity) {
        return data(entity).getFloat(TAG_RAD_BUF);
    }

    public static void setRadBuf(final LivingEntity entity, final float radiation) {
        data(entity).putFloat(TAG_RAD_BUF, radiation);
    }

    public static List<ContaminationEffect> getContaminationEffects(final LivingEntity entity) {
        final ListTag listTag = data(entity).getList(TAG_CONTAMINATION, Tag.TAG_COMPOUND);
        final List<ContaminationEffect> effects = new ArrayList<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            effects.add(ContaminationEffect.load(listTag.getCompound(i)));
        }
        return effects;
    }

    public static void addContaminationEffect(final LivingEntity entity, final ContaminationEffect contaminationEffect) {
        final List<ContaminationEffect> effects = getContaminationEffects(entity);
        effects.add(contaminationEffect);
        setContaminationEffects(entity, effects);
    }

    public static void tickContaminationEffects(final LivingEntity entity) {
        final List<ContaminationEffect> effects = getContaminationEffects(entity);
        if (effects.isEmpty()) {
            return;
        }
        final Iterator<ContaminationEffect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            final ContaminationEffect contaminationEffect = iterator.next();
            contaminate(entity, HazardType.RADIATION, contaminationEffect.ignoreArmor() ? ContaminationType.RAD_BYPASS : ContaminationType.CREATIVE,
                contaminationEffect.getRad());
            contaminationEffect.tick();
            if (contaminationEffect.isExpired()) {
                iterator.remove();
            }
        }
        setContaminationEffects(entity, effects);
    }

    public static boolean isRadImmune(final Entity entity) {
        return entity instanceof IRadiationImmune
            || entity instanceof MushroomCow
            || entity instanceof Zombie
            || entity instanceof AbstractSkeleton
            || entity instanceof Ocelot;
    }

    public static boolean contaminate(final LivingEntity entity, final HazardType hazard, final ContaminationType contaminationType, final float amount) {
        if (hazard == HazardType.RADIATION) {
            setRadEnv(entity, getRadEnv(entity) + amount);
        }

        if (entity instanceof final Player player) {
            if (player.isCreative() && contaminationType != ContaminationType.NONE) {
                return false;
            }
            if (player.tickCount < 200) {
                return false;
            }
        }

        if (hazard == HazardType.RADIATION && isRadImmune(entity)) {
            return false;
        }

        if (hazard == HazardType.RADIATION) {
            incrementRadiation(entity, amount * (contaminationType == ContaminationType.RAD_BYPASS ? 1.0F : calculateRadiationMod(entity)));
        }
        return true;
    }

    public static CompoundTag copyData(final LivingEntity entity) {
        return data(entity).copy();
    }

    public static void loadData(final LivingEntity entity, final CompoundTag tag) {
        entity.getPersistentData().put(TAG_ROOT, tag.copy());
    }

    private static void setContaminationEffects(final LivingEntity entity, final List<ContaminationEffect> effects) {
        final ListTag listTag = new ListTag();
        for (final ContaminationEffect contaminationEffect : effects) {
            listTag.add(contaminationEffect.save());
        }
        data(entity).put(TAG_CONTAMINATION, listTag);
    }

    private static CompoundTag data(final LivingEntity entity) {
        final CompoundTag persistentData = entity.getPersistentData();
        if (!persistentData.contains(TAG_ROOT, Tag.TAG_COMPOUND)) {
            persistentData.put(TAG_ROOT, new CompoundTag());
        }
        return persistentData.getCompound(TAG_ROOT);
    }

    private static float clampRadiation(final float radiation) {
        return Math.max(0.0F, Math.min(MAX_RADIATION, radiation));
    }

    public enum HazardType {
        RADIATION
    }

    public enum ContaminationType {
        CREATIVE,
        RAD_BYPASS,
        NONE
    }

    public static final class ContaminationEffect {
        private static final String TAG_MAX_RAD = "max_rad";
        private static final String TAG_MAX_TIME = "max_time";
        private static final String TAG_TIME = "time";
        private static final String TAG_IGNORE_ARMOR = "ignore_armor";
        private final float maxRad;
        private final int maxTime;
        private int time;
        private final boolean ignoreArmor;

        public ContaminationEffect(final float maxRad, final int maxTime, final boolean ignoreArmor) {
            this.maxRad = maxRad;
            this.maxTime = maxTime;
            this.time = maxTime;
            this.ignoreArmor = ignoreArmor;
        }

        public float getRad() {
            return maxRad * ((float) time / (float) maxTime);
        }

        public boolean ignoreArmor() {
            return ignoreArmor;
        }

        public boolean isExpired() {
            return time <= 0;
        }

        public void tick() {
            time--;
        }

        private CompoundTag save() {
            final CompoundTag tag = new CompoundTag();
            tag.putFloat(TAG_MAX_RAD, maxRad);
            tag.putInt(TAG_MAX_TIME, maxTime);
            tag.putInt(TAG_TIME, time);
            tag.putBoolean(TAG_IGNORE_ARMOR, ignoreArmor);
            return tag;
        }

        private static ContaminationEffect load(final CompoundTag tag) {
            final ContaminationEffect contaminationEffect = new ContaminationEffect(tag.getFloat(TAG_MAX_RAD), tag.getInt(TAG_MAX_TIME),
                tag.getBoolean(TAG_IGNORE_ARMOR));
            contaminationEffect.time = tag.getInt(TAG_TIME);
            return contaminationEffect;
        }
    }
}

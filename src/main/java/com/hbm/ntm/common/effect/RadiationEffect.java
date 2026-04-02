package com.hbm.ntm.common.effect;

import com.hbm.ntm.common.radiation.RadiationUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadiationEffect extends MobEffect {
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 0x84C128);
    }

    @Override
    public void applyEffectTick(final LivingEntity entity, final int amplifier) {
        RadiationUtil.contaminate(entity, RadiationUtil.HazardType.RADIATION, RadiationUtil.ContaminationType.CREATIVE, (amplifier + 1.0F) * 0.05F);
    }

    @Override
    public boolean isDurationEffectTick(final int duration, final int amplifier) {
        return true;
    }
}

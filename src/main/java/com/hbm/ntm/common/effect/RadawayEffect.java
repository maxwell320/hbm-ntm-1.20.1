package com.hbm.ntm.common.effect;

import com.hbm.ntm.common.radiation.RadiationUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadawayEffect extends MobEffect {
    public RadawayEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xBB4B00);
    }

    @Override
    public void applyEffectTick(final LivingEntity entity, final int amplifier) {
        RadiationUtil.incrementRadiation(entity, -(amplifier + 1));
    }

    @Override
    public boolean isDurationEffectTick(final int duration, final int amplifier) {
        return true;
    }
}

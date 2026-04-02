package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.effect.RadawayEffect;
import com.hbm.ntm.common.effect.RadiationEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HbmMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HbmNtmMod.MOD_ID);
    public static final RegistryObject<MobEffect> RADIATION = MOB_EFFECTS.register("radiation", RadiationEffect::new);
    public static final RegistryObject<MobEffect> RADAWAY = MOB_EFFECTS.register("radaway", RadawayEffect::new);
    public static final RegistryObject<MobEffect> RAD_X = MOB_EFFECTS.register("radx", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xBB4B00) {
    });

    private HbmMobEffects() {
    }
}

package com.hbm.ntm.common.radiation;

import net.minecraft.world.item.ItemStack;

public interface IRadiationResistanceProvider {
    float getRadiationResistance(ItemStack stack);
}

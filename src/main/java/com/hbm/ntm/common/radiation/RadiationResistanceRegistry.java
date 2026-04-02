package com.hbm.ntm.common.radiation;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class RadiationResistanceRegistry {
    private static final float HELMET_SHARE = 0.2F;
    private static final float CHEST_SHARE = 0.4F;
    private static final float LEGS_SHARE = 0.3F;
    private static final float BOOTS_SHARE = 0.1F;
    private static final float IRON_OR_GOLD_COEFFICIENT = 0.0225F;
    private static final Map<Item, Float> ITEM_RESISTANCE = new IdentityHashMap<>();

    static {
        registerDefaults();
    }

    private RadiationResistanceRegistry() {
    }

    public static void register(final Item item, final float resistance) {
        ITEM_RESISTANCE.put(item, resistance);
    }

    public static float getResistance(final ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0F;
        }
        if (stack.getItem() instanceof final IRadiationResistanceProvider provider) {
            return provider.getRadiationResistance(stack);
        }
        return ITEM_RESISTANCE.getOrDefault(stack.getItem(), 0.0F);
    }

    public static float getResistance(final LivingEntity entity) {
        float resistance = 0.0F;
        for (final ItemStack stack : entity.getArmorSlots()) {
            resistance += getResistance(stack);
        }
        return resistance;
    }

    private static void registerDefaults() {
        register(Items.IRON_HELMET, IRON_OR_GOLD_COEFFICIENT * HELMET_SHARE);
        register(Items.IRON_CHESTPLATE, IRON_OR_GOLD_COEFFICIENT * CHEST_SHARE);
        register(Items.IRON_LEGGINGS, IRON_OR_GOLD_COEFFICIENT * LEGS_SHARE);
        register(Items.IRON_BOOTS, IRON_OR_GOLD_COEFFICIENT * BOOTS_SHARE);
        register(Items.GOLDEN_HELMET, IRON_OR_GOLD_COEFFICIENT * HELMET_SHARE);
        register(Items.GOLDEN_CHESTPLATE, IRON_OR_GOLD_COEFFICIENT * CHEST_SHARE);
        register(Items.GOLDEN_LEGGINGS, IRON_OR_GOLD_COEFFICIENT * LEGS_SHARE);
        register(Items.GOLDEN_BOOTS, IRON_OR_GOLD_COEFFICIENT * BOOTS_SHARE);
    }
}

package com.hbm.ntm.common.item;

import net.minecraft.world.item.Item;

/**
 * Shredder blade item — direct port of legacy {@code ItemBlades}.
 * <p>
 * Legacy durabilities (from {@code ModItems}):
 * <ul>
 *   <li>{@code blades_steel} — 200</li>
 *   <li>{@code blades_titanium} — 350</li>
 *   <li>{@code blades_advanced_alloy} — 700</li>
 *   <li>{@code blades_desh} — 0 (infinite)</li>
 * </ul>
 * A durability of 0 means the blades never break (legacy: {@code maxDamage == 0}).
 */
public class ShredderBladesItem extends Item {

    public ShredderBladesItem(final int durability) {
        super(new Item.Properties().stacksTo(1).durability(durability));
    }
}

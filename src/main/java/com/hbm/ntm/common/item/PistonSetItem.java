package com.hbm.ntm.common.item;

import com.hbm.ntm.common.fluid.CombustibleFuelRegistry.FuelGrade;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PistonSetItem extends Item {
    public enum Tier {
        STEEL(0.5D, 0.75D, 1.0D, 1.5D, 0.01D),
        DURA(0.25D, 0.5D, 0.75D, 1.0D, 0.1D),
        DESH(0.1D, 0.25D, 0.5D, 0.75D, 0.35D),
        STARMETAL(0.1D, 0.15D, 0.25D, 0.5D, 0.5D);

        private final double low;
        private final double medium;
        private final double high;
        private final double aero;
        private final double gas;

        Tier(final double low, final double medium, final double high, final double aero, final double gas) {
            this.low = low;
            this.medium = medium;
            this.high = high;
            this.aero = aero;
            this.gas = gas;
        }

        public double efficiency(final FuelGrade grade) {
            return switch (grade) {
                case LOW -> this.low;
                case MEDIUM -> this.medium;
                case HIGH -> this.high;
                case AERO -> this.aero;
                case GAS -> this.gas;
            };
        }
    }

    private final Tier tier;

    public PistonSetItem(final Tier tier, final Properties properties) {
        super(properties.stacksTo(1));
        this.tier = tier;
    }

    public Tier getTier() {
        return this.tier;
    }

    @Override
    public void appendHoverText(final ItemStack stack, final @Nullable Level level, final List<Component> tooltip, final TooltipFlag flag) {
        tooltip.add(Component.literal("Combustion piston tier: " + this.tier.name().toLowerCase()));
    }
}

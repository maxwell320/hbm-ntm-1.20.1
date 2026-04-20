package com.hbm.ntm.common.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@SuppressWarnings("null")
public class ModuleBurnTime {
    public static final int MOD_LOG = 0;
    public static final int MOD_WOOD = 1;
    public static final int MOD_COAL = 2;
    public static final int MOD_LIGNITE = 3;
    public static final int MOD_COKE = 4;
    public static final int MOD_SOLID = 5;
    public static final int MOD_ROCKET = 6;
    public static final int MOD_BALEFIRE = 7;

    private static final int CATEGORY_COUNT = 8;

    private final double[] modTime = new double[CATEGORY_COUNT];
    private final double[] modHeat = new double[CATEGORY_COUNT];

    public ModuleBurnTime() {
        for (int i = 0; i < CATEGORY_COUNT; i++) {
            this.modTime[i] = 1.0D;
            this.modHeat[i] = 1.0D;
        }
    }

    public double[] getModTime() {
        return this.modTime;
    }

    public double[] getModHeat() {
        return this.modHeat;
    }

    public List<Component> getDesc() {
        final List<Component> desc = new ArrayList<>();
        desc.addAll(this.getTimeDesc());
        desc.addAll(this.getHeatDesc());
        return desc;
    }

    public List<Component> getTimeDesc() {
        final List<Component> list = new ArrayList<>();
        list.add(Component.literal("Burn time bonuses:").withStyle(ChatFormatting.GOLD));
        addIf(list, "Logs", this.modTime[MOD_LOG]);
        addIf(list, "Wood", this.modTime[MOD_WOOD]);
        addIf(list, "Coal", this.modTime[MOD_COAL]);
        addIf(list, "Lignite", this.modTime[MOD_LIGNITE]);
        addIf(list, "Coke", this.modTime[MOD_COKE]);
        addIf(list, "Solid Fuel", this.modTime[MOD_SOLID]);
        addIf(list, "Rocket Fuel", this.modTime[MOD_ROCKET]);
        addIf(list, "Balefire", this.modTime[MOD_BALEFIRE]);
        if (list.size() == 1) {
            list.clear();
        }
        return list;
    }

    public List<Component> getHeatDesc() {
        final List<Component> list = new ArrayList<>();
        list.add(Component.literal("Burn heat bonuses:").withStyle(ChatFormatting.RED));
        addIf(list, "Logs", this.modHeat[MOD_LOG]);
        addIf(list, "Wood", this.modHeat[MOD_WOOD]);
        addIf(list, "Coal", this.modHeat[MOD_COAL]);
        addIf(list, "Lignite", this.modHeat[MOD_LIGNITE]);
        addIf(list, "Coke", this.modHeat[MOD_COKE]);
        addIf(list, "Solid Fuel", this.modHeat[MOD_SOLID]);
        addIf(list, "Rocket Fuel", this.modHeat[MOD_ROCKET]);
        addIf(list, "Balefire", this.modHeat[MOD_BALEFIRE]);
        if (list.size() == 1) {
            list.clear();
        }
        return list;
    }

    private static void addIf(final List<Component> list, final String name, final double mod) {
        if (mod != 1.0D) {
            final MutableComponent line = Component.literal("- " + name + ": ").withStyle(ChatFormatting.YELLOW);
            line.append(formatPercent(mod));
            list.add(line);
        }
    }

    private static Component formatPercent(final double mod) {
        final double diff = mod - 1.0D;
        final int pct = (int) (diff * 100);
        if (diff < 0.0D) {
            return Component.literal(pct + "%").withStyle(ChatFormatting.RED);
        }
        return Component.literal("+" + pct + "%").withStyle(ChatFormatting.GREEN);
    }

    public ModuleBurnTime setLogTimeMod(final double mod) { this.modTime[MOD_LOG] = mod; return this; }
    public ModuleBurnTime setWoodTimeMod(final double mod) { this.modTime[MOD_WOOD] = mod; return this; }
    public ModuleBurnTime setCoalTimeMod(final double mod) { this.modTime[MOD_COAL] = mod; return this; }
    public ModuleBurnTime setLigniteTimeMod(final double mod) { this.modTime[MOD_LIGNITE] = mod; return this; }
    public ModuleBurnTime setCokeTimeMod(final double mod) { this.modTime[MOD_COKE] = mod; return this; }
    public ModuleBurnTime setSolidTimeMod(final double mod) { this.modTime[MOD_SOLID] = mod; return this; }
    public ModuleBurnTime setRocketTimeMod(final double mod) { this.modTime[MOD_ROCKET] = mod; return this; }
    public ModuleBurnTime setBalefireTimeMod(final double mod) { this.modTime[MOD_BALEFIRE] = mod; return this; }

    public ModuleBurnTime setLogHeatMod(final double mod) { this.modHeat[MOD_LOG] = mod; return this; }
    public ModuleBurnTime setWoodHeatMod(final double mod) { this.modHeat[MOD_WOOD] = mod; return this; }
    public ModuleBurnTime setCoalHeatMod(final double mod) { this.modHeat[MOD_COAL] = mod; return this; }
    public ModuleBurnTime setLigniteHeatMod(final double mod) { this.modHeat[MOD_LIGNITE] = mod; return this; }
    public ModuleBurnTime setCokeHeatMod(final double mod) { this.modHeat[MOD_COKE] = mod; return this; }
    public ModuleBurnTime setSolidHeatMod(final double mod) { this.modHeat[MOD_SOLID] = mod; return this; }
    public ModuleBurnTime setRocketHeatMod(final double mod) { this.modHeat[MOD_ROCKET] = mod; return this; }
    public ModuleBurnTime setBalefireHeatMod(final double mod) { this.modHeat[MOD_BALEFIRE] = mod; return this; }
}

package com.hbm.ntm.client.sync;

import net.minecraft.nbt.CompoundTag;

public final class PermaSyncClientState {
    private static float chunkRadiation;
    private static float pollutionSoot;
    private static float pollutionHeavyMetal;
    private static float pollutionPoison;
    private static float tomDust;
    private static float tomFire;
    private static boolean tomImpact;
    private static long updateCounter;

    private PermaSyncClientState() {
    }

    public static void apply(final CompoundTag data) {
        if (data == null) {
            return;
        }
        chunkRadiation = data.getFloat("chunkRadiation");
        pollutionSoot = data.getFloat("pollutionSoot");
        pollutionHeavyMetal = data.getFloat("pollutionHeavyMetal");
        pollutionPoison = data.getFloat("pollutionPoison");
        tomDust = data.getFloat("tomDust");
        tomFire = data.getFloat("tomFire");
        tomImpact = data.getBoolean("tomImpact");
        updateCounter++;
    }

    public static float chunkRadiation() {
        return chunkRadiation;
    }

    public static float pollutionSoot() {
        return pollutionSoot;
    }

    public static float pollutionHeavyMetal() {
        return pollutionHeavyMetal;
    }

    public static float pollutionPoison() {
        return pollutionPoison;
    }

    public static float tomDust() {
        return tomDust;
    }

    public static float tomFire() {
        return tomFire;
    }

    public static boolean tomImpact() {
        return tomImpact;
    }

    public static long updateCounter() {
        return updateCounter;
    }
}

package com.hbm.ntm.client.debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HbmDebugHitboxTracker {

    public record Hitbox(String label, int x, int y, int width, int height, int color) {
    }

    public static final int COLOR_FLUID = 0xFF4FA3FF;
    public static final int COLOR_ENERGY = 0xFFFFD84F;
    public static final int COLOR_INFO_PANEL = 0xFFB04FFF;
    public static final int COLOR_UPGRADE = 0xFF4FFFA3;
    public static final int COLOR_BURN_BONUS = 0xFFFF8040;
    public static final int COLOR_CLICK = 0xFFFF4F4F;
    public static final int COLOR_GENERIC = 0xFFFFFFFF;

    private static final List<Hitbox> CURRENT = new ArrayList<>();

    private HbmDebugHitboxTracker() {
    }

    public static void beginFrame() {
        CURRENT.clear();
    }

    public static void record(final String label, final int x, final int y, final int width, final int height, final int color) {
        if (!HbmDebugOverlay.isEnabled(HbmDebugOverlay.OverlayMode.HITBOX)) {
            return;
        }
        CURRENT.add(new Hitbox(label, x, y, width, height, color));
    }

    public static void record(final String label, final int x, final int y, final int width, final int height) {
        record(label, x, y, width, height, COLOR_GENERIC);
    }

    public static List<Hitbox> snapshot() {
        if (CURRENT.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(CURRENT);
    }
}

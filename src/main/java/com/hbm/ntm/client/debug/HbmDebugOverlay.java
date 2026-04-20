package com.hbm.ntm.client.debug;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

public final class HbmDebugOverlay {

    public enum OverlayMode {
        HITBOX,
        SLOTS,
        GRID,
        COORDS,
        LABELS;

        public String key() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public static OverlayMode fromKey(final String key) {
            if (key == null) {
                return null;
            }
            final String normalized = key.toLowerCase(Locale.ROOT);
            for (final OverlayMode mode : values()) {
                if (mode.key().equals(normalized)) {
                    return mode;
                }
            }
            return null;
        }
    }

    private static final Set<OverlayMode> ENABLED = EnumSet.noneOf(OverlayMode.class);
    private static volatile boolean probeRequested;

    private HbmDebugOverlay() {
    }

    public static void enable(final OverlayMode mode) {
        if (mode != null) {
            ENABLED.add(mode);
        }
    }

    public static void disable(final OverlayMode mode) {
        if (mode != null) {
            ENABLED.remove(mode);
        }
    }

    public static void enableAll() {
        for (final OverlayMode mode : OverlayMode.values()) {
            ENABLED.add(mode);
        }
    }

    public static void disableAll() {
        ENABLED.clear();
    }

    public static boolean isEnabled(final OverlayMode mode) {
        return ENABLED.contains(mode);
    }

    public static boolean anyEnabled() {
        return !ENABLED.isEmpty();
    }

    public static Set<OverlayMode> snapshot() {
        return EnumSet.copyOf(ENABLED.isEmpty() ? EnumSet.noneOf(OverlayMode.class) : ENABLED);
    }

    public static void requestProbe() {
        probeRequested = true;
    }

    public static boolean consumeProbeRequest() {
        if (probeRequested) {
            probeRequested = false;
            return true;
        }
        return false;
    }
}

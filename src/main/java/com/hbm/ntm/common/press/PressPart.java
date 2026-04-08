package com.hbm.ntm.common.press;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PressPart implements StringRepresentable {
    CORE("core"),
    MIDDLE("middle"),
    TOP("top");

    private final String name;

    PressPart(final String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}

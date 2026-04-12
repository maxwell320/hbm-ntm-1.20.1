package com.hbm.ntm.common.block;

import net.minecraft.util.StringRepresentable;

public enum IcfLaserComponentPart implements StringRepresentable {
    CASING("casing", false),
    PORT("port", false),
    CELL("cell", true),
    EMITTER("emitter", true),
    CAPACITOR("capacitor", true),
    TURBO("turbo", true);

    private final String name;
    private final boolean propagatesAssembly;

    IcfLaserComponentPart(final String name, final boolean propagatesAssembly) {
        this.name = name;
        this.propagatesAssembly = propagatesAssembly;
    }

    public boolean propagatesAssembly() {
        return this.propagatesAssembly;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
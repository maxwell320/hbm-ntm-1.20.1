package com.hbm.ntm.common.material;

public enum HbmMaterialShape {
    INGOT("ingot", "Ingot"),
    NUGGET("nugget", "Nugget"),
    DUST("dust", "Dust"),
    PLATE("plate", "Plate"),
    BILLET("billet", "Billet");

    private final String registryPrefix;
    private final String displaySuffix;

    HbmMaterialShape(final String registryPrefix, final String displaySuffix) {
        this.registryPrefix = registryPrefix;
        this.displaySuffix = displaySuffix;
    }

    public String registryPrefix() {
        return this.registryPrefix;
    }

    public String displaySuffix() {
        return this.displaySuffix;
    }
}

package com.hbm.ntm.common.block;

public enum BasaltOreType {
    SULFUR("ore_basalt_sulfur", "Sulfur-Rich Basalt"),
    FLUORITE("ore_basalt_fluorite", "Fluorite-Rich Basalt"),
    ASBESTOS("ore_basalt_asbestos", "Asbestos-Rich Basalt"),
    GEM("ore_basalt_gem", "Gem-Rich Basalt"),
    MOLYSITE("ore_basalt_molysite", "Molysite-Rich Basalt");

    private final String blockId;
    private final String displayName;

    BasaltOreType(final String blockId, final String displayName) {
        this.blockId = blockId;
        this.displayName = displayName;
    }

    public String blockId() {
        return this.blockId;
    }

    public String displayName() {
        return this.displayName;
    }
}

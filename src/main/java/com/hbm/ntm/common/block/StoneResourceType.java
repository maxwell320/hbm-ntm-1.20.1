package com.hbm.ntm.common.block;

public enum StoneResourceType {
    SULFUR("stone_resource_sulfur", "Sulfurous Stone"),
    ASBESTOS("stone_resource_asbestos", "Chrysotile"),
    LIMESTONE("stone_resource_limestone", "Limestone"),
    BAUXITE("stone_resource_bauxite", "Bauxite"),
    HEMATITE("stone_resource_hematite", "Hematite"),
    MALACHITE("stone_resource_malachite", "Malachite");

    private final String blockId;
    private final String displayName;

    StoneResourceType(final String blockId, final String displayName) {
        this.blockId = blockId;
        this.displayName = displayName;
    }

    public String blockId() {
        return this.blockId;
    }

    public String displayName() {
        return this.displayName;
    }

    public String texturePath() {
        return "block/" + this.blockId;
    }
}

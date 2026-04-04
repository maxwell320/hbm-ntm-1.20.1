package com.hbm.ntm.common.item;

public enum CasingItemType {
    SMALL("casing_small", "Small Gunmetal Casing", "item/casing_small"),
    LARGE("casing_large", "Large Gunmetal Casing", "item/casing_large"),
    SMALL_STEEL("casing_small_steel", "Small Weapon Steel Casing", "item/casing_small_steel"),
    LARGE_STEEL("casing_large_steel", "Large Weapon Steel Casing", "item/casing_large_steel");

    private final String itemId;
    private final String displayName;
    private final String defaultTexturePath;

    CasingItemType(final String itemId, final String displayName, final String defaultTexturePath) {
        this.itemId = itemId;
        this.displayName = displayName;
        this.defaultTexturePath = defaultTexturePath;
    }

    public String itemId() {
        return this.itemId;
    }

    public String displayName() {
        return this.displayName;
    }

    public String defaultTexturePath() {
        return this.defaultTexturePath;
    }
}

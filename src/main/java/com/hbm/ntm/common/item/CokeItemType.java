package com.hbm.ntm.common.item;

public enum CokeItemType {
    COAL("coke_coal", "Coal Coke", "item/coke_coal"),
    LIGNITE("coke_lignite", "Lignite Coke", "item/coke_lignite"),
    PETROLEUM("coke_petroleum", "Petroleum Coke", "item/coke_petroleum");

    private final String itemId;
    private final String displayName;
    private final String defaultTexturePath;

    CokeItemType(final String itemId, final String displayName, final String defaultTexturePath) {
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

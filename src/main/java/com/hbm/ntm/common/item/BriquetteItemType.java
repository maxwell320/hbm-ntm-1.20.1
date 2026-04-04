package com.hbm.ntm.common.item;

public enum BriquetteItemType {
    COAL("briquette_coal", "Coal Briquette", "item/briquette_coal"),
    LIGNITE("briquette_lignite", "Lignite Briquette", "item/briquette_lignite"),
    WOOD("briquette_wood", "Sawdust Briquette", "item/briquette_wood");

    private final String itemId;
    private final String displayName;
    private final String defaultTexturePath;

    BriquetteItemType(final String itemId, final String displayName, final String defaultTexturePath) {
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

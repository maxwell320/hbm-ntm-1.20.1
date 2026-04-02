package com.hbm.ntm.common.item;

public enum ChunkOreItemType {
    RARE("chunk_ore_rare", "Rare Earth Ore Chunk", "item/chunk_ore_rare"),
    MALACHITE("chunk_ore_malachite", "Malachite Chunk", "item/chunk_ore_malachite"),
    CRYOLITE("chunk_ore_cryolite", "Cryolite Chunk", "item/chunk_ore_cryolite"),
    MOONSTONE("chunk_ore_moonstone", "Moonstone", "item/chunk_ore_moonstone");

    private final String itemId;
    private final String displayName;
    private final String defaultTexturePath;

    ChunkOreItemType(final String itemId, final String displayName, final String defaultTexturePath) {
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

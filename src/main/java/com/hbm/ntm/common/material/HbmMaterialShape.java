package com.hbm.ntm.common.material;

public enum HbmMaterialShape {
    FRAGMENT("bedrockorefragment", "Fragment", "fragments", "item/prismarine_shard", "%s Bedrock Ore Fragment"),
    INGOT("ingot", "Ingot", "ingots", "item/iron_ingot"),
    NUGGET("nugget", "Nugget", "nuggets", "item/gold_nugget"),
    GEM("gem", "Gem", "gems", "item/emerald"),
    CRYSTAL("crystal", "Crystal", "crystals", "item/amethyst_shard", "%s Crystals"),
    DUST_TINY("dust_tiny", "Tiny Dust", "tiny_dusts", "item/sugar", "Tiny Pile of %s Powder"),
    ORE_DUST("dust_ore", "Ore Dust", "ore_dusts", "item/gunpowder", "Crushed %s"),
    DUST("dust", "Dust", "dusts", "item/gunpowder", "%s Powder"),
    WIRE("wire", "Fine Wire", "wires", "item/string", "%s Wire"),
    DENSE_WIRE("wire_dense", "Dense Wire", "dense_wires", "item/redstone", "Dense %s Wire"),
    BOLT("bolt", "Bolt", "bolts", "item/iron_nugget"),
    PLATE("plate", "Plate", "plates", "item/paper"),
    CAST_PLATE("plate_cast", "Cast Plate", "cast_plates", "item/map", "Cast %s Plate"),
    WELDED_PLATE("plate_welded", "Welded Plate", "welded_plates", "item/book", "Welded %s Plate"),
    SHELL("shell", "Shell", "shells", "item/rabbit_hide"),
    PIPE("pipentm", "Pipe", "pipes", "item/copper_ingot"),
    BILLET("billet", "Billet", "billets", "item/brick"),
    LIGHT_BARREL("barrel_light", "Light Barrel", "light_barrels", "item/blaze_rod", "Light %s Barrel"),
    HEAVY_BARREL("barrel_heavy", "Heavy Barrel", "heavy_barrels", "item/blaze_powder", "Heavy %s Barrel"),
    LIGHT_RECEIVER("receiver_light", "Light Receiver", "light_receivers", "item/flint", "Light %s Receiver"),
    HEAVY_RECEIVER("receiver_heavy", "Heavy Receiver", "heavy_receivers", "item/netherite_scrap", "Heavy %s Receiver"),
    MECHANISM("gun_mechanism", "Gun Mechanism", "mechanisms", "item/iron_nugget"),
    STOCK("stock", "Stock", "stocks", "item/stick"),
    GRIP("grip", "Grip", "grips", "item/leather");

    private final String registryPrefix;
    private final String displaySuffix;
    private final String tagFolder;
    private final String defaultTexturePath;
    private final String translationFormat;

    HbmMaterialShape(final String registryPrefix, final String displaySuffix, final String tagFolder, final String defaultTexturePath) {
        this(registryPrefix, displaySuffix, tagFolder, defaultTexturePath, "%s " + displaySuffix);
    }

    HbmMaterialShape(final String registryPrefix, final String displaySuffix, final String tagFolder, final String defaultTexturePath, final String translationFormat) {
        this.registryPrefix = registryPrefix;
        this.displaySuffix = displaySuffix;
        this.tagFolder = tagFolder;
        this.defaultTexturePath = defaultTexturePath;
        this.translationFormat = translationFormat;
    }

    public String registryPrefix() {
        return this.registryPrefix;
    }

    public String displaySuffix() {
        return this.displaySuffix;
    }

    public String formatDisplayName(final String materialDisplayName) {
        return this.translationFormat.formatted(materialDisplayName);
    }

    public String tagFolder() {
        return this.tagFolder;
    }

    public String defaultTexturePath() {
        return this.defaultTexturePath;
    }
}

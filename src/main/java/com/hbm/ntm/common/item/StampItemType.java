package com.hbm.ntm.common.item;

public enum StampItemType {
    STONE_FLAT("stamp_stone_flat", "Flat Stamp (Stone)", "item/stamp_stone_flat", 32),
    STONE_PLATE("stamp_stone_plate", "Plate Stamp (Stone)", "item/stamp_stone_plate", 32),
    STONE_WIRE("stamp_stone_wire", "Wire Stamp (Stone)", "item/stamp_stone_wire", 32),
    STONE_CIRCUIT("stamp_stone_circuit", "Circuit Stamp (Stone)", "item/stamp_stone_circuit", 32),
    IRON_FLAT("stamp_iron_flat", "Flat Stamp (Iron)", "item/stamp_iron_flat", 64),
    IRON_PLATE("stamp_iron_plate", "Plate Stamp (Iron)", "item/stamp_iron_plate", 64),
    IRON_WIRE("stamp_iron_wire", "Wire Stamp (Iron)", "item/stamp_iron_wire", 64),
    IRON_CIRCUIT("stamp_iron_circuit", "Circuit Stamp (Iron)", "item/stamp_iron_circuit", 64),
    IRON_C9("stamp_9", "Small Caliber Stamp", "item/stamp_9", 1000),
    IRON_C50("stamp_50", "Large Caliber Stamp", "item/stamp_50", 1000),
    STEEL_FLAT("stamp_steel_flat", "Flat Stamp (Steel)", "item/stamp_steel_flat", 192),
    STEEL_PLATE("stamp_steel_plate", "Plate Stamp (Steel)", "item/stamp_steel_plate", 192),
    STEEL_WIRE("stamp_steel_wire", "Wire Stamp (Steel)", "item/stamp_steel_wire", 192),
    STEEL_CIRCUIT("stamp_steel_circuit", "Circuit Stamp (Steel)", "item/stamp_steel_circuit", 192),
    TITANIUM_FLAT("stamp_titanium_flat", "Flat Stamp (Titanium)", "item/stamp_titanium_flat", 256),
    TITANIUM_PLATE("stamp_titanium_plate", "Plate Stamp (Titanium)", "item/stamp_titanium_plate", 256),
    TITANIUM_WIRE("stamp_titanium_wire", "Wire Stamp (Titanium)", "item/stamp_titanium_wire", 256),
    TITANIUM_CIRCUIT("stamp_titanium_circuit", "Circuit Stamp (Titanium)", "item/stamp_titanium_circuit", 256),
    OBSIDIAN_FLAT("stamp_obsidian_flat", "Flat Stamp (Obsidian)", "item/stamp_obsidian_flat", 512),
    OBSIDIAN_PLATE("stamp_obsidian_plate", "Plate Stamp (Obsidian)", "item/stamp_obsidian_plate", 512),
    OBSIDIAN_WIRE("stamp_obsidian_wire", "Wire Stamp (Obsidian)", "item/stamp_obsidian_wire", 512),
    OBSIDIAN_CIRCUIT("stamp_obsidian_circuit", "Circuit Stamp (Obsidian)", "item/stamp_obsidian_circuit", 512),
    DESH_FLAT("stamp_desh_flat", "Flat Stamp (Desh)", "item/stamp_desh_flat", 0),
    DESH_PLATE("stamp_desh_plate", "Plate Stamp (Desh)", "item/stamp_desh_plate", 0),
    DESH_WIRE("stamp_desh_wire", "Wire Stamp (Desh)", "item/stamp_desh_wire", 0),
    DESH_CIRCUIT("stamp_desh_circuit", "Circuit Stamp (Desh)", "item/stamp_desh_circuit", 0),
    DESH_C9("stamp_desh_9", "Small Caliber Stamp (Desh)", "item/stamp_9_desh", 0),
    DESH_C50("stamp_desh_50", "Large Caliber Stamp (Desh)", "item/stamp_50_desh", 0);

    private final String itemId;
    private final String displayName;
    private final String defaultTexturePath;
    private final int durability;

    StampItemType(final String itemId, final String displayName, final String defaultTexturePath, final int durability) {
        this.itemId = itemId;
        this.displayName = displayName;
        this.defaultTexturePath = defaultTexturePath;
        this.durability = durability;
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

    public int durability() {
        return this.durability;
    }
}

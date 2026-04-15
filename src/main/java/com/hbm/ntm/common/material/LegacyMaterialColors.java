package com.hbm.ntm.common.material;

import java.util.Map;

public final class LegacyMaterialColors {
    private static final Map<String, Integer> SHARED_PART_TINTS = Map.ofEntries(
        Map.entry("actinium", 0x958989),
        Map.entry("iron", 0xFFA259),
        Map.entry("gold", 0xE8D754),
        Map.entry("uranium", 0x9AA196),
        Map.entry("u238", 0x9AA196),
        Map.entry("th232", 0xBF825F),
        Map.entry("polonium", 0x715E4A),
        Map.entry("technetium", 0xCADFDF),
        Map.entry("ra226", 0xE9FAF6),
        Map.entry("titanium", 0xA99E79),
        Map.entry("copper", 0xC18336),
        Map.entry("aluminium", 0xD0B8EB),
        Map.entry("tungsten", 0x977474),
        Map.entry("tantalium", 0xA89B74),
        Map.entry("zirconium", 0xADA688),
        Map.entry("niobium", 0xD576B1),
        Map.entry("neodymium", 0x8F8F5F),
        Map.entry("beryllium", 0xAE9572),
        Map.entry("emerald", 0x17DD62),
        Map.entry("steel", 0x4A4A4A),
        Map.entry("red_copper", 0xE44C0F),
        Map.entry("advanced_alloy", 0xFF7318),
        Map.entry("tcalloy", 0x9CA6A6),
        Map.entry("cdalloy", 0xFBD368),
        Map.entry("bismuth_bronze", 0x987D65),
        Map.entry("arsenic_bronze", 0x77644D),
        Map.entry("bscco", 0x5E62C0),
        Map.entry("magnetized_tungsten", 0x22A2A2),
        Map.entry("combine_steel", 0x6F6FB4),
        Map.entry("starmetal", 0xA5A5D3),
        Map.entry("gunmetal", 0xF9C62C),
        Map.entry("weaponsteel", 0x808080),
        Map.entry("saturnite", 0x30A4B7),
        Map.entry("ferrouranium", 0x6B6B8B),
        Map.entry("schrabidium", 0x32FFFF),
        Map.entry("pu241", 0x78817E),
        Map.entry("schrabidate", 0x6589B4),
        Map.entry("dineutronium", 0x455289),
        Map.entry("dura_steel", 0x42665C),
        Map.entry("desh", 0xF22929),
        Map.entry("sodalite", 0x96A7E6),
        Map.entry("lithium", 0xD6D6D6),
        Map.entry("sulfur", 0xF1DF68),
        Map.entry("nickel", 0xB3A69B),
        Map.entry("kno", 0xC9C9C9),
        Map.entry("fluorite", 0xE1DBD4),
        Map.entry("red_phosphorus", 0xBA0615),
        Map.entry("chlorocalcite", 0xB8B963),
        Map.entry("molysite", 0xD0D264),
        Map.entry("cinnabar", 0xBF4E4E),
        Map.entry("gh336", 0xC6C6A1),
        Map.entry("am241", 0x93767B),
        Map.entry("am242", 0x93767B),
        Map.entry("am_mix", 0x93767B),
        Map.entry("lead", 0x646470),
        Map.entry("polymer", 0x272727),
        Map.entry("bakelite", 0xC93940),
        Map.entry("rubber", 0x4B4A3F),
        Map.entry("pc", 0xE1DBB8),
        Map.entry("pvc", 0xF0F0F0)
    );

    private LegacyMaterialColors() {
    }

    public static int sharedPartTint(final HbmMaterialDefinition material) {
        return SHARED_PART_TINTS.getOrDefault(material.id(), 0xFFFFFF);
    }
}

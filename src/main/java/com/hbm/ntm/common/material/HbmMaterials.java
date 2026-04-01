package com.hbm.ntm.common.material;

import java.util.List;

public final class HbmMaterials {
    public static final HbmMaterialDefinition CARBON = HbmMaterialDefinition.of("carbon", "Carbon", HbmMaterialShape.WIRE)
        .withItemId(HbmMaterialShape.WIRE, "wire_carbon");
    public static final HbmMaterialDefinition COAL = HbmMaterialDefinition.of("coal", "Coal", HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_coal_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_coal")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_coal");
    public static final HbmMaterialDefinition LIGNITE = HbmMaterialDefinition.of("lignite", "Lignite", HbmMaterialShape.GEM, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.GEM, "lignite")
        .withItemDisplayName(HbmMaterialShape.GEM, "Lignite")
        .withItemId(HbmMaterialShape.DUST, "powder_lignite");
    public static final HbmMaterialDefinition GRAPHITE = HbmMaterialDefinition.of("graphite", "Graphite", HbmMaterialShape.INGOT);
    public static final HbmMaterialDefinition DIAMOND = HbmMaterialDefinition.of("diamond", "Diamond", HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "powder_diamond")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_diamond");
    public static final HbmMaterialDefinition FIREBRICK = HbmMaterialDefinition.of("firebrick", "Firebrick", HbmMaterialShape.INGOT)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Firebrick");
    public static final HbmMaterialDefinition IRON = HbmMaterialDefinition.of("iron", "Iron", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.PLATE, HbmMaterialShape.PIPE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE)
        .withItemId(HbmMaterialShape.DUST, "powder_iron")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_iron");
    public static final HbmMaterialDefinition GOLD = HbmMaterialDefinition.of("gold", "Gold", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.PLATE, HbmMaterialShape.WIRE, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.CAST_PLATE)
        .withItemId(HbmMaterialShape.WIRE, "wire_gold")
        .withItemDisplayName(HbmMaterialShape.WIRE, "Gold Wire")
        .withItemId(HbmMaterialShape.DENSE_WIRE, "coil_gold")
        .withItemDisplayName(HbmMaterialShape.DENSE_WIRE, "Gold Coil")
        .withItemId(HbmMaterialShape.DUST, "powder_gold")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_gold");
    public static final HbmMaterialDefinition REDSTONE = HbmMaterialDefinition.of("redstone", "Redstone", HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_redstone");
    public static final HbmMaterialDefinition LAPIS = HbmMaterialDefinition.of("lapis", "Lapis", HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "powder_lapis")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_lapis");
    public static final HbmMaterialDefinition QUARTZ = HbmMaterialDefinition.of("quartz", "Quartz", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_quartz");
    public static final HbmMaterialDefinition LITHIUM = HbmMaterialDefinition.of("lithium", "Lithium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.INGOT, "lithium")
        .withItemDisplayName(HbmMaterialShape.INGOT, "Lithium Cube")
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_lithium_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_lithium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_lithium");
    public static final HbmMaterialDefinition CALCIUM = HbmMaterialDefinition.of("calcium", "Calcium", HbmMaterialShape.INGOT, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_calcium");
    public static final HbmMaterialDefinition CADMIUM = HbmMaterialDefinition.of("cadmium", "Cadmium", HbmMaterialShape.INGOT, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_cadmium");
    public static final HbmMaterialDefinition URANIUM = HbmMaterialDefinition.of("uranium", "Uranium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "powder_uranium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_uranium");
    public static final HbmMaterialDefinition AUSTRALIUM = HbmMaterialDefinition.of("australium", "Australium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_australium");
    public static final HbmMaterialDefinition U233 = HbmMaterialDefinition.of("u233", "U-233", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Uranium-233 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Uranium-233 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Uranium-233 Billet");
    public static final HbmMaterialDefinition U235 = HbmMaterialDefinition.of("u235", "U-235", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Uranium-235 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Uranium-235 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Uranium-235 Billet");
    public static final HbmMaterialDefinition U238 = HbmMaterialDefinition.of("u238", "U-238", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Uranium-238 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Uranium-238 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Uranium-238 Billet");
    public static final HbmMaterialDefinition TH232 = HbmMaterialDefinition.of("th232", "Thorium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Thorium-232 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Thorium-232 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Thorium-232 Billet")
        .withItemId(HbmMaterialShape.DUST, "powder_thorium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_thorium");
    public static final HbmMaterialDefinition PLUTONIUM = HbmMaterialDefinition.of("plutonium", "Plutonium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "powder_plutonium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_plutonium");
    public static final HbmMaterialDefinition PU238 = HbmMaterialDefinition.of("pu238", "Pu-238", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Plutonium-238 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Plutonium-238 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Plutonium-238 Billet");
    public static final HbmMaterialDefinition PU239 = HbmMaterialDefinition.of("pu239", "Pu-239", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Plutonium-239 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Plutonium-239 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Plutonium-239 Billet");
    public static final HbmMaterialDefinition PU240 = HbmMaterialDefinition.of("pu240", "Pu-240", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Plutonium-240 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Plutonium-240 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Plutonium-240 Billet");
    public static final HbmMaterialDefinition NEPTUNIUM = HbmMaterialDefinition.of("neptunium", "Neptunium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_neptunium");
    public static final HbmMaterialDefinition POLONIUM = HbmMaterialDefinition.of("polonium", "Polonium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Polonium-210 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Polonium-210 Nugget")
        .withItemId(HbmMaterialShape.DUST, "powder_polonium");
    public static final HbmMaterialDefinition TECHNETIUM = HbmMaterialDefinition.of("technetium", "Technetium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Technetium-99 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Technetium-99 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Technetium-99 Billet");
    public static final HbmMaterialDefinition RA226 = HbmMaterialDefinition.of("ra226", "Radium-226", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Radium-226 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Radium-226 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Radium-226 Billet")
        .withItemId(HbmMaterialShape.DUST, "powder_ra226");
    public static final HbmMaterialDefinition ACTINIUM = HbmMaterialDefinition.of("actinium", "Actinium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Actinium-227 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Actinium-227 Nugget")
        .withItemDisplayName(HbmMaterialShape.BILLET, "Actinium-227 Billet")
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_actinium_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_actinium");
    public static final HbmMaterialDefinition SR90 = HbmMaterialDefinition.of("sr90", "Strontium-90", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_sr90_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_sr90");
    public static final HbmMaterialDefinition I131 = HbmMaterialDefinition.of("i131", "Iodine-131", HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_i131_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_i131");
    public static final HbmMaterialDefinition XE135 = HbmMaterialDefinition.of("xe135", "Xenon-135", HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_xe135_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_xe135");
    public static final HbmMaterialDefinition CS137 = HbmMaterialDefinition.of("cs137", "Caesium-137", HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_cs137_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_cs137");
    public static final HbmMaterialDefinition AT209 = HbmMaterialDefinition.of("at209", "Astatine-209", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_at209");
    public static final HbmMaterialDefinition CO60 = HbmMaterialDefinition.of("co60", "Cobalt-60", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Cobalt-60 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Cobalt-60 Nugget")
        .withItemId(HbmMaterialShape.DUST, "powder_co60");
    public static final HbmMaterialDefinition AU198 = HbmMaterialDefinition.of("au198", "Gold-198", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Gold-198 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Gold-198 Nugget")
        .withItemId(HbmMaterialShape.DUST, "powder_au198");
    public static final HbmMaterialDefinition PB209 = HbmMaterialDefinition.of("pb209", "Lead-209", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Lead-209 Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Lead-209 Nugget");
    public static final HbmMaterialDefinition TITANIUM = HbmMaterialDefinition.of("titanium", "Titanium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.PLATE, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE, HbmMaterialShape.SHELL)
        .withItemId(HbmMaterialShape.DUST, "powder_titanium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_titanium");
    public static final HbmMaterialDefinition COPPER = HbmMaterialDefinition.of("copper", "Copper", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.WIRE, HbmMaterialShape.PLATE, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE, HbmMaterialShape.SHELL, HbmMaterialShape.PIPE)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Industrial Grade Copper")
        .withItemId(HbmMaterialShape.WIRE, "wire_copper")
        .withItemDisplayName(HbmMaterialShape.WIRE, "Copper Wire")
        .withItemId(HbmMaterialShape.DENSE_WIRE, "coil_copper")
        .withItemDisplayName(HbmMaterialShape.DENSE_WIRE, "Copper Coil")
        .withItemId(HbmMaterialShape.DUST, "powder_copper")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_copper");
    public static final HbmMaterialDefinition RED_COPPER = HbmMaterialDefinition.of("red_copper", "Red Copper", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.WIRE, HbmMaterialShape.DENSE_WIRE)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Minecraft Grade Copper")
        .withItemId(HbmMaterialShape.WIRE, "wire_red_copper")
        .withItemDisplayName(HbmMaterialShape.WIRE, "Red Copper Wire")
        .withItemId(HbmMaterialShape.DUST, "powder_red_copper");
    public static final HbmMaterialDefinition ADVANCED_ALLOY = HbmMaterialDefinition.of("advanced_alloy", "Advanced Alloy", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.WIRE, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE)
        .withItemId(HbmMaterialShape.WIRE, "wire_advanced_alloy")
        .withItemDisplayName(HbmMaterialShape.WIRE, "Super Conductor")
        .withItemId(HbmMaterialShape.DENSE_WIRE, "coil_advanced_alloy")
        .withItemDisplayName(HbmMaterialShape.DENSE_WIRE, "Super Conducting Coil")
        .withItemId(HbmMaterialShape.DUST, "powder_advanced_alloy");
    public static final HbmMaterialDefinition TUNGSTEN = HbmMaterialDefinition.of("tungsten", "Tungsten", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.WIRE, HbmMaterialShape.BOLT, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE)
        .withItemId(HbmMaterialShape.WIRE, "wire_tungsten")
        .withItemDisplayName(HbmMaterialShape.WIRE, "Tungsten Wire")
        .withItemId(HbmMaterialShape.DENSE_WIRE, "coil_tungsten")
        .withItemDisplayName(HbmMaterialShape.DENSE_WIRE, "Heating Coil")
        .withItemId(HbmMaterialShape.DUST, "powder_tungsten")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_tungsten");
    public static final HbmMaterialDefinition ALUMINIUM = HbmMaterialDefinition.of("aluminium", "Aluminium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.WIRE, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE, HbmMaterialShape.SHELL, HbmMaterialShape.PIPE)
        .withItemId(HbmMaterialShape.WIRE, "wire_aluminium")
        .withItemDisplayName(HbmMaterialShape.WIRE, "Aluminium Wire")
        .withItemId(HbmMaterialShape.DUST, "powder_aluminium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_aluminium");
    public static final HbmMaterialDefinition STEEL = HbmMaterialDefinition.of("steel", "Steel", HbmMaterialShape.INGOT, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST, HbmMaterialShape.WIRE, HbmMaterialShape.BOLT, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE, HbmMaterialShape.SHELL, HbmMaterialShape.PIPE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.GRIP)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_steel_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_steel")
        .withItemId(HbmMaterialShape.PIPE, "pipes_steel")
        .withItemDisplayName(HbmMaterialShape.PIPE, "Steel Pipes");
    public static final HbmMaterialDefinition TCALLOY = HbmMaterialDefinition.of("tcalloy", "Technetium Steel", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER)
        .withItemId(HbmMaterialShape.DUST, "powder_tcalloy");
    public static final HbmMaterialDefinition CDALLOY = HbmMaterialDefinition.of("cdalloy", "Cadmium Steel", HbmMaterialShape.INGOT, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER);
    public static final HbmMaterialDefinition BISMUTH_BRONZE = HbmMaterialDefinition.of("bismuth_bronze", "Bismuth Bronze", HbmMaterialShape.INGOT, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER);
    public static final HbmMaterialDefinition ARSENIC_BRONZE = HbmMaterialDefinition.of("arsenic_bronze", "Arsenic Bronze", HbmMaterialShape.INGOT, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER);
    public static final HbmMaterialDefinition BSCCO = HbmMaterialDefinition.of("bscco", "BSCCO", HbmMaterialShape.INGOT, HbmMaterialShape.DENSE_WIRE);
    public static final HbmMaterialDefinition MAGNETIZED_TUNGSTEN = HbmMaterialDefinition.of("magnetized_tungsten", "Magnetized Tungsten", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.WIRE, HbmMaterialShape.DENSE_WIRE)
        .withItemId(HbmMaterialShape.WIRE, "wire_magnetized_tungsten")
        .withItemDisplayName(HbmMaterialShape.WIRE, "4000K High Temperature Super Conductor")
        .withItemId(HbmMaterialShape.DENSE_WIRE, "coil_magnetized_tungsten")
        .withItemDisplayName(HbmMaterialShape.DENSE_WIRE, "4000K High Temperature Super Conducting Coil")
        .withItemId(HbmMaterialShape.DUST, "powder_magnetized_tungsten");
    public static final HbmMaterialDefinition COMBINE_STEEL = HbmMaterialDefinition.of("combine_steel", "CMB Steel", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE)
        .withItemDisplayName(HbmMaterialShape.INGOT, "CMB Steel Ingot")
        .withItemId(HbmMaterialShape.DUST, "powder_combine_steel")
        .withItemDisplayName(HbmMaterialShape.PLATE, "CMB Steel Plate");
    public static final HbmMaterialDefinition STARMETAL = HbmMaterialDefinition.of("starmetal", "Starmetal", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.CAST_PLATE)
        .withItemDisplayName(HbmMaterialShape.INGOT, "§9Starmetal Ingot§r")
        .withItemId(HbmMaterialShape.DUST, "powder_starmetal")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_starmetal");
    public static final HbmMaterialDefinition FERRORANIUM = HbmMaterialDefinition.of("ferrouranium", "Ferrouranium", HbmMaterialShape.INGOT, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.HEAVY_RECEIVER);
    public static final HbmMaterialDefinition GUNMETAL = HbmMaterialDefinition.of("gunmetal", "Gunmetal", HbmMaterialShape.INGOT, HbmMaterialShape.PLATE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER, HbmMaterialShape.MECHANISM, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP);
    public static final HbmMaterialDefinition WEAPONSTEEL = HbmMaterialDefinition.of("weaponsteel", "Weapon Steel", HbmMaterialShape.INGOT, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.SHELL, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER, HbmMaterialShape.MECHANISM, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP);
    public static final HbmMaterialDefinition SATURNITE = HbmMaterialDefinition.of("saturnite", "Saturnite", HbmMaterialShape.INGOT, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.SHELL, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER, HbmMaterialShape.MECHANISM, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP);
    public static final HbmMaterialDefinition SCHRABIDIUM = HbmMaterialDefinition.of("schrabidium", "Schrabidium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.WIRE, HbmMaterialShape.BILLET, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE)
        .withItemId(HbmMaterialShape.WIRE, "wire_schrabidium")
        .withItemDisplayName(HbmMaterialShape.WIRE, "Schrabidium Wire")
        .withItemId(HbmMaterialShape.DUST, "powder_schrabidium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_schrabidium");
    public static final HbmMaterialDefinition SOLINIUM = HbmMaterialDefinition.of("solinium", "Solinium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET);
    public static final HbmMaterialDefinition SCHRABIDATE = HbmMaterialDefinition.of("schrabidate", "Ferric Schrabidate", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.DENSE_WIRE, HbmMaterialShape.CAST_PLATE)
        .withItemId(HbmMaterialShape.DUST, "powder_schrabidate");
    public static final HbmMaterialDefinition SCHRARANIUM = HbmMaterialDefinition.of("schraranium", "Schraranium", HbmMaterialShape.INGOT, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.INGOT, "ingot_schraranium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_schraranium");
    public static final HbmMaterialDefinition GH336 = HbmMaterialDefinition.of("gh336", "Ghiorsium-336", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET);
    public static final HbmMaterialDefinition LEAD = HbmMaterialDefinition.of("lead", "Lead", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL, HbmMaterialShape.PLATE)
        .withItemId(HbmMaterialShape.DUST, "powder_lead")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_lead");
    public static final HbmMaterialDefinition BISMUTH = HbmMaterialDefinition.of("bismuth", "Bismuth", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_bismuth");
    public static final HbmMaterialDefinition ARSENIC = HbmMaterialDefinition.of("arsenic", "Arsenic", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET);
    public static final HbmMaterialDefinition BORON = HbmMaterialDefinition.of("boron", "Boron", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_boron_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_boron");
    public static final HbmMaterialDefinition BORAX = HbmMaterialDefinition.of("borax", "Borax", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_borax")
        .withItemDisplayName(HbmMaterialShape.DUST, "Borax");
    public static final HbmMaterialDefinition TANTALIUM = HbmMaterialDefinition.of("tantalium", "Tantalium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.GEM, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Tantalum Ingot")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Tantalum Nugget")
        .withItemDisplayName(HbmMaterialShape.GEM, "Tantalum Polycrystal")
        .withItemDisplayName(HbmMaterialShape.DUST, "Tantalum Powder")
        .withItemId(HbmMaterialShape.GEM, "gem_tantalium")
        .withItemId(HbmMaterialShape.DUST, "powder_tantalium");
    public static final HbmMaterialDefinition RARE_EARTH = HbmMaterialDefinition.of("rare_earth", "Rare Earth", HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_rare");
    public static final HbmMaterialDefinition LANTHANIUM = HbmMaterialDefinition.of("lanthanium", "Lanthanium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Semi-Stable Lanthanium Ingot")
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_lanthanium_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_lanthanium");
    public static final HbmMaterialDefinition NEODYMIUM = HbmMaterialDefinition.of("neodymium", "Neodymium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST, HbmMaterialShape.DENSE_WIRE)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_neodymium_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_neodymium");
    public static final HbmMaterialDefinition NIOBIUM = HbmMaterialDefinition.of("niobium", "Niobium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST, HbmMaterialShape.DENSE_WIRE)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_niobium_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_niobium");
    public static final HbmMaterialDefinition BERYLLIUM = HbmMaterialDefinition.of("beryllium", "Beryllium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "powder_beryllium")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_beryllium");
    public static final HbmMaterialDefinition EMERALD = HbmMaterialDefinition.of("emerald", "Emerald", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_emerald");
    public static final HbmMaterialDefinition COBALT = HbmMaterialDefinition.of("cobalt", "Cobalt", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_cobalt_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_cobalt")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_cobalt");
    public static final HbmMaterialDefinition SODIUM = HbmMaterialDefinition.of("sodium", "Sodium", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_sodium")
        .withItemDisplayName(HbmMaterialShape.DUST, "Sodium");
    public static final HbmMaterialDefinition STRONTIUM = HbmMaterialDefinition.of("strontium", "Strontium", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_strontium");
    public static final HbmMaterialDefinition SULFUR = HbmMaterialDefinition.of("sulfur", "Sulfur", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "sulfur")
        .withItemDisplayName(HbmMaterialShape.DUST, "Sulfur")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_sulfur");
    public static final HbmMaterialDefinition KNO = HbmMaterialDefinition.of("kno", "KNO", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "nitra")
        .withItemDisplayName(HbmMaterialShape.DUST, "Niter")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_niter")
        .withItemDisplayName(HbmMaterialShape.CRYSTAL, "Niter Crystals");
    public static final HbmMaterialDefinition FLUORITE = HbmMaterialDefinition.of("fluorite", "Fluorite", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "fluorite")
        .withItemDisplayName(HbmMaterialShape.DUST, "Fluorite")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_fluorite");
    public static final HbmMaterialDefinition RED_PHOSPHORUS = HbmMaterialDefinition.of("red_phosphorus", "Red Phosphorus", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.DUST, "powder_fire")
        .withItemDisplayName(HbmMaterialShape.DUST, "Red Phosphorus")
        .withItemDisplayName(HbmMaterialShape.CRYSTAL, "Phosphorus Crystals")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_phosphorus");
    public static final HbmMaterialDefinition WHITE_PHOSPHORUS = HbmMaterialDefinition.of("white_phosphorus", "White Phosphorus", HbmMaterialShape.INGOT)
        .withItemId(HbmMaterialShape.INGOT, "ingot_phosphorus")
        .withItemDisplayName(HbmMaterialShape.INGOT, "Bar of White Phosphorus");
    public static final HbmMaterialDefinition SODALITE = HbmMaterialDefinition.of("sodalite", "Sodalite", HbmMaterialShape.FRAGMENT, HbmMaterialShape.GEM)
        .withItemId(HbmMaterialShape.GEM, "gem_sodalite")
        .withItemDisplayName(HbmMaterialShape.GEM, "Sodalite");
    public static final HbmMaterialDefinition CHLOROCALCITE = HbmMaterialDefinition.of("chlorocalcite", "Chlorocalcite", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_chlorocalcite")
        .withItemDisplayName(HbmMaterialShape.DUST, "Chlorocalcite");
    public static final HbmMaterialDefinition MOLYSITE = HbmMaterialDefinition.of("molysite", "Molysite", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_molysite")
        .withItemDisplayName(HbmMaterialShape.DUST, "Molysite");
    public static final HbmMaterialDefinition CINNABAR = HbmMaterialDefinition.of("cinnabar", "Cinnabar", HbmMaterialShape.FRAGMENT, HbmMaterialShape.GEM, HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.GEM, "cinnebar")
        .withItemDisplayName(HbmMaterialShape.GEM, "Cinnebar")
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_cinnebar")
        .withItemDisplayName(HbmMaterialShape.CRYSTAL, "Cinnabar Crystals");
    public static final HbmMaterialDefinition IODINE = HbmMaterialDefinition.of("iodine", "Iodine", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_iodine");
    public static final HbmMaterialDefinition ASTATINE = HbmMaterialDefinition.of("astatine", "Astatine", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_astatine");
    public static final HbmMaterialDefinition CAESIUM = HbmMaterialDefinition.of("caesium", "Caesium", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_caesium");
    public static final HbmMaterialDefinition BROMINE = HbmMaterialDefinition.of("bromine", "Bromine", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_bromine");
    public static final HbmMaterialDefinition TENNESSINE = HbmMaterialDefinition.of("tennessine", "Tennessine", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_tennessine");
    public static final HbmMaterialDefinition FLUX = HbmMaterialDefinition.of("flux", "Flux", HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_flux")
        .withItemDisplayName(HbmMaterialShape.DUST, "Flux");
    public static final HbmMaterialDefinition CERIUM = HbmMaterialDefinition.of("cerium", "Cerium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.DUST_TINY, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST_TINY, "powder_cerium_tiny")
        .withItemId(HbmMaterialShape.DUST, "powder_cerium");
    public static final HbmMaterialDefinition ZIRCONIUM = HbmMaterialDefinition.of("zirconium", "Zirconium", HbmMaterialShape.FRAGMENT, HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.WIRE, HbmMaterialShape.BILLET, HbmMaterialShape.DUST, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Zirconium Cube")
        .withItemDisplayName(HbmMaterialShape.NUGGET, "Zirconium Splinter")
        .withItemId(HbmMaterialShape.DUST, "powder_zirconium");
    public static final HbmMaterialDefinition SILICON = HbmMaterialDefinition.of("silicon", "Silicon", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Silicon Boule");
    public static final HbmMaterialDefinition POLYMER = HbmMaterialDefinition.of("polymer", "Polymer", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.PLATE, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Polymer Bar")
        .withItemId(HbmMaterialShape.DUST, "powder_polymer")
        .withItemId(HbmMaterialShape.PLATE, "plate_polymer")
        .withItemDisplayName(HbmMaterialShape.PLATE, "Insulator");
    public static final HbmMaterialDefinition BAKELITE = HbmMaterialDefinition.of("bakelite", "Bakelite", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Bakelite Bar")
        .withItemId(HbmMaterialShape.DUST, "powder_bakelite");
    public static final HbmMaterialDefinition LATEX = HbmMaterialDefinition.of("latex", "Latex", HbmMaterialShape.GEM, HbmMaterialShape.INGOT)
        .withItemId(HbmMaterialShape.GEM, "ball_resin")
        .withItemDisplayName(HbmMaterialShape.GEM, "Resin Ball")
        .withItemId(HbmMaterialShape.INGOT, "ingot_biorubber")
        .withItemDisplayName(HbmMaterialShape.INGOT, "Latex Bar");
    public static final HbmMaterialDefinition RUBBER = HbmMaterialDefinition.of("rubber", "Rubber", HbmMaterialShape.INGOT, HbmMaterialShape.PIPE, HbmMaterialShape.GRIP)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Rubber Bar");
    public static final HbmMaterialDefinition PC = HbmMaterialDefinition.of("pc", "PC", HbmMaterialShape.INGOT, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP)
        .withItemId(HbmMaterialShape.INGOT, "ingot_pc")
        .withItemDisplayName(HbmMaterialShape.INGOT, "Hard Plastic Bar");
    public static final HbmMaterialDefinition PVC = HbmMaterialDefinition.of("pvc", "PVC", HbmMaterialShape.INGOT, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP)
        .withItemId(HbmMaterialShape.INGOT, "ingot_pvc")
        .withItemDisplayName(HbmMaterialShape.INGOT, "PVC Bar");
    public static final HbmMaterialDefinition FIBERGLASS = HbmMaterialDefinition.of("fiberglass", "Fiberglass", HbmMaterialShape.INGOT)
        .withItemId(HbmMaterialShape.INGOT, "ingot_fiberglass")
        .withItemDisplayName(HbmMaterialShape.INGOT, "Fiberglass Bar");
    public static final HbmMaterialDefinition ASBESTOS = HbmMaterialDefinition.of("asbestos", "Asbestos", HbmMaterialShape.INGOT, HbmMaterialShape.DUST)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Asbestos Sheet")
        .withItemId(HbmMaterialShape.DUST, "powder_asbestos");
    public static final HbmMaterialDefinition OSMIRIDIUM = HbmMaterialDefinition.of("osmiridium", "Osmiridium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.CRYSTAL, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.WELDED_PLATE)
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_osmiridium");
    public static final HbmMaterialDefinition TRIXITE = HbmMaterialDefinition.of("trixite", "Trixite", HbmMaterialShape.CRYSTAL)
        .withItemId(HbmMaterialShape.CRYSTAL, "crystal_trixite");
    public static final HbmMaterialDefinition ELECTRONIUM = HbmMaterialDefinition.of("electronium", "Electronium", HbmMaterialShape.INGOT)
        .withItemId(HbmMaterialShape.INGOT, "ingot_electronium");
    public static final HbmMaterialDefinition SLAG = HbmMaterialDefinition.of("slag", "Slag", HbmMaterialShape.INGOT);
    public static final HbmMaterialDefinition MUD = HbmMaterialDefinition.of("mud", "Mud", HbmMaterialShape.INGOT)
        .withItemDisplayName(HbmMaterialShape.INGOT, "Solid Mud Brick");
    public static final HbmMaterialDefinition DURA_STEEL = HbmMaterialDefinition.of("dura_steel", "High-Speed Steel", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.BOLT, HbmMaterialShape.PLATE, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.PIPE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.HEAVY_RECEIVER, HbmMaterialShape.GRIP)
        .withItemDisplayName(HbmMaterialShape.INGOT, "High-Speed Steel Ingot")
        .withItemId(HbmMaterialShape.DUST, "powder_dura_steel")
        .withItemDisplayName(HbmMaterialShape.DUST, "High-Speed Steel Powder")
        .withItemDisplayName(HbmMaterialShape.BOLT, "High-Speed Steel Bolt")
        .withItemDisplayName(HbmMaterialShape.PLATE, "High-Speed Steel Plate");
    public static final HbmMaterialDefinition DESH = HbmMaterialDefinition.of("desh", "Desh", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.DUST, HbmMaterialShape.CAST_PLATE, HbmMaterialShape.LIGHT_BARREL, HbmMaterialShape.HEAVY_BARREL, HbmMaterialShape.LIGHT_RECEIVER, HbmMaterialShape.STOCK, HbmMaterialShape.GRIP)
        .withItemId(HbmMaterialShape.DUST, "powder_desh")
        .withItemId(HbmMaterialShape.CAST_PLATE, "plate_desh")
        .withItemDisplayName(HbmMaterialShape.CAST_PLATE, "Desh Compound Plate");
    public static final HbmMaterialDefinition EUPHEMIUM = HbmMaterialDefinition.of("euphemium", "Euphemium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.DUST)
        .withItemId(HbmMaterialShape.DUST, "powder_euphemium");
    public static final HbmMaterialDefinition DINEUTRONIUM = HbmMaterialDefinition.of("dineutronium", "Dineutronium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.DUST, HbmMaterialShape.DENSE_WIRE)
        .withItemId(HbmMaterialShape.DUST, "powder_dineutronium");

    private static final List<HbmMaterialDefinition> ORDERED = List.of(
        CARBON,
        COAL,
        LIGNITE,
        GRAPHITE,
        DIAMOND,
        FIREBRICK,
        IRON,
        GOLD,
        REDSTONE,
        LAPIS,
        QUARTZ,
        LITHIUM,
        CALCIUM,
        CADMIUM,
        URANIUM,
        AUSTRALIUM,
        U233,
        U235,
        U238,
        TH232,
        PLUTONIUM,
        PU238,
        PU239,
        PU240,
        NEPTUNIUM,
        POLONIUM,
        TECHNETIUM,
        RA226,
        ACTINIUM,
        SR90,
        I131,
        XE135,
        CS137,
        AT209,
        CO60,
        AU198,
        PB209,
        TITANIUM,
        COPPER,
        RED_COPPER,
        ADVANCED_ALLOY,
        TUNGSTEN,
        ALUMINIUM,
        STEEL,
        TCALLOY,
        CDALLOY,
        BISMUTH_BRONZE,
        ARSENIC_BRONZE,
        BSCCO,
        MAGNETIZED_TUNGSTEN,
        COMBINE_STEEL,
        STARMETAL,
        FERRORANIUM,
        GUNMETAL,
        WEAPONSTEEL,
        SATURNITE,
        SCHRABIDIUM,
        SOLINIUM,
        SCHRABIDATE,
        SCHRARANIUM,
        GH336,
        LEAD,
        BISMUTH,
        ARSENIC,
        BORON,
        BORAX,
        TANTALIUM,
        RARE_EARTH,
        LANTHANIUM,
        NEODYMIUM,
        NIOBIUM,
        BERYLLIUM,
        EMERALD,
        COBALT,
        SODIUM,
        STRONTIUM,
        SULFUR,
        KNO,
        FLUORITE,
        RED_PHOSPHORUS,
        WHITE_PHOSPHORUS,
        SODALITE,
        CHLOROCALCITE,
        MOLYSITE,
        CINNABAR,
        IODINE,
        ASTATINE,
        CAESIUM,
        BROMINE,
        TENNESSINE,
        FLUX,
        CERIUM,
        ZIRCONIUM,
        SILICON,
        POLYMER,
        BAKELITE,
        LATEX,
        RUBBER,
        PC,
        PVC,
        FIBERGLASS,
        ASBESTOS,
        OSMIRIDIUM,
        TRIXITE,
        ELECTRONIUM,
        SLAG,
        MUD,
        DURA_STEEL,
        DESH,
        EUPHEMIUM,
        DINEUTRONIUM
    );

    private HbmMaterials() {
    }

    public static List<HbmMaterialDefinition> ordered() {
        return ORDERED;
    }
}

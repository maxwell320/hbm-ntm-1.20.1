package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BarrelType;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.OverworldOreType;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.item.BarrelBlockItem;
import com.hbm.ntm.common.item.BriquetteItemType;
import com.hbm.ntm.common.item.BlowtorchItem;
import com.hbm.ntm.common.item.BatteryBlockItem;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.BlueprintItem;
import com.hbm.ntm.common.item.CasingItemType;
import com.hbm.ntm.common.item.CanisterItem;
import com.hbm.ntm.common.item.CokeItemType;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.item.DosimeterItem;
import com.hbm.ntm.common.item.DisperserCanisterItem;
import com.hbm.ntm.common.item.FluidIdentifierItem;
import com.hbm.ntm.common.item.FluidTankItem;
import com.hbm.ntm.common.item.GasTankItem;
import com.hbm.ntm.common.item.GeigerCounterItem;
import com.hbm.ntm.common.item.IcfPelletItem;
import com.hbm.ntm.common.item.MaterialPartItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.item.PageItem;
import com.hbm.ntm.common.item.PistonSetItem;
import com.hbm.ntm.common.item.RadXItem;
import com.hbm.ntm.common.item.RadawayItem;
import com.hbm.ntm.common.item.RtgDepletedPelletItem;
import com.hbm.ntm.common.item.RtgPelletItem;
import com.hbm.ntm.common.item.SellafieldBlockItem;
import com.hbm.ntm.common.item.ShredderBladesItem;
import com.hbm.ntm.common.item.StampBookItem;
import com.hbm.ntm.common.item.StampItem;
import com.hbm.ntm.common.item.StampItemType;
import com.hbm.ntm.common.item.TemplateFolderItem;
import com.hbm.ntm.common.item.UndefinedItem;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HbmItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HbmNtmMod.MOD_ID);
    private static final Map<String, RegistryObject<Item>> MATERIAL_PARTS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> BRIQUETTES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> CASINGS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> COKES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> CHUNK_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> CIRCUITS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> STAMPS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> SIMPLE_ITEMS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> HIDDEN_ITEMS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> BLOCK_ITEMS = new LinkedHashMap<>();
    public static final RegistryObject<Item> BURNT_BARK = registerSimpleItem("burnt_bark", SIMPLE_ITEMS);
    public static final RegistryObject<Item> BIOMASS = registerSimpleItem("biomass", SIMPLE_ITEMS);
    public static final RegistryObject<Item> BIOMASS_COMPRESSED = registerSimpleItem("biomass_compressed", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SCRAP = registerSimpleItem("scrap", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SCRAP_NUCLEAR = registerSimpleItem("scrap_nuclear", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SCRAP_OIL = registerSimpleItem("scrap_oil", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SCRAP_PLASTIC = registerSimpleItem("scrap_plastic", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DUCTTAPE = registerSimpleItem("ducttape", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FUSE = registerSimpleItem("fuse", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SAFETY_FUSE = registerSimpleItem("safety_fuse", SIMPLE_ITEMS);
    public static final RegistryObject<Item> BATTERY_POTATO = registerItem("battery_potato", () -> new BatteryItem(1_000, 0, 100, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> COIL_COPPER_TORUS = registerSimpleItem("coil_copper_torus", SIMPLE_ITEMS);
    public static final RegistryObject<Item> MOTOR = registerSimpleItem("motor", SIMPLE_ITEMS);
    public static final RegistryObject<Item> MOTOR_DESH = registerSimpleItem("motor_desh", SIMPLE_ITEMS);
    public static final RegistryObject<Item> TANK_STEEL = registerSimpleItem("tank_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> CANISTER_EMPTY = registerItem("canister_empty", () -> new CanisterItem(false, HbmItems::canisterEmptyItem, HbmItems::canisterFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> CANISTER_FULL = registerItem("canister_full", () -> new CanisterItem(true, HbmItems::canisterEmptyItem, HbmItems::canisterFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> CANISTER_NAPALM = registerSimpleItem("canister_napalm", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DISPERSER_CANISTER_EMPTY = registerItem("disperser_canister_empty",
        () -> new DisperserCanisterItem(false, DisperserCanisterItem.ContainerKind.DISPERSER, 2_000,
            HbmItems::disperserCanisterEmptyItem, HbmItems::disperserCanisterItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> DISPERSER_CANISTER = registerItem("disperser_canister",
        () -> new DisperserCanisterItem(true, DisperserCanisterItem.ContainerKind.DISPERSER, 2_000,
            HbmItems::disperserCanisterEmptyItem, HbmItems::disperserCanisterItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> GLYPHID_GLAND_EMPTY = registerItem("glyphid_gland_empty",
        () -> new DisperserCanisterItem(false, DisperserCanisterItem.ContainerKind.GLYPHID, 4_000,
            HbmItems::glyphidGlandEmptyItem, HbmItems::glyphidGlandItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> GLYPHID_GLAND = registerItem("glyphid_gland",
        () -> new DisperserCanisterItem(true, DisperserCanisterItem.ContainerKind.GLYPHID, 4_000,
            HbmItems::glyphidGlandEmptyItem, HbmItems::glyphidGlandItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> GAS_EMPTY = registerItem("gas_empty", () -> new GasTankItem(false, HbmItems::gasEmptyItem, HbmItems::gasFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> GAS_FULL = registerItem("gas_full", () -> new GasTankItem(true, HbmItems::gasEmptyItem, HbmItems::gasFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_TANK_EMPTY = registerSimpleItem("fluid_tank_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_TANK_FULL = registerItem("fluid_tank_full", () -> new FluidTankItem(true, false, 1_000, HbmItems::fluidTankEmptyItem, HbmItems::fluidTankFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_TANK_LEAD_EMPTY = registerSimpleItem("fluid_tank_lead_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_TANK_LEAD_FULL = registerItem("fluid_tank_lead_full", () -> new FluidTankItem(true, true, 1_000, HbmItems::fluidTankLeadEmptyItem, HbmItems::fluidTankLeadFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_BARREL_EMPTY = registerSimpleItem("fluid_barrel_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_BARREL_FULL = registerItem("fluid_barrel_full", () -> new FluidTankItem(true, false, 16_000, HbmItems::fluidBarrelEmptyItem, HbmItems::fluidBarrelFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_PACK_EMPTY = registerSimpleItem("fluid_pack_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_PACK_FULL = registerItem("fluid_pack_full", () -> new FluidTankItem(true, false, 32_000, HbmItems::fluidPackEmptyItem, HbmItems::fluidPackFullItem, new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> COIL_ADVANCED_TORUS = registerSimpleItem("coil_advanced_torus", SIMPLE_ITEMS);
    public static final RegistryObject<Item> COIL_GOLD_TORUS = registerSimpleItem("coil_gold_torus", SIMPLE_ITEMS);
    public static final RegistryObject<Item> INGOT_CFT = registerSimpleItem("ingot_cft", SIMPLE_ITEMS);
    public static final RegistryObject<Item> CENTRIFUGE_ELEMENT = registerSimpleItem("centrifuge_element", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PHOTO_PANEL = registerSimpleItem("photo_panel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PIN = registerSimpleItem("pin", SIMPLE_ITEMS);
    public static final RegistryObject<Item> CATALYST_CLAY = registerSimpleItem("catalyst_clay", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEUTERIUM_FILTER = registerSimpleItem("deuterium_filter", SIMPLE_ITEMS);
    public static final RegistryObject<Item> HAZMAT_CLOTH = registerSimpleItem("hazmat_cloth", SIMPLE_ITEMS);
    public static final RegistryObject<Item> ASBESTOS_CLOTH = registerSimpleItem("asbestos_cloth", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FILTER_COAL = registerSimpleItem("filter_coal", SIMPLE_ITEMS);
    public static final RegistryObject<Item> REACTOR_CORE = registerSimpleItem("reactor_core", SIMPLE_ITEMS);
    public static final RegistryObject<Item> THERMO_ELEMENT = registerSimpleItem("thermo_element", SIMPLE_ITEMS);
    public static final RegistryObject<Item> RTG_UNIT = registerSimpleItem("rtg_unit", SIMPLE_ITEMS);
    public static final RegistryObject<Item> MAGNETRON = registerSimpleItem("magnetron", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILL_TITANIUM = registerSimpleItem("drill_titanium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> ENTANGLEMENT_KIT = registerSimpleItem("entanglement_kit", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_STEEL = registerSimpleItem("drillbit_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_STEEL_DIAMOND = registerSimpleItem("drillbit_steel_diamond", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_HSS = registerSimpleItem("drillbit_hss", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_HSS_DIAMOND = registerSimpleItem("drillbit_hss_diamond", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_DESH = registerSimpleItem("drillbit_desh", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_DESH_DIAMOND = registerSimpleItem("drillbit_desh_diamond", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_TCALLOY = registerSimpleItem("drillbit_tcalloy", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_TCALLOY_DIAMOND = registerSimpleItem("drillbit_tcalloy_diamond", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_FERRO = registerSimpleItem("drillbit_ferro", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DRILLBIT_FERRO_DIAMOND = registerSimpleItem("drillbit_ferro_diamond", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PART_LITHIUM = registerSimpleItem("part_lithium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PART_BERYLLIUM = registerSimpleItem("part_beryllium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PART_CARBON = registerSimpleItem("part_carbon", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PART_COPPER = registerSimpleItem("part_copper", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PART_PLUTONIUM = registerSimpleItem("part_plutonium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> BLUEPRINTS = registerItem("blueprints", BlueprintItem::new, HIDDEN_ITEMS);
    public static final RegistryObject<Item> TEMPLATE_FOLDER = registerItem("template_folder", TemplateFolderItem::new, SIMPLE_ITEMS);
    public static final RegistryObject<Item> FINS_FLAT = registerSimpleItem("fins_flat", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SPHERE_STEEL = registerSimpleItem("sphere_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PEDESTAL_STEEL = registerSimpleItem("pedestal_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FINS_BIG_STEEL = registerSimpleItem("fins_big_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FINS_SMALL_STEEL = registerSimpleItem("fins_small_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FINS_TRI_STEEL = registerSimpleItem("fins_tri_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FINS_QUAD_TITANIUM = registerSimpleItem("fins_quad_titanium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> BLADE_TITANIUM = registerSimpleItem("blade_titanium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> TURBINE_TITANIUM = registerSimpleItem("turbine_titanium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PISTON_SET_STEEL = registerItem("piston_set_steel", () -> new PistonSetItem(PistonSetItem.Tier.STEEL, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PISTON_SET_DURA = registerItem("piston_set_dura", () -> new PistonSetItem(PistonSetItem.Tier.DURA, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PISTON_SET_DESH = registerItem("piston_set_desh", () -> new PistonSetItem(PistonSetItem.Tier.DESH, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PISTON_SET_STARMETAL = registerItem("piston_set_starmetal", () -> new PistonSetItem(PistonSetItem.Tier.STARMETAL, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLYWHEEL_BERYLLIUM = registerSimpleItem("flywheel_beryllium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> RING_STARMETAL = registerSimpleItem("ring_starmetal", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SAWBLADE = registerSimpleItem("sawblade", SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD = registerItem("meteorite_sword", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_SEARED = registerItem("meteorite_sword_seared", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_REFORGED = registerItem("meteorite_sword_reforged", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_HARDENED = registerItem("meteorite_sword_hardened", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_ALLOYED = registerItem("meteorite_sword_alloyed", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_MACHINED = registerItem("meteorite_sword_machined", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_TREATED = registerItem("meteorite_sword_treated", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_ETCHED = registerItem("meteorite_sword_etched", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_BRED = registerItem("meteorite_sword_bred", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_IRRADIATED = registerItem("meteorite_sword_irradiated", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_FUSED = registerItem("meteorite_sword_fused", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> METEORITE_SWORD_BALEFUL = registerItem("meteorite_sword_baleful", () -> new Item(new Item.Properties().stacksTo(1)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> NUGGET_MERCURY_TINY = registerSimpleItem("nugget_mercury_tiny", SIMPLE_ITEMS);
    public static final RegistryObject<Item> NUGGET_MERCURY = registerSimpleItem("nugget_mercury", SIMPLE_ITEMS);
    public static final RegistryObject<Item> INGOT_MERCURY = NUGGET_MERCURY;
    public static final RegistryObject<Item> BOTTLE_MERCURY = registerItem("bottle_mercury", () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> NUCLEAR_WASTE = registerSimpleItem("nuclear_waste", SIMPLE_ITEMS);
    public static final RegistryObject<Item> NUCLEAR_WASTE_VITRIFIED = registerSimpleItem("nuclear_waste_vitrified", SIMPLE_ITEMS);
    public static final RegistryObject<Item> NUCLEAR_WASTE_TINY = registerSimpleItem("nuclear_waste_tiny", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEBRIS_CONCRETE = registerSimpleItem("debris_concrete", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEBRIS_SHRAPNEL = registerSimpleItem("debris_shrapnel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEBRIS_EXCHANGER = registerSimpleItem("debris_exchanger", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEBRIS_ELEMENT = registerSimpleItem("debris_element", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEBRIS_METAL = registerSimpleItem("debris_metal", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEBRIS_GRAPHITE = registerSimpleItem("debris_graphite", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FALLOUT = registerSimpleItem("fallout", SIMPLE_ITEMS);
    public static final RegistryObject<Item> POWDER_SAWDUST = registerSimpleItem("powder_sawdust", SIMPLE_ITEMS);
    public static final RegistryObject<Item> POWDER_ASH = registerSimpleItem("powder_ash", SIMPLE_ITEMS);
    public static final RegistryObject<Item> POWDER_ICE = registerSimpleItem("powder_ice", SIMPLE_ITEMS);
    public static final RegistryObject<Item> POWDER_MAGIC = registerSimpleItem("powder_magic", SIMPLE_ITEMS);
    public static final RegistryObject<Item> POWDER_POISON = registerSimpleItem("powder_poison", SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_MUFFLER = registerSimpleItem("upgrade_muffler", SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_TEMPLATE = registerSimpleItem("upgrade_template", SIMPLE_ITEMS);
    public static final RegistryObject<Item> BLADES_STEEL = registerItem("blades_steel", () -> new ShredderBladesItem(200), SIMPLE_ITEMS);
    public static final RegistryObject<Item> BLADES_TITANIUM = registerItem("blades_titanium", () -> new ShredderBladesItem(350), SIMPLE_ITEMS);
    public static final RegistryObject<Item> BLADES_ADVANCED_ALLOY = registerItem("blades_advanced_alloy", () -> new ShredderBladesItem(700), SIMPLE_ITEMS);
    public static final RegistryObject<Item> BLADES_DESH = registerItem("blades_desh", () -> new ShredderBladesItem(0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_SPEED_1 = registerItem("upgrade_speed_1", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.SPEED, 1), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_SPEED_2 = registerItem("upgrade_speed_2", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.SPEED, 2), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_SPEED_3 = registerItem("upgrade_speed_3", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.SPEED, 3), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_EFFECT_1 = registerItem("upgrade_effect_1", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.EFFECT, 1), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_EFFECT_2 = registerItem("upgrade_effect_2", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.EFFECT, 2), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_EFFECT_3 = registerItem("upgrade_effect_3", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.EFFECT, 3), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_POWER_1 = registerItem("upgrade_power_1", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.POWER, 1), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_POWER_2 = registerItem("upgrade_power_2", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.POWER, 2), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_POWER_3 = registerItem("upgrade_power_3", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.POWER, 3), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_FORTUNE_1 = registerItem("upgrade_fortune_1", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.FORTUNE, 1), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_FORTUNE_2 = registerItem("upgrade_fortune_2", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.FORTUNE, 2), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_FORTUNE_3 = registerItem("upgrade_fortune_3", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.FORTUNE, 3), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_AFTERBURN_1 = registerItem("upgrade_afterburn_1", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.AFTERBURN, 1), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_AFTERBURN_2 = registerItem("upgrade_afterburn_2", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.AFTERBURN, 2), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_AFTERBURN_3 = registerItem("upgrade_afterburn_3", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.AFTERBURN, 3), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_OVERDRIVE_1 = registerItem("upgrade_overdrive_1", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.OVERDRIVE, 1), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_OVERDRIVE_2 = registerItem("upgrade_overdrive_2", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.OVERDRIVE, 2), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_OVERDRIVE_3 = registerItem("upgrade_overdrive_3", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.OVERDRIVE, 3), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_RADIUS = registerItem("upgrade_radius", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.RADIUS, 0, 16), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_HEALTH = registerItem("upgrade_health", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.HEALTH, 0, 16), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_SMELTER = registerItem("upgrade_smelter", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.SMELTER, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_SHREDDER = registerItem("upgrade_shredder", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.SHREDDER, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_CENTRIFUGE = registerItem("upgrade_centrifuge", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.CENTRIFUGE, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_CRYSTALLIZER = registerItem("upgrade_crystallizer", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.CRYSTALLIZER, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_NULLIFIER = registerItem("upgrade_nullifier", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.NULLIFIER, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_SCREM = registerItem("upgrade_screm", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.SCREM, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_GC_SPEED = registerItem("upgrade_gc_speed", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.GC_SPEED, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_5G = registerItem("upgrade_5g", () -> new MachineUpgradeItem(MachineUpgradeItem.UpgradeType.SPECIAL, 0), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_CHARGED = registerSimpleItem("pellet_charged", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PARTICLE_MUON = registerSimpleItem("particle_muon", SIMPLE_ITEMS);
    public static final RegistryObject<Item> ICF_PELLET_EMPTY = registerSimpleItem("icf_pellet_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> ICF_PELLET = registerItem("icf_pellet", IcfPelletItem::new, SIMPLE_ITEMS);
    public static final RegistryObject<Item> ICF_PELLET_DEPLETED = registerSimpleItem("icf_pellet_depleted", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_DEPLETED_BISMUTH = registerItem("pellet_rtg_depleted_bismuth", () -> new RtgDepletedPelletItem(() -> Objects.requireNonNull(getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get())), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_DEPLETED_MERCURY = registerItem("pellet_rtg_depleted_mercury", () -> new RtgDepletedPelletItem(() -> Objects.requireNonNull(getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get())), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_DEPLETED_NEPTUNIUM = registerItem("pellet_rtg_depleted_neptunium", () -> new RtgDepletedPelletItem(() -> Objects.requireNonNull(getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get())), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_DEPLETED_LEAD = registerItem("pellet_rtg_depleted_lead", () -> new RtgDepletedPelletItem(() -> Objects.requireNonNull(getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get())), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_DEPLETED_ZIRCONIUM = registerItem("pellet_rtg_depleted_zirconium", () -> new RtgDepletedPelletItem(() -> Objects.requireNonNull(getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get())), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_DEPLETED_NICKEL = registerItem("pellet_rtg_depleted_nickel", () -> new RtgDepletedPelletItem(() -> Objects.requireNonNull(getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get())), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_RADIUM = registerItem("pellet_rtg_radium", () -> new RtgPelletItem(3, false)
        .setDecays(PELLET_RTG_DEPLETED_LEAD::get, rtgLifespan(16.0F, RtgPelletItem.HalfLifeType.LONG)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_WEAK = registerItem("pellet_rtg_weak", () -> new RtgPelletItem(5, false)
        .setDecays(PELLET_RTG_DEPLETED_LEAD::get, rtgLifespan(1.0F, RtgPelletItem.HalfLifeType.LONG)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG = registerItem("pellet_rtg", () -> new RtgPelletItem(10, true)
        .setDecays(PELLET_RTG_DEPLETED_LEAD::get, rtgLifespan(87.7F, RtgPelletItem.HalfLifeType.MEDIUM)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_STRONTIUM = registerItem("pellet_rtg_strontium", () -> new RtgPelletItem(15, false)
        .setDecays(PELLET_RTG_DEPLETED_ZIRCONIUM::get, rtgLifespan(29.0F, RtgPelletItem.HalfLifeType.MEDIUM)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_COBALT = registerItem("pellet_rtg_cobalt", () -> new RtgPelletItem(15, false)
        .setDecays(PELLET_RTG_DEPLETED_NICKEL::get, rtgLifespan(5.3F, RtgPelletItem.HalfLifeType.MEDIUM)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_ACTINIUM = registerItem("pellet_rtg_actinium", () -> new RtgPelletItem(20, false)
        .setDecays(PELLET_RTG_DEPLETED_LEAD::get, rtgLifespan(21.8F, RtgPelletItem.HalfLifeType.MEDIUM)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_AMERICIUM = registerItem("pellet_rtg_americium", () -> new RtgPelletItem(20, false)
        .setDecays(PELLET_RTG_DEPLETED_NEPTUNIUM::get, rtgLifespan(4.7F, RtgPelletItem.HalfLifeType.LONG)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_POLONIUM = registerItem("pellet_rtg_polonium", () -> new RtgPelletItem(50, false)
        .setDecays(PELLET_RTG_DEPLETED_LEAD::get, rtgLifespan(138.0F, RtgPelletItem.HalfLifeType.SHORT)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_GOLD = registerItem("pellet_rtg_gold", () -> new RtgPelletItem(200, false)
        .setDecays(PELLET_RTG_DEPLETED_MERCURY::get, rtgLifespan(2.7F, RtgPelletItem.HalfLifeType.SHORT)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> PELLET_RTG_LEAD = registerItem("pellet_rtg_lead", () -> new RtgPelletItem(600, false)
        .setDecays(PELLET_RTG_DEPLETED_BISMUTH::get, rtgLifespan(0.3F, RtgPelletItem.HalfLifeType.SHORT)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> UNDEFINED = registerItem("undefined", UndefinedItem::new, SIMPLE_ITEMS);
    public static final RegistryObject<Item> FALLOUT_LAYER = registerBlockItem("fallout_layer", HbmBlocks.FALLOUT, BLOCK_ITEMS);
    public static final RegistryObject<Item> CREATIVE_ENERGY_SOURCE = registerBlockItem("creative_energy_source", HbmBlocks.CREATIVE_ENERGY_SOURCE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_BATTERY = registerItem("machine_battery", () -> new BatteryBlockItem(HbmBlocks.MACHINE_BATTERY.get(), new Item.Properties()), HIDDEN_ITEMS);
    public static final RegistryObject<Item> STEEL_BEAM = registerBlockItem("steel_beam", HbmBlocks.STEEL_BEAM, BLOCK_ITEMS);
    public static final RegistryObject<Item> STEEL_GRATE = registerBlockItem("steel_grate", HbmBlocks.STEEL_GRATE, BLOCK_ITEMS);
    public static final RegistryObject<Item> STEEL_GRATE_WIDE = registerBlockItem("steel_grate_wide", HbmBlocks.STEEL_GRATE_WIDE, BLOCK_ITEMS);
    public static final RegistryObject<Item> RED_CABLE = registerBlockItem("red_cable", HbmBlocks.RED_CABLE, BLOCK_ITEMS);
    public static final RegistryObject<Item> RED_CABLE_CLASSIC = registerBlockItem("red_cable_classic", HbmBlocks.RED_CABLE_CLASSIC, BLOCK_ITEMS);
    public static final RegistryObject<Item> FLUID_DUCT_NEO = registerBlockItem("fluid_duct_neo", HbmBlocks.FLUID_DUCT_NEO, BLOCK_ITEMS);
    public static final RegistryObject<Item> GLASS_BORON = registerBlockItem("glass_boron", HbmBlocks.GLASS_BORON, BLOCK_ITEMS);
    public static final RegistryObject<Item> GEIGER = registerBlockItem("geiger", HbmBlocks.GEIGER, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_PRESS = registerBlockItem("machine_press", HbmBlocks.MACHINE_PRESS, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_ASSEMBLY_MACHINE = registerBlockItem("machine_assembly_machine", HbmBlocks.MACHINE_ASSEMBLY_MACHINE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_SOLDERING_STATION = registerBlockItem("machine_soldering_station", HbmBlocks.MACHINE_SOLDERING_STATION, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_DI_FURNACE = registerBlockItem("machine_difurnace", HbmBlocks.MACHINE_DI_FURNACE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_DI_FURNACE_EXTENSION = registerBlockItem("machine_difurnace_extension", HbmBlocks.MACHINE_DI_FURNACE_EXTENSION, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_DI_FURNACE_RTG = registerBlockItem("machine_difurnace_rtg", HbmBlocks.MACHINE_DI_FURNACE_RTG, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_ELECTRIC_FURNACE = registerBlockItem("machine_electric_furnace", HbmBlocks.MACHINE_ELECTRIC_FURNACE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_RTG_FURNACE = registerBlockItem("machine_rtg_furnace", HbmBlocks.MACHINE_RTG_FURNACE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_RTG_GREY = registerBlockItem("machine_rtg_grey", HbmBlocks.MACHINE_RTG_GREY, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_DIESEL_GENERATOR = registerBlockItem("machine_diesel", HbmBlocks.MACHINE_DIESEL_GENERATOR, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_COMBUSTION_ENGINE = registerBlockItem("machine_combustion", HbmBlocks.MACHINE_COMBUSTION_ENGINE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_ASHPIT = registerBlockItem("machine_ashpit", HbmBlocks.MACHINE_ASHPIT, BLOCK_ITEMS);
    public static final RegistryObject<Item> CHIMNEY_BRICK = registerBlockItem("chimney_brick", HbmBlocks.CHIMNEY_BRICK, BLOCK_ITEMS);
    public static final RegistryObject<Item> CHIMNEY_INDUSTRIAL = registerBlockItem("chimney_industrial", HbmBlocks.CHIMNEY_INDUSTRIAL, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_MINI_RTG = registerBlockItem("machine_minirtg", HbmBlocks.MACHINE_MINI_RTG, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_POWER_RTG = registerBlockItem("machine_powerrtg", HbmBlocks.MACHINE_POWER_RTG, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_SHREDDER = registerBlockItem("machine_shredder", HbmBlocks.MACHINE_SHREDDER, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_CENTRIFUGE = registerBlockItem("machine_centrifuge", HbmBlocks.MACHINE_CENTRIFUGE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_GAS_CENTRIFUGE = registerBlockItem("machine_gascent", HbmBlocks.MACHINE_GAS_CENTRIFUGE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_CYCLOTRON = registerBlockItem("machine_cyclotron", HbmBlocks.MACHINE_CYCLOTRON, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_PUREX = registerBlockItem("machine_purex", HbmBlocks.MACHINE_PUREX, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_ICF = registerBlockItem("machine_icf", HbmBlocks.MACHINE_ICF, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_ICF_CONTROLLER = registerBlockItem("machine_icf_controller", HbmBlocks.MACHINE_ICF_CONTROLLER, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_ICF_LASER_COMPONENT = registerBlockItem("machine_icf_laser_component", HbmBlocks.MACHINE_ICF_LASER_COMPONENT, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_ICF_PRESS = registerBlockItem("machine_icf_press", HbmBlocks.MACHINE_ICF_PRESS, BLOCK_ITEMS);
    public static final RegistryObject<Item> PRESS_PREHEATER = registerBlockItem("press_preheater", HbmBlocks.PRESS_PREHEATER, BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_PLASTIC = registerItem("barrel_plastic", () -> new BarrelBlockItem(HbmBlocks.BARREL_PLASTIC.get(), BarrelType.PLASTIC, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_CORRODED = registerItem("barrel_corroded", () -> new BarrelBlockItem(HbmBlocks.BARREL_CORRODED.get(), BarrelType.CORRODED, new Item.Properties()), HIDDEN_ITEMS);
    public static final RegistryObject<Item> BARREL_IRON = registerItem("barrel_iron", () -> new BarrelBlockItem(HbmBlocks.BARREL_IRON.get(), BarrelType.IRON, new Item.Properties()), HIDDEN_ITEMS);
    public static final RegistryObject<Item> BARREL_STEEL = registerItem("barrel_steel", () -> new BarrelBlockItem(HbmBlocks.BARREL_STEEL.get(), BarrelType.STEEL, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_TCALLOY = registerItem("barrel_tcalloy", () -> new BarrelBlockItem(HbmBlocks.BARREL_TCALLOY.get(), BarrelType.TCALLOY, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_ANTIMATTER = registerItem("barrel_antimatter", () -> new BarrelBlockItem(HbmBlocks.BARREL_ANTIMATTER.get(), BarrelType.ANTIMATTER, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_RED = registerBlockItem("barrel_red", HbmBlocks.BARREL_RED, BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_PINK = registerBlockItem("barrel_pink", HbmBlocks.BARREL_PINK, BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_LOX = registerBlockItem("barrel_lox", HbmBlocks.BARREL_LOX, BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_TAINT = registerBlockItem("barrel_taint", HbmBlocks.BARREL_TAINT, BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_YELLOW = registerBlockItem("barrel_yellow", HbmBlocks.BARREL_YELLOW, BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_VITRIFIED = registerBlockItem("barrel_vitrified", HbmBlocks.BARREL_VITRIFIED, BLOCK_ITEMS);
    public static final RegistryObject<Item> SELLAFIELD = registerItem("sellafield", () -> new SellafieldBlockItem(HbmBlocks.SELLAFIELD.get(), new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> GEIGER_COUNTER = registerItem("geiger_counter", () -> new GeigerCounterItem(new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> GEM_RAD = registerSimpleItem("gem_rad", SIMPLE_ITEMS);
    public static final RegistryObject<Item> GEM_ALEXANDRITE = registerSimpleItem("gem_alexandrite", SIMPLE_ITEMS);
    public static final RegistryObject<Item> IV_EMPTY = registerSimpleItem("iv_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> CRT_DISPLAY = registerSimpleItem("crt_display", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DOSIMETER = registerItem("dosimeter", () -> new DosimeterItem(new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_IDENTIFIER_MULTI = registerItem("fluid_identifier_multi", () -> new FluidIdentifierItem(new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> BLOWTORCH = registerItem("blowtorch", () -> new BlowtorchItem(BlowtorchItem.FuelProfile.BLOWTORCH), SIMPLE_ITEMS);
    public static final RegistryObject<Item> ACETYLENE_TORCH = registerItem("acetylene_torch", () -> new BlowtorchItem(BlowtorchItem.FuelProfile.ACETYLENE), SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADAWAY = registerItem("radaway", () -> new RadawayItem(140, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADAWAY_STRONG = registerItem("radaway_strong", () -> new RadawayItem(350, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADAWAY_FLUSH = registerItem("radaway_flush", () -> new RadawayItem(500, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADX = registerItem("radx", () -> new RadXItem(new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> STAMP_BOOK = registerItem("stamp_book", StampBookItem::new, HIDDEN_ITEMS);
    public static final RegistryObject<Item> PAGE_OF = registerItem("page_of_", PageItem::new, HIDDEN_ITEMS);

    static {
        HbmMaterials.ordered().forEach(HbmItems::registerMaterialSet);
        registerBriquetteItems();
        registerCasingItems();
        registerCokeItems();
        registerChunkOreItems();
        registerCircuitItems();
        registerStampItems();
        registerAnvilBlockItems();
        registerMaterialBlockItems();
        registerStoneResourceBlockItems();
        registerBasaltBlockItems();
        registerBasaltOreBlockItems();
        registerOverworldOreBlockItems();
        registerNetherOreBlockItems();
    }

    private HbmItems() {
    }

    private static void registerMaterialSet(final HbmMaterialDefinition material) {
        material.shapes().forEach(shape -> registerMaterialPart(material, shape));
    }

    private static void registerChunkOreItems() {
        for (final ChunkOreItemType type : ChunkOreItemType.values()) {
            registerSimpleItem(type.itemId(), CHUNK_ORES);
        }
    }

    private static void registerBriquetteItems() {
        for (final BriquetteItemType type : BriquetteItemType.values()) {
            registerSimpleItem(type.itemId(), BRIQUETTES);
        }
    }

    private static void registerCasingItems() {
        for (final CasingItemType type : CasingItemType.values()) {
            registerSimpleItem(type.itemId(), CASINGS);
        }
    }

    private static void registerCokeItems() {
        for (final CokeItemType type : CokeItemType.values()) {
            registerSimpleItem(type.itemId(), COKES);
        }
    }

    private static void registerCircuitItems() {
        for (final CircuitItemType type : CircuitItemType.values()) {
            registerSimpleItem(type.itemId(), CIRCUITS);
        }
    }

    private static void registerStampItems() {
        for (final StampItemType type : StampItemType.values()) {
            registerItem(type.itemId(), () -> new StampItem(type), STAMPS);
        }
    }

    private static void registerAnvilBlockItems() {
        registerBlockItem("anvil_iron", HbmBlocks.ANVIL_IRON, BLOCK_ITEMS);
        registerBlockItem("anvil_lead", HbmBlocks.ANVIL_LEAD, BLOCK_ITEMS);
        registerBlockItem("anvil_steel", HbmBlocks.ANVIL_STEEL, BLOCK_ITEMS);
        registerBlockItem("anvil_desh", HbmBlocks.ANVIL_DESH, BLOCK_ITEMS);
        registerBlockItem("anvil_ferrouranium", HbmBlocks.ANVIL_FERRORANIUM, BLOCK_ITEMS);
        registerBlockItem("anvil_saturnite", HbmBlocks.ANVIL_SATURNITE, BLOCK_ITEMS);
        registerBlockItem("anvil_bismuth_bronze", HbmBlocks.ANVIL_BISMUTH_BRONZE, BLOCK_ITEMS);
        registerBlockItem("anvil_arsenic_bronze", HbmBlocks.ANVIL_ARSENIC_BRONZE, BLOCK_ITEMS);
        registerBlockItem("anvil_schrabidate", HbmBlocks.ANVIL_SCHRABIDATE, BLOCK_ITEMS);
        registerBlockItem("anvil_dnt", HbmBlocks.ANVIL_DNT, BLOCK_ITEMS);
        registerBlockItem("anvil_osmiridium", HbmBlocks.ANVIL_OSMIRIDIUM, BLOCK_ITEMS);
        registerBlockItem("anvil_murky", HbmBlocks.ANVIL_MURKY, BLOCK_ITEMS);
    }

    private static void registerMaterialBlockItems() {
        for (final MaterialBlockType type : MaterialBlockType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getMaterialBlock(type), BLOCK_ITEMS);
        }
    }

    private static void registerStoneResourceBlockItems() {
        registerBlockItem("gas_asbestos", HbmBlocks.GAS_ASBESTOS, BLOCK_ITEMS);
        registerBlockItem("sellafield_slaked", HbmBlocks.SELLAFIELD_SLAKED, BLOCK_ITEMS);
        registerBlockItem("waste_log", HbmBlocks.WASTE_LOG, BLOCK_ITEMS);
        registerBlockItem("waste_planks", HbmBlocks.WASTE_PLANKS, BLOCK_ITEMS);
        for (final StoneResourceType type : StoneResourceType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getStoneResource(type), BLOCK_ITEMS);
        }
    }

    private static void registerBasaltBlockItems() {
        for (final BasaltBlockType type : BasaltBlockType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getBasaltBlock(type), BLOCK_ITEMS);
        }
    }

    private static void registerBasaltOreBlockItems() {
        for (final BasaltOreType type : BasaltOreType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getBasaltOre(type), BLOCK_ITEMS);
        }

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getSellafieldOre(type), BLOCK_ITEMS);
        }
    }

    private static void registerOverworldOreBlockItems() {
        for (final OverworldOreType type : OverworldOreType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getOverworldOre(type), BLOCK_ITEMS);
        }
    }

    private static void registerNetherOreBlockItems() {
        for (final NetherOreType type : NetherOreType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getNetherOre(type), BLOCK_ITEMS);
        }
    }

    private static Item canisterEmptyItem() {
        return Objects.requireNonNull(CANISTER_EMPTY.get());
    }

    private static Item canisterFullItem() {
        return Objects.requireNonNull(CANISTER_FULL.get());
    }

    private static Item disperserCanisterEmptyItem() {
        return Objects.requireNonNull(DISPERSER_CANISTER_EMPTY.get());
    }

    private static Item disperserCanisterItem() {
        return Objects.requireNonNull(DISPERSER_CANISTER.get());
    }

    private static Item glyphidGlandEmptyItem() {
        return Objects.requireNonNull(GLYPHID_GLAND_EMPTY.get());
    }

    private static Item glyphidGlandItem() {
        return Objects.requireNonNull(GLYPHID_GLAND.get());
    }

    private static Item gasEmptyItem() {
        return Objects.requireNonNull(GAS_EMPTY.get());
    }

    private static Item gasFullItem() {
        return Objects.requireNonNull(GAS_FULL.get());
    }

    private static Item fluidTankEmptyItem() {
        return Objects.requireNonNull(FLUID_TANK_EMPTY.get());
    }

    private static Item fluidTankFullItem() {
        return Objects.requireNonNull(FLUID_TANK_FULL.get());
    }

    private static Item fluidTankLeadEmptyItem() {
        return Objects.requireNonNull(FLUID_TANK_LEAD_EMPTY.get());
    }

    private static Item fluidTankLeadFullItem() {
        return Objects.requireNonNull(FLUID_TANK_LEAD_FULL.get());
    }

    private static Item fluidBarrelEmptyItem() {
        return Objects.requireNonNull(FLUID_BARREL_EMPTY.get());
    }

    private static Item fluidBarrelFullItem() {
        return Objects.requireNonNull(FLUID_BARREL_FULL.get());
    }

    private static Item fluidPackEmptyItem() {
        return Objects.requireNonNull(FLUID_PACK_EMPTY.get());
    }

    private static Item fluidPackFullItem() {
        return Objects.requireNonNull(FLUID_PACK_FULL.get());
    }

    private static long rtgLifespan(final float halfLife, final RtgPelletItem.HalfLifeType type) {
        return (long) (RtgPelletItem.getLifespanFromHalfLife(halfLife, type, false) * 1.5D);
    }

    private static RegistryObject<Item> registerMaterialPart(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        final String id = material.itemId(shape);
        final RegistryObject<Item> registryObject = ITEMS.register(id, () -> new MaterialPartItem(material, shape, new Item.Properties()));
        MATERIAL_PARTS.put(id, registryObject);
        return registryObject;
    }

    private static RegistryObject<Item> registerSimpleItem(final String id, final Map<String, RegistryObject<Item>> targetMap) {
        final RegistryObject<Item> registryObject = ITEMS.register(id, () -> new Item(new Item.Properties()));
        targetMap.put(id, registryObject);
        return registryObject;
    }

    private static RegistryObject<Item> registerItem(final String id, final java.util.function.Supplier<? extends Item> supplier,
                                                     final Map<String, RegistryObject<Item>> targetMap) {
        final RegistryObject<Item> registryObject = ITEMS.register(id, supplier);
        targetMap.put(id, registryObject);
        return registryObject;
    }

    private static RegistryObject<Item> registerBlockItem(final String id, final RegistryObject<Block> block, final Map<String, RegistryObject<Item>> targetMap) {
        final RegistryObject<Item> registryObject = ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
        targetMap.put(id, registryObject);
        return registryObject;
    }

    public static Collection<RegistryObject<Item>> creativeTabEntries() {
        final List<RegistryObject<Item>> entries = new ArrayList<>(MATERIAL_PARTS.values());
        entries.addAll(BRIQUETTES.values());
        entries.addAll(CASINGS.values());
        entries.addAll(COKES.values());
        entries.addAll(CHUNK_ORES.values());
        entries.addAll(CIRCUITS.values());
        entries.addAll(STAMPS.values());
        entries.addAll(SIMPLE_ITEMS.values());
        entries.addAll(BLOCK_ITEMS.values());
        return Collections.unmodifiableList(entries);
    }

    public static RegistryObject<Item> getMaterialPart(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        final RegistryObject<Item> registryObject = MATERIAL_PARTS.get(material.itemId(shape));
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown material part: " + material.itemId(shape));
        }
        return registryObject;
    }

    public static RegistryObject<Item> getChunkOre(final ChunkOreItemType type) {
        final RegistryObject<Item> registryObject = CHUNK_ORES.get(type.itemId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown chunk ore item: " + type.itemId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getBriquette(final BriquetteItemType type) {
        final RegistryObject<Item> registryObject = BRIQUETTES.get(type.itemId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown briquette item: " + type.itemId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getCasing(final CasingItemType type) {
        final RegistryObject<Item> registryObject = CASINGS.get(type.itemId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown casing item: " + type.itemId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getCoke(final CokeItemType type) {
        final RegistryObject<Item> registryObject = COKES.get(type.itemId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown coke item: " + type.itemId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getCircuit(final CircuitItemType type) {
        final RegistryObject<Item> registryObject = CIRCUITS.get(type.itemId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown circuit item: " + type.itemId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getStamp(final StampItemType type) {
        final RegistryObject<Item> registryObject = STAMPS.get(type.itemId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown stamp item: " + type.itemId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getSimpleItem(final String id) {
        final RegistryObject<Item> registryObject = SIMPLE_ITEMS.get(id);
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown simple item: " + id);
        }
        return registryObject;
    }

    public static RegistryObject<Item> getStoneResourceBlockItem(final StoneResourceType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown stone resource block item: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getBasaltBlockItem(final BasaltBlockType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt block item: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getMaterialBlockItem(final MaterialBlockType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown material block item: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getBasaltOreBlockItem(final BasaltOreType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt ore block item: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getOverworldOreBlockItem(final OverworldOreType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown overworld ore block item: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getNetherOreBlockItem(final NetherOreType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown nether ore block item: " + type.blockId());
        }
        return registryObject;
    }
}

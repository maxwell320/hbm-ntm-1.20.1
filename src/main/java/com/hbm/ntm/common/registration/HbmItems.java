package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BarrelType;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.item.BarrelBlockItem;
import com.hbm.ntm.common.item.BriquetteItemType;
import com.hbm.ntm.common.item.BatteryBlockItem;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.CasingItemType;
import com.hbm.ntm.common.item.CokeItemType;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.item.DosimeterItem;
import com.hbm.ntm.common.item.FluidIdentifierItem;
import com.hbm.ntm.common.item.GeigerCounterItem;
import com.hbm.ntm.common.item.MaterialPartItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.item.PageItem;
import com.hbm.ntm.common.item.RadXItem;
import com.hbm.ntm.common.item.RadawayItem;
import com.hbm.ntm.common.item.SellafieldBlockItem;
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
    public static final RegistryObject<Item> DUCTTAPE = registerSimpleItem("ducttape", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FUSE = registerSimpleItem("fuse", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SAFETY_FUSE = registerSimpleItem("safety_fuse", SIMPLE_ITEMS);
    public static final RegistryObject<Item> BATTERY_POTATO = registerItem("battery_potato", () -> new BatteryItem(1_000, 0, 100, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> COIL_COPPER_TORUS = registerSimpleItem("coil_copper_torus", SIMPLE_ITEMS);
    public static final RegistryObject<Item> MOTOR = registerSimpleItem("motor", SIMPLE_ITEMS);
    public static final RegistryObject<Item> MOTOR_DESH = registerSimpleItem("motor_desh", SIMPLE_ITEMS);
    public static final RegistryObject<Item> TANK_STEEL = registerSimpleItem("tank_steel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> COIL_ADVANCED_TORUS = registerSimpleItem("coil_advanced_torus", SIMPLE_ITEMS);
    public static final RegistryObject<Item> COIL_GOLD_TORUS = registerSimpleItem("coil_gold_torus", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PHOTO_PANEL = registerSimpleItem("photo_panel", SIMPLE_ITEMS);
    public static final RegistryObject<Item> PIN = registerSimpleItem("pin", SIMPLE_ITEMS);
    public static final RegistryObject<Item> CATALYST_CLAY = registerSimpleItem("catalyst_clay", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DEUTERIUM_FILTER = registerSimpleItem("deuterium_filter", SIMPLE_ITEMS);
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
    public static final RegistryObject<Item> FLYWHEEL_BERYLLIUM = registerSimpleItem("flywheel_beryllium", SIMPLE_ITEMS);
    public static final RegistryObject<Item> RING_STARMETAL = registerSimpleItem("ring_starmetal", SIMPLE_ITEMS);
    public static final RegistryObject<Item> SAWBLADE = registerSimpleItem("sawblade", SIMPLE_ITEMS);
    public static final RegistryObject<Item> NUGGET_MERCURY_TINY = registerSimpleItem("nugget_mercury_tiny", SIMPLE_ITEMS);
    public static final RegistryObject<Item> NUGGET_MERCURY = registerSimpleItem("nugget_mercury", SIMPLE_ITEMS);
    public static final RegistryObject<Item> INGOT_MERCURY = NUGGET_MERCURY;
    public static final RegistryObject<Item> BOTTLE_MERCURY = registerItem("bottle_mercury", () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FALLOUT = registerSimpleItem("fallout", SIMPLE_ITEMS);
    public static final RegistryObject<Item> POWDER_SAWDUST = registerSimpleItem("powder_sawdust", SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_MUFFLER = registerSimpleItem("upgrade_muffler", SIMPLE_ITEMS);
    public static final RegistryObject<Item> UPGRADE_TEMPLATE = registerSimpleItem("upgrade_template", SIMPLE_ITEMS);
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
    public static final RegistryObject<Item> UNDEFINED = registerItem("undefined", UndefinedItem::new, SIMPLE_ITEMS);
    public static final RegistryObject<Item> FALLOUT_LAYER = registerBlockItem("fallout_layer", HbmBlocks.FALLOUT, BLOCK_ITEMS);
    public static final RegistryObject<Item> CREATIVE_ENERGY_SOURCE = registerBlockItem("creative_energy_source", HbmBlocks.CREATIVE_ENERGY_SOURCE, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_BATTERY = registerItem("machine_battery", () -> new BatteryBlockItem(HbmBlocks.MACHINE_BATTERY.get(), new Item.Properties()), HIDDEN_ITEMS);
    public static final RegistryObject<Item> RED_CABLE = registerBlockItem("red_cable", HbmBlocks.RED_CABLE, BLOCK_ITEMS);
    public static final RegistryObject<Item> RED_CABLE_CLASSIC = registerBlockItem("red_cable_classic", HbmBlocks.RED_CABLE_CLASSIC, BLOCK_ITEMS);
    public static final RegistryObject<Item> FLUID_DUCT_NEO = registerBlockItem("fluid_duct_neo", HbmBlocks.FLUID_DUCT_NEO, BLOCK_ITEMS);
    public static final RegistryObject<Item> GEIGER = registerBlockItem("geiger", HbmBlocks.GEIGER, BLOCK_ITEMS);
    public static final RegistryObject<Item> MACHINE_PRESS = registerBlockItem("machine_press", HbmBlocks.MACHINE_PRESS, BLOCK_ITEMS);
    public static final RegistryObject<Item> PRESS_PREHEATER = registerBlockItem("press_preheater", HbmBlocks.PRESS_PREHEATER, BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_PLASTIC = registerItem("barrel_plastic", () -> new BarrelBlockItem(HbmBlocks.BARREL_PLASTIC.get(), BarrelType.PLASTIC, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_CORRODED = registerItem("barrel_corroded", () -> new BarrelBlockItem(HbmBlocks.BARREL_CORRODED.get(), BarrelType.CORRODED, new Item.Properties()), HIDDEN_ITEMS);
    public static final RegistryObject<Item> BARREL_IRON = registerItem("barrel_iron", () -> new BarrelBlockItem(HbmBlocks.BARREL_IRON.get(), BarrelType.IRON, new Item.Properties()), HIDDEN_ITEMS);
    public static final RegistryObject<Item> BARREL_STEEL = registerItem("barrel_steel", () -> new BarrelBlockItem(HbmBlocks.BARREL_STEEL.get(), BarrelType.STEEL, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_TCALLOY = registerItem("barrel_tcalloy", () -> new BarrelBlockItem(HbmBlocks.BARREL_TCALLOY.get(), BarrelType.TCALLOY, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> BARREL_ANTIMATTER = registerItem("barrel_antimatter", () -> new BarrelBlockItem(HbmBlocks.BARREL_ANTIMATTER.get(), BarrelType.ANTIMATTER, new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> SELLAFIELD = registerItem("sellafield", () -> new SellafieldBlockItem(HbmBlocks.SELLAFIELD.get(), new Item.Properties()), BLOCK_ITEMS);
    public static final RegistryObject<Item> GEIGER_COUNTER = registerItem("geiger_counter", () -> new GeigerCounterItem(new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> GEM_RAD = registerSimpleItem("gem_rad", SIMPLE_ITEMS);
    public static final RegistryObject<Item> IV_EMPTY = registerSimpleItem("iv_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> CRT_DISPLAY = registerSimpleItem("crt_display", SIMPLE_ITEMS);
    public static final RegistryObject<Item> DOSIMETER = registerItem("dosimeter", () -> new DosimeterItem(new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> FLUID_IDENTIFIER_MULTI = registerItem("fluid_identifier_multi", () -> new FluidIdentifierItem(new Item.Properties()), SIMPLE_ITEMS);
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
}

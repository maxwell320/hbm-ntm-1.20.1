package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.AssemblyMachineBlock;
import com.hbm.ntm.common.block.AshpitBlock;
import com.hbm.ntm.common.block.BarrelBlock;
import com.hbm.ntm.common.block.BarrelType;
import com.hbm.ntm.common.block.BatteryBlock;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreBlock;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.CableBlock;
import com.hbm.ntm.common.block.CentrifugeBlock;
import com.hbm.ntm.common.block.ChimneyBrickBlock;
import com.hbm.ntm.common.block.ChimneyIndustrialBlock;
import com.hbm.ntm.common.block.CombustionEngineBlock;
import com.hbm.ntm.common.block.ConstantRtgBlock;
import com.hbm.ntm.common.block.CyclotronBlock;
import com.hbm.ntm.common.block.CreativeEnergySourceBlock;
import com.hbm.ntm.common.block.DieselGeneratorBlock;
import com.hbm.ntm.common.block.DiFurnaceBlock;
import com.hbm.ntm.common.block.DiFurnaceExtensionBlock;
import com.hbm.ntm.common.block.DiFurnaceRtgBlock;
import com.hbm.ntm.common.block.ElectricFurnaceBlock;
import com.hbm.ntm.common.block.ExplosiveBarrelBlock;
import com.hbm.ntm.common.block.FalloutBlock;
import com.hbm.ntm.common.block.FluidDuctBlock;
import com.hbm.ntm.common.block.GasCentrifugeBlock;
import com.hbm.ntm.common.block.GasAsbestosBlock;
import com.hbm.ntm.common.block.GeigerCounterBlock;
import com.hbm.ntm.common.block.IcfBlock;
import com.hbm.ntm.common.block.IcfControllerBlock;
import com.hbm.ntm.common.block.IcfLaserComponentBlock;
import com.hbm.ntm.common.block.IcfPressBlock;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.NetherOreBlock;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.NtmAnvilBlock;
import com.hbm.ntm.common.block.OverworldOreBlock;
import com.hbm.ntm.common.block.OverworldOreType;
import com.hbm.ntm.common.block.PressBlock;
import com.hbm.ntm.common.block.PurexBlock;
import com.hbm.ntm.common.block.RtgGeneratorBlock;
import com.hbm.ntm.common.block.RtgFurnaceBlock;
import com.hbm.ntm.common.block.SellafieldBlock;
import com.hbm.ntm.common.block.ShredderBlock;
import com.hbm.ntm.common.block.SolderingStationBlock;
import com.hbm.ntm.common.block.SellafieldOreBlock;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.SellafieldSlakedBlock;
import com.hbm.ntm.common.block.StoneResourceBlock;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.block.SteelGrateBlock;
import com.hbm.ntm.common.block.WasteBarrelBlock;
import com.hbm.ntm.common.block.WasteLogBlock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("null")
public final class HbmBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HbmNtmMod.MOD_ID);
    private static final Map<String, RegistryObject<Block>> BASALT_BLOCKS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> BASALT_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> MATERIAL_BLOCKS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> OVERWORLD_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> NETHER_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> SELLAFIELD_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> STONE_RESOURCES = new LinkedHashMap<>();

    public static final RegistryObject<Block> GAS_ASBESTOS = BLOCKS.register("gas_asbestos",
        () -> new GasAsbestosBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.AIR).replaceable().noCollission().noOcclusion().randomTicks())));
    public static final RegistryObject<Block> FALLOUT = BLOCKS.register("fallout", FalloutBlock::new);
    public static final RegistryObject<Block> GEIGER = BLOCKS.register("geiger",
        () -> new GeigerCounterBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(15.0F, 0.25F).noOcclusion())));
    public static final RegistryObject<Block> CREATIVE_ENERGY_SOURCE = BLOCKS.register("creative_energy_source",
        () -> new CreativeEnergySourceBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops().lightLevel(state -> 15))));
    public static final RegistryObject<Block> MACHINE_BATTERY = BLOCKS.register("machine_battery",
        () -> new BatteryBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> STEEL_BEAM = BLOCKS.register("steel_beam",
        () -> new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> STEEL_GRATE = BLOCKS.register("steel_grate",
        () -> new SteelGrateBlock(false, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> STEEL_GRATE_WIDE = BLOCKS.register("steel_grate_wide",
        () -> new SteelGrateBlock(true, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> RED_CABLE = BLOCKS.register("red_cable",
        () -> new CableBlock(20_000, 20_000, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> RED_CABLE_CLASSIC = BLOCKS.register("red_cable_classic",
        () -> new CableBlock(20_000, 20_000, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> FLUID_DUCT_NEO = BLOCKS.register("fluid_duct_neo",
        () -> new FluidDuctBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> GLASS_BORON = BLOCKS.register("glass_boron",
        () -> new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(0.3F).noOcclusion())));
    public static final RegistryObject<Block> MACHINE_PRESS = BLOCKS.register("machine_press",
        () -> new PressBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_ASSEMBLY_MACHINE = BLOCKS.register("machine_assembly_machine",
        () -> new AssemblyMachineBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_SOLDERING_STATION = BLOCKS.register("machine_soldering_station",
        () -> new SolderingStationBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 30.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_DI_FURNACE = BLOCKS.register("machine_difurnace",
        () -> new DiFurnaceBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_DI_FURNACE_EXTENSION = BLOCKS.register("machine_difurnace_extension",
        () -> new DiFurnaceExtensionBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> MACHINE_DI_FURNACE_RTG = BLOCKS.register("machine_difurnace_rtg",
        () -> new DiFurnaceRtgBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_ELECTRIC_FURNACE = BLOCKS.register("machine_electric_furnace",
        () -> new ElectricFurnaceBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_RTG_FURNACE = BLOCKS.register("machine_rtg_furnace",
        () -> new RtgFurnaceBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_RTG_GREY = BLOCKS.register("machine_rtg_grey",
        () -> new RtgGeneratorBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_DIESEL_GENERATOR = BLOCKS.register("machine_diesel",
        () -> new DieselGeneratorBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_COMBUSTION_ENGINE = BLOCKS.register("machine_combustion",
        () -> new CombustionEngineBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_ASHPIT = BLOCKS.register("machine_ashpit",
        () -> new AshpitBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> CHIMNEY_BRICK = BLOCKS.register("chimney_brick",
        () -> new ChimneyBrickBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 100.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> CHIMNEY_INDUSTRIAL = BLOCKS.register("chimney_industrial",
        () -> new ChimneyIndustrialBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 100.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> MACHINE_MINI_RTG = BLOCKS.register("machine_minirtg",
        () -> new ConstantRtgBlock(false, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_POWER_RTG = BLOCKS.register("machine_powerrtg",
        () -> new ConstantRtgBlock(true, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_SHREDDER = BLOCKS.register("machine_shredder",
        () -> new ShredderBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_CENTRIFUGE = BLOCKS.register("machine_centrifuge",
        () -> new CentrifugeBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_GAS_CENTRIFUGE = BLOCKS.register("machine_gascent",
        () -> new GasCentrifugeBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_CYCLOTRON = BLOCKS.register("machine_cyclotron",
        () -> new CyclotronBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_PUREX = BLOCKS.register("machine_purex",
        () -> new PurexBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_ICF = BLOCKS.register("machine_icf",
        () -> new IcfBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 60.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_ICF_CONTROLLER = BLOCKS.register("machine_icf_controller",
        () -> new IcfControllerBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 60.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_ICF_LASER_COMPONENT = BLOCKS.register("machine_icf_laser_component",
        () -> new IcfLaserComponentBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 60.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> MACHINE_ICF_PRESS = BLOCKS.register("machine_icf_press",
        () -> new IcfPressBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 60.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> PRESS_PREHEATER = BLOCKS.register("press_preheater",
        () -> new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> BARREL_PLASTIC = BLOCKS.register("barrel_plastic",
        () -> new BarrelBlock(BarrelType.PLASTIC, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_CORRODED = BLOCKS.register("barrel_corroded",
        () -> new BarrelBlock(BarrelType.CORRODED, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_IRON = BLOCKS.register("barrel_iron",
        () -> new BarrelBlock(BarrelType.IRON, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_STEEL = BLOCKS.register("barrel_steel",
        () -> new BarrelBlock(BarrelType.STEEL, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_TCALLOY = BLOCKS.register("barrel_tcalloy",
        () -> new BarrelBlock(BarrelType.TCALLOY, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_ANTIMATTER = BLOCKS.register("barrel_antimatter",
        () -> new BarrelBlock(BarrelType.ANTIMATTER, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0F, 5.0F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_RED = BLOCKS.register("barrel_red",
        () -> new ExplosiveBarrelBlock(ExplosiveBarrelBlock.ExplosiveBarrelType.RED, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5F, 2.5F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_PINK = BLOCKS.register("barrel_pink",
        () -> new ExplosiveBarrelBlock(ExplosiveBarrelBlock.ExplosiveBarrelType.PINK, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5F, 2.5F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_LOX = BLOCKS.register("barrel_lox",
        () -> new ExplosiveBarrelBlock(ExplosiveBarrelBlock.ExplosiveBarrelType.LOX, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5F, 2.5F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_TAINT = BLOCKS.register("barrel_taint",
        () -> new ExplosiveBarrelBlock(ExplosiveBarrelBlock.ExplosiveBarrelType.TAINT, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5F, 2.5F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_YELLOW = BLOCKS.register("barrel_yellow",
        () -> new WasteBarrelBlock(WasteBarrelBlock.WasteBarrelType.YELLOW, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5F, 2.5F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> BARREL_VITRIFIED = BLOCKS.register("barrel_vitrified",
        () -> new WasteBarrelBlock(WasteBarrelBlock.WasteBarrelType.VITRIFIED, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5F, 2.5F).requiresCorrectToolForDrops().noOcclusion())));
    public static final RegistryObject<Block> ANVIL_IRON = BLOCKS.register("anvil_iron",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_IRON, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_LEAD = BLOCKS.register("anvil_lead",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_IRON, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_STEEL = BLOCKS.register("anvil_steel",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_STEEL, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_DESH = BLOCKS.register("anvil_desh",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_DESH, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_FERRORANIUM = BLOCKS.register("anvil_ferrouranium",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_FERRORANIUM, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_SATURNITE = BLOCKS.register("anvil_saturnite",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_RBMK, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_BISMUTH_BRONZE = BLOCKS.register("anvil_bismuth_bronze",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_RBMK, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_ARSENIC_BRONZE = BLOCKS.register("anvil_arsenic_bronze",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_RBMK, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_SCHRABIDATE = BLOCKS.register("anvil_schrabidate",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_SCHRABIDATE, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_DNT = BLOCKS.register("anvil_dnt",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_DNT, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_OSMIRIDIUM = BLOCKS.register("anvil_osmiridium",
        () -> new NtmAnvilBlock(NtmAnvilBlock.TIER_OSMIRIDIUM, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ANVIL_MURKY = BLOCKS.register("anvil_murky",
        () -> new NtmAnvilBlock(1916169, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(5.0F, 100.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> SELLAFIELD = BLOCKS.register("sellafield",
        () -> new SellafieldBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5.0F, 6.0F).requiresCorrectToolForDrops().randomTicks())));
    public static final RegistryObject<Block> SELLAFIELD_SLAKED = BLOCKS.register("sellafield_slaked",
        () -> new SellafieldSlakedBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5.0F, 6.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ORE_SELLAFIELD_DIAMOND = registerSellafieldOre(SellafieldOreType.DIAMOND);
    public static final RegistryObject<Block> ORE_SELLAFIELD_EMERALD = registerSellafieldOre(SellafieldOreType.EMERALD);
    public static final RegistryObject<Block> ORE_SELLAFIELD_URANIUM_SCORCHED = registerSellafieldOre(SellafieldOreType.URANIUM_SCORCHED);
    public static final RegistryObject<Block> ORE_SELLAFIELD_SCHRABIDIUM = registerSellafieldOre(SellafieldOreType.SCHRABIDIUM);
    public static final RegistryObject<Block> ORE_SELLAFIELD_RADGEM = registerSellafieldOre(SellafieldOreType.RADGEM);
    public static final RegistryObject<Block> WASTE_LOG = BLOCKS.register("waste_log",
        () -> new WasteLogBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(5.0F, 2.5F))));
    public static final RegistryObject<Block> WASTE_PLANKS = BLOCKS.register("waste_planks",
        () -> new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5F, 2.5F))));
    public static final RegistryObject<Block> BLOCK_STEEL = registerMaterialBlock(MaterialBlockType.STEEL);
    public static final RegistryObject<Block> BLOCK_BERYLLIUM = registerMaterialBlock(MaterialBlockType.BERYLLIUM);
    public static final RegistryObject<Block> BLOCK_LEAD = registerMaterialBlock(MaterialBlockType.LEAD);
    public static final RegistryObject<Block> BLOCK_URANIUM = registerMaterialBlock(MaterialBlockType.URANIUM);
    public static final RegistryObject<Block> BLOCK_TITANIUM = registerMaterialBlock(MaterialBlockType.TITANIUM);
    public static final RegistryObject<Block> BLOCK_COPPER = registerMaterialBlock(MaterialBlockType.COPPER);
    public static final RegistryObject<Block> BLOCK_TUNGSTEN = registerMaterialBlock(MaterialBlockType.TUNGSTEN);
    public static final RegistryObject<Block> BLOCK_ALUMINIUM = registerMaterialBlock(MaterialBlockType.ALUMINIUM);
    public static final RegistryObject<Block> BLOCK_RED_COPPER = registerMaterialBlock(MaterialBlockType.RED_COPPER);
    public static final RegistryObject<Block> BLOCK_ADVANCED_ALLOY = registerMaterialBlock(MaterialBlockType.ADVANCED_ALLOY);
    public static final RegistryObject<Block> BLOCK_THORIUM = registerMaterialBlock(MaterialBlockType.THORIUM);
    public static final RegistryObject<Block> BLOCK_PLUTONIUM = registerMaterialBlock(MaterialBlockType.PLUTONIUM);
    public static final RegistryObject<Block> BLOCK_NEPTUNIUM = registerMaterialBlock(MaterialBlockType.NEPTUNIUM);
    public static final RegistryObject<Block> BLOCK_POLONIUM = registerMaterialBlock(MaterialBlockType.POLONIUM);
    public static final RegistryObject<Block> BLOCK_TCALLOY = registerMaterialBlock(MaterialBlockType.TCALLOY);
    public static final RegistryObject<Block> BLOCK_CDALLOY = registerMaterialBlock(MaterialBlockType.CDALLOY);
    public static final RegistryObject<Block> BLOCK_AUSTRALIUM = registerMaterialBlock(MaterialBlockType.AUSTRALIUM);
    public static final RegistryObject<Block> BLOCK_SCHRABIDIUM = registerMaterialBlock(MaterialBlockType.SCHRABIDIUM);
    public static final RegistryObject<Block> BLOCK_SCHRARANIUM = registerMaterialBlock(MaterialBlockType.SCHRARANIUM);
    public static final RegistryObject<Block> BLOCK_SCHRABIDATE = registerMaterialBlock(MaterialBlockType.SCHRABIDATE);
    public static final RegistryObject<Block> BLOCK_SOLINIUM = registerMaterialBlock(MaterialBlockType.SOLINIUM);
    public static final RegistryObject<Block> BLOCK_MAGNETIZED_TUNGSTEN = registerMaterialBlock(MaterialBlockType.MAGNETIZED_TUNGSTEN);
    public static final RegistryObject<Block> BLOCK_COMBINE_STEEL = registerMaterialBlock(MaterialBlockType.COMBINE_STEEL);
    public static final RegistryObject<Block> BLOCK_DESH = registerMaterialBlock(MaterialBlockType.DESH);
    public static final RegistryObject<Block> BLOCK_DURA_STEEL = registerMaterialBlock(MaterialBlockType.DURA_STEEL);
    public static final RegistryObject<Block> BLOCK_STARMETAL = registerMaterialBlock(MaterialBlockType.STARMETAL);
    public static final RegistryObject<Block> BLOCK_EUPHEMIUM = registerMaterialBlock(MaterialBlockType.EUPHEMIUM);
    public static final RegistryObject<Block> BLOCK_DINEUTRONIUM = registerMaterialBlock(MaterialBlockType.DINEUTRONIUM);
    public static final RegistryObject<Block> BLOCK_YELLOWCAKE = registerMaterialBlock(MaterialBlockType.YELLOWCAKE);
    public static final RegistryObject<Block> BLOCK_COBALT = registerMaterialBlock(MaterialBlockType.COBALT);
    public static final RegistryObject<Block> BLOCK_LITHIUM = registerMaterialBlock(MaterialBlockType.LITHIUM);
    public static final RegistryObject<Block> BLOCK_BISMUTH = registerMaterialBlock(MaterialBlockType.BISMUTH);
    public static final RegistryObject<Block> BLOCK_ZIRCONIUM = registerMaterialBlock(MaterialBlockType.ZIRCONIUM);
    public static final RegistryObject<Block> BLOCK_POLYMER = registerMaterialBlock(MaterialBlockType.POLYMER);
    public static final RegistryObject<Block> BLOCK_BAKELITE = registerMaterialBlock(MaterialBlockType.BAKELITE);
    public static final RegistryObject<Block> BLOCK_RUBBER = registerMaterialBlock(MaterialBlockType.RUBBER);
    public static final RegistryObject<Block> BLOCK_ASBESTOS = registerMaterialBlock(MaterialBlockType.ASBESTOS);
    public static final RegistryObject<Block> BLOCK_FIBERGLASS = registerMaterialBlock(MaterialBlockType.FIBERGLASS);
    public static final RegistryObject<Block> BASALT = registerBasaltBlock(BasaltBlockType.BASALT);
    public static final RegistryObject<Block> BASALT_SMOOTH = registerBasaltBlock(BasaltBlockType.BASALT_SMOOTH);
    public static final RegistryObject<Block> BASALT_POLISHED = registerBasaltBlock(BasaltBlockType.BASALT_POLISHED);
    public static final RegistryObject<Block> BASALT_BRICK = registerBasaltBlock(BasaltBlockType.BASALT_BRICK);
    public static final RegistryObject<Block> BASALT_TILES = registerBasaltBlock(BasaltBlockType.BASALT_TILES);
    public static final RegistryObject<Block> ORE_BASALT_SULFUR = registerBasaltOre(BasaltOreType.SULFUR);
    public static final RegistryObject<Block> ORE_BASALT_FLUORITE = registerBasaltOre(BasaltOreType.FLUORITE);
    public static final RegistryObject<Block> ORE_BASALT_ASBESTOS = registerBasaltOre(BasaltOreType.ASBESTOS);
    public static final RegistryObject<Block> ORE_BASALT_GEM = registerBasaltOre(BasaltOreType.GEM);
    public static final RegistryObject<Block> ORE_BASALT_MOLYSITE = registerBasaltOre(BasaltOreType.MOLYSITE);
    public static final RegistryObject<Block> STONE_RESOURCE_SULFUR = registerStoneResource(StoneResourceType.SULFUR);
    public static final RegistryObject<Block> STONE_RESOURCE_ASBESTOS = registerStoneResource(StoneResourceType.ASBESTOS);
    public static final RegistryObject<Block> STONE_RESOURCE_LIMESTONE = registerStoneResource(StoneResourceType.LIMESTONE);
    public static final RegistryObject<Block> STONE_RESOURCE_BAUXITE = registerStoneResource(StoneResourceType.BAUXITE);
    public static final RegistryObject<Block> STONE_RESOURCE_HEMATITE = registerStoneResource(StoneResourceType.HEMATITE);
    public static final RegistryObject<Block> STONE_RESOURCE_MALACHITE = registerStoneResource(StoneResourceType.MALACHITE);
    public static final RegistryObject<Block> ORE_URANIUM = registerOverworldOre(OverworldOreType.URANIUM);
    public static final RegistryObject<Block> ORE_URANIUM_SCORCHED = registerOverworldOre(OverworldOreType.URANIUM_SCORCHED);
    public static final RegistryObject<Block> ORE_TITANIUM = registerOverworldOre(OverworldOreType.TITANIUM);
    public static final RegistryObject<Block> ORE_THORIUM = registerOverworldOre(OverworldOreType.THORIUM);
    public static final RegistryObject<Block> ORE_NITER = registerOverworldOre(OverworldOreType.NITER);
    public static final RegistryObject<Block> ORE_COPPER = registerOverworldOre(OverworldOreType.COPPER);
    public static final RegistryObject<Block> ORE_TUNGSTEN = registerOverworldOre(OverworldOreType.TUNGSTEN);
    public static final RegistryObject<Block> ORE_ALUMINIUM = registerOverworldOre(OverworldOreType.ALUMINIUM);
    public static final RegistryObject<Block> ORE_FLUORITE = registerOverworldOre(OverworldOreType.FLUORITE);
    public static final RegistryObject<Block> ORE_LEAD = registerOverworldOre(OverworldOreType.LEAD);
    public static final RegistryObject<Block> ORE_SCHRABIDIUM = registerOverworldOre(OverworldOreType.SCHRABIDIUM);
    public static final RegistryObject<Block> ORE_BERYLLIUM = registerOverworldOre(OverworldOreType.BERYLLIUM);
    public static final RegistryObject<Block> ORE_AUSTRALIUM = registerOverworldOre(OverworldOreType.AUSTRALIUM);
    public static final RegistryObject<Block> ORE_RARE = registerOverworldOre(OverworldOreType.RARE);
    public static final RegistryObject<Block> ORE_COBALT = registerOverworldOre(OverworldOreType.COBALT);
    public static final RegistryObject<Block> ORE_CINNEBAR = registerOverworldOre(OverworldOreType.CINNEBAR);
    public static final RegistryObject<Block> ORE_COLTAN = registerOverworldOre(OverworldOreType.COLTAN);
    public static final RegistryObject<Block> ORE_ALEXANDRITE = registerOverworldOre(OverworldOreType.ALEXANDRITE);
    public static final RegistryObject<Block> ORE_LIGNITE = registerOverworldOre(OverworldOreType.LIGNITE);
    public static final RegistryObject<Block> ORE_ASBESTOS = registerOverworldOre(OverworldOreType.ASBESTOS);
    public static final RegistryObject<Block> ORE_NETHER_COAL = registerNetherOre(NetherOreType.COAL);
    public static final RegistryObject<Block> ORE_NETHER_SMOLDERING = registerNetherOre(NetherOreType.SMOLDERING);
    public static final RegistryObject<Block> ORE_NETHER_URANIUM = registerNetherOre(NetherOreType.URANIUM);
    public static final RegistryObject<Block> ORE_NETHER_URANIUM_SCORCHED = registerNetherOre(NetherOreType.URANIUM_SCORCHED);
    public static final RegistryObject<Block> ORE_NETHER_PLUTONIUM = registerNetherOre(NetherOreType.PLUTONIUM);
    public static final RegistryObject<Block> ORE_NETHER_TUNGSTEN = registerNetherOre(NetherOreType.TUNGSTEN);
    public static final RegistryObject<Block> ORE_NETHER_SULFUR = registerNetherOre(NetherOreType.SULFUR);
    public static final RegistryObject<Block> ORE_NETHER_FIRE = registerNetherOre(NetherOreType.FIRE);
    public static final RegistryObject<Block> ORE_NETHER_COBALT = registerNetherOre(NetherOreType.COBALT);
    public static final RegistryObject<Block> ORE_NETHER_SCHRABIDIUM = registerNetherOre(NetherOreType.SCHRABIDIUM);

    private HbmBlocks() {
    }

    private static RegistryObject<Block> registerBasaltBlock(final BasaltBlockType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> type.pillar()
                ? new RotatedPillarBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(5.0F, 10.0F).requiresCorrectToolForDrops()))
                : new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
        BASALT_BLOCKS.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerMaterialBlock(final MaterialBlockType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(type.hardness(), type.resistance()).requiresCorrectToolForDrops())));
        MATERIAL_BLOCKS.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerBasaltOre(final BasaltOreType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new BasaltOreBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
        BASALT_ORES.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerSellafieldOre(final SellafieldOreType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new SellafieldOreBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5.0F, 6.0F).requiresCorrectToolForDrops())));
        SELLAFIELD_ORES.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerStoneResource(final StoneResourceType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new StoneResourceBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).requiresCorrectToolForDrops())));
        STONE_RESOURCES.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerOverworldOre(final OverworldOreType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new OverworldOreBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(type.hardness(), type.resistance()).requiresCorrectToolForDrops())));
        OVERWORLD_ORES.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerNetherOre(final NetherOreType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new NetherOreBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.NETHERRACK).strength(type.hardness(), type.resistance()).requiresCorrectToolForDrops())));
        NETHER_ORES.put(type.blockId(), registryObject);
        return registryObject;
    }

    public static RegistryObject<Block> getBasaltOre(final BasaltOreType type) {
        final RegistryObject<Block> registryObject = BASALT_ORES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt ore block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getBasaltBlock(final BasaltBlockType type) {
        final RegistryObject<Block> registryObject = BASALT_BLOCKS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getMaterialBlock(final MaterialBlockType type) {
        final RegistryObject<Block> registryObject = MATERIAL_BLOCKS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown material block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getSellafieldOre(final SellafieldOreType type) {
        final RegistryObject<Block> registryObject = SELLAFIELD_ORES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown sellafield ore block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getStoneResource(final StoneResourceType type) {
        final RegistryObject<Block> registryObject = STONE_RESOURCES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown stone resource block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getOverworldOre(final OverworldOreType type) {
        final RegistryObject<Block> registryObject = OVERWORLD_ORES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown overworld ore block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getNetherOre(final NetherOreType type) {
        final RegistryObject<Block> registryObject = NETHER_ORES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown nether ore block: " + type.blockId());
        }
        return registryObject;
    }
}

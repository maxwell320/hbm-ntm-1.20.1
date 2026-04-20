package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.assembly.AssemblyMachinePart;
import com.hbm.ntm.common.block.AssemblyMachineBlock;
import com.hbm.ntm.common.block.BatteryBlock;
import com.hbm.ntm.common.block.BrickFurnaceBlock;
import com.hbm.ntm.common.block.CentrifugeBlock;
import com.hbm.ntm.common.block.CombustionEngineBlock;
import com.hbm.ntm.common.block.CyclotronBlock;
import com.hbm.ntm.common.block.DiFurnaceBlock;
import com.hbm.ntm.common.block.DiFurnaceRtgBlock;
import com.hbm.ntm.common.block.DieselGeneratorBlock;
import com.hbm.ntm.common.block.ElectricFurnaceBlock;
import com.hbm.ntm.common.block.FurnaceCombinationBlock;
import com.hbm.ntm.common.block.FurnaceIronBlock;
import com.hbm.ntm.common.block.FurnaceSteelBlock;
import com.hbm.ntm.common.block.GasCentrifugeBlock;
import com.hbm.ntm.common.block.IcfBlock;
import com.hbm.ntm.common.block.IcfControllerBlock;
import com.hbm.ntm.common.block.IcfLaserComponentBlock;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.OverworldOreType;
import com.hbm.ntm.common.block.PressBlock;
import com.hbm.ntm.common.block.PurexBlock;
import com.hbm.ntm.common.block.RotaryFurnaceBlock;
import com.hbm.ntm.common.block.RtgFurnaceBlock;
import com.hbm.ntm.common.block.ShredderBlock;
import com.hbm.ntm.common.block.SolderingStationBlock;
import com.hbm.ntm.common.block.SteelGrateBlock;
import com.hbm.ntm.common.press.PressPart;
import com.hbm.ntm.common.rotary.RotaryFurnacePart;
import com.hbm.ntm.common.soldering.SolderingStationPart;
import com.hbm.ntm.common.registration.HbmBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

@SuppressWarnings("null")
public class HbmBlockStateProvider extends BlockStateProvider {
    private final ExistingFileHelper existingFileHelper;

    public HbmBlockStateProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, HbmNtmMod.MOD_ID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        batteryBlock(HbmBlocks.MACHINE_BATTERY.get(), "machine_battery", "battery_front_alt", "battery_side_alt", "battery_top");
        pressBlock(HbmBlocks.MACHINE_PRESS.get(), "machine_press", "machine_press");
        assemblyMachineBlock(HbmBlocks.MACHINE_ASSEMBLY_MACHINE.get(), "machine_assembly_machine");
        solderingStationBlock(HbmBlocks.MACHINE_SOLDERING_STATION.get(), "machine_soldering_station");
        diFurnaceBlock(HbmBlocks.MACHINE_DI_FURNACE.get(), "machine_difurnace");
        diFurnaceExtensionBlock(HbmBlocks.MACHINE_DI_FURNACE_EXTENSION.get(), "machine_difurnace_extension");
        diFurnaceRtgBlock(HbmBlocks.MACHINE_DI_FURNACE_RTG.get(), "machine_difurnace_rtg");
        electricFurnaceBlock(HbmBlocks.MACHINE_ELECTRIC_FURNACE.get(), "machine_electric_furnace");
        furnaceIronBlock(HbmBlocks.FURNACE_IRON.get(), "furnace_iron");
        furnaceSteelBlock(HbmBlocks.FURNACE_STEEL.get(), "furnace_steel");
        furnaceCombinationBlock(HbmBlocks.FURNACE_COMBINATION.get(), "furnace_combination");
        rotaryFurnaceBlock(HbmBlocks.MACHINE_ROTARY_FURNACE.get(), "machine_rotary_furnace");
        rtgFurnaceBlock(HbmBlocks.MACHINE_RTG_FURNACE.get(), "machine_rtg_furnace");
        simpleCubeBlock(HbmBlocks.MACHINE_RTG_GREY.get(), "machine_rtg_grey", "rtg");
        dieselGeneratorBlock(HbmBlocks.MACHINE_DIESEL_GENERATOR.get(), "machine_diesel");
        combustionEngineBlock(HbmBlocks.MACHINE_COMBUSTION_ENGINE.get(), "machine_combustion");
        brickFurnaceBlock(HbmBlocks.MACHINE_FURNACE_BRICK.get(), "machine_furnace_brick");
        existingModelBlock(HbmBlocks.MACHINE_ASHPIT.get(), "machine_ashpit");
        existingModelBlock(HbmBlocks.CHIMNEY_BRICK.get(), "chimney_brick");
        existingModelBlock(HbmBlocks.CHIMNEY_INDUSTRIAL.get(), "chimney_industrial");
        simpleCubeBlock(HbmBlocks.MACHINE_MINI_RTG.get(), "machine_minirtg", "rtg_cell");
        simpleCubeBlock(HbmBlocks.MACHINE_POWER_RTG.get(), "machine_powerrtg", "rtg_polonium");
        centrifugeBlock(HbmBlocks.MACHINE_CENTRIFUGE.get(), "machine_centrifuge");
        gasCentrifugeBlock(HbmBlocks.MACHINE_GAS_CENTRIFUGE.get(), "machine_gascent");
        cyclotronBlock(HbmBlocks.MACHINE_CYCLOTRON.get(), "machine_cyclotron");
        purexBlock(HbmBlocks.MACHINE_PUREX.get(), "machine_purex");
        icfBlock(HbmBlocks.MACHINE_ICF.get(), "machine_icf");
        icfControllerBlock(HbmBlocks.MACHINE_ICF_CONTROLLER.get(), "machine_icf_controller");
        icfLaserComponentBlock(HbmBlocks.MACHINE_ICF_LASER_COMPONENT.get(), "machine_icf_laser_component");
        icfPressBlock(HbmBlocks.MACHINE_ICF_PRESS.get(), "machine_icf_press");
        barrelBlock(HbmBlocks.BARREL_PLASTIC.get(), "barrel_plastic", "barrel_plastic");
        barrelBlock(HbmBlocks.BARREL_CORRODED.get(), "barrel_corroded", "barrel_corroded");
        barrelBlock(HbmBlocks.BARREL_IRON.get(), "barrel_iron", "barrel_iron");
        barrelBlock(HbmBlocks.BARREL_STEEL.get(), "barrel_steel", "barrel_steel");
        barrelBlock(HbmBlocks.BARREL_TCALLOY.get(), "barrel_tcalloy", "barrel_tcalloy");
        barrelBlock(HbmBlocks.BARREL_ANTIMATTER.get(), "barrel_antimatter", "barrel_antimatter");
        barrelBlock(HbmBlocks.BARREL_RED.get(), "barrel_red", "barrel_red");
        barrelBlock(HbmBlocks.BARREL_PINK.get(), "barrel_pink", "barrel_pink");
        barrelBlock(HbmBlocks.BARREL_LOX.get(), "barrel_lox", "barrel_lox");
        barrelBlock(HbmBlocks.BARREL_TAINT.get(), "barrel_taint", "barrel_taint");
        barrelBlock(HbmBlocks.BARREL_YELLOW.get(), "barrel_yellow", "barrel_yellow");
        barrelBlock(HbmBlocks.BARREL_VITRIFIED.get(), "barrel_vitrified", "barrel_vitrified");
        simpleCubeBlock(HbmBlocks.GLASS_BORON.get(), "glass_boron", "glass_boron");
        existingModelBlock(HbmBlocks.STEEL_BEAM.get(), "steel_beam");
        grateBlock(HbmBlocks.STEEL_GRATE.get(), "steel_grate", "grate_top");
        grateBlock(HbmBlocks.STEEL_GRATE_WIDE.get(), "steel_grate_wide", "grate_wide_top");
        anvilBlock(HbmBlocks.ANVIL_IRON.get(), "anvil_iron", "anvil_iron");
        anvilBlock(HbmBlocks.ANVIL_LEAD.get(), "anvil_lead", "anvil_lead");
        anvilBlock(HbmBlocks.ANVIL_STEEL.get(), "anvil_steel", "anvil_steel");
        anvilBlock(HbmBlocks.ANVIL_DESH.get(), "anvil_desh", "anvil_desh");
        anvilBlock(HbmBlocks.ANVIL_FERRORANIUM.get(), "anvil_ferrouranium", "anvil_ferrouranium");
        anvilBlock(HbmBlocks.ANVIL_SATURNITE.get(), "anvil_saturnite", "anvil_saturnite");
        anvilBlock(HbmBlocks.ANVIL_BISMUTH_BRONZE.get(), "anvil_bismuth_bronze", "anvil_bismuth_bronze");
        anvilBlock(HbmBlocks.ANVIL_ARSENIC_BRONZE.get(), "anvil_arsenic_bronze", "anvil_arsenic_bronze");
        anvilBlock(HbmBlocks.ANVIL_SCHRABIDATE.get(), "anvil_schrabidate", "anvil_schrabidate");
        anvilBlock(HbmBlocks.ANVIL_DNT.get(), "anvil_dnt", "anvil_dnt");
        anvilBlock(HbmBlocks.ANVIL_OSMIRIDIUM.get(), "anvil_osmiridium", "anvil_osmiridium");
        anvilBlock(HbmBlocks.ANVIL_MURKY.get(), "anvil_murky", "anvil_steel");
        shredderBlock(HbmBlocks.MACHINE_SHREDDER.get(), "machine_shredder");

        for (final OverworldOreType type : OverworldOreType.values()) {
            simpleCubeBlock(HbmBlocks.getOverworldOre(type).get(), type.blockId(), type.blockId());
        }

        for (final NetherOreType type : NetherOreType.values()) {
            simpleCubeBlock(HbmBlocks.getNetherOre(type).get(), type.blockId(), type.blockId());
        }

        for (final MaterialBlockType type : MaterialBlockType.values()) {
            simpleCubeBlock(HbmBlocks.getMaterialBlock(type).get(), type.blockId(), type.blockId());
        }
    }

    private void anvilBlock(final Block block, final String modelName, final String textureName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/template_anvil"))
            .texture("particle", modLoc("block/" + textureName))
            .texture("body", modLoc("block/" + textureName))
            .texture("top", modLoc("block/" + textureName));

        getVariantBuilder(block)
            .partialState().with(AnvilBlock.FACING, Direction.SOUTH)
            .modelForState().modelFile(model).addModel()
            .partialState().with(AnvilBlock.FACING, Direction.WEST)
            .modelForState().modelFile(model).rotationY(90).addModel()
            .partialState().with(AnvilBlock.FACING, Direction.NORTH)
            .modelForState().modelFile(model).rotationY(180).addModel()
            .partialState().with(AnvilBlock.FACING, Direction.EAST)
            .modelForState().modelFile(model).rotationY(270).addModel();

        simpleBlockItem(block, model);
    }

    private void batteryBlock(final Block block, final String modelName, final String frontTexture, final String sideTexture, final String topTexture) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/" + sideTexture))
            .texture("front", modLoc("block/" + frontTexture))
            .texture("side", modLoc("block/" + sideTexture))
            .texture("top", modLoc("block/" + topTexture))
            .texture("bottom", modLoc("block/" + topTexture));

        getVariantBuilder(block)
            .partialState().with(BatteryBlock.FACING, Direction.NORTH)
            .modelForState().modelFile(model).addModel()
            .partialState().with(BatteryBlock.FACING, Direction.EAST)
            .modelForState().modelFile(model).rotationY(90).addModel()
            .partialState().with(BatteryBlock.FACING, Direction.SOUTH)
            .modelForState().modelFile(model).rotationY(180).addModel()
            .partialState().with(BatteryBlock.FACING, Direction.WEST)
            .modelForState().modelFile(model).rotationY(270).addModel();
    }

    private void barrelBlock(final Block block, final String modelName, final String textureName) {
        final ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + modelName), existingFileHelper);

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void shredderBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_shredder_side_alt"))
            .texture("front", modLoc("block/machine_shredder_front_alt"))
            .texture("side", modLoc("block/machine_shredder_side_alt"))
            .texture("top", modLoc("block/machine_shredder_top_alt"))
            .texture("bottom", modLoc("block/machine_shredder_bottom_alt"));

        getVariantBuilder(block)
            .partialState().with(ShredderBlock.FACING, Direction.NORTH)
            .modelForState().modelFile(model).addModel()
            .partialState().with(ShredderBlock.FACING, Direction.EAST)
            .modelForState().modelFile(model).rotationY(90).addModel()
            .partialState().with(ShredderBlock.FACING, Direction.SOUTH)
            .modelForState().modelFile(model).rotationY(180).addModel()
            .partialState().with(ShredderBlock.FACING, Direction.WEST)
            .modelForState().modelFile(model).rotationY(270).addModel();

        simpleBlockItem(block, model);
    }

    private void diFurnaceBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/difurnace_side_alt"))
            .texture("front", modLoc("block/difurnace_front_off_alt"))
            .texture("side", modLoc("block/difurnace_side_alt"))
            .texture("top", modLoc("block/difurnace_top_off_alt"))
            .texture("bottom", modLoc("block/brick_fire"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/difurnace_side_alt"))
            .texture("front", modLoc("block/difurnace_front_on_alt"))
            .texture("side", modLoc("block/difurnace_side_alt"))
            .texture("top", modLoc("block/difurnace_top_on_alt"))
            .texture("bottom", modLoc("block/brick_fire"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(DiFurnaceBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(DiFurnaceBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void diFurnaceExtensionBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/cube_bottom_top"))
            .texture("bottom", modLoc("block/brick_fire"))
            .texture("top", modLoc("block/difurnace_top_off_alt"))
            .texture("side", modLoc("block/difurnace_side_alt"));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void diFurnaceRtgBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/rtg_difurnace_front_off"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/rtg_difurnace_top_off"))
            .texture("bottom", modLoc("block/machine_rtg_furnace_base_alt"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/rtg_difurnace_front_on"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/rtg_difurnace_top_on"))
            .texture("bottom", modLoc("block/machine_rtg_furnace_base_alt"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(DiFurnaceRtgBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(DiFurnaceRtgBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void electricFurnaceBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_electric_furnace_side"))
            .texture("front", modLoc("block/machine_electric_furnace_front_off"))
            .texture("side", modLoc("block/machine_electric_furnace_side"))
            .texture("top", modLoc("block/machine_electric_furnace_top"))
            .texture("bottom", modLoc("block/machine_electric_furnace_bottom"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_electric_furnace_side"))
            .texture("front", modLoc("block/machine_electric_furnace_front_on"))
            .texture("side", modLoc("block/machine_electric_furnace_side"))
            .texture("top", modLoc("block/machine_electric_furnace_top"))
            .texture("bottom", modLoc("block/machine_electric_furnace_bottom"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(ElectricFurnaceBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(ElectricFurnaceBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void furnaceIronBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_electric_furnace_side"))
            .texture("front", modLoc("block/machine_electric_furnace_front_off"))
            .texture("side", modLoc("block/machine_electric_furnace_side"))
            .texture("top", modLoc("block/machine_electric_furnace_top"))
            .texture("bottom", modLoc("block/machine_electric_furnace_bottom"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_electric_furnace_side"))
            .texture("front", modLoc("block/machine_electric_furnace_front_on"))
            .texture("side", modLoc("block/machine_electric_furnace_side"))
            .texture("top", modLoc("block/machine_electric_furnace_top"))
            .texture("bottom", modLoc("block/machine_electric_furnace_bottom"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(FurnaceIronBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(FurnaceIronBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void furnaceSteelBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/machine_rtg_furnace_off_alt"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/machine_rtg_furnace_base_alt"))
            .texture("bottom", modLoc("block/machine_rtg_furnace_base_alt"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/machine_rtg_furnace_on_alt"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/machine_rtg_furnace_base_alt"))
            .texture("bottom", modLoc("block/machine_rtg_furnace_base_alt"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(FurnaceSteelBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(FurnaceSteelBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void furnaceCombinationBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/furnace_combination"))
            .texture("front", modLoc("block/furnace_combination"))
            .texture("side", modLoc("block/furnace_combination"))
            .texture("top", modLoc("block/furnace_combination"))
            .texture("bottom", modLoc("block/furnace_combination"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(model)
            .rotationY(switch (state.getValue(FurnaceCombinationBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void rotaryFurnaceBlock(final Block block, final String modelName) {
        final ModelFile coreModel = new ModelFile.ExistingModelFile(modLoc("block/" + modelName), existingFileHelper);
        final ModelFile invisibleModel = new ModelFile.UncheckedModelFile(modLoc("block/invisible"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(RotaryFurnaceBlock.PART) == RotaryFurnacePart.CORE ? coreModel : invisibleModel)
            .rotationY(switch (state.getValue(RotaryFurnaceBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, coreModel);
    }

    private void rtgFurnaceBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/machine_rtg_furnace_off_alt"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/machine_rtg_furnace_base_alt"))
            .texture("bottom", modLoc("block/machine_rtg_furnace_base_alt"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/machine_rtg_furnace_on_alt"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/machine_rtg_furnace_base_alt"))
            .texture("bottom", modLoc("block/machine_rtg_furnace_base_alt"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(RtgFurnaceBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(RtgFurnaceBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void dieselGeneratorBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/machine_rtg_furnace_off_alt"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/machine_rtg_furnace_base_alt"))
            .texture("bottom", modLoc("block/brick_fire"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("front", modLoc("block/machine_rtg_furnace_on_alt"))
            .texture("side", modLoc("block/machine_rtg_furnace_side_alt"))
            .texture("top", modLoc("block/machine_rtg_furnace_base_alt"))
            .texture("bottom", modLoc("block/brick_fire"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(DieselGeneratorBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(DieselGeneratorBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void combustionEngineBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_electric_furnace_side"))
            .texture("front", modLoc("block/machine_electric_furnace_front_off"))
            .texture("side", modLoc("block/machine_electric_furnace_side"))
            .texture("top", modLoc("block/machine_electric_furnace_top"))
            .texture("bottom", modLoc("block/machine_electric_furnace_bottom"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_electric_furnace_side"))
            .texture("front", modLoc("block/machine_electric_furnace_front_on"))
            .texture("side", modLoc("block/machine_electric_furnace_side"))
            .texture("top", modLoc("block/machine_electric_furnace_top"))
            .texture("bottom", modLoc("block/machine_electric_furnace_bottom"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(CombustionEngineBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(CombustionEngineBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void brickFurnaceBlock(final Block block, final String modelName) {
        final ModelFile offModel = models().withExistingParent(modelName + "_off", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_furnace_brick_side"))
            .texture("front", modLoc("block/machine_furnace_brick_front_off"))
            .texture("side", modLoc("block/machine_furnace_brick_side"))
            .texture("top", modLoc("block/machine_furnace_brick_top"))
            .texture("bottom", modLoc("block/machine_furnace_brick_bottom"));
        final ModelFile onModel = models().withExistingParent(modelName + "_on", mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_furnace_brick_side"))
            .texture("front", modLoc("block/machine_furnace_brick_front_on"))
            .texture("side", modLoc("block/machine_furnace_brick_side"))
            .texture("top", modLoc("block/machine_furnace_brick_top"))
            .texture("bottom", modLoc("block/machine_furnace_brick_bottom"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(BrickFurnaceBlock.LIT) ? onModel : offModel)
            .rotationY(switch (state.getValue(BrickFurnaceBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, offModel);
    }

    private void pressBlock(final Block block, final String modelName, final String textureName) {
        final ModelFile coreModel = models().cubeAll(modelName, preferredBlockTexture(textureName));
        final ModelFile invisibleModel = new ModelFile.UncheckedModelFile(modLoc("block/invisible"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(PressBlock.PART) == PressPart.CORE ? coreModel : invisibleModel)
            .rotationY(switch (state.getValue(PressBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, coreModel);
    }

    private void centrifugeBlock(final Block block, final String modelName) {
        final ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + modelName + "_obj"), existingFileHelper);

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(model)
            .rotationY(switch (state.getValue(CentrifugeBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void gasCentrifugeBlock(final Block block, final String modelName) {
        final ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + modelName), existingFileHelper);

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(model)
            .rotationY(switch (state.getValue(GasCentrifugeBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void cyclotronBlock(final Block block, final String modelName) {
        final ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + modelName + "_obj"), existingFileHelper);

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(model)
            .rotationY(switch (state.getValue(CyclotronBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void purexBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/block_steel"))
            .texture("front", modLoc("block/block_steel"))
            .texture("side", modLoc("block/block_steel"))
            .texture("top", modLoc("block/block_steel"))
            .texture("bottom", modLoc("block/block_steel"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(model)
            .rotationY(switch (state.getValue(PurexBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void icfBlock(final Block block, final String modelName) {
        final ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + modelName + "_obj"), existingFileHelper);

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(model)
            .rotationY(switch (state.getValue(IcfBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void icfControllerBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/icf_casing"))
            .texture("front", modLoc("block/icf_controller"))
            .texture("side", modLoc("block/icf_casing"))
            .texture("top", modLoc("block/icf_casing"))
            .texture("bottom", modLoc("block/icf_casing"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(model)
            .rotationY(switch (state.getValue(IcfControllerBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void icfLaserComponentBlock(final Block block, final String modelName) {
        final ModelFile casing = models().cubeAll(modelName + "_casing", modLoc("block/icf_casing"));
        final ModelFile port = models().cubeAll(modelName + "_port", modLoc("block/icf_port"));
        final ModelFile cell = models().cubeAll(modelName + "_cell", modLoc("block/icf_cell"));
        final ModelFile emitter = models().cubeAll(modelName + "_emitter", modLoc("block/icf_emitter"));
        final ModelFile capacitor = models().withExistingParent(modelName + "_capacitor", mcLoc("block/orientable"))
            .texture("particle", modLoc("block/icf_capacitor_side"))
            .texture("front", modLoc("block/icf_capacitor_side"))
            .texture("side", modLoc("block/icf_capacitor_side"))
            .texture("top", modLoc("block/icf_capacitor_top"))
            .texture("bottom", modLoc("block/icf_capacitor_top"));
        final ModelFile turbo = models().cubeAll(modelName + "_turbo", modLoc("block/icf_turbocharger"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(switch (state.getValue(IcfLaserComponentBlock.PART)) {
                case CASING -> casing;
                case PORT -> port;
                case CELL -> cell;
                case EMITTER -> emitter;
                case CAPACITOR -> capacitor;
                case TURBO -> turbo;
            })
            .build());

        simpleBlockItem(block, casing);
    }

    private void icfPressBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/cube"))
            .texture("particle", modLoc("block/machine_icf_press_side"))
            .texture("down", modLoc("block/machine_icf_press_top"))
            .texture("up", modLoc("block/machine_icf_press_top"))
            .texture("north", modLoc("block/machine_icf_press_side"))
            .texture("south", modLoc("block/machine_icf_press_side"))
            .texture("west", modLoc("block/machine_icf_press_side"))
            .texture("east", modLoc("block/machine_icf_press_side"));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void assemblyMachineBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/block_steel"))
            .texture("front", modLoc("block/block_steel"))
            .texture("side", modLoc("block/block_steel"))
            .texture("top", modLoc("block/block_steel"))
            .texture("bottom", modLoc("block/block_steel"));
        final ModelFile invisibleModel = new ModelFile.UncheckedModelFile(modLoc("block/invisible"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(AssemblyMachineBlock.PART) == AssemblyMachinePart.CORE ? model : invisibleModel)
            .rotationY(switch (state.getValue(AssemblyMachineBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void solderingStationBlock(final Block block, final String modelName) {
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/block_steel"))
            .texture("front", modLoc("block/block_steel"))
            .texture("side", modLoc("block/block_steel"))
            .texture("top", modLoc("block/block_steel"))
            .texture("bottom", modLoc("block/block_steel"));
        final ModelFile invisibleModel = new ModelFile.UncheckedModelFile(modLoc("block/invisible"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(SolderingStationBlock.PART) == SolderingStationPart.CORE ? model : invisibleModel)
            .rotationY(switch (state.getValue(SolderingStationBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            })
            .build());

        simpleBlockItem(block, model);
    }

    private void grateBlock(final Block block, final String modelName, final String topTexture) {
        final boolean wide = modelName.contains("wide");
        final float thickness = wide ? 1.984F : 2.0F;
        final ModelFile[] levelModels = new ModelFile[10];

        for (int level = 0; level < levelModels.length; level++) {
            final float minY = level == 9 ? -2.0F : level * 2.0F;
            final float maxY = minY + thickness;

            levelModels[level] = models().getBuilder(modelName + "_level_" + level)
                .texture("particle", modLoc("block/grate_side"))
                .texture("side", modLoc("block/grate_side"))
                .texture("top", modLoc("block/" + topTexture))
                .texture("bottom", modLoc("block/" + topTexture))
                .element()
                .from(0.0F, minY, 0.0F)
                .to(16.0F, maxY, 16.0F)
                .face(Direction.DOWN).texture("#bottom").end()
                .face(Direction.UP).texture("#top").end()
                .face(Direction.NORTH).texture("#side").end()
                .face(Direction.SOUTH).texture("#side").end()
                .face(Direction.WEST).texture("#side").end()
                .face(Direction.EAST).texture("#side").end()
                .end();
        }

        final ModelFile baseModel = models().withExistingParent(modelName, modLoc("block/" + modelName + "_level_0"));

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(levelModels[state.getValue(SteelGrateBlock.LEVEL)])
            .build());

        simpleBlockItem(block, baseModel);
    }

    private void simpleCubeBlock(final Block block, final String modelName, final String textureName) {
        final ModelFile model = models().cubeAll(modelName, preferredBlockTexture(textureName));
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void existingModelBlock(final Block block, final String modelName) {
        final ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + modelName), existingFileHelper);
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private ResourceLocation preferredBlockTexture(final String textureName) {
        final ResourceLocation modTexture = modLoc("block/" + textureName);
        if (existingFileHelper.exists(modTexture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
            return modTexture;
        }
        if (textureName.startsWith("ore_nether_")) {
            return mcLoc("block/netherrack");
        }
        return mcLoc("block/stone");
    }
}

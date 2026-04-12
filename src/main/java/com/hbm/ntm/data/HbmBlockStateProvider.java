package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.assembly.AssemblyMachinePart;
import com.hbm.ntm.common.block.AssemblyMachineBlock;
import com.hbm.ntm.common.block.BatteryBlock;
import com.hbm.ntm.common.block.CentrifugeBlock;
import com.hbm.ntm.common.block.GasCentrifugeBlock;
import com.hbm.ntm.common.block.IcfBlock;
import com.hbm.ntm.common.block.IcfControllerBlock;
import com.hbm.ntm.common.block.IcfLaserComponentBlock;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.OverworldOreType;
import com.hbm.ntm.common.block.PressBlock;
import com.hbm.ntm.common.block.PurexBlock;
import com.hbm.ntm.common.block.ShredderBlock;
import com.hbm.ntm.common.block.SolderingStationBlock;
import com.hbm.ntm.common.press.PressPart;
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
import net.minecraftforge.client.model.generators.loaders.ObjModelBuilder;
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
        centrifugeBlock(HbmBlocks.MACHINE_CENTRIFUGE.get(), "machine_centrifuge");
        gasCentrifugeBlock(HbmBlocks.MACHINE_GAS_CENTRIFUGE.get(), "machine_gascent");
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
        final ModelFile model = models().getBuilder(modelName)
            .customLoader(ObjModelBuilder::begin)
            .modelLocation(modLoc("models/block/barrel_body.obj"))
            .flipV(true)
            .end()
            .texture("particle", modLoc("block/" + textureName))
            .texture("texture0", modLoc("block/" + textureName));

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
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_centrifuge"))
            .texture("front", modLoc("block/machine_centrifuge"))
            .texture("side", modLoc("block/machine_centrifuge"))
            .texture("top", modLoc("block/machine_centrifuge"))
            .texture("bottom", modLoc("block/machine_centrifuge"));

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
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/machine_gascent"))
            .texture("front", modLoc("block/machine_gascent"))
            .texture("side", modLoc("block/machine_gascent"))
            .texture("top", modLoc("block/machine_gascent"))
            .texture("bottom", modLoc("block/machine_gascent"));

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
        final ModelFile model = models().withExistingParent(modelName, mcLoc("block/orientable_with_bottom"))
            .texture("particle", modLoc("block/block_steel"))
            .texture("front", modLoc("block/block_steel"))
            .texture("side", modLoc("block/block_steel"))
            .texture("top", modLoc("block/block_steel"))
            .texture("bottom", modLoc("block/block_steel"));

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

    private void simpleCubeBlock(final Block block, final String modelName, final String textureName) {
        final ModelFile model = models().cubeAll(modelName, preferredBlockTexture(textureName));
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

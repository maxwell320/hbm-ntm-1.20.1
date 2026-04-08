package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BatteryBlock;
import com.hbm.ntm.common.registration.HbmBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

@SuppressWarnings("null")
public class HbmBlockStateProvider extends BlockStateProvider {
    public HbmBlockStateProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, HbmNtmMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        batteryBlock(HbmBlocks.MACHINE_BATTERY.get(), "machine_battery", "battery_front_alt", "battery_side_alt", "battery_top");
        simpleCubeBlock(HbmBlocks.MACHINE_PRESS.get(), "machine_press", "machine_press");
        simpleCubeBlock(HbmBlocks.BARREL_PLASTIC.get(), "barrel_plastic", "barrel_plastic");
        simpleCubeBlock(HbmBlocks.BARREL_CORRODED.get(), "barrel_corroded", "barrel_corroded");
        simpleCubeBlock(HbmBlocks.BARREL_IRON.get(), "barrel_iron", "barrel_iron");
        simpleCubeBlock(HbmBlocks.BARREL_STEEL.get(), "barrel_steel", "barrel_steel");
        simpleCubeBlock(HbmBlocks.BARREL_TCALLOY.get(), "barrel_tcalloy", "barrel_tcalloy");
        simpleCubeBlock(HbmBlocks.BARREL_ANTIMATTER.get(), "barrel_antimatter", "barrel_antimatter");
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

    private void simpleCubeBlock(final Block block, final String modelName, final String textureName) {
        final ModelFile model = models().cubeAll(modelName, modLoc("block/" + textureName));
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }
}

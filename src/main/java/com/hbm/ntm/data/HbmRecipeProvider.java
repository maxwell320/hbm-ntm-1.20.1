package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.StampItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class HbmRecipeProvider extends RecipeProvider {
    public HbmRecipeProvider(final PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(final @NotNull Consumer<FinishedRecipe> recipeOutput) {
        for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {
            buildNuggetPacking(recipeOutput, material);
            buildBilletConversions(recipeOutput, material);
            buildTinyDustPacking(recipeOutput, material);
            buildDustSmelting(recipeOutput, material);
        }

        buildBatteryRecipes(recipeOutput);
        buildMaterialBlockRecipes(recipeOutput);
        buildBasaltRecipes(recipeOutput);
        buildCircuitRecipes(recipeOutput);
        buildEnergyRecipes(recipeOutput);
        buildSteelStructureRecipes(recipeOutput);
        buildRtgGeneratorRecipes(recipeOutput);
        buildFluidStorageRecipes(recipeOutput);
        buildMercuryRecipes(recipeOutput);
        buildRtgPelletRecipes(recipeOutput);
        buildPressSupportRecipes(recipeOutput);
        buildIcfRecipes(recipeOutput);
        buildStampRecipes(recipeOutput);
        buildFalloutRecipes(recipeOutput);
        buildReadoutToolRecipes(recipeOutput);
        buildServiceToolRecipes(recipeOutput);
        buildBladeRecipes(recipeOutput);
    }

    private void buildNuggetPacking(final Consumer<FinishedRecipe> recipeOutput, final HbmMaterialDefinition material) {
        if (!material.hasShape(HbmMaterialShape.INGOT) || !material.hasShape(HbmMaterialShape.NUGGET)) {
            return;
        }

        final ItemLike ingot = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.INGOT).get());
        final ItemLike nugget = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.NUGGET).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot)
            .pattern("NNN")
            .pattern("NNN")
            .pattern("NNN")
            .define('N', nugget)
            .unlockedBy(getHasName(nugget), has(nugget))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, material.itemId(HbmMaterialShape.INGOT) + "_from_" + material.itemId(HbmMaterialShape.NUGGET))));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
            .requires(ingot)
            .unlockedBy(getHasName(ingot), has(ingot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, material.itemId(HbmMaterialShape.NUGGET) + "_from_" + material.itemId(HbmMaterialShape.INGOT))));
    }

    private void buildBilletConversions(final Consumer<FinishedRecipe> recipeOutput, final HbmMaterialDefinition material) {
        if (!material.hasShape(HbmMaterialShape.BILLET) || !material.hasShape(HbmMaterialShape.NUGGET)) {
            return;
        }

        final ItemLike billet = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.BILLET).get());
        final ItemLike nugget = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.NUGGET).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, billet)
            .pattern("NNN")
            .pattern("NNN")
            .define('N', nugget)
            .unlockedBy(getHasName(nugget), has(nugget))
            .save(recipeOutput, rl(material.itemId(HbmMaterialShape.BILLET) + "_from_" + material.itemId(HbmMaterialShape.NUGGET)));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 6)
            .requires(billet)
            .unlockedBy(getHasName(billet), has(billet))
            .save(recipeOutput, rl(material.itemId(HbmMaterialShape.NUGGET) + "_from_" + material.itemId(HbmMaterialShape.BILLET)));

        if (!material.hasShape(HbmMaterialShape.INGOT)) {
            return;
        }

        final ItemLike ingot = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.INGOT).get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 2)
            .requires(billet)
            .requires(billet)
            .requires(billet)
            .unlockedBy(getHasName(billet), has(billet))
            .save(recipeOutput, rl(material.itemId(HbmMaterialShape.INGOT) + "_from_" + material.itemId(HbmMaterialShape.BILLET)));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, billet, 3)
            .pattern("II")
            .define('I', ingot)
            .unlockedBy(getHasName(ingot), has(ingot))
            .save(recipeOutput, rl(material.itemId(HbmMaterialShape.BILLET) + "_from_" + material.itemId(HbmMaterialShape.INGOT)));
    }

    private void buildTinyDustPacking(final Consumer<FinishedRecipe> recipeOutput, final HbmMaterialDefinition material) {
        if (!material.hasShape(HbmMaterialShape.DUST_TINY) || !material.hasShape(HbmMaterialShape.DUST)) {
            return;
        }

        final ItemLike tinyDust = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.DUST_TINY).get());
        final ItemLike dust = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.DUST).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, dust)
            .pattern("TTT")
            .pattern("TTT")
            .pattern("TTT")
            .define('T', tinyDust)
            .unlockedBy(getHasName(tinyDust), has(tinyDust))
            .save(recipeOutput, rl(material.itemId(HbmMaterialShape.DUST) + "_from_" + material.itemId(HbmMaterialShape.DUST_TINY)));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, tinyDust, 9)
            .requires(dust)
            .unlockedBy(getHasName(dust), has(dust))
            .save(recipeOutput, rl(material.itemId(HbmMaterialShape.DUST_TINY) + "_from_" + material.itemId(HbmMaterialShape.DUST)));
    }

    private void buildDustSmelting(final Consumer<FinishedRecipe> recipeOutput, final HbmMaterialDefinition material) {
        if (!material.autoDustSmelting() || !material.hasShape(HbmMaterialShape.DUST) || !material.hasShape(HbmMaterialShape.INGOT)) {
            return;
        }

        final ItemLike dust = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.DUST).get());
        final ItemLike ingot = Objects.requireNonNull(HbmItems.getMaterialPart(material, HbmMaterialShape.INGOT).get());
        final List<ItemLike> ingredients = List.of(dust);

        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, ingredients, RecipeCategory.MISC, ingot, 0.1F, 200, material.id(), "_from_smelting");
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, ingredients, RecipeCategory.MISC, ingot, 0.1F, 100, material.id(), "_from_blasting");
    }

    private void buildMaterialBlockRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        for (final MaterialBlockType type : MaterialBlockType.values()) {
            final ItemLike ingot = Objects.requireNonNull(HbmItems.getMaterialPart(type.material(), HbmMaterialShape.INGOT).get());
            final ItemLike block = Objects.requireNonNull(HbmBlocks.getMaterialBlock(type).get());

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ingot)
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, type.blockId() + "_from_ingots")));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(block)
                .unlockedBy(getHasName(block), has(block))
                .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, type.material().itemId(HbmMaterialShape.INGOT) + "_from_" + type.blockId())));
        }
    }

    private void buildBatteryRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike potatoBattery = Objects.requireNonNull(HbmItems.BATTERY_POTATO.get());
        final ItemLike aluminiumWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.WIRE).get());
        final ItemLike copperWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.WIRE).get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, potatoBattery)
            .requires(Items.POTATO)
            .requires(aluminiumWire)
            .requires(copperWire)
            .unlockedBy(getHasName(aluminiumWire), has(aluminiumWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "battery_potato")));
    }

    private void buildMercuryRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike tinyDropMercury = Objects.requireNonNull(HbmItems.NUGGET_MERCURY_TINY.get());
        final ItemLike dropMercury = Objects.requireNonNull(HbmItems.NUGGET_MERCURY.get());
        final ItemLike bottleMercury = Objects.requireNonNull(HbmItems.BOTTLE_MERCURY.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, dropMercury)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .define('#', tinyDropMercury)
            .unlockedBy(getHasName(tinyDropMercury), has(tinyDropMercury))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "nugget_mercury_from_tiny_drops")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, tinyDropMercury, 9)
            .requires(dropMercury)
            .unlockedBy(getHasName(dropMercury), has(dropMercury))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "nugget_mercury_tiny_from_drop")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, bottleMercury)
            .pattern("###")
            .pattern("#B#")
            .pattern("###")
            .define('#', dropMercury)
            .define('B', Items.GLASS_BOTTLE)
            .unlockedBy(getHasName(dropMercury), has(dropMercury))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "bottle_mercury")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, dropMercury, 8)
            .requires(bottleMercury)
            .unlockedBy(getHasName(bottleMercury), has(bottleMercury))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "nugget_mercury_from_bottle")));
    }

    private void buildRtgPelletRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike ironCastPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.CAST_PLATE).get());

        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PU238, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_RADIUM.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.RA226, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_radium");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_WEAK.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.U238, HbmMaterialShape.BILLET).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.U238, HbmMaterialShape.BILLET).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PU238, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_weak");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_STRONTIUM.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SR90, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_strontium");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_COBALT.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.CO60, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_cobalt");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_ACTINIUM.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ACTINIUM, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_actinium");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_POLONIUM.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLONIUM, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_polonium");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_LEAD.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PB209, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_lead");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_GOLD.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.AU198, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_gold");
        buildRtgPelletRecipe(recipeOutput, Objects.requireNonNull(HbmItems.PELLET_RTG_AMERICIUM.get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.AM241, HbmMaterialShape.BILLET).get()), ironCastPlate, "pellet_rtg_americium");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BISMUTH, HbmMaterialShape.BILLET).get()), 3)
            .requires(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_BISMUTH.get()))
            .unlockedBy(getHasName(HbmItems.PELLET_RTG_DEPLETED_BISMUTH.get()), has(HbmItems.PELLET_RTG_DEPLETED_BISMUTH.get()))
            .save(recipeOutput, rl("billet_bismuth_from_pellet_rtg_depleted_bismuth"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LEAD, HbmMaterialShape.INGOT).get()), 2)
            .requires(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_LEAD.get()))
            .unlockedBy(getHasName(HbmItems.PELLET_RTG_DEPLETED_LEAD.get()), has(HbmItems.PELLET_RTG_DEPLETED_LEAD.get()))
            .save(recipeOutput, rl("ingot_lead_from_pellet_rtg_depleted_lead"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.NICKEL, HbmMaterialShape.INGOT).get()), 2)
            .requires(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_NICKEL.get()))
            .unlockedBy(getHasName(HbmItems.PELLET_RTG_DEPLETED_NICKEL.get()), has(HbmItems.PELLET_RTG_DEPLETED_NICKEL.get()))
            .save(recipeOutput, rl("ingot_nickel_from_pellet_rtg_depleted_nickel"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(HbmItems.INGOT_MERCURY.get()), 2)
            .requires(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_MERCURY.get()))
            .unlockedBy(getHasName(HbmItems.PELLET_RTG_DEPLETED_MERCURY.get()), has(HbmItems.PELLET_RTG_DEPLETED_MERCURY.get()))
            .save(recipeOutput, rl("ingot_mercury_from_pellet_rtg_depleted_mercury"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.NEPTUNIUM, HbmMaterialShape.BILLET).get()), 3)
            .requires(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_NEPTUNIUM.get()))
            .unlockedBy(getHasName(HbmItems.PELLET_RTG_DEPLETED_NEPTUNIUM.get()), has(HbmItems.PELLET_RTG_DEPLETED_NEPTUNIUM.get()))
            .save(recipeOutput, rl("billet_neptunium_from_pellet_rtg_depleted_neptunium"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ZIRCONIUM, HbmMaterialShape.BILLET).get()), 3)
            .requires(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_ZIRCONIUM.get()))
            .unlockedBy(getHasName(HbmItems.PELLET_RTG_DEPLETED_ZIRCONIUM.get()), has(HbmItems.PELLET_RTG_DEPLETED_ZIRCONIUM.get()))
            .save(recipeOutput, rl("billet_zirconium_from_pellet_rtg_depleted_zirconium"));
    }

    private void buildRtgPelletRecipe(final Consumer<FinishedRecipe> recipeOutput, final ItemLike output,
                                      final ItemLike billet, final ItemLike plate, final String recipeName) {
        buildRtgPelletRecipe(recipeOutput, output, billet, billet, billet, plate, recipeName);
    }

    private void buildRtgPelletRecipe(final Consumer<FinishedRecipe> recipeOutput, final ItemLike output,
                                      final ItemLike billetA, final ItemLike billetB, final ItemLike billetC,
                                      final ItemLike plate, final String recipeName) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output)
            .requires(billetA)
            .requires(billetB)
            .requires(billetC)
            .requires(plate)
            .unlockedBy(getHasName(plate), has(plate))
            .save(recipeOutput, rl(recipeName));
    }

    private void buildEnergyRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike redCable = Objects.requireNonNull(HbmBlocks.RED_CABLE.get());
        final ItemLike redCableClassic = Objects.requireNonNull(HbmBlocks.RED_CABLE_CLASSIC.get());
        final ItemLike polymerPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.PLATE).get());
        final ItemLike redCopperWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.RED_COPPER, HbmMaterialShape.WIRE).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, redCable, 16)
            .pattern(" W ")
            .pattern("RRR")
            .pattern(" W ")
            .define('W', polymerPlate)
            .define('R', redCopperWire)
            .unlockedBy(getHasName(redCopperWire), has(redCopperWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "red_cable")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, redCableClassic)
            .requires(redCable)
            .unlockedBy(getHasName(redCable), has(redCable))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "red_cable_classic")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, redCable)
            .requires(redCableClassic)
            .unlockedBy(getHasName(redCableClassic), has(redCableClassic))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "red_cable_from_classic")));
    }

    private void buildSteelStructureRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike steelIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get());
        final ItemLike steelBeam = Objects.requireNonNull(HbmBlocks.STEEL_BEAM.get());
        final ItemLike steelGrate = Objects.requireNonNull(HbmBlocks.STEEL_GRATE.get());
        final ItemLike steelGrateWide = Objects.requireNonNull(HbmBlocks.STEEL_GRATE_WIDE.get());
        final ItemLike copperPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get());
        final ItemLike firebrickIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.FIREBRICK, HbmMaterialShape.INGOT).get());
        final ItemLike extension = Objects.requireNonNull(HbmBlocks.MACHINE_DI_FURNACE_EXTENSION.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, steelBeam, 8)
            .pattern("S")
            .pattern("S")
            .pattern("S")
            .define('S', steelIngot)
            .unlockedBy(getHasName(steelIngot), has(steelIngot))
            .save(recipeOutput, rl("steel_beam"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, steelGrate, 4)
            .pattern("BB")
            .pattern("BB")
            .define('B', steelBeam)
            .unlockedBy(getHasName(steelBeam), has(steelBeam))
            .save(recipeOutput, rl("steel_grate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, steelGrateWide, 4)
            .pattern("GG")
            .define('G', steelGrate)
            .unlockedBy(getHasName(steelGrate), has(steelGrate))
            .save(recipeOutput, rl("steel_grate_wide"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, steelGrate)
            .pattern("WW")
            .define('W', steelGrateWide)
            .unlockedBy(getHasName(steelGrateWide), has(steelGrateWide))
            .save(recipeOutput, rl("steel_grate_from_wide"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, extension)
            .pattern(" C ")
            .pattern("BGB")
            .pattern("BGB")
            .define('C', copperPlate)
            .define('B', firebrickIngot)
            .define('G', steelGrate)
            .unlockedBy(getHasName(steelGrate), has(steelGrate))
            .save(recipeOutput, rl("machine_difurnace_extension"));
    }

    private void buildRtgGeneratorRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike machineMiniRtg = Objects.requireNonNull(HbmBlocks.MACHINE_MINI_RTG.get());
        final ItemLike machinePowerRtg = Objects.requireNonNull(HbmBlocks.MACHINE_POWER_RTG.get());
        final ItemLike leadPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LEAD, HbmMaterialShape.PLATE).get());
        final ItemLike pu238Billet = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PU238, HbmMaterialShape.BILLET).get());
        final ItemLike thermoElement = Objects.requireNonNull(HbmItems.THERMO_ELEMENT.get());
        final ItemLike rtgUnit = Objects.requireNonNull(HbmItems.RTG_UNIT.get());
        final ItemLike starmetalIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STARMETAL, HbmMaterialShape.INGOT).get());
        final ItemLike poloniumBillet = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLONIUM, HbmMaterialShape.BILLET).get());
        final ItemLike tennessineDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TENNESSINE, HbmMaterialShape.DUST).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, machineMiniRtg)
            .pattern("LLL")
            .pattern("PPP")
            .pattern("TRT")
            .define('L', leadPlate)
            .define('P', pu238Billet)
            .define('T', thermoElement)
            .define('R', rtgUnit)
            .unlockedBy(getHasName(rtgUnit), has(rtgUnit))
            .save(recipeOutput, rl("machine_minirtg"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, machinePowerRtg)
            .pattern("SRS")
            .pattern("PTP")
            .pattern("SRS")
            .define('S', starmetalIngot)
            .define('R', rtgUnit)
            .define('P', poloniumBillet)
            .define('T', tennessineDust)
            .unlockedBy(getHasName(rtgUnit), has(rtgUnit))
            .save(recipeOutput, rl("machine_powerrtg"));
    }

    private void buildFluidStorageRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike plasticBarrel = Objects.requireNonNull(HbmBlocks.BARREL_PLASTIC.get());
        final ItemLike steelBarrel = Objects.requireNonNull(HbmBlocks.BARREL_STEEL.get());
        final ItemLike tcalloyBarrel = Objects.requireNonNull(HbmBlocks.BARREL_TCALLOY.get());
        final ItemLike antimatterBarrel = Objects.requireNonNull(HbmBlocks.BARREL_ANTIMATTER.get());
        final ItemLike fluidDuctNeo = Objects.requireNonNull(HbmBlocks.FLUID_DUCT_NEO.get());
        final ItemLike glassBoron = Objects.requireNonNull(HbmBlocks.GLASS_BORON.get());
        final ItemLike fluidIdentifierMulti = Objects.requireNonNull(HbmItems.FLUID_IDENTIFIER_MULTI.get());
        final ItemLike canisterEmpty = Objects.requireNonNull(HbmItems.CANISTER_EMPTY.get());
        final ItemLike disperserCanisterEmpty = Objects.requireNonNull(HbmItems.DISPERSER_CANISTER_EMPTY.get());
        final ItemLike gasEmpty = Objects.requireNonNull(HbmItems.GAS_EMPTY.get());
        final ItemLike fluidTankEmpty = Objects.requireNonNull(HbmItems.FLUID_TANK_EMPTY.get());
        final ItemLike fluidTankLeadEmpty = Objects.requireNonNull(HbmItems.FLUID_TANK_LEAD_EMPTY.get());
        final ItemLike fluidBarrelEmpty = Objects.requireNonNull(HbmItems.FLUID_BARREL_EMPTY.get());
        final ItemLike polymerPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.PLATE).get());
        final ItemLike aluminiumPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.PLATE).get());
        final ItemLike copperPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get());
        final ItemLike leadPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LEAD, HbmMaterialShape.PLATE).get());
        final ItemLike steelPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.PLATE).get());
        final ItemLike steelIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get());
        final ItemLike titaniumPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE).get());
        final ItemLike tcalloyIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TCALLOY, HbmMaterialShape.INGOT).get());
        final ItemLike schrabidiumPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.PLATE).get());
        final ItemLike boronDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BORON, HbmMaterialShape.DUST).get());
        final ItemLike advancedCoil = Objects.requireNonNull(HbmItems.COIL_ADVANCED_TORUS.get());
        final ItemLike vacuumTube = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.VACUUM_TUBE).get());
        final ItemLike ironPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get());
        final ItemLike u238Billet = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.U238, HbmMaterialShape.BILLET).get());
        final Ingredient hardPlasticBars = Ingredient.of(
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.INGOT).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BAKELITE, HbmMaterialShape.INGOT).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LATEX, HbmMaterialShape.INGOT).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.RUBBER, HbmMaterialShape.INGOT).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PET, HbmMaterialShape.INGOT).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PC, HbmMaterialShape.INGOT).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PVC, HbmMaterialShape.INGOT).get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, plasticBarrel)
            .pattern("IPI")
            .pattern("I I")
            .pattern("IPI")
            .define('I', polymerPlate)
            .define('P', aluminiumPlate)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "barrel_plastic")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, canisterEmpty, 2)
            .pattern("S ")
            .pattern("AA")
            .pattern("AA")
            .define('S', steelPlate)
            .define('A', aluminiumPlate)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "canister_empty")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, gasEmpty, 2)
            .pattern("S ")
            .pattern("AA")
            .pattern("AA")
            .define('S', copperPlate)
            .define('A', steelPlate)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "gas_empty")));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, glassBoron, 8)
            .pattern("GGG")
            .pattern("GBG")
            .pattern("GGG")
            .define('G', Blocks.GLASS)
            .define('B', boronDust)
            .unlockedBy(getHasName(boronDust), has(boronDust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "glass_boron")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, disperserCanisterEmpty, 4)
            .pattern(" P ")
            .pattern("PGP")
            .pattern(" P ")
            .define('P', hardPlasticBars)
            .define('G', glassBoron)
            .unlockedBy(getHasName(glassBoron), has(glassBoron))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "disperser_canister_empty")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, fluidTankEmpty, 8)
            .pattern("APA")
            .pattern("AGA")
            .pattern("APA")
            .define('A', aluminiumPlate)
            .define('P', ironPlate)
            .define('G', Tags.Items.GLASS_PANES)
            .unlockedBy(getHasName(aluminiumPlate), has(aluminiumPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fluid_tank_empty")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, fluidTankLeadEmpty, 4)
            .pattern("LUL")
            .pattern("LTL")
            .pattern("LUL")
            .define('L', leadPlate)
            .define('U', u238Billet)
            .define('T', fluidTankEmpty)
            .unlockedBy(getHasName(fluidTankEmpty), has(fluidTankEmpty))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fluid_tank_lead_empty")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, fluidBarrelEmpty, 2)
            .pattern("SAS")
            .pattern("SGS")
            .pattern("SAS")
            .define('S', steelPlate)
            .define('A', aluminiumPlate)
            .define('G', Tags.Items.GLASS_PANES)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fluid_barrel_empty")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fluidDuctNeo, 8)
            .pattern("SAS")
            .pattern("   ")
            .pattern("SAS")
            .define('S', steelPlate)
            .define('A', aluminiumPlate)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fluid_duct_neo")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, steelBarrel)
            .pattern("IPI")
            .pattern("I I")
            .pattern("IPI")
            .define('I', steelPlate)
            .define('P', steelIngot)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "barrel_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, tcalloyBarrel)
            .pattern("IPI")
            .pattern("I I")
            .pattern("IPI")
            .define('I', tcalloyIngot)
            .define('P', titaniumPlate)
            .unlockedBy(getHasName(tcalloyIngot), has(tcalloyIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "barrel_tcalloy")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, antimatterBarrel)
            .pattern("IPI")
            .pattern("I I")
            .pattern("IPI")
            .define('I', schrabidiumPlate)
            .define('P', advancedCoil)
            .unlockedBy(getHasName(schrabidiumPlate), has(schrabidiumPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "barrel_antimatter")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fluidIdentifierMulti)
            .pattern("D")
            .pattern("C")
            .pattern("P")
            .define('D', Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "dyes"))))
            .define('C', vacuumTube)
            .define('P', ironPlate)
            .unlockedBy(getHasName(vacuumTube), has(vacuumTube))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fluid_identifier_multi")));
    }

    private void buildBasaltRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike basalt = Objects.requireNonNull(HbmBlocks.getBasaltBlock(BasaltBlockType.BASALT).get());
        final ItemLike smoothBasalt = Objects.requireNonNull(HbmBlocks.getBasaltBlock(BasaltBlockType.BASALT_SMOOTH).get());
        final ItemLike polishedBasalt = Objects.requireNonNull(HbmBlocks.getBasaltBlock(BasaltBlockType.BASALT_POLISHED).get());
        final ItemLike basaltBrick = Objects.requireNonNull(HbmBlocks.getBasaltBlock(BasaltBlockType.BASALT_BRICK).get());
        final ItemLike basaltTiles = Objects.requireNonNull(HbmBlocks.getBasaltBlock(BasaltBlockType.BASALT_TILES).get());

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(basalt), RecipeCategory.BUILDING_BLOCKS, smoothBasalt, 0.1F, 200)
            .unlockedBy(getHasName(basalt), has(basalt))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, BasaltBlockType.BASALT_SMOOTH.blockId() + "_from_smelting")));

        buildTwoByTwoBlockRecipe(recipeOutput, polishedBasalt, smoothBasalt, BasaltBlockType.BASALT_POLISHED.blockId());
        buildTwoByTwoBlockRecipe(recipeOutput, basaltBrick, polishedBasalt, BasaltBlockType.BASALT_BRICK.blockId());
        buildTwoByTwoBlockRecipe(recipeOutput, basaltTiles, basaltBrick, BasaltBlockType.BASALT_TILES.blockId());
    }

    private void buildCircuitRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike vacuumTube = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.VACUUM_TUBE).get());
        final ItemLike capacitor = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CAPACITOR).get());
        final ItemLike tantalumCapacitor = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CAPACITOR_TANTALIUM).get());
        final ItemLike pcb = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.PCB).get());
        final ItemLike siliconWafer = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.SILICON).get());
        final ItemLike chip = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CHIP).get());
        final ItemLike chipBismoid = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CHIP_BISMOID).get());
        final ItemLike chipQuantum = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CHIP_QUANTUM).get());
        final ItemLike atomicClock = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.ATOMIC_CLOCK).get());
        final ItemLike controllerChassis = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CONTROLLER_CHASSIS).get());
        final ItemLike numitron = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.NUMITRON).get());
        final ItemLike analogCircuit = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.ANALOG).get());
        final ItemLike basicCircuit = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.BASIC).get());
        final ItemLike crtDisplay = Objects.requireNonNull(HbmItems.CRT_DISPLAY.get());
        final ItemLike upgradeTemplate = Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get());
        final ItemLike pelletCharged = Objects.requireNonNull(HbmItems.PELLET_CHARGED.get());
        final ItemLike gemAlexandrite = Objects.requireNonNull(HbmItems.GEM_ALEXANDRITE.get());
        final ItemLike polymerPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.PLATE).get());
        final ItemLike polymerBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.INGOT).get());
        final ItemLike bakeliteBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BAKELITE, HbmMaterialShape.INGOT).get());
        final ItemLike latexBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LATEX, HbmMaterialShape.INGOT).get());
        final ItemLike rubberBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.RUBBER, HbmMaterialShape.INGOT).get());
        final ItemLike petBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PET, HbmMaterialShape.INGOT).get());
        final ItemLike pcBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PC, HbmMaterialShape.INGOT).get());
        final ItemLike pvcBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PVC, HbmMaterialShape.INGOT).get());
        final ItemLike bsccoCoil = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BSCCO, HbmMaterialShape.DENSE_WIRE).get());
        final ItemLike tungstenWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.WIRE).get());
        final ItemLike heatingCoil = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.DENSE_WIRE).get());
        final ItemLike magnetizedTungstenWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.MAGNETIZED_TUNGSTEN, HbmMaterialShape.WIRE).get());
        final ItemLike magnetizedTungstenCoil = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.MAGNETIZED_TUNGSTEN, HbmMaterialShape.DENSE_WIRE).get());
        final ItemLike carbonWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.CARBON, HbmMaterialShape.WIRE).get());
        final ItemLike niobiumNugget = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.NIOBIUM, HbmMaterialShape.NUGGET).get());
        final ItemLike tantaliumNugget = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TANTALIUM, HbmMaterialShape.NUGGET).get());
        final ItemLike aluminiumWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.WIRE).get());
        final ItemLike copperWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.WIRE).get());
        final ItemLike redCopperWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.RED_COPPER, HbmMaterialShape.WIRE).get());
        final ItemLike aluminiumDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.DUST).get());
        final ItemLike bismuthNugget = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BISMUTH, HbmMaterialShape.NUGGET).get());
        final ItemLike ironPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.PLATE).get());
        final ItemLike copperPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get());
        final ItemLike goldPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GOLD, HbmMaterialShape.PLATE).get());
        final ItemLike goldWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GOLD, HbmMaterialShape.WIRE).get());
        final ItemLike quartzDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.QUARTZ, HbmMaterialShape.DUST).get());
        final ItemLike goldCoil = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GOLD, HbmMaterialShape.DENSE_WIRE).get());
        final ItemLike advancedAlloyWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ADVANCED_ALLOY, HbmMaterialShape.WIRE).get());
        final ItemLike advancedAlloyCoil = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ADVANCED_ALLOY, HbmMaterialShape.DENSE_WIRE).get());
        final ItemLike steelPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.PLATE).get());
        final ItemLike copperCoil = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.DENSE_WIRE).get());
        final ItemLike copperCoilTorus = Objects.requireNonNull(HbmItems.COIL_COPPER_TORUS.get());
        final ItemLike motor = Objects.requireNonNull(HbmItems.MOTOR.get());
        final ItemLike motorDesh = Objects.requireNonNull(HbmItems.MOTOR_DESH.get());
        final ItemLike tankSteel = Objects.requireNonNull(HbmItems.TANK_STEEL.get());
        final ItemLike coilAdvancedTorus = Objects.requireNonNull(HbmItems.COIL_ADVANCED_TORUS.get());
        final ItemLike coilGoldTorus = Objects.requireNonNull(HbmItems.COIL_GOLD_TORUS.get());
        final ItemLike photoPanel = Objects.requireNonNull(HbmItems.PHOTO_PANEL.get());
        final ItemLike pin = Objects.requireNonNull(HbmItems.PIN.get());
        final ItemLike catalystClay = Objects.requireNonNull(HbmItems.CATALYST_CLAY.get());
        final ItemLike deuteriumFilter = Objects.requireNonNull(HbmItems.DEUTERIUM_FILTER.get());
        final ItemLike templateFolder = Objects.requireNonNull(HbmItems.TEMPLATE_FOLDER.get());
        final ItemLike finsFlat = Objects.requireNonNull(HbmItems.FINS_FLAT.get());
        final ItemLike sphereSteel = Objects.requireNonNull(HbmItems.SPHERE_STEEL.get());
        final ItemLike pedestalSteel = Objects.requireNonNull(HbmItems.PEDESTAL_STEEL.get());
        final ItemLike finsBigSteel = Objects.requireNonNull(HbmItems.FINS_BIG_STEEL.get());
        final ItemLike finsSmallSteel = Objects.requireNonNull(HbmItems.FINS_SMALL_STEEL.get());
        final ItemLike finsTriSteel = Objects.requireNonNull(HbmItems.FINS_TRI_STEEL.get());
        final ItemLike finsQuadTitanium = Objects.requireNonNull(HbmItems.FINS_QUAD_TITANIUM.get());
        final ItemLike bladeTitanium = Objects.requireNonNull(HbmItems.BLADE_TITANIUM.get());
        final ItemLike turbineTitanium = Objects.requireNonNull(HbmItems.TURBINE_TITANIUM.get());
        final ItemLike flywheelBeryllium = Objects.requireNonNull(HbmItems.FLYWHEEL_BERYLLIUM.get());
        final ItemLike ringStarmetal = Objects.requireNonNull(HbmItems.RING_STARMETAL.get());
        final ItemLike sawblade = Objects.requireNonNull(HbmItems.SAWBLADE.get());
        final ItemLike ironDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.DUST).get());
        final ItemLike sulfurDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SULFUR, HbmMaterialShape.DUST).get());
        final ItemLike ironCastPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.CAST_PLATE).get());
        final ItemLike supportSteelIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get());
        final ItemLike berylliumBlock = Objects.requireNonNull(HbmBlocks.getMaterialBlock(MaterialBlockType.BERYLLIUM).get());
        final ItemLike steelBlock = Objects.requireNonNull(HbmBlocks.getMaterialBlock(MaterialBlockType.STEEL).get());
        final ItemLike titaniumPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE).get());
        final ItemLike starmetalIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STARMETAL, HbmMaterialShape.INGOT).get());
        final ItemLike strontiumDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STRONTIUM, HbmMaterialShape.DUST).get());
        final ItemLike duraPipe = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DURA_STEEL, HbmMaterialShape.PIPE).get());
        final Ingredient plasticBars = Ingredient.of(polymerBar, bakeliteBar, latexBar, rubberBar, petBar, pcBar, pvcBar);
        final Ingredient resistantAlloyIngots = Ingredient.of(
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TCALLOY, HbmMaterialShape.INGOT).get()),
            Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.CDALLOY, HbmMaterialShape.INGOT).get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, vacuumTube)
            .pattern("G")
            .pattern("W")
            .pattern("I")
            .define('G', Ingredient.of(Tags.Items.GLASS_PANES))
            .define('W', tungstenWire)
            .define('I', polymerPlate)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_vacuum_tube_from_tungsten")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, vacuumTube)
            .pattern("G")
            .pattern("W")
            .pattern("I")
            .define('G', Ingredient.of(Tags.Items.GLASS_PANES))
            .define('W', carbonWire)
            .define('I', polymerPlate)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_vacuum_tube_from_carbon")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, capacitor)
            .pattern("I")
            .pattern("N")
            .pattern("W")
            .define('I', polymerPlate)
            .define('N', niobiumNugget)
            .define('W', aluminiumWire)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_capacitor_from_aluminium_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, capacitor)
            .pattern("I")
            .pattern("N")
            .pattern("W")
            .define('I', polymerPlate)
            .define('N', niobiumNugget)
            .define('W', copperWire)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_capacitor_from_copper_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, capacitor, 2)
            .pattern("IAI")
            .pattern("W W")
            .define('I', polymerPlate)
            .define('A', aluminiumDust)
            .define('W', aluminiumWire)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_capacitor_double_from_aluminium_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, capacitor, 2)
            .pattern("IAI")
            .pattern("W W")
            .define('I', polymerPlate)
            .define('A', aluminiumDust)
            .define('W', copperWire)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_capacitor_double_from_copper_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, tantalumCapacitor)
            .pattern("I")
            .pattern("N")
            .pattern("W")
            .define('I', polymerPlate)
            .define('N', tantaliumNugget)
            .define('W', aluminiumWire)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_capacitor_tantalium_from_aluminium_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, tantalumCapacitor)
            .pattern("I")
            .pattern("N")
            .pattern("W")
            .define('I', polymerPlate)
            .define('N', tantaliumNugget)
            .define('W', copperWire)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_capacitor_tantalium_from_copper_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pcb)
            .pattern("I")
            .pattern("P")
            .define('I', polymerPlate)
            .define('P', copperPlate)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_pcb_from_copper_plate")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pcb, 4)
            .pattern("I")
            .pattern("P")
            .define('I', polymerPlate)
            .define('P', goldPlate)
            .unlockedBy(getHasName(polymerPlate), has(polymerPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_pcb_from_gold_plate")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, chip)
            .pattern("I")
            .pattern("S")
            .pattern("W")
            .define('I', polymerPlate)
            .define('S', siliconWafer)
            .define('W', copperWire)
            .unlockedBy(getHasName(siliconWafer), has(siliconWafer))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_chip_from_copper_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, chip)
            .pattern("I")
            .pattern("S")
            .pattern("W")
            .define('I', polymerPlate)
            .define('S', siliconWafer)
            .define('W', goldWire)
            .unlockedBy(getHasName(siliconWafer), has(siliconWafer))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_chip_from_gold_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, chipBismoid)
            .pattern("III")
            .pattern("SNS")
            .pattern("WWW")
            .define('I', polymerPlate)
            .define('S', siliconWafer)
            .define('N', bismuthNugget)
            .define('W', copperWire)
            .unlockedBy(getHasName(siliconWafer), has(siliconWafer))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_chip_bismoid_from_copper_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, chipBismoid)
            .pattern("III")
            .pattern("SNS")
            .pattern("WWW")
            .define('I', polymerPlate)
            .define('S', siliconWafer)
            .define('N', bismuthNugget)
            .define('W', goldWire)
            .unlockedBy(getHasName(siliconWafer), has(siliconWafer))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_chip_bismoid_from_gold_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, chipQuantum)
            .pattern("HHH")
            .pattern("SIS")
            .pattern("WWW")
            .define('H', Ingredient.of(pcBar, pvcBar))
            .define('S', bsccoCoil)
            .define('I', pelletCharged)
            .define('W', copperWire)
            .unlockedBy(getHasName(pelletCharged), has(pelletCharged))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_chip_quantum_from_copper_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, chipQuantum)
            .pattern("HHH")
            .pattern("SIS")
            .pattern("WWW")
            .define('H', Ingredient.of(pcBar, pvcBar))
            .define('S', bsccoCoil)
            .define('I', pelletCharged)
            .define('W', goldWire)
            .unlockedBy(getHasName(pelletCharged), has(pelletCharged))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_chip_quantum_from_gold_wire")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, crtDisplay, 4)
            .pattern(" A ")
            .pattern("SGS")
            .pattern(" T ")
            .define('A', aluminiumDust)
            .define('S', steelPlate)
            .define('G', Ingredient.of(Tags.Items.GLASS_PANES))
            .define('T', vacuumTube)
            .unlockedBy(getHasName(vacuumTube), has(vacuumTube))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "crt_display")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, atomicClock)
            .pattern("ICI")
            .pattern("CSC")
            .pattern("ICI")
            .define('I', polymerPlate)
            .define('C', chip)
            .define('S', strontiumDust)
            .unlockedBy(getHasName(chip), has(chip))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_atomic_clock")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, copperCoil)
            .pattern("WWW")
            .pattern("WIW")
            .pattern("WWW")
            .define('W', redCopperWire)
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(redCopperWire), has(redCopperWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_copper")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, heatingCoil)
            .pattern("WWW")
            .pattern("WIW")
            .pattern("WWW")
            .define('W', tungstenWire)
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(tungstenWire), has(tungstenWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_tungsten")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, magnetizedTungstenCoil)
            .pattern("WWW")
            .pattern("WIW")
            .pattern("WWW")
            .define('W', magnetizedTungstenWire)
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(magnetizedTungstenWire), has(magnetizedTungstenWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_magnetized_tungsten")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, copperCoilTorus, 2)
            .pattern(" C ")
            .pattern("CPC")
            .pattern(" C ")
            .define('C', copperCoil)
            .define('P', ironPlate)
            .unlockedBy(getHasName(copperCoil), has(copperCoil))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_copper_torus_from_iron")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, copperCoilTorus, 2)
            .pattern(" C ")
            .pattern("CPC")
            .pattern(" C ")
            .define('C', copperCoil)
            .define('P', steelPlate)
            .unlockedBy(getHasName(copperCoil), has(copperCoil))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_copper_torus_from_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, advancedAlloyCoil)
            .pattern("WWW")
            .pattern("WIW")
            .pattern("WWW")
            .define('W', advancedAlloyWire)
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(advancedAlloyWire), has(advancedAlloyWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_advanced_alloy")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, goldCoil)
            .pattern("WWW")
            .pattern("WIW")
            .pattern("WWW")
            .define('W', goldWire)
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(goldWire), has(goldWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_gold")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, coilAdvancedTorus, 2)
            .pattern(" C ")
            .pattern("CPC")
            .pattern(" C ")
            .define('C', advancedAlloyCoil)
            .define('P', ironPlate)
            .unlockedBy(getHasName(advancedAlloyCoil), has(advancedAlloyCoil))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_advanced_torus_from_iron")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, coilAdvancedTorus, 2)
            .pattern(" C ")
            .pattern("CPC")
            .pattern(" C ")
            .define('C', advancedAlloyCoil)
            .define('P', steelPlate)
            .unlockedBy(getHasName(advancedAlloyCoil), has(advancedAlloyCoil))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_advanced_torus_from_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, coilGoldTorus, 2)
            .pattern(" C ")
            .pattern("CPC")
            .pattern(" C ")
            .define('C', goldCoil)
            .define('P', ironPlate)
            .unlockedBy(getHasName(goldCoil), has(goldCoil))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_gold_torus_from_iron")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, coilGoldTorus, 2)
            .pattern(" C ")
            .pattern("CPC")
            .pattern(" C ")
            .define('C', goldCoil)
            .define('P', steelPlate)
            .unlockedBy(getHasName(goldCoil), has(goldCoil))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "coil_gold_torus_from_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, motor, 2)
            .pattern(" R ")
            .pattern("ICI")
            .pattern("ITI")
            .define('R', redCopperWire)
            .define('I', ironPlate)
            .define('C', copperCoil)
            .define('T', copperCoilTorus)
            .unlockedBy(getHasName(copperCoilTorus), has(copperCoilTorus))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "motor_from_iron")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, motor, 2)
            .pattern(" R ")
            .pattern("ICI")
            .pattern(" T ")
            .define('R', redCopperWire)
            .define('I', steelPlate)
            .define('C', copperCoil)
            .define('T', copperCoilTorus)
            .unlockedBy(getHasName(copperCoilTorus), has(copperCoilTorus))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "motor_from_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, tankSteel, 2)
            .pattern("STS")
            .pattern("S S")
            .pattern("STS")
            .define('S', steelPlate)
            .define('T', titaniumPlate)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "tank_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, photoPanel)
            .pattern(" G ")
            .pattern("IPI")
            .pattern(" C ")
            .define('G', Ingredient.of(Tags.Items.GLASS_PANES))
            .define('I', polymerPlate)
            .define('P', quartzDust)
            .define('C', pcb)
            .unlockedBy(getHasName(pcb), has(pcb))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "photo_panel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pin)
            .pattern("W ")
            .pattern(" W")
            .pattern(" W")
            .define('W', copperWire)
            .unlockedBy(getHasName(copperWire), has(copperWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "pin")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, catalystClay)
            .requires(ironDust)
            .requires(Items.CLAY_BALL)
            .unlockedBy(getHasName(ironDust), has(ironDust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "catalyst_clay")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, deuteriumFilter)
            .pattern("TST")
            .pattern("SCS")
            .pattern("TST")
            .define('T', resistantAlloyIngots)
            .define('S', sulfurDust)
            .define('C', catalystClay)
            .unlockedBy(getHasName(catalystClay), has(catalystClay))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "deuterium_filter")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, templateFolder)
            .pattern("LPL")
            .pattern("BPB")
            .pattern("LPL")
            .define('P', Items.PAPER)
            .define('L', Tags.Items.DYES)
            .define('B', Tags.Items.DYES)
            .unlockedBy("has_paper", has(Items.PAPER))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "template_folder")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, finsFlat)
            .pattern("IP")
            .pattern("PP")
            .pattern("IP")
            .define('I', supportSteelIngot)
            .define('P', steelPlate)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fins_flat")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, sphereSteel)
            .pattern("PIP")
            .pattern("I I")
            .pattern("PIP")
            .define('P', steelPlate)
            .define('I', supportSteelIngot)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "sphere_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pedestalSteel)
            .pattern("P P")
            .pattern("P P")
            .pattern("III")
            .define('P', steelPlate)
            .define('I', supportSteelIngot)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "pedestal_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, finsBigSteel)
            .pattern(" PI")
            .pattern("III")
            .pattern(" PI")
            .define('P', steelPlate)
            .define('I', supportSteelIngot)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fins_big_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, finsSmallSteel)
            .pattern(" PP")
            .pattern("PII")
            .pattern(" PP")
            .define('P', steelPlate)
            .define('I', supportSteelIngot)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fins_small_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, finsTriSteel)
            .pattern(" PI")
            .pattern("IIB")
            .pattern(" PI")
            .define('P', steelPlate)
            .define('I', supportSteelIngot)
            .define('B', steelBlock)
            .unlockedBy(getHasName(steelBlock), has(steelBlock))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fins_tri_steel")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, finsQuadTitanium)
            .pattern(" PP")
            .pattern("III")
            .pattern(" PP")
            .define('P', titaniumPlate)
            .define('I', Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.INGOT).get()))
            .unlockedBy(getHasName(titaniumPlate), has(titaniumPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fins_quad_titanium")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, bladeTitanium, 2)
            .pattern("TP")
            .pattern("TP")
            .pattern("TT")
            .define('P', titaniumPlate)
            .define('T', Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.INGOT).get()))
            .unlockedBy(getHasName(titaniumPlate), has(titaniumPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "blade_titanium")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, turbineTitanium)
            .pattern("BBB")
            .pattern("BSB")
            .pattern("BBB")
            .define('B', bladeTitanium)
            .define('S', supportSteelIngot)
            .unlockedBy(getHasName(bladeTitanium), has(bladeTitanium))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "turbine_titanium")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ringStarmetal)
            .pattern(" S ")
            .pattern("S S")
            .pattern(" S ")
            .define('S', starmetalIngot)
            .unlockedBy(getHasName(starmetalIngot), has(starmetalIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "ring_starmetal")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, flywheelBeryllium)
            .pattern("IBI")
            .pattern("BTB")
            .pattern("IBI")
            .define('I', ironCastPlate)
            .define('B', berylliumBlock)
            .define('T', duraPipe)
            .unlockedBy(getHasName(berylliumBlock), has(berylliumBlock))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "flywheel_beryllium")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, sawblade)
            .pattern("III")
            .pattern("ICI")
            .pattern("III")
            .define('I', steelPlate)
            .define('C', Items.IRON_INGOT)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "sawblade")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, motorDesh)
            .pattern("PCP")
            .pattern("DMD")
            .pattern("PCP")
            .define('P', plasticBars)
            .define('C', coilGoldTorus)
            .define('D', Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DESH, HbmMaterialShape.INGOT).get()))
            .define('M', motor)
            .unlockedBy(getHasName(motor), has(motor))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "motor_desh")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, numitron, 3)
            .pattern("G")
            .pattern("W")
            .pattern("I")
            .define('G', Ingredient.of(Tags.Items.GLASS_PANES))
            .define('W', heatingCoil)
            .define('I', copperPlate)
            .unlockedBy(getHasName(copperPlate), has(copperPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_numitron")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, controllerChassis)
            .pattern("PPP")
            .pattern("CBB")
            .pattern("PPP")
            .define('P', plasticBars)
            .define('C', crtDisplay)
            .define('B', pcb)
            .unlockedBy(getHasName(crtDisplay), has(crtDisplay))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "circuit_controller_chassis")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, upgradeTemplate)
            .pattern("WIW")
            .pattern("PCP")
            .pattern("WIW")
            .define('W', copperWire)
            .define('I', ironPlate)
            .define('P', polymerPlate)
            .define('C', analogCircuit)
            .unlockedBy(getHasName(analogCircuit), has(analogCircuit))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "upgrade_template_from_analog")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, upgradeTemplate)
            .pattern("WIW")
            .pattern("PCP")
            .pattern("WIW")
            .define('W', copperWire)
            .define('I', pcBar)
            .define('P', polymerPlate)
            .define('C', basicCircuit)
            .unlockedBy(getHasName(basicCircuit), has(basicCircuit))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "upgrade_template_from_basic")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Objects.requireNonNull(HbmItems.UPGRADE_MUFFLER.get()), 16)
            .pattern("III")
            .pattern("IWI")
            .pattern("III")
            .define('I', rubberBar)
            .define('W', ItemTags.WOOL)
            .unlockedBy(getHasName(rubberBar), has(rubberBar))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "upgrade_muffler")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, Objects.requireNonNull(HbmItems.UPGRADE_5G.get()))
            .requires(upgradeTemplate)
            .requires(gemAlexandrite)
            .unlockedBy(getHasName(gemAlexandrite), has(gemAlexandrite))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "upgrade_5g")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, Objects.requireNonNull(HbmItems.FUSE.get()))
            .requires(steelPlate)
            .requires(polymerPlate)
            .requires(tungstenWire)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fuse")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Objects.requireNonNull(HbmItems.SAFETY_FUSE.get()), 8)
            .pattern("SSS")
            .pattern("SGS")
            .pattern("SSS")
            .define('S', Items.STRING)
            .define('G', Items.GUNPOWDER)
            .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "safety_fuse")));
    }

    private void buildPressSupportRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike machinePress = Objects.requireNonNull(HbmBlocks.MACHINE_PRESS.get());
        final ItemLike pressPreheater = Objects.requireNonNull(HbmBlocks.PRESS_PREHEATER.get());
        final ItemLike copperPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get());
        final ItemLike biomass = Objects.requireNonNull(HbmItems.BIOMASS.get());
        final ItemLike sawdust = Objects.requireNonNull(HbmItems.POWDER_SAWDUST.get());
        final ItemLike tungstenIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, machinePress)
            .pattern("IRI")
            .pattern("IPI")
            .pattern("IBI")
            .define('I', Items.IRON_INGOT)
            .define('R', Blocks.FURNACE)
            .define('P', Blocks.PISTON)
            .define('B', Blocks.IRON_BLOCK)
            .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "machine_press")));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pressPreheater)
            .pattern("CCC")
            .pattern("SLS")
            .pattern("TST")
            .define('C', copperPlate)
            .define('S', Blocks.STONE)
            .define('L', Items.LAVA_BUCKET)
            .define('T', tungstenIngot)
            .unlockedBy(getHasName(copperPlate), has(copperPlate))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "press_preheater")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PAPER, 3)
            .pattern("SSS")
            .define('S', sawdust)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "paper_from_sawdust")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Objects.requireNonNull(HbmItems.DUCTTAPE.get()), 4)
            .pattern("F")
            .pattern("P")
            .pattern("S")
            .define('F', Items.STRING)
            .define('P', Items.PAPER)
            .define('S', Items.SLIME_BALL)
            .unlockedBy("has_paper", has(Items.PAPER))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "ducttape")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(sawdust)
            .requires(sawdust)
            .requires(sawdust)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_sawdust")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Items.APPLE)
            .requires(Items.APPLE)
            .requires(Items.APPLE)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_apple")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Items.SUGAR_CANE)
            .requires(Items.SUGAR_CANE)
            .requires(Items.SUGAR_CANE)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_sugar_cane")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Items.ROTTEN_FLESH)
            .requires(Items.ROTTEN_FLESH)
            .requires(Items.ROTTEN_FLESH)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_rotten_flesh")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Items.CARROT)
            .requires(Items.CARROT)
            .requires(Items.CARROT)
            .requires(Items.CARROT)
            .requires(Items.CARROT)
            .requires(Items.CARROT)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_carrot")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Items.POTATO)
            .requires(Items.POTATO)
            .requires(Items.POTATO)
            .requires(Items.POTATO)
            .requires(Items.POTATO)
            .requires(Items.POTATO)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_potato")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(ItemTags.SAPLINGS)
            .requires(ItemTags.SAPLINGS)
            .requires(ItemTags.SAPLINGS)
            .requires(ItemTags.SAPLINGS)
            .requires(ItemTags.SAPLINGS)
            .requires(ItemTags.SAPLINGS)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_saplings")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(ItemTags.LEAVES)
            .requires(ItemTags.LEAVES)
            .requires(ItemTags.LEAVES)
            .requires(ItemTags.LEAVES)
            .requires(ItemTags.LEAVES)
            .requires(ItemTags.LEAVES)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_leaves")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Blocks.PUMPKIN)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_pumpkin")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Blocks.MELON)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_melon")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Blocks.CACTUS)
            .requires(Blocks.CACTUS)
            .requires(Blocks.CACTUS)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_cactus")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, biomass, 4)
            .requires(Items.SUGAR)
            .requires(sawdust)
            .requires(sawdust)
            .requires(Items.WHEAT)
            .requires(Items.WHEAT)
            .requires(Items.WHEAT)
            .requires(Items.WHEAT)
            .requires(Items.WHEAT)
            .requires(Items.WHEAT)
            .unlockedBy(getHasName(sawdust), has(sawdust))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "biomass_from_wheat")));
    }

    private void buildIcfRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike pelletEmpty = Objects.requireNonNull(HbmItems.ICF_PELLET_EMPTY.get());
        final ItemLike zirconiumWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ZIRCONIUM, HbmMaterialShape.WIRE).get());
        final ItemLike leadWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LEAD, HbmMaterialShape.WIRE).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pelletEmpty)
            .pattern("ZLZ")
            .pattern("L L")
            .pattern("ZLZ")
            .define('Z', zirconiumWire)
            .define('L', leadWire)
            .unlockedBy(getHasName(zirconiumWire), has(zirconiumWire))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "icf_pellet_empty")));
    }

    private void buildStampRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike stoneFlatStamp = Objects.requireNonNull(HbmItems.getStamp(StampItemType.STONE_FLAT).get());
        final ItemLike ironFlatStamp = Objects.requireNonNull(HbmItems.getStamp(StampItemType.IRON_FLAT).get());
        final ItemLike steelFlatStamp = Objects.requireNonNull(HbmItems.getStamp(StampItemType.STEEL_FLAT).get());
        final ItemLike titaniumFlatStamp = Objects.requireNonNull(HbmItems.getStamp(StampItemType.TITANIUM_FLAT).get());
        final ItemLike obsidianFlatStamp = Objects.requireNonNull(HbmItems.getStamp(StampItemType.OBSIDIAN_FLAT).get());
        final ItemLike deshFlatStamp = Objects.requireNonNull(HbmItems.getStamp(StampItemType.DESH_FLAT).get());
        final ItemLike steelIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get());
        final ItemLike titaniumIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.INGOT).get());
        final ItemLike deshIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DESH, HbmMaterialShape.INGOT).get());
        final ItemLike ferrouraniumIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.FERRORANIUM, HbmMaterialShape.INGOT).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, stoneFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.BRICK)
            .define('S', Ingredient.of(Tags.Items.STONE))
            .unlockedBy("has_brick", has(Items.BRICK))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_stone_flat_from_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, stoneFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.NETHER_BRICK)
            .define('S', Ingredient.of(Tags.Items.STONE))
            .unlockedBy("has_nether_brick", has(Items.NETHER_BRICK))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_stone_flat_from_nether_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ironFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.BRICK)
            .define('S', Tags.Items.INGOTS_IRON)
            .unlockedBy("has_brick", has(Items.BRICK))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_iron_flat_from_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ironFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.NETHER_BRICK)
            .define('S', Tags.Items.INGOTS_IRON)
            .unlockedBy("has_nether_brick", has(Items.NETHER_BRICK))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_iron_flat_from_nether_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, steelFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.BRICK)
            .define('S', steelIngot)
            .unlockedBy(getHasName(steelIngot), has(steelIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_steel_flat_from_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, steelFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.NETHER_BRICK)
            .define('S', steelIngot)
            .unlockedBy(getHasName(steelIngot), has(steelIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_steel_flat_from_nether_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, titaniumFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.BRICK)
            .define('S', titaniumIngot)
            .unlockedBy(getHasName(titaniumIngot), has(titaniumIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_titanium_flat_from_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, titaniumFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.NETHER_BRICK)
            .define('S', titaniumIngot)
            .unlockedBy(getHasName(titaniumIngot), has(titaniumIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_titanium_flat_from_nether_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, obsidianFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.BRICK)
            .define('S', Blocks.OBSIDIAN)
            .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_obsidian_flat_from_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, obsidianFlatStamp)
            .pattern("III")
            .pattern("SSS")
            .define('I', Items.NETHER_BRICK)
            .define('S', Blocks.OBSIDIAN)
            .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_obsidian_flat_from_nether_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, deshFlatStamp)
            .pattern("BDB")
            .pattern("DSD")
            .pattern("BDB")
            .define('B', Items.BRICK)
            .define('D', deshIngot)
            .define('S', ferrouraniumIngot)
            .unlockedBy(getHasName(deshIngot), has(deshIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_desh_flat_from_brick")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, deshFlatStamp)
            .pattern("BDB")
            .pattern("DSD")
            .pattern("BDB")
            .define('B', Items.NETHER_BRICK)
            .define('D', deshIngot)
            .define('S', ferrouraniumIngot)
            .unlockedBy(getHasName(deshIngot), has(deshIngot))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "stamp_desh_flat_from_nether_brick")));
    }

    private void buildFalloutRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike fallout = Objects.requireNonNull(HbmItems.FALLOUT.get());
        final ItemLike falloutLayer = Objects.requireNonNull(HbmItems.FALLOUT_LAYER.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, falloutLayer, 2)
            .pattern("FF")
            .define('F', fallout)
            .unlockedBy(getHasName(fallout), has(fallout))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fallout_layer")));
    }

    private void buildReadoutToolRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike ironAnvil = Objects.requireNonNull(HbmBlocks.ANVIL_IRON.get());
        final ItemLike leadAnvil = Objects.requireNonNull(HbmBlocks.ANVIL_LEAD.get());
        final ItemLike steelAnvil = Objects.requireNonNull(HbmBlocks.ANVIL_STEEL.get());
        final ItemLike murkyAnvil = Objects.requireNonNull(HbmBlocks.ANVIL_MURKY.get());
        final ItemLike dosimeter = Objects.requireNonNull(HbmItems.DOSIMETER.get());
        final ItemLike geigerCounter = Objects.requireNonNull(HbmItems.GEIGER_COUNTER.get());
        final ItemLike geigerBlock = Objects.requireNonNull(HbmBlocks.GEIGER.get());
        final ItemLike undefined = Objects.requireNonNull(HbmItems.UNDEFINED.get());
        final ItemLike vacuumTube = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.VACUUM_TUBE).get());
        final ItemLike berylliumIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BERYLLIUM, HbmMaterialShape.INGOT).get());
        final ItemLike leadIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LEAD, HbmMaterialShape.INGOT).get());
        final ItemLike leadBlock = Objects.requireNonNull(HbmBlocks.getMaterialBlock(MaterialBlockType.LEAD).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ironAnvil)
            .pattern("III")
            .pattern(" B ")
            .pattern("III")
            .define('I', Tags.Items.INGOTS_IRON)
            .define('B', Blocks.IRON_BLOCK)
            .unlockedBy("has_iron_block", has(Blocks.IRON_BLOCK))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "anvil_iron")));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, leadAnvil)
            .pattern("III")
            .pattern(" B ")
            .pattern("III")
            .define('I', leadIngot)
            .define('B', leadBlock)
            .unlockedBy(getHasName(leadBlock), has(leadBlock))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "anvil_lead")));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, murkyAnvil)
            .pattern("UUU")
            .pattern("UAU")
            .pattern("UUU")
            .define('U', undefined)
            .define('A', steelAnvil)
            .unlockedBy(getHasName(undefined), has(undefined))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "anvil_murky")));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, dosimeter)
            .pattern("WGW")
            .pattern("WCW")
            .pattern("WBW")
            .define('W', Ingredient.of(ItemTags.PLANKS))
            .define('G', Ingredient.of(Tags.Items.GLASS_PANES))
            .define('C', vacuumTube)
            .define('B', berylliumIngot)
            .unlockedBy(getHasName(vacuumTube), has(vacuumTube))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "dosimeter")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, geigerBlock)
            .requires(geigerCounter)
            .unlockedBy(getHasName(geigerCounter), has(geigerCounter))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "geiger_from_geiger_counter")));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, geigerCounter)
            .requires(geigerBlock)
            .unlockedBy(getHasName(geigerBlock), has(geigerBlock))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "geiger_counter_from_geiger")));
    }

    private void buildServiceToolRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike blowtorch = Objects.requireNonNull(HbmItems.BLOWTORCH.get());
        final ItemLike acetyleneTorch = Objects.requireNonNull(HbmItems.ACETYLENE_TORCH.get());
        final ItemLike copperPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get());
        final ItemLike steelPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.PLATE).get());
        final ItemLike polymerIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.INGOT).get());
        final ItemLike bakeliteIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BAKELITE, HbmMaterialShape.INGOT).get());
        final ItemLike rubberIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.RUBBER, HbmMaterialShape.INGOT).get());
        final ItemLike tankSteel = Objects.requireNonNull(HbmItems.TANK_STEEL.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, blowtorch)
            .pattern("CC ")
            .pattern(" I ")
            .pattern("CCC")
            .define('C', copperPlate)
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(copperPlate), has(copperPlate))
            .save(recipeOutput, rl("blowtorch"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, acetyleneTorch)
            .pattern("SS ")
            .pattern(" PS")
            .pattern(" T ")
            .define('S', steelPlate)
            .define('P', Ingredient.of(polymerIngot, bakeliteIngot, rubberIngot))
            .define('T', tankSteel)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, rl("acetylene_torch"));
    }

    private void buildBladeRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike steelPlate = HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.PLATE).get();
        final ItemLike steelIngot = HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get();
        final ItemLike titaniumPlate = HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE).get();
        final ItemLike titaniumIngot = HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.INGOT).get();
        final ItemLike alloyPlate = HbmItems.getMaterialPart(HbmMaterials.ADVANCED_ALLOY, HbmMaterialShape.PLATE).get();
        final ItemLike alloyIngot = HbmItems.getMaterialPart(HbmMaterials.ADVANCED_ALLOY, HbmMaterialShape.INGOT).get();
        final ItemLike deshPlate = HbmItems.getMaterialPart(HbmMaterials.DESH, HbmMaterialShape.CAST_PLATE).get();
        final ItemLike deshIngot = HbmItems.getMaterialPart(HbmMaterials.DESH, HbmMaterialShape.INGOT).get();

        // blades_steel: " P " / "PIP" / " P "
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HbmItems.BLADES_STEEL.get())
            .pattern(" P ").pattern("PIP").pattern(" P ")
            .define('P', steelPlate).define('I', steelIngot)
            .unlockedBy(getHasName(steelPlate), has(steelPlate))
            .save(recipeOutput, rl("blades_steel"));

        // blades_titanium
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HbmItems.BLADES_TITANIUM.get())
            .pattern(" P ").pattern("PIP").pattern(" P ")
            .define('P', titaniumPlate).define('I', titaniumIngot)
            .unlockedBy(getHasName(titaniumPlate), has(titaniumPlate))
            .save(recipeOutput, rl("blades_titanium"));

        // blades_advanced_alloy
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HbmItems.BLADES_ADVANCED_ALLOY.get())
            .pattern(" P ").pattern("PIP").pattern(" P ")
            .define('P', alloyPlate).define('I', alloyIngot)
            .unlockedBy(getHasName(alloyPlate), has(alloyPlate))
            .save(recipeOutput, rl("blades_advanced_alloy"));

        // blades_desh: " P " / "PBP" / " P "
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HbmItems.BLADES_DESH.get())
            .pattern(" P ").pattern("PIP").pattern(" P ")
            .define('P', deshPlate).define('I', deshIngot)
            .unlockedBy(getHasName(deshPlate), has(deshPlate))
            .save(recipeOutput, rl("blades_desh"));
    }

    private static ResourceLocation rl(final String path) {
        return Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, path));
    }

    private void buildTwoByTwoBlockRecipe(final Consumer<FinishedRecipe> recipeOutput, final ItemLike output, final ItemLike ingredient, final String recipeName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
            .pattern("CC")
            .pattern("CC")
            .define('C', ingredient)
            .unlockedBy(getHasName(ingredient), has(ingredient))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, recipeName)));
    }
}

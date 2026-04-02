package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
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
            buildDustSmelting(recipeOutput, material);
        }

        buildBasaltRecipes(recipeOutput);
        buildFalloutRecipes(recipeOutput);
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

    private void buildFalloutRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike fallout = Objects.requireNonNull(HbmItems.FALLOUT.get());
        final ItemLike falloutLayer = Objects.requireNonNull(HbmItems.FALLOUT_LAYER.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, falloutLayer, 2)
            .pattern("FF")
            .define('F', fallout)
            .unlockedBy(getHasName(fallout), has(fallout))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "fallout_layer")));
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

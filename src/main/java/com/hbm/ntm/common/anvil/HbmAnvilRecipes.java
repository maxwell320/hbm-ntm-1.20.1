package com.hbm.ntm.common.anvil;

import com.hbm.ntm.common.block.NtmAnvilBlock;
import com.hbm.ntm.common.item.StampItemType;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public final class HbmAnvilRecipes {
    public static final List<ConstructionRecipe> CONSTRUCTION_RECIPES = List.of(
        construction(StampItemType.STONE_FLAT, StampItemType.STONE_PLATE, NtmAnvilBlock.TIER_IRON),
        construction(StampItemType.STONE_FLAT, StampItemType.STONE_WIRE, NtmAnvilBlock.TIER_IRON),
        construction(StampItemType.STONE_FLAT, StampItemType.STONE_CIRCUIT, NtmAnvilBlock.TIER_IRON),
        construction(StampItemType.IRON_FLAT, StampItemType.IRON_PLATE, NtmAnvilBlock.TIER_IRON),
        construction(StampItemType.IRON_FLAT, StampItemType.IRON_WIRE, NtmAnvilBlock.TIER_IRON),
        construction(StampItemType.IRON_FLAT, StampItemType.IRON_CIRCUIT, NtmAnvilBlock.TIER_IRON),
        construction(StampItemType.STEEL_FLAT, StampItemType.STEEL_PLATE, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.STEEL_FLAT, StampItemType.STEEL_WIRE, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.STEEL_FLAT, StampItemType.STEEL_CIRCUIT, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.TITANIUM_FLAT, StampItemType.TITANIUM_PLATE, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.TITANIUM_FLAT, StampItemType.TITANIUM_WIRE, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.TITANIUM_FLAT, StampItemType.TITANIUM_CIRCUIT, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.OBSIDIAN_FLAT, StampItemType.OBSIDIAN_PLATE, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.OBSIDIAN_FLAT, StampItemType.OBSIDIAN_WIRE, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.OBSIDIAN_FLAT, StampItemType.OBSIDIAN_CIRCUIT, NtmAnvilBlock.TIER_STEEL),
        construction(StampItemType.DESH_FLAT, StampItemType.DESH_PLATE, NtmAnvilBlock.TIER_DESH),
        construction(StampItemType.DESH_FLAT, StampItemType.DESH_WIRE, NtmAnvilBlock.TIER_DESH),
        construction(StampItemType.DESH_FLAT, StampItemType.DESH_CIRCUIT, NtmAnvilBlock.TIER_DESH)
    );

    public static final List<SmithingRecipe> SMITHING_RECIPES = List.of(
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_IRON, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_DESH, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DESH, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_STEEL, stamp(StampItemType.IRON_C9), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.IRON_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GUNMETAL, HbmMaterialShape.INGOT).get())), 2),
        smithing(NtmAnvilBlock.TIER_STEEL, stamp(StampItemType.IRON_C50), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.IRON_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GUNMETAL, HbmMaterialShape.INGOT).get())), 2),
        smithing(NtmAnvilBlock.TIER_DESH, stamp(StampItemType.DESH_C9), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.DESH_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.INGOT).get())), 4),
        smithing(NtmAnvilBlock.TIER_DESH, stamp(StampItemType.DESH_C50), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.DESH_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.INGOT).get())), 4)
    );

    private HbmAnvilRecipes() {
    }

    public static List<ConstructionRecipe> constructionRecipesForTier(final int tier) {
        return CONSTRUCTION_RECIPES.stream().filter(recipe -> recipe.tier() <= tier).toList();
    }

    public static List<SmithingRecipe> smithingRecipesForTier(final int tier) {
        return SMITHING_RECIPES.stream().filter(recipe -> recipe.tier() <= tier).toList();
    }

    private static ConstructionRecipe construction(final StampItemType input, final StampItemType output, final int tier) {
        return new ConstructionRecipe(stamp(input), stamp(output), tier);
    }

    private static SmithingRecipe smithing(final int tier, final Supplier<? extends ItemLike> output, final Ingredient left, final int leftCount,
                                           final Ingredient right, final int rightCount) {
        return new SmithingRecipe(tier, () -> Objects.requireNonNull(output.get()).asItem(), left, leftCount, right, rightCount);
    }

    private static Supplier<Item> stamp(final StampItemType type) {
        return () -> Objects.requireNonNull(HbmItems.getStamp(type).get());
    }

    public record ConstructionRecipe(Supplier<Item> input, Supplier<Item> output, int tier) {
        public Item inputItem() {
            return Objects.requireNonNull(this.input.get());
        }

        public Item outputItem() {
            return Objects.requireNonNull(this.output.get());
        }

        public ItemStack inputStack() {
            return new ItemStack(inputItem());
        }

        public ItemStack outputStack() {
            return new ItemStack(outputItem());
        }
    }

    public record SmithingRecipe(int tier, Supplier<Item> output, Ingredient left, int leftCount, Ingredient right, int rightCount) {
        public boolean matches(final ItemStack leftStack, final ItemStack rightStack) {
            return this.left.test(leftStack) && leftStack.getCount() >= this.leftCount
                && this.right.test(rightStack) && rightStack.getCount() >= this.rightCount;
        }

        public ItemStack outputStack() {
            return new ItemStack(Objects.requireNonNull(this.output.get()));
        }
    }
}

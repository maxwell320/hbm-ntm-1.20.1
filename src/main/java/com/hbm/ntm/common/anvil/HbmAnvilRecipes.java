package com.hbm.ntm.common.anvil;

import com.hbm.ntm.common.block.NtmAnvilBlock;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.StampItemType;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("null")
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
        construction(StampItemType.DESH_FLAT, StampItemType.DESH_CIRCUIT, NtmAnvilBlock.TIER_DESH),
        construction(HbmBlocks.MACHINE_SOLDERING_STATION,
            List.of(
                ingredient(Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.CAST_PLATE).get())), 2),
                ingredient(Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.DENSE_WIRE).get())), 4),
                ingredient(Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.BOLT).get())), 4),
                ingredient(Ingredient.of(Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.VACUUM_TUBE).get())), 2)
            ),
            NtmAnvilBlock.TIER_STEEL),
        construction(HbmBlocks.MACHINE_ROTARY_FURNACE,
            List.of(
                ingredient(Ingredient.of(Items.STONE_BRICKS), 8),
                ingredient(Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.FIREBRICK, HbmMaterialShape.INGOT).get())), 16),
                ingredient(Ingredient.of(Items.IRON_INGOT), 4),
                ingredient(Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get())), 8)
            ),
            NtmAnvilBlock.TIER_STEEL)
    );

    public static final List<SmithingRecipe> SMITHING_RECIPES = List.of(
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_STEEL, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_LEAD, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LEAD, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_DESH, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DESH, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_STEEL, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_DESH, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DESH, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_SATURNITE, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_FERRORANIUM, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.FERRORANIUM, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_BISMUTH_BRONZE, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BISMUTH_BRONZE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_ARSENIC_BRONZE, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ARSENIC_BRONZE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_SCHRABIDATE, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SCHRABIDATE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_DNT, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DINEUTRONIUM, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_OSMIRIDIUM, Ingredient.of(HbmBlocks.ANVIL_IRON.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.OSMIRIDIUM, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_SATURNITE, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_FERRORANIUM, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.FERRORANIUM, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_BISMUTH_BRONZE, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BISMUTH_BRONZE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_ARSENIC_BRONZE, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ARSENIC_BRONZE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_SCHRABIDATE, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SCHRABIDATE, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_DNT, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.DINEUTRONIUM, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_IRON, HbmBlocks.ANVIL_OSMIRIDIUM, Ingredient.of(HbmBlocks.ANVIL_LEAD.get()), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.OSMIRIDIUM, HbmMaterialShape.INGOT).get())), 10),
        smithing(NtmAnvilBlock.TIER_STEEL, stamp(StampItemType.IRON_C9), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.IRON_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GUNMETAL, HbmMaterialShape.INGOT).get())), 2),
        smithing(NtmAnvilBlock.TIER_STEEL, stamp(StampItemType.IRON_C50), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.IRON_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GUNMETAL, HbmMaterialShape.INGOT).get())), 2),
        smithing(NtmAnvilBlock.TIER_DESH, stamp(StampItemType.DESH_C9), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.DESH_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.INGOT).get())), 4),
        smithing(NtmAnvilBlock.TIER_DESH, stamp(StampItemType.DESH_C50), Ingredient.of(Objects.requireNonNull(HbmItems.getStamp(StampItemType.DESH_FLAT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.INGOT).get())), 4),
        smithing(NtmAnvilBlock.TIER_IRON, material(HbmMaterials.GUNMETAL, HbmMaterialShape.INGOT),
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.INGOT).get())), 1,
            Ingredient.of(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.INGOT).get())), 1),
        smithingSpecial(NtmAnvilBlock.TIER_IRON, 1, 1, HbmAnvilRecipes::renameMatches, HbmAnvilRecipes::renameOutput)
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
        return construction(stamp(output), List.of(ingredient(Ingredient.of(stamp(input).get()), 1)), tier);
    }

    private static ConstructionRecipe construction(final Supplier<? extends ItemLike> output,
                                                   final List<ConstructionIngredient> ingredients,
                                                   final int tier) {
        return new ConstructionRecipe(() -> Objects.requireNonNull(output.get()).asItem(), List.copyOf(ingredients), tier);
    }

    private static ConstructionIngredient ingredient(final Ingredient ingredient, final int count) {
        return new ConstructionIngredient(ingredient, count);
    }

    private static SmithingRecipe smithing(final int tier, final Supplier<? extends ItemLike> output, final Ingredient left, final int leftCount,
                                           final Ingredient right, final int rightCount) {
        return new SmithingRecipe(tier, () -> Objects.requireNonNull(output.get()).asItem(), left, leftCount, right, rightCount, null, null);
    }

    private static SmithingRecipe smithingSpecial(final int tier, final int leftCount, final int rightCount,
                                                  final BiPredicate<ItemStack, ItemStack> matcher,
                                                  final BiFunction<ItemStack, ItemStack, ItemStack> outputFactory) {
        return new SmithingRecipe(tier, () -> Items.AIR, Ingredient.EMPTY, leftCount, Ingredient.EMPTY, rightCount, matcher, outputFactory);
    }

    private static Supplier<Item> stamp(final StampItemType type) {
        return () -> Objects.requireNonNull(HbmItems.getStamp(type).get());
    }

    private static Supplier<Item> material(final com.hbm.ntm.common.material.HbmMaterialDefinition material, final HbmMaterialShape shape) {
        return () -> Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
    }

    private static boolean renameMatches(final ItemStack left, final ItemStack right) {
        return !left.isEmpty() && right.is(Items.NAME_TAG) && right.hasCustomHoverName();
    }

    private static ItemStack renameOutput(final ItemStack left, final ItemStack right) {
        final ItemStack out = left.copy();
        out.setCount(1);
        out.setHoverName(parseLegacyName(right.getHoverName().getString()));
        return out;
    }

    private static MutableComponent parseLegacyName(final String rawName) {
        final String formatted = rawName.replace("\\&", "§");
        final MutableComponent result = Component.empty();
        final StringBuilder segment = new StringBuilder();
        Style style = Style.EMPTY;

        for (int i = 0; i < formatted.length(); i++) {
            final char c = formatted.charAt(i);
            if (c == '§' && i + 1 < formatted.length()) {
                final ChatFormatting formatting = ChatFormatting.getByCode(formatted.charAt(i + 1));
                if (formatting != null) {
                    if (segment.length() > 0) {
                        result.append(Component.literal(segment.toString()).setStyle(style));
                        segment.setLength(0);
                    }
                    style = formatting == ChatFormatting.RESET ? Style.EMPTY : style.applyLegacyFormat(formatting);
                    i++;
                    continue;
                }
            }
            segment.append(c);
        }

        if (segment.length() > 0) {
            result.append(Component.literal(segment.toString()).setStyle(style));
        }

        return result;
    }

    public record ConstructionRecipe(Supplier<Item> output, List<ConstructionIngredient> ingredients, int tier) {
        public Item outputItem() {
            return Objects.requireNonNull(this.output.get());
        }

        public ItemStack outputStack() {
            return new ItemStack(outputItem());
        }
    }

    public record ConstructionIngredient(Ingredient ingredient, int count) {
        public ConstructionIngredient {
            if (count <= 0) {
                throw new IllegalArgumentException("Construction ingredient count must be > 0");
            }
        }

        public ItemStack displayStack() {
            final ItemStack[] stacks = this.ingredient.getItems();
            if (stacks.length <= 0) {
                return ItemStack.EMPTY;
            }
            final ItemStack display = stacks[0].copy();
            display.setCount(this.count);
            return display;
        }
    }

    public record SmithingRecipe(int tier, Supplier<Item> output, Ingredient left, int leftCount, Ingredient right, int rightCount,
                                 BiPredicate<ItemStack, ItemStack> matcher, BiFunction<ItemStack, ItemStack, ItemStack> outputFactory) {
        public boolean matches(final ItemStack leftStack, final ItemStack rightStack) {
            if (this.matcher != null) {
                return this.matcher.test(leftStack, rightStack);
            }
            return this.left.test(leftStack) && leftStack.getCount() >= this.leftCount
                && this.right.test(rightStack) && rightStack.getCount() >= this.rightCount;
        }

        public ItemStack outputStack(final ItemStack leftStack, final ItemStack rightStack) {
            if (this.outputFactory != null) {
                return this.outputFactory.apply(leftStack, rightStack);
            }
            return new ItemStack(Objects.requireNonNull(this.output.get()));
        }
    }
}

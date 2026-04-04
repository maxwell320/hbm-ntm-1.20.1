package com.hbm.ntm.common.press;

import com.hbm.ntm.common.item.BriquetteItemType;
import com.hbm.ntm.common.item.CasingItemType;
import com.hbm.ntm.common.item.CokeItemType;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.StampItem;
import com.hbm.ntm.common.item.StampItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("null")
public final class HbmPressRecipes {
    private static final List<PressRecipe> RECIPES = new ArrayList<>();
    private static boolean initialized;

    private HbmPressRecipes() {
    }

    public static List<PressRecipe> all() {
        ensureInitialized();
        return List.copyOf(RECIPES);
    }

    public static Optional<PressRecipe> findRecipe(final ItemStack inputStack, final ItemStack stampStack) {
        ensureInitialized();
        if (inputStack.isEmpty() || stampStack.isEmpty() || !(stampStack.getItem() instanceof final StampItem stampItem)) {
            return Optional.empty();
        }
        final PressStampType stampType = PressStampType.from(stampItem.type());
        if (stampType == null) {
            return Optional.empty();
        }
        return RECIPES.stream().filter(recipe -> recipe.matches(inputStack, stampType)).findFirst();
    }

    public static ItemStack getOutput(final ItemStack inputStack, final ItemStack stampStack) {
        return findRecipe(inputStack, stampStack).map(PressRecipe::resultCopy).orElse(ItemStack.EMPTY);
    }

    private static synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }
        initialized = true;
        registerDefaults();
    }

    private static void registerDefaults() {
        add(PressStampType.FLAT, Ingredient.of(item(HbmMaterials.QUARTZ, HbmMaterialShape.DUST)), new ItemStack(Items.QUARTZ));
        add(PressStampType.FLAT, Ingredient.of(item(HbmMaterials.LAPIS, HbmMaterialShape.DUST)), new ItemStack(Items.LAPIS_LAZULI));
        add(PressStampType.FLAT, Ingredient.of(item(HbmMaterials.DIAMOND, HbmMaterialShape.DUST)), new ItemStack(Items.DIAMOND));
        add(PressStampType.FLAT, Ingredient.of(item(HbmMaterials.EMERALD, HbmMaterialShape.DUST)), new ItemStack(Items.EMERALD));
        add(PressStampType.FLAT, Ingredient.of(Objects.requireNonNull(HbmItems.BIOMASS.get())), new ItemStack(Objects.requireNonNull(HbmItems.BIOMASS_COMPRESSED.get())));
        add(PressStampType.FLAT,
            Ingredient.of(
                Objects.requireNonNull(HbmItems.getCoke(CokeItemType.COAL).get()),
                Objects.requireNonNull(HbmItems.getCoke(CokeItemType.LIGNITE).get()),
                Objects.requireNonNull(HbmItems.getCoke(CokeItemType.PETROLEUM).get())),
            new ItemStack(item(HbmMaterials.GRAPHITE, HbmMaterialShape.INGOT)));
        add(PressStampType.FLAT, Ingredient.of(Blocks.JUNGLE_LOG), new ItemStack(item(HbmMaterials.LATEX, HbmMaterialShape.GEM)));
        add(PressStampType.FLAT,
            Ingredient.of(item(HbmMaterials.COAL, HbmMaterialShape.DUST)),
            new ItemStack(Objects.requireNonNull(HbmItems.getBriquette(BriquetteItemType.COAL).get())));
        add(PressStampType.FLAT,
            Ingredient.of(item(HbmMaterials.LIGNITE, HbmMaterialShape.DUST)),
            new ItemStack(Objects.requireNonNull(HbmItems.getBriquette(BriquetteItemType.LIGNITE).get())));
        add(PressStampType.FLAT,
            Ingredient.of(Objects.requireNonNull(HbmItems.POWDER_SAWDUST.get())),
            new ItemStack(Objects.requireNonNull(HbmItems.getBriquette(BriquetteItemType.WOOD).get())));

        addPlateRecipe(HbmMaterials.IRON);
        addPlateRecipe(HbmMaterials.GOLD);
        addPlateRecipe(HbmMaterials.TITANIUM);
        addPlateRecipe(HbmMaterials.ALUMINIUM);
        addPlateRecipe(HbmMaterials.STEEL);
        addPlateRecipe(HbmMaterials.LEAD);
        addPlateRecipe(HbmMaterials.COPPER);
        addPlateRecipe(HbmMaterials.ADVANCED_ALLOY);
        addPlateRecipe(HbmMaterials.SCHRABIDIUM);
        addPlateRecipe(HbmMaterials.COMBINE_STEEL);
        addPlateRecipe(HbmMaterials.GUNMETAL);
        addPlateRecipe(HbmMaterials.WEAPONSTEEL);
        addPlateRecipe(HbmMaterials.SATURNITE);
        addPlateRecipe(HbmMaterials.DURA_STEEL);

        add(PressStampType.C9,
            Ingredient.of(item(HbmMaterials.GUNMETAL, HbmMaterialShape.PLATE)),
            new ItemStack(Objects.requireNonNull(HbmItems.getCasing(CasingItemType.SMALL).get()), 4));
        add(PressStampType.C50,
            Ingredient.of(item(HbmMaterials.GUNMETAL, HbmMaterialShape.PLATE)),
            new ItemStack(Objects.requireNonNull(HbmItems.getCasing(CasingItemType.LARGE).get()), 2));
        add(PressStampType.C9,
            Ingredient.of(item(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.PLATE)),
            new ItemStack(Objects.requireNonNull(HbmItems.getCasing(CasingItemType.SMALL_STEEL).get()), 4));
        add(PressStampType.C50,
            Ingredient.of(item(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.PLATE)),
            new ItemStack(Objects.requireNonNull(HbmItems.getCasing(CasingItemType.LARGE_STEEL).get()), 2));

        for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {
            if (material.hasShape(HbmMaterialShape.INGOT) && material.hasShape(HbmMaterialShape.WIRE)) {
                add(PressStampType.WIRE,
                    Ingredient.of(item(material, HbmMaterialShape.INGOT)),
                    new ItemStack(item(material, HbmMaterialShape.WIRE), 8));
            }
        }

        add(PressStampType.CIRCUIT,
            Ingredient.of(item(HbmMaterials.SILICON, HbmMaterialShape.BILLET)),
            new ItemStack(Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.SILICON).get())));
    }

    private static void addPlateRecipe(final HbmMaterialDefinition material) {
        add(PressStampType.PLATE,
            Ingredient.of(item(material, HbmMaterialShape.INGOT)),
            new ItemStack(item(material, HbmMaterialShape.PLATE)));
    }

    private static void add(final PressStampType stampType, final Ingredient input, final ItemStack output) {
        RECIPES.add(new PressRecipe(input, stampType, output.copy()));
    }

    private static Item item(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        return Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
    }

    public record PressRecipe(Ingredient input, PressStampType stampType, ItemStack output) {
        public boolean matches(final ItemStack inputStack, final PressStampType activeStampType) {
            return this.stampType == activeStampType && this.input.test(inputStack);
        }

        public ItemStack resultCopy() {
            return this.output.copy();
        }
    }

    public enum PressStampType {
        FLAT,
        PLATE,
        WIRE,
        CIRCUIT,
        C9,
        C50;

        public static PressStampType from(final StampItemType type) {
            return switch (type) {
                case STONE_FLAT, IRON_FLAT, STEEL_FLAT, TITANIUM_FLAT, OBSIDIAN_FLAT, DESH_FLAT -> FLAT;
                case STONE_PLATE, IRON_PLATE, STEEL_PLATE, TITANIUM_PLATE, OBSIDIAN_PLATE, DESH_PLATE -> PLATE;
                case STONE_WIRE, IRON_WIRE, STEEL_WIRE, TITANIUM_WIRE, OBSIDIAN_WIRE, DESH_WIRE -> WIRE;
                case STONE_CIRCUIT, IRON_CIRCUIT, STEEL_CIRCUIT, TITANIUM_CIRCUIT, OBSIDIAN_CIRCUIT, DESH_CIRCUIT -> CIRCUIT;
                case IRON_C9, DESH_C9 -> C9;
                case IRON_C50, DESH_C50 -> C50;
            };
        }
    }
}

package com.hbm.ntm.common.soldering;

import com.hbm.ntm.common.item.CircuitItemType;
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
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("null")
public final class HbmSolderingRecipes {
    private static final List<SolderingRecipe> RECIPES = List.of(
        circuit(CircuitItemType.ANALOG, 100, 100,
            List.of(circuitIngredient(CircuitItemType.VACUUM_TUBE, 3), circuitIngredient(CircuitItemType.CAPACITOR, 2)),
            List.of(circuitIngredient(CircuitItemType.PCB, 4)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 4))),
        circuit(CircuitItemType.BASIC, 200, 250,
            List.of(circuitIngredient(CircuitItemType.CHIP, 4)),
            List.of(circuitIngredient(CircuitItemType.PCB, 4)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 4)))
    );

    private HbmSolderingRecipes() {
    }

    public static List<SolderingRecipe> all() {
        return RECIPES;
    }

    public static Optional<SolderingRecipe> findRecipe(final ItemStack... inputs) {
        if (inputs.length < 6) {
            return Optional.empty();
        }
        return findRecipe(
            List.of(stackOrEmpty(inputs[0]), stackOrEmpty(inputs[1]), stackOrEmpty(inputs[2])),
            List.of(stackOrEmpty(inputs[3]), stackOrEmpty(inputs[4])),
            List.of(stackOrEmpty(inputs[5])));
    }

    public static Optional<SolderingRecipe> findRecipe(final List<ItemStack> toppings, final List<ItemStack> pcb, final List<ItemStack> solder) {
        return RECIPES.stream().filter(recipe -> recipe.matches(toppings, pcb, solder)).findFirst();
    }

    private static SolderingRecipe circuit(final CircuitItemType outputType, final int duration, final long consumption,
                                           final List<IngredientStack> toppings, final List<IngredientStack> pcb, final List<IngredientStack> solder) {
        return new SolderingRecipe(new ItemStack(circuitItem(outputType)), duration, consumption, List.copyOf(toppings), List.copyOf(pcb), List.copyOf(solder));
    }

    private static IngredientStack circuitIngredient(final CircuitItemType type, final int count) {
        return new IngredientStack(Ingredient.of(circuitItem(type)), count);
    }

    private static IngredientStack materialIngredient(final HbmMaterialDefinition material, final HbmMaterialShape shape, final int count) {
        return new IngredientStack(Ingredient.of(materialItem(material, shape)), count);
    }

    private static Item circuitItem(final CircuitItemType type) {
        return Objects.requireNonNull(HbmItems.getCircuit(type).get());
    }

    private static Item materialItem(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        return Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
    }

    private static ItemStack stackOrEmpty(final ItemStack stack) {
        return stack == null ? ItemStack.EMPTY : stack;
    }

    private static boolean matchesGroup(final List<ItemStack> inputs, final List<IngredientStack> recipe) {
        final List<IngredientStack> remaining = new ArrayList<>(recipe);
        for (final ItemStack input : inputs) {
            if (input == null || input.isEmpty()) {
                continue;
            }
            boolean hasMatch = false;
            for (int i = 0; i < remaining.size(); i++) {
                if (remaining.get(i).matches(input)) {
                    remaining.remove(i);
                    hasMatch = true;
                    break;
                }
            }
            if (!hasMatch) {
                return false;
            }
        }
        return remaining.isEmpty();
    }

    public record IngredientStack(Ingredient ingredient, int count) {
        public boolean matches(final ItemStack stack) {
            return stack.getCount() >= this.count && this.ingredient.test(stack);
        }
    }

    public record SolderingRecipe(ItemStack output, int duration, long consumption, List<IngredientStack> toppings,
                                  List<IngredientStack> pcb, List<IngredientStack> solder) {
        public boolean matches(final List<ItemStack> toppings, final List<ItemStack> pcb, final List<ItemStack> solder) {
            return matchesGroup(toppings, this.toppings)
                && matchesGroup(pcb, this.pcb)
                && matchesGroup(solder, this.solder);
        }

        public ItemStack outputCopy() {
            return this.output.copy();
        }
    }
}

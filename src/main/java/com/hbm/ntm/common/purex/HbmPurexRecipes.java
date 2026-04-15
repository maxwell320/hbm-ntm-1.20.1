package com.hbm.ntm.common.purex;

import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.recipe.CountedIngredient;
import com.hbm.ntm.common.recipe.MachineRecipeRegistry;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("null")
public final class HbmPurexRecipes {
    private static final PurexRecipeRegistry REGISTRY = new PurexRecipeRegistry();

    private HbmPurexRecipes() {
    }

    public static List<PurexRecipe> all() {
        return REGISTRY.all();
    }

    public static Optional<PurexRecipe> findById(final String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return REGISTRY.findFirst(recipe -> recipe.id().equals(id));
    }

    public static Optional<PurexRecipe> findRecipe(final List<ItemStack> itemInputs, final List<FluidStack> fluidInputs) {
        return REGISTRY.findFirst(recipe -> recipe.matches(itemInputs, fluidInputs));
    }

    public record PurexRecipe(String id,
                              int duration,
                              int powerPerTick,
                              List<CountedIngredient> itemInputs,
                              List<FluidStack> fluidInputs,
                              List<ItemStack> itemOutputs,
                              List<FluidStack> fluidOutputs) {
        public PurexRecipe {
            id = Objects.requireNonNull(id, "id");
            duration = Math.max(1, duration);
            powerPerTick = Math.max(1, powerPerTick);
            itemInputs = List.copyOf(Objects.requireNonNull(itemInputs, "itemInputs"));
            fluidInputs = List.copyOf(Objects.requireNonNull(fluidInputs, "fluidInputs").stream().map(FluidStack::copy).toList());
            itemOutputs = List.copyOf(Objects.requireNonNull(itemOutputs, "itemOutputs").stream().map(ItemStack::copy).toList());
            fluidOutputs = List.copyOf(Objects.requireNonNull(fluidOutputs, "fluidOutputs").stream().map(FluidStack::copy).toList());
        }

        public boolean matches(final List<ItemStack> items, final List<FluidStack> fluids) {
            return matchesItemRequirements(items, this.itemInputs)
                && matchesFluidRequirements(fluids, this.fluidInputs);
        }

        public List<ItemStack> itemOutputsCopy() {
            return this.itemOutputs.stream().map(ItemStack::copy).toList();
        }

        public List<FluidStack> fluidOutputsCopy() {
            return this.fluidOutputs.stream().map(FluidStack::copy).toList();
        }

        public List<FluidStack> fluidInputsCopy() {
            return this.fluidInputs.stream().map(FluidStack::copy).toList();
        }

        public List<CountedIngredient> itemInputsCopy() {
            return List.copyOf(this.itemInputs);
        }
    }

    private static final class PurexRecipeRegistry extends MachineRecipeRegistry<PurexRecipe> {
        @Override
        protected void registerDefaults() {
            this.addRecipe(new PurexRecipe(
                "purex.icf",
                300,
                10_000,
                List.of(new CountedIngredient(Ingredient.of(item(HbmItems.ICF_PELLET_DEPLETED)), 1)),
                List.of(),
                List.of(
                    new ItemStack(item(HbmItems.ICF_PELLET_EMPTY), 1),
                    new ItemStack(item(HbmItems.PELLET_CHARGED), 1),
                    new ItemStack(item(HbmItems.getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.DUST)), 1)),
                List.of(new FluidStack(HbmFluids.HELIUM4.getStillFluid(), 1_250))));
        }
    }

    private static boolean matchesItemRequirements(final List<ItemStack> available, final List<CountedIngredient> required) {
        if (required.isEmpty()) {
            for (final ItemStack stack : available) {
                if (stack != null && !stack.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        final int[] remainingCounts = new int[available.size()];
        for (int i = 0; i < available.size(); i++) {
            final ItemStack stack = available.get(i);
            remainingCounts[i] = stack == null || stack.isEmpty() ? 0 : stack.getCount();
        }

        for (final CountedIngredient ingredient : required) {
            int needed = ingredient.count();
            for (int i = 0; i < available.size() && needed > 0; i++) {
                final ItemStack stack = available.get(i);
                if (stack == null || stack.isEmpty() || remainingCounts[i] <= 0) {
                    continue;
                }
                if (!ingredient.ingredient().test(stack)) {
                    continue;
                }
                final int consumed = Math.min(needed, remainingCounts[i]);
                needed -= consumed;
                remainingCounts[i] -= consumed;
            }
            if (needed > 0) {
                return false;
            }
        }

        for (final ItemStack stack : available) {
            if (stack == null || stack.isEmpty()) {
                continue;
            }
            final boolean matchesAny = required.stream().anyMatch(ingredient -> ingredient.ingredient().test(stack));
            if (!matchesAny) {
                return false;
            }
        }

        return true;
    }

    private static boolean matchesFluidRequirements(final List<FluidStack> available, final List<FluidStack> required) {
        if (required.isEmpty()) {
            return true;
        }

        final int[] remaining = new int[available.size()];
        for (int i = 0; i < available.size(); i++) {
            final FluidStack stack = available.get(i);
            remaining[i] = stack == null || stack.isEmpty() ? 0 : stack.getAmount();
        }

        for (final FluidStack requirement : required) {
            if (requirement == null || requirement.isEmpty()) {
                continue;
            }
            int needed = requirement.getAmount();
            for (int i = 0; i < available.size() && needed > 0; i++) {
                final FluidStack stack = available.get(i);
                if (stack == null || stack.isEmpty() || remaining[i] <= 0) {
                    continue;
                }
                if (!stack.isFluidEqual(requirement)) {
                    continue;
                }
                final int consumed = Math.min(needed, remaining[i]);
                needed -= consumed;
                remaining[i] -= consumed;
            }
            if (needed > 0) {
                return false;
            }
        }

        return true;
    }

    private static Item item(final net.minecraftforge.registries.RegistryObject<Item> item) {
        return Objects.requireNonNull(item.get());
    }
}

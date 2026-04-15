package com.hbm.ntm.common.cyclotron;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.recipe.MachineRecipeRegistry;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("null")
public final class HbmCyclotronRecipes {
    private static final CyclotronRecipeRegistry REGISTRY = new CyclotronRecipeRegistry();

    private HbmCyclotronRecipes() {
    }

    public static List<CyclotronRecipe> all() {
        return REGISTRY.all();
    }

    public static Optional<CyclotronRecipe> findRecipe(final ItemStack input, final ItemStack particle) {
        if (input == null || input.isEmpty() || particle == null || particle.isEmpty()) {
            return Optional.empty();
        }
        return REGISTRY.findFirst(recipe -> recipe.input().test(input) && recipe.particle().test(particle));
    }

    public static boolean isCyclotronPart(final ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return REGISTRY.all().stream().anyMatch(recipe -> recipe.particle().test(stack));
    }

    public static boolean hasTargetRecipe(final ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return REGISTRY.all().stream().anyMatch(recipe -> recipe.input().test(stack));
    }

    public record CyclotronRecipe(Ingredient particle, Ingredient input, ItemStack output, int antimatterYield) {
        public CyclotronRecipe {
            output = output.copy();
            antimatterYield = Math.max(0, antimatterYield);
        }

        public ItemStack outputCopy() {
            return this.output.copy();
        }
    }

    private static final class CyclotronRecipeRegistry extends MachineRecipeRegistry<CyclotronRecipe> {
        @Override
        protected void registerDefaults() {
            final Ingredient partLithium = ingredient(HbmItems.PART_LITHIUM);
            final Ingredient partBeryllium = ingredient(HbmItems.PART_BERYLLIUM);
            final Ingredient partCarbon = ingredient(HbmItems.PART_CARBON);
            final Ingredient partCopper = ingredient(HbmItems.PART_COPPER);
            final Ingredient partPlutonium = ingredient(HbmItems.PART_PLUTONIUM);

            final Ingredient dustLithium = materialIngredient(HbmMaterials.LITHIUM, HbmMaterialShape.DUST);
            final Ingredient dustBeryllium = materialIngredient(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST);
            final Ingredient dustBoron = materialIngredient(HbmMaterials.BORON, HbmMaterialShape.DUST);
            final Ingredient dustQuartz = materialIngredient(HbmMaterials.QUARTZ, HbmMaterialShape.DUST);
            final Ingredient dustPhosphorus = materialIngredient(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST);
            final Ingredient dustIron = materialIngredient(HbmMaterials.IRON, HbmMaterialShape.DUST);
            final Ingredient dustStrontium = materialIngredient(HbmMaterials.STRONTIUM, HbmMaterialShape.DUST);
            final Ingredient dustGold = materialIngredient(HbmMaterials.GOLD, HbmMaterialShape.DUST);
            final Ingredient dustPolonium = materialIngredient(HbmMaterials.POLONIUM, HbmMaterialShape.DUST);
            final Ingredient dustLanthanium = materialIngredient(HbmMaterials.LANTHANIUM, HbmMaterialShape.DUST);
            final Ingredient dustActinium = materialIngredient(HbmMaterials.ACTINIUM, HbmMaterialShape.DUST);
            final Ingredient dustUranium = materialIngredient(HbmMaterials.URANIUM, HbmMaterialShape.DUST);
            final Ingredient dustNeptunium = materialIngredient(HbmMaterials.NEPTUNIUM, HbmMaterialShape.DUST);
            final Ingredient dustTitanium = materialIngredient(HbmMaterials.TITANIUM, HbmMaterialShape.DUST);
            final Ingredient dustCobalt = materialIngredient(HbmMaterials.COBALT, HbmMaterialShape.DUST);
            final Ingredient dustCerium = materialIngredient(HbmMaterials.CERIUM, HbmMaterialShape.DUST);
            final Ingredient dustThorium = materialIngredient(HbmMaterials.TH232, HbmMaterialShape.DUST);
            final Ingredient dustSulfur = materialIngredient(HbmMaterials.SULFUR, HbmMaterialShape.DUST);
            final Ingredient dustCaesium = materialIngredient(HbmMaterials.CAESIUM, HbmMaterialShape.DUST);
            final Ingredient dustNeodymium = materialIngredient(HbmMaterials.NEODYMIUM, HbmMaterialShape.DUST);
            final Ingredient dustLead = materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.DUST);
            final Ingredient dustCoal = materialIngredient(HbmMaterials.COAL, HbmMaterialShape.DUST);
            final Ingredient dustBromine = materialIngredient(HbmMaterials.BROMINE, HbmMaterialShape.DUST);
            final Ingredient dustIodine = materialIngredient(HbmMaterials.IODINE, HbmMaterialShape.DUST);
            final Ingredient dustNiobium = materialIngredient(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST);
            final Ingredient dustPlutonium = materialIngredient(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST);
            final Ingredient dustTennessine = materialIngredient(HbmMaterials.TENNESSINE, HbmMaterialShape.DUST);
            final Ingredient pelletCharged = ingredient(HbmItems.PELLET_CHARGED);
            final Ingredient mercuryIngot = ingredient(HbmItems.INGOT_MERCURY);

            addRecipe(partLithium, dustLithium, material(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustBeryllium, material(HbmMaterials.BORON, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustBoron, material(HbmMaterials.COAL, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustQuartz, material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustPhosphorus, material(HbmMaterials.SULFUR, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustIron, material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustStrontium, material(HbmMaterials.ZIRCONIUM, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustGold, item(HbmItems.INGOT_MERCURY, 1), 50);
            addRecipe(partLithium, dustPolonium, material(HbmMaterials.ASTATINE, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustLanthanium, material(HbmMaterials.CERIUM, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustActinium, material(HbmMaterials.TH232, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustUranium, material(HbmMaterials.NEPTUNIUM, HbmMaterialShape.DUST, 1), 50);
            addRecipe(partLithium, dustNeptunium, material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 1), 50);

            addRecipe(partBeryllium, dustLithium, material(HbmMaterials.BORON, HbmMaterialShape.DUST, 1), 25);
            addRecipe(partBeryllium, dustQuartz, material(HbmMaterials.SULFUR, HbmMaterialShape.DUST, 1), 25);
            addRecipe(partBeryllium, dustTitanium, material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1), 25);
            addRecipe(partBeryllium, dustCobalt, material(HbmMaterials.COPPER, HbmMaterialShape.DUST, 1), 25);
            addRecipe(partBeryllium, dustStrontium, material(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST, 1), 25);
            addRecipe(partBeryllium, dustCerium, material(HbmMaterials.NEODYMIUM, HbmMaterialShape.DUST, 1), 25);
            addRecipe(partBeryllium, dustThorium, material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 1), 25);

            addRecipe(partCarbon, dustBoron, material(HbmMaterials.ALUMINIUM, HbmMaterialShape.DUST, 1), 10);
            addRecipe(partCarbon, dustSulfur, material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 1), 10);
            addRecipe(partCarbon, dustTitanium, material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 1), 10);
            addRecipe(partCarbon, dustCaesium, material(HbmMaterials.LANTHANIUM, HbmMaterialShape.DUST, 1), 10);
            addRecipe(partCarbon, dustNeodymium, material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 1), 10);
            addRecipe(partCarbon, mercuryIngot, material(HbmMaterials.POLONIUM, HbmMaterialShape.DUST, 1), 10);
            addRecipe(partCarbon, dustLead, material(HbmMaterials.RA226, HbmMaterialShape.DUST, 1), 10);
            addRecipe(partCarbon, materialIngredient(HbmMaterials.ASTATINE, HbmMaterialShape.DUST), material(HbmMaterials.ACTINIUM, HbmMaterialShape.DUST, 1), 10);

            addRecipe(partCopper, dustBeryllium, material(HbmMaterials.QUARTZ, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustCoal, material(HbmMaterials.BROMINE, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustTitanium, material(HbmMaterials.STRONTIUM, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustIron, material(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustBromine, material(HbmMaterials.IODINE, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustStrontium, material(HbmMaterials.NEODYMIUM, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustNiobium, material(HbmMaterials.CAESIUM, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustIodine, material(HbmMaterials.POLONIUM, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustCaesium, material(HbmMaterials.ACTINIUM, HbmMaterialShape.DUST, 1), 15);
            addRecipe(partCopper, dustGold, material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 1), 15);

            addRecipe(partPlutonium, dustPhosphorus, material(HbmMaterials.TENNESSINE, HbmMaterialShape.DUST, 1), 100);
            addRecipe(partPlutonium, dustPlutonium, material(HbmMaterials.TENNESSINE, HbmMaterialShape.DUST, 1), 100);
            addRecipe(partPlutonium, dustTennessine, material(HbmMaterials.AUSTRALIUM, HbmMaterialShape.DUST, 1), 100);
            addRecipe(partPlutonium, pelletCharged, material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.NUGGET, 1), 1_000);

            final ItemStack cellAntimatter = itemById(HbmNtmMod.MOD_ID + ":cell_antimatter", 1);
            final ItemStack cellAntiSchrabidium = itemById(HbmNtmMod.MOD_ID + ":cell_anti_schrabidium", 1);
            if (!cellAntimatter.isEmpty() && !cellAntiSchrabidium.isEmpty()) {
                addRecipe(partPlutonium, Ingredient.of(cellAntimatter.getItem()), cellAntiSchrabidium, 0);
            }
        }

        private void addRecipe(final Ingredient particle, final Ingredient input, final ItemStack output, final int antimatterYield) {
            if (particle == null || particle.isEmpty() || input == null || input.isEmpty() || output == null || output.isEmpty()) {
                return;
            }
            this.addRecipe(new CyclotronRecipe(particle, input, output, antimatterYield));
        }
    }

    private static Ingredient ingredient(final RegistryObject<Item> item) {
        try {
            return Ingredient.of(Objects.requireNonNull(item.get()));
        } catch (final RuntimeException ignored) {
            return Ingredient.EMPTY;
        }
    }

    private static Ingredient materialIngredient(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        final ItemStack stack = material(material, shape, 1);
        return stack.isEmpty() ? Ingredient.EMPTY : Ingredient.of(stack.getItem());
    }

    private static ItemStack material(final HbmMaterialDefinition material, final HbmMaterialShape shape, final int count) {
        try {
            final Item item = Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
            return new ItemStack(item, count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack item(final RegistryObject<Item> item, final int count) {
        try {
            return new ItemStack(Objects.requireNonNull(item.get()), count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack itemById(final String itemId, final int count) {
        if (itemId == null || itemId.isBlank()) {
            return ItemStack.EMPTY;
        }
        final ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) {
            return ItemStack.EMPTY;
        }
        try {
            final Item resolved = Objects.requireNonNull(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id));
            return new ItemStack(resolved, count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }
}

package com.hbm.ntm.common.soldering;

import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.recipe.CountedIngredient;
import com.hbm.ntm.common.recipe.MachineRecipeUtil;
import com.hbm.ntm.common.recipe.MachineRecipeRegistry;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("null")
public final class HbmSolderingRecipes {
    private static final SolderingRecipeRegistry REGISTRY = new SolderingRecipeRegistry();
    private static final List<SolderingRecipe> DEFAULT_RECIPES = List.of(
        circuit(CircuitItemType.ANALOG, 100, 100,
            List.of(circuitIngredient(CircuitItemType.VACUUM_TUBE, 3), circuitIngredient(CircuitItemType.CAPACITOR, 2)),
            List.of(circuitIngredient(CircuitItemType.PCB, 4)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 4))),
        circuit(CircuitItemType.BASIC, 200, 250,
            List.of(circuitIngredient(CircuitItemType.CHIP, 4)),
            List.of(circuitIngredient(CircuitItemType.PCB, 4)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 4))),
        circuit(CircuitItemType.ADVANCED, new FluidStack(HbmFluids.SULFURIC_ACID.getStillFluid(), 1_000), 300, 1_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 16), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(circuitIngredient(CircuitItemType.PCB, 8), materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 2)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 8))),
        circuit(CircuitItemType.CAPACITOR_BOARD, new FluidStack(HbmFluids.PEROXIDE.getStillFluid(), 250), 200, 300,
            List.of(circuitIngredient(CircuitItemType.CAPACITOR_TANTALIUM, 3)),
            List.of(circuitIngredient(CircuitItemType.PCB, 1)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 3))),
        circuit(CircuitItemType.BISMOID, new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 1_000), 400, 10_000,
            List.of(circuitIngredient(CircuitItemType.CHIP_BISMOID, 4), circuitIngredient(CircuitItemType.CHIP, 16), circuitIngredient(CircuitItemType.CAPACITOR, 24)),
            List.of(circuitIngredient(CircuitItemType.PCB, 12), hardPlasticIngredient(2)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 12))),
        circuit(CircuitItemType.QUANTUM, new FluidStack(HbmFluids.HELIUM4.getStillFluid(), 1_000), 400, 100_000,
            List.of(circuitIngredient(CircuitItemType.CHIP_QUANTUM, 4), circuitIngredient(CircuitItemType.CHIP_BISMOID, 16), circuitIngredient(CircuitItemType.ATOMIC_CLOCK, 4)),
            List.of(circuitIngredient(CircuitItemType.PCB, 16), hardPlasticIngredient(4)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 16))),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_1.get()), 200, 1_000,
            List.of(circuitIngredient(CircuitItemType.VACUUM_TUBE, 4), circuitIngredient(CircuitItemType.CAPACITOR, 1)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1), materialIngredient(HbmMaterials.RED_COPPER, HbmMaterialShape.DUST, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_1.get()), 200, 1_000,
            List.of(circuitIngredient(CircuitItemType.VACUUM_TUBE, 4), circuitIngredient(CircuitItemType.CAPACITOR, 1)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1), materialIngredient(HbmMaterials.EMERALD, HbmMaterialShape.DUST, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_POWER_1.get()), 200, 1_000,
            List.of(circuitIngredient(CircuitItemType.VACUUM_TUBE, 4), circuitIngredient(CircuitItemType.CAPACITOR, 1)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1), materialIngredient(HbmMaterials.GOLD, HbmMaterialShape.DUST, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_1.get()), 200, 1_000,
            List.of(circuitIngredient(CircuitItemType.VACUUM_TUBE, 4), circuitIngredient(CircuitItemType.CAPACITOR, 1)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1), materialIngredient(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_1.get()), 200, 1_000,
            List.of(circuitIngredient(CircuitItemType.VACUUM_TUBE, 4), circuitIngredient(CircuitItemType.CAPACITOR, 1)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1), materialIngredient(HbmMaterials.TUNGSTEN, HbmMaterialShape.DUST, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_2.get()), 300, 10_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 8), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_1.get()), 1), plasticIngredient(4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_2.get()), 300, 10_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 8), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_1.get()), 1), plasticIngredient(4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_POWER_2.get()), 300, 10_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 8), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_POWER_1.get()), 1), plasticIngredient(4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_2.get()), 300, 10_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 8), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_1.get()), 1), plasticIngredient(4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_2.get()), 300, 10_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 8), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_1.get()), 1), plasticIngredient(4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_RADIUS.get()), 200, 1_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 4), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1), simpleItemIngredient(Items.GLOWSTONE_DUST, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_HEALTH.get()), 200, 1_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 4), circuitIngredient(CircuitItemType.CAPACITOR, 4)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1), materialIngredient(HbmMaterials.LITHIUM, HbmMaterialShape.DUST, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_3.get()), new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 500), 400, 25_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 16), circuitIngredient(CircuitItemType.CAPACITOR, 16)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_2.get()), 1), materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_3.get()), new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 500), 400, 25_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 16), circuitIngredient(CircuitItemType.CAPACITOR, 16)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_2.get()), 1), materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_POWER_3.get()), new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 500), 400, 25_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 16), circuitIngredient(CircuitItemType.CAPACITOR, 16)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_POWER_2.get()), 1), materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_3.get()), new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 500), 400, 25_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 16), circuitIngredient(CircuitItemType.CAPACITOR, 16)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_2.get()), 1), materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 4)),
            List.of()),
        simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_3.get()), new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 500), 400, 25_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 16), circuitIngredient(CircuitItemType.CAPACITOR, 16)),
            List.of(simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_2.get()), 1), materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 4)),
            List.of()),
        circuit(CircuitItemType.CONTROLLER, new FluidStack(HbmFluids.PERFLUOROMETHYL.getStillFluid(), 1_000), 400, 15_000,
            List.of(circuitIngredient(CircuitItemType.CHIP, 32), circuitIngredient(CircuitItemType.CAPACITOR, 32), circuitIngredient(CircuitItemType.CAPACITOR_TANTALIUM, 16)),
            List.of(circuitIngredient(CircuitItemType.CONTROLLER_CHASSIS, 1), simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_1.get()), 1)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 16))),
        circuit(CircuitItemType.CONTROLLER_ADVANCED, new FluidStack(HbmFluids.PERFLUOROMETHYL.getStillFluid(), 4_000), 600, 25_000,
            List.of(circuitIngredient(CircuitItemType.CHIP_BISMOID, 16), circuitIngredient(CircuitItemType.CAPACITOR_TANTALIUM, 48), circuitIngredient(CircuitItemType.ATOMIC_CLOCK, 1)),
            List.of(circuitIngredient(CircuitItemType.CONTROLLER_CHASSIS, 1), simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_3.get()), 1)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 24))),
        circuit(CircuitItemType.CONTROLLER_QUANTUM, new FluidStack(HbmFluids.PERFLUOROMETHYL_COLD.getStillFluid(), 6_000), 600, 250_000,
            List.of(circuitIngredient(CircuitItemType.CHIP_QUANTUM, 16), circuitIngredient(CircuitItemType.CHIP_BISMOID, 48), circuitIngredient(CircuitItemType.ATOMIC_CLOCK, 8)),
            List.of(circuitIngredient(CircuitItemType.CONTROLLER_ADVANCED, 2), simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_OVERDRIVE_1.get()), 1)),
            List.of(materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 32)))
    );

    private HbmSolderingRecipes() {
    }

    public static List<SolderingRecipe> all() {
        return REGISTRY.all();
    }

    public static Optional<SolderingRecipe> findRecipe(final ItemStack... inputs) {
        if (inputs.length < 6) {
            return Optional.empty();
        }
        return findRecipe(FluidStack.EMPTY,
            List.of(stackOrEmpty(inputs[0]), stackOrEmpty(inputs[1]), stackOrEmpty(inputs[2])),
            List.of(stackOrEmpty(inputs[3]), stackOrEmpty(inputs[4])),
            List.of(stackOrEmpty(inputs[5])));
    }

    public static Optional<SolderingRecipe> findRecipe(final FluidStack fluid, final ItemStack... inputs) {
        if (inputs.length < 6) {
            return Optional.empty();
        }
        return findRecipe(
            fluid,
            List.of(stackOrEmpty(inputs[0]), stackOrEmpty(inputs[1]), stackOrEmpty(inputs[2])),
            List.of(stackOrEmpty(inputs[3]), stackOrEmpty(inputs[4])),
            List.of(stackOrEmpty(inputs[5])));
    }

    public static Optional<SolderingRecipe> findRecipe(final List<ItemStack> toppings, final List<ItemStack> pcb, final List<ItemStack> solder) {
        return findRecipe(FluidStack.EMPTY, toppings, pcb, solder);
    }

    public static Optional<SolderingRecipe> findRecipe(final FluidStack fluid, final List<ItemStack> toppings, final List<ItemStack> pcb, final List<ItemStack> solder) {
        return REGISTRY.findFirst(recipe -> recipe.matches(fluid, toppings, pcb, solder));
    }

    private static SolderingRecipe circuit(final CircuitItemType outputType, final int duration, final long consumption,
                                           final List<CountedIngredient> toppings, final List<CountedIngredient> pcb, final List<CountedIngredient> solder) {
        return circuit(outputType, FluidStack.EMPTY, duration, consumption, toppings, pcb, solder);
    }

    private static SolderingRecipe circuit(final CircuitItemType outputType, final FluidStack fluid, final int duration, final long consumption,
                                           final List<CountedIngredient> toppings, final List<CountedIngredient> pcb, final List<CountedIngredient> solder) {
        return new SolderingRecipe(new ItemStack(circuitItem(outputType)), fluid.copy(), duration, consumption, List.copyOf(toppings), List.copyOf(pcb), List.copyOf(solder));
    }

    private static SolderingRecipe simpleItem(final Item output, final int duration, final long consumption,
                                              final List<CountedIngredient> toppings, final List<CountedIngredient> pcb, final List<CountedIngredient> solder) {
        return simpleItem(output, FluidStack.EMPTY, duration, consumption, toppings, pcb, solder);
    }

    private static SolderingRecipe simpleItem(final Item output, final FluidStack fluid, final int duration, final long consumption,
                                              final List<CountedIngredient> toppings, final List<CountedIngredient> pcb, final List<CountedIngredient> solder) {
        return new SolderingRecipe(new ItemStack(output), fluid.copy(), duration, consumption, List.copyOf(toppings), List.copyOf(pcb), List.copyOf(solder));
    }

    private static CountedIngredient circuitIngredient(final CircuitItemType type, final int count) {
        return new CountedIngredient(Ingredient.of(circuitItem(type)), count);
    }

    private static CountedIngredient materialIngredient(final HbmMaterialDefinition material, final HbmMaterialShape shape, final int count) {
        return new CountedIngredient(Ingredient.of(materialItem(material, shape)), count);
    }

    private static CountedIngredient simpleItemIngredient(final Item item, final int count) {
        return new CountedIngredient(Ingredient.of(item), count);
    }

    private static CountedIngredient plasticIngredient(final int count) {
        return new CountedIngredient(Ingredient.of(
            materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT),
            materialItem(HbmMaterials.BAKELITE, HbmMaterialShape.INGOT)), count);
    }

    private static CountedIngredient hardPlasticIngredient(final int count) {
        return new CountedIngredient(Ingredient.of(
            materialItem(HbmMaterials.PC, HbmMaterialShape.INGOT),
            materialItem(HbmMaterials.PVC, HbmMaterialShape.INGOT)), count);
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

    public record SolderingRecipe(ItemStack output, FluidStack fluid, int duration, long consumption, List<CountedIngredient> toppings,
                                  List<CountedIngredient> pcb, List<CountedIngredient> solder) {
        public boolean matches(final FluidStack fluid, final List<ItemStack> toppings, final List<ItemStack> pcb, final List<ItemStack> solder) {
            return MachineRecipeUtil.matchesFluidRequirement(fluid, this.fluid)
                && MachineRecipeUtil.matchesShapelessIngredients(toppings, this.toppings)
                && MachineRecipeUtil.matchesShapelessIngredients(pcb, this.pcb)
                && MachineRecipeUtil.matchesShapelessIngredients(solder, this.solder);
        }

        public ItemStack outputCopy() {
            return this.output.copy();
        }

        public FluidStack fluidCopy() {
            return this.fluid.copy();
        }
    }

    private static final class SolderingRecipeRegistry extends MachineRecipeRegistry<SolderingRecipe> {
        @Override
        protected void registerDefaults() {
            this.addAllRecipes(DEFAULT_RECIPES);
        }
    }
}

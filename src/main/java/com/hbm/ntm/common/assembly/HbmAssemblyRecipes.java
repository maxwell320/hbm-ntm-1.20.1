package com.hbm.ntm.common.assembly;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.item.CanisterItem;
import com.hbm.ntm.common.item.FluidTankItem;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.recipe.MachineRecipeRegistry;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public final class HbmAssemblyRecipes {
    private static final int FLUID_PACK_CAPACITY = 32_000;
    public static final String POOL_PREFIX_ALT = "alt.";
    public static final String POOL_PREFIX_DISCOVER = "discover.";
    public static final String POOL_PREFIX_SECRET = "secret.";
    public static final String POOL_PREFIX_528 = "528.";
    private static final String POOL_ALT_PLATES = "alt.plates";
    private static final String GROUP_AUTOSWITCH_PLATES = "autoswitch.plates";
    private static final AssemblyRecipeRegistry REGISTRY = new AssemblyRecipeRegistry();

    private HbmAssemblyRecipes() {
    }

    public static List<AssemblyRecipe> all() {
        return REGISTRY.all();
    }

    public static void ensureInitialized() {
        REGISTRY.all();
    }

    public static Optional<AssemblyRecipe> findById(final String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        for (final AssemblyRecipe recipe : REGISTRY.all()) {
            if (recipe.id().equals(id)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    public static List<AssemblyRecipe> visibleForPool(final @Nullable String pool) {
        final List<AssemblyRecipe> visible = new ArrayList<>();
        for (final AssemblyRecipe recipe : REGISTRY.all()) {
            if (recipe.matchesBlueprintPool(pool)) {
                visible.add(recipe);
            }
        }
        return List.copyOf(visible);
    }

    public static Optional<AssemblyRecipe> findRecipe(final List<ItemStack> itemInputs, final FluidStack fluidInput) {
        return REGISTRY.findFirst(recipe -> recipe.matches(itemInputs, fluidInput == null ? FluidStack.EMPTY : fluidInput));
    }

    public static Optional<AssemblyRecipe> findAutoSwitchRecipe(final @Nullable String currentRecipeId, final @Nullable String group,
                                                                final @Nullable String blueprintPool, final ItemStack firstInput) {
        if (group == null || group.isBlank() || firstInput.isEmpty()) {
            return Optional.empty();
        }
        for (final AssemblyRecipe recipe : REGISTRY.all()) {
            if (!recipe.matchesBlueprintPool(blueprintPool) || !group.equals(recipe.autoSwitchGroup())) {
                continue;
            }
            if (currentRecipeId != null && recipe.id().equals(currentRecipeId)) {
                continue;
            }
            if (recipe.firstRequirementAccepts(firstInput)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    public static Optional<AssemblyRecipe> findRecipe(final FluidStack fluidInput, final ItemStack... itemInputs) {
        return findRecipe(List.of(itemInputs), fluidInput);
    }

    private static void registerDefaults(final AssemblyRecipeRegistry registry) {
        registerLegacyPlateRecipes(registry);
        registerLegacySupportRecipes(registry);
        registerLegacyDrillRecipes(registry);
        registerLegacyUpgradeRecipes(registry);

        registry.add(new AssemblyRecipe(
            "ass.fluid_pack_empty",
            new ItemStack(item(HbmItems.FLUID_PACK_EMPTY)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE), 4),
                ingredient(
                    Ingredient.of(
                        materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT),
                        materialItem(HbmMaterials.BAKELITE, HbmMaterialShape.INGOT)),
                    materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT),
                    2))));

        for (final ResourceLocation fluidId : ForgeRegistries.FLUIDS.getKeys()) {
            if (!isPackagedFluid(fluidId)) {
                continue;
            }
            final Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
            if (fluid == null) {
                continue;
            }
            final ItemStack packedStack = FluidTankItem.withFluid(item(HbmItems.FLUID_PACK_FULL), fluidId);
            final FluidStack fluidStack = new FluidStack(fluid, FLUID_PACK_CAPACITY);
            registry.add(new AssemblyRecipe(
                "ass.fluid_pack_fill." + fluidId.getPath(),
                packedStack,
                fluidStack,
                FluidStack.EMPTY,
                40,
                100,
                List.of(ingredient(Ingredient.of(item(HbmItems.FLUID_PACK_EMPTY)), item(HbmItems.FLUID_PACK_EMPTY), 1))));
            registry.add(new AssemblyRecipe(
                "ass.fluid_pack_drain." + fluidId.getPath(),
                new ItemStack(item(HbmItems.FLUID_PACK_EMPTY)),
                FluidStack.EMPTY,
                fluidStack,
                40,
                100,
                List.of(exact(packedStack, 1))));
        }

        final ItemStack canisterPetroil = canisterWithFluid("petroil");
        final ItemStack canisterDiesel = canisterWithFluid("diesel");
        final ItemStack canisterHeatingOil = canisterWithFluid("heatingoil");
        final ItemStack canisterLubricantPair = canisterWithFluid("lubricant");
        canisterLubricantPair.setCount(2);

        registry.add(new AssemblyRecipe(
            "ass.canister_diesel",
            canisterDiesel,
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(exact(canisterPetroil, 1))));

        registry.add(new AssemblyRecipe(
            "ass.canister_lubricant",
            canisterLubricantPair,
            new FluidStack(HbmFluids.UNSATURATEDS.getStillFluid(), 1_000),
            FluidStack.EMPTY,
            40,
            100,
            List.of(
                exact(canisterHeatingOil, 1),
                ingredient(Ingredient.of(item(HbmItems.CANISTER_EMPTY)), item(HbmItems.CANISTER_EMPTY), 1))));

        registry.add(new AssemblyRecipe(
            "ass.yellowbarrel",
            new ItemStack(item(HbmItems.BARREL_YELLOW)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            400,
            400,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.TANK_STEEL)), item(HbmItems.TANK_STEEL), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.LEAD, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.LEAD, HbmMaterialShape.PLATE), 2),
                ingredient(Ingredient.of(item(HbmItems.NUCLEAR_WASTE)), item(HbmItems.NUCLEAR_WASTE), 10))));

        registry.add(new AssemblyRecipe(
            "ass.vitrifiedbarrel",
            new ItemStack(item(HbmItems.BARREL_VITRIFIED)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            400,
            400,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.TANK_STEEL)), item(HbmItems.TANK_STEEL), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.LEAD, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.LEAD, HbmMaterialShape.PLATE), 2),
                ingredient(Ingredient.of(item(HbmItems.NUCLEAR_WASTE_VITRIFIED)), item(HbmItems.NUCLEAR_WASTE_VITRIFIED), 10))));

        registry.add(new AssemblyRecipe(
            "ass.shredder",
            new ItemStack(item(HbmItems.MACHINE_SHREDDER)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 8),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(item(HbmItems.MOTOR)), item(HbmItems.MOTOR), 2))));

        registry.add(new AssemblyRecipe(
            "ass.assembler",
            new ItemStack(item(HbmItems.MACHINE_ASSEMBLY_MACHINE)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.INGOT), 4),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(item(HbmItems.MOTOR)), item(HbmItems.MOTOR), 2),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.ANALOG))), item(HbmItems.getCircuit(CircuitItemType.ANALOG)), 1))));

        registry.add(new AssemblyRecipe(
            "ass.centrifuge",
            new ItemStack(item(HbmItems.MACHINE_CENTRIFUGE)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.CENTRIFUGE_ELEMENT)), item(HbmItems.CENTRIFUGE_ELEMENT), 1),
                ingredient(plasticIngotIngredient(), materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT), 4),
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 8),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.ANALOG))), item(HbmItems.getCircuit(CircuitItemType.ANALOG)), 1))));

        registry.add(new AssemblyRecipe(
            "ass.gascent",
            new ItemStack(item(HbmItems.MACHINE_GAS_CENTRIFUGE)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            400,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.CENTRIFUGE_ELEMENT)), item(HbmItems.CENTRIFUGE_ELEMENT), 4),
                ingredient(plasticIngotIngredient(), materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT), 8),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT), 2),
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 8),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.ADVANCED))), item(HbmItems.getCircuit(CircuitItemType.ADVANCED)), 1)),
            List.of(POOL_PREFIX_528 + "gascent")));

        registry.add(new AssemblyRecipe(
            "ass.rtg",
            new ItemStack(item(HbmItems.MACHINE_RTG_GREY)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.RTG_UNIT)), item(HbmItems.RTG_UNIT), 3),
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(materialItem(HbmMaterials.RED_COPPER, HbmMaterialShape.WIRE)), materialItem(HbmMaterials.RED_COPPER, HbmMaterialShape.WIRE), 16),
                ingredient(plasticIngotIngredient(), materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT), 4))));

        registry.add(new AssemblyRecipe(
            "ass.dieselgen",
            new ItemStack(item(HbmItems.MACHINE_DIESEL_GENERATOR)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.SHELL)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.SHELL), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.CAST_PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.CAST_PLATE), 2),
                ingredient(Ingredient.of(item(HbmItems.COIL_COPPER_TORUS)), item(HbmItems.COIL_COPPER_TORUS), 4))));

        registry.add(new AssemblyRecipe(
            "ass.combustiongen",
            new ItemStack(item(HbmItems.MACHINE_COMBUSTION_ENGINE)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            300,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 16),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.INGOT), 12),
                ingredient(Ingredient.of(materialItem(HbmMaterials.GOLD, HbmMaterialShape.DENSE_WIRE)), materialItem(HbmMaterials.GOLD, HbmMaterialShape.DENSE_WIRE), 8),
                ingredient(Ingredient.of(item(HbmItems.CANISTER_EMPTY)), item(HbmItems.CANISTER_EMPTY), 4),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.BASIC))), item(HbmItems.getCircuit(CircuitItemType.BASIC)), 1))));

        registry.add(new AssemblyRecipe(
            "ass.pistonsetsteel",
            new ItemStack(item(HbmItems.PISTON_SET_STEEL)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 16),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT), 8),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.BOLT)), materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.BOLT), 16))));

        registry.add(new AssemblyRecipe(
            "ass.pistonsetdura",
            new ItemStack(item(HbmItems.PISTON_SET_DURA)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.INGOT), 24),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE), 8),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT), 8),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.BOLT)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.BOLT), 16))));

        registry.add(new AssemblyRecipe(
            "ass.pistonsetdesh",
            new ItemStack(item(HbmItems.PISTON_SET_DESH)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT), 24),
                ingredient(plasticIngotIngredient(), materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT), 12),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 24),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.PIPE)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.PIPE), 4))));

        registry.add(new AssemblyRecipe(
            "ass.pistonsetstar",
            new ItemStack(item(HbmItems.PISTON_SET_STARMETAL)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STARMETAL, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.STARMETAL, HbmMaterialShape.INGOT), 24),
                ingredient(Ingredient.of(materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.PLATE), 24),
                ingredient(Ingredient.of(materialItem(HbmMaterials.NIOBIUM, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.NIOBIUM, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.PIPE)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.PIPE), 4))));

        registry.add(new AssemblyRecipe(
            "ass.cyclotron",
            new ItemStack(item(HbmItems.MACHINE_CYCLOTRON)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            600,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.MACHINE_BATTERY)), item(HbmItems.MACHINE_BATTERY), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.NEODYMIUM, HbmMaterialShape.DENSE_WIRE)), materialItem(HbmMaterials.NEODYMIUM, HbmMaterialShape.DENSE_WIRE), 32),
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.CAST_PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.CAST_PLATE), 16),
                ingredient(plasticIngotIngredient(), materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT), 24),
                ingredient(Ingredient.of(materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT), 24),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.BASIC))), item(HbmItems.getCircuit(CircuitItemType.BASIC)), 16))));

        registry.add(new AssemblyRecipe(
            "ass.purex",
            new ItemStack(item(HbmItems.MACHINE_PUREX)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            300,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.SHELL)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.SHELL), 4),
                ingredient(Ingredient.of(materialItem(HbmMaterials.RUBBER, HbmMaterialShape.PIPE)), materialItem(HbmMaterials.RUBBER, HbmMaterialShape.PIPE), 8),
                ingredient(Ingredient.of(materialItem(HbmMaterials.LEAD, HbmMaterialShape.CAST_PLATE)), materialItem(HbmMaterials.LEAD, HbmMaterialShape.CAST_PLATE), 4),
                ingredient(Ingredient.of(item(HbmItems.MOTOR_DESH)), item(HbmItems.MOTOR_DESH), 1),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.BASIC))), item(HbmItems.getCircuit(CircuitItemType.BASIC)), 4))));
    }

    private static void registerLegacySupportRecipes(final AssemblyRecipeRegistry registry) {
        registry.add(new AssemblyRecipe(
            "ass.hazcloth",
            new ItemStack(item(HbmItems.HAZMAT_CLOTH), 4),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            50,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.LEAD, HbmMaterialShape.DUST)), materialItem(HbmMaterials.LEAD, HbmMaterialShape.DUST), 4),
                ingredient(Ingredient.of(Items.STRING), Items.STRING, 8))));

        registry.add(new AssemblyRecipe(
            "ass.firecloth",
            new ItemStack(item(HbmItems.ASBESTOS_CLOTH), 4),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            50,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.ASBESTOS, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.ASBESTOS, HbmMaterialShape.INGOT), 1),
                ingredient(Ingredient.of(Items.STRING), Items.STRING, 8))));

        registry.add(new AssemblyRecipe(
            "ass.filtercoal",
            new ItemStack(item(HbmItems.FILTER_COAL)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            50,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.COAL, HbmMaterialShape.DUST)), materialItem(HbmMaterials.COAL, HbmMaterialShape.DUST), 4),
                ingredient(Ingredient.of(Items.STRING), Items.STRING, 2),
                ingredient(Ingredient.of(Items.PAPER), Items.PAPER, 1))));

        registry.add(new AssemblyRecipe(
            "ass.centrifugetower",
            new ItemStack(item(HbmItems.CENTRIFUGE_ELEMENT)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(item(HbmItems.MOTOR)), item(HbmItems.MOTOR), 1))));

        registry.add(new AssemblyRecipe(
            "ass.thermoelement",
            new ItemStack(item(HbmItems.THERMO_ELEMENT)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            60,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.RED_COPPER, HbmMaterialShape.WIRE)), materialItem(HbmMaterials.RED_COPPER, HbmMaterialShape.WIRE), 2),
                ingredient(Ingredient.of(materialItem(HbmMaterials.QUARTZ, HbmMaterialShape.DUST)), materialItem(HbmMaterials.QUARTZ, HbmMaterialShape.DUST), 2))));

        registry.add(new AssemblyRecipe(
            "ass.thermoelementsilicon",
            new ItemStack(item(HbmItems.THERMO_ELEMENT)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            60,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.GOLD, HbmMaterialShape.WIRE)), materialItem(HbmMaterials.GOLD, HbmMaterialShape.WIRE), 2),
                ingredient(Ingredient.of(materialItem(HbmMaterials.SILICON, HbmMaterialShape.BILLET)), materialItem(HbmMaterials.SILICON, HbmMaterialShape.BILLET), 1))));

        registry.add(new AssemblyRecipe(
            "ass.rtgunit",
            new ItemStack(item(HbmItems.RTG_UNIT)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.LEAD, HbmMaterialShape.CAST_PLATE)), materialItem(HbmMaterials.LEAD, HbmMaterialShape.CAST_PLATE), 2),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 4),
                ingredient(Ingredient.of(item(HbmItems.THERMO_ELEMENT)), item(HbmItems.THERMO_ELEMENT), 2))));

        registry.add(new AssemblyRecipe(
            "ass.magnetron",
            new ItemStack(item(HbmItems.MAGNETRON)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 3),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.WIRE)), materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.WIRE), 4))));

        registry.add(new AssemblyRecipe(
            "ass.partlith",
            new ItemStack(item(HbmItems.PART_LITHIUM), 8),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(ingredient(Ingredient.of(materialItem(HbmMaterials.LITHIUM, HbmMaterialShape.DUST)), materialItem(HbmMaterials.LITHIUM, HbmMaterialShape.DUST), 1))));

        registry.add(new AssemblyRecipe(
            "ass.partberyl",
            new ItemStack(item(HbmItems.PART_BERYLLIUM), 8),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(ingredient(Ingredient.of(materialItem(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST)), materialItem(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST), 1))));

        registry.add(new AssemblyRecipe(
            "ass.partcoal",
            new ItemStack(item(HbmItems.PART_CARBON), 8),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(ingredient(Ingredient.of(materialItem(HbmMaterials.COAL, HbmMaterialShape.DUST)), materialItem(HbmMaterials.COAL, HbmMaterialShape.DUST), 1))));

        registry.add(new AssemblyRecipe(
            "ass.partcop",
            new ItemStack(item(HbmItems.PART_COPPER), 8),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.DUST)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.DUST), 1))));

        registry.add(new AssemblyRecipe(
            "ass.partplut",
            new ItemStack(item(HbmItems.PART_PLUTONIUM), 8),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            40,
            100,
            List.of(ingredient(Ingredient.of(materialItem(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST)), materialItem(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST), 1))));
    }

    private static void registerLegacyDrillRecipes(final AssemblyRecipeRegistry registry) {
        registry.add(new AssemblyRecipe(
            "ass.titaniumdrill",
            new ItemStack(item(HbmItems.DRILL_TITANIUM)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.CAST_PLATE)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.CAST_PLATE), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE), 8))));

        registry.add(new AssemblyRecipe(
            "ass.entanglementkit",
            new ItemStack(item(HbmItems.ENTANGLEMENT_KIT)),
            new FluidStack(HbmFluids.XENON.getStillFluid(), 8_000),
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.CAST_PLATE)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.CAST_PLATE), 4),
                ingredient(Ingredient.of(materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE)), materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 24),
                ingredient(Ingredient.of(materialItem(HbmMaterials.GOLD, HbmMaterialShape.DENSE_WIRE)), materialItem(HbmMaterials.GOLD, HbmMaterialShape.DENSE_WIRE), 16))));

        registry.add(new AssemblyRecipe(
            "ass.drillsteel",
            new ItemStack(item(HbmItems.DRILLBIT_STEEL)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.STEEL, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.STEEL, HbmMaterialShape.INGOT), 12),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT), 4))));

        registry.add(new AssemblyRecipe(
            "ass.drillsteeldiamond",
            new ItemStack(item(HbmItems.DRILLBIT_STEEL_DIAMOND)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.DRILLBIT_STEEL)), item(HbmItems.DRILLBIT_STEEL), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST)), materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST), 16))));

        registry.add(new AssemblyRecipe(
            "ass.drilldura",
            new ItemStack(item(HbmItems.DRILLBIT_HSS)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.INGOT), 12),
                ingredient(plasticIngotIngredient(), materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT), 12),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.INGOT), 8))));

        registry.add(new AssemblyRecipe(
            "ass.drillduradiamond",
            new ItemStack(item(HbmItems.DRILLBIT_HSS_DIAMOND)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.DRILLBIT_HSS)), item(HbmItems.DRILLBIT_HSS), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST)), materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST), 24))));

        registry.add(new AssemblyRecipe(
            "ass.drilldesh",
            new ItemStack(item(HbmItems.DRILLBIT_DESH)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT), 12),
                ingredient(Ingredient.of(materialItem(HbmMaterials.NIOBIUM, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.NIOBIUM, HbmMaterialShape.INGOT), 4))));

        registry.add(new AssemblyRecipe(
            "ass.drilldeshdiamond",
            new ItemStack(item(HbmItems.DRILLBIT_DESH_DIAMOND)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.DRILLBIT_DESH)), item(HbmItems.DRILLBIT_DESH), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST)), materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST), 32))));

        registry.add(new AssemblyRecipe(
            "ass.drilltc",
            new ItemStack(item(HbmItems.DRILLBIT_TCALLOY)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.TCALLOY, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.TCALLOY, HbmMaterialShape.INGOT), 20),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT), 12),
                ingredient(Ingredient.of(materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT), 8))));

        registry.add(new AssemblyRecipe(
            "ass.drilltcdiamond",
            new ItemStack(item(HbmItems.DRILLBIT_TCALLOY_DIAMOND)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.DRILLBIT_TCALLOY)), item(HbmItems.DRILLBIT_TCALLOY), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST)), materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST), 48))));

        registry.add(new AssemblyRecipe(
            "ass.drillferro",
            new ItemStack(item(HbmItems.DRILLBIT_FERRO)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(materialItem(HbmMaterials.FERRORANIUM, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.FERRORANIUM, HbmMaterialShape.INGOT), 24),
                ingredient(Ingredient.of(materialItem(HbmMaterials.TCALLOY, HbmMaterialShape.INGOT), materialItem(HbmMaterials.CDALLOY, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.TCALLOY, HbmMaterialShape.INGOT), 12),
                ingredient(Ingredient.of(materialItem(HbmMaterials.BISMUTH, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.BISMUTH, HbmMaterialShape.INGOT), 4))));

        registry.add(new AssemblyRecipe(
            "ass.drillferrodiamond",
            new ItemStack(item(HbmItems.DRILLBIT_FERRO_DIAMOND)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            100,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.DRILLBIT_FERRO)), item(HbmItems.DRILLBIT_FERRO), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST)), materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST), 56))));
    }

    private static void registerLegacyPlateRecipes(final AssemblyRecipeRegistry registry) {
        registerPlateRecipe(registry, "ass.plateiron", materialItem(HbmMaterials.IRON, HbmMaterialShape.PLATE), Items.IRON_INGOT);
        registerPlateRecipe(registry, "ass.plategold", materialItem(HbmMaterials.GOLD, HbmMaterialShape.PLATE), Items.GOLD_INGOT);
        registerPlateRecipe(registry, "ass.platetitanium", materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE), materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platealu", materialItem(HbmMaterials.ALUMINIUM, HbmMaterialShape.PLATE), materialItem(HbmMaterials.ALUMINIUM, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platesteel", materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), materialItem(HbmMaterials.STEEL, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platelead", materialItem(HbmMaterials.LEAD, HbmMaterialShape.PLATE), materialItem(HbmMaterials.LEAD, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platecopper", materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), materialItem(HbmMaterials.COPPER, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platealloy", materialItem(HbmMaterials.ADVANCED_ALLOY, HbmMaterialShape.PLATE), materialItem(HbmMaterials.ADVANCED_ALLOY, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.plateschrab", materialItem(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.PLATE), materialItem(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platecmb", materialItem(HbmMaterials.COMBINE_STEEL, HbmMaterialShape.PLATE), materialItem(HbmMaterials.COMBINE_STEEL, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.plategunmetal", materialItem(HbmMaterials.GUNMETAL, HbmMaterialShape.PLATE), materialItem(HbmMaterials.GUNMETAL, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.plateweaponsteel", materialItem(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.PLATE), materialItem(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platesaturnite", materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.PLATE), materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT));
        registerPlateRecipe(registry, "ass.platedura", materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.PLATE), materialItem(HbmMaterials.DURA_STEEL, HbmMaterialShape.INGOT));
    }

    private static void registerLegacyUpgradeRecipes(final AssemblyRecipeRegistry registry) {
        registry.add(new AssemblyRecipe(
            "ass.overdrive1",
            new ItemStack(item(HbmItems.UPGRADE_OVERDRIVE_1)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            200,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_SPEED_3)), item(HbmItems.UPGRADE_SPEED_3), 1),
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_EFFECT_3)), item(HbmItems.UPGRADE_EFFECT_3), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(materialItem(HbmMaterials.PC, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.PC, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.ADVANCED))), item(HbmItems.getCircuit(CircuitItemType.ADVANCED)), 16))));

        registry.add(new AssemblyRecipe(
            "ass.overdrive2",
            new ItemStack(item(HbmItems.UPGRADE_OVERDRIVE_2)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            600,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_OVERDRIVE_1)), item(HbmItems.UPGRADE_OVERDRIVE_1), 1),
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_SPEED_3)), item(HbmItems.UPGRADE_SPEED_3), 1),
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_EFFECT_3)), item(HbmItems.UPGRADE_EFFECT_3), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(item(HbmItems.INGOT_CFT)), item(HbmItems.INGOT_CFT), 8),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.CAPACITOR_BOARD))), item(HbmItems.getCircuit(CircuitItemType.CAPACITOR_BOARD)), 16))));

        registry.add(new AssemblyRecipe(
            "ass.overdrive3",
            new ItemStack(item(HbmItems.UPGRADE_OVERDRIVE_3)),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            1_200,
            100,
            List.of(
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_OVERDRIVE_2)), item(HbmItems.UPGRADE_OVERDRIVE_2), 1),
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_SPEED_3)), item(HbmItems.UPGRADE_SPEED_3), 1),
                ingredient(Ingredient.of(item(HbmItems.UPGRADE_EFFECT_3)), item(HbmItems.UPGRADE_EFFECT_3), 1),
                ingredient(Ingredient.of(materialItem(HbmMaterials.BISMUTH_BRONZE, HbmMaterialShape.INGOT)), materialItem(HbmMaterials.BISMUTH_BRONZE, HbmMaterialShape.INGOT), 16),
                ingredient(Ingredient.of(item(HbmItems.INGOT_CFT)), item(HbmItems.INGOT_CFT), 16),
                ingredient(Ingredient.of(item(HbmItems.getCircuit(CircuitItemType.BISMOID))), item(HbmItems.getCircuit(CircuitItemType.BISMOID)), 16))));
    }

    private static void registerPlateRecipe(final AssemblyRecipeRegistry registry, final String id, final Item outputPlate, final Item inputIngot) {
        registry.add(new AssemblyRecipe(
            id,
            new ItemStack(outputPlate),
            FluidStack.EMPTY,
            FluidStack.EMPTY,
            60,
            100,
            List.of(ingredient(Ingredient.of(inputIngot), inputIngot, 1)),
            List.of(POOL_ALT_PLATES),
            GROUP_AUTOSWITCH_PLATES));
    }

    private static Ingredient plasticIngotIngredient() {
        return Ingredient.of(
            materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT),
            materialItem(HbmMaterials.BAKELITE, HbmMaterialShape.INGOT),
            materialItem(HbmMaterials.RUBBER, HbmMaterialShape.INGOT));
    }

    private static boolean isPackagedFluid(final ResourceLocation fluidId) {
        return !fluidId.getPath().startsWith("flowing_") && FluidTankItem.supportsFluid(fluidId, false);
    }

    private static IngredientRequirement ingredient(final Ingredient ingredient, final Item displayItem, final int count) {
        return new IngredientRequirement(ingredient, new ItemStack(displayItem), count);
    }

    private static ExactStackRequirement exact(final ItemStack stack, final int count) {
        return new ExactStackRequirement(stack, count);
    }

    private static Item item(final RegistryObject<Item> registryObject) {
        return Objects.requireNonNull(registryObject.get());
    }

    private static ItemStack canisterWithFluid(final String fluidPath) {
        return CanisterItem.withFluid(item(HbmItems.CANISTER_FULL), ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, fluidPath));
    }

    private static Item materialItem(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        return item(HbmItems.getMaterialPart(material, shape));
    }

    public sealed interface AssemblyRequirement permits IngredientRequirement, ExactStackRequirement {
        boolean matches(ItemStack stack);

        boolean accepts(ItemStack stack);

        ItemStack displayStack();

        int count();
    }

    public record IngredientRequirement(Ingredient ingredient, ItemStack displayStack, int count) implements AssemblyRequirement {
        public IngredientRequirement {
            ingredient = Objects.requireNonNull(ingredient, "ingredient");
            displayStack = displayStack.copy();
            displayStack.setCount(Math.max(1, count));
            count = Math.max(1, count);
        }

        @Override
        public boolean matches(final ItemStack stack) {
            return this.accepts(stack) && stack.getCount() >= this.count;
        }

        @Override
        public boolean accepts(final ItemStack stack) {
            return !stack.isEmpty() && this.ingredient.test(stack);
        }

        @Override
        public ItemStack displayStack() {
            return this.displayStack.copy();
        }
    }

    public record ExactStackRequirement(ItemStack displayStack, int count) implements AssemblyRequirement {
        public ExactStackRequirement {
            displayStack = displayStack.copy();
            displayStack.setCount(Math.max(1, count));
            count = Math.max(1, count);
        }

        @Override
        public boolean matches(final ItemStack stack) {
            return this.accepts(stack) && stack.getCount() >= this.count;
        }

        @Override
        public boolean accepts(final ItemStack stack) {
            return !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, this.displayStack);
        }

        @Override
        public ItemStack displayStack() {
            return this.displayStack.copy();
        }
    }

    public record AssemblyRecipe(String id, ItemStack output, FluidStack fluidInput, FluidStack fluidOutput, int duration, long consumption,
                                 List<AssemblyRequirement> itemInputs, List<List<AssemblyRequirement>> itemInputVariants,
                                 List<String> blueprintPools, @Nullable String autoSwitchGroup) {
        public AssemblyRecipe(final String id, final ItemStack output, final FluidStack fluidInput, final FluidStack fluidOutput,
                              final int duration, final long consumption, final List<AssemblyRequirement> itemInputs) {
            this(id, output, fluidInput, fluidOutput, duration, consumption, itemInputs, List.of(itemInputs), List.of(), null);
        }

        public AssemblyRecipe(final String id, final ItemStack output, final FluidStack fluidInput, final FluidStack fluidOutput,
                              final int duration, final long consumption, final List<AssemblyRequirement> itemInputs,
                              final List<String> blueprintPools) {
            this(id, output, fluidInput, fluidOutput, duration, consumption, itemInputs, List.of(itemInputs), blueprintPools, null);
        }

        public AssemblyRecipe(final String id, final ItemStack output, final FluidStack fluidInput, final FluidStack fluidOutput,
                              final int duration, final long consumption, final List<AssemblyRequirement> itemInputs,
                              final List<String> blueprintPools, final @Nullable String autoSwitchGroup) {
            this(id, output, fluidInput, fluidOutput, duration, consumption, itemInputs, List.of(itemInputs), blueprintPools, autoSwitchGroup);
        }

        public AssemblyRecipe {
            id = Objects.requireNonNull(id, "id");
            output = output.copy();
            fluidInput = fluidInput.copy();
            fluidOutput = fluidOutput.copy();
            duration = Math.max(1, duration);
            consumption = Math.max(0L, consumption);
            itemInputs = List.copyOf(itemInputs);
            itemInputVariants = itemInputVariants.isEmpty() ? List.of(itemInputs) : copyVariants(itemInputVariants);
            blueprintPools = List.copyOf(blueprintPools);
            autoSwitchGroup = autoSwitchGroup == null || autoSwitchGroup.isBlank() ? null : autoSwitchGroup;
        }

        public boolean matches(final List<ItemStack> itemInputs, final FluidStack fluidInput) {
            if (!matchesFluid(fluidInput)) {
                return false;
            }
            return this.findMatchingRequirements(itemInputs).isPresent();
        }

        public Optional<List<AssemblyRequirement>> findMatchingRequirements(final List<ItemStack> itemInputs) {
            for (final List<AssemblyRequirement> requirements : this.itemInputVariants) {
                if (matchesRequirements(requirements, itemInputs)) {
                    return Optional.of(requirements);
                }
            }
            return Optional.empty();
        }

        public @Nullable AssemblyRequirement requirementForSlot(final int slot, final List<ItemStack> itemInputs) {
            if (slot < 0) {
                return null;
            }
            final Optional<List<AssemblyRequirement>> matched = this.findMatchingRequirements(itemInputs);
            if (matched.isPresent()) {
                final List<AssemblyRequirement> requirements = matched.get();
                return slot < requirements.size() ? requirements.get(slot) : null;
            }
            return slot < this.itemInputs.size() ? this.itemInputs.get(slot) : null;
        }

        public boolean acceptsInAnyVariant(final int slot, final ItemStack stack) {
            if (slot < 0 || stack.isEmpty()) {
                return false;
            }
            for (final List<AssemblyRequirement> requirements : this.itemInputVariants) {
                if (slot < requirements.size() && requirements.get(slot).accepts(stack)) {
                    return true;
                }
            }
            return false;
        }

        public boolean firstRequirementAccepts(final ItemStack stack) {
            return this.acceptsInAnyVariant(0, stack);
        }

        private static boolean matchesRequirements(final List<AssemblyRequirement> requirements, final List<ItemStack> itemInputs) {
            if (itemInputs.size() < requirements.size()) {
                return false;
            }
            for (int i = 0; i < requirements.size(); i++) {
                final ItemStack input = i < itemInputs.size() && itemInputs.get(i) != null ? itemInputs.get(i) : ItemStack.EMPTY;
                if (!requirements.get(i).matches(input)) {
                    return false;
                }
            }
            for (int i = requirements.size(); i < itemInputs.size(); i++) {
                final ItemStack input = itemInputs.get(i);
                if (input != null && !input.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        private static List<List<AssemblyRequirement>> copyVariants(final List<List<AssemblyRequirement>> variants) {
            final List<List<AssemblyRequirement>> copied = new ArrayList<>(variants.size());
            for (final List<AssemblyRequirement> variant : variants) {
                copied.add(List.copyOf(variant));
            }
            return List.copyOf(copied);
        }

        public ItemStack outputCopy() {
            return this.output.copy();
        }

        public FluidStack fluidInputCopy() {
            return this.fluidInput.copy();
        }

        public FluidStack fluidOutputCopy() {
            return this.fluidOutput.copy();
        }

        public boolean isPooled() {
            return !this.blueprintPools.isEmpty();
        }

        public boolean matchesBlueprintPool(final @Nullable String pool) {
            if (!this.isPooled()) {
                return true;
            }
            if (pool == null || pool.isBlank()) {
                return false;
            }
            for (final String recipePool : this.blueprintPools) {
                if (recipePool.equals(pool)) {
                    return true;
                }
            }
            return false;
        }

        private boolean matchesFluid(final FluidStack input) {
            if (this.fluidInput.isEmpty()) {
                return true;
            }
            return !input.isEmpty() && input.getAmount() >= this.fluidInput.getAmount() && input.isFluidEqual(this.fluidInput);
        }
    }

    private static final class AssemblyRecipeRegistry extends MachineRecipeRegistry<AssemblyRecipe> {
        @Override
        protected void registerDefaults() {
            HbmAssemblyRecipes.registerDefaults(this);
        }

        private void add(final AssemblyRecipe recipe) {
            this.addRecipe(recipe);
        }
    }
}

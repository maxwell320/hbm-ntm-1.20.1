package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
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
            buildDustSmelting(recipeOutput, material);
        }

        buildBasaltRecipes(recipeOutput);
        buildCircuitRecipes(recipeOutput);
        buildPressSupportRecipes(recipeOutput);
        buildStampRecipes(recipeOutput);
        buildFalloutRecipes(recipeOutput);
        buildReadoutToolRecipes(recipeOutput);
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

    private void buildCircuitRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike vacuumTube = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.VACUUM_TUBE).get());
        final ItemLike capacitor = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CAPACITOR).get());
        final ItemLike tantalumCapacitor = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CAPACITOR_TANTALIUM).get());
        final ItemLike pcb = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.PCB).get());
        final ItemLike siliconWafer = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.SILICON).get());
        final ItemLike chip = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CHIP).get());
        final ItemLike atomicClock = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.ATOMIC_CLOCK).get());
        final ItemLike controllerChassis = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.CONTROLLER_CHASSIS).get());
        final ItemLike numitron = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.NUMITRON).get());
        final ItemLike crtDisplay = Objects.requireNonNull(HbmItems.CRT_DISPLAY.get());
        final ItemLike polymerPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.PLATE).get());
        final ItemLike polymerBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.POLYMER, HbmMaterialShape.INGOT).get());
        final ItemLike bakeliteBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BAKELITE, HbmMaterialShape.INGOT).get());
        final ItemLike latexBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.LATEX, HbmMaterialShape.INGOT).get());
        final ItemLike rubberBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.RUBBER, HbmMaterialShape.INGOT).get());
        final ItemLike petBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PET, HbmMaterialShape.INGOT).get());
        final ItemLike pcBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PC, HbmMaterialShape.INGOT).get());
        final ItemLike pvcBar = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.PVC, HbmMaterialShape.INGOT).get());
        final ItemLike tungstenWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.WIRE).get());
        final ItemLike heatingCoil = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.DENSE_WIRE).get());
        final ItemLike carbonWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.CARBON, HbmMaterialShape.WIRE).get());
        final ItemLike niobiumNugget = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.NIOBIUM, HbmMaterialShape.NUGGET).get());
        final ItemLike tantaliumNugget = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TANTALIUM, HbmMaterialShape.NUGGET).get());
        final ItemLike aluminiumWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.WIRE).get());
        final ItemLike copperWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.WIRE).get());
        final ItemLike aluminiumDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.DUST).get());
        final ItemLike copperPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get());
        final ItemLike goldPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GOLD, HbmMaterialShape.PLATE).get());
        final ItemLike goldWire = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.GOLD, HbmMaterialShape.WIRE).get());
        final ItemLike steelPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.PLATE).get());
        final ItemLike strontiumDust = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.STRONTIUM, HbmMaterialShape.DUST).get());
        final Ingredient plasticBars = Ingredient.of(polymerBar, bakeliteBar, latexBar, rubberBar, petBar, pcBar, pvcBar);

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
    }

    private void buildPressSupportRecipes(final Consumer<FinishedRecipe> recipeOutput) {
        final ItemLike pressPreheater = Objects.requireNonNull(HbmBlocks.PRESS_PREHEATER.get());
        final ItemLike copperPlate = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.PLATE).get());
        final ItemLike tungstenIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT).get());

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
        final ItemLike dosimeter = Objects.requireNonNull(HbmItems.DOSIMETER.get());
        final ItemLike geigerCounter = Objects.requireNonNull(HbmItems.GEIGER_COUNTER.get());
        final ItemLike geigerBlock = Objects.requireNonNull(HbmBlocks.GEIGER.get());
        final ItemLike vacuumTube = Objects.requireNonNull(HbmItems.getCircuit(CircuitItemType.VACUUM_TUBE).get());
        final ItemLike berylliumIngot = Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.BERYLLIUM, HbmMaterialShape.INGOT).get());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ironAnvil)
            .pattern("III")
            .pattern(" B ")
            .pattern("III")
            .define('I', Tags.Items.INGOTS_IRON)
            .define('B', Blocks.IRON_BLOCK)
            .unlockedBy("has_iron_block", has(Blocks.IRON_BLOCK))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "anvil_iron")));

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

    private void buildTwoByTwoBlockRecipe(final Consumer<FinishedRecipe> recipeOutput, final ItemLike output, final ItemLike ingredient, final String recipeName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
            .pattern("CC")
            .pattern("CC")
            .define('C', ingredient)
            .unlockedBy(getHasName(ingredient), has(ingredient))
            .save(recipeOutput, Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, recipeName)));
    }
}

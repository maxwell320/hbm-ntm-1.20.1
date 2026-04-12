package com.hbm.ntm.common.centrifuge;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.OverworldOreType;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.recipe.MachineRecipeRegistry;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("null")
public final class HbmCentrifugeRecipes {
    private static final CentrifugeRecipeRegistry REGISTRY = new CentrifugeRecipeRegistry();

    private HbmCentrifugeRecipes() {
    }

    public static List<CentrifugeRecipe> all() {
        return REGISTRY.all();
    }

    public static Optional<CentrifugeRecipe> findRecipe(final ItemStack input) {
        if (input == null || input.isEmpty()) {
            return Optional.empty();
        }
        return REGISTRY.findFirst(recipe -> recipe.input().test(input));
    }

    public record CentrifugeRecipe(Ingredient input, List<ItemStack> outputs) {
        public CentrifugeRecipe {
            outputs = outputs.stream().map(ItemStack::copy).toList();
        }
    }

    private static final class CentrifugeRecipeRegistry extends MachineRecipeRegistry<CentrifugeRecipe> {
        @Override
        protected void registerDefaults() {
            addRecipe(Ingredient.of(HbmItems.getChunkOre(ChunkOreItemType.RARE).get()),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST_TINY, 2),
                material(HbmMaterials.BORON, HbmMaterialShape.DUST_TINY, 2),
                material(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST_TINY, 2),
                material(HbmMaterials.ZIRCONIUM, HbmMaterialShape.NUGGET, 3));

            addRecipe(Ingredient.of(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, HbmBlocks.getNetherOre(NetherOreType.COAL).get()),
                material(HbmMaterials.COAL, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.COAL, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.COAL, HbmMaterialShape.DUST, 2),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.LIGNITE).get()),
                material(HbmMaterials.LIGNITE, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.LIGNITE, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.LIGNITE, HbmMaterialShape.DUST, 2),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, HbmBlocks.getSellafieldOre(SellafieldOreType.DIAMOND).get()),
                material(HbmMaterials.DIAMOND, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.DIAMOND, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.DIAMOND, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE, HbmBlocks.getSellafieldOre(SellafieldOreType.EMERALD).get()),
                material(HbmMaterials.EMERALD, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.EMERALD, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.EMERALD, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.TITANIUM).get()),
                material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(Blocks.NETHER_QUARTZ_ORE),
                material(HbmMaterials.QUARTZ, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.QUARTZ, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1),
                new ItemStack(Blocks.NETHERRACK));

            final ItemStack certusOre = firstAvailableItem(1, "ae2:quartz_ore", "appliedenergistics2:quartz_ore");
            final ItemStack certusCrystal = firstAvailableItem(2, "ae2:certus_quartz_crystal", "appliedenergistics2:certus_quartz_crystal");
            addRecipeIfInputPresent(certusOre,
                certusCrystal,
                certusCrystal,
                certusCrystal,
                certusCrystal);

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.TUNGSTEN).get(), HbmBlocks.getNetherOre(NetherOreType.TUNGSTEN).get()),
                material(HbmMaterials.TUNGSTEN, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.TUNGSTEN, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.COPPER).get()),
                material(HbmMaterials.COPPER, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.COPPER, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.ALUMINIUM).get()),
                new ItemStack(Objects.requireNonNull(HbmItems.getChunkOre(ChunkOreItemType.CRYOLITE).get()), 2),
                material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.LEAD).get()),
                material(HbmMaterials.LEAD, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.LEAD, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(
                    HbmBlocks.getOverworldOre(OverworldOreType.URANIUM).get(),
                    HbmBlocks.getOverworldOre(OverworldOreType.URANIUM_SCORCHED).get(),
                    HbmBlocks.getNetherOre(NetherOreType.URANIUM).get(),
                    HbmBlocks.getNetherOre(NetherOreType.URANIUM_SCORCHED).get(),
                    HbmBlocks.getSellafieldOre(SellafieldOreType.URANIUM_SCORCHED).get()),
                material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.RA226, HbmMaterialShape.NUGGET, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getNetherOre(NetherOreType.PLUTONIUM).get()),
                material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.POLONIUM, HbmMaterialShape.NUGGET, 3),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.THORIUM).get()),
                material(HbmMaterials.TH232, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.TH232, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.BERYLLIUM).get()),
                material(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.EMERALD, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.FLUORITE).get()),
                material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.SODALITE, HbmMaterialShape.GEM, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.SCHRABIDIUM).get(),
                    HbmBlocks.getNetherOre(NetherOreType.SCHRABIDIUM).get(),
                    HbmBlocks.getSellafieldOre(SellafieldOreType.SCHRABIDIUM).get()),
                material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.SOLINIUM, HbmMaterialShape.NUGGET, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.RARE).get()),
                hbmItem("powder_desh_mix", 1),
                material(HbmMaterials.ZIRCONIUM, HbmMaterialShape.NUGGET, 1),
                material(HbmMaterials.ZIRCONIUM, HbmMaterialShape.NUGGET, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getOverworldOre(OverworldOreType.COBALT).get(), HbmBlocks.getNetherOre(NetherOreType.COBALT).get()),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.COPPER, HbmMaterialShape.DUST, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE),
                new ItemStack(Items.REDSTONE, 3),
                new ItemStack(Items.REDSTONE, 3),
                new ItemStack(Objects.requireNonNull(HbmItems.INGOT_MERCURY.get()), 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE),
                material(HbmMaterials.LAPIS, HbmMaterialShape.DUST, 6),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST_TINY, 1),
                material(HbmMaterials.SODALITE, HbmMaterialShape.GEM, 1),
                new ItemStack(Blocks.GRAVEL));

            addRecipe(Ingredient.of(HbmBlocks.getNetherOre(NetherOreType.FIRE).get()),
                new ItemStack(Items.BLAZE_POWDER, 2),
                material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.WHITE_PHOSPHORUS, HbmMaterialShape.INGOT, 1),
                new ItemStack(Blocks.NETHERRACK, 1));

            addRecipeIfInputPresent(hbmItem("ore_tikite", 1),
                material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST, 2),
                new ItemStack(Blocks.END_STONE, 1));

            addRecipeIfInputPresent(hbmItem("block_euphemium_cluster", 1),
                material(HbmMaterials.EUPHEMIUM, HbmMaterialShape.NUGGET, 7),
                material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.DUST, 4),
                material(HbmMaterials.STARMETAL, HbmMaterialShape.INGOT, 2),
                material(HbmMaterials.SOLINIUM, HbmMaterialShape.NUGGET, 2));

            addRecipe(Ingredient.of(Items.BLAZE_ROD),
                new ItemStack(Items.BLAZE_POWDER, 1),
                new ItemStack(Items.BLAZE_POWDER, 1),
                material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 1));

            addRecipeIfInputPresent(hbmItem("powder_tektite", 1),
                material(HbmMaterials.METEORITE, HbmMaterialShape.DUST_TINY, 1),
                hbmItem("powder_paleogenite_tiny", 1),
                material(HbmMaterials.METEORITE, HbmMaterialShape.DUST_TINY, 1),
                hbmItem("dust", 6));

            addRecipeIfInputPresent(hbmItem("block_slag", 1),
                new ItemStack(Blocks.GRAVEL, 1),
                material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.CALCIUM, HbmMaterialShape.DUST, 1),
                hbmItem("dust", 1));

            addRecipeIfInputPresent(hbmItem("powder_ash", 1),
                material(HbmMaterials.COAL, HbmMaterialShape.DUST_TINY, 2),
                material(HbmMaterials.BORON, HbmMaterialShape.DUST_TINY, 1),
                hbmItem("dust_tiny", 6));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.DIAMOND, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.DIAMOND, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.DIAMOND, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.DIAMOND, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.DIAMOND, HbmMaterialShape.DUST, 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.COAL, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.COAL, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.COAL, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.COAL, HbmMaterialShape.DUST, 3),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.IRON, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.GOLD, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 2),
                new ItemStack(Objects.requireNonNull(HbmItems.INGOT_MERCURY.get()), 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.REDSTONE, HbmMaterialShape.CRYSTAL).get()),
                new ItemStack(Items.REDSTONE, 3),
                new ItemStack(Items.REDSTONE, 3),
                new ItemStack(Items.REDSTONE, 3),
                new ItemStack(Objects.requireNonNull(HbmItems.INGOT_MERCURY.get()), 3));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.LAPIS, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.LAPIS, HbmMaterialShape.DUST, 4),
                material(HbmMaterials.LAPIS, HbmMaterialShape.DUST, 4),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.SODALITE, HbmMaterialShape.GEM, 2));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.SULFUR, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.SULFUR, HbmMaterialShape.DUST, 4),
                material(HbmMaterials.SULFUR, HbmMaterialShape.DUST, 4),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                new ItemStack(Objects.requireNonNull(HbmItems.INGOT_MERCURY.get()), 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.URANIUM, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.RA226, HbmMaterialShape.NUGGET, 2),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.TH232, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.TH232, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.TH232, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.RA226, HbmMaterialShape.NUGGET, 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.PLUTONIUM, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.POLONIUM, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.TITANIUM, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.KNO, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.KNO, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.KNO, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.KNO, HbmMaterialShape.DUST, 3),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.COPPER, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.COPPER, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.COPPER, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.SULFUR, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST_TINY, 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.TUNGSTEN, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.TUNGSTEN, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.ALUMINIUM, HbmMaterialShape.CRYSTAL).get()),
                new ItemStack(Objects.requireNonNull(HbmItems.getChunkOre(ChunkOreItemType.CRYOLITE).get()), 3),
                material(HbmMaterials.TITANIUM, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.BERYLLIUM, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.BERYLLIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.QUARTZ, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.LEAD, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.LEAD, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.LEAD, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.GOLD, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.SCHRARANIUM, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.NUGGET, 2),
                material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.NUGGET, 2),
                material(HbmMaterials.URANIUM, HbmMaterialShape.NUGGET, 2),
                material(HbmMaterials.NEPTUNIUM, HbmMaterialShape.NUGGET, 2));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.SCHRABIDIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 1),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.RARE_EARTH, HbmMaterialShape.CRYSTAL).get()),
                hbmItem("powder_desh_mix", 1),
                hbmItem("powder_desh_mix", 1),
                material(HbmMaterials.ZIRCONIUM, HbmMaterialShape.NUGGET, 2),
                material(HbmMaterials.ZIRCONIUM, HbmMaterialShape.NUGGET, 2));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.WHITE_PHOSPHORUS, HbmMaterialShape.INGOT, 2),
                new ItemStack(Items.BLAZE_POWDER, 2));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.TRIXITE, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST, 2),
                hbmItem("powder_nitan_mix", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.LITHIUM, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.LITHIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.LITHIUM, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.QUARTZ, HbmMaterialShape.DUST, 1),
                material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.COBALT, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 2),
                material(HbmMaterials.IRON, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.COPPER, HbmMaterialShape.DUST, 3),
                tinyDustById("powder_lithium_tiny", 1));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.STARMETAL, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.DURA_STEEL, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 3),
                material(HbmMaterials.ASTATINE, HbmMaterialShape.DUST, 2),
                new ItemStack(Objects.requireNonNull(HbmItems.INGOT_MERCURY.get()), 5));

            addRecipe(Ingredient.of(HbmItems.getMaterialPart(HbmMaterials.FLUORITE, HbmMaterialShape.CRYSTAL).get()),
                material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 4),
                material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 4),
                material(HbmMaterials.SODALITE, HbmMaterialShape.GEM, 2),
                tinyDustById("powder_lithium_tiny", 1));
        }

        private void addRecipeIfInputPresent(final ItemStack input, final ItemStack... outputs) {
            if (input == null || input.isEmpty()) {
                return;
            }
            this.addRecipe(Ingredient.of(input), outputs);
        }

        private void addRecipe(final Ingredient input, final ItemStack... outputs) {
            final List<ItemStack> sanitized = Arrays.stream(outputs)
                .filter(stack -> stack != null && !stack.isEmpty())
                .map(ItemStack::copy)
                .toList();
            if (sanitized.isEmpty()) {
                return;
            }
            this.addRecipe(new CentrifugeRecipe(input, sanitized));
        }
    }

    private static ItemStack material(final HbmMaterialDefinition material, final HbmMaterialShape shape, final int count) {
        try {
            final Item item = Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
            return new ItemStack(item, count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack tinyDustById(final String itemId, final int count) {
        return hbmItem(itemId, count);
    }

    private static ItemStack hbmItem(final String path, final int count) {
        return itemById(HbmNtmMod.MOD_ID + ":" + path, count);
    }

    private static ItemStack firstAvailableItem(final int count, final String... itemIds) {
        for (final String itemId : itemIds) {
            final ItemStack stack = itemById(itemId, count);
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
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
            final Item item = Objects.requireNonNull(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id));
            if (item == Items.AIR) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(item, count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }
}

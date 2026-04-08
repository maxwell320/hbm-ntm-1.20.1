package com.hbm.ntm.common.shredder;

import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

/**
 * Shredder recipe registry — direct port of legacy {@code ShredderRecipes}.
 * <p>
 * Legacy behavior: each input item maps to exactly one output {@link ItemStack}.
 * If no recipe is found the result is {@code scrap} (legacy {@code ModItems.scrap}).
 * <p>
 * This initial safe slice covers:
 * <ul>
 *   <li>Auto-generated ingot/plate → dust recipes for every modern material that has both shapes</li>
 *   <li>Vanilla block/item shredding that maps to vanilla or in-tree modern outputs</li>
 *   <li>Legacy stone → gravel → sand chain</li>
 *   <li>Legacy wood → sawdust recipes</li>
 * </ul>
 * Broader crystal, recycling, sellafite, fracking, and ore-dict recipes remain blocked
 * on content not yet in-tree.
 */
@SuppressWarnings("null")
public final class HbmShredderRecipes {

    private static final Map<Item, ItemStack> RECIPES = new HashMap<>();
    private static boolean initialized;

    private HbmShredderRecipes() {
    }

    public static void ensureInitialized() {
        if (initialized) {
            return;
        }
        initialized = true;
        registerDefaults();
        registerMaterialRecipes();
    }

    /**
     * Returns the shredder output for the given input, or {@code null} if none.
     * Legacy returns scrap when nothing matches; callers should handle null → scrap.
     */
    public static @Nullable ItemStack getResult(final ItemStack input) {
        if (input.isEmpty()) {
            return null;
        }
        final ItemStack result = RECIPES.get(input.getItem());
        return result == null ? null : result.copy();
    }

    public static Map<Item, ItemStack> getAllRecipes() {
        ensureInitialized();
        return Map.copyOf(RECIPES);
    }

    private static void setRecipe(final Item input, final ItemStack output) {
        if (input != null && !output.isEmpty()) {
            RECIPES.putIfAbsent(input, output);
        }
    }

    private static void setRecipe(final ItemStack input, final ItemStack output) {
        if (!input.isEmpty()) {
            setRecipe(input.getItem(), output);
        }
    }

    /**
     * Legacy {@code registerDefaults} — only the slice whose items are in-tree.
     * Values verified against legacy {@code ShredderRecipes.java} lines 137–364.
     */
    private static void registerDefaults() {
        // Glowstone block → 4 glowstone dust (legacy line 141)
        setRecipe(Items.GLOWSTONE, new ItemStack(Items.GLOWSTONE_DUST, 4));

        // Quartz items (legacy lines 142-148)
        setRecipe(Items.QUARTZ, new ItemStack(Items.QUARTZ, 1)); // legacy gives powder_quartz; we map to self until powder_quartz is in-tree

        // Stone/cobble/gravel → gravel/sand chain (legacy lines 160-164)
        setRecipe(Items.STONE, new ItemStack(Items.GRAVEL, 1));
        setRecipe(Items.COBBLESTONE, new ItemStack(Items.GRAVEL, 1));
        setRecipe(Blocks.STONE_BRICKS.asItem(), new ItemStack(Items.GRAVEL, 1));
        setRecipe(Items.GRAVEL, new ItemStack(Items.SAND, 1));

        // Brick recycling (legacy lines 165-168)
        setRecipe(Blocks.BRICKS.asItem(), new ItemStack(Items.CLAY_BALL, 4));
        setRecipe(Blocks.BRICK_STAIRS.asItem(), new ItemStack(Items.CLAY_BALL, 3));
        setRecipe(Items.FLOWER_POT, new ItemStack(Items.CLAY_BALL, 3));
        setRecipe(Items.BRICK, new ItemStack(Items.CLAY_BALL, 1));

        // Sandstone (legacy lines 169-170)
        setRecipe(Blocks.SANDSTONE.asItem(), new ItemStack(Items.SAND, 4));
        setRecipe(Blocks.SANDSTONE_STAIRS.asItem(), new ItemStack(Items.SAND, 6));

        // Clay (legacy lines 171-172)
        setRecipe(Blocks.CLAY.asItem(), new ItemStack(Items.CLAY_BALL, 4));
        setRecipe(Blocks.TERRACOTTA.asItem(), new ItemStack(Items.CLAY_BALL, 4));

        // TNT → 5 gunpowder (legacy line 173, non-GT6 branch)
        setRecipe(Blocks.TNT.asItem(), new ItemStack(Items.GUNPOWDER, 5));

        // Sugar cane → 3 sugar, apple/carrot → 1 sugar (legacy lines 204-206)
        setRecipe(Items.SUGAR_CANE, new ItemStack(Items.SUGAR, 3));
        setRecipe(Items.APPLE, new ItemStack(Items.SUGAR, 1));
        setRecipe(Items.CARROT, new ItemStack(Items.SUGAR, 1));

        // Wool → 4 string (legacy lines 332-336)
        for (final Item dye : new Item[] {
            Blocks.WHITE_WOOL.asItem(), Blocks.ORANGE_WOOL.asItem(), Blocks.MAGENTA_WOOL.asItem(),
            Blocks.LIGHT_BLUE_WOOL.asItem(), Blocks.YELLOW_WOOL.asItem(), Blocks.LIME_WOOL.asItem(),
            Blocks.PINK_WOOL.asItem(), Blocks.GRAY_WOOL.asItem(), Blocks.LIGHT_GRAY_WOOL.asItem(),
            Blocks.CYAN_WOOL.asItem(), Blocks.PURPLE_WOOL.asItem(), Blocks.BLUE_WOOL.asItem(),
            Blocks.BROWN_WOOL.asItem(), Blocks.GREEN_WOOL.asItem(), Blocks.RED_WOOL.asItem(),
            Blocks.BLACK_WOOL.asItem()
        }) {
            setRecipe(dye, new ItemStack(Items.STRING, 4));
        }

        // Stained terracotta → 4 clay (legacy line 334)
        for (final Item terracotta : new Item[] {
            Blocks.WHITE_TERRACOTTA.asItem(), Blocks.ORANGE_TERRACOTTA.asItem(), Blocks.MAGENTA_TERRACOTTA.asItem(),
            Blocks.LIGHT_BLUE_TERRACOTTA.asItem(), Blocks.YELLOW_TERRACOTTA.asItem(), Blocks.LIME_TERRACOTTA.asItem(),
            Blocks.PINK_TERRACOTTA.asItem(), Blocks.GRAY_TERRACOTTA.asItem(), Blocks.LIGHT_GRAY_TERRACOTTA.asItem(),
            Blocks.CYAN_TERRACOTTA.asItem(), Blocks.PURPLE_TERRACOTTA.asItem(), Blocks.BLUE_TERRACOTTA.asItem(),
            Blocks.BROWN_TERRACOTTA.asItem(), Blocks.GREEN_TERRACOTTA.asItem(), Blocks.RED_TERRACOTTA.asItem(),
            Blocks.BLACK_TERRACOTTA.asItem()
        }) {
            setRecipe(terracotta, new ItemStack(Items.CLAY_BALL, 4));
        }

        // Wood → sawdust (legacy lines 217-218)
        final @Nullable Item sawdust = HbmItems.POWDER_SAWDUST.get();
        if (sawdust != null) {
            // Logs → 4 sawdust
            for (final Item log : new Item[] {
                Items.OAK_LOG, Items.SPRUCE_LOG, Items.BIRCH_LOG, Items.JUNGLE_LOG,
                Items.ACACIA_LOG, Items.DARK_OAK_LOG, Items.MANGROVE_LOG, Items.CHERRY_LOG,
                Items.STRIPPED_OAK_LOG, Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_BIRCH_LOG,
                Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_DARK_OAK_LOG
            }) {
                setRecipe(log, new ItemStack(sawdust, 4));
            }
            // Planks → 1 sawdust
            for (final Item plank : new Item[] {
                Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.BIRCH_PLANKS, Items.JUNGLE_PLANKS,
                Items.ACACIA_PLANKS, Items.DARK_OAK_PLANKS, Items.MANGROVE_PLANKS, Items.CHERRY_PLANKS,
                Items.BAMBOO_PLANKS, Items.CRIMSON_PLANKS, Items.WARPED_PLANKS
            }) {
                setRecipe(plank, new ItemStack(sawdust, 1));
            }
        }

        // Saplings → 1 stick (legacy line 219)
        for (final Item sapling : new Item[] {
            Items.OAK_SAPLING, Items.SPRUCE_SAPLING, Items.BIRCH_SAPLING, Items.JUNGLE_SAPLING,
            Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING, Items.MANGROVE_PROPAGULE, Items.CHERRY_SAPLING
        }) {
            setRecipe(sapling, new ItemStack(Items.STICK, 1));
        }

        // Fermented spider eye → 3 poison powder (legacy line 200) — blocked: poison powder not in-tree
        // Enchanted book → magic powder (legacy line 186) — blocked: magic powder not in-tree
    }

    /**
     * Auto-generate ingot → 1 dust and plate → 1 dust recipes for each modern material
     * that has both the source shape and the DUST shape.
     * This mirrors the legacy ore-dict auto-generation in {@code registerPost()}.
     */
    private static void registerMaterialRecipes() {
        for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {
            final @Nullable Item dust = getShapeItem(material, HbmMaterialShape.DUST);
            if (dust == null) {
                continue;
            }

            // ingot → 1 dust (legacy: generateRecipes("ingot", ..., 1))
            final @Nullable Item ingot = getShapeItem(material, HbmMaterialShape.INGOT);
            if (ingot != null) {
                setRecipe(ingot, new ItemStack(dust, 1));
            }

            // plate → 1 dust (legacy: generateRecipes("plate", ..., 1))
            final @Nullable Item plate = getShapeItem(material, HbmMaterialShape.PLATE);
            if (plate != null) {
                setRecipe(plate, new ItemStack(dust, 1));
            }
        }
    }

    private static @Nullable Item getShapeItem(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        if (!material.shapes().contains(shape)) {
            return null;
        }
        final String itemId = material.itemId(shape);
        if (itemId == null) {
            return null;
        }
        try {
            return HbmItems.getMaterialPart(material, shape).get();
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}

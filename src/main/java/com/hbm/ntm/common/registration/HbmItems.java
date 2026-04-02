package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.item.MaterialPartItem;
import com.hbm.ntm.common.item.RadXItem;
import com.hbm.ntm.common.item.RadawayItem;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HbmItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HbmNtmMod.MOD_ID);
    private static final Map<String, RegistryObject<Item>> MATERIAL_PARTS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> CHUNK_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> SIMPLE_ITEMS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> BLOCK_ITEMS = new LinkedHashMap<>();
    public static final RegistryObject<Item> BURNT_BARK = registerSimpleItem("burnt_bark", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FALLOUT = registerSimpleItem("fallout", SIMPLE_ITEMS);
    public static final RegistryObject<Item> FALLOUT_LAYER = registerBlockItem("fallout_layer", HbmBlocks.FALLOUT, BLOCK_ITEMS);
    public static final RegistryObject<Item> GEM_RAD = registerSimpleItem("gem_rad", SIMPLE_ITEMS);
    public static final RegistryObject<Item> IV_EMPTY = registerSimpleItem("iv_empty", SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADAWAY = registerItem("radaway", () -> new RadawayItem(140, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADAWAY_STRONG = registerItem("radaway_strong", () -> new RadawayItem(350, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADAWAY_FLUSH = registerItem("radaway_flush", () -> new RadawayItem(500, new Item.Properties()), SIMPLE_ITEMS);
    public static final RegistryObject<Item> RADX = registerItem("radx", () -> new RadXItem(new Item.Properties()), SIMPLE_ITEMS);

    static {
        HbmMaterials.ordered().forEach(HbmItems::registerMaterialSet);
        registerChunkOreItems();
        registerStoneResourceBlockItems();
        registerBasaltBlockItems();
        registerBasaltOreBlockItems();
    }

    private HbmItems() {
    }

    private static void registerMaterialSet(final HbmMaterialDefinition material) {
        material.shapes().forEach(shape -> registerMaterialPart(material, shape));
    }

    private static void registerChunkOreItems() {
        for (final ChunkOreItemType type : ChunkOreItemType.values()) {
            registerSimpleItem(type.itemId(), CHUNK_ORES);
        }
    }

    private static void registerStoneResourceBlockItems() {
        registerBlockItem("gas_asbestos", HbmBlocks.GAS_ASBESTOS, BLOCK_ITEMS);
        registerBlockItem("sellafield_slaked", HbmBlocks.SELLAFIELD_SLAKED, BLOCK_ITEMS);
        registerBlockItem("waste_log", HbmBlocks.WASTE_LOG, BLOCK_ITEMS);
        registerBlockItem("waste_planks", HbmBlocks.WASTE_PLANKS, BLOCK_ITEMS);
        for (final StoneResourceType type : StoneResourceType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getStoneResource(type), BLOCK_ITEMS);
        }
    }

    private static void registerBasaltBlockItems() {
        for (final BasaltBlockType type : BasaltBlockType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getBasaltBlock(type), BLOCK_ITEMS);
        }
    }

    private static void registerBasaltOreBlockItems() {
        for (final BasaltOreType type : BasaltOreType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getBasaltOre(type), BLOCK_ITEMS);
        }

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getSellafieldOre(type), BLOCK_ITEMS);
        }
    }

    private static RegistryObject<Item> registerMaterialPart(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        final String id = material.itemId(shape);
        final RegistryObject<Item> registryObject = ITEMS.register(id, () -> new MaterialPartItem(material, shape, new Item.Properties()));
        MATERIAL_PARTS.put(id, registryObject);
        return registryObject;
    }

    private static RegistryObject<Item> registerSimpleItem(final String id, final Map<String, RegistryObject<Item>> targetMap) {
        final RegistryObject<Item> registryObject = ITEMS.register(id, () -> new Item(new Item.Properties()));
        targetMap.put(id, registryObject);
        return registryObject;
    }

    private static RegistryObject<Item> registerItem(final String id, final java.util.function.Supplier<? extends Item> supplier,
                                                     final Map<String, RegistryObject<Item>> targetMap) {
        final RegistryObject<Item> registryObject = ITEMS.register(id, supplier);
        targetMap.put(id, registryObject);
        return registryObject;
    }

    private static RegistryObject<Item> registerBlockItem(final String id, final RegistryObject<Block> block, final Map<String, RegistryObject<Item>> targetMap) {
        final RegistryObject<Item> registryObject = ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
        targetMap.put(id, registryObject);
        return registryObject;
    }

    public static Collection<RegistryObject<Item>> creativeTabEntries() {
        final List<RegistryObject<Item>> entries = new ArrayList<>(MATERIAL_PARTS.values());
        entries.addAll(CHUNK_ORES.values());
        entries.addAll(SIMPLE_ITEMS.values());
        entries.addAll(BLOCK_ITEMS.values());
        return Collections.unmodifiableList(entries);
    }

    public static RegistryObject<Item> getMaterialPart(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        final RegistryObject<Item> registryObject = MATERIAL_PARTS.get(material.itemId(shape));
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown material part: " + material.itemId(shape));
        }
        return registryObject;
    }

    public static RegistryObject<Item> getChunkOre(final ChunkOreItemType type) {
        final RegistryObject<Item> registryObject = CHUNK_ORES.get(type.itemId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown chunk ore item: " + type.itemId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getSimpleItem(final String id) {
        final RegistryObject<Item> registryObject = SIMPLE_ITEMS.get(id);
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown simple item: " + id);
        }
        return registryObject;
    }

    public static RegistryObject<Item> getStoneResourceBlockItem(final StoneResourceType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown stone resource block item: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getBasaltBlockItem(final BasaltBlockType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt block item: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Item> getBasaltOreBlockItem(final BasaltOreType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt ore block item: " + type.blockId());
        }
        return registryObject;
    }
}

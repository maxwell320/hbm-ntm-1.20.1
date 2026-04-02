package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.item.MaterialPartItem;
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
    private static final Map<String, RegistryObject<Item>> BLOCK_ITEMS = new LinkedHashMap<>();

    static {
        HbmMaterials.ordered().forEach(HbmItems::registerMaterialSet);
        registerChunkOreItems();
        registerStoneResourceBlockItems();
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
        for (final StoneResourceType type : StoneResourceType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getStoneResource(type), BLOCK_ITEMS);
        }
    }

    private static void registerBasaltOreBlockItems() {
        for (final BasaltOreType type : BasaltOreType.values()) {
            registerBlockItem(type.blockId(), HbmBlocks.getBasaltOre(type), BLOCK_ITEMS);
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

    private static RegistryObject<Item> registerBlockItem(final String id, final RegistryObject<Block> block, final Map<String, RegistryObject<Item>> targetMap) {
        final RegistryObject<Item> registryObject = ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
        targetMap.put(id, registryObject);
        return registryObject;
    }

    public static Collection<RegistryObject<Item>> creativeTabEntries() {
        final List<RegistryObject<Item>> entries = new ArrayList<>(MATERIAL_PARTS.values());
        entries.addAll(CHUNK_ORES.values());
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

    public static RegistryObject<Item> getStoneResourceBlockItem(final StoneResourceType type) {
        final RegistryObject<Item> registryObject = BLOCK_ITEMS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown stone resource block item: " + type.blockId());
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

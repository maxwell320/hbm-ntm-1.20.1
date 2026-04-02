package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreBlock;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.FalloutBlock;
import com.hbm.ntm.common.block.GasAsbestosBlock;
import com.hbm.ntm.common.block.SellafieldOreBlock;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.SellafieldSlakedBlock;
import com.hbm.ntm.common.block.StoneResourceBlock;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.block.WasteLogBlock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HbmBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HbmNtmMod.MOD_ID);
    private static final Map<String, RegistryObject<Block>> BASALT_BLOCKS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> BASALT_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> SELLAFIELD_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> STONE_RESOURCES = new LinkedHashMap<>();

    public static final RegistryObject<Block> GAS_ASBESTOS = BLOCKS.register("gas_asbestos",
        () -> new GasAsbestosBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.AIR).replaceable().noCollission().noOcclusion().randomTicks())));
    public static final RegistryObject<Block> FALLOUT = BLOCKS.register("fallout", FalloutBlock::new);
    public static final RegistryObject<Block> SELLAFIELD_SLAKED = BLOCKS.register("sellafield_slaked",
        () -> new SellafieldSlakedBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5.0F, 6.0F).requiresCorrectToolForDrops())));
    public static final RegistryObject<Block> ORE_SELLAFIELD_DIAMOND = registerSellafieldOre(SellafieldOreType.DIAMOND);
    public static final RegistryObject<Block> ORE_SELLAFIELD_EMERALD = registerSellafieldOre(SellafieldOreType.EMERALD);
    public static final RegistryObject<Block> ORE_SELLAFIELD_URANIUM_SCORCHED = registerSellafieldOre(SellafieldOreType.URANIUM_SCORCHED);
    public static final RegistryObject<Block> ORE_SELLAFIELD_SCHRABIDIUM = registerSellafieldOre(SellafieldOreType.SCHRABIDIUM);
    public static final RegistryObject<Block> ORE_SELLAFIELD_RADGEM = registerSellafieldOre(SellafieldOreType.RADGEM);
    public static final RegistryObject<Block> WASTE_LOG = BLOCKS.register("waste_log",
        () -> new WasteLogBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(5.0F, 2.5F))));
    public static final RegistryObject<Block> WASTE_PLANKS = BLOCKS.register("waste_planks",
        () -> new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5F, 2.5F))));
    public static final RegistryObject<Block> BASALT = registerBasaltBlock(BasaltBlockType.BASALT);
    public static final RegistryObject<Block> BASALT_SMOOTH = registerBasaltBlock(BasaltBlockType.BASALT_SMOOTH);
    public static final RegistryObject<Block> BASALT_POLISHED = registerBasaltBlock(BasaltBlockType.BASALT_POLISHED);
    public static final RegistryObject<Block> BASALT_BRICK = registerBasaltBlock(BasaltBlockType.BASALT_BRICK);
    public static final RegistryObject<Block> BASALT_TILES = registerBasaltBlock(BasaltBlockType.BASALT_TILES);
    public static final RegistryObject<Block> ORE_BASALT_SULFUR = registerBasaltOre(BasaltOreType.SULFUR);
    public static final RegistryObject<Block> ORE_BASALT_FLUORITE = registerBasaltOre(BasaltOreType.FLUORITE);
    public static final RegistryObject<Block> ORE_BASALT_ASBESTOS = registerBasaltOre(BasaltOreType.ASBESTOS);
    public static final RegistryObject<Block> ORE_BASALT_GEM = registerBasaltOre(BasaltOreType.GEM);
    public static final RegistryObject<Block> ORE_BASALT_MOLYSITE = registerBasaltOre(BasaltOreType.MOLYSITE);
    public static final RegistryObject<Block> STONE_RESOURCE_SULFUR = registerStoneResource(StoneResourceType.SULFUR);
    public static final RegistryObject<Block> STONE_RESOURCE_ASBESTOS = registerStoneResource(StoneResourceType.ASBESTOS);
    public static final RegistryObject<Block> STONE_RESOURCE_LIMESTONE = registerStoneResource(StoneResourceType.LIMESTONE);
    public static final RegistryObject<Block> STONE_RESOURCE_BAUXITE = registerStoneResource(StoneResourceType.BAUXITE);
    public static final RegistryObject<Block> STONE_RESOURCE_HEMATITE = registerStoneResource(StoneResourceType.HEMATITE);
    public static final RegistryObject<Block> STONE_RESOURCE_MALACHITE = registerStoneResource(StoneResourceType.MALACHITE);

    private HbmBlocks() {
    }

    private static RegistryObject<Block> registerBasaltBlock(final BasaltBlockType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> type.pillar()
                ? new RotatedPillarBlock(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(5.0F, 10.0F).requiresCorrectToolForDrops()))
                : new Block(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
        BASALT_BLOCKS.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerBasaltOre(final BasaltOreType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new BasaltOreBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(5.0F, 10.0F).requiresCorrectToolForDrops())));
        BASALT_ORES.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerSellafieldOre(final SellafieldOreType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new SellafieldOreBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5.0F, 6.0F).requiresCorrectToolForDrops())));
        SELLAFIELD_ORES.put(type.blockId(), registryObject);
        return registryObject;
    }

    private static RegistryObject<Block> registerStoneResource(final StoneResourceType type) {
        final RegistryObject<Block> registryObject = BLOCKS.register(type.blockId(),
            () -> new StoneResourceBlock(type, Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).requiresCorrectToolForDrops())));
        STONE_RESOURCES.put(type.blockId(), registryObject);
        return registryObject;
    }

    public static RegistryObject<Block> getBasaltOre(final BasaltOreType type) {
        final RegistryObject<Block> registryObject = BASALT_ORES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt ore block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getBasaltBlock(final BasaltBlockType type) {
        final RegistryObject<Block> registryObject = BASALT_BLOCKS.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown basalt block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getSellafieldOre(final SellafieldOreType type) {
        final RegistryObject<Block> registryObject = SELLAFIELD_ORES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown sellafield ore block: " + type.blockId());
        }
        return registryObject;
    }

    public static RegistryObject<Block> getStoneResource(final StoneResourceType type) {
        final RegistryObject<Block> registryObject = STONE_RESOURCES.get(type.blockId());
        if (registryObject == null) {
            throw new IllegalArgumentException("Unknown stone resource block: " + type.blockId());
        }
        return registryObject;
    }
}

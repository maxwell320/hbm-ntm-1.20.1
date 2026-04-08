package com.hbm.ntm.data;

import com.hbm.ntm.common.block.PressBlock;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.press.PressPart;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;

@SuppressWarnings("null")
public class HbmBlockLootProvider extends BlockLootSubProvider {
    private final Set<Block> knownBlocks = new LinkedHashSet<>();

    public HbmBlockLootProvider() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void add(final Block block, final LootTable.Builder builder) {
        super.add(block, builder);
        knownBlocks.add(block);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected void generate() {
        add(HbmBlocks.GAS_ASBESTOS.get(), noDrop());
        dropSelf(HbmBlocks.ANVIL_IRON.get());
        dropSelf(HbmBlocks.ANVIL_LEAD.get());
        dropSelf(HbmBlocks.ANVIL_STEEL.get());
        dropSelf(HbmBlocks.ANVIL_DESH.get());
        dropSelf(HbmBlocks.ANVIL_FERRORANIUM.get());
        dropSelf(HbmBlocks.ANVIL_SATURNITE.get());
        dropSelf(HbmBlocks.ANVIL_BISMUTH_BRONZE.get());
        dropSelf(HbmBlocks.ANVIL_ARSENIC_BRONZE.get());
        dropSelf(HbmBlocks.ANVIL_SCHRABIDATE.get());
        dropSelf(HbmBlocks.ANVIL_DNT.get());
        dropSelf(HbmBlocks.ANVIL_OSMIRIDIUM.get());
        dropSelf(HbmBlocks.ANVIL_MURKY.get());
        dropSelf(HbmBlocks.CREATIVE_ENERGY_SOURCE.get());
        dropSelf(HbmBlocks.MACHINE_BATTERY.get());
        add(HbmBlocks.FALLOUT.get(), createSingleItemTable(HbmItems.FALLOUT.get()));
        dropSelf(HbmBlocks.GEIGER.get());
        add(HbmBlocks.MACHINE_PRESS.get(), createPressTable());
        dropSelf(HbmBlocks.PRESS_PREHEATER.get());
        dropSelf(HbmBlocks.BARREL_PLASTIC.get());
        dropSelf(HbmBlocks.BARREL_CORRODED.get());
        dropSelf(HbmBlocks.BARREL_IRON.get());
        dropSelf(HbmBlocks.BARREL_STEEL.get());
        dropSelf(HbmBlocks.BARREL_TCALLOY.get());
        dropSelf(HbmBlocks.BARREL_ANTIMATTER.get());
        dropSelf(HbmBlocks.RED_CABLE.get());
        dropSelf(HbmBlocks.RED_CABLE_CLASSIC.get());
        dropSelf(HbmBlocks.FLUID_DUCT_NEO.get());
        dropSelf(HbmBlocks.SELLAFIELD_SLAKED.get());
        add(HbmBlocks.WASTE_LOG.get(), createWasteLogTable(HbmBlocks.WASTE_LOG.get()));
        dropSelf(HbmBlocks.WASTE_PLANKS.get());

        for (final MaterialBlockType type : MaterialBlockType.values()) {
            dropSelf(HbmBlocks.getMaterialBlock(type).get());
        }

        for (final BasaltBlockType type : BasaltBlockType.values()) {
            dropSelf(HbmBlocks.getBasaltBlock(type).get());
        }

        add(HbmBlocks.getBasaltOre(BasaltOreType.SULFUR).get(), createSingleItemTable(HbmItems.getMaterialPart(HbmMaterials.SULFUR, HbmMaterialShape.DUST).get()));
        add(HbmBlocks.getBasaltOre(BasaltOreType.FLUORITE).get(), createSingleItemTable(HbmItems.getMaterialPart(HbmMaterials.FLUORITE, HbmMaterialShape.DUST).get()));
        add(HbmBlocks.getBasaltOre(BasaltOreType.ASBESTOS).get(), createSingleItemTable(HbmItems.getMaterialPart(HbmMaterials.ASBESTOS, HbmMaterialShape.INGOT).get()));
        add(HbmBlocks.getBasaltOre(BasaltOreType.GEM).get(), createSingleItemTable(HbmItems.getMaterialPart(HbmMaterials.VOLCANIC, HbmMaterialShape.GEM).get()));
        add(HbmBlocks.getBasaltOre(BasaltOreType.MOLYSITE).get(), createSingleItemTable(HbmItems.getMaterialPart(HbmMaterials.MOLYSITE, HbmMaterialShape.DUST).get()));

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            final Block block = HbmBlocks.getSellafieldOre(type).get();
            final Item drop = switch (type) {
                case DIAMOND -> Items.DIAMOND;
                case EMERALD -> Items.EMERALD;
                case RADGEM -> HbmItems.GEM_RAD.get();
                case URANIUM_SCORCHED, SCHRABIDIUM -> block.asItem();
            };

            if (type == SellafieldOreType.URANIUM_SCORCHED || type == SellafieldOreType.SCHRABIDIUM) {
                dropSelf(block);
            } else {
                add(block, createOreDrop(block, drop));
            }
        }

        dropSelf(HbmBlocks.getStoneResource(StoneResourceType.SULFUR).get());
        dropSelf(HbmBlocks.getStoneResource(StoneResourceType.ASBESTOS).get());
        dropSelf(HbmBlocks.getStoneResource(StoneResourceType.LIMESTONE).get());
        dropSelf(HbmBlocks.getStoneResource(StoneResourceType.BAUXITE).get());
        dropSelf(HbmBlocks.getStoneResource(StoneResourceType.HEMATITE).get());
        dropSelf(HbmBlocks.getStoneResource(StoneResourceType.MALACHITE).get());
    }

    private LootTable.Builder createWasteLogTable(final Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(AlternativesEntry.alternatives(
                applyExplosionDecay(block, LootItem.lootTableItem(HbmItems.BURNT_BARK.get())
                    .when(LootItemRandomChanceCondition.randomChance(0.001F))),
                applyExplosionDecay(block, LootItem.lootTableItem(Items.CHARCOAL)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
            )));
    }

    private LootTable.Builder createPressTable() {
        return LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(applyExplosionDecay(HbmBlocks.MACHINE_PRESS.get(), LootItem.lootTableItem(HbmBlocks.MACHINE_PRESS.get())
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(HbmBlocks.MACHINE_PRESS.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PressBlock.PART, PressPart.CORE))))));
    }
}

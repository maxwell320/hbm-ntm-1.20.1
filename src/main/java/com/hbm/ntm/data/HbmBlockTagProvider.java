package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.OverworldOreType;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.tag.HbmBlockTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class HbmBlockTagProvider extends BlockTagsProvider {
    public HbmBlockTagProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HbmNtmMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final @NotNull HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(HbmBlocks.FALLOUT.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(HbmBlocks.WASTE_LOG.get(), HbmBlocks.WASTE_PLANKS.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.CREATIVE_ENERGY_SOURCE.get(), HbmBlocks.MACHINE_BATTERY.get(), HbmBlocks.GEIGER.get(), HbmBlocks.MACHINE_PRESS.get(), HbmBlocks.MACHINE_ASSEMBLY_MACHINE.get(), HbmBlocks.MACHINE_SOLDERING_STATION.get(), HbmBlocks.MACHINE_SHREDDER.get(), HbmBlocks.MACHINE_CENTRIFUGE.get(), HbmBlocks.MACHINE_GAS_CENTRIFUGE.get(), HbmBlocks.MACHINE_PUREX.get(), HbmBlocks.MACHINE_ICF.get(), HbmBlocks.MACHINE_ICF_CONTROLLER.get(), HbmBlocks.MACHINE_ICF_LASER_COMPONENT.get(), HbmBlocks.MACHINE_ICF_PRESS.get(), HbmBlocks.PRESS_PREHEATER.get(), HbmBlocks.BARREL_PLASTIC.get(), HbmBlocks.BARREL_CORRODED.get(), HbmBlocks.BARREL_IRON.get(), HbmBlocks.BARREL_STEEL.get(), HbmBlocks.BARREL_TCALLOY.get(), HbmBlocks.BARREL_ANTIMATTER.get(), HbmBlocks.RED_CABLE.get(), HbmBlocks.RED_CABLE_CLASSIC.get(), HbmBlocks.FLUID_DUCT_NEO.get(), HbmBlocks.ANVIL_IRON.get(), HbmBlocks.ANVIL_LEAD.get(),
            HbmBlocks.ANVIL_STEEL.get(), HbmBlocks.ANVIL_DESH.get(), HbmBlocks.ANVIL_FERRORANIUM.get(), HbmBlocks.ANVIL_SATURNITE.get(), HbmBlocks.ANVIL_BISMUTH_BRONZE.get(), HbmBlocks.ANVIL_ARSENIC_BRONZE.get(), HbmBlocks.ANVIL_SCHRABIDATE.get(),
            HbmBlocks.ANVIL_DNT.get(), HbmBlocks.ANVIL_OSMIRIDIUM.get(), HbmBlocks.ANVIL_MURKY.get(), HbmBlocks.SELLAFIELD.get(), HbmBlocks.SELLAFIELD_SLAKED.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.CREATIVE_ENERGY_SOURCE.get(), HbmBlocks.MACHINE_BATTERY.get(), HbmBlocks.MACHINE_PRESS.get(), HbmBlocks.MACHINE_ASSEMBLY_MACHINE.get(), HbmBlocks.MACHINE_SOLDERING_STATION.get(), HbmBlocks.MACHINE_SHREDDER.get(), HbmBlocks.MACHINE_CENTRIFUGE.get(), HbmBlocks.MACHINE_GAS_CENTRIFUGE.get(), HbmBlocks.MACHINE_PUREX.get(), HbmBlocks.MACHINE_ICF.get(), HbmBlocks.MACHINE_ICF_CONTROLLER.get(), HbmBlocks.MACHINE_ICF_LASER_COMPONENT.get(), HbmBlocks.MACHINE_ICF_PRESS.get(), HbmBlocks.PRESS_PREHEATER.get(), HbmBlocks.BARREL_PLASTIC.get(), HbmBlocks.BARREL_CORRODED.get(), HbmBlocks.BARREL_IRON.get(), HbmBlocks.BARREL_STEEL.get(), HbmBlocks.BARREL_TCALLOY.get(), HbmBlocks.BARREL_ANTIMATTER.get(), HbmBlocks.RED_CABLE.get(), HbmBlocks.RED_CABLE_CLASSIC.get(), HbmBlocks.FLUID_DUCT_NEO.get(), HbmBlocks.ANVIL_IRON.get(), HbmBlocks.ANVIL_LEAD.get(), HbmBlocks.ANVIL_STEEL.get(),
            HbmBlocks.ANVIL_DESH.get(), HbmBlocks.ANVIL_FERRORANIUM.get(), HbmBlocks.ANVIL_SATURNITE.get(), HbmBlocks.ANVIL_BISMUTH_BRONZE.get(), HbmBlocks.ANVIL_ARSENIC_BRONZE.get(), HbmBlocks.ANVIL_SCHRABIDATE.get(),
            HbmBlocks.ANVIL_DNT.get(), HbmBlocks.ANVIL_OSMIRIDIUM.get(), HbmBlocks.ANVIL_MURKY.get(), HbmBlocks.SELLAFIELD.get(), HbmBlocks.SELLAFIELD_SLAKED.get());

        for (final MaterialBlockType type : MaterialBlockType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getMaterialBlock(type).get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getMaterialBlock(type).get());
        }

        for (final BasaltBlockType type : BasaltBlockType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getBasaltBlock(type).get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getBasaltBlock(type).get());
        }

        for (final BasaltOreType type : BasaltOreType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getBasaltOre(type).get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getBasaltOre(type).get());
        }

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getSellafieldOre(type).get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getSellafieldOre(type).get());
        }

        for (final StoneResourceType type : StoneResourceType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getStoneResource(type).get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getStoneResource(type).get());
        }

        for (final OverworldOreType type : OverworldOreType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getOverworldOre(type).get());
            switch (type.harvestLevel()) {
                case 0 -> {}
                case 1 -> tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getOverworldOre(type).get());
                case 2 -> tag(BlockTags.NEEDS_IRON_TOOL).add(HbmBlocks.getOverworldOre(type).get());
                default -> tag(BlockTags.NEEDS_DIAMOND_TOOL).add(HbmBlocks.getOverworldOre(type).get());
            }
        }

        for (final NetherOreType type : NetherOreType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getNetherOre(type).get());
            tag(BlockTags.NEEDS_IRON_TOOL).add(HbmBlocks.getNetherOre(type).get());
        }

        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/sulfur")).add(HbmBlocks.getBasaltOre(BasaltOreType.SULFUR).get(), HbmBlocks.getStoneResource(StoneResourceType.SULFUR).get());
        tag(HbmBlockTags.named("forge", "ores/sulfur")).add(HbmBlocks.getBasaltOre(BasaltOreType.SULFUR).get(), HbmBlocks.getStoneResource(StoneResourceType.SULFUR).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/asbestos")).add(HbmBlocks.getBasaltOre(BasaltOreType.ASBESTOS).get(), HbmBlocks.getStoneResource(StoneResourceType.ASBESTOS).get());
        tag(HbmBlockTags.named("forge", "ores/asbestos")).add(HbmBlocks.getBasaltOre(BasaltOreType.ASBESTOS).get(), HbmBlocks.getStoneResource(StoneResourceType.ASBESTOS).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/fluorite")).add(HbmBlocks.getBasaltOre(BasaltOreType.FLUORITE).get());
        tag(HbmBlockTags.named("forge", "ores/fluorite")).add(HbmBlocks.getBasaltOre(BasaltOreType.FLUORITE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/molysite")).add(HbmBlocks.getBasaltOre(BasaltOreType.MOLYSITE).get());
        tag(HbmBlockTags.named("forge", "ores/molysite")).add(HbmBlocks.getBasaltOre(BasaltOreType.MOLYSITE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/volcanic")).add(HbmBlocks.getBasaltOre(BasaltOreType.GEM).get());
        tag(HbmBlockTags.named("forge", "ores/volcanic")).add(HbmBlocks.getBasaltOre(BasaltOreType.GEM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/limestone")).add(HbmBlocks.getStoneResource(StoneResourceType.LIMESTONE).get());
        tag(HbmBlockTags.named("forge", "ores/limestone")).add(HbmBlocks.getStoneResource(StoneResourceType.LIMESTONE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/bauxite")).add(HbmBlocks.getStoneResource(StoneResourceType.BAUXITE).get());
        tag(HbmBlockTags.named("forge", "ores/bauxite")).add(HbmBlocks.getStoneResource(StoneResourceType.BAUXITE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/hematite")).add(HbmBlocks.getStoneResource(StoneResourceType.HEMATITE).get());
        tag(HbmBlockTags.named("forge", "ores/hematite")).add(HbmBlocks.getStoneResource(StoneResourceType.HEMATITE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/malachite")).add(HbmBlocks.getStoneResource(StoneResourceType.MALACHITE).get());
        tag(HbmBlockTags.named("forge", "ores/malachite")).add(HbmBlocks.getStoneResource(StoneResourceType.MALACHITE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/uranium")).add(HbmBlocks.getSellafieldOre(SellafieldOreType.URANIUM_SCORCHED).get(), HbmBlocks.getOverworldOre(OverworldOreType.URANIUM).get(), HbmBlocks.getOverworldOre(OverworldOreType.URANIUM_SCORCHED).get(), HbmBlocks.getNetherOre(NetherOreType.URANIUM).get(), HbmBlocks.getNetherOre(NetherOreType.URANIUM_SCORCHED).get());
        tag(HbmBlockTags.named("forge", "ores/uranium")).add(HbmBlocks.getOverworldOre(OverworldOreType.URANIUM).get(), HbmBlocks.getOverworldOre(OverworldOreType.URANIUM_SCORCHED).get(), HbmBlocks.getNetherOre(NetherOreType.URANIUM).get(), HbmBlocks.getNetherOre(NetherOreType.URANIUM_SCORCHED).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/schrabidium")).add(HbmBlocks.getSellafieldOre(SellafieldOreType.SCHRABIDIUM).get(), HbmBlocks.getOverworldOre(OverworldOreType.SCHRABIDIUM).get(), HbmBlocks.getNetherOre(NetherOreType.SCHRABIDIUM).get());
        tag(HbmBlockTags.named("forge", "ores/schrabidium")).add(HbmBlocks.getOverworldOre(OverworldOreType.SCHRABIDIUM).get(), HbmBlocks.getNetherOre(NetherOreType.SCHRABIDIUM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/titanium")).add(HbmBlocks.getOverworldOre(OverworldOreType.TITANIUM).get());
        tag(HbmBlockTags.named("forge", "ores/titanium")).add(HbmBlocks.getOverworldOre(OverworldOreType.TITANIUM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/thorium")).add(HbmBlocks.getOverworldOre(OverworldOreType.THORIUM).get());
        tag(HbmBlockTags.named("forge", "ores/thorium")).add(HbmBlocks.getOverworldOre(OverworldOreType.THORIUM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/copper")).add(HbmBlocks.getOverworldOre(OverworldOreType.COPPER).get());
        tag(HbmBlockTags.named("forge", "ores/copper")).add(HbmBlocks.getOverworldOre(OverworldOreType.COPPER).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/tungsten")).add(HbmBlocks.getOverworldOre(OverworldOreType.TUNGSTEN).get(), HbmBlocks.getNetherOre(NetherOreType.TUNGSTEN).get());
        tag(HbmBlockTags.named("forge", "ores/tungsten")).add(HbmBlocks.getOverworldOre(OverworldOreType.TUNGSTEN).get(), HbmBlocks.getNetherOre(NetherOreType.TUNGSTEN).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/aluminium")).add(HbmBlocks.getOverworldOre(OverworldOreType.ALUMINIUM).get());
        tag(HbmBlockTags.named("forge", "ores/aluminium")).add(HbmBlocks.getOverworldOre(OverworldOreType.ALUMINIUM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/fluorite")).add(HbmBlocks.getBasaltOre(BasaltOreType.FLUORITE).get(), HbmBlocks.getOverworldOre(OverworldOreType.FLUORITE).get());
        tag(HbmBlockTags.named("forge", "ores/fluorite")).add(HbmBlocks.getBasaltOre(BasaltOreType.FLUORITE).get(), HbmBlocks.getOverworldOre(OverworldOreType.FLUORITE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/lead")).add(HbmBlocks.getOverworldOre(OverworldOreType.LEAD).get());
        tag(HbmBlockTags.named("forge", "ores/lead")).add(HbmBlocks.getOverworldOre(OverworldOreType.LEAD).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/beryllium")).add(HbmBlocks.getOverworldOre(OverworldOreType.BERYLLIUM).get());
        tag(HbmBlockTags.named("forge", "ores/beryllium")).add(HbmBlocks.getOverworldOre(OverworldOreType.BERYLLIUM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/australium")).add(HbmBlocks.getOverworldOre(OverworldOreType.AUSTRALIUM).get());
        tag(HbmBlockTags.named("forge", "ores/australium")).add(HbmBlocks.getOverworldOre(OverworldOreType.AUSTRALIUM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/rare_earth")).add(HbmBlocks.getOverworldOre(OverworldOreType.RARE).get());
        tag(HbmBlockTags.named("forge", "ores/rare_earth")).add(HbmBlocks.getOverworldOre(OverworldOreType.RARE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/cobalt")).add(HbmBlocks.getOverworldOre(OverworldOreType.COBALT).get(), HbmBlocks.getNetherOre(NetherOreType.COBALT).get());
        tag(HbmBlockTags.named("forge", "ores/cobalt")).add(HbmBlocks.getOverworldOre(OverworldOreType.COBALT).get(), HbmBlocks.getNetherOre(NetherOreType.COBALT).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/cinnabar")).add(HbmBlocks.getOverworldOre(OverworldOreType.CINNEBAR).get());
        tag(HbmBlockTags.named("forge", "ores/cinnabar")).add(HbmBlocks.getOverworldOre(OverworldOreType.CINNEBAR).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/coltan")).add(HbmBlocks.getOverworldOre(OverworldOreType.COLTAN).get());
        tag(HbmBlockTags.named("forge", "ores/coltan")).add(HbmBlocks.getOverworldOre(OverworldOreType.COLTAN).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/lignite")).add(HbmBlocks.getOverworldOre(OverworldOreType.LIGNITE).get());
        tag(HbmBlockTags.named("forge", "ores/lignite")).add(HbmBlocks.getOverworldOre(OverworldOreType.LIGNITE).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/niter")).add(HbmBlocks.getOverworldOre(OverworldOreType.NITER).get());
        tag(HbmBlockTags.named("forge", "ores/niter")).add(HbmBlocks.getOverworldOre(OverworldOreType.NITER).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/asbestos")).add(HbmBlocks.getBasaltOre(BasaltOreType.ASBESTOS).get(), HbmBlocks.getStoneResource(StoneResourceType.ASBESTOS).get(), HbmBlocks.getOverworldOre(OverworldOreType.ASBESTOS).get());
        tag(HbmBlockTags.named("forge", "ores/asbestos")).add(HbmBlocks.getBasaltOre(BasaltOreType.ASBESTOS).get(), HbmBlocks.getStoneResource(StoneResourceType.ASBESTOS).get(), HbmBlocks.getOverworldOre(OverworldOreType.ASBESTOS).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/plutonium")).add(HbmBlocks.getNetherOre(NetherOreType.PLUTONIUM).get());
        tag(HbmBlockTags.named("forge", "ores/plutonium")).add(HbmBlocks.getNetherOre(NetherOreType.PLUTONIUM).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "ores/sulfur")).add(HbmBlocks.getBasaltOre(BasaltOreType.SULFUR).get(), HbmBlocks.getStoneResource(StoneResourceType.SULFUR).get(), HbmBlocks.getNetherOre(NetherOreType.SULFUR).get());
        tag(HbmBlockTags.named("forge", "ores/sulfur")).add(HbmBlocks.getBasaltOre(BasaltOreType.SULFUR).get(), HbmBlocks.getStoneResource(StoneResourceType.SULFUR).get(), HbmBlocks.getNetherOre(NetherOreType.SULFUR).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "storage_blocks/steel")).add(HbmBlocks.getMaterialBlock(MaterialBlockType.STEEL).get());
        tag(HbmBlockTags.named("forge", "storage_blocks/steel")).add(HbmBlocks.getMaterialBlock(MaterialBlockType.STEEL).get());
        tag(HbmBlockTags.named(HbmNtmMod.MOD_ID, "storage_blocks/beryllium")).add(HbmBlocks.getMaterialBlock(MaterialBlockType.BERYLLIUM).get());
        tag(HbmBlockTags.named("forge", "storage_blocks/beryllium")).add(HbmBlocks.getMaterialBlock(MaterialBlockType.BERYLLIUM).get());
    }

    @Override
    public String getName() {
        return "HBM Block Tags";
    }
}

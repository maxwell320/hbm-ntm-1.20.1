package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltOreType;
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

public class HbmBlockTagProvider extends BlockTagsProvider {
    public HbmBlockTagProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HbmNtmMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final @NotNull HolderLookup.Provider provider) {
        for (final BasaltOreType type : BasaltOreType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getBasaltOre(type).get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getBasaltOre(type).get());
        }

        for (final StoneResourceType type : StoneResourceType.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(HbmBlocks.getStoneResource(type).get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(HbmBlocks.getStoneResource(type).get());
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
    }

    @Override
    public String getName() {
        return "HBM Block Tags";
    }
}

package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmItems;
import com.hbm.ntm.common.tag.HbmItemTags;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class HbmItemTagProvider extends ItemTagsProvider {
    public HbmItemTagProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, HbmNtmMod.MOD_ID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("null")
    protected void addTags(final @NotNull HolderLookup.Provider provider) {
        for (final HbmMaterialShape shape : HbmMaterialShape.values()) {
            tag(Objects.requireNonNull(HbmItemTags.shape(shape)));
        }

        for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {
            final var materialTag = tag(Objects.requireNonNull(HbmItemTags.material(material)));

            for (final HbmMaterialShape shape : material.shapes()) {
                final Item partItem = Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
                materialTag.add(partItem);
                tag(Objects.requireNonNull(HbmItemTags.shape(shape))).add(partItem);
                tag(Objects.requireNonNull(HbmItemTags.materialShape(material, shape))).add(partItem);
                tag(Objects.requireNonNull(HbmItemTags.forgeMaterialShape(material, shape))).add(partItem);
            }
        }

        addLegacyOreAliases();
    }

    private void addLegacyOreAliases() {
        final Item chunkRare = Objects.requireNonNull(HbmItems.getChunkOre(ChunkOreItemType.RARE).get());
        final Item chunkCryolite = Objects.requireNonNull(HbmItems.getChunkOre(ChunkOreItemType.CRYOLITE).get());
        final Item chunkMalachite = Objects.requireNonNull(HbmItems.getChunkOre(ChunkOreItemType.MALACHITE).get());
        final Item basaltSulfur = Objects.requireNonNull(HbmItems.getBasaltOreBlockItem(BasaltOreType.SULFUR).get());
        final Item basaltFluorite = Objects.requireNonNull(HbmItems.getBasaltOreBlockItem(BasaltOreType.FLUORITE).get());
        final Item basaltAsbestos = Objects.requireNonNull(HbmItems.getBasaltOreBlockItem(BasaltOreType.ASBESTOS).get());
        final Item basaltGem = Objects.requireNonNull(HbmItems.getBasaltOreBlockItem(BasaltOreType.GEM).get());
        final Item basaltMolysite = Objects.requireNonNull(HbmItems.getBasaltOreBlockItem(BasaltOreType.MOLYSITE).get());
        final Item stoneSulfur = Objects.requireNonNull(HbmItems.getStoneResourceBlockItem(StoneResourceType.SULFUR).get());
        final Item stoneAsbestos = Objects.requireNonNull(HbmItems.getStoneResourceBlockItem(StoneResourceType.ASBESTOS).get());
        final Item stoneLimestone = Objects.requireNonNull(HbmItems.getStoneResourceBlockItem(StoneResourceType.LIMESTONE).get());
        final Item stoneBauxite = Objects.requireNonNull(HbmItems.getStoneResourceBlockItem(StoneResourceType.BAUXITE).get());
        final Item stoneHematite = Objects.requireNonNull(HbmItems.getStoneResourceBlockItem(StoneResourceType.HEMATITE).get());
        final Item stoneMalachite = Objects.requireNonNull(HbmItems.getStoneResourceBlockItem(StoneResourceType.MALACHITE).get());

        tag(Objects.requireNonNull(HbmItemTags.shape(HbmMaterialShape.INGOT))).add(chunkRare, chunkMalachite);
        tag(Objects.requireNonNull(HbmItemTags.shape(HbmMaterialShape.CRYSTAL))).add(chunkCryolite);
        tag(Objects.requireNonNull(HbmItemTags.shape(HbmMaterialShape.GEM))).add(stoneBauxite);

        tag(Objects.requireNonNull(HbmItemTags.material(HbmMaterials.RARE_EARTH))).add(chunkRare);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ingots/rare_earth"))).add(chunkRare);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ingots/rare_earth"))).add(chunkRare);

        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "materials/cryolite"))).add(chunkCryolite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "crystals/cryolite"))).add(chunkCryolite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "crystals/cryolite"))).add(chunkCryolite);

        tag(Objects.requireNonNull(HbmItemTags.material(HbmMaterials.SULFUR))).add(basaltSulfur, stoneSulfur);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/sulfur"))).add(basaltSulfur, stoneSulfur);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/sulfur"))).add(basaltSulfur, stoneSulfur);

        tag(Objects.requireNonNull(HbmItemTags.material(HbmMaterials.ASBESTOS))).add(basaltAsbestos, stoneAsbestos);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/asbestos"))).add(basaltAsbestos, stoneAsbestos);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/asbestos"))).add(basaltAsbestos, stoneAsbestos);

        tag(Objects.requireNonNull(HbmItemTags.material(HbmMaterials.FLUORITE))).add(basaltFluorite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/fluorite"))).add(basaltFluorite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/fluorite"))).add(basaltFluorite);

        tag(Objects.requireNonNull(HbmItemTags.material(HbmMaterials.MOLYSITE))).add(basaltMolysite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/molysite"))).add(basaltMolysite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/molysite"))).add(basaltMolysite);

        tag(Objects.requireNonNull(HbmItemTags.material(HbmMaterials.VOLCANIC))).add(basaltGem);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/volcanic"))).add(basaltGem);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/volcanic"))).add(basaltGem);

        tag(Objects.requireNonNull(HbmItemTags.material(HbmMaterials.LIMESTONE))).add(stoneLimestone);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/limestone"))).add(stoneLimestone);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/limestone"))).add(stoneLimestone);

        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "materials/malachite"))).add(chunkMalachite, stoneMalachite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ingots/malachite"))).add(chunkMalachite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ingots/malachite"))).add(chunkMalachite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/malachite"))).add(stoneMalachite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/malachite"))).add(stoneMalachite);

        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "materials/bauxite"))).add(stoneBauxite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "gems/bauxite"))).add(stoneBauxite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "gems/bauxite"))).add(stoneBauxite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/bauxite"))).add(stoneBauxite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/bauxite"))).add(stoneBauxite);

        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "materials/hematite"))).add(stoneHematite);
        tag(Objects.requireNonNull(HbmItemTags.named(HbmNtmMod.MOD_ID, "ores/hematite"))).add(stoneHematite);
        tag(Objects.requireNonNull(HbmItemTags.named("forge", "ores/hematite"))).add(stoneHematite);
    }

    @Override
    public String getName() {
        return "HBM Item Tags";
    }
}

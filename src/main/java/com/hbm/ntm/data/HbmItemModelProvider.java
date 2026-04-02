package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class HbmItemModelProvider extends ItemModelProvider {
    public HbmItemModelProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, HbmNtmMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture("burnt_bark", mcLoc("item/generated"), "layer0", modLoc("item/burnt_bark"));
        withExistingParent("fallout_layer", modLoc("block/fallout"));
        singleTexture("gem_rad", mcLoc("item/generated"), "layer0", modLoc("item/gem_rad"));
        singleTexture("iv_empty", mcLoc("item/generated"), "layer0", modLoc("item/iv_empty"));
        singleTexture("radaway", mcLoc("item/generated"), "layer0", modLoc("item/radaway"));
        singleTexture("radaway_strong", mcLoc("item/generated"), "layer0", modLoc("item/radaway_strong"));
        singleTexture("radaway_flush", mcLoc("item/generated"), "layer0", modLoc("item/radaway_flush"));
        singleTexture("radx", mcLoc("item/generated"), "layer0", modLoc("item/radx"));
        withExistingParent("sellafield_slaked", modLoc("block/sellafield_slaked"));

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            withExistingParent(type.blockId(), modLoc("block/" + type.blockId() + "_inventory"));
        }

        for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {
            for (final HbmMaterialShape shape : material.shapes()) {
                singleTexture(material.itemId(shape), mcLoc("item/generated"), "layer0", mcLoc(shape.defaultTexturePath()));
            }
        }

        for (final ChunkOreItemType type : ChunkOreItemType.values()) {
            singleTexture(type.itemId(), mcLoc("item/generated"), "layer0", modLoc(type.defaultTexturePath()));
        }
    }
}

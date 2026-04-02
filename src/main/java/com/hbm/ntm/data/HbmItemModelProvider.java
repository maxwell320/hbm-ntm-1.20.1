package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
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

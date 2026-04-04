package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.item.BriquetteItemType;
import com.hbm.ntm.common.item.CasingItemType;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.item.CokeItemType;
import com.hbm.ntm.common.item.StampItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class HbmItemModelProvider extends ItemModelProvider {
    public HbmItemModelProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, HbmNtmMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture("biomass", mcLoc("item/generated"), "layer0", modLoc("item/biomass"));
        singleTexture("biomass_compressed", mcLoc("item/generated"), "layer0", modLoc("item/biomass_compressed"));
        singleTexture("burnt_bark", mcLoc("item/generated"), "layer0", modLoc("item/burnt_bark"));
        singleTexture("coil_advanced_torus", mcLoc("item/generated"), "layer0", modLoc("item/coil_advanced_torus"));
        singleTexture("coil_copper_torus", mcLoc("item/generated"), "layer0", modLoc("item/coil_copper_torus"));
        singleTexture("coil_gold_torus", mcLoc("item/generated"), "layer0", modLoc("item/coil_gold_torus"));
        singleTexture("crt_display", mcLoc("item/generated"), "layer0", modLoc("item/crt_display"));
        singleTexture("motor", mcLoc("item/generated"), "layer0", modLoc("item/motor"));
        singleTexture("motor_desh", mcLoc("item/generated"), "layer0", modLoc("item/motor_desh"));
        singleTexture("photo_panel", mcLoc("item/generated"), "layer0", modLoc("item/photo_panel"));
        singleTexture("pin", mcLoc("item/generated"), "layer0", modLoc("item/pin"));
        singleTexture("dosimeter", mcLoc("item/generated"), "layer0", modLoc("item/dosimeter"));
        singleTexture("ducttape", mcLoc("item/generated"), "layer0", modLoc("item/ducttape"));
        singleTexture("fuse", mcLoc("item/generated"), "layer0", modLoc("item/fuse"));
        singleTexture("safety_fuse", mcLoc("item/generated"), "layer0", modLoc("item/safety_fuse"));
        singleTexture("tank_steel", mcLoc("item/generated"), "layer0", modLoc("item/tank_steel"));
        withExistingParent("fallout_layer", modLoc("block/fallout"));
        withExistingParent("geiger", modLoc("block/geiger"));
        singleTexture("geiger_counter", mcLoc("item/generated"), "layer0", modLoc("item/geiger_counter"));
        singleTexture("gem_rad", mcLoc("item/generated"), "layer0", modLoc("item/gem_rad"));
        singleTexture("iv_empty", mcLoc("item/generated"), "layer0", modLoc("item/iv_empty"));
        singleTexture("powder_sawdust", mcLoc("item/generated"), "layer0", modLoc("item/powder_sawdust"));
        singleTexture("radaway", mcLoc("item/generated"), "layer0", modLoc("item/radaway"));
        singleTexture("radaway_strong", mcLoc("item/generated"), "layer0", modLoc("item/radaway_strong"));
        singleTexture("radaway_flush", mcLoc("item/generated"), "layer0", modLoc("item/radaway_flush"));
        singleTexture("radx", mcLoc("item/generated"), "layer0", modLoc("item/radx"));
        singleTexture("upgrade_template", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_template"));
        singleTexture("sellafield", mcLoc("item/generated"), "layer0", modLoc("block/sellafield_slaked"));
        withExistingParent("sellafield_slaked", modLoc("block/sellafield_slaked"));

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            withExistingParent(type.blockId(), modLoc("block/" + type.blockId() + "_inventory"));
        }

        for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {
            for (final HbmMaterialShape shape : material.shapes()) {
                final String itemId = material.itemId(shape);
                singleTexture(itemId, mcLoc("item/generated"), "layer0", preferredMaterialTexture(itemId, shape));
            }
        }

        for (final BriquetteItemType type : BriquetteItemType.values()) {
            singleTexture(type.itemId(), mcLoc("item/generated"), "layer0", modLoc(type.defaultTexturePath()));
        }

        for (final CasingItemType type : CasingItemType.values()) {
            singleTexture(type.itemId(), mcLoc("item/generated"), "layer0", modLoc(type.defaultTexturePath()));
        }

        for (final CokeItemType type : CokeItemType.values()) {
            singleTexture(type.itemId(), mcLoc("item/generated"), "layer0", modLoc(type.defaultTexturePath()));
        }

        for (final ChunkOreItemType type : ChunkOreItemType.values()) {
            singleTexture(type.itemId(), mcLoc("item/generated"), "layer0", modLoc(type.defaultTexturePath()));
        }

        for (final CircuitItemType type : CircuitItemType.values()) {
            singleTexture(type.itemId(), mcLoc("item/generated"), "layer0", modLoc(type.defaultTexturePath()));
        }

        for (final StampItemType type : StampItemType.values()) {
            singleTexture(type.itemId(), mcLoc("item/generated"), "layer0", modLoc(type.defaultTexturePath()));
        }
    }

    private ResourceLocation preferredMaterialTexture(final String itemId, final HbmMaterialShape shape) {
        final ResourceLocation modTexture = modLoc("item/" + itemId);
        if (existingFileHelper.exists(modTexture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
            return modTexture;
        }
        if (shape.usesLegacySharedTexture()) {
            final ResourceLocation sharedTexture = modLoc(shape.legacySharedTexturePath());
            if (existingFileHelper.exists(sharedTexture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
                return sharedTexture;
            }
        }
        return mcLoc(shape.defaultTexturePath());
    }
}

package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.MaterialBlockType;
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
        singleTexture("battery_potato", mcLoc("item/generated"), "layer0", modLoc("item/battery_potato"));
        withExistingParent("machine_battery", modLoc("block/machine_battery"));
        singleTexture("biomass", mcLoc("item/generated"), "layer0", modLoc("item/biomass"));
        singleTexture("biomass_compressed", mcLoc("item/generated"), "layer0", modLoc("item/biomass_compressed"));
        singleTexture("burnt_bark", mcLoc("item/generated"), "layer0", modLoc("item/burnt_bark"));
        singleTexture("coil_advanced_torus", mcLoc("item/generated"), "layer0", modLoc("item/coil_advanced_torus"));
        singleTexture("coil_copper_torus", mcLoc("item/generated"), "layer0", modLoc("item/coil_copper_torus"));
        singleTexture("coil_gold_torus", mcLoc("item/generated"), "layer0", modLoc("item/coil_gold_torus"));
        singleTexture("crt_display", mcLoc("item/generated"), "layer0", modLoc("item/crt_display"));
        singleTexture("motor", mcLoc("item/generated"), "layer0", modLoc("item/motor"));
        singleTexture("motor_desh", mcLoc("item/generated"), "layer0", modLoc("item/motor_desh"));
        withExistingParent("machine_press", modLoc("block/machine_press"));
        singleTexture("photo_panel", mcLoc("item/generated"), "layer0", modLoc("item/photo_panel"));
        singleTexture("pin", mcLoc("item/generated"), "layer0", modLoc("item/pin"));
        singleTexture("catalyst_clay", mcLoc("item/generated"), "layer0", modLoc("item/catalyst_clay"));
        singleTexture("deuterium_filter", mcLoc("item/generated"), "layer0", modLoc("item/deuterium_filter"));
        singleTexture("template_folder", mcLoc("item/generated"), "layer0", modLoc("item/template_folder"));
        singleTexture("fins_flat", mcLoc("item/generated"), "layer0", modLoc("item/fins_flat"));
        singleTexture("sphere_steel", mcLoc("item/generated"), "layer0", modLoc("item/sphere_steel"));
        singleTexture("pedestal_steel", mcLoc("item/generated"), "layer0", modLoc("item/pedestal_steel"));
        withExistingParent("fluid_identifier_multi", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/fluid_identifier_multi"))
            .texture("layer1", modLoc("item/fluid_identifier_overlay"));
        singleTexture("fins_big_steel", mcLoc("item/generated"), "layer0", modLoc("item/fins_big_steel"));
        singleTexture("fins_small_steel", mcLoc("item/generated"), "layer0", modLoc("item/fins_small_steel"));
        singleTexture("fins_tri_steel", mcLoc("item/generated"), "layer0", modLoc("item/fins_tri_steel"));
        singleTexture("fins_quad_titanium", mcLoc("item/generated"), "layer0", modLoc("item/fins_quad_titanium"));
        singleTexture("blade_titanium", mcLoc("item/generated"), "layer0", modLoc("item/blade_titanium"));
        singleTexture("turbine_titanium", mcLoc("item/generated"), "layer0", modLoc("item/turbine_titanium"));
        singleTexture("flywheel_beryllium", mcLoc("item/generated"), "layer0", modLoc("item/flywheel_beryllium"));
        singleTexture("ring_starmetal", mcLoc("item/generated"), "layer0", modLoc("item/ring_starmetal"));
        singleTexture("sawblade", mcLoc("item/generated"), "layer0", modLoc("item/sawblade"));
        singleTexture("bottle_mercury", mcLoc("item/generated"), "layer0", modLoc("item/bottle_mercury"));
        singleTexture("nugget_mercury", mcLoc("item/generated"), "layer0", modLoc("item/nugget_mercury"));
        singleTexture("nugget_mercury_tiny", mcLoc("item/generated"), "layer0", modLoc("item/nugget_mercury_tiny"));
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
        singleTexture("page_of_", mcLoc("item/generated"), "layer0", modLoc("item/page_of_"));
        singleTexture("radaway", mcLoc("item/generated"), "layer0", modLoc("item/radaway"));
        singleTexture("radaway_strong", mcLoc("item/generated"), "layer0", modLoc("item/radaway_strong"));
        singleTexture("radaway_flush", mcLoc("item/generated"), "layer0", modLoc("item/radaway_flush"));
        singleTexture("radx", mcLoc("item/generated"), "layer0", modLoc("item/radx"));
        singleTexture("stamp_book", mcLoc("item/generated"), "layer0", modLoc("item/stamp_book"));
        singleTexture("undefined", mcLoc("item/generated"), "layer0", modLoc("item/undefined"));
        singleTexture("upgrade_muffler", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_muffler"));
        singleTexture("upgrade_speed_1", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_speed_1"));
        singleTexture("upgrade_speed_2", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_speed_2"));
        singleTexture("upgrade_speed_3", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_speed_3"));
        singleTexture("upgrade_effect_1", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_effect_1"));
        singleTexture("upgrade_effect_2", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_effect_2"));
        singleTexture("upgrade_effect_3", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_effect_3"));
        singleTexture("upgrade_power_1", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_power_1"));
        singleTexture("upgrade_power_2", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_power_2"));
        singleTexture("upgrade_power_3", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_power_3"));
        singleTexture("upgrade_fortune_1", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_fortune_1"));
        singleTexture("upgrade_fortune_2", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_fortune_2"));
        singleTexture("upgrade_fortune_3", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_fortune_3"));
        singleTexture("upgrade_afterburn_1", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_afterburn_1"));
        singleTexture("upgrade_afterburn_2", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_afterburn_2"));
        singleTexture("upgrade_afterburn_3", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_afterburn_3"));
        singleTexture("upgrade_overdrive_1", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_overdrive_1"));
        singleTexture("upgrade_overdrive_2", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_overdrive_2"));
        singleTexture("upgrade_overdrive_3", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_overdrive_3"));
        singleTexture("upgrade_radius", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_radius"));
        singleTexture("upgrade_health", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_health"));
        singleTexture("upgrade_smelter", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_smelter"));
        singleTexture("upgrade_shredder", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_shredder"));
        singleTexture("upgrade_centrifuge", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_centrifuge"));
        singleTexture("upgrade_crystallizer", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_crystallizer"));
        singleTexture("upgrade_nullifier", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_nullifier"));
        singleTexture("upgrade_screm", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_screm"));
        singleTexture("upgrade_gc_speed", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_gc_speed"));
        singleTexture("upgrade_5g", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_5g"));
        singleTexture("upgrade_template", mcLoc("item/generated"), "layer0", modLoc("item/upgrade_template"));
        singleTexture("sellafield", mcLoc("item/generated"), "layer0", modLoc("block/sellafield_slaked"));
        withExistingParent("sellafield_slaked", modLoc("block/sellafield_slaked"));

        for (final MaterialBlockType type : MaterialBlockType.values()) {
            withExistingParent(type.blockId(), modLoc("block/" + type.blockId()));
        }

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

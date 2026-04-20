package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.OverworldOreType;
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
        singleTexture("centrifuge_element", mcLoc("item/generated"), "layer0", modLoc("item/centrifuge_element"));
        singleTexture("ingot_cft", mcLoc("item/generated"), "layer0", modLoc("item/ingot_cft"));
        singleTexture("canister_empty", mcLoc("item/generated"), "layer0", modLoc("item/canister_empty"));
        withExistingParent("canister_full", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/canister_empty"))
            .texture("layer1", modLoc("item/canister_overlay"));
        singleTexture("canister_napalm", mcLoc("item/generated"), "layer0", modLoc("item/canister_napalm"));
        singleTexture("disperser_canister_empty", mcLoc("item/generated"), "layer0", modLoc("item/disperser_canister"));
        withExistingParent("disperser_canister", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/disperser_canister"))
            .texture("layer1", modLoc("item/disperser_canister_overlay"));
        singleTexture("glyphid_gland_empty", mcLoc("item/generated"), "layer0", modLoc("item/glyphid_gland"));
        withExistingParent("glyphid_gland", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/glyphid_gland"))
            .texture("layer1", modLoc("item/fluid_identifier_overlay"));
        singleTexture("gas_empty", mcLoc("item/generated"), "layer0", modLoc("item/gas_empty"));
        withExistingParent("gas_full", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/gas_empty"))
            .texture("layer1", modLoc("item/gas_bottle"))
            .texture("layer2", modLoc("item/gas_label"));
        singleTexture("fluid_tank_empty", mcLoc("item/generated"), "layer0", modLoc("item/fluid_tank"));
        withExistingParent("fluid_tank_full", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/fluid_tank"))
            .texture("layer1", modLoc("item/fluid_tank_overlay"));
        singleTexture("fluid_tank_lead_empty", mcLoc("item/generated"), "layer0", modLoc("item/fluid_tank_lead"));
        withExistingParent("fluid_tank_lead_full", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/fluid_tank_lead"))
            .texture("layer1", modLoc("item/fluid_tank_lead_overlay"));
        singleTexture("fluid_barrel_empty", mcLoc("item/generated"), "layer0", modLoc("item/fluid_barrel"));
        withExistingParent("fluid_barrel_full", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/fluid_barrel"))
            .texture("layer1", modLoc("item/fluid_barrel_overlay"));
        singleTexture("fluid_pack_empty", mcLoc("item/generated"), "layer0", modLoc("item/fluid_pack"));
        withExistingParent("fluid_pack_full", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/fluid_pack"))
            .texture("layer1", modLoc("item/fluid_pack_overlay"));
        singleTexture("crt_display", mcLoc("item/generated"), "layer0", modLoc("item/crt_display"));
        singleTexture("motor", mcLoc("item/generated"), "layer0", modLoc("item/motor"));
        singleTexture("motor_desh", mcLoc("item/generated"), "layer0", modLoc("item/motor_desh"));
        withExistingParent("machine_press", modLoc("block/machine_press"));
        withExistingParent("machine_assembly_machine", modLoc("block/machine_assembly_machine"));
        withExistingParent("machine_soldering_station", modLoc("block/machine_soldering_station"));
        withExistingParent("machine_difurnace", modLoc("block/machine_difurnace_off"));
        withExistingParent("machine_difurnace_extension", modLoc("block/machine_difurnace_extension"));
        withExistingParent("machine_difurnace_rtg", modLoc("block/machine_difurnace_rtg_off"));
        withExistingParent("machine_electric_furnace", modLoc("block/machine_electric_furnace_off"));
        withExistingParent("furnace_iron", modLoc("block/furnace_iron_off"));
        withExistingParent("furnace_steel", modLoc("block/furnace_steel_off"));
        withExistingParent("furnace_combination", modLoc("block/furnace_combination"));
        withExistingParent("machine_rotary_furnace", modLoc("block/machine_rotary_furnace"));
        withExistingParent("machine_rtg_furnace", modLoc("block/machine_rtg_furnace_off"));
        withExistingParent("machine_rtg_grey", modLoc("block/machine_rtg_grey"));
        withExistingParent("machine_diesel", modLoc("block/machine_diesel_off"));
        withExistingParent("machine_combustion", modLoc("block/machine_combustion_off"));
        withExistingParent("machine_furnace_brick", modLoc("block/machine_furnace_brick_off"));
        withExistingParent("machine_ashpit", modLoc("block/machine_ashpit"));
        withExistingParent("chimney_brick", modLoc("block/chimney_brick"));
        withExistingParent("chimney_industrial", modLoc("block/chimney_industrial"));
        withExistingParent("steel_beam", modLoc("block/steel_beam"));
        withExistingParent("steel_grate", modLoc("block/steel_grate"));
        withExistingParent("steel_grate_wide", modLoc("block/steel_grate_wide"));
        withExistingParent("machine_minirtg", modLoc("block/machine_minirtg"));
        withExistingParent("machine_powerrtg", modLoc("block/machine_powerrtg"));
        withExistingParent("machine_centrifuge", modLoc("block/machine_centrifuge_obj"));
        withExistingParent("machine_gascent", modLoc("block/machine_gascent"));
        withExistingParent("machine_cyclotron", modLoc("block/machine_cyclotron_obj"));
        withExistingParent("machine_purex", modLoc("block/machine_purex"));
        withExistingParent("machine_icf", modLoc("block/machine_icf_obj"));
        withExistingParent("machine_icf_controller", modLoc("block/machine_icf_controller"));
        withExistingParent("machine_icf_laser_component", modLoc("block/machine_icf_laser_component_casing"));
        withExistingParent("machine_icf_press", modLoc("block/machine_icf_press"));
        singleTexture("photo_panel", mcLoc("item/generated"), "layer0", modLoc("item/photo_panel"));
        singleTexture("pin", mcLoc("item/generated"), "layer0", modLoc("item/pin"));
        singleTexture("catalyst_clay", mcLoc("item/generated"), "layer0", modLoc("item/catalyst_clay"));
        singleTexture("deuterium_filter", mcLoc("item/generated"), "layer0", modLoc("item/deuterium_filter"));
        singleTexture("hazmat_cloth", mcLoc("item/generated"), "layer0", modLoc("item/hazmat_cloth"));
        singleTexture("asbestos_cloth", mcLoc("item/generated"), "layer0", modLoc("item/asbestos_cloth"));
        singleTexture("filter_coal", mcLoc("item/generated"), "layer0", modLoc("item/filter_coal"));
        singleTexture("reactor_core", mcLoc("item/generated"), "layer0", modLoc("item/reactor_core"));
        singleTexture("thermo_element", mcLoc("item/generated"), "layer0", modLoc("item/thermo_element"));
        singleTexture("rtg_unit", mcLoc("item/generated"), "layer0", modLoc("item/rtg_unit"));
        singleTexture("magnetron", mcLoc("item/generated"), "layer0", modLoc("item/magnetron"));
        singleTexture("drill_titanium", mcLoc("item/generated"), "layer0", modLoc("item/drill_titanium"));
        singleTexture("entanglement_kit", mcLoc("item/generated"), "layer0", modLoc("item/entanglement_kit"));
        singleTexture("drillbit_steel", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_steel"));
        singleTexture("drillbit_steel_diamond", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_steel_diamond"));
        singleTexture("drillbit_hss", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_hss"));
        singleTexture("drillbit_hss_diamond", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_hss_diamond"));
        singleTexture("drillbit_desh", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_desh"));
        singleTexture("drillbit_desh_diamond", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_desh_diamond"));
        singleTexture("drillbit_tcalloy", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_tcalloy"));
        singleTexture("drillbit_tcalloy_diamond", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_tcalloy_diamond"));
        singleTexture("drillbit_ferro", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_ferro"));
        singleTexture("drillbit_ferro_diamond", mcLoc("item/generated"), "layer0", modLoc("item/drillbit_ferro_diamond"));
        singleTexture("part_lithium", mcLoc("item/generated"), "layer0", modLoc("item/part_lithium"));
        singleTexture("part_beryllium", mcLoc("item/generated"), "layer0", modLoc("item/part_beryllium"));
        singleTexture("part_carbon", mcLoc("item/generated"), "layer0", modLoc("item/part_carbon"));
        singleTexture("part_copper", mcLoc("item/generated"), "layer0", modLoc("item/part_copper"));
        singleTexture("part_plutonium", mcLoc("item/generated"), "layer0", modLoc("item/part_plutonium"));
        singleTexture("blueprints", mcLoc("item/generated"), "layer0", modLoc("item/template_folder"));
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
        singleTexture("piston_set_steel", mcLoc("item/generated"), "layer0", modLoc("item/piston_set_steel"));
        singleTexture("piston_set_dura", mcLoc("item/generated"), "layer0", modLoc("item/piston_set_dura"));
        singleTexture("piston_set_desh", mcLoc("item/generated"), "layer0", modLoc("item/piston_set_desh"));
        singleTexture("piston_set_starmetal", mcLoc("item/generated"), "layer0", modLoc("item/piston_set_starmetal"));
        singleTexture("flywheel_beryllium", mcLoc("item/generated"), "layer0", modLoc("item/flywheel_beryllium"));
        singleTexture("ring_starmetal", mcLoc("item/generated"), "layer0", modLoc("item/ring_starmetal"));
        singleTexture("sawblade", mcLoc("item/generated"), "layer0", modLoc("item/sawblade"));
        singleTexture("meteorite_sword", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_seared", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_reforged", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_hardened", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_alloyed", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_machined", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_treated", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_etched", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_bred", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_irradiated", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_fused", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("meteorite_sword_baleful", mcLoc("item/generated"), "layer0", modLoc("item/meteorite_sword"));
        singleTexture("bottle_mercury", mcLoc("item/generated"), "layer0", modLoc("item/bottle_mercury"));
        singleTexture("nugget_mercury", mcLoc("item/generated"), "layer0", modLoc("item/nugget_mercury"));
        singleTexture("nugget_mercury_tiny", mcLoc("item/generated"), "layer0", modLoc("item/nugget_mercury_tiny"));
        singleTexture("nuclear_waste", mcLoc("item/generated"), "layer0", modLoc("item/nuclear_waste"));
        singleTexture("nuclear_waste_vitrified", mcLoc("item/generated"), "layer0", modLoc("item/nuclear_waste_vitrified"));
        singleTexture("nuclear_waste_tiny", mcLoc("item/generated"), "layer0", modLoc("item/nuclear_waste_tiny"));
        singleTexture("pellet_charged", mcLoc("item/generated"), "layer0", modLoc("item/pellets_charged"));
        singleTexture("particle_muon", mcLoc("item/generated"), "layer0", modLoc("item/particle_muon"));
        singleTexture("icf_pellet_empty", mcLoc("item/generated"), "layer0", modLoc("item/icf_pellet_empty"));
        withExistingParent("icf_pellet", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/icf_pellet_bg"))
            .texture("layer1", modLoc("item/icf_pellet_shape"));
        singleTexture("icf_pellet_depleted", mcLoc("item/generated"), "layer0", modLoc("item/icf_pellet_depleted"));
        singleTexture("pellet_rtg", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg"));
        singleTexture("pellet_rtg_radium", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_radium"));
        singleTexture("pellet_rtg_weak", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_weak"));
        singleTexture("pellet_rtg_strontium", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_strontium"));
        singleTexture("pellet_rtg_cobalt", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_cobalt"));
        singleTexture("pellet_rtg_actinium", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_actinium"));
        singleTexture("pellet_rtg_americium", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_americium"));
        singleTexture("pellet_rtg_polonium", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_polonium"));
        singleTexture("pellet_rtg_gold", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_gold"));
        singleTexture("pellet_rtg_lead", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_lead"));
        singleTexture("pellet_rtg_depleted_bismuth", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_depleted_bismuth"));
        singleTexture("pellet_rtg_depleted_lead", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_depleted_lead"));
        singleTexture("pellet_rtg_depleted_neptunium", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_depleted_neptunium"));
        singleTexture("pellet_rtg_depleted_mercury", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_depleted_mercury"));
        singleTexture("pellet_rtg_depleted_nickel", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_depleted_nickel"));
        singleTexture("pellet_rtg_depleted_zirconium", mcLoc("item/generated"), "layer0", modLoc("item/pellet_rtg_depleted_zirconium"));
        singleTexture("dosimeter", mcLoc("item/generated"), "layer0", modLoc("item/dosimeter"));
        singleTexture("ducttape", mcLoc("item/generated"), "layer0", modLoc("item/ducttape"));
        singleTexture("fuse", mcLoc("item/generated"), "layer0", modLoc("item/fuse"));
        singleTexture("safety_fuse", mcLoc("item/generated"), "layer0", modLoc("item/safety_fuse"));
        singleTexture("scrap", mcLoc("item/generated"), "layer0", modLoc("item/scrap"));
        singleTexture("scrap_nuclear", mcLoc("item/generated"), "layer0", modLoc("item/scrap_nuclear"));
        singleTexture("scrap_oil", mcLoc("item/generated"), "layer0", modLoc("item/scrap_oil"));
        singleTexture("scrap_plastic", mcLoc("item/generated"), "layer0", modLoc("item/scrap_plastic"));
        singleTexture("debris_concrete", mcLoc("item/generated"), "layer0", modLoc("item/debris_concrete"));
        singleTexture("debris_shrapnel", mcLoc("item/generated"), "layer0", modLoc("item/debris_shrapnel"));
        singleTexture("debris_exchanger", mcLoc("item/generated"), "layer0", modLoc("item/debris_exchanger"));
        singleTexture("debris_element", mcLoc("item/generated"), "layer0", modLoc("item/debris_element"));
        singleTexture("debris_metal", mcLoc("item/generated"), "layer0", modLoc("item/debris_metal"));
        singleTexture("debris_graphite", mcLoc("item/generated"), "layer0", modLoc("item/debris_graphite"));
        singleTexture("tank_steel", mcLoc("item/generated"), "layer0", modLoc("item/tank_steel"));
        singleTexture("blowtorch", mcLoc("item/generated"), "layer0", modLoc("item/blowtorch"));
        singleTexture("acetylene_torch", mcLoc("item/generated"), "layer0", modLoc("item/acetylene_torch"));
        withExistingParent("fallout_layer", modLoc("block/fallout"));
        withExistingParent("geiger", modLoc("block/geiger"));
        singleTexture("geiger_counter", mcLoc("item/generated"), "layer0", modLoc("item/geiger_counter"));
        singleTexture("gem_rad", mcLoc("item/generated"), "layer0", modLoc("item/gem_rad"));
        singleTexture("gem_alexandrite", mcLoc("item/generated"), "layer0", modLoc("item/gem_alexandrite"));
        singleTexture("iv_empty", mcLoc("item/generated"), "layer0", modLoc("item/iv_empty"));
        singleTexture("powder_sawdust", mcLoc("item/generated"), "layer0", modLoc("item/powder_sawdust"));
        singleTexture("powder_ash", mcLoc("item/generated"), "layer0", modLoc("item/powder_ash"));
        singleTexture("powder_ice", mcLoc("item/generated"), "layer0", modLoc("item/powder_ice"));
        singleTexture("powder_magic", mcLoc("item/generated"), "layer0", modLoc("item/powder_magic"));
        singleTexture("powder_poison", mcLoc("item/generated"), "layer0", modLoc("item/powder_poison"));
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
        singleTexture("blades_steel", mcLoc("item/generated"), "layer0", modLoc("item/blades_steel"));
        singleTexture("blades_titanium", mcLoc("item/generated"), "layer0", modLoc("item/blades_titanium"));
        singleTexture("blades_advanced_alloy", mcLoc("item/generated"), "layer0", modLoc("item/blades_advanced_alloy"));
        singleTexture("blades_desh", mcLoc("item/generated"), "layer0", modLoc("item/blades_desh"));
        singleTexture("sellafield", mcLoc("item/generated"), "layer0", modLoc("block/sellafield_slaked"));
        withExistingParent("sellafield_slaked", modLoc("block/sellafield_slaked"));

        for (final MaterialBlockType type : MaterialBlockType.values()) {
            withExistingParent(type.blockId(), modLoc("block/" + type.blockId()));
        }

        for (final OverworldOreType type : OverworldOreType.values()) {
            withExistingParent(type.blockId(), modLoc("block/" + type.blockId()));
        }

        for (final NetherOreType type : NetherOreType.values()) {
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

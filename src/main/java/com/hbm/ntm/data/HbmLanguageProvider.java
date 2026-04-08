package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.item.BriquetteItemType;
import com.hbm.ntm.common.item.CasingItemType;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.item.CokeItemType;
import com.hbm.ntm.common.item.PageItemType;
import com.hbm.ntm.common.item.PrintingStampType;
import com.hbm.ntm.common.item.StampItemType;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import com.hbm.ntm.common.registration.HbmMobEffects;
import java.util.Objects;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

@SuppressWarnings("null")
public class HbmLanguageProvider extends LanguageProvider {
    public HbmLanguageProvider(final PackOutput output) {
        super(output, HbmNtmMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + HbmNtmMod.MOD_ID + ".main", "HBM Nuclear Tech");
        add("fluid.hbmntm.coolant", "Coolant");
        add("fluid.hbmntm.coolant_hot", "Hot Coolant");
        add("fluid.hbmntm.cryogel", "Cryogel");
        add("fluid.hbmntm.carbondioxide", "Carbon Dioxide");
        add("fluid.hbmntm.deuterium", "Deuterium");
        add("death.attack.radiation", "%1$s died from radiation poisoning");
        add("fluid.hbmntm.heavywater", "Heavy Water");
        add("fluid.hbmntm.heavywater_hot", "Hot Heavy Water");
        add("fluid.hbmntm.helium3", "Helium-3");
        add("fluid.hbmntm.hydrogen", "Liquid Hydrogen");
        add("fluid.hbmntm.helium4", "Helium-4");
        add("fluid.hbmntm.nitric_acid", "Nitric Acid");
        add("fluid.hbmntm.oil", "Crude Oil");
        add("fluid.hbmntm.oxygen", "Liquid Oxygen");
        add("fluid.hbmntm.perfluoromethyl", "Perfluoromethyl");
        add("fluid.hbmntm.perfluoromethyl_cold", "Cold Perfluoromethyl");
        add("fluid.hbmntm.perfluoromethyl_hot", "Hot Perfluoromethyl");
        add("fluid.hbmntm.peroxide", "Hydrogen Peroxide");
        add("fluid.hbmntm.lye", "Lye");
        add("fluid.hbmntm.sodium_aluminate", "Sodium Aluminate");
        add("fluid.hbmntm.sodium", "Liquid Sodium");
        add("fluid.hbmntm.sodium_hot", "Hot Liquid Sodium");
        add("fluid.hbmntm.bauxite_solution", "Bauxite Solution");
        add("fluid.hbmntm.alumina", "Aluminam");
        add("fluid.hbmntm.chlorine", "Chlorine Gas");
        add("fluid.hbmntm.redmud", "Red Mud");
        add("fluid.hbmntm.schrabidic", "Schrabidic Acid");
        add("fluid.hbmntm.vitriol", "Vitriol");
        add("fluid.hbmntm.slop", "Ore Slop");
        add("fluid.hbmntm.lead", "Liquid Lead");
        add("fluid.hbmntm.lead_hot", "Hot Liquid Lead");
        add("fluid.hbmntm.fullerene", "Fullerene Solution");
        add("fluid.hbmntm.pheromone", "Booster Pheromone");
        add("fluid.hbmntm.pheromone_m", "Modified Booster Pheromone");
        add("fluid.hbmntm.oil_ds", "Desulfurized Crude Oil");
        add("fluid.hbmntm.hotoil_ds", "Desulfurized Hot Crude Oil");
        add("fluid.hbmntm.crackoil_ds", "Desulfurized Cracked Oil");
        add("fluid.hbmntm.hotcrackoil_ds", "Desulfurized Hot Cracked Oil");
        add("fluid.hbmntm.naphtha_ds", "Desulfurized Naphtha");
        add("fluid.hbmntm.lightoil_ds", "Desulfurized Light Oil");
        add("fluid.hbmntm.syngas", "Syngas");
        add("fluid.hbmntm.oxyhydrogen", "Oxyhydrogen");
        add("fluid.hbmntm.radiosolvent", "High-Performance Solvent");
        add("fluid.hbmntm.sourgas", "Sour Gas");
        add("fluid.hbmntm.reformgas", "Reformate Gas");
        add("fluid.hbmntm.phosgene", "Phosgene");
        add("fluid.hbmntm.mustardgas", "Mustard Gas");
        add("fluid.hbmntm.iongel", "Ionic Gel");
        add("fluid.hbmntm.heavyoil_vacuum", "Vacuum Heavy Oil");
        add("fluid.hbmntm.reformate", "Reformate");
        add("fluid.hbmntm.lightoil_vacuum", "Vacuum Light Oil");
        add("fluid.hbmntm.xylene", "BTX");
        add("fluid.hbmntm.heatingoil_vacuum", "Heavy Heating Oil");
        add("fluid.hbmntm.diesel_reform", "High-Cetane Diesel");
        add("fluid.hbmntm.diesel_crack_reform", "High-Cetane Cracked Diesel");
        add("fluid.hbmntm.kerosene_reform", "Jet Fuel");
        add("fluid.hbmntm.colloid", "Colloid");
        add("fluid.hbmntm.oil_coker", "Coker Oil");
        add("fluid.hbmntm.naphtha_coker", "Coker Naphtha");
        add("fluid.hbmntm.gas_coker", "Coker Gas");
        add("fluid.hbmntm.egg", "Dissolved Egg");
        add("fluid.hbmntm.cholesterol", "Cholesterol Solution");
        add("fluid.hbmntm.estradiol", "Estradiol Solution");
        add("fluid.hbmntm.fishoil", "Fish Oil");
        add("fluid.hbmntm.sunfloweroil", "Sunflower Seed Oil");
        add("fluid.hbmntm.nitroglycerin", "Nitroglycerin");
        add("fluid.hbmntm.chlorocalcite_solution", "Chlorocalcite Solution");
        add("fluid.hbmntm.chlorocalcite_mix", "Mixed Chlorocalcite Solution");
        add("fluid.hbmntm.chlorocalcite_cleaned", "Cleaned Chlorocalcite Solution");
        add("fluid.hbmntm.potassium_chloride", "Potassiumchloride Solution");
        add("fluid.hbmntm.calcium_chloride", "Calciumchloride Solution");
        add("fluid.hbmntm.calcium_solution", "Calcium Solution");
        add("fluid.hbmntm.aromatics", "Aromatic Hydrocarbons");
        add("fluid.hbmntm.unsaturateds", "Unsaturated Hydrocarbons");
        add("fluid.hbmntm.air", "Compressed Air");
        add("fluid.hbmntm.salient", "Salient Green");
        add("fluid.hbmntm.xpjuice", "Experience Juice");
        add("fluid.hbmntm.enderjuice", "Ender Juice");
        add("fluid.hbmntm.petroil_leaded", "Leaded Petroil");
        add("fluid.hbmntm.gasoline_leaded", "Leaded Gasoline");
        add("fluid.hbmntm.coalgas_leaded", "Leaded Coal Gasoline");
        add("fluid.hbmntm.woodoil", "Wood Oil");
        add("fluid.hbmntm.coalcreosote", "Coal Tar Creosote");
        add("fluid.hbmntm.seedslurry", "Seeding Slurry");
        add("fluid.hbmntm.blood", "Blood");
        add("fluid.hbmntm.blood_hot", "Hot Blood");
        add("fluid.hbmntm.mug", "Mug Root Beer");
        add("fluid.hbmntm.mug_hot", "Hot Mug Root Beer");
        add("fluid.hbmntm.coaloil", "Coal Oil");
        add("fluid.hbmntm.hotcrackoil", "Hot Cracked Oil");
        add("fluid.hbmntm.naphtha_crack", "Cracked Naphtha");
        add("fluid.hbmntm.lightoil_crack", "Cracked Light Oil");
        add("fluid.hbmntm.diesel_crack", "Cracked Diesel");
        add("fluid.hbmntm.heavyoil", "Heavy Oil");
        add("fluid.hbmntm.heatingoil", "Heating Oil");
        add("fluid.hbmntm.petroil", "Petroil");
        add("fluid.hbmntm.naphtha", "Naphtha");
        add("fluid.hbmntm.diesel", "Diesel");
        add("fluid.hbmntm.lightoil", "Light Oil");
        add("fluid.hbmntm.kerosene", "Kerosene");
        add("fluid.hbmntm.gasoline", "Gasoline");
        add("fluid.hbmntm.reclaimed", "Reclaimed Industrial Oil");
        add("fluid.hbmntm.lubricant", "Engine Lubricant");
        add("fluid.hbmntm.gas", "Natural Gas");
        add("fluid.hbmntm.petroleum", "Petroleum Gas");
        add("fluid.hbmntm.lpg", "LPG");
        add("fluid.hbmntm.biogas", "Biogas");
        add("fluid.hbmntm.biofuel", "Biofuel");
        add("fluid.hbmntm.ethanol", "Ethanol");
        add("fluid.hbmntm.coalgas", "Coal Gasoline");
        add("fluid.hbmntm.hotoil", "Hot Crude Oil");
        add("fluid.hbmntm.crackoil", "Cracked Oil");
        add("fluid.hbmntm.nitan", "NITAN© 100 Octane Super Fuel");
        add("fluid.hbmntm.fracksol", "Fracking Solution");
        add("fluid.hbmntm.pain", "Pandemonium(III)tantalite Solution");
        add("fluid.hbmntm.death", "Osmiridic Solution");
        add("fluid.hbmntm.watz", "Poisonous Mud");
        add("fluid.hbmntm.dhc", "Deuterated Hydrocarbon");
        add("fluid.hbmntm.uf6", "Uranium Hexafluoride");
        add("fluid.hbmntm.puf6", "Plutonium Hexafluoride");
        add("fluid.hbmntm.sas3", "Schrabidium Trisulfide");
        add("fluid.hbmntm.steam", "Steam");
        add("fluid.hbmntm.hotsteam", "Dense Steam");
        add("fluid.hbmntm.superhotsteam", "Super Dense Steam");
        add("fluid.hbmntm.ultrahotsteam", "Ultra Dense Steam");
        add("fluid.hbmntm.spentsteam", "Low-Pressure Steam");
        add("fluid.hbmntm.bitumen", "Bitumen");
        add("fluid.hbmntm.smear", "Industrial Oil");
        add("fluid.hbmntm.amat", "Antimatter");
        add("fluid.hbmntm.aschrab", "Antischrabidium");
        add("fluid.hbmntm.plasma_dt", "Deuterium-Tritium Plasma");
        add("fluid.hbmntm.plasma_hd", "Hydrogen-Deuterium Plasma");
        add("fluid.hbmntm.plasma_ht", "Hydrogen-Tritium Plasma");
        add("fluid.hbmntm.plasma_xm", "Helium-4-Oxygen Plasma");
        add("fluid.hbmntm.plasma_bf", "Balefire Plasma");
        add("fluid.hbmntm.plasma_dh3", "Deuterium-Helium-3 Plasma");
        add("fluid.hbmntm.wastefluid", "Liquid Nuclear Waste");
        add("fluid.hbmntm.wastegas", "Gaseous Nuclear Waste");
        add("fluid.hbmntm.smoke", "Smoke");
        add("fluid.hbmntm.smoke_leaded", "Leaded Smoke");
        add("fluid.hbmntm.smoke_poison", "Poison Smoke");
        add("fluid.hbmntm.balefire", "BF Rocket Fuel");
        add("fluid.hbmntm.stellar_flux", "Stellar Flux");
        add("fluid.hbmntm.concrete", "Liquid Concrete");
        add("fluid.hbmntm.solvent", "Solvent");
        add("fluid.hbmntm.sulfuric_acid", "Sulfuric Acid");
        add("fluid.hbmntm.thorium_salt", "Liquid Thorium Salt");
        add("fluid.hbmntm.thorium_salt_depleted", "Depleted Liquid Thorium Salt");
        add("fluid.hbmntm.thorium_salt_hot", "Hot Liquid Thorium Salt");
        add("fluid.hbmntm.tritium", "Tritium");
        add("fluid.hbmntm.xenon", "Xenon Gas");
        add(HbmMobEffects.RADIATION.get(), "Radiation");
        add(HbmMobEffects.RADAWAY.get(), "RadAway");
        add(HbmMobEffects.RAD_X.get(), "Rad-X");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_IRON.get()), "Iron Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_LEAD.get()), "Lead Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_STEEL.get()), "Steel Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_DESH.get()), "Desh Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_FERRORANIUM.get()), "Ferrouranium Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_SATURNITE.get()), "Saturnite Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_BISMUTH_BRONZE.get()), "Bismuth Bronze Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_ARSENIC_BRONZE.get()), "Arsenic Bronze Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_SCHRABIDATE.get()), "Ferric Schrabidate Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_DNT.get()), "Dineutronium Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_OSMIRIDIUM.get()), "Osmiridium Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_MURKY.get()), "Murky Anvil");
        add(Objects.requireNonNull(HbmBlocks.CREATIVE_ENERGY_SOURCE.get()), "Creative Energy Source");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_BATTERY.get()), "Energy Storage Block (LEGACY)");
        add(Objects.requireNonNull(HbmBlocks.FALLOUT.get()), "Fallout");
        add(Objects.requireNonNull(HbmBlocks.GAS_ASBESTOS.get()), "Airborne Asbestos Particles");
        add(Objects.requireNonNull(HbmBlocks.GEIGER.get()), "Geiger Counter");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_PRESS.get()), "Burner Press");
        add(Objects.requireNonNull(HbmBlocks.PRESS_PREHEATER.get()), "Burner Press Preheater");
        add(Objects.requireNonNull(HbmBlocks.BARREL_PLASTIC.get()), "Safe Barrel™");
        add(Objects.requireNonNull(HbmBlocks.BARREL_CORRODED.get()), "Corroded Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_IRON.get()), "Iron Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_STEEL.get()), "Steel Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_TCALLOY.get()), "Technetium Steel Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_ANTIMATTER.get()), "Magnetic Antimatter Container");
        add(Objects.requireNonNull(HbmBlocks.RED_CABLE.get()), "Red Copper Cable");
        add(Objects.requireNonNull(HbmBlocks.RED_CABLE_CLASSIC.get()), "Red Copper Cable (Classic)");
        add(Objects.requireNonNull(HbmBlocks.FLUID_DUCT_NEO.get()), "Universal Fluid Duct");
        add(Objects.requireNonNull(HbmBlocks.SELLAFIELD.get()), "Sellafite");
        add(Objects.requireNonNull(HbmItems.BATTERY_POTATO.get()), "Potato Battery");
        add(Objects.requireNonNull(HbmItems.BIOMASS.get()), "Biomass");
        add(Objects.requireNonNull(HbmItems.BIOMASS_COMPRESSED.get()), "Compressed Biomass");
        add(Objects.requireNonNull(HbmItems.COIL_ADVANCED_TORUS.get()), "Super Conducting Ring Coil");
        add(Objects.requireNonNull(HbmItems.COIL_COPPER_TORUS.get()), "Ring Coil");
        add(Objects.requireNonNull(HbmItems.COIL_GOLD_TORUS.get()), "Golden Ring Coil");
        add(Objects.requireNonNull(HbmItems.CRT_DISPLAY.get()), "Cathode Ray Tube");
        add(Objects.requireNonNull(HbmItems.FLUID_IDENTIFIER_MULTI.get()), "Multi Fluid Identifier");
        add("item.hbmntm.fluid_identifier_multi.info", "Universal fluid identifier for:");
        add("item.hbmntm.fluid_identifier_multi.info2", "Secondary type:");
        add("item.hbmntm.fluid_identifier.usage0", "Right click fluid ducts to set their fluid type.");
        add("item.hbmntm.fluid_identifier.usage1", "Shift right click fluid ducts to set adjacent ducts");
        add(Objects.requireNonNull(HbmItems.MOTOR.get()), "Motor");
        add(Objects.requireNonNull(HbmItems.MOTOR_DESH.get()), "Desh Motor");
        add(Objects.requireNonNull(HbmItems.DOSIMETER.get()), "Dosimeter");
        add(Objects.requireNonNull(HbmItems.TANK_STEEL.get()), "Steel Tank");
        add(Objects.requireNonNull(HbmItems.DUCTTAPE.get()), "Duct Tape");
        add(Objects.requireNonNull(HbmItems.FUSE.get()), "Fuse");
        add(Objects.requireNonNull(HbmItems.SAFETY_FUSE.get()), "Safety Fuse");
        add(Objects.requireNonNull(HbmItems.FALLOUT.get()), "Pile of Fallout");
        add(Objects.requireNonNull(HbmItems.GEIGER_COUNTER.get()), "Geiger Counter");
        add(Objects.requireNonNull(HbmItems.IV_EMPTY.get()), "Empty IV Bag");
        add(Objects.requireNonNull(HbmItems.POWDER_SAWDUST.get()), "Sawdust");
        add(Objects.requireNonNull(HbmItems.PHOTO_PANEL.get()), "Photovoltaic Panel");
        add(Objects.requireNonNull(HbmItems.PIN.get()), "Bobby Pin");
        add(Objects.requireNonNull(HbmItems.CATALYST_CLAY.get()), "Clay Catalyst");
        add(Objects.requireNonNull(HbmItems.DEUTERIUM_FILTER.get()), "Deuterium Filter");
        add(Objects.requireNonNull(HbmItems.TEMPLATE_FOLDER.get()), "Machine Template Folder");
        add(Objects.requireNonNull(HbmItems.FINS_FLAT.get()), "Flat Steel Casing");
        add(Objects.requireNonNull(HbmItems.SPHERE_STEEL.get()), "Steel Sphere");
        add(Objects.requireNonNull(HbmItems.PEDESTAL_STEEL.get()), "Steel Pedestal");
        add(Objects.requireNonNull(HbmItems.FINS_BIG_STEEL.get()), "Big Steel Grid Fins");
        add(Objects.requireNonNull(HbmItems.FINS_SMALL_STEEL.get()), "Small Steel Grid Fins");
        add(Objects.requireNonNull(HbmItems.FINS_TRI_STEEL.get()), "Large Steel Fins");
        add(Objects.requireNonNull(HbmItems.FINS_QUAD_TITANIUM.get()), "Small Titanium Fins");
        add(Objects.requireNonNull(HbmItems.BLADE_TITANIUM.get()), "Titanium Blade");
        add(Objects.requireNonNull(HbmItems.TURBINE_TITANIUM.get()), "Titanium Steam Turbine");
        add(Objects.requireNonNull(HbmItems.FLYWHEEL_BERYLLIUM.get()), "Beryllium Flywheel");
        add(Objects.requireNonNull(HbmItems.RING_STARMETAL.get()), "§9Starmetal Ring§r");
        add(Objects.requireNonNull(HbmItems.SAWBLADE.get()), "Sawblade");
        add(Objects.requireNonNull(HbmItems.BOTTLE_MERCURY.get()), "Bottle of Mercury");
        add(Objects.requireNonNull(HbmItems.NUGGET_MERCURY.get()), "Drop of Mercury");
        add(Objects.requireNonNull(HbmItems.NUGGET_MERCURY_TINY.get()), "Tiny Drop of Mercury");
        add("item.hbmntm.pin.desc", "Standard success rate of picking a regular lock is ~10%%.");
        add(Objects.requireNonNull(HbmItems.RADAWAY.get()), "RadAway");
        add(Objects.requireNonNull(HbmItems.RADAWAY_STRONG.get()), "Strong RadAway");
        add(Objects.requireNonNull(HbmItems.RADAWAY_FLUSH.get()), "Elite RadAway");
        add(Objects.requireNonNull(HbmItems.RADX.get()), "Rad-X");
        add(Objects.requireNonNull(HbmItems.UNDEFINED.get()), "Undefined");
        add(Objects.requireNonNull(HbmItems.UPGRADE_MUFFLER.get()), "Muffler");
        add(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_1.get()), "Speed Upgrade Mk.I");
        add(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_2.get()), "Speed Upgrade Mk.II");
        add(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_3.get()), "Speed Upgrade Mk.III");
        add(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_1.get()), "Effectiveness Upgrade Mk.I");
        add(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_2.get()), "Effectiveness Upgrade Mk.II");
        add(Objects.requireNonNull(HbmItems.UPGRADE_EFFECT_3.get()), "Effectiveness Upgrade Mk.III");
        add(Objects.requireNonNull(HbmItems.UPGRADE_POWER_1.get()), "Power Saving Upgrade Mk.I");
        add(Objects.requireNonNull(HbmItems.UPGRADE_POWER_2.get()), "Power Saving Upgrade Mk.II");
        add(Objects.requireNonNull(HbmItems.UPGRADE_POWER_3.get()), "Power Saving Upgrade Mk.III");
        add(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_1.get()), "Fortune Upgrade Mk.I");
        add(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_2.get()), "Fortune Upgrade Mk.II");
        add(Objects.requireNonNull(HbmItems.UPGRADE_FORTUNE_3.get()), "Fortune Upgrade Mk.III");
        add(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_1.get()), "Afterburner Upgrade Mk.I");
        add(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_2.get()), "Afterburner Upgrade Mk.II");
        add(Objects.requireNonNull(HbmItems.UPGRADE_AFTERBURN_3.get()), "Afterburner Upgrade Mk.III");
        add(Objects.requireNonNull(HbmItems.UPGRADE_OVERDRIVE_1.get()), "Overdrive Upgrade Mk.I");
        add(Objects.requireNonNull(HbmItems.UPGRADE_OVERDRIVE_2.get()), "Overdrive Upgrade Mk.II");
        add(Objects.requireNonNull(HbmItems.UPGRADE_OVERDRIVE_3.get()), "Overdrive Upgrade Mk.III");
        add(Objects.requireNonNull(HbmItems.UPGRADE_RADIUS.get()), "Emitter Radius Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_HEALTH.get()), "Emitter Health Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_SMELTER.get()), "Smelter Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_SHREDDER.get()), "Shredder Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_CENTRIFUGE.get()), "Centrifuge Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_CRYSTALLIZER.get()), "Crystallizer Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_NULLIFIER.get()), "Scrap Destroyer Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_SCREM.get()), "Screaming Scientist Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_GC_SPEED.get()), "Gas Centrifuge Overclocking Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_5G.get()), "5G Radiation Emitter Upgrade");
        add(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), "Machine Upgrade Template");
        add("geiger.chunkRad", "Current chunk radiation:");
        add("geiger.envRad", "Total environmental radiation:");
        add("geiger.playerRad", "Player contamination:");
        add("geiger.playerRes", "Player resistance:");
        add("geiger.title", "GEIGER COUNTER");
        add("geiger.title.dosimeter", "DOSIMETER");
        add("item.hbmntm.radaway.desc", "Removes 140 RAD");
        add("item.hbmntm.radaway_strong.desc", "Removes 350 RAD");
        add("item.hbmntm.radaway_flush.desc", "Removes 500 RAD");
        add("item.hbmntm.radx.desc", "Increases radiation resistance by 0.2 (37%) for 3 minutes");
        add("item.hbmntm.upgrade_radius.desc1", "Forcefield Range Upgrade");
        add("item.hbmntm.upgrade_radius.desc2", "Radius +16 / Consumption +500");
        add("item.hbmntm.upgrade_radius.desc3", "Stacks to 16");
        add("item.hbmntm.upgrade_health.desc1", "Forcefield Health Upgrade");
        add("item.hbmntm.upgrade_health.desc2", "Max. Health +50 / Consumption +250");
        add("item.hbmntm.upgrade_health.desc3", "Stacks to 16");
        add("item.hbmntm.upgrade_smelter.desc1", "Mining Laser Upgrade");
        add("item.hbmntm.upgrade_smelter.desc2", "Smelts blocks. Easy enough.");
        add("item.hbmntm.upgrade_smelter.desc3", "");
        add("item.hbmntm.upgrade_shredder.desc1", "Mining Laser Upgrade");
        add("item.hbmntm.upgrade_shredder.desc2", "Crunches ores");
        add("item.hbmntm.upgrade_shredder.desc3", "");
        add("item.hbmntm.upgrade_centrifuge.desc1", "Mining Laser Upgrade");
        add("item.hbmntm.upgrade_centrifuge.desc2", "Hopefully self-explanatory");
        add("item.hbmntm.upgrade_centrifuge.desc3", "");
        add("item.hbmntm.upgrade_crystallizer.desc1", "Mining Laser Upgrade");
        add("item.hbmntm.upgrade_crystallizer.desc2", "Your new best friend");
        add("item.hbmntm.upgrade_crystallizer.desc3", "");
        add("item.hbmntm.upgrade_nullifier.desc1", "Mining Laser Upgrade");
        add("item.hbmntm.upgrade_nullifier.desc2", "50% chance to override worthless items with /dev/zero");
        add("item.hbmntm.upgrade_nullifier.desc3", "50% chance to move worthless items to /dev/null");
        add("item.hbmntm.upgrade_screm.desc1", "Mining Laser Upgrade");
        add("item.hbmntm.upgrade_screm.desc2", "It's like in Super Mario where all blocks are");
        add("item.hbmntm.upgrade_screm.desc3", "actually Toads, but here it's Half-Life scientists");
        add("item.hbmntm.upgrade_screm.desc4", "and they scream. A lot.");
        add("item.hbmntm.upgrade_gc_speed.desc1", "Gas Centrifuge Upgrade");
        add("item.hbmntm.upgrade_gc_speed.desc2", "Allows for total isotopic separation of HEUF6");
        add("item.hbmntm.upgrade_gc_speed.desc3", "also your centrifuge goes sicko mode");
        add("item.hbmntm.template_folder.desc1", "Machine Templates: Paper + Dye");
        add("item.hbmntm.template_folder.desc2", "Press Stamps: Flat Stamp");
        add("item.hbmntm.template_folder.desc3", "Siren Tracks: Insulator + Steel Plate");
        add(HbmFluids.RAD_LAVA.getBlock(), "Volcanic Lava");
        add(HbmFluids.VOLCANIC_LAVA.getBlock(), "Volcanic Lava");
        add(Objects.requireNonNull(HbmBlocks.SELLAFIELD_SLAKED.get()), "Slaked Sellafite");
        add("block." + HbmNtmMod.MOD_ID + ".sellafield.0", "Sellafite");
        add("block." + HbmNtmMod.MOD_ID + ".sellafield.1", "Hot Sellafite");
        add("block." + HbmNtmMod.MOD_ID + ".sellafield.2", "Boiling Sellafite");
        add("block." + HbmNtmMod.MOD_ID + ".sellafield.3", "Blazing Sellafite");
        add("block." + HbmNtmMod.MOD_ID + ".sellafield.4", "Infernal Sellafite");
        add("block." + HbmNtmMod.MOD_ID + ".sellafield.5", "Sellafite-Corium");
        add(Objects.requireNonNull(HbmBlocks.WASTE_LOG.get()), "Charred Log");
        add(Objects.requireNonNull(HbmBlocks.WASTE_PLANKS.get()), "Charred Wooden Planks");
        add(Objects.requireNonNull(HbmItems.BURNT_BARK.get()), "Burnt Bark");
        add(Objects.requireNonNull(HbmItems.GEM_RAD.get()), "Radioactive Gem");

        for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {
            for (final HbmMaterialShape shape : material.shapes()) {
                add(Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get()), Objects.requireNonNull(material.itemTranslation(shape)));
            }
        }

        for (final BriquetteItemType type : BriquetteItemType.values()) {
            add(Objects.requireNonNull(HbmItems.getBriquette(type).get()), type.displayName());
        }

        for (final CasingItemType type : CasingItemType.values()) {
            add(Objects.requireNonNull(HbmItems.getCasing(type).get()), type.displayName());
        }

        for (final CokeItemType type : CokeItemType.values()) {
            add(Objects.requireNonNull(HbmItems.getCoke(type).get()), type.displayName());
        }

        for (final ChunkOreItemType type : ChunkOreItemType.values()) {
            add(Objects.requireNonNull(HbmItems.getChunkOre(type).get()), type.displayName());
        }

        for (final CircuitItemType type : CircuitItemType.values()) {
            add(Objects.requireNonNull(HbmItems.getCircuit(type).get()), type.displayName());
        }

        for (final StampItemType type : StampItemType.values()) {
            add(Objects.requireNonNull(HbmItems.getStamp(type).get()), type.displayName());
        }

        for (final PrintingStampType type : PrintingStampType.values()) {
            add("item.hbmntm.stamp_book." + type.translationSuffix(), type.displayName());
        }

        for (final PageItemType type : PageItemType.values()) {
            add("item.hbmntm.page_of_." + type.translationSuffix(), type.displayName());
        }

        for (final BasaltBlockType type : BasaltBlockType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getBasaltBlock(type).get()), type.displayName());
        }

        for (final MaterialBlockType type : MaterialBlockType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getMaterialBlock(type).get()), type.displayName());
        }

        for (final StoneResourceType type : StoneResourceType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getStoneResource(type).get()), type.displayName());
        }

        for (final BasaltOreType type : BasaltOreType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getBasaltOre(type).get()), type.displayName());
        }

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getSellafieldOre(type).get()), type.displayName());
        }
    }
}

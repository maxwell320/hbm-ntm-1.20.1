package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.MaterialBlockType;
import com.hbm.ntm.common.block.NetherOreType;
import com.hbm.ntm.common.block.OverworldOreType;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("null")
public class HbmLanguageProvider extends LanguageProvider {
    private final Set<String> trackedKeys = new HashSet<>();

    public HbmLanguageProvider(final PackOutput output) {
        super(output, HbmNtmMod.MOD_ID, "en_us");
    }

    @Override
    public void add(final String key, final String value) {
        super.add(key, value);
        trackedKeys.add(key);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + HbmNtmMod.MOD_ID + ".main", "HBM Nuclear Tech");
        add("container.ashpit", "Ashpit");
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
        add("hbmfluid.none", "None");
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
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ASSEMBLY_MACHINE.get()), "Assembly Machine");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_SOLDERING_STATION.get()), "Soldering Station");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_DI_FURNACE.get()), "Dual Furnace");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_DI_FURNACE_EXTENSION.get()), "Dual Furnace Extension");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_DI_FURNACE_RTG.get()), "RTG Dual Furnace");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ELECTRIC_FURNACE.get()), "Electric Furnace");
        add(Objects.requireNonNull(HbmBlocks.FURNACE_IRON.get()), "Iron Furnace");
        add(Objects.requireNonNull(HbmBlocks.FURNACE_STEEL.get()), "Steel Furnace");
        add(Objects.requireNonNull(HbmBlocks.FURNACE_COMBINATION.get()), "Combination Oven");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ROTARY_FURNACE.get()), "Rotary Furnace");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_RTG_FURNACE.get()), "RTG Furnace");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_RTG_GREY.get()), "RT Generator");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_DIESEL_GENERATOR.get()), "Diesel Generator");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_COMBUSTION_ENGINE.get()), "Industrial Combustion Engine");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_FURNACE_BRICK.get()), "Bricked Furnace");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ASHPIT.get()), "Ashpit");
        add(Objects.requireNonNull(HbmBlocks.CHIMNEY_BRICK.get()), "Brick Chimney");
        add(Objects.requireNonNull(HbmBlocks.CHIMNEY_INDUSTRIAL.get()), "Industrial Chimney");
        add(Objects.requireNonNull(HbmBlocks.STEEL_BEAM.get()), "Steel Beam");
        add(Objects.requireNonNull(HbmBlocks.STEEL_GRATE.get()), "Steel Grate");
        add(Objects.requireNonNull(HbmBlocks.STEEL_GRATE_WIDE.get()), "Item Grate");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_MINI_RTG.get()), "Radio Isotope Cell");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_POWER_RTG.get()), "PT Isotope Cell");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_CENTRIFUGE.get()), "Centrifuge");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_GAS_CENTRIFUGE.get()), "Gas Centrifuge");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_CYCLOTRON.get()), "Cyclotron");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_PUREX.get()), "PUREX");
        add("desc.gui.gasCent.enrichment", "\u00A72Enrichment\u00A7r$Uranium enrichment requires cascades.$Two-centrifuge cascades will give$uranium fuel, four-centrifuge cascades$will give total separation.");
        add("desc.gui.gasCent.output", "\u00A76Fluid Transfer\u00A7r$Fluid can be transferred to another centrifuge$via the output port for further processing.");
        add("desc.gui.rtg.heat", "\u00A7eCurrent heat level: %s");
        add("desc.gui.rtg.pellets", "Accepted Pellets:");
        add("desc.gui.rtg.pelletHeat", "%s (%s heat)");
        add("desc.gui.rtg.pelletPower", "%s (%s HE/tick)");
        add("desc.gui.rtgBFurnace.desc", "Requires at least 15 heat to process$The more heat on top of that, the faster it runs$Heat going over maximum speed will have no effect$Short-lived pellets may decay");
        add("desc.gui.upgrade", "\u00A7lAcceptable Upgrades:\u00A7r");
        add("desc.gui.upgrade.speed", " * \u00A74Speed\u00A7r: Stacks to level 3");
        add("desc.gui.upgrade.power", " * \u00A71Power-Saving\u00A7r: Stacks to level 3");
        add("desc.gui.upgrade.effectiveness", " * \u00A7aEffectiveness\u00A7r: Stacks to level 3");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ICF.get()), "Inertial Confinement Fusion Reactor");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ICF_CONTROLLER.get()), "ICF Laser Controller");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ICF_LASER_COMPONENT.get()), "ICF Laser Component");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_ICF_PRESS.get()), "ICF Fuel Pellet Maker");
        add(Objects.requireNonNull(HbmBlocks.PRESS_PREHEATER.get()), "Burner Press Preheater");
        add(Objects.requireNonNull(HbmBlocks.BARREL_PLASTIC.get()), "Safe Barrel™");
        add(Objects.requireNonNull(HbmBlocks.BARREL_CORRODED.get()), "Corroded Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_IRON.get()), "Iron Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_STEEL.get()), "Steel Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_TCALLOY.get()), "Technetium Steel Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_ANTIMATTER.get()), "Magnetic Antimatter Container");
        add(Objects.requireNonNull(HbmBlocks.BARREL_RED.get()), "Explosive Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_PINK.get()), "Kerosene Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_LOX.get()), "LOX Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_TAINT.get()), "IMP Residue Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_YELLOW.get()), "Radioactive Barrel");
        add(Objects.requireNonNull(HbmBlocks.BARREL_VITRIFIED.get()), "Vitrified Nuclear Waste Drum");
        add(Objects.requireNonNull(HbmBlocks.RED_CABLE.get()), "Red Copper Cable");
        add(Objects.requireNonNull(HbmBlocks.RED_CABLE_CLASSIC.get()), "Red Copper Cable (Classic)");
        add(Objects.requireNonNull(HbmBlocks.FLUID_DUCT_NEO.get()), "Universal Fluid Duct");
        add(Objects.requireNonNull(HbmBlocks.GLASS_BORON.get()), "Boron Glass");
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
        add(Objects.requireNonNull(HbmItems.BLOWTORCH.get()), "Blowtorch");
        add(Objects.requireNonNull(HbmItems.ACETYLENE_TORCH.get()), "Acetylene Torch");
        add(Objects.requireNonNull(HbmItems.CANISTER_EMPTY.get()), "Empty Canister");
        add(Objects.requireNonNull(HbmItems.CANISTER_FULL.get()), "Canister:");
        add(Objects.requireNonNull(HbmItems.CANISTER_NAPALM.get()), "Napalm B");
        add(Objects.requireNonNull(HbmItems.DISPERSER_CANISTER_EMPTY.get()), "Disperser Canister");
        add(Objects.requireNonNull(HbmItems.DISPERSER_CANISTER.get()), "Disperser Canister:");
        add(Objects.requireNonNull(HbmItems.GLYPHID_GLAND_EMPTY.get()), "Glyphid's Fluid Gland");
        add(Objects.requireNonNull(HbmItems.GLYPHID_GLAND.get()), "Gland");
        add(Objects.requireNonNull(HbmItems.GAS_EMPTY.get()), "Empty Gas Tank");
        add(Objects.requireNonNull(HbmItems.GAS_FULL.get()), "Gas Tank:");
        add(Objects.requireNonNull(HbmItems.FLUID_TANK_EMPTY.get()), "Empty Universal Fluid Tank");
        add(Objects.requireNonNull(HbmItems.FLUID_TANK_FULL.get()), "Universal Fluid Tank: %s");
        add(Objects.requireNonNull(HbmItems.FLUID_TANK_LEAD_EMPTY.get()), "Empty Hazardous Material Tank");
        add(Objects.requireNonNull(HbmItems.FLUID_TANK_LEAD_FULL.get()), "Hazardous Material Tank: %s");
        add(Objects.requireNonNull(HbmItems.FLUID_BARREL_EMPTY.get()), "Empty Fluid Barrel");
        add(Objects.requireNonNull(HbmItems.FLUID_BARREL_FULL.get()), "Fluid Barrel: %s");
        add(Objects.requireNonNull(HbmItems.FLUID_PACK_EMPTY.get()), "Large Fluid Container");
        add(Objects.requireNonNull(HbmItems.FLUID_PACK_FULL.get()), "Packaged %s");
        add(Objects.requireNonNull(HbmItems.DUCTTAPE.get()), "Duct Tape");
        add(Objects.requireNonNull(HbmItems.FUSE.get()), "Fuse");
        add(Objects.requireNonNull(HbmItems.SAFETY_FUSE.get()), "Safety Fuse");
        add(Objects.requireNonNull(HbmItems.FALLOUT.get()), "Pile of Fallout");
        add(Objects.requireNonNull(HbmItems.GEIGER_COUNTER.get()), "Geiger Counter");
        add(Objects.requireNonNull(HbmItems.IV_EMPTY.get()), "Empty IV Bag");
        add(Objects.requireNonNull(HbmItems.POWDER_SAWDUST.get()), "Sawdust");
        add(Objects.requireNonNull(HbmItems.POWDER_ASH.get()), "Ash");
        add(Objects.requireNonNull(HbmItems.POWDER_ICE.get()), "Cryo Powder");
        add(Objects.requireNonNull(HbmItems.POWDER_MAGIC.get()), "Pulverized Enchantment");
        add(Objects.requireNonNull(HbmItems.POWDER_POISON.get()), "Poison Powder");
        add(Objects.requireNonNull(HbmItems.PHOTO_PANEL.get()), "Photovoltaic Panel");
        add(Objects.requireNonNull(HbmItems.PIN.get()), "Bobby Pin");
        add(Objects.requireNonNull(HbmItems.CATALYST_CLAY.get()), "Clay Catalyst");
        add(Objects.requireNonNull(HbmItems.DEUTERIUM_FILTER.get()), "Deuterium Filter");
        add(Objects.requireNonNull(HbmItems.INGOT_CFT.get()), "Crystalline Fullerite");
        add(Objects.requireNonNull(HbmItems.HAZMAT_CLOTH.get()), "Hazmat Cloth");
        add(Objects.requireNonNull(HbmItems.ASBESTOS_CLOTH.get()), "Asbestos Cloth");
        add(Objects.requireNonNull(HbmItems.FILTER_COAL.get()), "Coal Filter");
        add(Objects.requireNonNull(HbmItems.REACTOR_CORE.get()), "Reactor Core");
        add(Objects.requireNonNull(HbmItems.THERMO_ELEMENT.get()), "Thermoelectric Element");
        add(Objects.requireNonNull(HbmItems.RTG_UNIT.get()), "RTG Element");
        add(Objects.requireNonNull(HbmItems.MAGNETRON.get()), "Magnetron");
        add(Objects.requireNonNull(HbmItems.DRILL_TITANIUM.get()), "Titanium Drill");
        add(Objects.requireNonNull(HbmItems.ENTANGLEMENT_KIT.get()), "Entanglement Kit");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_STEEL.get()), "Steel Drillbit");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_STEEL_DIAMOND.get()), "Steel Drillbit (Diamond-Tipped)");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_HSS.get()), "High-Speed Steel Drillbit");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_HSS_DIAMOND.get()), "High-Speed Steel Drillbit (Diamond-Tipped)");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_DESH.get()), "Desh Drillbit");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_DESH_DIAMOND.get()), "Desh Drillbit (Diamond-Tipped)");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_TCALLOY.get()), "Technetium Steel Drillbit");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_TCALLOY_DIAMOND.get()), "Technetium Steel Drillbit (Diamond-Tipped)");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_FERRO.get()), "Ferrouranium Drillbit");
        add(Objects.requireNonNull(HbmItems.DRILLBIT_FERRO_DIAMOND.get()), "Ferrouranium Drillbit (Diamond-Tipped)");
        add(Objects.requireNonNull(HbmItems.PART_LITHIUM.get()), "Lithium Powder Part");
        add(Objects.requireNonNull(HbmItems.PART_BERYLLIUM.get()), "Beryllium Powder Part");
        add(Objects.requireNonNull(HbmItems.PART_CARBON.get()), "Carbon Powder Part");
        add(Objects.requireNonNull(HbmItems.PART_COPPER.get()), "Copper Powder Part");
        add(Objects.requireNonNull(HbmItems.PART_PLUTONIUM.get()), "Plutonium Powder Part");
        add(Objects.requireNonNull(HbmItems.CENTRIFUGE_ELEMENT.get()), "Gas Centrifuge Element");
        add(Objects.requireNonNull(HbmItems.BLUEPRINTS.get()), "Blueprints");
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
        add(Objects.requireNonNull(HbmItems.PISTON_SET_STEEL.get()), "Steel Piston Set");
        add(Objects.requireNonNull(HbmItems.PISTON_SET_DURA.get()), "Dura-Steel Piston Set");
        add(Objects.requireNonNull(HbmItems.PISTON_SET_DESH.get()), "Desh Piston Set");
        add(Objects.requireNonNull(HbmItems.PISTON_SET_STARMETAL.get()), "Starmetal Piston Set");
        add(Objects.requireNonNull(HbmItems.FLYWHEEL_BERYLLIUM.get()), "Beryllium Flywheel");
        add(Objects.requireNonNull(HbmItems.RING_STARMETAL.get()), "§9Starmetal Ring§r");
        add(Objects.requireNonNull(HbmItems.SAWBLADE.get()), "Sawblade");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD.get()), "Meteorite Sword");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_SEARED.get()), "Meteorite Sword (Seared)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_REFORGED.get()), "Meteorite Sword (Reforged)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_HARDENED.get()), "Meteorite Sword (Hardened)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_ALLOYED.get()), "Meteorite Sword (Alloyed)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_MACHINED.get()), "Meteorite Sword (Machined)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_TREATED.get()), "Meteorite Sword (Treated)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_ETCHED.get()), "Meteorite Sword (Etched)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_BRED.get()), "Meteorite Sword (Bred)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_IRRADIATED.get()), "Meteorite Sword (Irradiated)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_FUSED.get()), "Meteorite Sword (Fused)");
        add(Objects.requireNonNull(HbmItems.METEORITE_SWORD_BALEFUL.get()), "Meteorite Sword (Baleful)");
        add(Objects.requireNonNull(HbmItems.BOTTLE_MERCURY.get()), "Bottle of Mercury");
        add(Objects.requireNonNull(HbmItems.NUGGET_MERCURY.get()), "Drop of Mercury");
        add(Objects.requireNonNull(HbmItems.NUGGET_MERCURY_TINY.get()), "Tiny Drop of Mercury");
        add(Objects.requireNonNull(HbmItems.NUCLEAR_WASTE.get()), "Nuclear Waste");
        add(Objects.requireNonNull(HbmItems.NUCLEAR_WASTE_VITRIFIED.get()), "Vitrified Nuclear Waste");
        add(Objects.requireNonNull(HbmItems.NUCLEAR_WASTE_TINY.get()), "Tiny Nuclear Waste");
        add(Objects.requireNonNull(HbmItems.PELLET_CHARGED.get()), "Ionized Particles");
        add(Objects.requireNonNull(HbmItems.PARTICLE_MUON.get()), "Muon Capsule");
        add(Objects.requireNonNull(HbmItems.ICF_PELLET_EMPTY.get()), "Empty ICF Fuel Pellet");
        add(Objects.requireNonNull(HbmItems.ICF_PELLET.get()), "ICF Fuel Pellet");
        add(Objects.requireNonNull(HbmItems.ICF_PELLET_DEPLETED.get()), "Depleted ICF Fuel Pellet");
        add("icffuel.hydrogen", "Hydrogen");
        add("icffuel.deuterium", "Deuterium");
        add("icffuel.tritium", "Tritium");
        add("icffuel.helium3", "Helium-3");
        add("icffuel.helium4", "Helium-4");
        add("icffuel.lithium", "Lithium");
        add("icffuel.beryllium", "Beryllium");
        add("icffuel.boron", "Boron");
        add("icffuel.carbon", "Carbon");
        add("icffuel.oxygen", "Oxygen");
        add("icffuel.sodium", "Sodium");
        add("icffuel.chlorine", "Chlorine");
        add("icffuel.calcium", "Calcium");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG.get()), "Plutonium-238 RTG Pellet");
        add("item.hbmntm.pellet_rtg.desc", "RTG fuel pellet for infinite energy! (almost)");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_RADIUM.get()), "Radium-226 RTG Pellet");
        add("item.hbmntm.pellet_rtg_radium.desc", "Great starter pellet, sourced from all-natural radium!");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_WEAK.get()), "Weak Uranium RTG Pellet");
        add("item.hbmntm.pellet_rtg_weak.desc", "Cheaper and weaker pellet, now with more U238!");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_STRONTIUM.get()), "Strontium-90 RTG Pellet");
        add("item.hbmntm.pellet_rtg_strontium.desc", "Known to the State of California...");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_COBALT.get()), "Cobalt-60 RTG Pellet");
        add("item.hbmntm.pellet_rtg_cobalt.desc", "Not the best as an RTG, but great for gamma radiation!");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_ACTINIUM.get()), "Actinium-227 RTG Pellet");
        add("item.hbmntm.pellet_rtg_actinium.desc", "A glow of blue light and beta rays.");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_AMERICIUM.get()), "Americium-241 RTG Pellet");
        add("item.hbmntm.pellet_rtg_americium.desc", "Rare and reliable, good old Americium!");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_POLONIUM.get()), "Polonium-210 RTG Pellet");
        add("item.hbmntm.pellet_rtg_polonium.desc", "More powerful RTG pellet, made from finest polonium!");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_GOLD.get()), "Gold-198 RTG Pellet");
        add("item.hbmntm.pellet_rtg_gold.desc", "Made from a rare, highly unstable gold isotope.");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_LEAD.get()), "Lead-209 RTG Pellet");
        add("item.hbmntm.pellet_rtg_lead.desc", "Exposure will result in immediate death.");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_BISMUTH.get()), "Decayed Bismuth RTG Pellet");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_LEAD.get()), "Decayed Lead RTG Pellet");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_NEPTUNIUM.get()), "Decayed Neptunium RTG Pellet");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_MERCURY.get()), "Decayed Mercury RTG Pellet");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_NICKEL.get()), "Decayed Nickel RTG Pellet");
        add(Objects.requireNonNull(HbmItems.PELLET_RTG_DEPLETED_ZIRCONIUM.get()), "Decayed Zirconium RTG Pellet");
        add("desc.item.rtgDecay", "Decays to: %s");
        add("desc.item.rtgHeat", "Power Level: %s");
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
        add(Objects.requireNonNull(HbmItems.SCRAP.get()), "Scrap");
        add(Objects.requireNonNull(HbmItems.SCRAP_NUCLEAR.get()), "Radioactive Scraps");
        add(Objects.requireNonNull(HbmItems.SCRAP_OIL.get()), "Oily Scraps");
        add(Objects.requireNonNull(HbmItems.SCRAP_PLASTIC.get()), "Plastic Scraps");
        add(Objects.requireNonNull(HbmItems.DEBRIS_CONCRETE.get()), "Broken Concrete");
        add(Objects.requireNonNull(HbmItems.DEBRIS_SHRAPNEL.get()), "Walkway Shrapnel");
        add(Objects.requireNonNull(HbmItems.DEBRIS_EXCHANGER.get()), "Heat Exchanger Piece");
        add(Objects.requireNonNull(HbmItems.DEBRIS_ELEMENT.get()), "Broken ZIRNOX Fuel Element");
        add(Objects.requireNonNull(HbmItems.DEBRIS_METAL.get()), "Broken Metal Bar");
        add(Objects.requireNonNull(HbmItems.DEBRIS_GRAPHITE.get()), "Hot Graphite Chunk");
        add(Objects.requireNonNull(HbmItems.BLADES_STEEL.get()), "Steel Shredder Blades");
        add(Objects.requireNonNull(HbmItems.BLADES_TITANIUM.get()), "Titanium Shredder Blades");
        add(Objects.requireNonNull(HbmItems.BLADES_ADVANCED_ALLOY.get()), "Advanced Shredder Blades");
        add(Objects.requireNonNull(HbmItems.BLADES_DESH.get()), "Desh Shredder Blades");
        add(Objects.requireNonNull(HbmBlocks.MACHINE_SHREDDER.get()), "Shredder");
        add("screen.hbmntm.machine_gascent.warning", "WARNING! THIS MACHINE OUTPUTS RADIATION");
        add("screen.hbmntm.machine_rotary_furnace.no_output", "No crucible acceptor found near the output spout.");
        add("hbmpseudofluid.none", "Empty");
        add("hbmpseudofluid.heuf6", "Highly Enriched UF6");
        add("hbmpseudofluid.meuf6", "Medium Enriched UF6");
        add("hbmpseudofluid.leuf6", "Low Enriched UF6");
        add("hbmpseudofluid.nuf6", "Natural UF6");
        add("hbmpseudofluid.pf6", "Plutonium Hexafluoride");
        add("hbmpseudofluid.mud_heavy", "Heavy Sludge Fraction");
        add("hbmpseudofluid.mud", "Poisonous Sludge Gas");
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
        add(Objects.requireNonNull(HbmItems.GEM_ALEXANDRITE.get()), "Alexandrite Gem");

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

        add("item.hbmntm.stamp_book", "Printing Press Stamp");
        for (final PrintingStampType type : PrintingStampType.values()) {
            add("item.hbmntm.stamp_book." + type.translationSuffix(), type.displayName());
        }

        add("item.hbmntm.page_of_", "Page");
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

        for (final OverworldOreType type : OverworldOreType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getOverworldOre(type).get()), type.displayName());
        }

        for (final NetherOreType type : NetherOreType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getNetherOre(type).get()), type.displayName());
        }

        validateCoverage();
    }

    private void validateCoverage() {
        final List<String> missing = new ArrayList<>();
        final String prefix = "." + HbmNtmMod.MOD_ID + ".";
        for (final RegistryObject<net.minecraft.world.level.block.Block> ro : HbmBlocks.BLOCKS.getEntries()) {
            final String key = "block" + prefix + ro.getId().getPath();
            if (!trackedKeys.contains(key)) {
                missing.add(key);
            }
        }
        for (final RegistryObject<net.minecraft.world.item.Item> ro : HbmItems.ITEMS.getEntries()) {
            if (ro.get() instanceof BlockItem) {
                continue;
            }
            final String key = "item" + prefix + ro.getId().getPath();
            if (!trackedKeys.contains(key)) {
                missing.add(key);
            }
        }
        if (!missing.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("HbmLanguageProvider missing ").append(missing.size()).append(" translation keys:\n");
            for (final String key : missing) {
                sb.append("  ").append(key).append('\n');
            }
            throw new IllegalStateException(sb.toString());
        }
    }
}

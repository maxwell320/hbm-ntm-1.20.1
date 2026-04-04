package com.hbm.ntm.data;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.BasaltBlockType;
import com.hbm.ntm.common.block.BasaltOreType;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.block.StoneResourceType;
import com.hbm.ntm.common.item.BriquetteItemType;
import com.hbm.ntm.common.item.CasingItemType;
import com.hbm.ntm.common.item.CircuitItemType;
import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.item.CokeItemType;
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
        add("death.attack.radiation", "%1$s died from radiation poisoning");
        add(HbmMobEffects.RADIATION.get(), "Radiation");
        add(HbmMobEffects.RADAWAY.get(), "RadAway");
        add(HbmMobEffects.RAD_X.get(), "Rad-X");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_IRON.get()), "Iron Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_STEEL.get()), "Steel Anvil");
        add(Objects.requireNonNull(HbmBlocks.ANVIL_DESH.get()), "Desh Anvil");
        add(Objects.requireNonNull(HbmBlocks.FALLOUT.get()), "Fallout");
        add(Objects.requireNonNull(HbmBlocks.GAS_ASBESTOS.get()), "Airborne Asbestos Particles");
        add(Objects.requireNonNull(HbmBlocks.GEIGER.get()), "Geiger Counter");
        add(Objects.requireNonNull(HbmBlocks.PRESS_PREHEATER.get()), "Burner Press Preheater");
        add(Objects.requireNonNull(HbmBlocks.SELLAFIELD.get()), "Sellafite");
        add(Objects.requireNonNull(HbmItems.BIOMASS.get()), "Biomass");
        add(Objects.requireNonNull(HbmItems.BIOMASS_COMPRESSED.get()), "Compressed Biomass");
        add(Objects.requireNonNull(HbmItems.COIL_ADVANCED_TORUS.get()), "Super Conducting Ring Coil");
        add(Objects.requireNonNull(HbmItems.COIL_COPPER_TORUS.get()), "Ring Coil");
        add(Objects.requireNonNull(HbmItems.COIL_GOLD_TORUS.get()), "Golden Ring Coil");
        add(Objects.requireNonNull(HbmItems.CRT_DISPLAY.get()), "Cathode Ray Tube");
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
        add("item.hbmntm.pin.desc", "Standard success rate of picking a regular lock is ~10%%.");
        add(Objects.requireNonNull(HbmItems.RADAWAY.get()), "RadAway");
        add(Objects.requireNonNull(HbmItems.RADAWAY_STRONG.get()), "Strong RadAway");
        add(Objects.requireNonNull(HbmItems.RADAWAY_FLUSH.get()), "Elite RadAway");
        add(Objects.requireNonNull(HbmItems.RADX.get()), "Rad-X");
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

        for (final BasaltBlockType type : BasaltBlockType.values()) {
            add(Objects.requireNonNull(HbmBlocks.getBasaltBlock(type).get()), type.displayName());
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

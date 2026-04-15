package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.SellafieldBlock;
import com.hbm.ntm.common.item.CanisterItem;
import com.hbm.ntm.common.item.DisperserCanisterItem;
import com.hbm.ntm.common.item.FluidTankItem;
import com.hbm.ntm.common.item.GasTankItem;
import com.hbm.ntm.common.item.SellafieldBlockItem;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class HbmCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HbmNtmMod.MOD_ID);
    @SuppressWarnings("null")
    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
        .title(Objects.requireNonNull(Component.translatable("itemGroup." + HbmNtmMod.MOD_ID + ".main")))
        .icon(() -> new ItemStack(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.URANIUM, HbmMaterialShape.INGOT).get())))
        .displayItems((parameters, output) -> HbmItems.creativeTabEntries().forEach(item -> {
            if (item == HbmItems.SELLAFIELD) {
                for (int level = 0; level <= SellafieldBlock.MAX_LEVEL; level++) {
                    output.accept(SellafieldBlockItem.withLevel(Objects.requireNonNull(HbmItems.SELLAFIELD.get()), level));
                }
            } else if (item == HbmItems.CANISTER_FULL) {
                CanisterItem.creativeStacks(Objects.requireNonNull(HbmItems.CANISTER_FULL.get())).forEach(output::accept);
            } else if (item == HbmItems.DISPERSER_CANISTER) {
                DisperserCanisterItem.creativeStacks(Objects.requireNonNull(HbmItems.DISPERSER_CANISTER.get()), DisperserCanisterItem.ContainerKind.DISPERSER)
                    .forEach(output::accept);
            } else if (item == HbmItems.GLYPHID_GLAND) {
                DisperserCanisterItem.creativeStacks(Objects.requireNonNull(HbmItems.GLYPHID_GLAND.get()), DisperserCanisterItem.ContainerKind.GLYPHID)
                    .forEach(output::accept);
            } else if (item == HbmItems.GAS_FULL) {
                GasTankItem.creativeStacks(Objects.requireNonNull(HbmItems.GAS_FULL.get())).forEach(output::accept);
            } else if (item == HbmItems.FLUID_TANK_FULL) {
                FluidTankItem.creativeStacks(Objects.requireNonNull(HbmItems.FLUID_TANK_FULL.get()), false).forEach(output::accept);
            } else if (item == HbmItems.FLUID_TANK_LEAD_FULL) {
                FluidTankItem.creativeStacks(Objects.requireNonNull(HbmItems.FLUID_TANK_LEAD_FULL.get()), true).forEach(output::accept);
            } else if (item == HbmItems.FLUID_BARREL_FULL) {
                FluidTankItem.creativeStacks(Objects.requireNonNull(HbmItems.FLUID_BARREL_FULL.get()), false).forEach(output::accept);
            } else if (item == HbmItems.FLUID_PACK_FULL) {
                FluidTankItem.creativeStacks(Objects.requireNonNull(HbmItems.FLUID_PACK_FULL.get()), false).forEach(output::accept);
            } else {
                output.accept(Objects.requireNonNull(item.get()));
            }
        }))
        .build());

    private HbmCreativeTabs() {
    }
}

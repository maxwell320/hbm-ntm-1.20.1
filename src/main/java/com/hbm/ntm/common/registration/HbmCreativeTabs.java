package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
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
    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + HbmNtmMod.MOD_ID + ".main"))
        .icon(() -> new ItemStack(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.URANIUM, HbmMaterialShape.INGOT).get())))
        .displayItems((parameters, output) -> HbmItems.creativeTabEntries().forEach(item -> output.accept(Objects.requireNonNull(item.get()))))
        .build());

    private HbmCreativeTabs() {
    }
}

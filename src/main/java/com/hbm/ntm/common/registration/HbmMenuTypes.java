package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.anvil.NtmAnvilMenu;
import com.hbm.ntm.common.menu.BarrelMenu;
import com.hbm.ntm.common.menu.FluidIdentifierMenu;
import com.hbm.ntm.common.menu.PressMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HbmMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, HbmNtmMod.MOD_ID);
    public static final RegistryObject<MenuType<BarrelMenu>> BARREL = MENU_TYPES.register("barrel", () -> IForgeMenuType.create(BarrelMenu::new));
    public static final RegistryObject<MenuType<NtmAnvilMenu>> NTM_ANVIL = MENU_TYPES.register("ntm_anvil", () -> IForgeMenuType.create(NtmAnvilMenu::new));
    public static final RegistryObject<MenuType<FluidIdentifierMenu>> FLUID_IDENTIFIER = MENU_TYPES.register("fluid_identifier", () -> IForgeMenuType.create(FluidIdentifierMenu::new));
    public static final RegistryObject<MenuType<PressMenu>> MACHINE_PRESS = MENU_TYPES.register("machine_press", () -> IForgeMenuType.create(PressMenu::new));

    private HbmMenuTypes() {
    }
}

package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.anvil.NtmAnvilMenu;
import com.hbm.ntm.common.menu.AssemblyMachineMenu;
import com.hbm.ntm.common.menu.BarrelMenu;
import com.hbm.ntm.common.menu.CentrifugeMenu;
import com.hbm.ntm.common.menu.FluidIdentifierMenu;
import com.hbm.ntm.common.menu.GasCentrifugeMenu;
import com.hbm.ntm.common.menu.IcfMenu;
import com.hbm.ntm.common.menu.IcfPressMenu;
import com.hbm.ntm.common.menu.PressMenu;
import com.hbm.ntm.common.menu.PurexMenu;
import com.hbm.ntm.common.menu.ShredderMenu;
import com.hbm.ntm.common.menu.SolderingStationMenu;
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
    public static final RegistryObject<MenuType<AssemblyMachineMenu>> MACHINE_ASSEMBLY_MACHINE = MENU_TYPES.register("machine_assembly_machine", () -> IForgeMenuType.create(AssemblyMachineMenu::new));
    public static final RegistryObject<MenuType<SolderingStationMenu>> MACHINE_SOLDERING_STATION = MENU_TYPES.register("machine_soldering_station", () -> IForgeMenuType.create(SolderingStationMenu::new));
    public static final RegistryObject<MenuType<ShredderMenu>> MACHINE_SHREDDER = MENU_TYPES.register("machine_shredder", () -> IForgeMenuType.create(ShredderMenu::new));
    public static final RegistryObject<MenuType<CentrifugeMenu>> MACHINE_CENTRIFUGE = MENU_TYPES.register("machine_centrifuge", () -> IForgeMenuType.create(CentrifugeMenu::new));
    public static final RegistryObject<MenuType<GasCentrifugeMenu>> MACHINE_GAS_CENTRIFUGE = MENU_TYPES.register("machine_gascent", () -> IForgeMenuType.create(GasCentrifugeMenu::new));
    public static final RegistryObject<MenuType<PurexMenu>> MACHINE_PUREX = MENU_TYPES.register("machine_purex", () -> IForgeMenuType.create(PurexMenu::new));
    public static final RegistryObject<MenuType<IcfMenu>> MACHINE_ICF = MENU_TYPES.register("machine_icf", () -> IForgeMenuType.create(IcfMenu::new));
    public static final RegistryObject<MenuType<IcfPressMenu>> MACHINE_ICF_PRESS = MENU_TYPES.register("machine_icf_press", () -> IForgeMenuType.create(IcfPressMenu::new));

    private HbmMenuTypes() {
    }
}

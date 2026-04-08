package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.BatteryBlockEntity;
import com.hbm.ntm.common.block.entity.BarrelBlockEntity;
import com.hbm.ntm.common.block.entity.CableBlockEntity;
import com.hbm.ntm.common.block.entity.CreativeEnergySourceBlockEntity;
import com.hbm.ntm.common.block.entity.FluidDuctBlockEntity;
import com.hbm.ntm.common.block.entity.GeigerCounterBlockEntity;
import com.hbm.ntm.common.block.entity.PressBlockEntity;
import com.hbm.ntm.common.block.entity.PressProxyBlockEntity;
import com.hbm.ntm.common.block.entity.ShredderBlockEntity;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("null")
public final class HbmBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HbmNtmMod.MOD_ID);
    public static final RegistryObject<BlockEntityType<CableBlockEntity>> RED_CABLE = register("red_cable", CableBlockEntity::new,
        HbmBlocks.RED_CABLE,
        HbmBlocks.RED_CABLE_CLASSIC);
    public static final RegistryObject<BlockEntityType<FluidDuctBlockEntity>> FLUID_DUCT = register("fluid_duct", FluidDuctBlockEntity::new,
        HbmBlocks.FLUID_DUCT_NEO);
    public static final RegistryObject<BlockEntityType<CreativeEnergySourceBlockEntity>> CREATIVE_ENERGY_SOURCE = register("creative_energy_source", CreativeEnergySourceBlockEntity::new,
        HbmBlocks.CREATIVE_ENERGY_SOURCE);
    public static final RegistryObject<BlockEntityType<BatteryBlockEntity>> MACHINE_BATTERY = register("machine_battery", BatteryBlockEntity::new,
        HbmBlocks.MACHINE_BATTERY);
    public static final RegistryObject<BlockEntityType<GeigerCounterBlockEntity>> GEIGER = register("geiger", GeigerCounterBlockEntity::new,
        HbmBlocks.GEIGER);
    public static final RegistryObject<BlockEntityType<PressBlockEntity>> MACHINE_PRESS = register("machine_press", PressBlockEntity::new,
        HbmBlocks.MACHINE_PRESS);
    public static final RegistryObject<BlockEntityType<PressProxyBlockEntity>> MACHINE_PRESS_PROXY = register("machine_press_proxy", PressProxyBlockEntity::new,
        HbmBlocks.MACHINE_PRESS);
    public static final RegistryObject<BlockEntityType<ShredderBlockEntity>> MACHINE_SHREDDER = register("machine_shredder", ShredderBlockEntity::new,
        HbmBlocks.MACHINE_SHREDDER);
    public static final RegistryObject<BlockEntityType<BarrelBlockEntity>> BARREL = register("barrel", BarrelBlockEntity::new,
        HbmBlocks.BARREL_PLASTIC,
        HbmBlocks.BARREL_IRON,
        HbmBlocks.BARREL_CORRODED,
        HbmBlocks.BARREL_STEEL,
        HbmBlocks.BARREL_TCALLOY,
        HbmBlocks.BARREL_ANTIMATTER);

    @SafeVarargs
    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(final String id,
                                                                                        final BlockEntityType.BlockEntitySupplier<T> factory,
                                                                                        final Supplier<? extends Block>... validBlocks) {
        return BLOCK_ENTITY_TYPES.register(id, () -> BlockEntityType.Builder.of(factory, Arrays.stream(validBlocks)
            .map(Supplier::get)
            .toArray(Block[]::new)).build(null));
    }

    private HbmBlockEntityTypes() {
    }
}

package com.hbm.ntm.common.registration;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.RadLavaBlock;
import com.hbm.ntm.common.block.VolcanicLavaBlock;
import com.hbm.ntm.common.fluid.HbmFluidType;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("null")
public final class HbmFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, HbmNtmMod.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, HbmNtmMod.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HbmNtmMod.MOD_ID);
    private static final ResourceLocation COOLANT_STILL = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/coolant_still"));
    private static final ResourceLocation COOLANT_FLOWING = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/coolant_flowing"));
    private static final ResourceLocation COOLANT_HOT_STILL = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/coolant_hot_still"));
    private static final ResourceLocation COOLANT_HOT_FLOWING = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/coolant_hot_flowing"));
    private static final ResourceLocation OIL_STILL = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/oil_still"));
    private static final ResourceLocation OIL_FLOWING = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/oil_flowing"));
    private static final ResourceLocation VOLCANIC_LAVA_STILL = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/volcanic_lava_still"));
    private static final ResourceLocation VOLCANIC_LAVA_FLOWING = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/volcanic_lava_flowing"));
    private static final ResourceLocation RAD_LAVA_STILL = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/rad_lava_still"));
    private static final ResourceLocation RAD_LAVA_FLOWING = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "block/rad_lava_flowing"));
    public static final FluidEntry COOLANT = registerCoolant("coolant", 0xFFD8FCFF, 1_000, COOLANT_STILL, COOLANT_FLOWING, MapColor.ICE);
    public static final FluidEntry COOLANT_HOT = registerCoolant("coolant_hot", 0xFF99525E, 1_000, COOLANT_HOT_STILL, COOLANT_HOT_FLOWING, MapColor.COLOR_RED);
    public static final FluidEntry OIL = registerOil();
    public static final FluidEntry VOLCANIC_LAVA = registerVolcanicLava();
    public static final FluidEntry RAD_LAVA = registerRadLava();

    private HbmFluids() {
    }

    private static FluidEntry registerCoolant(final String id, final int tintColor, final int density, final ResourceLocation stillTexture,
                                              final ResourceLocation flowingTexture, final MapColor mapColor) {
        final FluidEntry entry = new FluidEntry();
        final ResourceLocation fluidId = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, id));
        final FluidType.Properties fluidTypeProperties = FluidType.Properties.create()
            .density(density)
            .viscosity(1_000)
            .descriptionId(Util.makeDescriptionId("fluid", fluidId));
        final ForgeFlowingFluid.Properties fluidProperties = new ForgeFlowingFluid.Properties(entry::getFluidType, entry::getStillFluid, entry::getFlowingFluid)
            .block(entry::getBlock)
            .levelDecreasePerBlock(1)
            .slopeFindDistance(2)
            .tickRate(10)
            .explosionResistance(100.0F);

        entry.fluidType = FLUID_TYPES.register(id, () -> new HbmFluidType(fluidTypeProperties, stillTexture, flowingTexture, tintColor));
        entry.stillFluid = FLUIDS.register(id, () -> new ForgeFlowingFluid.Source(fluidProperties));
        entry.flowingFluid = FLUIDS.register("flowing_" + id, () -> new ForgeFlowingFluid.Flowing(fluidProperties));
        entry.block = BLOCKS.register(id + "_block", () -> new LiquidBlock(entry::getStillFluid,
            Objects.requireNonNull(BlockBehaviour.Properties.of().noCollission().strength(100.0F).noLootTable().replaceable().pushReaction(PushReaction.DESTROY).liquid().mapColor(Objects.requireNonNull(mapColor)))));
        return entry;
    }

    private static FluidEntry registerOil() {
        final FluidEntry entry = new FluidEntry();
        final ResourceLocation fluidId = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "oil"));
        final FluidType.Properties fluidTypeProperties = FluidType.Properties.create()
            .density(1_200)
            .viscosity(2_000)
            .descriptionId(Util.makeDescriptionId("fluid", fluidId));
        final ForgeFlowingFluid.Properties fluidProperties = new ForgeFlowingFluid.Properties(entry::getFluidType, entry::getStillFluid, entry::getFlowingFluid)
            .block(entry::getBlock)
            .levelDecreasePerBlock(1)
            .slopeFindDistance(2)
            .tickRate(25)
            .explosionResistance(100.0F);

        entry.fluidType = FLUID_TYPES.register("oil", () -> new HbmFluidType(fluidTypeProperties, OIL_STILL, OIL_FLOWING, 0xFF020202));
        entry.stillFluid = FLUIDS.register("oil", () -> new ForgeFlowingFluid.Source(fluidProperties));
        entry.flowingFluid = FLUIDS.register("flowing_oil", () -> new ForgeFlowingFluid.Flowing(fluidProperties));
        entry.block = BLOCKS.register("oil_block", () -> new LiquidBlock(entry::getStillFluid,
            Objects.requireNonNull(BlockBehaviour.Properties.of().noCollission().strength(100.0F).noLootTable().replaceable().pushReaction(PushReaction.DESTROY).liquid().mapColor(Objects.requireNonNull(MapColor.COLOR_BLACK)))));
        return entry;
    }

    private static FluidEntry registerVolcanicLava() {
        final FluidEntry entry = new FluidEntry();
        final ResourceLocation blockId = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "volcanic_lava_block"));
        final FluidType.Properties fluidTypeProperties = FluidType.Properties.create()
            .lightLevel(15)
            .density(3000)
            .viscosity(3000)
            .temperature(1300)
            .descriptionId(Util.makeDescriptionId("block", blockId));
        final ForgeFlowingFluid.Properties fluidProperties = new ForgeFlowingFluid.Properties(entry::getFluidType, entry::getStillFluid, entry::getFlowingFluid)
            .block(entry::getBlock)
            .levelDecreasePerBlock(1)
            .slopeFindDistance(2)
            .tickRate(30)
            .explosionResistance(500.0F);

        entry.fluidType = FLUID_TYPES.register("volcanic_lava", () -> new HbmFluidType(fluidTypeProperties, VOLCANIC_LAVA_STILL, VOLCANIC_LAVA_FLOWING, 0xFFFFFFFF));
        entry.stillFluid = FLUIDS.register("volcanic_lava", () -> new ForgeFlowingFluid.Source(fluidProperties));
        entry.flowingFluid = FLUIDS.register("flowing_volcanic_lava", () -> new ForgeFlowingFluid.Flowing(fluidProperties));
        entry.block = BLOCKS.register("volcanic_lava_block", () -> new VolcanicLavaBlock(entry::getStillFluid,
            Objects.requireNonNull(BlockBehaviour.Properties.of().noCollission().strength(100.0F).noLootTable().replaceable().pushReaction(PushReaction.DESTROY).liquid().mapColor(MapColor.FIRE).randomTicks())));
        return entry;
    }

    private static FluidEntry registerRadLava() {
        final FluidEntry entry = new FluidEntry();
        final ResourceLocation blockId = Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "rad_lava_block"));
        final FluidType.Properties fluidTypeProperties = FluidType.Properties.create()
            .lightLevel(15)
            .density(3000)
            .viscosity(3000)
            .temperature(1300)
            .descriptionId(Util.makeDescriptionId("block", blockId));
        final ForgeFlowingFluid.Properties fluidProperties = new ForgeFlowingFluid.Properties(entry::getFluidType, entry::getStillFluid, entry::getFlowingFluid)
            .block(entry::getBlock)
            .levelDecreasePerBlock(1)
            .slopeFindDistance(2)
            .tickRate(30)
            .explosionResistance(500.0F);

        entry.fluidType = FLUID_TYPES.register("rad_lava", () -> new HbmFluidType(fluidTypeProperties, RAD_LAVA_STILL, RAD_LAVA_FLOWING, 0xFFFFFFFF));
        entry.stillFluid = FLUIDS.register("rad_lava", () -> new ForgeFlowingFluid.Source(fluidProperties));
        entry.flowingFluid = FLUIDS.register("flowing_rad_lava", () -> new ForgeFlowingFluid.Flowing(fluidProperties));
        entry.block = BLOCKS.register("rad_lava_block", () -> new RadLavaBlock(entry::getStillFluid,
            Objects.requireNonNull(BlockBehaviour.Properties.of().noCollission().strength(100.0F).noLootTable().replaceable().pushReaction(PushReaction.DESTROY).liquid().mapColor(MapColor.FIRE).randomTicks())));
        return entry;
    }

    public static final class FluidEntry {
        private RegistryObject<FluidType> fluidType;
        private RegistryObject<FlowingFluid> stillFluid;
        private RegistryObject<FlowingFluid> flowingFluid;
        private RegistryObject<LiquidBlock> block;

        private FluidEntry() {
        }

        public FluidType getFluidType() {
            return Objects.requireNonNull(fluidType.get());
        }

        public FlowingFluid getStillFluid() {
            return Objects.requireNonNull(stillFluid.get());
        }

        public FlowingFluid getFlowingFluid() {
            return Objects.requireNonNull(flowingFluid.get());
        }

        public LiquidBlock getBlock() {
            return Objects.requireNonNull(block.get());
        }
    }
}

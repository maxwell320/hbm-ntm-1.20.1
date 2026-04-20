package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("null")
public final class HbmDebugFluidInspector {

    private HbmDebugFluidInspector() {
    }

    public static Path listFluids() throws IOException {
        final StringBuilder sb = new StringBuilder(4096);
        sb.append("=== HBM fluid list ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');

        final Map<ResourceLocation, Fluid> fluids = new TreeMap<>();
        for (final ResourceLocation id : ForgeRegistries.FLUIDS.getKeys()) {
            if (!HbmNtmMod.MOD_ID.equals(id.getNamespace())) {
                continue;
            }
            final Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
            if (fluid != null) {
                fluids.put(id, fluid);
            }
        }
        sb.append("hbmntm fluid count: ").append(fluids.size()).append('\n');
        sb.append("\nid\tclass\tfluidType\n");
        for (final Map.Entry<ResourceLocation, Fluid> entry : fluids.entrySet()) {
            final Fluid fluid = entry.getValue();
            sb.append(entry.getKey()).append('\t')
              .append(fluid.getClass().getSimpleName()).append('\t')
              .append(describeFluidType(fluid.getFluidType()))
              .append('\n');
        }

        sb.append("\n=== hbmntm fluid types ===\n");
        int typeCount = 0;
        final IForgeRegistry<FluidType> fluidTypes = ForgeRegistries.FLUID_TYPES.get();
        if (fluidTypes != null) {
            final Map<ResourceLocation, FluidType> sorted = new TreeMap<>();
            for (final ResourceLocation id : fluidTypes.getKeys()) {
                if (!HbmNtmMod.MOD_ID.equals(id.getNamespace())) {
                    continue;
                }
                final FluidType type = fluidTypes.getValue(id);
                if (type != null) {
                    sorted.put(id, type);
                }
            }
            for (final Map.Entry<ResourceLocation, FluidType> entry : sorted.entrySet()) {
                typeCount++;
                sb.append(entry.getKey()).append(": ")
                  .append("density=").append(entry.getValue().getDensity())
                  .append(", temperature=").append(entry.getValue().getTemperature())
                  .append(", viscosity=").append(entry.getValue().getViscosity())
                  .append(", lightLevel=").append(entry.getValue().getLightLevel())
                  .append('\n');
            }
        }
        sb.append("hbmntm fluid type count: ").append(typeCount).append('\n');

        return HbmDebugWriter.write("fluid-list", sb.toString());
    }

    public static Path dumpFluid(final String idString) throws IOException {
        final ResourceLocation id = ResourceLocation.tryParse(idString);
        final StringBuilder sb = new StringBuilder(1024);
        sb.append("=== HBM fluid dump ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("requested: ").append(idString).append('\n');
        if (id == null) {
            sb.append("ERROR: cannot parse resource location '").append(idString).append("'\n");
            return HbmDebugWriter.write("fluid-dump", sb.toString());
        }
        final Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
        if (fluid == null) {
            sb.append("ERROR: no fluid registered under ").append(id).append('\n');
            return HbmDebugWriter.write("fluid-dump", sb.toString());
        }
        sb.append("id: ").append(id).append('\n');
        sb.append("class: ").append(fluid.getClass().getName()).append('\n');
        sb.append("fluidType: ").append(describeFluidType(fluid.getFluidType())).append('\n');
        sb.append("defaultState: ").append(fluid.defaultFluidState()).append('\n');
        sb.append("source: ").append(fluid.isSource(fluid.defaultFluidState())).append('\n');
        return HbmDebugWriter.write("fluid-dump", sb.toString());
    }

    private static String describeFluidType(final FluidType type) {
        if (type == null) {
            return "null";
        }
        final IForgeRegistry<FluidType> registry = ForgeRegistries.FLUID_TYPES.get();
        final ResourceLocation typeId = registry == null ? null : registry.getKey(type);
        return (typeId == null ? type.getClass().getSimpleName() : typeId.toString())
            + " desc=" + type.getDescriptionId();
    }
}

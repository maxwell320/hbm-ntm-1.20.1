package com.hbm.ntm.common.item;

import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class IcfPelletItem extends Item {
    private static final String TAG_TYPE_1 = "type1";
    private static final String TAG_TYPE_2 = "type2";
    private static final String TAG_MUON = "muon";
    private static final String TAG_DEPLETION = "depletion";
    private static final long BASE_MAX_DEPLETION = 50_000_000_000L;
    private static final long BASE_FUSING_DIFFICULTY = 10_000_000L;

    private static final Map<ResourceLocation, EnumICFFuel> FLUID_FUEL_MAP = new LinkedHashMap<>();
    private static final Map<Item, EnumICFFuel> MATERIAL_FUEL_MAP = new IdentityHashMap<>();
    private static boolean mappingsInitialized;

    public enum EnumICFFuel {
        HYDROGEN(0x4040FF, 1.00D, 0.85D, 1.00D),
        DEUTERIUM(0x2828CB, 1.25D, 1.00D, 1.00D),
        TRITIUM(0x000092, 1.50D, 1.00D, 1.05D),
        HELIUM3(0xFFF09F, 1.75D, 1.00D, 1.25D),
        HELIUM4(0xFF9B60, 2.00D, 1.00D, 1.50D),
        LITHIUM(0xE9E9E9, 1.25D, 0.85D, 2.00D),
        BERYLLIUM(0xA79D80, 2.00D, 1.00D, 2.50D),
        BORON(0x697F89, 3.00D, 0.50D, 3.50D),
        CARBON(0x454545, 2.00D, 1.00D, 5.00D),
        OXYGEN(0xB4E2FF, 1.25D, 1.50D, 7.50D),
        SODIUM(0xDFE4E7, 3.00D, 0.75D, 8.75D),
        CHLORINE(0xDAE598, 2.50D, 1.00D, 9.25D),
        CALCIUM(0xD2C7A9, 3.00D, 1.00D, 9.75D);

        private final int color;
        private final double reactionMultiplier;
        private final double depletionSpeed;
        private final double fusingDifficulty;

        EnumICFFuel(final int color, final double reactionMultiplier, final double depletionSpeed, final double fusingDifficulty) {
            this.color = color;
            this.reactionMultiplier = reactionMultiplier;
            this.depletionSpeed = depletionSpeed;
            this.fusingDifficulty = fusingDifficulty;
        }

        public int color() {
            return this.color;
        }

        public double reactionMultiplier() {
            return this.reactionMultiplier;
        }

        public double depletionSpeed() {
            return this.depletionSpeed;
        }

        public double fusingDifficulty() {
            return this.fusingDifficulty;
        }
    }

    public IcfPelletItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public static void ensureFuelMappings() {
        if (mappingsInitialized) {
            return;
        }
        mappingsInitialized = true;

        registerFluidFuel(HbmFluids.HYDROGEN, EnumICFFuel.HYDROGEN);
        registerFluidFuel(HbmFluids.DEUTERIUM, EnumICFFuel.DEUTERIUM);
        registerFluidFuel(HbmFluids.TRITIUM, EnumICFFuel.TRITIUM);
        registerFluidFuel(HbmFluids.HELIUM3, EnumICFFuel.HELIUM3);
        registerFluidFuel(HbmFluids.HELIUM4, EnumICFFuel.HELIUM4);
        registerFluidFuel(HbmFluids.OXYGEN, EnumICFFuel.OXYGEN);
        registerFluidFuel(HbmFluids.CHLORINE, EnumICFFuel.CHLORINE);

        registerMaterialFuel(HbmMaterials.LITHIUM, HbmMaterialShape.INGOT, EnumICFFuel.LITHIUM);
        registerMaterialFuel(HbmMaterials.BERYLLIUM, HbmMaterialShape.INGOT, EnumICFFuel.BERYLLIUM);
        registerMaterialFuel(HbmMaterials.BORON, HbmMaterialShape.INGOT, EnumICFFuel.BORON);
        registerMaterialFuel(HbmMaterials.GRAPHITE, HbmMaterialShape.INGOT, EnumICFFuel.CARBON);
        registerMaterialFuel(HbmMaterials.SODIUM, HbmMaterialShape.DUST, EnumICFFuel.SODIUM);
        registerMaterialFuel(HbmMaterials.CALCIUM, HbmMaterialShape.INGOT, EnumICFFuel.CALCIUM);
    }

    private static void registerFluidFuel(final HbmFluids.FluidEntry entry, final EnumICFFuel fuel) {
        registerFluidFuelId(ForgeRegistries.FLUIDS.getKey(entry.getStillFluid()), fuel);
        registerFluidFuelId(ForgeRegistries.FLUIDS.getKey(entry.getFlowingFluid()), fuel);
    }

    private static void registerFluidFuelId(final @Nullable ResourceLocation fluidId, final EnumICFFuel fuel) {
        if (fluidId == null) {
            return;
        }
        FLUID_FUEL_MAP.put(fluidId, fuel);
    }

    private static void registerMaterialFuel(final HbmMaterialDefinition material,
                                             final HbmMaterialShape shape,
                                             final EnumICFFuel fuel) {
        try {
            final Item item = Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
            MATERIAL_FUEL_MAP.put(item, fuel);
        } catch (final IllegalArgumentException ignored) {
            // Keep legacy fuel tables tolerant while upstream material surfaces are still being ported.
        }
    }

    public static @Nullable EnumICFFuel fuelForFluid(final FluidStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return fuelForFluid(stack.getFluid());
    }

    public static @Nullable EnumICFFuel fuelForFluid(final @Nullable Fluid fluid) {
        if (fluid == null) {
            return null;
        }
        ensureFuelMappings();
        final ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);
        if (key != null) {
            final EnumICFFuel direct = FLUID_FUEL_MAP.get(key);
            if (direct != null) {
                return direct;
            }
        }

        for (final Map.Entry<ResourceLocation, EnumICFFuel> entry : FLUID_FUEL_MAP.entrySet()) {
            final Fluid mappedFluid = ForgeRegistries.FLUIDS.getValue(entry.getKey());
            if (mappedFluid != null && fluid.isSame(mappedFluid)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static @Nullable EnumICFFuel fuelForMaterial(final ItemStack stack) {
        ensureFuelMappings();
        return stack.isEmpty() ? null : MATERIAL_FUEL_MAP.get(stack.getItem());
    }

    public static boolean isMaterialFuel(final ItemStack stack) {
        return fuelForMaterial(stack) != null;
    }

    public static ItemStack setup(final EnumICFFuel type1, final EnumICFFuel type2, final boolean muonCatalyzed) {
        return setup(new ItemStack(HbmItems.ICF_PELLET.get()), type1, type2, muonCatalyzed);
    }

    public static ItemStack setup(final ItemStack stack,
                                  final EnumICFFuel type1,
                                  final EnumICFFuel type2,
                                  final boolean muonCatalyzed) {
        final CompoundTag tag = stack.getOrCreateTag();
        tag.putByte(TAG_TYPE_1, (byte) type1.ordinal());
        tag.putByte(TAG_TYPE_2, (byte) type2.ordinal());
        tag.putBoolean(TAG_MUON, muonCatalyzed);
        return stack;
    }

    public static EnumICFFuel getType(final ItemStack stack, final boolean first) {
        final CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(first ? TAG_TYPE_1 : TAG_TYPE_2)) {
            return first ? EnumICFFuel.DEUTERIUM : EnumICFFuel.TRITIUM;
        }

        final int raw = Byte.toUnsignedInt(tag.getByte(first ? TAG_TYPE_1 : TAG_TYPE_2));
        final EnumICFFuel[] values = EnumICFFuel.values();
        if (raw < 0 || raw >= values.length) {
            return first ? EnumICFFuel.DEUTERIUM : EnumICFFuel.TRITIUM;
        }
        return values[raw];
    }

    public static boolean hasMuonCatalyst(final ItemStack stack) {
        final CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(TAG_MUON);
    }

    public static long getMaxDepletion(final ItemStack stack) {
        final EnumICFFuel type1 = getType(stack, true);
        final EnumICFFuel type2 = getType(stack, false);
        final double depletion = BASE_MAX_DEPLETION / type1.depletionSpeed() / type2.depletionSpeed();
        return Math.max(1L, Math.round(depletion));
    }

    public static long getFusingDifficulty(final ItemStack stack) {
        final EnumICFFuel type1 = getType(stack, true);
        final EnumICFFuel type2 = getType(stack, false);
        double difficulty = BASE_FUSING_DIFFICULTY * type1.fusingDifficulty() * type2.fusingDifficulty();
        if (hasMuonCatalyst(stack)) {
            difficulty /= 4.0D;
        }
        return Math.max(1L, Math.round(difficulty));
    }

    public static long getDepletion(final ItemStack stack) {
        final CompoundTag tag = stack.getTag();
        if (tag == null) {
            return 0L;
        }
        return Math.max(0L, tag.getLong(TAG_DEPLETION));
    }

    public static long react(final ItemStack stack, final long heat) {
        if (heat <= 0L) {
            return 0L;
        }
        final CompoundTag tag = stack.getOrCreateTag();
        tag.putLong(TAG_DEPLETION, Math.max(0L, tag.getLong(TAG_DEPLETION)) + heat);

        final EnumICFFuel type1 = getType(stack, true);
        final EnumICFFuel type2 = getType(stack, false);
        return Math.max(0L, Math.round(heat * type1.reactionMultiplier() * type2.reactionMultiplier()));
    }

    public static double getDepletionRatio(final ItemStack stack) {
        final long max = getMaxDepletion(stack);
        if (max <= 0L) {
            return 0.0D;
        }
        return Math.min(1.0D, Math.max(0.0D, (double) getDepletion(stack) / (double) max));
    }

    public static int getTintColor(final ItemStack stack, final int tintIndex) {
        if (tintIndex != 0) {
            return 0xFFFFFF;
        }

        final EnumICFFuel type1 = getType(stack, true);
        final EnumICFFuel type2 = getType(stack, false);
        final int r = (((type1.color() >>> 16) & 0xFF) + ((type2.color() >>> 16) & 0xFF)) / 2;
        final int g = (((type1.color() >>> 8) & 0xFF) + ((type2.color() >>> 8) & 0xFF)) / 2;
        final int b = ((type1.color() & 0xFF) + (type2.color() & 0xFF)) / 2;
        return (r << 16) | (g << 8) | b;
    }

    @Override
    public boolean isBarVisible(final ItemStack stack) {
        return getDepletionRatio(stack) > 0.0D;
    }

    @Override
    public int getBarWidth(final ItemStack stack) {
        final double ratio = getDepletionRatio(stack);
        return Math.max(0, Math.round((float) ((1.0D - ratio) * 13.0D)));
    }

    @Override
    public void appendHoverText(final ItemStack stack,
                                final @Nullable Level level,
                                final @NotNull List<net.minecraft.network.chat.Component> tooltip,
                                final @NotNull TooltipFlag flag) {
        final EnumICFFuel type1 = getType(stack, true);
        final EnumICFFuel type2 = getType(stack, false);

        tooltip.add(net.minecraft.network.chat.Component.literal(
            "Depletion: " + String.format(Locale.ROOT, "%.1f%%", getDepletionRatio(stack) * 100.0D)).withStyle(ChatFormatting.GREEN));
        tooltip.add(net.minecraft.network.chat.Component.literal(
            "Fuel: "
                + net.minecraft.network.chat.Component.translatable("icffuel." + type1.name().toLowerCase(Locale.ROOT)).getString()
                + " / "
                + net.minecraft.network.chat.Component.translatable("icffuel." + type2.name().toLowerCase(Locale.ROOT)).getString())
            .withStyle(ChatFormatting.YELLOW));
        tooltip.add(net.minecraft.network.chat.Component.literal("Heat required: "
            + String.format(Locale.ROOT, "%,d", getFusingDifficulty(stack)) + " TU").withStyle(ChatFormatting.YELLOW));
        tooltip.add(net.minecraft.network.chat.Component.literal(
            "Reactivity multiplier: x"
                + String.format(Locale.ROOT, "%.2f", type1.reactionMultiplier() * type2.reactionMultiplier()))
            .withStyle(ChatFormatting.YELLOW));
        if (hasMuonCatalyst(stack)) {
            tooltip.add(net.minecraft.network.chat.Component.literal("Muon catalyzed!").withStyle(ChatFormatting.DARK_AQUA));
        }
    }
}

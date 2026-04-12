package com.hbm.ntm.common.centrifuge;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("null")
public final class HbmGasCentrifugeRecipes {
    private static final ResourceLocation UF6_ID = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "uf6");
    private static final ResourceLocation PUF6_ID = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "puf6");
    private static final ResourceLocation WATZ_ID = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "watz");

    private HbmGasCentrifugeRecipes() {
    }

    public static Optional<PseudoFluidType> getPseudoInputForFluid(final ResourceLocation fluidId) {
        if (fluidId == null) {
            return Optional.empty();
        }
        if (fluidId.equals(UF6_ID)) {
            return Optional.of(PseudoFluidType.NUF6);
        }
        if (fluidId.equals(PUF6_ID)) {
            return Optional.of(PseudoFluidType.PF6);
        }
        if (fluidId.equals(WATZ_ID)) {
            return Optional.of(PseudoFluidType.MUD);
        }
        return Optional.empty();
    }

    public static boolean isSupportedFeedFluid(final ResourceLocation fluidId) {
        return getPseudoInputForFluid(fluidId).isPresent();
    }

    public enum PseudoFluidType {
        NONE("none", 0, 0, null, false, 0x00000000),
        HEUF6("heuf6", 300, 0, NONE, true, 0xFFD1CEBE,
            material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 2),
            material(HbmMaterials.U235, HbmMaterialShape.NUGGET, 1),
            material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 1)),
        MEUF6("meuf6", 200, 100, HEUF6, false, 0xFFD1CEBE,
            material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 1)),
        LEUF6("leuf6", 300, 200, MEUF6, false, 0xFFD1CEBE,
            material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 1),
            material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 1)),
        NUF6("nuf6", 400, 300, LEUF6, false, 0xFFD1CEBE,
            material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 1)),
        PF6("pf6", 300, 0, NONE, false, 0xFF4C4C4C,
            material(HbmMaterials.PU238, HbmMaterialShape.NUGGET, 1),
            material(HbmMaterials.PU_MIX, HbmMaterialShape.NUGGET, 2),
            material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 1)),
        MUD_HEAVY("mud_heavy", 500, 0, NONE, false, 0xFF86653E,
            material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1),
            hbmItem("dust", 1),
            item(HbmItems.NUCLEAR_WASTE_TINY, 1)),
        MUD("mud", 1000, 500, MUD_HEAVY, false, 0xFF86653E,
            material(HbmMaterials.LEAD, HbmMaterialShape.DUST, 1),
            hbmItem("dust", 1));

        private final String name;
        private final int fluidConsumed;
        private final int fluidProduced;
        private final PseudoFluidType outputType;
        private final boolean requiresHighSpeed;
        private final int color;
        private final List<ItemStack> outputs;

        PseudoFluidType(final String name,
                        final int fluidConsumed,
                        final int fluidProduced,
                        final PseudoFluidType outputType,
                        final boolean requiresHighSpeed,
                        final int color,
                        final ItemStack... outputs) {
            this.name = name;
            this.fluidConsumed = fluidConsumed;
            this.fluidProduced = fluidProduced;
            this.outputType = outputType;
            this.requiresHighSpeed = requiresHighSpeed;
            this.color = color;
            this.outputs = java.util.Arrays.stream(outputs)
                .filter(stack -> stack != null && !stack.isEmpty())
                .map(ItemStack::copy)
                .collect(Collectors.toUnmodifiableList());
        }

        public int fluidConsumed() {
            return this.fluidConsumed;
        }

        public int fluidProduced() {
            return this.fluidProduced;
        }

        public PseudoFluidType outputType() {
            return this.outputType == null ? NONE : this.outputType;
        }

        public boolean requiresHighSpeed() {
            return this.requiresHighSpeed;
        }

        public int color() {
            return this.color;
        }

        public String translationKey() {
            return "hbmpseudofluid." + this.name;
        }

        public List<ItemStack> outputs() {
            return this.outputs.stream().map(ItemStack::copy).toList();
        }

        public static PseudoFluidType byName(final String name) {
            if (name == null || name.isBlank()) {
                return NONE;
            }
            for (final PseudoFluidType value : values()) {
                if (value.name().equalsIgnoreCase(name) || value.name.equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return NONE;
        }
    }

    private static ItemStack material(final HbmMaterialDefinition material, final HbmMaterialShape shape, final int count) {
        try {
            final Item item = Objects.requireNonNull(HbmItems.getMaterialPart(material, shape).get());
            return new ItemStack(item, count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack item(final net.minecraftforge.registries.RegistryObject<Item> item, final int count) {
        try {
            return new ItemStack(Objects.requireNonNull(item.get()), count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack hbmItem(final String path, final int count) {
        return itemById(HbmNtmMod.MOD_ID + ":" + path, count);
    }

    private static ItemStack itemById(final String itemId, final int count) {
        if (itemId == null || itemId.isBlank()) {
            return ItemStack.EMPTY;
        }
        final ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) {
            return ItemStack.EMPTY;
        }
        try {
            final Item resolved = Objects.requireNonNull(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id));
            return new ItemStack(resolved, count);
        } catch (final RuntimeException ignored) {
            return ItemStack.EMPTY;
        }
    }
}

package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.BarrelBlockEntity;
import com.hbm.ntm.common.fluid.HbmFluidType;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class BarrelMenu extends AbstractContainerMenu {
    private static final int MACHINE_SLOT_COUNT = 6;

    private final BarrelBlockEntity barrel;
    private final ContainerLevelAccess access;

    public BarrelMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final BarrelBlockEntity barrel ? barrel : null,
            ContainerLevelAccess.NULL, BlockPos.ZERO);
    }

    public BarrelMenu(final int containerId, final Inventory inventory, final BarrelBlockEntity barrel, final ContainerLevelAccess access, final BlockPos pos) {
        super(HbmMenuTypes.BARREL.get(), containerId);
        this.barrel = barrel;
        this.access = access;
        if (barrel != null) {
            this.addSlot(new SlotItemHandler(barrel.getItemHandler(), 0, 8, 17));
            this.addSlot(new SlotItemHandler(barrel.getItemHandler(), 1, 8, 53));
            this.addSlot(new SlotItemHandler(barrel.getItemHandler(), 2, 35, 17));
            this.addSlot(new SlotItemHandler(barrel.getItemHandler(), 3, 35, 53));
            this.addSlot(new SlotItemHandler(barrel.getItemHandler(), 4, 125, 17));
            this.addSlot(new SlotItemHandler(barrel.getItemHandler(), 5, 125, 53));
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new net.minecraft.world.inventory.Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int hotbar = 0; hotbar < 9; hotbar++) {
            this.addSlot(new net.minecraft.world.inventory.Slot(inventory, hotbar, 8 + hotbar * 18, 142));
        }
    }

    @Override
    public boolean stillValid(final @NotNull Player player) {
        return this.barrel != null && this.barrel.stillUsableByPlayer(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(final @NotNull Player player, final int index) {
        if (index < 0 || index >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        final int playerStart = this.barrel == null ? 0 : Math.max(0, Math.min(MACHINE_SLOT_COUNT, this.slots.size()));

        final net.minecraft.world.inventory.Slot slot = this.slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }

        final ItemStack original = slot.getItem();
        final ItemStack copy = original.copy();
        if (index < playerStart) {
            if (!this.moveItemStackTo(original, playerStart, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (playerStart <= 0 || !this.moveItemStackTo(original, 0, playerStart, false)) {
            return ItemStack.EMPTY;
        }
        if (original.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (original.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, original);
        return copy;
    }

    @Override
    public boolean clickMenuButton(final @NotNull Player player, final int buttonId) {
        if (buttonId != 0 || this.barrel == null) {
            return false;
        }
        this.barrel.cycleMode();
        return true;
    }

    public int fluidAmount() {
        return this.barrel == null ? 0 : this.barrel.getFluidAmount();
    }

    public int capacity() {
        return this.barrel == null ? 0 : this.barrel.capacity();
    }

    public String fluidName() {
        return this.barrel == null ? "" : this.barrel.getConfiguredFluidDisplayName();
    }

    public int mode() {
        return this.barrel == null ? 0 : this.barrel.getMode();
    }

    public String modeName() {
        return switch (this.mode()) {
            case 0 -> "Receive";
            case 1 -> "Buffer";
            case 2 -> "Provide";
            case 3 -> "Disabled";
            default -> "Unknown";
        };
    }

    public int fluidColor() {
        if (this.barrel == null) {
            return 0xFF3F76E4;
        }
        final FluidStack fluidStack = this.barrel.getFluid();
        if (!fluidStack.isEmpty()) {
            return colorFor(fluidStack.getFluid().getFluidType());
        }
        final ResourceLocation configuredFluid = this.barrel.getConfiguredFluidId();
        if (configuredFluid != null && ForgeRegistries.FLUIDS.containsKey(configuredFluid)) {
            final var fluid = ForgeRegistries.FLUIDS.getValue(configuredFluid);
            if (fluid != null) {
                return colorFor(fluid.getFluidType());
            }
        }
        return 0xFF3F76E4;
    }

    private static int colorFor(final net.minecraftforge.fluids.FluidType fluidType) {
        final int tint = fluidType instanceof HbmFluidType hbmFluidType ? hbmFluidType.getTintColor() : 0xFF3F76E4;
        return tint | 0xFF000000;
    }
}

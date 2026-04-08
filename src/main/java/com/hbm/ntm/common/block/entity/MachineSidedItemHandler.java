package com.hbm.ntm.common.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

@SuppressWarnings("null")
public class MachineSidedItemHandler implements IItemHandler {
    private final MachineBlockEntity machine;
    private final Direction side;

    public MachineSidedItemHandler(final MachineBlockEntity machine, final Direction side) {
        this.machine = machine;
        this.side = side;
    }

    @Override
    public int getSlots() {
        return this.machine.getInternalItemHandler().getSlots();
    }

    @Override
    public ItemStack getStackInSlot(final int slot) {
        return this.machine.getInternalItemHandler().getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
        if (stack.isEmpty() || !this.machine.canInsertIntoSlot(slot, stack, this.side)) {
            return stack;
        }
        return this.machine.getInternalItemHandler().insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
        if (amount <= 0 || !this.machine.canExtractFromSlot(slot, this.side)) {
            return ItemStack.EMPTY;
        }
        return this.machine.getInternalItemHandler().extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(final int slot) {
        return this.machine.getInternalItemHandler().getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(final int slot, final ItemStack stack) {
        return this.machine.canInsertIntoSlot(slot, stack, this.side);
    }
}

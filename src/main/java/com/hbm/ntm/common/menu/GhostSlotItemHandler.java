package com.hbm.ntm.common.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

@SuppressWarnings("null")
public class GhostSlotItemHandler extends SlotItemHandler {
    private final boolean allowStackSize;
    private boolean highlightable = true;

    public GhostSlotItemHandler(final IItemHandler itemHandler,
                                final int index,
                                final int xPosition,
                                final int yPosition) {
        this(itemHandler, index, xPosition, yPosition, false);
    }

    public GhostSlotItemHandler(final IItemHandler itemHandler,
                                final int index,
                                final int xPosition,
                                final int yPosition,
                                final boolean allowStackSize) {
        super(itemHandler, index, xPosition, yPosition);
        this.allowStackSize = allowStackSize;
    }

    @Override
    public boolean mayPickup(final Player player) {
        return false;
    }

    @Override
    public boolean mayPlace(final ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return this.allowStackSize ? super.getMaxStackSize() : 1;
    }

    @Override
    public void set(final ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            super.set(ItemStack.EMPTY);
            return;
        }

        final ItemStack copied = stack.copy();
        if (!this.allowStackSize) {
            copied.setCount(1);
        }
        super.set(copied);
    }

    public GhostSlotItemHandler disableHighlight() {
        this.highlightable = false;
        return this;
    }

    @Override
    public boolean isHighlightable() {
        return this.highlightable;
    }
}
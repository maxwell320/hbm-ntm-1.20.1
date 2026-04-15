package com.hbm.ntm.common.menu;

import java.util.function.BiPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

@SuppressWarnings("null")
public class FilteredSlotItemHandler extends SlotItemHandler {
    private final BiPredicate<Integer, ItemStack> validator;

    public FilteredSlotItemHandler(final IItemHandler itemHandler, final int index, final int xPosition, final int yPosition,
                                   final BiPredicate<Integer, ItemStack> validator) {
        super(itemHandler, index, xPosition, yPosition);
        this.validator = validator;
    }

    @Override
    public boolean mayPlace(final ItemStack stack) {
        return super.mayPlace(stack) && this.validator.test(this.getSlotIndex(), stack);
    }
}

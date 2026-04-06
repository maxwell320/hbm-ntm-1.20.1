package com.hbm.ntm.common.fluid;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

@SuppressWarnings("null")
public class HbmFluidTank extends FluidTank {
    private final Runnable changeListener;

    public HbmFluidTank(final int capacity) {
        this(capacity, fluidStack -> true, () -> {
        });
    }

    public HbmFluidTank(final int capacity, final Predicate<FluidStack> validator) {
        this(capacity, validator, () -> {
        });
    }

    public HbmFluidTank(final int capacity, final Predicate<FluidStack> validator, final Runnable changeListener) {
        super(capacity, validator);
        this.changeListener = Objects.requireNonNull(changeListener);
    }

    @Override
    public FluidTank setCapacity(final int capacity) {
        this.capacity = Math.max(0, capacity);
        if (!this.fluid.isEmpty() && this.fluid.getAmount() > this.capacity) {
            this.fluid.setAmount(this.capacity);
        }
        this.onContentsChanged();
        return this;
    }

    public void setFluidAmount(final int amount) {
        if (this.fluid.isEmpty()) {
            return;
        }
        this.fluid.setAmount(Math.max(0, Math.min(amount, this.capacity)));
        if (this.fluid.getAmount() == 0) {
            this.fluid = FluidStack.EMPTY;
        }
        this.onContentsChanged();
    }

    public void setFluidStack(final FluidStack stack) {
        if (stack.isEmpty()) {
            this.fluid = FluidStack.EMPTY;
            this.onContentsChanged();
            return;
        }
        if (!this.isFluidValid(stack)) {
            throw new IllegalArgumentException("Invalid fluid for this tank: " + stack.getFluid());
        }
        this.fluid = stack.copy();
        if (this.fluid.getAmount() > this.capacity) {
            this.fluid.setAmount(this.capacity);
        }
        this.onContentsChanged();
    }

    @Override
    protected void onContentsChanged() {
        this.changeListener.run();
    }
}

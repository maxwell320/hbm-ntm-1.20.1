package com.hbm.ntm.common.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public record SidedFluidHandler(IFluidHandler delegate, boolean canFill, boolean canDrain) implements IFluidHandler {
    @Override
    public int getTanks() {
        return this.delegate.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(final int tank) {
        return this.delegate.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(final int tank) {
        return this.delegate.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(final int tank, final FluidStack stack) {
        return this.delegate.isFluidValid(tank, stack);
    }

    @Override
    public int fill(final FluidStack resource, final FluidAction action) {
        return this.canFill ? this.delegate.fill(resource, action) : 0;
    }

    @Override
    public FluidStack drain(final FluidStack resource, final FluidAction action) {
        return this.canDrain ? this.delegate.drain(resource, action) : FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(final int maxDrain, final FluidAction action) {
        return this.canDrain ? this.delegate.drain(maxDrain, action) : FluidStack.EMPTY;
    }
}

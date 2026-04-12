package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.block.IcfLaserComponentBlock;
import com.hbm.ntm.common.block.IcfLaserComponentPart;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class IcfLaserComponentBlockEntity extends net.minecraft.world.level.block.entity.BlockEntity {
    private static final BlockPos NO_CONTROLLER = new BlockPos(0, 0, 0);

    private BlockPos controllerPos = NO_CONTROLLER;
    private LazyOptional<IEnergyStorage> energyCapability = LazyOptional.empty();

    public IcfLaserComponentBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.ICF_LASER_COMPONENT.get(), pos, state);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final IcfLaserComponentBlockEntity component) {
        if (level.getGameTime() % 20L != 0L) {
            return;
        }
        if (!component.hasController()) {
            return;
        }
        final IcfControllerBlockEntity controller = component.getController();
        if (controller == null || !controller.isAssembled()) {
            component.clearControllerPos();
            component.setChanged();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.createCapabilities();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyCapability.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.createCapabilities();
    }

    @Override
    protected void saveAdditional(final CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.hasController()) {
            tag.putInt("controllerX", this.controllerPos.getX());
            tag.putInt("controllerY", this.controllerPos.getY());
            tag.putInt("controllerZ", this.controllerPos.getZ());
        }
    }

    @Override
    public void load(final CompoundTag tag) {
        super.load(tag);
        if (tag.contains("controllerX") && tag.contains("controllerY") && tag.contains("controllerZ")) {
            this.controllerPos = new BlockPos(tag.getInt("controllerX"), tag.getInt("controllerY"), tag.getInt("controllerZ"));
        } else {
            this.controllerPos = NO_CONTROLLER;
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side) {
        if (capability == ForgeCapabilities.ENERGY && this.isPort()) {
            return this.energyCapability.cast();
        }
        return super.getCapability(capability, side);
    }

    public void notifyControllerOfBreak() {
        final IcfControllerBlockEntity controller = this.getController();
        if (controller != null) {
            controller.onComponentBroken(this.worldPosition);
        }
    }

    @Override
    public void setRemoved() {
        if (this.level != null && !this.level.isClientSide() && !this.level.getBlockState(this.worldPosition).is(HbmBlocks.MACHINE_ICF_LASER_COMPONENT.get())) {
            this.notifyControllerOfBreak();
        }
        super.setRemoved();
    }

    public boolean hasController() {
        return !this.controllerPos.equals(NO_CONTROLLER);
    }

    public @Nullable BlockPos getControllerPos() {
        return this.hasController() ? this.controllerPos : null;
    }

    public void setControllerPos(final BlockPos pos) {
        this.controllerPos = pos.immutable();
        this.setChanged();
    }

    public void clearControllerPos() {
        this.controllerPos = NO_CONTROLLER;
        this.setChanged();
    }

    public boolean isPort() {
        return this.getPart() == IcfLaserComponentPart.PORT;
    }

    private IcfLaserComponentPart getPart() {
        final BlockState state = this.getBlockState();
        return state.getBlock() instanceof IcfLaserComponentBlock
            ? state.getValue(IcfLaserComponentBlock.PART)
            : IcfLaserComponentPart.CASING;
    }

    private @Nullable IcfControllerBlockEntity getController() {
        if (!this.hasController() || this.level == null) {
            return null;
        }
        return this.level.getBlockEntity(this.controllerPos) instanceof final IcfControllerBlockEntity controller ? controller : null;
    }

    private void createCapabilities() {
        this.energyCapability = LazyOptional.of(() -> new PortEnergyStorage(this));
    }

    private static final class PortEnergyStorage implements IEnergyStorage {
        private final IcfLaserComponentBlockEntity component;

        private PortEnergyStorage(final IcfLaserComponentBlockEntity component) {
            this.component = component;
        }

        @Override
        public int receiveEnergy(final int maxReceive, final boolean simulate) {
            if (!this.component.isPort()) {
                return 0;
            }
            final IcfControllerBlockEntity controller = this.component.getController();
            if (controller == null) {
                return 0;
            }
            return controller.receivePortEnergy(maxReceive, this.component.worldPosition, simulate);
        }

        @Override
        public int extractEnergy(final int maxExtract, final boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            final IcfControllerBlockEntity controller = this.component.getController();
            return controller == null ? 0 : controller.getPortEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            final IcfControllerBlockEntity controller = this.component.getController();
            return controller == null ? 0 : controller.getPortEnergyCapacity();
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return this.component.isPort();
        }
    }
}
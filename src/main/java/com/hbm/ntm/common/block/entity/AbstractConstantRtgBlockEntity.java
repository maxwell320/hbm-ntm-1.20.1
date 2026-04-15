package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.EnergyNetworkDistributor;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.energy.IEnergyGenerator;
import com.hbm.ntm.common.energy.IEnergyNetworkProvider;
import com.hbm.ntm.common.energy.IEnergyUser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public abstract class AbstractConstantRtgBlockEntity extends BlockEntity implements IEnergyUser, IEnergyGenerator, IEnergyNetworkProvider {
    private final int outputPerTick;
    private final HbmEnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> energyCapability = LazyOptional.empty();

    protected AbstractConstantRtgBlockEntity(final BlockEntityType<?> type,
                                             final BlockPos pos,
                                             final BlockState state,
                                             final int outputPerTick,
                                             final int capacity) {
        super(type, pos, state);
        this.outputPerTick = Math.max(1, outputPerTick);
        this.energyStorage = new ConstantRtgEnergyStorage(Math.max(1, capacity));
    }

    public static <T extends AbstractConstantRtgBlockEntity> void serverTick(final Level level,
                                                                              final BlockPos pos,
                                                                              final BlockState state,
                                                                              final T blockEntity) {
        blockEntity.tickServer(level, pos);
    }

    public int getComparatorOutput() {
        final int stored = this.energyStorage.getEnergyStored();
        if (stored <= 0) {
            return 0;
        }
        return Math.min(15, Math.max(1, (int) Math.ceil(stored * 15.0D / this.energyStorage.getMaxEnergyStored())));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.energyCapability = LazyOptional.of(() -> this.energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyCapability.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.energyCapability = LazyOptional.of(() -> this.energyStorage);
    }

    @Override
    protected void saveAdditional(final CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy", this.energyStorage.serializeNBT());
    }

    @Override
    public void load(final CompoundTag tag) {
        super.load(tag);
        final Tag energyTag = tag.get("energy");
        if (energyTag != null) {
            this.energyStorage.deserializeNBT(energyTag);
        }
    }

    @Override
    public EnergyConnectionMode getEnergyConnection(@Nullable final Direction side) {
        return EnergyConnectionMode.EXTRACT;
    }

    @Override
    public @Nullable IEnergyStorage getEnergyStorage(@Nullable final Direction side) {
        return this.energyStorage;
    }

    @Override
    public long getAvailableNetworkEnergy(@Nullable final Direction side) {
        return this.energyStorage.extractEnergy(Integer.MAX_VALUE, true);
    }

    @Override
    public void consumeNetworkEnergy(@Nullable final Direction side, final long amount) {
        if (amount > 0) {
            this.energyStorage.extractEnergy((int) Math.min(Integer.MAX_VALUE, amount), false);
        }
    }

    @Override
    public long getNetworkProviderSpeed(@Nullable final Direction side) {
        return this.energyStorage.getMaxExtract();
    }

    public int getOutputPerTick() {
        return this.outputPerTick;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(final @NotNull Capability<T> cap, final @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return this.energyCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    protected final void tickServer(final Level level, final BlockPos pos) {
        final int current = this.energyStorage.getEnergyStored();
        final int target = Math.min(this.energyStorage.getMaxEnergyStored(), current + this.outputPerTick);
        if (target != current) {
            this.energyStorage.setEnergy(target);
        }

        final int available = Math.min(this.energyStorage.getEnergyStored(), this.energyStorage.extractEnergy(Integer.MAX_VALUE, true));
        if (available <= 0) {
            return;
        }

        final int transferred = EnergyNetworkDistributor.distribute(level, pos, available, available, null);
        if (transferred > 0) {
            this.energyStorage.extractEnergy(transferred, false);
        }
    }

    private final class ConstantRtgEnergyStorage extends HbmEnergyStorage {
        private ConstantRtgEnergyStorage(final int capacity) {
            super(capacity, 0, capacity);
        }

        @Override
        protected void onEnergyChanged() {
            AbstractConstantRtgBlockEntity.this.setChanged();
            if (AbstractConstantRtgBlockEntity.this.level != null) {
                final BlockState state = AbstractConstantRtgBlockEntity.this.getBlockState();
                AbstractConstantRtgBlockEntity.this.level.updateNeighbourForOutputSignal(AbstractConstantRtgBlockEntity.this.worldPosition, state.getBlock());
            }
        }
    }
}

package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.fluid.IFluidNetworkReceiver;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.transfer.TransferGraphManager;
import com.hbm.ntm.common.transfer.TransferNetworkKind;
import com.hbm.ntm.common.transfer.TransferNodeProvider;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class FluidDuctBlockEntity extends BlockEntity implements TransferNodeProvider {
    public static final int BUFFER_CAPACITY = 1_000;
    public static final int TRANSFER_PER_TICK = 250;
    private static final String CONFIGURED_FLUID_TAG = "configured_fluid";
    private final HbmFluidTank buffer = new HbmFluidTank(BUFFER_CAPACITY, this::isValidFluid, this::onContentsChanged);
    @Nullable
    private ResourceLocation configuredFluidId;
    @Nullable
    private Direction lastPullDirection;
    private long lastPullTick = Long.MIN_VALUE;

    public FluidDuctBlockEntity(final BlockPos pos, final BlockState state) {
        this(HbmBlockEntityTypes.FLUID_DUCT.get(), pos, state);
    }

    protected FluidDuctBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
        super(type, pos, state);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final FluidDuctBlockEntity blockEntity) {
        TransferGraphManager.rebuildIfDirty(level, pos, TransferNetworkKind.FLUID);
        blockEntity.pushFluid(level, pos);
        blockEntity.pullFluid(level, pos);
    }

    @Override
    public TransferNetworkKind getTransferNetworkKind() {
        return TransferNetworkKind.FLUID;
    }

    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.allOf(Direction.class);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide) {
            TransferGraphManager.markDirty(this.level, this.worldPosition, TransferNetworkKind.FLUID);
        }
    }

    @Override
    public void invalidateCaps() {
        if (this.level != null && !this.level.isClientSide) {
            TransferGraphManager.markDirty(this.level, this.worldPosition, TransferNetworkKind.FLUID);
            TransferGraphManager.removeNode(this.level, this.worldPosition, TransferNetworkKind.FLUID);
        }
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(final CompoundTag tag) {
        super.saveAdditional(tag);
        final CompoundTag tankTag = new CompoundTag();
        this.buffer.writeToNBT(tankTag);
        tag.put("buffer", tankTag);
        if (this.configuredFluidId != null) {
            tag.putString(CONFIGURED_FLUID_TAG, this.configuredFluidId.toString());
        }
    }

    @Override
    public void load(final CompoundTag tag) {
        super.load(tag);
        if (tag.contains("buffer", CompoundTag.TAG_COMPOUND)) {
            this.buffer.readFromNBT(tag.getCompound("buffer"));
        }
        this.configuredFluidId = tag.contains(CONFIGURED_FLUID_TAG) ? ResourceLocation.tryParse(tag.getString(CONFIGURED_FLUID_TAG)) : null;
    }

    public @Nullable ResourceLocation getConfiguredFluidId() {
        return this.configuredFluidId;
    }

    public void setConfiguredFluidId(@Nullable final ResourceLocation configuredFluidId) {
        if (Objects.equals(this.configuredFluidId, configuredFluidId)) {
            return;
        }
        this.configuredFluidId = configuredFluidId;
        if (!this.buffer.isEmpty() && !this.isValidFluid(this.buffer.getFluid())) {
            this.buffer.setFluidStack(FluidStack.EMPTY);
        }
        if (this.level != null && !this.level.isClientSide) {
            TransferGraphManager.markDirty(this.level, this.worldPosition, TransferNetworkKind.FLUID);
        }
        this.onContentsChanged();
    }

    @Override
    public boolean canConnectTo(final Level level, final BlockPos pos, final Direction side, final BlockEntity neighbor) {
        if (!(neighbor instanceof final FluidDuctBlockEntity duct)) {
            return true;
        }
        return this.configuredFluidId == null
            || duct.configuredFluidId == null
            || Objects.equals(this.configuredFluidId, duct.configuredFluidId);
    }

    private void pullFluid(final Level level, final BlockPos pos) {
        if (this.buffer.getFluidAmount() >= BUFFER_CAPACITY) {
            return;
        }
        for (final Direction direction : Direction.values()) {
            final BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor == null || neighbor instanceof FluidDuctBlockEntity) {
                continue;
            }
            if (!this.canPull(direction, neighbor)) {
                continue;
            }
            final IFluidHandler handler = neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).orElse(null);
            if (handler == null) {
                continue;
            }
            for (int tank = 0; tank < handler.getTanks(); tank++) {
                final FluidStack simulated = handler.drain(Math.min(TRANSFER_PER_TICK, BUFFER_CAPACITY - this.buffer.getFluidAmount()), IFluidHandler.FluidAction.SIMULATE);
                if (simulated.isEmpty() || !this.isValidFluid(simulated)) {
                    continue;
                }
                final int accepted = this.buffer.fill(simulated, IFluidHandler.FluidAction.EXECUTE);
                if (accepted <= 0) {
                    continue;
                }
                handler.drain(new FluidStack(simulated, accepted), IFluidHandler.FluidAction.EXECUTE);
                this.lastPullDirection = direction;
                this.lastPullTick = level.getGameTime();
                return;
            }
        }
    }

    private void pushFluid(final Level level, final BlockPos pos) {
        if (this.buffer.isEmpty()) {
            return;
        }
        final FluidStack stored = this.buffer.getFluid().copy();
        for (final Direction direction : Direction.values()) {
            if (stored.isEmpty()) {
                return;
            }
            if (this.lastPullDirection == direction && this.lastPullTick == level.getGameTime()) {
                continue;
            }
            final BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor == null) {
                continue;
            }
            if (!this.canPush(direction, neighbor)) {
                continue;
            }
            if (neighbor instanceof final FluidDuctBlockEntity duct) {
                if (!Objects.equals(this.configuredFluidId, duct.configuredFluidId) && duct.configuredFluidId != null) {
                    continue;
                }
                final int moved = duct.receiveFromDuct(stored, TRANSFER_PER_TICK, direction.getOpposite());
                if (moved > 0) {
                    TransferGraphManager.recordTransfer(level, pos, TransferNetworkKind.FLUID, moved);
                    this.buffer.drain(moved, IFluidHandler.FluidAction.EXECUTE);
                    stored.shrink(moved);
                }
                continue;
            }
            final int moved = this.pushToNeighbor(neighbor, stored, direction.getOpposite());
            if (moved > 0) {
                TransferGraphManager.recordTransfer(level, pos, TransferNetworkKind.FLUID, moved);
                this.buffer.drain(moved, IFluidHandler.FluidAction.EXECUTE);
                stored.shrink(moved);
            }
        }
    }

    private int pushToNeighbor(final BlockEntity neighbor, final FluidStack stored, final Direction side) {
        if (stored.isEmpty()) {
            return 0;
        }
        final int offer = Math.min(TRANSFER_PER_TICK, stored.getAmount());
        if (offer <= 0) {
            return 0;
        }

        if (neighbor instanceof final IFluidNetworkReceiver receiver) {
            final long demandValue = Math.min(receiver.getNetworkFluidDemand(stored, 0, side), receiver.getFluidReceiverSpeed(stored, 0, side));
            final int demand = (int) Math.min(Integer.MAX_VALUE, demandValue);
            if (demand <= 0) {
                return 0;
            }
            final int transfer = Math.min(offer, demand);
            final long remainder = receiver.receiveNetworkFluid(new FluidStack(stored, transfer), 0, transfer, side);
            return Math.max(0, transfer - (int) Math.min(Integer.MAX_VALUE, remainder));
        }

        final IFluidHandler handler = neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, side).orElse(null);
        if (handler == null) {
            return 0;
        }
        return handler.fill(new FluidStack(stored, offer), IFluidHandler.FluidAction.EXECUTE);
    }

    protected int receiveFromDuct(final FluidStack stack, final int maxAmount, final Direction fromDirection) {
        if (stack.isEmpty() || !this.isValidFluid(stack) || !this.canReceive(fromDirection, stack)) {
            return 0;
        }
        final int amount = Math.min(maxAmount, stack.getAmount());
        return this.buffer.fill(new FluidStack(stack, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    protected boolean canPull(final Direction direction, final BlockEntity neighbor) {
        return true;
    }

    protected boolean canPush(final Direction direction, final BlockEntity neighbor) {
        return true;
    }

    protected boolean canReceive(final Direction direction, final FluidStack stack) {
        return true;
    }

    private boolean isValidFluid(final FluidStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        if (this.configuredFluidId == null) {
            return true;
        }
        if (!ForgeRegistries.FLUIDS.containsKey(this.configuredFluidId)) {
            return false;
        }
        return ForgeRegistries.FLUIDS.getValue(this.configuredFluidId) == stack.getFluid();
    }

    private void onContentsChanged() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }
}

package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.fluid.FluidNetworkPriority;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.pollution.PollutionSavedData;
import com.hbm.ntm.common.pollution.PollutionType;
import com.hbm.ntm.common.registration.HbmFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public abstract class ChimneyBlockEntity extends MachineBlockEntity {
    protected static final int SLOT_COUNT = 0;

    private static final int TANK_SMOKE = 0;
    private static final int TANK_SMOKE_LEADED = 1;
    private static final int TANK_SMOKE_POISON = 2;
    private static final int TANK_CAPACITY = 1_000_000;

    private static final int[] ACCESSIBLE_SLOTS = {};

    private long pendingFlyAsh;
    private long pendingSootAsh;
    private int activeTicks;

    protected ChimneyBlockEntity(final net.minecraft.world.level.block.entity.BlockEntityType<?> type,
                                 final BlockPos pos,
                                 final BlockState state) {
        super(type, pos, state, SLOT_COUNT);
    }

    protected abstract float pollutionModifier();

    protected boolean capturesFlyAsh() {
        return true;
    }

    protected boolean capturesSootAsh() {
        return false;
    }

    public static <T extends ChimneyBlockEntity> void serverTick(final Level level,
                                                                  final BlockPos pos,
                                                                  final BlockState state,
                                                                  final T chimney) {
        chimney.tickServer();
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        return new HbmFluidTank[] {
            this.createFluidTank(TANK_CAPACITY, this::isSmokeFluid),
            this.createFluidTank(TANK_CAPACITY, this::isLeadedSmokeFluid),
            this.createFluidTank(TANK_CAPACITY, this::isPoisonSmokeFluid)
        };
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull net.minecraft.world.item.ItemStack stack) {
        return false;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull net.minecraft.world.item.ItemStack stack, final @Nullable Direction side) {
        return false;
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return false;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return ACCESSIBLE_SLOTS;
    }

    @Override
    protected boolean canFillFromSide(final Direction side) {
        return true;
    }

    @Override
    protected boolean canDrainFromSide(final Direction side) {
        return false;
    }

    @Override
    public FluidNetworkPriority getFluidNetworkPriority() {
        return FluidNetworkPriority.HIGH;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(final int containerId,
                                                      final @NotNull Inventory inventory,
                                                      final @NotNull Player player) {
        return null;
    }

    public int getActiveTicks() {
        return this.activeTicks;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putLong("pendingFlyAsh", this.pendingFlyAsh);
        tag.putLong("pendingSootAsh", this.pendingSootAsh);
        tag.putInt("activeTicks", this.activeTicks);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.pendingFlyAsh = tag.getLong("pendingFlyAsh");
        this.pendingSootAsh = tag.getLong("pendingSootAsh");
        this.activeTicks = Math.max(0, tag.getInt("activeTicks"));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("activeTicks", this.activeTicks);
    }

    protected final void tickServer() {
        this.consumeSmokeTank(TANK_SMOKE, PollutionType.SOOT);
        this.consumeSmokeTank(TANK_SMOKE_LEADED, PollutionType.HEAVY_METAL);
        this.consumeSmokeTank(TANK_SMOKE_POISON, PollutionType.POISON);

        if (this.pendingFlyAsh > 0L || this.pendingSootAsh > 0L) {
            final net.minecraft.world.level.block.entity.BlockEntity below = this.level == null
                ? null
                : this.level.getBlockEntity(this.worldPosition.below());
            if (below instanceof final AshpitBlockEntity ashpit) {
                ashpit.addFlyAsh(this.pendingFlyAsh);
                ashpit.addSootAsh(this.pendingSootAsh);
            }
            this.pendingFlyAsh = 0L;
            this.pendingSootAsh = 0L;
        }

        if (this.activeTicks > 0) {
            this.activeTicks--;
        }

        this.tickMachineStateSync();
    }

    private void consumeSmokeTank(final int tankIndex, final PollutionType pollutionType) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }

        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        if (tank == null || tank.isEmpty()) {
            return;
        }

        final FluidStack drained = tank.drain(tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        if (drained.isEmpty()) {
            return;
        }

        final int amount = drained.getAmount();
        if (amount <= 0) {
            return;
        }

        this.activeTicks = 20;

        if (this.capturesFlyAsh()) {
            this.pendingFlyAsh += amount;
        }
        if (this.capturesSootAsh()) {
            this.pendingSootAsh += amount;
        }

        final float pollution = (amount / 100.0F) * this.pollutionModifier();
        if (pollution > 0.0F) {
            PollutionSavedData.incrementPollution(
                this.level,
                this.worldPosition.getX(),
                this.worldPosition.getY(),
                this.worldPosition.getZ(),
                pollutionType,
                pollution);
        }
    }

    private boolean isSmokeFluid(final FluidStack stack) {
        final Fluid smoke = HbmFluids.SMOKE.getStillFluid();
        return smoke != null && (stack.isEmpty() || stack.getFluid() == smoke);
    }

    private boolean isLeadedSmokeFluid(final FluidStack stack) {
        final Fluid smokeLeaded = HbmFluids.SMOKE_LEADED.getStillFluid();
        return smokeLeaded != null && (stack.isEmpty() || stack.getFluid() == smokeLeaded);
    }

    private boolean isPoisonSmokeFluid(final FluidStack stack) {
        final Fluid smokePoison = HbmFluids.SMOKE_POISON.getStillFluid();
        return smokePoison != null && (stack.isEmpty() || stack.getFluid() == smokePoison);
    }
}

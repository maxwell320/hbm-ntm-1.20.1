package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.block.GasCentrifugeBlock;
import com.hbm.ntm.common.centrifuge.HbmGasCentrifugeRecipes;
import com.hbm.ntm.common.centrifuge.HbmGasCentrifugeRecipes.PseudoFluidType;
import com.hbm.ntm.common.config.GasCentrifugeMachineConfig;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.menu.GasCentrifugeMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmSoundEvents;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class GasCentrifugeBlockEntity extends MachineBlockEntity {
    private static final int OPERATE_SOUND_INTERVAL = 40;
    public static final int SLOT_OUTPUT_1 = 0;
    public static final int SLOT_OUTPUT_2 = 1;
    public static final int SLOT_OUTPUT_3 = 2;
    public static final int SLOT_OUTPUT_4 = 3;
    public static final int SLOT_BATTERY = 4;
    public static final int SLOT_FLUID_ID = 5;
    public static final int SLOT_UPGRADE = 6;
    public static final int SLOT_COUNT = 7;

    private static final int[] SLOT_ACCESS = new int[]{SLOT_OUTPUT_1, SLOT_OUTPUT_2, SLOT_OUTPUT_3, SLOT_OUTPUT_4};

    private int progress;
    private int pseudoInputAmount;
    private int pseudoOutputAmount;
    private PseudoFluidType pseudoInputType = PseudoFluidType.NONE;
    private PseudoFluidType pseudoOutputType = PseudoFluidType.NONE;
    private @Nullable ResourceLocation selectedFeedFluid;
    private int operateSoundTimer;

    public GasCentrifugeBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_GAS_CENTRIFUGE.get(), pos, state, SLOT_COUNT);
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        final int capacity = Math.max(1, GasCentrifugeMachineConfig.INSTANCE.maxPower());
        return this.createSimpleEnergyStorage(capacity, capacity, 0);
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        final int tankCapacity = Math.max(1, GasCentrifugeMachineConfig.INSTANCE.fluidTankCapacity());
        return new HbmFluidTank[]{this.createFluidTank(tankCapacity, this::isAcceptedFeedFluid)};
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.RECEIVE;
    }

    @Override
    protected boolean canFillFromSide(final Direction side) {
        return true;
    }

    @Override
    protected boolean canDrainFromSide(final Direction side) {
        return false;
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final GasCentrifugeBlockEntity centrifuge) {
        boolean dirty = false;

        if (centrifuge.tryChargeFromBattery()) {
            dirty = true;
        }

        if (centrifuge.updateFluidSelection()) {
            dirty = true;
        }

        if (centrifuge.attemptConversion()) {
            dirty = true;
        }

        if (centrifuge.canEnrich()) {
            centrifuge.progress++;
            centrifuge.tickOperateSound(level, pos);
            final int cost = centrifuge.currentPowerPerTick();
            final int before = centrifuge.getStoredEnergy();
            centrifuge.consumeEnergy(cost);
            dirty = true;

            if (before < cost) {
                centrifuge.progress = 0;
            } else if (centrifuge.progress >= centrifuge.currentProcessingSpeed()) {
                centrifuge.enrich();
            }
        } else if (centrifuge.progress > 0) {
            centrifuge.progress = 0;
            centrifuge.operateSoundTimer = 0;
            dirty = true;
        } else {
            centrifuge.operateSoundTimer = 0;
        }

        final int transferInterval = Math.max(1, GasCentrifugeMachineConfig.INSTANCE.transferIntervalTicks());
        if (level.getGameTime() % transferInterval == 0 && centrifuge.attemptTransfer()) {
            dirty = true;
        }

        if (dirty) {
            centrifuge.markChangedAndSync();
        }
        centrifuge.tickMachineStateSync();
    }

    private boolean canEnrich() {
        if (this.getStoredEnergy() <= 0) {
            return false;
        }
        if (this.pseudoInputType == PseudoFluidType.NONE) {
            return false;
        }
        if (this.pseudoInputType.requiresHighSpeed() && !this.hasGcSpeedUpgrade()) {
            return false;
        }
        if (this.pseudoInputAmount < this.pseudoInputType.fluidConsumed()) {
            return false;
        }
        if (this.pseudoOutputAmount + this.pseudoInputType.fluidProduced() > this.pseudoTankCapacity()) {
            return false;
        }
        return this.canFitOutputs(this.pseudoInputType.outputs());
    }

    private void enrich() {
        this.progress = 0;
        this.pseudoInputAmount -= this.pseudoInputType.fluidConsumed();
        this.pseudoOutputAmount = Math.min(this.pseudoTankCapacity(), this.pseudoOutputAmount + this.pseudoInputType.fluidProduced());
        this.insertOutputs(this.pseudoInputType.outputs());
    }

    private boolean canFitOutputs(final java.util.List<ItemStack> outputs) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        for (final ItemStack out : outputs) {
            int remaining = out.getCount();

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_4 && remaining > 0; slot++) {
                final ItemStack existing = handler.getStackInSlot(slot);
                if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, out)) {
                    remaining -= Math.max(0, existing.getMaxStackSize() - existing.getCount());
                }
            }

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_4 && remaining > 0; slot++) {
                final ItemStack existing = handler.getStackInSlot(slot);
                if (existing.isEmpty()) {
                    remaining -= Math.min(out.getMaxStackSize(), remaining);
                }
            }

            if (remaining > 0) {
                return false;
            }
        }
        return true;
    }

    private void insertOutputs(final java.util.List<ItemStack> outputs) {
        final ItemStackHandler handler = this.getInternalItemHandler();

        for (final ItemStack out : outputs) {
            int remaining = out.getCount();

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_4 && remaining > 0; slot++) {
                final ItemStack existing = handler.getStackInSlot(slot);
                if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, out)) {
                    final int toMove = Math.min(remaining, existing.getMaxStackSize() - existing.getCount());
                    if (toMove > 0) {
                        final ItemStack grown = existing.copy();
                        grown.grow(toMove);
                        handler.setStackInSlot(slot, grown);
                        remaining -= toMove;
                    }
                }
            }

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_4 && remaining > 0; slot++) {
                final ItemStack existing = handler.getStackInSlot(slot);
                if (existing.isEmpty()) {
                    final ItemStack moved = out.copy();
                    moved.setCount(Math.min(remaining, moved.getMaxStackSize()));
                    handler.setStackInSlot(slot, moved);
                    remaining -= moved.getCount();
                }
            }
        }
    }

    private boolean updateFluidSelection() {
        final ItemStack identifierStack = this.getInternalItemHandler().getStackInSlot(SLOT_FLUID_ID);
        if (!(identifierStack.getItem() instanceof final IItemFluidIdentifier identifier)) {
            return false;
        }

        final ResourceLocation fluidId = identifier.getFluidId(identifierStack);
        if (fluidId == null) {
            return false;
        }

        final @Nullable PseudoFluidType mapped = HbmGasCentrifugeRecipes.getPseudoInputForFluid(fluidId).orElse(null);
        if (mapped == null) {
            return false;
        }

        if (Objects.equals(this.selectedFeedFluid, fluidId)) {
            return false;
        }

        this.selectedFeedFluid = fluidId;
        this.setPseudoChain(mapped);
        return true;
    }

    private void setPseudoChain(final PseudoFluidType inputType) {
        final PseudoFluidType newInput = inputType == null ? PseudoFluidType.NONE : inputType;
        if (this.pseudoInputType == newInput) {
            return;
        }

        this.pseudoInputType = newInput;
        this.pseudoOutputType = newInput.outputType();
        this.pseudoInputAmount = 0;
        this.pseudoOutputAmount = 0;
        this.progress = 0;
    }

    private boolean attemptConversion() {
        if (this.selectedFeedFluid == null || this.pseudoInputType == PseudoFluidType.NONE) {
            return false;
        }

        final HbmFluidTank tank = this.getFluidTank(0);
        if (tank == null || tank.isEmpty()) {
            return false;
        }

        final ResourceLocation stackId = ForgeRegistries.FLUIDS.getKey(tank.getFluid().getFluid());
        if (!Objects.equals(this.selectedFeedFluid, stackId)) {
            return false;
        }

        if (this.pseudoInputAmount >= this.pseudoTankCapacity()) {
            return false;
        }

        final int fill = Math.min(this.pseudoTankCapacity() - this.pseudoInputAmount, tank.getFluidAmount());
        if (fill <= 0) {
            return false;
        }

        tank.drain(fill, IFluidHandler.FluidAction.EXECUTE);
        this.pseudoInputAmount += fill;
        return true;
    }

    private boolean attemptTransfer() {
        if (this.pseudoOutputType == PseudoFluidType.NONE || this.pseudoOutputAmount <= 0) {
            return false;
        }

        if (!(this.getBlockState().getBlock() instanceof GasCentrifugeBlock)) {
            return false;
        }

        final Direction facing = this.getBlockState().getValue(GasCentrifugeBlock.FACING);
        final BlockPos outPos = this.worldPosition.relative(facing.getOpposite());
        if (!(Objects.requireNonNull(this.level).getBlockEntity(outPos) instanceof final GasCentrifugeBlockEntity neighbor)) {
            return false;
        }

        if (!Objects.equals(this.selectedFeedFluid, neighbor.selectedFeedFluid)) {
            return false;
        }

        final int moved = neighbor.acceptPseudoInput(this.pseudoOutputType, this.pseudoOutputAmount);
        if (moved <= 0) {
            return false;
        }

        this.pseudoOutputAmount -= moved;
        return true;
    }

    private int acceptPseudoInput(final PseudoFluidType incomingType, final int amount) {
        if (incomingType == null || incomingType == PseudoFluidType.NONE || amount <= 0) {
            return 0;
        }

        if (this.pseudoInputType != incomingType) {
            this.setPseudoChain(incomingType);
        }

        if (this.pseudoInputAmount >= this.pseudoTankCapacity()) {
            return 0;
        }

        final int moved = Math.min(this.pseudoTankCapacity() - this.pseudoInputAmount, amount);
        this.pseudoInputAmount += moved;
        return moved;
    }

    private boolean hasGcSpeedUpgrade() {
        final ItemStack stack = this.getInternalItemHandler().getStackInSlot(SLOT_UPGRADE);
        return stack.getItem() instanceof MachineUpgradeItem upgrade
            && upgrade.type() == MachineUpgradeItem.UpgradeType.GC_SPEED;
    }

    private int currentProcessingSpeed() {
        return this.hasGcSpeedUpgrade()
            ? Math.max(1, GasCentrifugeMachineConfig.INSTANCE.overclockProcessingSpeed())
            : Math.max(1, GasCentrifugeMachineConfig.INSTANCE.processingSpeed());
    }

    private int currentPowerPerTick() {
        return this.hasGcSpeedUpgrade()
            ? Math.max(1, GasCentrifugeMachineConfig.INSTANCE.overclockPowerPerTick())
            : Math.max(1, GasCentrifugeMachineConfig.INSTANCE.powerPerTick());
    }

    private int pseudoTankCapacity() {
        return Math.max(1, GasCentrifugeMachineConfig.INSTANCE.pseudoTankCapacity());
    }

    private boolean tryChargeFromBattery() {
        final ItemStack battery = this.getInternalItemHandler().getStackInSlot(SLOT_BATTERY);
        if (battery.isEmpty() || !(battery.getItem() instanceof final BatteryItem batteryItem)) {
            return false;
        }

        final int stored = this.getStoredEnergy();
        final int space = this.getMaxStoredEnergy() - stored;
        if (space <= 0) {
            return false;
        }

        final int rate = batteryItem.getDischargeRate();
        final int charge = batteryItem.getStoredEnergy(battery);
        final int toDischarge = Math.min(Math.min(space, rate), charge);
        if (toDischarge <= 0) {
            return false;
        }

        batteryItem.withStoredEnergy(battery, charge - toDischarge);
        final IEnergyStorage storage = this.getEnergyStorage(null);
        if (storage == null) {
            return false;
        }
        storage.receiveEnergy(toDischarge, false);
        return true;
    }

    private void consumeEnergy(final int amount) {
        final IEnergyStorage storage = this.getEnergyStorage(null);
        if (storage == null || amount <= 0) {
            return;
        }
        storage.extractEnergy(Math.min(amount, storage.getEnergyStored()), false);
    }

    private boolean isAcceptedFeedFluid(final FluidStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        final ResourceLocation id = ForgeRegistries.FLUIDS.getKey(stack.getFluid());
        if (id == null || !HbmGasCentrifugeRecipes.isSupportedFeedFluid(id)) {
            return false;
        }
        return this.selectedFeedFluid == null || this.selectedFeedFluid.equals(id);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot == SLOT_BATTERY) {
            return stack.getItem() instanceof BatteryItem;
        }
        if (slot == SLOT_FLUID_ID) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        }
        if (slot == SLOT_UPGRADE) {
            return stack.getItem() instanceof MachineUpgradeItem upgrade
                && upgrade.type() == MachineUpgradeItem.UpgradeType.GC_SPEED;
        }
        return false;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return slot <= SLOT_OUTPUT_4;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return SLOT_ACCESS;
    }

    @Override
    protected boolean isUpgradeSlot(final int slot) {
        return slot == SLOT_UPGRADE;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_GAS_CENTRIFUGE.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new GasCentrifugeMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getPseudoInputAmount() {
        return this.pseudoInputAmount;
    }

    public int getPseudoOutputAmount() {
        return this.pseudoOutputAmount;
    }

    public int getPseudoCapacity() {
        return this.pseudoTankCapacity();
    }

    public PseudoFluidType getPseudoInputType() {
        return this.pseudoInputType;
    }

    public PseudoFluidType getPseudoOutputType() {
        return this.pseudoOutputType;
    }

    public boolean hasSpeedUpgradeInstalled() {
        return this.hasGcSpeedUpgrade();
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("pseudoInputAmount", this.pseudoInputAmount);
        tag.putInt("pseudoOutputAmount", this.pseudoOutputAmount);
        tag.putString("pseudoInputType", this.pseudoInputType.name());
        tag.putString("pseudoOutputType", this.pseudoOutputType.name());
        if (this.selectedFeedFluid != null) {
            tag.putString("selectedFeedFluid", this.selectedFeedFluid.toString());
        }
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.pseudoInputAmount = Math.max(0, tag.getInt("pseudoInputAmount"));
        this.pseudoOutputAmount = Math.max(0, tag.getInt("pseudoOutputAmount"));
        this.pseudoInputType = PseudoFluidType.byName(tag.getString("pseudoInputType"));
        this.pseudoOutputType = PseudoFluidType.byName(tag.getString("pseudoOutputType"));
        if (tag.contains("selectedFeedFluid")) {
            this.selectedFeedFluid = ResourceLocation.tryParse(tag.getString("selectedFeedFluid"));
        }
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("energy", this.getStoredEnergy());
        tag.putInt("maxEnergy", this.getMaxStoredEnergy());
        tag.putInt("processingSpeed", this.currentProcessingSpeed());
        tag.putInt("powerPerTick", this.currentPowerPerTick());
        tag.putInt("pseudoInputAmount", this.pseudoInputAmount);
        tag.putInt("pseudoOutputAmount", this.pseudoOutputAmount);
        tag.putInt("pseudoCapacity", this.pseudoTankCapacity());
        tag.putString("pseudoInputType", this.pseudoInputType.name());
        tag.putString("pseudoOutputType", this.pseudoOutputType.name());
        tag.putString("pseudoInputTypeKey", this.pseudoInputType.translationKey());
        tag.putString("pseudoOutputTypeKey", this.pseudoOutputType.translationKey());
        tag.putBoolean("pseudoInputNeedsSpeed", this.pseudoInputType.requiresHighSpeed());
        tag.putBoolean("hasSpeedUpgrade", this.hasGcSpeedUpgrade());
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.pseudoInputAmount = Math.max(0, tag.getInt("pseudoInputAmount"));
        this.pseudoOutputAmount = Math.max(0, tag.getInt("pseudoOutputAmount"));
        this.pseudoInputType = PseudoFluidType.byName(tag.getString("pseudoInputType"));
        this.pseudoOutputType = PseudoFluidType.byName(tag.getString("pseudoOutputType"));
    }

    @Override
    public Map<MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of(MachineUpgradeItem.UpgradeType.GC_SPEED, 1);
    }

    private void tickOperateSound(final Level level, final BlockPos pos) {
        if (this.operateSoundTimer <= 0) {
            level.playSound(null, pos, HbmSoundEvents.BLOCK_CENTRIFUGE_OPERATE.get(), SoundSource.BLOCKS, this.getVolume(1.0F), 1.0F);
            this.operateSoundTimer = OPERATE_SOUND_INTERVAL;
        } else {
            this.operateSoundTimer--;
        }
    }
}

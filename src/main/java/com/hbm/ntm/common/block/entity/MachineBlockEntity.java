package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.EnergyNetworkPriority;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.energy.IEnergyGenerator;
import com.hbm.ntm.common.energy.IEnergyNetworkProvider;
import com.hbm.ntm.common.energy.IEnergyNetworkReceiver;
import com.hbm.ntm.common.energy.IEnergyUser;
import com.hbm.ntm.common.energy.SidedEnergyStorage;
import com.hbm.ntm.common.fluid.CombustibleFuelRegistry;
import com.hbm.ntm.common.fluid.FluidNetworkPriority;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.fluid.IFluidNetworkProvider;
import com.hbm.ntm.common.fluid.IFluidNetworkReceiver;
import com.hbm.ntm.common.fluid.MachineFluidHandler;
import com.hbm.ntm.common.fluid.SidedFluidHandler;
import com.hbm.ntm.common.item.MachineRepairToolItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.machine.IConditionalInventoryAccess;
import com.hbm.ntm.common.machine.IMachineStateSyncReceiver;
import com.hbm.ntm.common.machine.IRepairableMachine;
import com.hbm.ntm.common.machine.IUpgradeInfoProvider;
import com.hbm.ntm.common.machine.IMachineControlReceiver;
import com.hbm.ntm.common.machine.MachineUpgradeManager;
import com.hbm.ntm.common.network.HbmPacketHandler;
import com.hbm.ntm.common.pollution.PollutionSavedData;
import com.hbm.ntm.common.pollution.PollutionType;
import com.hbm.ntm.common.tag.HbmItemTags;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public abstract class MachineBlockEntity extends BlockEntity implements MenuProvider, IMachineControlReceiver, IEnergyUser, IEnergyGenerator,
    IConditionalInventoryAccess, IRepairableMachine, IMachineStateSyncReceiver, IUpgradeInfoProvider, IEnergyNetworkProvider, IEnergyNetworkReceiver,
    IFluidNetworkProvider, IFluidNetworkReceiver {
    protected static final float MACHINE_SOOT_PER_SECOND = 1.0F / 25.0F;

    private final ItemStackHandler items;
    private final MachineUpgradeManager upgradeManager;
    private final Map<Direction, LazyOptional<IItemHandler>> sidedItemCapabilities = new EnumMap<>(Direction.class);
    private final Map<Direction, LazyOptional<IEnergyStorage>> sidedEnergyCapabilities = new EnumMap<>(Direction.class);
    private final Map<Direction, LazyOptional<IFluidHandler>> sidedFluidCapabilities = new EnumMap<>(Direction.class);
    private LazyOptional<IItemHandler> itemCapability = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> energyCapability = LazyOptional.empty();
    private LazyOptional<IFluidHandler> fluidCapability = LazyOptional.empty();
    private boolean muffled;
    private boolean loaded = true;
    private boolean runtimeInitialized;
    private @Nullable HbmEnergyStorage energyStorage;
    private HbmFluidTank[] fluidTanks = new HbmFluidTank[0];
    private @Nullable MachineFluidHandler fluidHandler;
    private int maintenanceLevel = -1;
    private boolean maintenanceBlocked;
    private @Nullable CompoundTag lastSyncedMachineState;

    protected MachineBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState state, final int slotCount) {
        super(type, pos, state);
        this.items = new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(final int slot) {
                MachineBlockEntity.this.onInventoryChanged(slot);
            }
        };
        this.upgradeManager = new MachineUpgradeManager(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.loaded = true;
        this.ensureRuntimeInitialized();
        this.createCapabilities();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.loaded = false;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemCapability.invalidate();
        this.energyCapability.invalidate();
        this.fluidCapability.invalidate();
        this.sidedItemCapabilities.values().forEach(LazyOptional::invalidate);
        this.sidedEnergyCapabilities.values().forEach(LazyOptional::invalidate);
        this.sidedFluidCapabilities.values().forEach(LazyOptional::invalidate);
        this.sidedItemCapabilities.clear();
        this.sidedEnergyCapabilities.clear();
        this.sidedFluidCapabilities.clear();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.createCapabilities();
    }

    @Override
    protected void saveAdditional(final CompoundTag tag) {
        this.ensureRuntimeInitialized();
        super.saveAdditional(tag);
        tag.put("items", this.items.serializeNBT());
        tag.putBoolean("muffled", this.muffled);
        tag.putBoolean("maintenanceBlocked", this.maintenanceBlocked);
        tag.putInt("maintenanceLevel", this.getMaintenanceLevel());
        if (this.energyStorage != null) {
            tag.put("energy", this.energyStorage.serializeNBT());
        }
        if (this.fluidTanks.length > 0) {
            final ListTag tanksTag = new ListTag();
            for (int tankIndex = 0; tankIndex < this.fluidTanks.length; tankIndex++) {
                final CompoundTag tankTag = new CompoundTag();
                tankTag.putInt("slot", tankIndex);
                this.fluidTanks[tankIndex].writeToNBT(tankTag);
                tanksTag.add(tankTag);
            }
            tag.put("fluidTanks", tanksTag);
        }
        this.saveMachineData(tag);
    }

    @Override
    public void load(final CompoundTag tag) {
        this.ensureRuntimeInitialized();
        super.load(tag);
        if (tag.contains("items", CompoundTag.TAG_COMPOUND)) {
            this.items.deserializeNBT(tag.getCompound("items"));
        }
        this.muffled = tag.getBoolean("muffled");
        this.maintenanceBlocked = tag.getBoolean("maintenanceBlocked");
        if (tag.contains("maintenanceLevel", Tag.TAG_INT)) {
            this.maintenanceLevel = tag.getInt("maintenanceLevel");
        }
        final Tag energyTag = tag.get("energy");
        if (this.energyStorage != null && energyTag != null) {
            this.energyStorage.deserializeNBT(energyTag);
        }
        if (tag.contains("fluidTanks", Tag.TAG_LIST)) {
            final ListTag tanksTag = tag.getList("fluidTanks", Tag.TAG_COMPOUND);
            for (int i = 0; i < tanksTag.size(); i++) {
                final CompoundTag tankTag = tanksTag.getCompound(i);
                final int tankIndex = tankTag.getInt("slot");
                if (tankIndex >= 0 && tankIndex < this.fluidTanks.length) {
                    this.fluidTanks[tankIndex].readFromNBT(tankTag);
                }
            }
        }
        this.loadMachineData(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(final CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void onDataPacket(final Connection connection, final ClientboundBlockEntityDataPacket packet) {
        if (packet.getTag() != null) {
            this.load(packet.getTag());
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side) {
        this.ensureRuntimeInitialized();
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return side == null ? this.itemCapability.cast() : this.sidedItemCapabilities.getOrDefault(side, LazyOptional.empty()).cast();
        }
        if (capability == ForgeCapabilities.ENERGY && this.energyStorage != null) {
            return side == null ? this.energyCapability.cast() : this.sidedEnergyCapabilities.getOrDefault(side, LazyOptional.empty()).cast();
        }
        if (capability == ForgeCapabilities.FLUID_HANDLER && this.fluidHandler != null) {
            return side == null ? this.fluidCapability.cast() : this.sidedFluidCapabilities.getOrDefault(side, LazyOptional.empty()).cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean canPlayerControl(final Player player) {
        return !this.isRemoved() && player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isControlDataAllowed(final Player player, final CompoundTag data) {
        if (data == null || data.isEmpty()) {
            return false;
        }

        final Set<String> keys = data.getAllKeys();
        if (keys.isEmpty() || keys.size() > 4) {
            return false;
        }

        final boolean hasPatternSlot = keys.contains("patternSlot");
        final boolean hasPatternStack = keys.contains("patternStack");
        final boolean hasLegacySlot = keys.contains("slot");
        final boolean hasLegacyStack = keys.contains("stack");
        if (hasPatternSlot != hasPatternStack) {
            return false;
        }
        if (hasLegacySlot != hasLegacyStack) {
            return false;
        }
        if ((hasPatternSlot || hasPatternStack) && (hasLegacySlot || hasLegacyStack)) {
            return false;
        }

        final Set<String> allowed = this.allowedControlKeys();
        if (allowed == null || allowed.isEmpty()) {
            return false;
        }

        for (final String key : keys) {
            if (key == null || key.isBlank() || key.length() > 32 || !allowed.contains(key)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void receiveControl(final CompoundTag data) {
        if (this.applyPatternSlotControl(data)) {
            this.markChangedAndSync();
            return;
        }
        this.applyControlData(data);
        this.markChangedAndSync();
    }

    @Override
    public void receiveControl(final Player player, final CompoundTag data) {
        if (data.getBoolean("repair")) {
            if (this.tryRepairFromPlayer(player)) {
                this.markChangedAndSync();
            }
            return;
        }
        this.receiveControl(data);
    }

    public ItemStackHandler getInternalItemHandler() {
        return this.items;
    }

    public boolean isMuffled() {
        return this.muffled;
    }

    public void setMuffled(final boolean muffled) {
        this.muffled = muffled;
        this.markChangedAndSync();
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public float getVolume(final float baseVolume) {
        return this.muffled ? baseVolume * 0.1F : baseVolume;
    }

    protected final void emitPeriodicPollution(final PollutionType type, final float amountPerSecond) {
        if (this.level == null || this.level.isClientSide() || amountPerSecond == 0.0F || this.level.getGameTime() % 20L != 0L) {
            return;
        }
        PollutionSavedData.incrementPollution(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), type, amountPerSecond);
    }

    protected final void emitFuelBurnPollution(final @Nullable Fluid fluid, final float burnedMillibuckets) {
        if (this.level == null || this.level.isClientSide() || fluid == null || burnedMillibuckets <= 0.0F) {
            return;
        }

        final CombustibleFuelRegistry.BurnPollution pollution = CombustibleFuelRegistry.burnPollutionFor(fluid);
        if (pollution.isEmpty()) {
            return;
        }

        final int x = this.worldPosition.getX();
        final int y = this.worldPosition.getY();
        final int z = this.worldPosition.getZ();

        if (pollution.soot() > 0.0F) {
            PollutionSavedData.incrementPollution(this.level, x, y, z, PollutionType.SOOT, pollution.soot() * burnedMillibuckets);
        }
        if (pollution.heavyMetal() > 0.0F) {
            PollutionSavedData.incrementPollution(this.level, x, y, z, PollutionType.HEAVY_METAL, pollution.heavyMetal() * burnedMillibuckets);
        }
        if (pollution.poison() > 0.0F) {
            PollutionSavedData.incrementPollution(this.level, x, y, z, PollutionType.POISON, pollution.poison() * burnedMillibuckets);
        }
    }

    protected final int bufferPollution(final int currentBuffer,
                                        final int bufferCapacity,
                                        final PollutionType type,
                                        final float amount) {
        if (amount <= 0.0F || bufferCapacity <= 0) {
            return Math.max(0, Math.min(bufferCapacity, currentBuffer));
        }

        final int fluidAmount = (int) Math.ceil(amount * 100.0F);
        if (fluidAmount <= 0) {
            return Math.max(0, Math.min(bufferCapacity, currentBuffer));
        }

        int next = currentBuffer + fluidAmount;
        if (next > bufferCapacity) {
            final int overflow = next - bufferCapacity;
            next = bufferCapacity;
            if (this.level != null && !this.level.isClientSide()) {
                PollutionSavedData.incrementPollution(
                    this.level,
                    this.worldPosition.getX(),
                    this.worldPosition.getY(),
                    this.worldPosition.getZ(),
                    type,
                    overflow / 100.0F);
            }
        }

        return Math.max(0, Math.min(bufferCapacity, next));
    }

    protected final void bufferPollutionIntoSmokeTank(final int smokeTankIndex,
                                                      final PollutionType type,
                                                      final Fluid smokeFluid,
                                                      final float amount) {
        if (amount <= 0.0F || smokeFluid == null || this.level == null || this.level.isClientSide()) {
            return;
        }

        final HbmFluidTank tank = this.getFluidTank(smokeTankIndex);
        if (tank == null) {
            return;
        }

        final int fluidAmount = (int) Math.ceil(amount * 100.0F);
        if (fluidAmount <= 0) {
            return;
        }

        final int accepted = tank.fill(new FluidStack(smokeFluid, fluidAmount), IFluidHandler.FluidAction.EXECUTE);
        final int overflow = fluidAmount - accepted;
        if (overflow > 0) {
            PollutionSavedData.incrementPollution(
                this.level,
                this.worldPosition.getX(),
                this.worldPosition.getY(),
                this.worldPosition.getZ(),
                type,
                overflow / 100.0F);
        }
    }

    protected final boolean exportTankToNeighbors(final int tankIndex) {
        return this.exportTankToNeighbors(tankIndex, Integer.MAX_VALUE);
    }

    protected final boolean exportTankToNeighbors(final int tankIndex, final int maxTransferPerNeighbor) {
        if (this.level == null || this.level.isClientSide()) {
            return false;
        }

        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        if (tank == null || tank.isEmpty()) {
            return false;
        }

        final int transferLimit = maxTransferPerNeighbor <= 0 ? Integer.MAX_VALUE : maxTransferPerNeighbor;
        boolean moved = false;

        for (final Direction direction : Direction.values()) {
            if (tank.isEmpty()) {
                break;
            }

            final BlockEntity neighbor = this.level.getBlockEntity(this.worldPosition.relative(direction));
            if (neighbor == null) {
                continue;
            }

            final IFluidHandler neighborHandler = neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).orElse(null);
            if (neighborHandler == null) {
                continue;
            }

            final FluidStack stored = tank.getFluid();
            if (stored.isEmpty()) {
                continue;
            }

            final int offerAmount = transferLimit == Integer.MAX_VALUE ? stored.getAmount() : Math.min(transferLimit, stored.getAmount());
            if (offerAmount <= 0) {
                continue;
            }

            final int accepted = neighborHandler.fill(new FluidStack(stored, offerAmount), IFluidHandler.FluidAction.EXECUTE);
            if (accepted <= 0) {
                continue;
            }

            tank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
            moved = true;
        }

        return moved;
    }

    public void syncToClient() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void markChangedAndSync() {
        this.setChanged();
        this.syncToClient();
        this.syncMachineStatePacket(false);
    }

    public void forceMachineStateSync() {
        this.syncMachineStatePacket(true);
    }

    public void syncMachineStateToPlayer(final ServerPlayer player, final boolean force) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        final CompoundTag payload = this.writeMachineStateSync();
        if (!force && !this.shouldSyncMachineState(payload)) {
            return;
        }
        HbmPacketHandler.syncMachineStateToPlayer(this, payload, player);
        this.lastSyncedMachineState = payload.copy();
    }

    public void tickMachineStateSync() {
        this.syncMachineStatePacket(false);
    }

    public void dropContents() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        for (int slot = 0; slot < this.items.getSlots(); slot++) {
            Containers.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), this.items.getStackInSlot(slot));
        }
    }

    public int getComparatorOutput() {
        int filled = 0;
        int nonEmpty = 0;
        for (int slot = 0; slot < this.items.getSlots(); slot++) {
            final net.minecraft.world.item.ItemStack stack = this.items.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            filled += Math.max(1, stack.getCount() * 64 / Math.max(1, stack.getMaxStackSize()));
            nonEmpty++;
        }
        if (nonEmpty <= 0) {
            return 0;
        }
        return Math.min(15, Math.max(1, filled / Math.max(1, this.items.getSlots())));
    }

    protected final boolean isValidSlotIndex(final int slot) {
        return slot >= 0 && slot < this.items.getSlots();
    }

    public boolean canInsertIntoSlot(final int slot, final net.minecraft.world.item.ItemStack stack, final @Nullable Direction side) {
        if (!this.isValidSlotIndex(slot) || stack.isEmpty()) {
            return false;
        }
        return this.isItemValid(slot, stack);
    }

    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return this.isValidSlotIndex(slot);
    }

    @Override
    public boolean canInsertItem(final int slot, final net.minecraft.world.item.ItemStack stack, final @Nullable Direction side) {
        return this.canInsertIntoSlot(slot, stack, side);
    }

    @Override
    public boolean canExtractItem(final int slot, final net.minecraft.world.item.ItemStack stack, final @Nullable Direction side) {
        return this.canExtractFromSlot(slot, side);
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        final int[] slots = new int[this.items.getSlots()];
        for (int i = 0; i < this.items.getSlots(); i++) {
            slots[i] = i;
        }
        return slots;
    }

    public boolean isItemValid(final int slot, final net.minecraft.world.item.ItemStack stack) {
        return this.isValidSlotIndex(slot);
    }

    @Override
    public @Nullable IEnergyStorage getEnergyStorage(final @Nullable Direction side) {
        this.ensureRuntimeInitialized();
        if (this.energyStorage == null) {
            return null;
        }
        if (side == null) {
            return this.energyStorage;
        }
        return this.sidedEnergyCapabilities.getOrDefault(side, LazyOptional.empty()).orElse(this.energyStorage);
    }

    @Override
    public EnergyConnectionMode getEnergyConnection(final @Nullable Direction side) {
        this.ensureRuntimeInitialized();
        return this.energyStorage == null ? EnergyConnectionMode.NONE : this.getEnergyConnectionMode(side);
    }

    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.BOTH;
    }

    public int getStoredEnergy() {
        this.ensureRuntimeInitialized();
        return this.energyStorage == null ? 0 : this.energyStorage.getEnergyStored();
    }

    public int getMaxStoredEnergy() {
        this.ensureRuntimeInitialized();
        return this.energyStorage == null ? 0 : this.energyStorage.getMaxEnergyStored();
    }

    public boolean hasEnergyStorage() {
        this.ensureRuntimeInitialized();
        return this.energyStorage != null;
    }

    @Override
    public long getAvailableNetworkEnergy(final @Nullable Direction side) {
        final IEnergyStorage storage = this.getEnergyStorage(side);
        if (storage == null || !this.canExtractEnergy(side)) {
            return 0;
        }
        return Math.min(storage.getEnergyStored(), storage.extractEnergy(Integer.MAX_VALUE, true));
    }

    @Override
    public void consumeNetworkEnergy(final @Nullable Direction side, final long amount) {
        final IEnergyStorage storage = this.getEnergyStorage(side);
        if (storage == null || amount <= 0 || !this.canExtractEnergy(side)) {
            return;
        }
        storage.extractEnergy((int) Math.min(Integer.MAX_VALUE, amount), false);
    }

    @Override
    public long getNetworkProviderSpeed(final @Nullable Direction side) {
        final IEnergyStorage storage = this.getEnergyStorage(side);
        return storage == null ? 0 : storage.extractEnergy(Integer.MAX_VALUE, true);
    }

    @Override
    public long getNetworkEnergyDemand(final @Nullable Direction side) {
        final IEnergyStorage storage = this.getEnergyStorage(side);
        if (storage == null || !this.canReceiveEnergy(side)) {
            return 0;
        }
        return storage.receiveEnergy(Integer.MAX_VALUE, true);
    }

    @Override
    public long receiveNetworkEnergy(final @Nullable Direction side, final long amount) {
        final IEnergyStorage storage = this.getEnergyStorage(side);
        if (storage == null || amount <= 0 || !this.canReceiveEnergy(side)) {
            return amount;
        }
        final int received = storage.receiveEnergy((int) Math.min(Integer.MAX_VALUE, amount), false);
        return amount - received;
    }

    @Override
    public long getNetworkReceiverSpeed(final @Nullable Direction side) {
        final IEnergyStorage storage = this.getEnergyStorage(side);
        return storage == null ? 0 : storage.receiveEnergy(Integer.MAX_VALUE, true);
    }

    @Override
    public EnergyNetworkPriority getNetworkPriority() {
        return EnergyNetworkPriority.NORMAL;
    }

    public int getTankCount() {
        this.ensureRuntimeInitialized();
        return this.fluidTanks.length;
    }

    public @Nullable HbmFluidTank getFluidTank(final int tankIndex) {
        this.ensureRuntimeInitialized();
        return tankIndex >= 0 && tankIndex < this.fluidTanks.length ? this.fluidTanks[tankIndex] : null;
    }

    public boolean hasFluidTanks() {
        this.ensureRuntimeInitialized();
        return this.fluidTanks.length > 0;
    }

    @Override
    public long getAvailableNetworkFluid(final net.minecraftforge.fluids.FluidStack fluid, final int pressure, final @Nullable Direction side) {
        if (!this.canDrainFromFluidNetwork(side, fluid)) {
            return 0;
        }
        final HbmFluidTank tank = this.getPrimaryFluidTankFor(fluid);
        return tank == null ? 0 : tank.getFluidAmount();
    }

    @Override
    public void consumeNetworkFluid(final net.minecraftforge.fluids.FluidStack fluid, final int pressure, final long amount, final @Nullable Direction side) {
        if (amount <= 0 || !this.canDrainFromFluidNetwork(side, fluid)) {
            return;
        }
        final HbmFluidTank tank = this.getPrimaryFluidTankFor(fluid);
        if (tank != null) {
            tank.drain((int) Math.min(Integer.MAX_VALUE, amount), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    public long getNetworkFluidDemand(final net.minecraftforge.fluids.FluidStack fluid, final int pressure, final @Nullable Direction side) {
        if (!this.canFillFromFluidNetwork(side, fluid)) {
            return 0;
        }
        final HbmFluidTank tank = this.getPrimaryFluidTankFor(fluid);
        if (tank == null) {
            return 0;
        }
        return tank.fill(fluid.copy(), IFluidHandler.FluidAction.SIMULATE);
    }

    @Override
    public long receiveNetworkFluid(final net.minecraftforge.fluids.FluidStack fluid, final int pressure, final long amount, final @Nullable Direction side) {
        if (amount <= 0 || !this.canFillFromFluidNetwork(side, fluid)) {
            return amount;
        }
        final HbmFluidTank tank = this.getPrimaryFluidTankFor(fluid);
        if (tank == null) {
            return amount;
        }
        final int filled = tank.fill(new net.minecraftforge.fluids.FluidStack(fluid, (int) Math.min(Integer.MAX_VALUE, amount)), IFluidHandler.FluidAction.EXECUTE);
        return amount - filled;
    }

    @Override
    public long getFluidProviderSpeed(final net.minecraftforge.fluids.FluidStack fluid, final int pressure, final @Nullable Direction side) {
        return this.getAvailableNetworkFluid(fluid, pressure, side);
    }

    @Override
    public long getFluidReceiverSpeed(final net.minecraftforge.fluids.FluidStack fluid, final int pressure, final @Nullable Direction side) {
        return this.getNetworkFluidDemand(fluid, pressure, side);
    }

    @Override
    public FluidNetworkPriority getFluidNetworkPriority() {
        return FluidNetworkPriority.NORMAL;
    }

    protected boolean canFillFromSide(final Direction side) {
        return true;
    }

    protected boolean canDrainFromSide(final Direction side) {
        return true;
    }

    protected boolean canFillFromFluidNetwork(final @Nullable Direction side, final net.minecraftforge.fluids.FluidStack fluid) {
        return side == null || this.canFillFromSide(side);
    }

    protected boolean canDrainFromFluidNetwork(final @Nullable Direction side, final net.minecraftforge.fluids.FluidStack fluid) {
        return side == null || this.canDrainFromSide(side);
    }

    protected boolean isUpgradeSlot(final int slot) {
        return false;
    }

    public boolean isUpgradeInventorySlot(final int slot) {
        return this.isUpgradeSlot(slot);
    }

    public int countUpgrades(final MachineUpgradeItem.UpgradeType type) {
        return this.upgradeManager.getLevel(type);
    }

    public int highestUpgradeTier(final MachineUpgradeItem.UpgradeType type) {
        int tier = 0;
        for (int slot = 0; slot < this.items.getSlots(); slot++) {
            if (!this.isUpgradeSlot(slot)) {
                continue;
            }
            final net.minecraft.world.item.ItemStack stack = this.items.getStackInSlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof final MachineUpgradeItem upgrade && upgrade.type() == type) {
                tier = Math.max(tier, upgrade.tier());
            }
        }
        return tier;
    }

    public boolean hasUpgrade(final MachineUpgradeItem.UpgradeType type) {
        return this.upgradeManager.hasUpgrade(type) || this.highestUpgradeTier(type) > 0;
    }

    public Map<MachineUpgradeItem.UpgradeType, Integer> getUpgradeLevels() {
        return this.upgradeManager.getLevels();
    }

    protected boolean usesMaintenanceSystem() {
        return false;
    }

    protected int getMaxMaintenanceLevel() {
        return 100;
    }

    public int getMaintenanceLevel() {
        this.ensureRuntimeInitialized();
        return this.maintenanceLevel;
    }

    public boolean isMaintenanceBlocked() {
        return this.usesMaintenanceSystem() && this.maintenanceBlocked;
    }

    @Override
    public boolean isDamaged() {
        return this.isMaintenanceBlocked();
    }

    @Override
    public List<net.minecraft.world.item.ItemStack> getRepairMaterials() {
        return List.of();
    }

    public boolean tryRepairFromPlayer(final Player player) {
        if (!this.isDamaged()) {
            return false;
        }

        final List<net.minecraft.world.item.ItemStack> requirements = this.getRepairMaterials();
        if (requirements == null || requirements.isEmpty()) {
            this.repairMachine();
            return true;
        }

        if (!this.playerHasRepairMaterials(player, requirements)) {
            return false;
        }

        this.consumePlayerRepairMaterials(player, requirements);
        this.repairMachine();
        return true;
    }

    public boolean tryRepairInteraction(final Player player, final net.minecraft.world.item.ItemStack heldStack) {
        if (!this.isDamaged() || heldStack.isEmpty() || !this.canRepairWith(heldStack)) {
            return false;
        }
        if (heldStack.getItem() instanceof final MachineRepairToolItem repairTool && !repairTool.canRepairMachine(heldStack, player, this)) {
            return false;
        }
        if (!this.tryRepairFromPlayer(player)) {
            return false;
        }
        this.onRepairToolUsed(player, heldStack);
        return true;
    }

    @Override
    public boolean canRepairWith(final net.minecraft.world.item.ItemStack stack) {
        return stack.is(HbmItemTags.MACHINE_REPAIR_TOOLS);
    }

    @Override
    public void onRepairToolUsed(final Player player, final net.minecraft.world.item.ItemStack stack) {
        if (stack.getItem() instanceof final MachineRepairToolItem repairTool) {
            repairTool.onMachineRepairUsed(stack, player, this);
        }
    }

    @Override
    public void applyMachineStateSync(final CompoundTag data) {
        this.muffled = data.getBoolean("muffled");
        this.maintenanceBlocked = data.getBoolean("maintenanceBlocked");
        if (data.contains("maintenanceLevel", Tag.TAG_INT)) {
            this.maintenanceLevel = data.getInt("maintenanceLevel");
        }
        this.readMachineStateSync(data);
    }

    @Override
    public void repairMachine() {
        if (this.usesMaintenanceSystem()) {
            this.maintenanceLevel = this.getMaxMaintenanceLevel();
            this.maintenanceBlocked = false;
            this.markChangedAndSync();
        }
    }

    public boolean damageMachine(final int amount) {
        if (!this.usesMaintenanceSystem() || amount <= 0) {
            return false;
        }
        final int oldValue = this.maintenanceLevel;
        this.maintenanceLevel = Math.max(0, this.maintenanceLevel - amount);
        this.maintenanceBlocked = this.maintenanceLevel <= 0;
        if (this.maintenanceLevel != oldValue) {
            this.markChangedAndSync();
            return true;
        }
        return false;
    }

    public boolean repairMachine(final int amount) {
        if (!this.usesMaintenanceSystem() || amount <= 0) {
            return false;
        }
        final int oldValue = this.maintenanceLevel;
        this.maintenanceLevel = Math.min(this.getMaxMaintenanceLevel(), this.maintenanceLevel + amount);
        this.maintenanceBlocked = this.maintenanceLevel <= 0;
        if (this.maintenanceLevel != oldValue) {
            this.markChangedAndSync();
            return true;
        }
        return false;
    }

    protected void onInventoryChanged(final int slot) {
        this.upgradeManager.invalidate();
        this.setChanged();
    }

    protected void createCapabilities() {
        this.ensureRuntimeInitialized();
        this.itemCapability = LazyOptional.of(() -> this.items);
        this.sidedItemCapabilities.clear();
        for (final Direction direction : Direction.values()) {
            this.sidedItemCapabilities.put(direction, LazyOptional.of(() -> new MachineSidedItemHandler(this, direction)));
        }
        if (this.energyStorage != null) {
            this.energyCapability = LazyOptional.of(() -> this.energyStorage);
            this.sidedEnergyCapabilities.clear();
            for (final Direction direction : Direction.values()) {
                this.sidedEnergyCapabilities.put(direction, LazyOptional.of(() -> new SidedEnergyStorage(this.energyStorage, () -> this.getEnergyConnectionMode(direction))));
            }
        }
        if (this.fluidHandler != null) {
            this.fluidCapability = LazyOptional.of(() -> this.fluidHandler);
            this.sidedFluidCapabilities.clear();
            for (final Direction direction : Direction.values()) {
                final boolean canFill = this.canFillFromSide(direction);
                final boolean canDrain = this.canDrainFromSide(direction);
                this.sidedFluidCapabilities.put(direction, LazyOptional.of(() -> new SidedFluidHandler(this.fluidHandler, canFill, canDrain)));
            }
        }
    }

    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        return null;
    }

    protected HbmFluidTank[] createFluidTanks() {
        return new HbmFluidTank[0];
    }

    protected final HbmEnergyStorage createSimpleEnergyStorage(final int capacity, final int maxReceive, final int maxExtract) {
        return this.createSimpleEnergyStorage(capacity, maxReceive, maxExtract, 0);
    }

    protected final HbmEnergyStorage createSimpleEnergyStorage(final int capacity, final int maxReceive, final int maxExtract, final int energy) {
        return new HbmEnergyStorage(capacity, maxReceive, maxExtract, energy) {
            @Override
            protected void onEnergyChanged() {
                MachineBlockEntity.this.onEnergyContentsChanged();
            }
        };
    }

    protected final HbmFluidTank createFluidTank(final int capacity) {
        return this.createFluidTank(capacity, fluidStack -> true);
    }

    protected final HbmFluidTank createFluidTank(final int capacity, final java.util.function.Predicate<net.minecraftforge.fluids.FluidStack> validator) {
        return new HbmFluidTank(capacity, validator, this::onFluidContentsChanged);
    }

    protected void onEnergyContentsChanged() {
        this.markChangedAndSync();
        if (this.level != null) {
            this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
        }
    }

    protected void onFluidContentsChanged() {
        this.markChangedAndSync();
        if (this.level != null) {
            this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
        }
    }

    private @Nullable HbmFluidTank getPrimaryFluidTankFor(final net.minecraftforge.fluids.FluidStack fluid) {
        this.ensureRuntimeInitialized();
        for (final HbmFluidTank tank : this.fluidTanks) {
            if (tank == null) {
                continue;
            }
            if (tank.isEmpty()) {
                if (fluid.isEmpty() || tank.isFluidValid(fluid)) {
                    return tank;
                }
                continue;
            }
            if (fluid.isEmpty() || tank.getFluid().isFluidEqual(fluid)) {
                return tank;
            }
        }
        return this.fluidTanks.length > 0 ? this.fluidTanks[0] : null;
    }

    private void ensureRuntimeInitialized() {
        if (this.runtimeInitialized) {
            return;
        }
        this.energyStorage = this.createEnergyStorage();
        this.fluidTanks = this.createFluidTanks();
        if (this.fluidTanks == null) {
            this.fluidTanks = new HbmFluidTank[0];
        }
        this.fluidHandler = this.fluidTanks.length > 0 ? new MachineFluidHandler(this.fluidTanks) : null;
        if (this.maintenanceLevel < 0) {
            this.maintenanceLevel = this.getMaxMaintenanceLevel();
        }
        this.runtimeInitialized = true;
    }

    protected void saveMachineData(final CompoundTag tag) {
    }

    protected void loadMachineData(final CompoundTag tag) {
    }

    protected void applyControlData(final CompoundTag data) {
    }

    protected Set<String> allowedControlKeys() {
        if (!this.allowedPatternSlots().isEmpty()) {
            return Set.of("repair", "patternSlot", "patternStack", "slot", "stack");
        }
        return Set.of("repair");
    }

    protected Set<Integer> allowedPatternSlots() {
        return Set.of();
    }

    private boolean applyPatternSlotControl(final CompoundTag data) {
        if (data == null) {
            return false;
        }

        final String slotKey;
        final String stackKey;
        if (data.contains("patternSlot", Tag.TAG_INT) && data.contains("patternStack", Tag.TAG_COMPOUND)) {
            slotKey = "patternSlot";
            stackKey = "patternStack";
        } else if (data.contains("slot", Tag.TAG_INT) && data.contains("stack", Tag.TAG_COMPOUND)) {
            slotKey = "slot";
            stackKey = "stack";
        } else {
            return false;
        }

        final Set<Integer> allowedPatternSlots = this.allowedPatternSlots();
        if (allowedPatternSlots == null || allowedPatternSlots.isEmpty()) {
            return false;
        }

        final int slot = data.getInt(slotKey);
        if (!allowedPatternSlots.contains(slot) || slot < 0 || slot >= this.items.getSlots()) {
            return false;
        }

        final net.minecraft.world.item.ItemStack stack = net.minecraft.world.item.ItemStack.of(data.getCompound(stackKey));
        if (stack.isEmpty()) {
            this.items.setStackInSlot(slot, net.minecraft.world.item.ItemStack.EMPTY);
            return true;
        }

        final net.minecraft.world.item.ItemStack copied = stack.copy();
        copied.setCount(Math.max(1, Math.min(copied.getCount(), copied.getMaxStackSize())));
        if (!this.isItemValid(slot, copied)) {
            return false;
        }

        this.items.setStackInSlot(slot, copied);
        return true;
    }

    protected CompoundTag writeMachineStateSync() {
        final CompoundTag tag = new CompoundTag();
        tag.putBoolean("muffled", this.muffled);
        tag.putBoolean("maintenanceBlocked", this.maintenanceBlocked);
        tag.putInt("maintenanceLevel", this.getMaintenanceLevel());
        tag.putBoolean("hasEnergyStorage", this.hasEnergyStorage());
        tag.putInt("energyStored", this.getStoredEnergy());
        tag.putInt("energyCapacity", this.getMaxStoredEnergy());

        int fluidStoredTotal = 0;
        int fluidCapacityTotal = 0;
        final ListTag syncTanks = new ListTag();
        int tankIndex = 0;
        for (final HbmFluidTank tank : this.fluidTanks) {
            if (tank == null) {
                tankIndex++;
                continue;
            }
            final int amount = Math.max(0, tank.getFluidAmount());
            final int capacity = Math.max(0, tank.getCapacity());
            fluidStoredTotal += amount;
            fluidCapacityTotal += capacity;

            final CompoundTag tankTag = new CompoundTag();
            tankTag.putInt("slot", tankIndex);
            tankTag.putInt("amount", amount);
            tankTag.putInt("capacity", capacity);
            if (!tank.isEmpty()) {
                tankTag.putString("fluid", String.valueOf(BuiltInRegistries.FLUID.getKey(tank.getFluid().getFluid())));
            }
            syncTanks.add(tankTag);
            tankIndex++;
        }
        tag.putInt("fluidTankCount", this.fluidTanks.length);
        tag.putInt("fluidStoredTotal", fluidStoredTotal);
        tag.putInt("fluidCapacityTotal", fluidCapacityTotal);
        if (!syncTanks.isEmpty()) {
            tag.put("syncTanks", syncTanks);
        }

        int occupiedSlots = 0;
        int itemCountTotal = 0;
        for (int slot = 0; slot < this.items.getSlots(); slot++) {
            final net.minecraft.world.item.ItemStack stack = this.items.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            occupiedSlots++;
            itemCountTotal += stack.getCount();
        }
        tag.putInt("inventorySlotCount", this.items.getSlots());
        tag.putInt("inventoryOccupiedSlots", occupiedSlots);
        tag.putInt("inventoryItemTotal", itemCountTotal);

        this.writeRepairMaterialStateSync(tag);
        this.writeAdditionalMachineStateSync(tag);
        return tag;
    }

    private void writeRepairMaterialStateSync(final CompoundTag tag) {
        if (!this.isDamaged()) {
            return;
        }
        final List<net.minecraft.world.item.ItemStack> requirements = this.getRepairMaterials();
        if (requirements == null || requirements.isEmpty()) {
            return;
        }
        final ListTag list = new ListTag();
        for (final net.minecraft.world.item.ItemStack stack : requirements) {
            if (stack.isEmpty()) {
                continue;
            }
            list.add(stack.save(new CompoundTag()));
        }
        if (!list.isEmpty()) {
            tag.put("repairMaterials", list);
        }
    }

    private void syncMachineStatePacket(final boolean force) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        final CompoundTag payload = this.writeMachineStateSync();
        if (!force && !this.shouldSyncMachineState(payload)) {
            return;
        }
        HbmPacketHandler.syncMachineState(this, payload);
        this.lastSyncedMachineState = payload.copy();
    }

    private boolean shouldSyncMachineState(final CompoundTag payload) {
        if (this.level == null) {
            return true;
        }
        final boolean heartbeat = this.level.getGameTime() % 20L == 0L;
        return this.lastSyncedMachineState == null || !this.lastSyncedMachineState.equals(payload) || heartbeat;
    }

    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
    }

    protected void readMachineStateSync(final CompoundTag tag) {
    }

    private boolean playerHasRepairMaterials(final Player player, final List<net.minecraft.world.item.ItemStack> requirements) {
        final net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
        for (final net.minecraft.world.item.ItemStack requirement : requirements) {
            if (requirement.isEmpty()) {
                continue;
            }
            int remaining = requirement.getCount();
            for (int slot = 0; slot < inventory.getContainerSize() && remaining > 0; slot++) {
                final net.minecraft.world.item.ItemStack stack = inventory.getItem(slot);
                if (net.minecraft.world.item.ItemStack.isSameItemSameTags(stack, requirement)) {
                    remaining -= stack.getCount();
                }
            }
            if (remaining > 0) {
                return false;
            }
        }
        return true;
    }

    private void consumePlayerRepairMaterials(final Player player, final List<net.minecraft.world.item.ItemStack> requirements) {
        final net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
        for (final net.minecraft.world.item.ItemStack requirement : requirements) {
            if (requirement.isEmpty()) {
                continue;
            }
            int remaining = requirement.getCount();
            for (int slot = 0; slot < inventory.getContainerSize() && remaining > 0; slot++) {
                final net.minecraft.world.item.ItemStack stack = inventory.getItem(slot);
                if (!net.minecraft.world.item.ItemStack.isSameItemSameTags(stack, requirement)) {
                    continue;
                }
                final int consume = Math.min(remaining, stack.getCount());
                stack.shrink(consume);
                if (stack.isEmpty()) {
                    inventory.setItem(slot, net.minecraft.world.item.ItemStack.EMPTY);
                }
                remaining -= consume;
            }
        }
    }

    @Override
    public boolean canProvideInfo(final MachineUpgradeItem.UpgradeType type, final int level, final boolean extendedInfo) {
        return false;
    }

    @Override
    public void provideInfo(final MachineUpgradeItem.UpgradeType type, final int level, final List<net.minecraft.network.chat.Component> info,
                            final boolean extendedInfo) {
    }

    @Override
    public Map<MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of();
    }
}

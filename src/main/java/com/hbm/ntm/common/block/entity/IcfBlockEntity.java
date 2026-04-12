package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.config.IcfMachineConfig;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.item.IcfPelletItem;
import com.hbm.ntm.common.menu.IcfMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class IcfBlockEntity extends MachineBlockEntity {
    public static final int SLOT_INPUT_1 = 0;
    public static final int SLOT_INPUT_2 = 1;
    public static final int SLOT_INPUT_3 = 2;
    public static final int SLOT_INPUT_4 = 3;
    public static final int SLOT_INPUT_5 = 4;
    public static final int SLOT_ACTIVE_PELLET = 5;
    public static final int SLOT_OUTPUT_1 = 6;
    public static final int SLOT_OUTPUT_2 = 7;
    public static final int SLOT_OUTPUT_3 = 8;
    public static final int SLOT_OUTPUT_4 = 9;
    public static final int SLOT_OUTPUT_5 = 10;
    public static final int SLOT_FLUID_ID = 11;
    public static final int SLOT_COUNT = 12;

    public static final int TANK_COOLANT_IN = 0;
    public static final int TANK_COOLANT_OUT = 1;
    public static final int TANK_STELLAR_FLUX = 2;

    private static final long MAX_HEAT = 1_000_000_000_000L;
    private static final int[] ACCESSIBLE_IO_SLOTS = new int[]{
        SLOT_INPUT_1,
        SLOT_INPUT_2,
        SLOT_INPUT_3,
        SLOT_INPUT_4,
        SLOT_INPUT_5,
        SLOT_OUTPUT_1,
        SLOT_OUTPUT_2,
        SLOT_OUTPUT_3,
        SLOT_OUTPUT_4,
        SLOT_OUTPUT_5
    };

    private static final int COOLANT_AMOUNT_REQ = 1;
    private static final int COOLANT_AMOUNT_PRODUCED = 1;
    private static final int COOLANT_HEAT_REQ = 400;
    private static final double COOLANT_HEAT_FRACTION = 0.25D;
    private static final double COOLANT_EFFICIENCY = 3.0D;
    private static final double PASSIVE_HEAT_FACTOR = 0.25D;
    private static final double HEAT_DECAY = 0.999D;

    private long laser;
    private long maxLaser;
    private long heat;
    private long heatup;
    private long externalLaserPending;
    private long externalMaxLaserPending;
    private int coolantConsumption;
    private int hotCoolantOutput;
    private @Nullable ResourceLocation selectedCoolantFluid;

    public IcfBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_ICF.get(), pos, state, SLOT_COUNT);
        this.selectedCoolantFluid = defaultCoolantSelection();
    }

    private static @Nullable ResourceLocation defaultCoolantSelection() {
        return ForgeRegistries.FLUIDS.getKey(HbmFluids.SODIUM.getStillFluid());
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        final int capacity = Math.max(1, IcfMachineConfig.INSTANCE.energyBuffer());
        return this.createSimpleEnergyStorage(capacity, capacity, 0);
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        final int coolantCapacity = Math.max(1, IcfMachineConfig.INSTANCE.coolantTankCapacity());
        final int stellarFluxCapacity = Math.max(1, IcfMachineConfig.INSTANCE.stellarFluxTankCapacity());
        return new HbmFluidTank[]{
            this.createFluidTank(coolantCapacity, this::isAcceptedCoolantFluid),
            this.createFluidTank(coolantCapacity, this::isAcceptedHotCoolantFluid),
            this.createFluidTank(stellarFluxCapacity, this::isAcceptedStellarFluxFluid)
        };
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
        return true;
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final IcfBlockEntity icf) {
        boolean dirty = false;

        if (icf.updateCoolantSelection()) {
            dirty = true;
        }
        if (icf.ejectDepletedPellet()) {
            dirty = true;
        }
        if (icf.insertFreshPellet()) {
            dirty = true;
        }

        icf.heatup = 0L;
        if (icf.dischargeLaserBuffer()) {
            dirty = true;
        }
        if (icf.consumeExternalLaserInjection()) {
            dirty = true;
        }
        if (icf.reactPellet()) {
            dirty = true;
        }

        if (icf.heatup == 0L && icf.laser > 0L) {
            final long passiveHeat = Math.max(0L, Math.round(icf.laser * PASSIVE_HEAT_FACTOR));
            if (passiveHeat > 0L) {
                icf.heat = Math.min(MAX_HEAT, icf.heat + passiveHeat);
                dirty = true;
            }
        }

        if (icf.processCoolant()) {
            dirty = true;
        }

        final long decayed = clampHeat(Math.round(icf.heat * HEAT_DECAY));
        if (decayed != icf.heat) {
            icf.heat = decayed;
            dirty = true;
        }

        if (dirty) {
            icf.markChangedAndSync();
        }
        icf.tickMachineStateSync();

        icf.laser = 0L;
        icf.maxLaser = 0L;
    }

    private boolean dischargeLaserBuffer() {
        final IEnergyStorage storage = this.getEnergyStorage(null);
        final int configuredMaxLaser = Math.max(1, IcfMachineConfig.INSTANCE.maxLaserPerTick());
        int extracted = 0;
        if (storage != null) {
            extracted = storage.extractEnergy(Math.min(storage.getEnergyStored(), configuredMaxLaser), false);
        }

        final boolean changed = this.laser != extracted || this.maxLaser != configuredMaxLaser;
        this.laser = extracted;
        this.maxLaser = configuredMaxLaser;
        return changed;
    }

    private boolean consumeExternalLaserInjection() {
        if (this.externalLaserPending <= 0L && this.externalMaxLaserPending <= 0L) {
            return false;
        }

        this.laser = Math.max(0L, this.laser + this.externalLaserPending);
        this.maxLaser = Math.max(0L, this.maxLaser + this.externalMaxLaserPending);
        this.externalLaserPending = 0L;
        this.externalMaxLaserPending = 0L;
        return true;
    }

    public void injectExternalLaser(final long laserAmount, final long maxLaserAmount) {
        if (laserAmount <= 0L && maxLaserAmount <= 0L) {
            return;
        }
        this.externalLaserPending = clampAdd(this.externalLaserPending, Math.max(0L, laserAmount));
        this.externalMaxLaserPending = clampAdd(this.externalMaxLaserPending, Math.max(0L, maxLaserAmount));
        this.setChanged();
    }

    private boolean updateCoolantSelection() {
        ResourceLocation desired = defaultCoolantSelection();
        final ItemStack stack = this.getInternalItemHandler().getStackInSlot(SLOT_FLUID_ID);
        if (stack.getItem() instanceof IItemFluidIdentifier identifier) {
            final ResourceLocation fluidId = identifier.getFluidId(stack);
            if (fluidId != null) {
                desired = fluidId;
            }
        }

        if (Objects.equals(this.selectedCoolantFluid, desired)) {
            return false;
        }

        this.selectedCoolantFluid = desired;
        return true;
    }

    private boolean ejectDepletedPellet() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack active = handler.getStackInSlot(SLOT_ACTIVE_PELLET);
        if (active.isEmpty() || active.getItem() != HbmItems.ICF_PELLET_DEPLETED.get()) {
            return false;
        }

        for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_5; slot++) {
            final ItemStack output = handler.getStackInSlot(slot);
            if (!output.isEmpty()) {
                continue;
            }
            handler.setStackInSlot(slot, active.copy());
            handler.setStackInSlot(SLOT_ACTIVE_PELLET, ItemStack.EMPTY);
            return true;
        }

        return false;
    }

    private boolean insertFreshPellet() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        if (!handler.getStackInSlot(SLOT_ACTIVE_PELLET).isEmpty()) {
            return false;
        }

        for (int slot = SLOT_INPUT_1; slot <= SLOT_INPUT_5; slot++) {
            final ItemStack candidate = handler.getStackInSlot(slot);
            if (candidate.isEmpty() || candidate.getItem() != HbmItems.ICF_PELLET.get()) {
                continue;
            }
            handler.setStackInSlot(SLOT_ACTIVE_PELLET, candidate.copy());
            handler.setStackInSlot(slot, ItemStack.EMPTY);
            return true;
        }

        return false;
    }

    private boolean reactPellet() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack active = handler.getStackInSlot(SLOT_ACTIVE_PELLET);
        if (active.isEmpty() || active.getItem() != HbmItems.ICF_PELLET.get()) {
            return false;
        }
        if (IcfPelletItem.getFusingDifficulty(active) > this.laser) {
            return false;
        }

        this.heatup = IcfPelletItem.react(active, this.laser);
        this.heat = clampHeat(this.heat + this.heatup);

        if (IcfPelletItem.getDepletion(active) >= IcfPelletItem.getMaxDepletion(active)) {
            handler.setStackInSlot(SLOT_ACTIVE_PELLET, new ItemStack(HbmItems.ICF_PELLET_DEPLETED.get()));
        }

        final int fluxGain = (int) Math.ceil(this.heat * 10.0D / MAX_HEAT);
        if (fluxGain > 0) {
            final HbmFluidTank stellarFluxTank = this.getFluidTank(TANK_STELLAR_FLUX);
            if (stellarFluxTank != null) {
                stellarFluxTank.fill(new FluidStack(HbmFluids.STELLAR_FLUX.getStillFluid(), fluxGain), IFluidHandler.FluidAction.EXECUTE);
            }
        }

        return true;
    }

    private boolean processCoolant() {
        final int previousConsumption = this.coolantConsumption;
        final int previousOutput = this.hotCoolantOutput;
        this.coolantConsumption = 0;
        this.hotCoolantOutput = 0;

        final HbmFluidTank coolantInput = this.getFluidTank(TANK_COOLANT_IN);
        final HbmFluidTank coolantOutput = this.getFluidTank(TANK_COOLANT_OUT);
        if (coolantInput == null || coolantOutput == null || coolantInput.isEmpty()) {
            return previousConsumption != 0 || previousOutput != 0;
        }
        if (!isFluidInEntry(coolantInput.getFluid().getFluid(), HbmFluids.SODIUM)) {
            return previousConsumption != 0 || previousOutput != 0;
        }

        final int coolingCycles = coolantInput.getFluidAmount() / COOLANT_AMOUNT_REQ;
        final int heatingCycles = Math.max(0, coolantOutput.getCapacity() - coolantOutput.getFluidAmount()) / COOLANT_AMOUNT_PRODUCED;
        final long heatCyclesFraction = (long) Math.floor(this.heat * COOLANT_HEAT_FRACTION / COOLANT_HEAT_REQ * COOLANT_EFFICIENCY);
        final long heatCyclesLimit = this.heat / COOLANT_HEAT_REQ;
        final long heatCycles = Math.max(0L, Math.min(heatCyclesFraction, heatCyclesLimit));
        final int cycles = (int) Math.min(Math.min(coolingCycles, heatingCycles), Math.min(Integer.MAX_VALUE, heatCycles));

        if (cycles <= 0) {
            return previousConsumption != 0 || previousOutput != 0;
        }

        coolantInput.drain(cycles * COOLANT_AMOUNT_REQ, IFluidHandler.FluidAction.EXECUTE);
        coolantOutput.fill(new FluidStack(HbmFluids.SODIUM_HOT.getStillFluid(), cycles * COOLANT_AMOUNT_PRODUCED), IFluidHandler.FluidAction.EXECUTE);
        this.heat = clampHeat(this.heat - (long) COOLANT_HEAT_REQ * cycles);

        this.coolantConsumption = cycles * COOLANT_AMOUNT_REQ;
        this.hotCoolantOutput = cycles * COOLANT_AMOUNT_PRODUCED;
        return true;
    }

    private boolean isAcceptedCoolantFluid(final FluidStack stack) {
        if (stack.isEmpty()) {
            return true;
        }

        if (this.selectedCoolantFluid != null) {
            final Fluid selected = ForgeRegistries.FLUIDS.getValue(this.selectedCoolantFluid);
            if (selected != null && !stack.getFluid().isSame(selected)) {
                return false;
            }
        }

        return true;
    }

    private boolean isAcceptedHotCoolantFluid(final FluidStack stack) {
        return stack.isEmpty() || isFluidInEntry(stack.getFluid(), HbmFluids.SODIUM_HOT);
    }

    private boolean isAcceptedStellarFluxFluid(final FluidStack stack) {
        return stack.isEmpty() || isFluidInEntry(stack.getFluid(), HbmFluids.STELLAR_FLUX);
    }

    private static boolean isFluidInEntry(final Fluid fluid, final HbmFluids.FluidEntry entry) {
        return fluid.isSame(entry.getStillFluid()) || fluid.isSame(entry.getFlowingFluid());
    }

    private static long clampHeat(final long value) {
        return Math.max(0L, Math.min(MAX_HEAT, value));
    }

    private static long clampAdd(final long current, final long increment) {
        if (increment <= 0L) {
            return current;
        }
        final long remaining = Long.MAX_VALUE - current;
        if (remaining <= 0L) {
            return Long.MAX_VALUE;
        }
        return current + Math.min(remaining, increment);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot >= SLOT_INPUT_1 && slot <= SLOT_INPUT_5) {
            return stack.getItem() == HbmItems.ICF_PELLET.get();
        }
        if (slot == SLOT_FLUID_ID) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        }
        return false;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return slot >= SLOT_OUTPUT_1 && slot <= SLOT_OUTPUT_5;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return ACCESSIBLE_IO_SLOTS;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_ICF.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new IcfMenu(containerId, inventory, this);
    }

    public long getLaser() {
        return this.laser;
    }

    public long getMaxLaser() {
        return this.maxLaser;
    }

    public long getHeat() {
        return this.heat;
    }

    public long getMaxHeat() {
        return MAX_HEAT;
    }

    public long getHeatup() {
        return this.heatup;
    }

    public int getCoolantConsumption() {
        return this.coolantConsumption;
    }

    public int getHotCoolantOutput() {
        return this.hotCoolantOutput;
    }

    public int getTankAmount(final int tankIndex) {
        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        return tank == null ? 0 : tank.getFluidAmount();
    }

    public int getTankCapacity(final int tankIndex) {
        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        return tank == null ? 0 : tank.getCapacity();
    }

    public String getTankFluidName(final int tankIndex) {
        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        if (tank == null || tank.isEmpty()) {
            return "";
        }
        return tank.getFluid().getDisplayName().getString();
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putLong("heat", this.heat);
        if (this.selectedCoolantFluid != null) {
            tag.putString("selectedCoolant", this.selectedCoolantFluid.toString());
        }
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.heat = clampHeat(tag.getLong("heat"));
        this.selectedCoolantFluid = readFluidId(tag, "selectedCoolant", defaultCoolantSelection());
    }

    private static @Nullable ResourceLocation readFluidId(final CompoundTag tag,
                                                          final String key,
                                                          final @Nullable ResourceLocation fallback) {
        if (!tag.contains(key)) {
            return fallback;
        }
        final ResourceLocation id = ResourceLocation.tryParse(tag.getString(key));
        return id == null ? fallback : id;
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putLong("laser", this.laser);
        tag.putLong("maxLaser", this.maxLaser);
        tag.putLong("heat", this.heat);
        tag.putLong("maxHeat", MAX_HEAT);
        tag.putLong("heatup", this.heatup);
        tag.putInt("consumption", this.coolantConsumption);
        tag.putInt("output", this.hotCoolantOutput);

        for (int tank = TANK_COOLANT_IN; tank <= TANK_STELLAR_FLUX; tank++) {
            tag.putInt("fluid" + tank + "Amount", this.getTankAmount(tank));
            tag.putInt("fluid" + tank + "Capacity", this.getTankCapacity(tank));
            tag.putString("fluid" + tank + "Name", this.getTankFluidName(tank));
        }
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.laser = Math.max(0L, tag.getLong("laser"));
        this.maxLaser = Math.max(0L, tag.getLong("maxLaser"));
        this.heat = clampHeat(tag.getLong("heat"));
        this.heatup = Math.max(0L, tag.getLong("heatup"));
        this.coolantConsumption = Math.max(0, tag.getInt("consumption"));
        this.hotCoolantOutput = Math.max(0, tag.getInt("output"));
    }

    @Override
    public Map<com.hbm.ntm.common.item.MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of();
    }
}
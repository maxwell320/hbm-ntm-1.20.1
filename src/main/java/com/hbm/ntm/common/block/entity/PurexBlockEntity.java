package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.config.PurexMachineConfig;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.BlueprintItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.menu.PurexMenu;
import com.hbm.ntm.common.purex.HbmPurexRecipes;
import com.hbm.ntm.common.purex.HbmPurexRecipes.PurexRecipe;
import com.hbm.ntm.common.recipe.CountedIngredient;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class PurexBlockEntity extends MachineBlockEntity {
    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_BLUEPRINT = 1;
    public static final int SLOT_UPGRADE_1 = 2;
    public static final int SLOT_UPGRADE_2 = 3;
    public static final int SLOT_INPUT_1 = 4;
    public static final int SLOT_INPUT_2 = 5;
    public static final int SLOT_INPUT_3 = 6;
    public static final int SLOT_OUTPUT_1 = 7;
    public static final int SLOT_OUTPUT_2 = 8;
    public static final int SLOT_OUTPUT_3 = 9;
    public static final int SLOT_OUTPUT_4 = 10;
    public static final int SLOT_OUTPUT_5 = 11;
    public static final int SLOT_OUTPUT_6 = 12;
    public static final int SLOT_COUNT = 13;

    public static final int TANK_INPUT_1 = 0;
    public static final int TANK_INPUT_2 = 1;
    public static final int TANK_INPUT_3 = 2;
    public static final int TANK_OUTPUT = 3;

    private static final int[] SLOT_ACCESS = new int[]{
        SLOT_INPUT_1,
        SLOT_INPUT_2,
        SLOT_INPUT_3,
        SLOT_OUTPUT_1,
        SLOT_OUTPUT_2,
        SLOT_OUTPUT_3,
        SLOT_OUTPUT_4,
        SLOT_OUTPUT_5,
        SLOT_OUTPUT_6
    };

    private int progress;
    private int processTime = 1;
    private int lastConsumption;
    private int displayMaxPower = PurexMachineConfig.INSTANCE.maxPower();
    private boolean hasRecipe;
    private boolean canProcessRecipe;
    private boolean hasPowerForRecipe;

    public PurexBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_PUREX.get(), pos, state, SLOT_COUNT);
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        final int capacity = Math.max(1, PurexMachineConfig.INSTANCE.maxPower());
        return this.createSimpleEnergyStorage(capacity, capacity, 0);
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        final int capacity = Math.max(1, PurexMachineConfig.INSTANCE.fluidTankCapacity());
        return new HbmFluidTank[]{
            this.createFluidTank(capacity, this::isAcceptedInputFluid),
            this.createFluidTank(capacity, this::isAcceptedInputFluid),
            this.createFluidTank(capacity, this::isAcceptedInputFluid),
            this.createFluidTank(capacity, this::isAcceptedOutputFluid)
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

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final PurexBlockEntity purex) {
        boolean dirty = false;

        if (purex.tryChargeFromBattery()) {
            dirty = true;
        }

        final @Nullable PurexRecipe recipe = purex.findRecipe();
        purex.hasRecipe = recipe != null;

        final int speedLevel = Math.min(3, purex.countUpgrades(MachineUpgradeItem.UpgradeType.SPEED));
        final int powerLevel = Math.min(3, purex.countUpgrades(MachineUpgradeItem.UpgradeType.POWER));
        final int overdriveLevel = Math.min(3, purex.countUpgrades(MachineUpgradeItem.UpgradeType.OVERDRIVE));

        final double speedMultiplier = 1.0D + speedLevel / 3.0D + overdriveLevel;
        double powerMultiplier = 1.0D;
        powerMultiplier -= powerLevel * 0.25D;
        powerMultiplier += speedLevel;
        powerMultiplier += overdriveLevel * (10.0D / 3.0D);
        powerMultiplier = Math.max(0.25D, powerMultiplier);

        final int baseTime = recipe == null ? PurexMachineConfig.INSTANCE.baseProcessTime() : recipe.duration();
        final int baseConsumption = recipe == null ? PurexMachineConfig.INSTANCE.basePowerPerTick() : recipe.powerPerTick();

        purex.processTime = Math.max(1, (int) Math.ceil(baseTime / Math.max(0.1D, speedMultiplier)));
        purex.lastConsumption = Math.max(1, (int) Math.ceil(baseConsumption * powerMultiplier));
        purex.displayMaxPower = Math.max(Math.max(1, purex.getStoredEnergy()), Math.max(PurexMachineConfig.INSTANCE.maxPower(), baseConsumption * 100));

        final boolean canProcess = recipe != null && purex.canProcess(recipe);
        purex.canProcessRecipe = canProcess;
        purex.hasPowerForRecipe = purex.getStoredEnergy() >= purex.lastConsumption;

        if (canProcess && purex.hasPowerForRecipe) {
            purex.progress++;
            purex.consumeEnergy(purex.lastConsumption);
            dirty = true;

            if (purex.progress >= purex.processTime) {
                purex.progress = 0;
                if (purex.processRecipe(recipe)) {
                    dirty = true;
                }
            }
        } else if (purex.progress > 0) {
            purex.progress = 0;
            dirty = true;
        }

        if (dirty) {
            purex.markChangedAndSync();
        }
        purex.tickMachineStateSync();
    }

    private @Nullable PurexRecipe findRecipe() {
        return HbmPurexRecipes.findRecipe(this.collectInputItems(), this.collectInputFluids()).orElse(null);
    }

    private List<ItemStack> collectInputItems() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        return List.of(
            handler.getStackInSlot(SLOT_INPUT_1),
            handler.getStackInSlot(SLOT_INPUT_2),
            handler.getStackInSlot(SLOT_INPUT_3));
    }

    private List<FluidStack> collectInputFluids() {
        return List.of(this.getTankFluid(TANK_INPUT_1), this.getTankFluid(TANK_INPUT_2), this.getTankFluid(TANK_INPUT_3));
    }

    private FluidStack getTankFluid(final int tankIndex) {
        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        return tank == null || tank.isEmpty() ? FluidStack.EMPTY : tank.getFluid().copy();
    }

    private boolean canProcess(final PurexRecipe recipe) {
        if (!this.canFitItemOutputs(recipe.itemOutputsCopy())) {
            return false;
        }
        return this.canFitFluidOutputs(recipe.fluidOutputsCopy());
    }

    private boolean processRecipe(final PurexRecipe recipe) {
        if (!this.consumeItemInputs(recipe.itemInputsCopy())) {
            return false;
        }
        if (!this.consumeFluidInputs(recipe.fluidInputsCopy())) {
            return false;
        }
        this.insertItemOutputs(recipe.itemOutputsCopy());
        this.insertFluidOutputs(recipe.fluidOutputsCopy());
        return true;
    }

    private boolean consumeItemInputs(final List<CountedIngredient> requirements) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        final int[] slots = new int[]{SLOT_INPUT_1, SLOT_INPUT_2, SLOT_INPUT_3};
        final int[] toConsume = new int[slots.length];

        for (final CountedIngredient requirement : requirements) {
            int needed = requirement.count();
            for (int i = 0; i < slots.length && needed > 0; i++) {
                final int slot = slots[i];
                final ItemStack stack = handler.getStackInSlot(slot);
                if (stack.isEmpty() || !requirement.ingredient().test(stack)) {
                    continue;
                }
                final int available = Math.max(0, stack.getCount() - toConsume[i]);
                if (available <= 0) {
                    continue;
                }
                final int consumed = Math.min(needed, available);
                toConsume[i] += consumed;
                needed -= consumed;
            }
            if (needed > 0) {
                return false;
            }
        }

        for (int i = 0; i < slots.length; i++) {
            if (toConsume[i] <= 0) {
                continue;
            }
            final int slot = slots[i];
            final ItemStack current = handler.getStackInSlot(slot).copy();
            current.shrink(toConsume[i]);
            handler.setStackInSlot(slot, current.isEmpty() ? ItemStack.EMPTY : current);
        }

        return true;
    }

    private boolean consumeFluidInputs(final List<FluidStack> requirements) {
        final int[] tankIndices = new int[]{TANK_INPUT_1, TANK_INPUT_2, TANK_INPUT_3};
        final int[] toDrain = new int[tankIndices.length];

        for (final FluidStack requirement : requirements) {
            if (requirement.isEmpty()) {
                continue;
            }
            int needed = requirement.getAmount();
            for (int i = 0; i < tankIndices.length && needed > 0; i++) {
                final HbmFluidTank tank = this.getFluidTank(tankIndices[i]);
                if (tank == null || tank.isEmpty() || !tank.getFluid().isFluidEqual(requirement)) {
                    continue;
                }
                final int available = Math.max(0, tank.getFluidAmount() - toDrain[i]);
                if (available <= 0) {
                    continue;
                }
                final int drained = Math.min(needed, available);
                toDrain[i] += drained;
                needed -= drained;
            }
            if (needed > 0) {
                return false;
            }
        }

        for (int i = 0; i < tankIndices.length; i++) {
            if (toDrain[i] <= 0) {
                continue;
            }
            final HbmFluidTank tank = this.getFluidTank(tankIndices[i]);
            if (tank != null) {
                tank.drain(toDrain[i], IFluidHandler.FluidAction.EXECUTE);
            }
        }

        return true;
    }

    private boolean canFitItemOutputs(final List<ItemStack> outputs) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        for (final ItemStack out : outputs) {
            int remaining = out.getCount();

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_6 && remaining > 0; slot++) {
                final ItemStack existing = handler.getStackInSlot(slot);
                if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, out)) {
                    remaining -= Math.max(0, existing.getMaxStackSize() - existing.getCount());
                }
            }

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_6 && remaining > 0; slot++) {
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

    private void insertItemOutputs(final List<ItemStack> outputs) {
        final ItemStackHandler handler = this.getInternalItemHandler();

        for (final ItemStack out : outputs) {
            int remaining = out.getCount();

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_6 && remaining > 0; slot++) {
                final ItemStack existing = handler.getStackInSlot(slot);
                if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, out)) {
                    final int moved = Math.min(remaining, existing.getMaxStackSize() - existing.getCount());
                    if (moved > 0) {
                        final ItemStack grown = existing.copy();
                        grown.grow(moved);
                        handler.setStackInSlot(slot, grown);
                        remaining -= moved;
                    }
                }
            }

            for (int slot = SLOT_OUTPUT_1; slot <= SLOT_OUTPUT_6 && remaining > 0; slot++) {
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

    private boolean canFitFluidOutputs(final List<FluidStack> outputs) {
        if (outputs.isEmpty()) {
            return true;
        }

        final HbmFluidTank outputTank = this.getFluidTank(TANK_OUTPUT);
        if (outputTank == null) {
            return false;
        }

        int simulatedAmount = outputTank.getFluidAmount();
        FluidStack simulatedFluid = outputTank.isEmpty() ? FluidStack.EMPTY : outputTank.getFluid().copy();

        for (final FluidStack output : outputs) {
            if (output.isEmpty()) {
                continue;
            }

            if (simulatedFluid.isEmpty()) {
                simulatedFluid = output.copy();
                simulatedAmount = output.getAmount();
                if (simulatedAmount > outputTank.getCapacity()) {
                    return false;
                }
                continue;
            }

            if (!simulatedFluid.isFluidEqual(output)) {
                return false;
            }

            simulatedAmount += output.getAmount();
            if (simulatedAmount > outputTank.getCapacity()) {
                return false;
            }
        }

        return true;
    }

    private void insertFluidOutputs(final List<FluidStack> outputs) {
        final HbmFluidTank outputTank = this.getFluidTank(TANK_OUTPUT);
        if (outputTank == null) {
            return;
        }

        for (final FluidStack output : outputs) {
            if (output.isEmpty()) {
                continue;
            }
            outputTank.fill(output.copy(), IFluidHandler.FluidAction.EXECUTE);
        }
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

    private boolean isAcceptedInputFluid(final FluidStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        return HbmPurexRecipes.all().stream()
            .flatMap(recipe -> recipe.fluidInputs().stream())
            .filter(required -> !required.isEmpty())
            .anyMatch(required -> required.isFluidEqual(stack));
    }

    private boolean isAcceptedOutputFluid(final FluidStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        return HbmPurexRecipes.all().stream()
            .flatMap(recipe -> recipe.fluidOutputs().stream())
            .filter(required -> !required.isEmpty())
            .anyMatch(required -> required.isFluidEqual(stack));
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot == SLOT_BATTERY) {
            return stack.getItem() instanceof BatteryItem;
        }
        if (slot == SLOT_BLUEPRINT) {
            return stack.getItem() instanceof BlueprintItem;
        }
        if (slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2) {
            if (!(stack.getItem() instanceof MachineUpgradeItem upgrade)) {
                return false;
            }
            return this.getValidUpgrades().containsKey(upgrade.type());
        }
        if (slot >= SLOT_INPUT_1 && slot <= SLOT_INPUT_3) {
            return this.isAcceptedInputItem(stack);
        }
        return false;
    }

    private boolean isAcceptedInputItem(final ItemStack stack) {
        return HbmPurexRecipes.all().stream()
            .flatMap(recipe -> recipe.itemInputs().stream())
            .anyMatch(ingredient -> ingredient.ingredient().test(stack));
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return slot >= SLOT_OUTPUT_1 && slot <= SLOT_OUTPUT_6;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return SLOT_ACCESS;
    }

    @Override
    protected boolean isUpgradeSlot(final int slot) {
        return slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_PUREX.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new PurexMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getProcessTime() {
        return this.processTime;
    }

    public int getLastConsumption() {
        return this.lastConsumption;
    }

    public int getDisplayMaxPower() {
        return this.displayMaxPower;
    }

    public boolean hasRecipe() {
        return this.hasRecipe;
    }

    public boolean canProcessRecipe() {
        return this.canProcessRecipe;
    }

    public boolean hasPowerForRecipe() {
        return this.hasPowerForRecipe;
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
        tag.putInt("progress", this.progress);
        tag.putInt("processTime", this.processTime);
        tag.putInt("consumption", this.lastConsumption);
        tag.putInt("displayMaxPower", this.displayMaxPower);
        tag.putBoolean("hasRecipe", this.hasRecipe);
        tag.putBoolean("canProcess", this.canProcessRecipe);
        tag.putBoolean("hasPower", this.hasPowerForRecipe);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.processTime = Math.max(1, tag.getInt("processTime"));
        this.lastConsumption = Math.max(0, tag.getInt("consumption"));
        this.displayMaxPower = Math.max(1, tag.getInt("displayMaxPower"));
        this.hasRecipe = tag.getBoolean("hasRecipe");
        this.canProcessRecipe = tag.getBoolean("canProcess");
        this.hasPowerForRecipe = tag.getBoolean("hasPower");
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("processTime", this.processTime);
        tag.putInt("energy", this.getStoredEnergy());
        tag.putInt("maxEnergy", this.getDisplayMaxPower());
        tag.putInt("consumption", this.lastConsumption);
        tag.putBoolean("hasRecipe", this.hasRecipe);
        tag.putBoolean("canProcess", this.canProcessRecipe);
        tag.putBoolean("hasPower", this.hasPowerForRecipe);

        for (int tank = TANK_INPUT_1; tank <= TANK_OUTPUT; tank++) {
            tag.putInt("fluid" + tank + "Amount", this.getTankAmount(tank));
            tag.putInt("fluid" + tank + "Capacity", this.getTankCapacity(tank));
            tag.putString("fluid" + tank + "Name", this.getTankFluidName(tank));
        }
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.processTime = Math.max(1, tag.getInt("processTime"));
        this.lastConsumption = Math.max(0, tag.getInt("consumption"));
        this.displayMaxPower = Math.max(1, tag.getInt("maxEnergy"));
        this.hasRecipe = tag.getBoolean("hasRecipe");
        this.canProcessRecipe = tag.getBoolean("canProcess");
        this.hasPowerForRecipe = tag.getBoolean("hasPower");
    }

    @Override
    public Map<MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of(
            MachineUpgradeItem.UpgradeType.SPEED, 3,
            MachineUpgradeItem.UpgradeType.POWER, 3,
            MachineUpgradeItem.UpgradeType.OVERDRIVE, 3
        );
    }
}

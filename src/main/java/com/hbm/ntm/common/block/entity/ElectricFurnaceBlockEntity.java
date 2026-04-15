package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.block.ElectricFurnaceBlock;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.menu.ElectricFurnaceMenu;
import com.hbm.ntm.common.pollution.PollutionType;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class ElectricFurnaceBlockEntity extends MachineBlockEntity {
    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_INPUT = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_UPGRADE = 3;
    public static final int SLOT_COUNT = 4;

    private static final int[] SLOT_ACCESS = new int[]{SLOT_BATTERY, SLOT_INPUT, SLOT_OUTPUT};
    private static final int MAX_POWER = 100_000;
    private static final int BASE_PROGRESS = 100;
    private static final int BASE_CONSUMPTION = 50;

    private int progress;
    private int maxProgress = BASE_PROGRESS;
    private int consumption = BASE_CONSUMPTION;
    private int cooldown;

    public ElectricFurnaceBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_ELECTRIC_FURNACE.get(), pos, state, SLOT_COUNT);
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        return this.createSimpleEnergyStorage(MAX_POWER, MAX_POWER, 0);
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.RECEIVE;
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final ElectricFurnaceBlockEntity furnace) {
        boolean dirty = false;

        if (furnace.cooldown > 0) {
            furnace.cooldown--;
            dirty = true;
        }

        if (furnace.tryChargeFromBattery()) {
            dirty = true;
        }

        furnace.recomputeParameters();
        final @Nullable SmeltingRecipe recipe = furnace.findRecipe(furnace.getInternalItemHandler().getStackInSlot(SLOT_INPUT));
        final boolean hasPower = furnace.hasPower();
        if (!hasPower) {
            furnace.cooldown = 20;
        }

        final boolean canProcess = recipe != null && furnace.canProcess(recipe);
        if (hasPower && canProcess) {
            furnace.progress++;
            furnace.consumeEnergy(furnace.consumption);
            furnace.emitPeriodicPollution(PollutionType.SOOT, MACHINE_SOOT_PER_SECOND);
            dirty = true;

            if (furnace.progress >= furnace.maxProgress) {
                furnace.progress = 0;
                furnace.processRecipe(recipe);
            }
        } else if (furnace.progress > 0) {
            furnace.progress = 0;
            dirty = true;
        }

        furnace.updateLitState(furnace.progress > 0);

        if (dirty) {
            furnace.markChangedAndSync();
        }
        furnace.tickMachineStateSync();
    }

    private void recomputeParameters() {
        final int speedLevel = this.countUpgrades(MachineUpgradeItem.UpgradeType.SPEED);
        final int powerLevel = this.countUpgrades(MachineUpgradeItem.UpgradeType.POWER);

        this.maxProgress = BASE_PROGRESS;
        this.consumption = BASE_CONSUMPTION;

        this.maxProgress -= speedLevel * 25;
        this.consumption += speedLevel * 50;

        this.maxProgress += powerLevel * 10;
        this.consumption -= powerLevel * 15;

        this.maxProgress = Math.max(1, this.maxProgress);
        this.consumption = Math.max(1, this.consumption);
    }

    private @Nullable SmeltingRecipe findRecipe(final ItemStack input) {
        if (this.level == null || input.isEmpty()) {
            return null;
        }
        return this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(input.copyWithCount(1)), this.level).orElse(null);
    }

    private boolean canProcess(final SmeltingRecipe recipe) {
        if (this.cooldown > 0 || this.level == null) {
            return false;
        }

        if (this.getInternalItemHandler().getStackInSlot(SLOT_INPUT).isEmpty()) {
            return false;
        }

        final ItemStack result = recipe.getResultItem(this.level.registryAccess());
        if (result.isEmpty()) {
            return false;
        }

        final ItemStack existing = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (existing.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameTags(existing, result)) {
            return false;
        }
        return existing.getCount() + result.getCount() <= existing.getMaxStackSize();
    }

    private void processRecipe(final SmeltingRecipe recipe) {
        if (this.level == null) {
            return;
        }

        final ItemStack result = recipe.getResultItem(this.level.registryAccess()).copy();
        if (result.isEmpty()) {
            return;
        }

        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack existing = handler.getStackInSlot(SLOT_OUTPUT);
        if (existing.isEmpty()) {
            handler.setStackInSlot(SLOT_OUTPUT, result);
        } else {
            final ItemStack merged = existing.copy();
            merged.grow(result.getCount());
            handler.setStackInSlot(SLOT_OUTPUT, merged);
        }

        final ItemStack reducedInput = handler.getStackInSlot(SLOT_INPUT).copy();
        reducedInput.shrink(1);
        handler.setStackInSlot(SLOT_INPUT, reducedInput.isEmpty() ? ItemStack.EMPTY : reducedInput);
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

        final int toDischarge = Math.min(Math.min(space, batteryItem.getDischargeRate()), batteryItem.getStoredEnergy(battery));
        if (toDischarge <= 0) {
            return false;
        }

        batteryItem.withStoredEnergy(battery, batteryItem.getStoredEnergy(battery) - toDischarge);
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

    private void updateLitState(final boolean active) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        final BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof ElectricFurnaceBlock) || !state.hasProperty(ElectricFurnaceBlock.LIT)) {
            return;
        }
        if (state.getValue(ElectricFurnaceBlock.LIT) == active) {
            return;
        }
        this.level.setBlock(this.worldPosition, state.setValue(ElectricFurnaceBlock.LIT, active), 3);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (slot == SLOT_BATTERY) {
            return stack.getItem() instanceof BatteryItem;
        }
        if (slot == SLOT_INPUT) {
            return this.findRecipe(stack) != null;
        }
        if (slot == SLOT_UPGRADE) {
            if (!(stack.getItem() instanceof MachineUpgradeItem upgrade)) {
                return false;
            }
            return this.getValidUpgrades().containsKey(upgrade.type());
        }
        return false;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        if (slot == SLOT_OUTPUT) {
            return true;
        }
        if (slot == SLOT_BATTERY) {
            final ItemStack stack = this.getInternalItemHandler().getStackInSlot(SLOT_BATTERY);
            return !stack.isEmpty() && stack.getItem() instanceof BatteryItem battery && battery.getStoredEnergy(stack) <= 0;
        }
        return false;
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
        return Component.translatable(HbmBlocks.MACHINE_ELECTRIC_FURNACE.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new ElectricFurnaceMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public int getCurrentConsumption() {
        return this.consumption;
    }

    public boolean hasPower() {
        return this.getStoredEnergy() >= this.consumption;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("maxProgress", this.maxProgress);
        tag.putInt("consumption", this.consumption);
        tag.putInt("cooldown", this.cooldown);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.maxProgress = Math.max(1, tag.getInt("maxProgress"));
        this.consumption = Math.max(1, tag.getInt("consumption"));
        this.cooldown = Math.max(0, tag.getInt("cooldown"));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("maxProgress", this.maxProgress);
        tag.putInt("energy", this.getStoredEnergy());
        tag.putInt("maxEnergy", this.getMaxStoredEnergy());
        tag.putInt("consumption", this.consumption);
        tag.putBoolean("hasPower", this.hasPower());
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.maxProgress = Math.max(1, tag.getInt("maxProgress"));
        this.consumption = Math.max(1, tag.getInt("consumption"));
    }

    @Override
    public Map<MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of(
            MachineUpgradeItem.UpgradeType.SPEED, 3,
            MachineUpgradeItem.UpgradeType.POWER, 3
        );
    }
}
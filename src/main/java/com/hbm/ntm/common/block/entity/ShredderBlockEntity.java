package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.ShredderBladesItem;
import com.hbm.ntm.common.menu.ShredderMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.shredder.HbmShredderRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Shredder machine block entity — direct port of legacy {@code TileEntityMachineShredder}.
 * <p>
 * Legacy constants (verified against {@code TileEntityMachineShredder.java}):
 * <ul>
 *   <li>{@code maxPower = 10000}</li>
 *   <li>{@code processingSpeed = 60} ticks</li>
 *   <li>Power drain: 5 per tick during processing</li>
 *   <li>30 slots total: 0-8 input, 9-26 output, 27 left blade, 28 right blade, 29 battery</li>
 *   <li>Sound: vanilla minecart rolling, every 50 ticks while processing</li>
 * </ul>
 */
@SuppressWarnings("null")
public class ShredderBlockEntity extends MachineBlockEntity {

    // --- Legacy-matched slot layout ---
    public static final int SLOT_INPUT_START = 0;
    public static final int SLOT_INPUT_END = 9;           // exclusive
    public static final int INPUT_COUNT = 9;               // 3×3 grid
    public static final int SLOT_OUTPUT_START = 9;
    public static final int SLOT_OUTPUT_END = 27;          // exclusive
    public static final int OUTPUT_COUNT = 18;             // 3×6 grid
    public static final int SLOT_BLADE_LEFT = 27;
    public static final int SLOT_BLADE_RIGHT = 28;
    public static final int SLOT_BATTERY = 29;
    public static final int SLOT_COUNT = 30;

    // --- Legacy-matched constants ---
    public static final int MAX_POWER = 10_000;            // legacy: maxPower = 10000
    public static final int PROCESSING_SPEED = 60;         // legacy: processingSpeed = 60
    public static final int POWER_PER_TICK = 5;            // legacy: power -= 5

    // --- Runtime state ---
    private int progress;
    private int soundTimer;

    public ShredderBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_SHREDDER.get(), pos, state, SLOT_COUNT);
    }

    // ===== Energy storage (receive-only, legacy: IEnergyReceiverMK2) =====

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        return this.createSimpleEnergyStorage(MAX_POWER, MAX_POWER, 0);
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.RECEIVE;
    }

    // ===== Tick logic — mirrors legacy updateEntity() exactly =====

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final ShredderBlockEntity shredder) {
        HbmShredderRecipes.ensureInitialized();
        boolean dirty = false;

        // Charge from battery (legacy: Library.chargeTEFromItems)
        if (shredder.tryChargeFromBattery()) {
            dirty = true;
        }

        final boolean hasPower = shredder.getStoredEnergy() >= POWER_PER_TICK;
        final boolean canProcess = hasPower && shredder.canProcess();

        if (canProcess) {
            shredder.progress++;
            shredder.consumeEnergy(POWER_PER_TICK);
            dirty = true;

            if (shredder.progress >= PROCESSING_SPEED) {
                shredder.damageBlades();
                shredder.processAllInputSlots();
                shredder.progress = 0;
            }

            // Sound every 50 ticks while processing (legacy: worldObj.playSoundEffect(...VANILLA_MINECART...))
            shredder.soundTimer++;
            if (shredder.soundTimer >= 50) {
                shredder.soundTimer = 0;
                level.playSound(null, pos, SoundEvents.MINECART_RIDING, SoundSource.BLOCKS,
                    shredder.getVolume(1.0F), 1.0F);
            }
        } else {
            if (shredder.progress > 0) {
                shredder.progress = 0;
                dirty = true;
            }
            shredder.soundTimer = 0;
        }

        if (dirty) {
            shredder.markChangedAndSync();
        }
    }

    // ===== Legacy canProcess() — both blades must be present and not broken =====

    private boolean canProcess() {
        final int gearLeft = getGearState(SLOT_BLADE_LEFT);
        final int gearRight = getGearState(SLOT_BLADE_RIGHT);

        // Legacy: gearLeft == 0 || gearLeft == 3 → cannot process
        if (gearLeft == 0 || gearLeft == 3) return false;
        if (gearRight == 0 || gearRight == 3) return false;

        // At least one input slot must have a valid recipe with space in output
        for (int i = SLOT_INPUT_START; i < SLOT_INPUT_END; i++) {
            final ItemStack input = this.getInternalItemHandler().getStackInSlot(i);
            if (input.isEmpty()) continue;
            final ItemStack result = HbmShredderRecipes.getResult(input);
            if (result != null && canFitOutput(result)) {
                return true;
            }
        }
        return false;
    }

    // ===== Legacy processItem() — processes each input slot independently =====

    private void processAllInputSlots() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        for (int i = SLOT_INPUT_START; i < SLOT_INPUT_END; i++) {
            final ItemStack input = handler.getStackInSlot(i);
            if (input.isEmpty()) continue;

            final ItemStack result = HbmShredderRecipes.getResult(input);
            if (result == null || !canFitOutput(result)) continue;

            // Decrement input
            final ItemStack shrunk = input.copy();
            shrunk.shrink(1);
            handler.setStackInSlot(i, shrunk.isEmpty() ? ItemStack.EMPTY : shrunk);

            // Place output — try merge into existing stacks first, then empty slots (legacy behavior)
            mergeIntoOutputSlots(result);
        }
    }

    private boolean canFitOutput(final ItemStack result) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        for (int i = SLOT_OUTPUT_START; i < SLOT_OUTPUT_END; i++) {
            final ItemStack existing = handler.getStackInSlot(i);
            if (existing.isEmpty()) return true;
            if (ItemStack.isSameItemSameTags(existing, result)
                && existing.getCount() + result.getCount() <= existing.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private void mergeIntoOutputSlots(final ItemStack result) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        int remaining = result.getCount();

        // First pass: merge into matching stacks (legacy behavior)
        for (int i = SLOT_OUTPUT_START; i < SLOT_OUTPUT_END && remaining > 0; i++) {
            final ItemStack existing = handler.getStackInSlot(i);
            if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, result)) {
                final int toAdd = Math.min(remaining, existing.getMaxStackSize() - existing.getCount());
                if (toAdd > 0) {
                    final ItemStack grown = existing.copy();
                    grown.grow(toAdd);
                    handler.setStackInSlot(i, grown);
                    remaining -= toAdd;
                }
            }
        }

        // Second pass: place into empty slots
        for (int i = SLOT_OUTPUT_START; i < SLOT_OUTPUT_END && remaining > 0; i++) {
            final ItemStack existing = handler.getStackInSlot(i);
            if (existing.isEmpty()) {
                final ItemStack placed = result.copy();
                placed.setCount(remaining);
                handler.setStackInSlot(i, placed);
                remaining = 0;
            }
        }
    }

    // ===== Blade damage — legacy: damages both blades by 1 per operation =====

    private void damageBlades() {
        damageBlade(SLOT_BLADE_LEFT);
        damageBlade(SLOT_BLADE_RIGHT);
    }

    private void damageBlade(final int slot) {
        final ItemStack blade = this.getInternalItemHandler().getStackInSlot(slot);
        if (blade.isEmpty() || !blade.isDamageableItem()) {
            return; // legacy: maxDamage == 0 means infinite (desh blades)
        }
        final ItemStack damaged = blade.copy();
        damaged.setDamageValue(damaged.getDamageValue() + 1);
        if (damaged.getDamageValue() >= damaged.getMaxDamage()) {
            // Legacy: blade remains in slot at max damage (state 3 = broken), NOT destroyed
            this.getInternalItemHandler().setStackInSlot(slot, damaged);
        } else {
            this.getInternalItemHandler().setStackInSlot(slot, damaged);
        }
    }

    // ===== Gear state — legacy: 0 = empty, 1 = good, 2 = worn, 3 = broken =====

    /**
     * Returns the gear state for the given blade slot, matching legacy behavior:
     * <ul>
     *   <li>0 — no blade in slot</li>
     *   <li>1 — blade present and healthy (damage &lt; maxDamage/2, or infinite durability)</li>
     *   <li>2 — blade present but worn (damage &ge; maxDamage/2 but not fully broken)</li>
     *   <li>3 — blade present but fully broken (damage == maxDamage)</li>
     * </ul>
     */
    public int getGearState(final int slot) {
        final ItemStack blade = this.getInternalItemHandler().getStackInSlot(slot);
        if (blade.isEmpty() || !(blade.getItem() instanceof ShredderBladesItem)) {
            return 0;
        }
        if (!blade.isDamageableItem()) {
            return 1; // infinite durability (desh blades) — always good
        }
        if (blade.getDamageValue() >= blade.getMaxDamage()) {
            return 3; // fully broken
        }
        if (blade.getDamageValue() >= blade.getMaxDamage() / 2) {
            return 2; // worn
        }
        return 1; // good
    }

    public int getGearLeft() {
        return getGearState(SLOT_BLADE_LEFT);
    }

    public int getGearRight() {
        return getGearState(SLOT_BLADE_RIGHT);
    }

    // ===== Battery charging — mirrors legacy Library.chargeTEFromItems =====

    private boolean tryChargeFromBattery() {
        final ItemStack battery = this.getInternalItemHandler().getStackInSlot(SLOT_BATTERY);
        if (battery.isEmpty()) return false;

        if (battery.getItem() instanceof final BatteryItem batteryItem) {
            final int stored = this.getStoredEnergy();
            final int space = this.getMaxStoredEnergy() - stored;
            if (space <= 0) return false;
            final int rate = batteryItem.getDischargeRate();
            final int charge = batteryItem.getStoredEnergy(battery);
            final int toDischarge = Math.min(Math.min(space, rate), charge);
            if (toDischarge <= 0) return false;
            batteryItem.withStoredEnergy(battery, charge - toDischarge);
            this.receiveEnergy(toDischarge);
            return true;
        }
        return false;
    }

    private void consumeEnergy(final int amount) {
        final var storage = this.getEnergyStorage(null);
        if (storage != null) {
            storage.extractEnergy(amount, false);
        }
    }

    private void receiveEnergy(final int amount) {
        final var storage = this.getEnergyStorage(null);
        if (storage != null) {
            storage.receiveEnergy(amount, false);
        }
    }

    // ===== Slot access control — legacy ISidedInventory behavior =====

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (slot >= SLOT_INPUT_START && slot < SLOT_INPUT_END) return true;
        if (slot == SLOT_BLADE_LEFT || slot == SLOT_BLADE_RIGHT) return stack.getItem() instanceof ShredderBladesItem;
        if (slot == SLOT_BATTERY) return stack.getItem() instanceof BatteryItem;
        return false; // output slots
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        // Legacy: output slots always extractable; blades extractable only when broken
        if (slot >= SLOT_OUTPUT_START && slot < SLOT_OUTPUT_END) return true;
        if (slot == SLOT_BLADE_LEFT || slot == SLOT_BLADE_RIGHT) return getGearState(slot) == 3;
        return false;
    }

    // ===== Data sync — progress, gearLeft, gearRight =====

    public int getProgress() {
        return this.progress;
    }

    public void setClientProgress(final int progress) {
        this.progress = progress;
    }

    // ===== Menu / display =====

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_SHREDDER.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new ShredderMenu(containerId, inventory, this);
    }

    // ===== Save / load =====

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("progress", this.progress);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = tag.getInt("progress");
    }
}

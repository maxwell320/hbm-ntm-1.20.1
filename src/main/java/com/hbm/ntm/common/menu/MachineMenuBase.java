package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.MachineBlockEntity;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public abstract class MachineMenuBase<T extends MachineBlockEntity> extends AbstractContainerMenu {
    protected final T machine;
    protected final Inventory playerInventory;
    protected final int machineSlotCount;
    private boolean maintenanceBlocked;
    private int maintenanceLevel;
    private List<ItemStack> repairMaterials = List.of();
    private boolean syncedHasEnergyStorage;
    private int syncedEnergyStored;
    private int syncedEnergyCapacity;
    private int syncedFluidTankCount;
    private int syncedFluidStoredTotal;
    private int syncedFluidCapacityTotal;
    private final Map<Integer, Integer> syncedTankAmounts = new HashMap<>();
    private final Map<Integer, Integer> syncedTankCapacities = new HashMap<>();
    private final Map<Integer, String> syncedTankFluids = new HashMap<>();
    private int syncedInventorySlotCount;
    private int syncedInventoryOccupiedSlots;
    private int syncedInventoryItemTotal;

    protected MachineMenuBase(final MenuType<?> menuType, final int containerId, final Inventory playerInventory, final T machine, final int machineSlotCount) {
        super(menuType, containerId);
        this.machine = machine;
        this.playerInventory = playerInventory;
        this.machineSlotCount = machineSlotCount;
    }

    protected final void addPlayerInventory(final Inventory inventory, final int left, final int top) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, left + column * 18, top + row * 18));
            }
        }
        for (int hotbar = 0; hotbar < 9; hotbar++) {
            this.addSlot(new Slot(inventory, hotbar, left + hotbar * 18, top + 58));
        }
    }

    protected final void addMachineDataSlots(final ContainerData dataSlots) {
        this.addDataSlots(dataSlots);
    }

    protected final void addGridSlots(final IItemHandler itemHandler,
                                      final int fromSlot,
                                      final int x,
                                      final int y,
                                      final int rows,
                                      final int columns) {
        this.addGridSlots(itemHandler, fromSlot, x, y, rows, columns, 18);
    }

    protected final void addGridSlots(final IItemHandler itemHandler,
                                      final int fromSlot,
                                      final int x,
                                      final int y,
                                      final int rows,
                                      final int columns,
                                      final int slotStep) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                final int slot = fromSlot + row * columns + column;
                this.addSlot(new SlotItemHandler(itemHandler, slot, x + column * slotStep, y + row * slotStep));
            }
        }
    }

    protected final PatternSlotItemHandler addPatternSlot(final IItemHandler itemHandler,
                                                          final int slot,
                                                          final int x,
                                                          final int y) {
        final PatternSlotItemHandler patternSlot = new PatternSlotItemHandler(itemHandler, slot, x, y);
        this.addSlot(patternSlot);
        return patternSlot;
    }

    protected final PatternSlotItemHandler addPatternSlot(final IItemHandler itemHandler,
                                                          final int slot,
                                                          final int x,
                                                          final int y,
                                                          final boolean allowStackSize) {
        final PatternSlotItemHandler patternSlot = new PatternSlotItemHandler(itemHandler, slot, x, y, allowStackSize);
        this.addSlot(patternSlot);
        return patternSlot;
    }

    protected final PatternSlotItemHandler addTemplateSlot(final IItemHandler itemHandler,
                                                           final int slot,
                                                           final int x,
                                                           final int y) {
        return this.addPatternSlot(itemHandler, slot, x, y, false);
    }

    protected final PatternSlotItemHandler addTemplateSlot(final IItemHandler itemHandler,
                                                           final int slot,
                                                           final int x,
                                                           final int y,
                                                           final boolean allowStackSize) {
        return this.addPatternSlot(itemHandler, slot, x, y, allowStackSize);
    }

    protected final GhostSlotItemHandler addGhostSlot(final IItemHandler itemHandler,
                                                      final int slot,
                                                      final int x,
                                                      final int y) {
        final GhostSlotItemHandler ghostSlot = new GhostSlotItemHandler(itemHandler, slot, x, y);
        this.addSlot(ghostSlot);
        return ghostSlot;
    }

    protected final GhostSlotItemHandler addGhostSlot(final IItemHandler itemHandler,
                                                      final int slot,
                                                      final int x,
                                                      final int y,
                                                      final boolean allowStackSize) {
        final GhostSlotItemHandler ghostSlot = new GhostSlotItemHandler(itemHandler, slot, x, y, allowStackSize);
        this.addSlot(ghostSlot);
        return ghostSlot;
    }

    protected final UpgradeSlotItemHandler addUpgradeSlot(final IItemHandler itemHandler,
                                                          final int slot,
                                                          final int x,
                                                          final int y) {
        final UpgradeSlotItemHandler upgradeSlot = new UpgradeSlotItemHandler(itemHandler, slot, x, y);
        this.addSlot(upgradeSlot);
        return upgradeSlot;
    }

    protected final UpgradeSlotItemHandler addUpgradeSlot(final IItemHandler itemHandler,
                                                          final int slot,
                                                          final int x,
                                                          final int y,
                                                          final MachineUpgradeItem.UpgradeType... allowedTypes) {
        final UpgradeSlotItemHandler upgradeSlot = new UpgradeSlotItemHandler(itemHandler, slot, x, y, allowedTypes);
        this.addSlot(upgradeSlot);
        return upgradeSlot;
    }

    protected final void addFilteredGridSlots(final IItemHandler itemHandler,
                                              final int fromSlot,
                                              final int x,
                                              final int y,
                                              final int rows,
                                              final int columns,
                                              final BiPredicate<Integer, ItemStack> validator) {
        this.addFilteredGridSlots(itemHandler, fromSlot, x, y, rows, columns, 18, validator);
    }

    protected final void addFilteredGridSlots(final IItemHandler itemHandler,
                                              final int fromSlot,
                                              final int x,
                                              final int y,
                                              final int rows,
                                              final int columns,
                                              final int slotStep,
                                              final BiPredicate<Integer, ItemStack> validator) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                final int slot = fromSlot + row * columns + column;
                this.addSlot(new FilteredSlotItemHandler(itemHandler, slot, x + column * slotStep, y + row * slotStep, validator));
            }
        }
    }

    protected final void addOutputGridSlots(final IItemHandler itemHandler,
                                            final int fromSlot,
                                            final int x,
                                            final int y,
                                            final int rows,
                                            final int columns) {
        this.addOutputGridSlots(itemHandler, fromSlot, x, y, rows, columns, 18);
    }

    protected final void addOutputGridSlots(final IItemHandler itemHandler,
                                            final int fromSlot,
                                            final int x,
                                            final int y,
                                            final int rows,
                                            final int columns,
                                            final int slotStep) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                final int slot = fromSlot + row * columns + column;
                this.addSlot(new OutputSlotItemHandler(itemHandler, slot, x + column * slotStep, y + row * slotStep));
            }
        }
    }

    @Override
    public boolean stillValid(final @NotNull Player player) {
        return this.machine != null && this.machine.canPlayerControl(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(final @NotNull Player player, final int index) {
        if (index < 0 || index >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        final Slot slot = this.slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }

        final ItemStack original = slot.getItem();
        final ItemStack copy = original.copy();
        if (index < this.machineSlotCount) {
            if (!this.moveItemStackTo(original, this.machineSlotCount, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveToMachineSlots(original)) {
            return ItemStack.EMPTY;
        }
        if (original.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (original.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, original);
        return copy;
    }

    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (this.machineSlotCount <= 0 || this.machineSlotCount > this.slots.size()) {
            return false;
        }
        return this.moveItemStackTo(stack, 0, this.machineSlotCount, false);
    }

    protected final boolean moveToMachineRange(final ItemStack stack, final int startInclusive, final int endExclusive) {
        if (startInclusive < 0 || endExclusive <= startInclusive || endExclusive > this.slots.size()) {
            return false;
        }
        return this.moveItemStackTo(stack, startInclusive, endExclusive, false);
    }

    protected final boolean isUpgradeItem(final ItemStack stack, final MachineUpgradeItem.UpgradeType... allowed) {
        if (!(stack.getItem() instanceof MachineUpgradeItem upgrade)) {
            return false;
        }
        if (allowed == null || allowed.length == 0) {
            return true;
        }
        final EnumSet<MachineUpgradeItem.UpgradeType> allowedSet = EnumSet.noneOf(MachineUpgradeItem.UpgradeType.class);
        allowedSet.addAll(Arrays.asList(allowed));
        return allowedSet.contains(upgrade.type());
    }

    public T machine() {
        return this.machine;
    }

    public final boolean applyMachineStateSync(final BlockPos pos, final CompoundTag data) {
        if (this.machine == null || !this.machine.getBlockPos().equals(pos)) {
            return false;
        }
        this.readSharedMachineStateSync(data);
        this.readMachineStateSync(data);
        return true;
    }

    private void readSharedMachineStateSync(final CompoundTag data) {
        this.maintenanceBlocked = data.getBoolean("maintenanceBlocked");
        if (data.contains("maintenanceLevel", Tag.TAG_INT)) {
            this.maintenanceLevel = data.getInt("maintenanceLevel");
        }
        this.syncedHasEnergyStorage = data.getBoolean("hasEnergyStorage");
        this.syncedEnergyStored = Math.max(0, data.getInt("energyStored"));
        this.syncedEnergyCapacity = Math.max(0, data.getInt("energyCapacity"));
        this.syncedFluidTankCount = Math.max(0, data.getInt("fluidTankCount"));
        this.syncedFluidStoredTotal = Math.max(0, data.getInt("fluidStoredTotal"));
        this.syncedFluidCapacityTotal = Math.max(0, data.getInt("fluidCapacityTotal"));

        this.syncedTankAmounts.clear();
        this.syncedTankCapacities.clear();
        this.syncedTankFluids.clear();
        if (data.contains("syncTanks", Tag.TAG_LIST)) {
            final ListTag tanksTag = data.getList("syncTanks", Tag.TAG_COMPOUND);
            for (int i = 0; i < tanksTag.size(); i++) {
                final CompoundTag tankTag = tanksTag.getCompound(i);
                final int slot = tankTag.getInt("slot");
                if (slot < 0) {
                    continue;
                }
                this.syncedTankAmounts.put(slot, Math.max(0, tankTag.getInt("amount")));
                this.syncedTankCapacities.put(slot, Math.max(0, tankTag.getInt("capacity")));
                final String fluid = tankTag.getString("fluid");
                if (!fluid.isBlank()) {
                    this.syncedTankFluids.put(slot, fluid);
                }
            }
        }

        this.syncedInventorySlotCount = Math.max(0, data.getInt("inventorySlotCount"));
        this.syncedInventoryOccupiedSlots = Math.max(0, data.getInt("inventoryOccupiedSlots"));
        this.syncedInventoryItemTotal = Math.max(0, data.getInt("inventoryItemTotal"));

        if (!data.contains("repairMaterials", Tag.TAG_LIST)) {
            this.repairMaterials = List.of();
            return;
        }

        final ListTag listTag = data.getList("repairMaterials", Tag.TAG_COMPOUND);
        if (listTag.isEmpty()) {
            this.repairMaterials = List.of();
            return;
        }

        final List<ItemStack> synced = new ArrayList<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            final ItemStack stack = ItemStack.of(listTag.getCompound(i));
            if (!stack.isEmpty()) {
                synced.add(stack);
            }
        }
        this.repairMaterials = List.copyOf(synced);
    }

    public boolean isMaintenanceBlocked() {
        return this.maintenanceBlocked;
    }

    public int maintenanceLevel() {
        return this.maintenanceLevel;
    }

    public List<ItemStack> repairMaterials() {
        return this.repairMaterials;
    }

    public boolean syncedHasEnergyStorage() {
        return this.syncedHasEnergyStorage;
    }

    public int syncedEnergyStored() {
        return this.syncedEnergyStored;
    }

    public int syncedEnergyCapacity() {
        return this.syncedEnergyCapacity;
    }

    public int syncedFluidTankCount() {
        return this.syncedFluidTankCount;
    }

    public int syncedFluidStoredTotal() {
        return this.syncedFluidStoredTotal;
    }

    public int syncedFluidCapacityTotal() {
        return this.syncedFluidCapacityTotal;
    }

    public int syncedTankAmount(final int tankIndex) {
        return this.syncedTankAmounts.getOrDefault(tankIndex, 0);
    }

    public int syncedTankCapacity(final int tankIndex) {
        return this.syncedTankCapacities.getOrDefault(tankIndex, 0);
    }

    public String syncedTankFluid(final int tankIndex) {
        return this.syncedTankFluids.getOrDefault(tankIndex, "");
    }

    public int syncedInventorySlotCount() {
        return this.syncedInventorySlotCount;
    }

    public int syncedInventoryOccupiedSlots() {
        return this.syncedInventoryOccupiedSlots;
    }

    public int syncedInventoryItemTotal() {
        return this.syncedInventoryItemTotal;
    }

    protected void readMachineStateSync(final CompoundTag data) {
    }
}

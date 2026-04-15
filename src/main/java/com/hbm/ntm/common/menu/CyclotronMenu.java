package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.CyclotronBlockEntity;
import com.hbm.ntm.common.cyclotron.HbmCyclotronRecipes;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("null")
public class CyclotronMenu extends MachineMenuBase<CyclotronBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_DURATION = 1;
    private static final int DATA_ENERGY = 2;
    private static final int DATA_MAX_ENERGY = 3;
    private static final int DATA_CONSUMPTION = 4;
    private static final int DATA_COOLANT_USE = 5;
    private static final int DATA_COUNT = 6;

    private final ContainerData data;
    private int clientProgress;
    private int clientDuration = 1;
    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientConsumption;
    private int clientCoolantUse;
    private boolean clientHasRecipe;
    private boolean clientCanProcess;
    private final int[] fluidAmounts = new int[3];
    private final int[] fluidCapacities = new int[3];
    private final String[] fluidNames = new String[]{"", "", ""};

    public CyclotronMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final CyclotronBlockEntity cyclotron ? cyclotron : null);
    }

    public CyclotronMenu(final int containerId, final Inventory inventory, final CyclotronBlockEntity cyclotron) {
        super(HbmMenuTypes.MACHINE_CYCLOTRON.get(), containerId, inventory, cyclotron, CyclotronBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = cyclotron == null ? new ItemStackHandler(CyclotronBlockEntity.SLOT_COUNT) : cyclotron.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, CyclotronBlockEntity.SLOT_PART_1, 11, 18,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, CyclotronBlockEntity.SLOT_PART_2, 11, 36,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, CyclotronBlockEntity.SLOT_PART_3, 11, 54,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));

        this.addSlot(new FilteredSlotItemHandler(handler, CyclotronBlockEntity.SLOT_TARGET_1, 101, 18,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, CyclotronBlockEntity.SLOT_TARGET_2, 101, 36,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, CyclotronBlockEntity.SLOT_TARGET_3, 101, 54,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));

        this.addSlot(new OutputSlotItemHandler(handler, CyclotronBlockEntity.SLOT_OUTPUT_1, 131, 18));
        this.addSlot(new OutputSlotItemHandler(handler, CyclotronBlockEntity.SLOT_OUTPUT_2, 131, 36));
        this.addSlot(new OutputSlotItemHandler(handler, CyclotronBlockEntity.SLOT_OUTPUT_3, 131, 54));

        this.addSlot(new FilteredSlotItemHandler(handler, CyclotronBlockEntity.SLOT_BATTERY, 168, 83,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addUpgradeSlot(handler, CyclotronBlockEntity.SLOT_UPGRADE_1, 60, 81,
            MachineUpgradeItem.UpgradeType.SPEED,
            MachineUpgradeItem.UpgradeType.POWER,
            MachineUpgradeItem.UpgradeType.EFFECT);
        this.addUpgradeSlot(handler, CyclotronBlockEntity.SLOT_UPGRADE_2, 78, 81,
            MachineUpgradeItem.UpgradeType.SPEED,
            MachineUpgradeItem.UpgradeType.POWER,
            MachineUpgradeItem.UpgradeType.EFFECT);

        this.addPlayerInventory(inventory, 15, 133);

        this.data = cyclotron == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    cyclotron::getProgress,
                    cyclotron::getProcessDuration,
                    cyclotron::getStoredEnergy,
                    cyclotron::getMaxStoredEnergy,
                    cyclotron::getLastConsumption,
                    cyclotron::getLastCoolantUse
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientDuration = Math.max(1, value),
                    value -> this.clientEnergy = Math.max(0, value),
                    value -> this.clientMaxEnergy = Math.max(1, value),
                    value -> this.clientConsumption = Math.max(0, value),
                    value -> this.clientCoolantUse = Math.max(0, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, CyclotronBlockEntity.SLOT_BATTERY, CyclotronBlockEntity.SLOT_BATTERY + 1, false);
        }

        if (this.isValidUpgrade(stack)) {
            return this.moveToMachineRange(stack, CyclotronBlockEntity.SLOT_UPGRADE_1, CyclotronBlockEntity.SLOT_UPGRADE_2 + 1);
        }

        if (HbmCyclotronRecipes.isCyclotronPart(stack)) {
            return this.moveItemStackTo(stack, CyclotronBlockEntity.SLOT_PART_1, CyclotronBlockEntity.SLOT_PART_3 + 1, false);
        }

        if (HbmCyclotronRecipes.hasTargetRecipe(stack)) {
            return this.moveItemStackTo(stack, CyclotronBlockEntity.SLOT_TARGET_1, CyclotronBlockEntity.SLOT_TARGET_3 + 1, false);
        }

        return false;
    }

    private boolean isValidUpgrade(final ItemStack stack) {
        return this.isUpgradeItem(stack,
            MachineUpgradeItem.UpgradeType.SPEED,
            MachineUpgradeItem.UpgradeType.POWER,
            MachineUpgradeItem.UpgradeType.EFFECT);
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int processDuration() {
        if (this.clientDuration > 0) {
            return this.clientDuration;
        }
        return Math.max(1, this.data.get(DATA_DURATION));
    }

    public int energy() {
        return this.clientEnergy > 0 ? this.clientEnergy : this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        return this.clientMaxEnergy > 0 ? this.clientMaxEnergy : this.data.get(DATA_MAX_ENERGY);
    }

    public int consumption() {
        return Math.max(0, this.clientConsumption > 0 ? this.clientConsumption : this.data.get(DATA_CONSUMPTION));
    }

    public int coolantUse() {
        return Math.max(0, this.clientCoolantUse > 0 ? this.clientCoolantUse : this.data.get(DATA_COOLANT_USE));
    }

    public boolean hasRecipe() {
        return this.clientHasRecipe;
    }

    public boolean canProcess() {
        return this.clientCanProcess;
    }

    public int fluidAmount(final int tank) {
        return tank < 0 || tank >= this.fluidAmounts.length ? 0 : this.fluidAmounts[tank];
    }

    public int fluidCapacity(final int tank) {
        return tank < 0 || tank >= this.fluidCapacities.length ? 0 : this.fluidCapacities[tank];
    }

    public String fluidName(final int tank) {
        return tank < 0 || tank >= this.fluidNames.length ? "" : this.fluidNames[tank];
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientDuration = Math.max(1, data.getInt("processDuration"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(1, data.getInt("maxEnergy"));
        this.clientConsumption = Math.max(0, data.getInt("consumption"));
        this.clientCoolantUse = Math.max(0, data.getInt("coolantUse"));
        this.clientHasRecipe = data.getBoolean("hasRecipe");
        this.clientCanProcess = data.getBoolean("canProcess");

        for (int tank = 0; tank < 3; tank++) {
            this.fluidAmounts[tank] = Math.max(0, data.getInt("fluid" + tank + "Amount"));
            this.fluidCapacities[tank] = Math.max(0, data.getInt("fluid" + tank + "Capacity"));
            this.fluidNames[tank] = data.getString("fluid" + tank + "Name");
        }
    }
}

package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.ElectricFurnaceBlockEntity;
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
public class ElectricFurnaceMenu extends MachineMenuBase<ElectricFurnaceBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_MAX_PROGRESS = 1;
    private static final int DATA_ENERGY = 2;
    private static final int DATA_MAX_ENERGY = 3;
    private static final int DATA_CONSUMPTION = 4;
    private static final int DATA_HAS_POWER = 5;
    private static final int DATA_COUNT = 6;

    private final ContainerData data;
    private int clientProgress;
    private int clientMaxProgress;
    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientConsumption;
    private boolean clientHasPower;

    public ElectricFurnaceMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final ElectricFurnaceBlockEntity furnace ? furnace : null);
    }

    public ElectricFurnaceMenu(final int containerId, final Inventory inventory, final ElectricFurnaceBlockEntity furnace) {
        super(HbmMenuTypes.MACHINE_ELECTRIC_FURNACE.get(), containerId, inventory, furnace, ElectricFurnaceBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = furnace == null ? new ItemStackHandler(ElectricFurnaceBlockEntity.SLOT_COUNT) : furnace.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, ElectricFurnaceBlockEntity.SLOT_BATTERY, 56, 53,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addSlot(new FilteredSlotItemHandler(handler, ElectricFurnaceBlockEntity.SLOT_INPUT, 56, 17,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new OutputSlotItemHandler(handler, ElectricFurnaceBlockEntity.SLOT_OUTPUT, 116, 35));
        this.addUpgradeSlot(handler, ElectricFurnaceBlockEntity.SLOT_UPGRADE, 147, 34,
            MachineUpgradeItem.UpgradeType.SPEED,
            MachineUpgradeItem.UpgradeType.POWER);

        this.addPlayerInventory(inventory, 8, 84);

        this.data = furnace == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    furnace::getProgress,
                    furnace::getMaxProgress,
                    furnace::getStoredEnergy,
                    furnace::getMaxStoredEnergy,
                    furnace::getCurrentConsumption,
                    () -> furnace.hasPower() ? 1 : 0
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientMaxProgress = Math.max(1, value),
                    value -> this.clientEnergy = Math.max(0, value),
                    value -> this.clientMaxEnergy = Math.max(1, value),
                    value -> this.clientConsumption = Math.max(0, value),
                    value -> this.clientHasPower = value > 0
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, ElectricFurnaceBlockEntity.SLOT_BATTERY, ElectricFurnaceBlockEntity.SLOT_BATTERY + 1, false);
        }

        if (this.isUpgradeItem(stack, MachineUpgradeItem.UpgradeType.SPEED, MachineUpgradeItem.UpgradeType.POWER)) {
            return this.moveItemStackTo(stack, ElectricFurnaceBlockEntity.SLOT_UPGRADE, ElectricFurnaceBlockEntity.SLOT_UPGRADE + 1, false);
        }

        if (this.machine != null && this.machine.isItemValid(ElectricFurnaceBlockEntity.SLOT_INPUT, stack)) {
            return this.moveItemStackTo(stack, ElectricFurnaceBlockEntity.SLOT_INPUT, ElectricFurnaceBlockEntity.SLOT_INPUT + 1, false);
        }

        return false;
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int maxProgress() {
        if (this.clientMaxProgress > 0) {
            return this.clientMaxProgress;
        }
        return Math.max(1, this.data.get(DATA_MAX_PROGRESS));
    }

    public int energy() {
        return this.clientEnergy > 0 ? this.clientEnergy : this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        if (this.clientMaxEnergy > 0) {
            return this.clientMaxEnergy;
        }
        return Math.max(1, this.data.get(DATA_MAX_ENERGY));
    }

    public int consumption() {
        return this.clientConsumption > 0 ? this.clientConsumption : this.data.get(DATA_CONSUMPTION);
    }

    public boolean hasPower() {
        return this.clientHasPower || this.data.get(DATA_HAS_POWER) > 0;
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientMaxProgress = Math.max(1, data.getInt("maxProgress"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(1, data.getInt("maxEnergy"));
        this.clientConsumption = Math.max(0, data.getInt("consumption"));
        this.clientHasPower = data.getBoolean("hasPower");
    }
}
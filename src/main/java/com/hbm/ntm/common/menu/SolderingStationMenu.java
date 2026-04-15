package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.SolderingStationBlockEntity;
import com.hbm.ntm.common.config.SolderingStationMachineConfig;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
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
public class SolderingStationMenu extends MachineMenuBase<SolderingStationBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_PROCESS_TIME = 1;
    private static final int DATA_ENERGY_LO = 2;
    private static final int DATA_ENERGY_HI = 3;
    private static final int DATA_MAX_POWER_LO = 4;
    private static final int DATA_MAX_POWER_HI = 5;
    private static final int DATA_CONSUMPTION_LO = 6;
    private static final int DATA_CONSUMPTION_HI = 7;
    private static final int DATA_COLLISION = 8;
    private static final int DATA_COUNT = 9;

    private final ContainerData data;
    private int clientProgress;
    private int clientProcessTime = 1;
    private int clientEnergy;
    private int clientMaxPower = SolderingStationMachineConfig.INSTANCE.baseMaxPower();
    private int clientConsumption;
    private boolean clientCollisionPrevention;
    private int clientFluidAmount;
    private int clientFluidCapacity;
    private String clientFluidName = "";

    public SolderingStationMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final SolderingStationBlockEntity station ? station : null);
    }

    public SolderingStationMenu(final int containerId, final Inventory inventory, final SolderingStationBlockEntity station) {
        super(HbmMenuTypes.MACHINE_SOLDERING_STATION.get(), containerId, inventory, station, SolderingStationBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = station == null ? new ItemStackHandler(SolderingStationBlockEntity.SLOT_COUNT) : station.getInternalItemHandler();

        this.addFilteredGridSlots(handler, SolderingStationBlockEntity.SLOT_TOPPING_START, 17, 18, 2, 3,
            (slot, stack) -> station == null || station.isItemValid(slot, stack));

        this.addSlot(new OutputSlotItemHandler(handler, SolderingStationBlockEntity.SLOT_OUTPUT, 107, 27));
        this.addSlot(new FilteredSlotItemHandler(handler, SolderingStationBlockEntity.SLOT_BATTERY, 152, 72,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addSlot(new FilteredSlotItemHandler(handler, SolderingStationBlockEntity.SLOT_FLUID_ID, 17, 63,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));
        this.addUpgradeSlot(handler, SolderingStationBlockEntity.SLOT_UPGRADE_1, 89, 63);
        this.addUpgradeSlot(handler, SolderingStationBlockEntity.SLOT_UPGRADE_2, 107, 63);

        this.addPlayerInventory(inventory, 8, 122);
        this.data = station == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    station::getProgress,
                    station::getProcessTime,
                    MachineDataSlots.lowWord(station::getStoredEnergy),
                    MachineDataSlots.highWord(station::getStoredEnergy),
                    MachineDataSlots.lowWord(() -> (int) Math.min(Integer.MAX_VALUE, station.getDisplayMaxPower())),
                    MachineDataSlots.highWord(() -> (int) Math.min(Integer.MAX_VALUE, station.getDisplayMaxPower())),
                    MachineDataSlots.lowWord(() -> (int) Math.min(Integer.MAX_VALUE, station.getConsumption())),
                    MachineDataSlots.highWord(() -> (int) Math.min(Integer.MAX_VALUE, station.getConsumption())),
                    () -> station.isCollisionPrevention() ? 1 : 0
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientProcessTime = Math.max(1, value),
                    value -> this.clientEnergy = MachineDataSlots.withLowWord(this.clientEnergy, value),
                    value -> this.clientEnergy = MachineDataSlots.withHighWord(this.clientEnergy, value),
                    value -> this.clientMaxPower = MachineDataSlots.withLowWord(this.clientMaxPower, value),
                    value -> this.clientMaxPower = MachineDataSlots.withHighWord(this.clientMaxPower, value),
                    value -> this.clientConsumption = MachineDataSlots.withLowWord(this.clientConsumption, value),
                    value -> this.clientConsumption = MachineDataSlots.withHighWord(this.clientConsumption, value),
                    value -> this.clientCollisionPrevention = value != 0
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, SolderingStationBlockEntity.SLOT_BATTERY, SolderingStationBlockEntity.SLOT_BATTERY + 1, false);
        }
        if (stack.getItem() instanceof IItemFluidIdentifier) {
            return this.moveItemStackTo(stack, SolderingStationBlockEntity.SLOT_FLUID_ID, SolderingStationBlockEntity.SLOT_FLUID_ID + 1, false);
        }
        if (this.isUpgradeItem(stack)) {
            return this.moveToMachineRange(stack, SolderingStationBlockEntity.SLOT_UPGRADE_1, SolderingStationBlockEntity.SLOT_UPGRADE_2 + 1);
        }

        if (this.machine != null) {
            for (int slot = SolderingStationBlockEntity.SLOT_TOPPING_START; slot < SolderingStationBlockEntity.SLOT_TOPPING_END; slot++) {
                if (this.machine.isItemValid(slot, stack) && this.moveItemStackTo(stack, slot, slot + 1, false)) {
                    return true;
                }
            }
            for (int slot = SolderingStationBlockEntity.SLOT_PCB_START; slot < SolderingStationBlockEntity.SLOT_PCB_END; slot++) {
                if (this.machine.isItemValid(slot, stack) && this.moveItemStackTo(stack, slot, slot + 1, false)) {
                    return true;
                }
            }
            if (this.machine.isItemValid(SolderingStationBlockEntity.SLOT_SOLDER, stack)) {
                return this.moveItemStackTo(stack, SolderingStationBlockEntity.SLOT_SOLDER, SolderingStationBlockEntity.SLOT_SOLDER + 1, false);
            }
        }

        return this.moveItemStackTo(stack, SolderingStationBlockEntity.SLOT_TOPPING_START, SolderingStationBlockEntity.SLOT_SOLDER + 1, false);
    }

    public int progress() {
        return this.clientProgress;
    }

    public int processTime() {
        return Math.max(1, this.clientProcessTime);
    }

    public int energy() {
        return this.clientEnergy;
    }

    public int maxPower() {
        return Math.max(SolderingStationMachineConfig.INSTANCE.baseMaxPower(), this.clientMaxPower);
    }

    public int consumption() {
        return Math.max(0, this.clientConsumption);
    }

    public boolean collisionPrevention() {
        return this.clientCollisionPrevention;
    }

    public int fluidAmount() {
        return this.clientFluidAmount;
    }

    public int fluidCapacity() {
        return this.clientFluidCapacity;
    }

    public String fluidName() {
        return this.clientFluidName;
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientProcessTime = Math.max(1, data.getInt("processTime"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxPower = Math.max(1, (int) Math.min(Integer.MAX_VALUE, data.getLong("maxPower")));
        this.clientConsumption = Math.max(0, (int) Math.min(Integer.MAX_VALUE, data.getLong("consumption")));
        this.clientCollisionPrevention = data.getBoolean("collisionPrevention");
        this.clientFluidAmount = Math.max(0, data.getInt("fluidAmount"));
        this.clientFluidCapacity = Math.max(0, data.getInt("fluidCapacity"));
        this.clientFluidName = data.getString("fluidName");
    }
}


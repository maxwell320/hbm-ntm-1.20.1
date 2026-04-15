package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.CombustionEngineBlockEntity;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.item.PistonSetItem;
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
public class CombustionEngineMenu extends MachineMenuBase<CombustionEngineBlockEntity> {
    private static final int DATA_ENERGY = 0;
    private static final int DATA_MAX_ENERGY = 1;
    private static final int DATA_FUEL = 2;
    private static final int DATA_FUEL_CAPACITY = 3;
    private static final int DATA_SETTING = 4;
    private static final int DATA_ON = 5;
    private static final int DATA_GENERATION = 6;
    private static final int DATA_COUNT = 8;

    private final ContainerData data;

    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientFuel;
    private int clientFuelCapacity;
    private int clientSetting;
    private boolean clientEngineOn;
    private int clientGeneration;
    private int clientPistonTier = -1;
    private String clientFuelName = "Empty";

    public CombustionEngineMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final CombustionEngineBlockEntity machine ? machine : null);
    }

    public CombustionEngineMenu(final int containerId, final Inventory inventory, final CombustionEngineBlockEntity machine) {
        super(HbmMenuTypes.MACHINE_COMBUSTION_ENGINE.get(), containerId, inventory, machine, CombustionEngineBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = machine == null ? new ItemStackHandler(CombustionEngineBlockEntity.SLOT_COUNT) : machine.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, CombustionEngineBlockEntity.SLOT_FUEL_IN, 17, 17,
            (slot, stack) -> machine == null || machine.isItemValid(slot, stack)));
        this.addSlot(new OutputSlotItemHandler(handler, CombustionEngineBlockEntity.SLOT_FUEL_OUT, 17, 53));
        this.addSlot(new FilteredSlotItemHandler(handler, CombustionEngineBlockEntity.SLOT_PISTON, 88, 71,
            (slot, stack) -> stack.getItem() instanceof PistonSetItem));
        this.addSlot(new FilteredSlotItemHandler(handler, CombustionEngineBlockEntity.SLOT_BATTERY, 143, 71,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addSlot(new FilteredSlotItemHandler(handler, CombustionEngineBlockEntity.SLOT_ID, 35, 71,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));
        this.addPlayerInventory(inventory, 8, 121);

        this.data = machine == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    machine::getStoredEnergy,
                    machine::getMaxStoredEnergy,
                    machine::getFuelAmount,
                    machine::getFuelCapacity,
                    machine::getSetting,
                    () -> machine.isEngineOn() ? 1 : 0,
                    machine::getLastGeneration,
                    machine::getPistonTierIndex),
                List.of(
                    value -> this.clientEnergy = Math.max(0, value),
                    value -> this.clientMaxEnergy = Math.max(1, value),
                    value -> this.clientFuel = Math.max(0, value),
                    value -> this.clientFuelCapacity = Math.max(1, value),
                    value -> this.clientSetting = Math.max(0, value),
                    value -> this.clientEngineOn = value > 0,
                    value -> this.clientGeneration = Math.max(0, value),
                    value -> this.clientPistonTier = value));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof PistonSetItem) {
            return this.moveToMachineRange(stack, CombustionEngineBlockEntity.SLOT_PISTON, CombustionEngineBlockEntity.SLOT_PISTON + 1);
        }
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveToMachineRange(stack, CombustionEngineBlockEntity.SLOT_BATTERY, CombustionEngineBlockEntity.SLOT_BATTERY + 1);
        }
        if (stack.getItem() instanceof IItemFluidIdentifier) {
            return this.moveToMachineRange(stack, CombustionEngineBlockEntity.SLOT_ID, CombustionEngineBlockEntity.SLOT_ID + 1);
        }
        return this.moveToMachineRange(stack, CombustionEngineBlockEntity.SLOT_FUEL_IN, CombustionEngineBlockEntity.SLOT_FUEL_IN + 1);
    }

    public int energy() {
        return this.clientEnergy > 0 ? this.clientEnergy : this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        return Math.max(1, this.clientMaxEnergy > 0 ? this.clientMaxEnergy : this.data.get(DATA_MAX_ENERGY));
    }

    public int fuel() {
        return this.clientFuel > 0 ? this.clientFuel : this.data.get(DATA_FUEL);
    }

    public int fuelCapacity() {
        return Math.max(1, this.clientFuelCapacity > 0 ? this.clientFuelCapacity : this.data.get(DATA_FUEL_CAPACITY));
    }

    public int setting() {
        return this.clientSetting > 0 ? this.clientSetting : this.data.get(DATA_SETTING);
    }

    public boolean engineOn() {
        return this.clientEngineOn || this.data.get(DATA_ON) > 0;
    }

    public int generation() {
        return this.clientGeneration > 0 ? this.clientGeneration : this.data.get(DATA_GENERATION);
    }

    public int pistonTier() {
        return this.clientPistonTier;
    }

    public String fuelName() {
        return this.clientFuelName;
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(1, data.getInt("maxEnergy"));
        this.clientFuel = Math.max(0, data.getInt("fuel"));
        this.clientFuelCapacity = Math.max(1, data.getInt("fuelCapacity"));
        this.clientSetting = Math.max(0, data.getInt("setting"));
        this.clientEngineOn = data.getBoolean("engineOn");
        this.clientGeneration = Math.max(0, data.getInt("generation"));
        this.clientPistonTier = data.getInt("pistonTier");
        this.clientFuelName = data.getString("fuelName");
    }
}

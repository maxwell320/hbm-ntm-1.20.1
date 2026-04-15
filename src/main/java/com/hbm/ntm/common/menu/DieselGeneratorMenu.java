package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.DieselGeneratorBlockEntity;
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
public class DieselGeneratorMenu extends MachineMenuBase<DieselGeneratorBlockEntity> {
    private static final int DATA_ENERGY = 0;
    private static final int DATA_MAX_ENERGY = 1;
    private static final int DATA_FUEL = 2;
    private static final int DATA_FUEL_CAPACITY = 3;
    private static final int DATA_HE_PER_MB = 4;
    private static final int DATA_ACCEPTABLE = 5;
    private static final int DATA_COUNT = 6;

    private final ContainerData data;

    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientFuel;
    private int clientFuelCapacity;
    private int clientHePerMb;
    private boolean clientAcceptable;
    private String clientFuelName = "Empty";

    public DieselGeneratorMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final DieselGeneratorBlockEntity machine ? machine : null);
    }

    public DieselGeneratorMenu(final int containerId, final Inventory inventory, final DieselGeneratorBlockEntity machine) {
        super(HbmMenuTypes.MACHINE_DIESEL_GENERATOR.get(), containerId, inventory, machine, DieselGeneratorBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = machine == null ? new ItemStackHandler(DieselGeneratorBlockEntity.SLOT_COUNT) : machine.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, DieselGeneratorBlockEntity.SLOT_FUEL_IN, 44, 17,
            (slot, stack) -> machine == null || machine.isItemValid(slot, stack)));
        this.addSlot(new OutputSlotItemHandler(handler, DieselGeneratorBlockEntity.SLOT_FUEL_OUT, 44, 53));
        this.addSlot(new FilteredSlotItemHandler(handler, DieselGeneratorBlockEntity.SLOT_BATTERY, 116, 53,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addSlot(new FilteredSlotItemHandler(handler, DieselGeneratorBlockEntity.SLOT_ID_IN, 8, 17,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));
        this.addSlot(new OutputSlotItemHandler(handler, DieselGeneratorBlockEntity.SLOT_ID_OUT, 8, 53));
        this.addPlayerInventory(inventory, 8, 84);

        this.data = machine == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    machine::getStoredEnergy,
                    machine::getMaxStoredEnergy,
                    machine::getFuelAmount,
                    machine::getFuelCapacity,
                    machine::getCurrentFuelEnergyPerMillibucket,
                    () -> machine.hasAcceptableFuel() ? 1 : 0),
                List.of(
                    value -> this.clientEnergy = Math.max(0, value),
                    value -> this.clientMaxEnergy = Math.max(1, value),
                    value -> this.clientFuel = Math.max(0, value),
                    value -> this.clientFuelCapacity = Math.max(1, value),
                    value -> this.clientHePerMb = Math.max(0, value),
                    value -> this.clientAcceptable = value > 0));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveToMachineRange(stack, DieselGeneratorBlockEntity.SLOT_BATTERY, DieselGeneratorBlockEntity.SLOT_BATTERY + 1);
        }
        if (stack.getItem() instanceof IItemFluidIdentifier) {
            return this.moveToMachineRange(stack, DieselGeneratorBlockEntity.SLOT_ID_IN, DieselGeneratorBlockEntity.SLOT_ID_IN + 1);
        }
        return this.moveToMachineRange(stack, DieselGeneratorBlockEntity.SLOT_FUEL_IN, DieselGeneratorBlockEntity.SLOT_FUEL_IN + 1);
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

    public int hePerMb() {
        return this.clientHePerMb > 0 ? this.clientHePerMb : this.data.get(DATA_HE_PER_MB);
    }

    public boolean acceptableFuel() {
        return this.clientAcceptable || this.data.get(DATA_ACCEPTABLE) > 0;
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
        this.clientHePerMb = Math.max(0, data.getInt("hePerMb"));
        this.clientAcceptable = data.getBoolean("acceptableFuel");
        this.clientFuelName = data.getString("fuelName");
    }
}

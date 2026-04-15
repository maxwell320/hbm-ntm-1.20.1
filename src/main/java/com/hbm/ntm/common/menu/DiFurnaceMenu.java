package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.DiFurnaceBlockEntity;
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
public class DiFurnaceMenu extends MachineMenuBase<DiFurnaceBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_FUEL = 1;
    private static final int DATA_MAX_FUEL = 2;
    private static final int DATA_PROCESSING_SPEED = 3;
    private static final int DATA_COUNT = 4;

    private final ContainerData data;
    private int clientProgress;
    private int clientFuel;
    private int clientMaxFuel;
    private int clientProcessingSpeed;

    public DiFurnaceMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final DiFurnaceBlockEntity furnace ? furnace : null);
    }

    public DiFurnaceMenu(final int containerId, final Inventory inventory, final DiFurnaceBlockEntity furnace) {
        super(HbmMenuTypes.MACHINE_DI_FURNACE.get(), containerId, inventory, furnace, DiFurnaceBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = furnace == null ? new ItemStackHandler(DiFurnaceBlockEntity.SLOT_COUNT) : furnace.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, DiFurnaceBlockEntity.SLOT_INPUT_LEFT, 80, 18,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, DiFurnaceBlockEntity.SLOT_INPUT_RIGHT, 80, 54,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, DiFurnaceBlockEntity.SLOT_FUEL, 8, 36,
            (slot, stack) -> DiFurnaceBlockEntity.getFuelPower(stack) > 0));
        this.addSlot(new OutputSlotItemHandler(handler, DiFurnaceBlockEntity.SLOT_OUTPUT, 134, 36));

        this.addPlayerInventory(inventory, 8, 84);

        this.data = furnace == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    furnace::getProgress,
                    furnace::getFuel,
                    furnace::getMaxFuel,
                    furnace::getProcessingSpeed
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientFuel = value,
                    value -> this.clientMaxFuel = Math.max(1, value),
                    value -> this.clientProcessingSpeed = Math.max(1, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (DiFurnaceBlockEntity.getFuelPower(stack) > 0) {
            return this.moveItemStackTo(stack, DiFurnaceBlockEntity.SLOT_FUEL, DiFurnaceBlockEntity.SLOT_FUEL + 1, false);
        }

        if (this.machine != null && this.machine.isItemValid(DiFurnaceBlockEntity.SLOT_INPUT_LEFT, stack)) {
            return this.moveItemStackTo(stack, DiFurnaceBlockEntity.SLOT_INPUT_LEFT, DiFurnaceBlockEntity.SLOT_INPUT_RIGHT + 1, false);
        }

        return false;
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int fuel() {
        return this.clientFuel > 0 ? this.clientFuel : this.data.get(DATA_FUEL);
    }

    public int maxFuel() {
        if (this.clientMaxFuel > 0) {
            return this.clientMaxFuel;
        }
        return Math.max(1, this.data.get(DATA_MAX_FUEL));
    }

    public int processingSpeed() {
        if (this.clientProcessingSpeed > 0) {
            return this.clientProcessingSpeed;
        }
        return Math.max(1, this.data.get(DATA_PROCESSING_SPEED));
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientFuel = Math.max(0, data.getInt("fuel"));
        this.clientMaxFuel = Math.max(1, data.getInt("maxFuel"));
        this.clientProcessingSpeed = Math.max(1, data.getInt("processingSpeed"));
    }
}

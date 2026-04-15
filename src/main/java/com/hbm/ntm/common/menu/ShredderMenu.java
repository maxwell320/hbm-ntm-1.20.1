package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.ShredderBlockEntity;
import com.hbm.ntm.common.config.ShredderMachineConfig;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.ShredderBladesItem;
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
public class ShredderMenu extends MachineMenuBase<ShredderBlockEntity> {

    private static final int DATA_PROGRESS = 0;
    private static final int DATA_ENERGY = 1;
    private static final int DATA_MAX_ENERGY = 2;
    private static final int DATA_GEAR_LEFT = 3;
    private static final int DATA_GEAR_RIGHT = 4;
    private static final int DATA_COUNT = 5;

    private final ContainerData data;
    private int clientProgress;
    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientGearLeft;
    private int clientGearRight;
    private int clientProcessingSpeed;

    public ShredderMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final ShredderBlockEntity shredder ? shredder : null);
    }

    public ShredderMenu(final int containerId, final Inventory inventory, final ShredderBlockEntity shredder) {
        super(HbmMenuTypes.MACHINE_SHREDDER.get(), containerId, inventory, shredder, ShredderBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = shredder == null ? new ItemStackHandler(ShredderBlockEntity.SLOT_COUNT) : shredder.getInternalItemHandler();

        this.addFilteredGridSlots(handler, ShredderBlockEntity.SLOT_INPUT_START, 44, 18, 3, 3,
            (index, stack) -> this.machine != null && this.machine.isItemValid(index, stack));
        this.addOutputGridSlots(handler, ShredderBlockEntity.SLOT_OUTPUT_START, 116, 18, 6, 3);

        this.addSlot(new FilteredSlotItemHandler(handler, ShredderBlockEntity.SLOT_BLADE_LEFT, 44, 108,
            (slot, stack) -> stack.getItem() instanceof ShredderBladesItem));
        this.addSlot(new FilteredSlotItemHandler(handler, ShredderBlockEntity.SLOT_BLADE_RIGHT, 80, 108,
            (slot, stack) -> stack.getItem() instanceof ShredderBladesItem));
        this.addSlot(new FilteredSlotItemHandler(handler, ShredderBlockEntity.SLOT_BATTERY, 8, 108,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));

        this.addPlayerInventory(inventory, 8, 151);

        this.data = shredder == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    shredder::getProgress,
                    shredder::getStoredEnergy,
                    shredder::getMaxStoredEnergy,
                    shredder::getGearLeft,
                    shredder::getGearRight
                ),
                List.of(
                    shredder::setClientProgress,
                    v -> {}, // energy is display-only on client
                    v -> {}, // max energy is display-only on client
                    v -> {}, // gear left is display-only on client
                    v -> {}  // gear right is display-only on client
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, ShredderBlockEntity.SLOT_BATTERY, ShredderBlockEntity.SLOT_BATTERY + 1, false);
        }
        if (stack.getItem() instanceof ShredderBladesItem) {
            return this.moveItemStackTo(stack, ShredderBlockEntity.SLOT_BLADE_LEFT, ShredderBlockEntity.SLOT_BLADE_RIGHT + 1, false);
        }
        if (this.machine == null || !this.machine.isItemValid(ShredderBlockEntity.SLOT_INPUT_START, stack)) {
            return false;
        }
        return this.moveItemStackTo(stack, ShredderBlockEntity.SLOT_INPUT_START, ShredderBlockEntity.SLOT_INPUT_END, false);
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int energy() {
        return this.clientEnergy > 0 ? this.clientEnergy : this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        return this.clientMaxEnergy > 0 ? this.clientMaxEnergy : this.data.get(DATA_MAX_ENERGY);
    }

    public int gearLeft() {
        return this.clientGearLeft > 0 ? this.clientGearLeft : this.data.get(DATA_GEAR_LEFT);
    }

    public int gearRight() {
        return this.clientGearRight > 0 ? this.clientGearRight : this.data.get(DATA_GEAR_RIGHT);
    }

    public int processingSpeed() {
        if (this.clientProcessingSpeed > 0) {
            return this.clientProcessingSpeed;
        }
        return this.machine == null ? ShredderMachineConfig.INSTANCE.processingSpeed() : this.machine.configuredProcessingSpeed();
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(0, data.getInt("maxEnergy"));
        this.clientGearLeft = Math.max(0, data.getInt("gearLeft"));
        this.clientGearRight = Math.max(0, data.getInt("gearRight"));
        this.clientProcessingSpeed = Math.max(1, data.getInt("processingSpeed"));
    }
}

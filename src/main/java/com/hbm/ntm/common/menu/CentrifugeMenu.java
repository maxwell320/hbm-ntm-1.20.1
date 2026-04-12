package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.CentrifugeBlockEntity;
import com.hbm.ntm.common.config.CentrifugeMachineConfig;
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
public class CentrifugeMenu extends MachineMenuBase<CentrifugeBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_ENERGY = 1;
    private static final int DATA_MAX_ENERGY = 2;
    private static final int DATA_PROCESSING_SPEED = 3;
    private static final int DATA_CONSUMPTION = 4;
    private static final int DATA_COUNT = 5;

    private final ContainerData data;
    private int clientProgress;
    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientProcessingSpeed;
    private int clientConsumption;

    public CentrifugeMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final CentrifugeBlockEntity centrifuge ? centrifuge : null);
    }

    public CentrifugeMenu(final int containerId, final Inventory inventory, final CentrifugeBlockEntity centrifuge) {
        super(HbmMenuTypes.MACHINE_CENTRIFUGE.get(), containerId, inventory, centrifuge, CentrifugeBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = centrifuge == null ? new ItemStackHandler(CentrifugeBlockEntity.SLOT_COUNT) : centrifuge.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_INPUT, 36, 50,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_BATTERY, 9, 50,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));

        this.addSlot(new OutputSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_OUTPUT_1, 63, 50));
        this.addSlot(new OutputSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_OUTPUT_2, 83, 50));
        this.addSlot(new OutputSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_OUTPUT_3, 103, 50));
        this.addSlot(new OutputSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_OUTPUT_4, 123, 50));

        this.addSlot(new FilteredSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_UPGRADE_1, 149, 22,
            (slot, stack) -> stack.getItem() instanceof MachineUpgradeItem));
        this.addSlot(new FilteredSlotItemHandler(handler, CentrifugeBlockEntity.SLOT_UPGRADE_2, 149, 40,
            (slot, stack) -> stack.getItem() instanceof MachineUpgradeItem));

        this.addPlayerInventory(inventory, 8, 104);

        this.data = centrifuge == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    centrifuge::getProgress,
                    centrifuge::getStoredEnergy,
                    centrifuge::getMaxStoredEnergy,
                    () -> Math.max(1, CentrifugeMachineConfig.INSTANCE.processingSpeed()),
                    centrifuge::getLastConsumption
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientEnergy = value,
                    value -> this.clientMaxEnergy = value,
                    value -> this.clientProcessingSpeed = Math.max(1, value),
                    value -> this.clientConsumption = Math.max(0, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, CentrifugeBlockEntity.SLOT_BATTERY, CentrifugeBlockEntity.SLOT_BATTERY + 1, false);
        }

        if (stack.getItem() instanceof MachineUpgradeItem) {
            return this.moveItemStackTo(stack, CentrifugeBlockEntity.SLOT_UPGRADE_1, CentrifugeBlockEntity.SLOT_UPGRADE_2 + 1, false);
        }

        if (this.machine != null && this.machine.isItemValid(CentrifugeBlockEntity.SLOT_INPUT, stack)) {
            return this.moveItemStackTo(stack, CentrifugeBlockEntity.SLOT_INPUT, CentrifugeBlockEntity.SLOT_INPUT + 1, false);
        }

        return false;
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

    public int processingSpeed() {
        if (this.clientProcessingSpeed > 0) {
            return this.clientProcessingSpeed;
        }
        return Math.max(1, this.data.get(DATA_PROCESSING_SPEED));
    }

    public int consumption() {
        return Math.max(0, this.clientConsumption > 0 ? this.clientConsumption : this.data.get(DATA_CONSUMPTION));
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(1, data.getInt("maxEnergy"));
        this.clientProcessingSpeed = Math.max(1, data.getInt("processingSpeed"));
        this.clientConsumption = Math.max(0, data.getInt("consumption"));
    }
}

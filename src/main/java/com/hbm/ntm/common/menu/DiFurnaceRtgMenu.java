package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.DiFurnaceRtgBlockEntity;
import com.hbm.ntm.common.item.RtgPelletItem;
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
public class DiFurnaceRtgMenu extends MachineMenuBase<DiFurnaceRtgBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_POWER = 1;
    private static final int DATA_POWER_THRESHOLD = 2;
    private static final int DATA_PROCESSING_TIME = 3;
    private static final int DATA_COUNT = 4;

    private final ContainerData data;
    private int clientProgress;
    private int clientPower;
    private int clientPowerThreshold;
    private int clientProcessingTime;

    public DiFurnaceRtgMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final DiFurnaceRtgBlockEntity furnace ? furnace : null);
    }

    public DiFurnaceRtgMenu(final int containerId, final Inventory inventory, final DiFurnaceRtgBlockEntity furnace) {
        super(HbmMenuTypes.MACHINE_DI_FURNACE_RTG.get(), containerId, inventory, furnace, DiFurnaceRtgBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = furnace == null ? new ItemStackHandler(DiFurnaceRtgBlockEntity.SLOT_COUNT) : furnace.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, DiFurnaceRtgBlockEntity.SLOT_INPUT_LEFT, 80, 18,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, DiFurnaceRtgBlockEntity.SLOT_INPUT_RIGHT, 80, 54,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new OutputSlotItemHandler(handler, DiFurnaceRtgBlockEntity.SLOT_OUTPUT, 134, 36));

        this.addFilteredGridSlots(handler, DiFurnaceRtgBlockEntity.SLOT_RTG_START, 26, 18, 2, 3, 18,
            (slot, stack) -> stack.getItem() instanceof RtgPelletItem);

        this.addPlayerInventory(inventory, 8, 84);

        this.data = furnace == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    furnace::getProgress,
                    furnace::getPower,
                    furnace::getPowerThreshold,
                    furnace::getProcessingTime
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientPower = value,
                    value -> this.clientPowerThreshold = Math.max(1, value),
                    value -> this.clientProcessingTime = Math.max(1, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof RtgPelletItem) {
            return this.moveItemStackTo(stack, DiFurnaceRtgBlockEntity.SLOT_RTG_START, DiFurnaceRtgBlockEntity.SLOT_RTG_END + 1, false);
        }

        if (this.machine != null && this.machine.isItemValid(DiFurnaceRtgBlockEntity.SLOT_INPUT_LEFT, stack)) {
            return this.moveItemStackTo(stack, DiFurnaceRtgBlockEntity.SLOT_INPUT_LEFT, DiFurnaceRtgBlockEntity.SLOT_INPUT_RIGHT + 1, false);
        }

        return false;
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int power() {
        return this.clientPower > 0 ? this.clientPower : this.data.get(DATA_POWER);
    }

    public int powerThreshold() {
        if (this.clientPowerThreshold > 0) {
            return this.clientPowerThreshold;
        }
        return Math.max(1, this.data.get(DATA_POWER_THRESHOLD));
    }

    public int processingTime() {
        if (this.clientProcessingTime > 0) {
            return this.clientProcessingTime;
        }
        return Math.max(1, this.data.get(DATA_PROCESSING_TIME));
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientPower = Math.max(0, data.getInt("power"));
        this.clientPowerThreshold = Math.max(1, data.getInt("powerThreshold"));
        this.clientProcessingTime = Math.max(1, data.getInt("processingTime"));
    }
}

package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.RtgFurnaceBlockEntity;
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
public class RtgFurnaceMenu extends MachineMenuBase<RtgFurnaceBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_HEAT = 1;
    private static final int DATA_PROCESSING_SPEED = 2;
    private static final int DATA_COUNT = 3;

    private final ContainerData data;
    private int clientProgress;
    private int clientHeat;
    private int clientProcessingSpeed;

    public RtgFurnaceMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final RtgFurnaceBlockEntity furnace ? furnace : null);
    }

    public RtgFurnaceMenu(final int containerId, final Inventory inventory, final RtgFurnaceBlockEntity furnace) {
        super(HbmMenuTypes.MACHINE_RTG_FURNACE.get(), containerId, inventory, furnace, RtgFurnaceBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = furnace == null ? new ItemStackHandler(RtgFurnaceBlockEntity.SLOT_COUNT) : furnace.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, RtgFurnaceBlockEntity.SLOT_INPUT, 56, 17,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, RtgFurnaceBlockEntity.SLOT_RTG_1, 38, 53,
            (slot, stack) -> stack.getItem() instanceof RtgPelletItem));
        this.addSlot(new FilteredSlotItemHandler(handler, RtgFurnaceBlockEntity.SLOT_RTG_2, 56, 53,
            (slot, stack) -> stack.getItem() instanceof RtgPelletItem));
        this.addSlot(new FilteredSlotItemHandler(handler, RtgFurnaceBlockEntity.SLOT_RTG_3, 74, 53,
            (slot, stack) -> stack.getItem() instanceof RtgPelletItem));
        this.addSlot(new OutputSlotItemHandler(handler, RtgFurnaceBlockEntity.SLOT_OUTPUT, 116, 35));

        this.addPlayerInventory(inventory, 8, 84);

        this.data = furnace == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    furnace::getProgress,
                    furnace::getHeat,
                    furnace::getProcessingSpeed
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientHeat = Math.max(0, value),
                    value -> this.clientProcessingSpeed = Math.max(1, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof RtgPelletItem) {
            return this.moveItemStackTo(stack, RtgFurnaceBlockEntity.SLOT_RTG_1, RtgFurnaceBlockEntity.SLOT_RTG_3 + 1, false);
        }

        if (this.machine != null && this.machine.isItemValid(RtgFurnaceBlockEntity.SLOT_INPUT, stack)) {
            return this.moveItemStackTo(stack, RtgFurnaceBlockEntity.SLOT_INPUT, RtgFurnaceBlockEntity.SLOT_INPUT + 1, false);
        }

        return false;
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int heat() {
        return this.clientHeat > 0 ? this.clientHeat : this.data.get(DATA_HEAT);
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
        this.clientHeat = Math.max(0, data.getInt("heat"));
        this.clientProcessingSpeed = Math.max(1, data.getInt("processingSpeed"));
    }
}
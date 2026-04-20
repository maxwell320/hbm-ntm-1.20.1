package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.FurnaceIronBlockEntity;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.module.ModuleBurnTime;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("null")
public class FurnaceIronMenu extends MachineMenuBase<FurnaceIronBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_BURN_TIME = 1;
    private static final int DATA_MAX_BURN_TIME = 2;
    private static final int DATA_PROCESSING_TIME = 3;
    private static final int DATA_COUNT = 4;
    private static final List<Component> LEGACY_BURN_BONUSES = List.copyOf(new ModuleBurnTime()
        .setLigniteTimeMod(1.25)
        .setCoalTimeMod(1.25)
        .setCokeTimeMod(1.5)
        .setSolidTimeMod(2.0)
        .setRocketTimeMod(2.0)
        .setBalefireTimeMod(2.0)
        .getTimeDesc());

    private final ContainerData data;
    private final List<Component> burnBonuses;
    private int clientProgress;
    private int clientBurnTime;
    private int clientMaxBurnTime;
    private int clientProcessingTime;
    private boolean clientCanSmelt;

    public FurnaceIronMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final FurnaceIronBlockEntity furnace ? furnace : null);
    }

    public FurnaceIronMenu(final int containerId, final Inventory inventory, final FurnaceIronBlockEntity furnace) {
        super(HbmMenuTypes.FURNACE_IRON.get(), containerId, inventory, furnace, FurnaceIronBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = furnace == null ? new ItemStackHandler(FurnaceIronBlockEntity.SLOT_COUNT) : furnace.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, FurnaceIronBlockEntity.SLOT_INPUT, 53, 17,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, FurnaceIronBlockEntity.SLOT_FUEL_LEFT, 53, 53,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, FurnaceIronBlockEntity.SLOT_FUEL_RIGHT, 71, 53,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new OutputSlotItemHandler(handler, FurnaceIronBlockEntity.SLOT_OUTPUT, 125, 35));
        this.addUpgradeSlot(handler, FurnaceIronBlockEntity.SLOT_UPGRADE, 17, 35, MachineUpgradeItem.UpgradeType.SPEED);

        this.addPlayerInventory(inventory, 8, 84);
        this.burnBonuses = LEGACY_BURN_BONUSES;

        this.data = furnace == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    furnace::getProgress,
                    furnace::getBurnTime,
                    furnace::getMaxBurnTime,
                    furnace::getProcessingTime
                ),
                List.of(
                    value -> this.clientProgress = Math.max(0, value),
                    value -> this.clientBurnTime = Math.max(0, value),
                    value -> this.clientMaxBurnTime = Math.max(1, value),
                    value -> this.clientProcessingTime = Math.max(1, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (this.isUpgradeItem(stack, MachineUpgradeItem.UpgradeType.SPEED)) {
            return this.moveItemStackTo(stack, FurnaceIronBlockEntity.SLOT_UPGRADE, FurnaceIronBlockEntity.SLOT_UPGRADE + 1, false);
        }

        if (this.machine != null && this.machine.isItemValid(FurnaceIronBlockEntity.SLOT_INPUT, stack)) {
            return this.moveItemStackTo(stack, FurnaceIronBlockEntity.SLOT_INPUT, FurnaceIronBlockEntity.SLOT_INPUT + 1, false);
        }

        if (this.machine != null && this.machine.isItemValid(FurnaceIronBlockEntity.SLOT_FUEL_LEFT, stack)) {
            return this.moveItemStackTo(stack, FurnaceIronBlockEntity.SLOT_FUEL_LEFT, FurnaceIronBlockEntity.SLOT_FUEL_RIGHT + 1, false);
        }

        return false;
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int burnTime() {
        return this.clientBurnTime > 0 ? this.clientBurnTime : this.data.get(DATA_BURN_TIME);
    }

    public int maxBurnTime() {
        if (this.clientMaxBurnTime > 0) {
            return this.clientMaxBurnTime;
        }
        return Math.max(1, this.data.get(DATA_MAX_BURN_TIME));
    }

    public int processingTime() {
        if (this.clientProcessingTime > 0) {
            return this.clientProcessingTime;
        }
        return Math.max(1, this.data.get(DATA_PROCESSING_TIME));
    }

    public boolean canSmelt() {
        return this.clientCanSmelt;
    }

    public List<Component> burnBonuses() {
        return this.burnBonuses;
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientBurnTime = Math.max(0, data.getInt("burnTime"));
        this.clientMaxBurnTime = Math.max(1, data.getInt("maxBurnTime"));
        this.clientProcessingTime = Math.max(1, data.getInt("processingTime"));
        this.clientCanSmelt = data.getBoolean("canSmelt");
    }
}

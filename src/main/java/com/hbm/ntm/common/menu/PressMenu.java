package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.PressBlockEntity;
import com.hbm.ntm.common.item.StampBookItem;
import com.hbm.ntm.common.item.StampItem;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class PressMenu extends MachineMenuBase<PressBlockEntity> {
    private final ContainerData data;

    public PressMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final PressBlockEntity press ? press : null);
    }

    public PressMenu(final int containerId, final Inventory inventory, final PressBlockEntity press) {
        super(HbmMenuTypes.MACHINE_PRESS.get(), containerId, inventory, press, 13);
        final ItemStackHandler handler = press == null ? new ItemStackHandler(13) : press.getInternalItemHandler();
        this.addSlot(new FilteredSlotItemHandler(handler, PressBlockEntity.SLOT_FUEL, 26, 53,
            (slot, stack) -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0));
        this.addSlot(new FilteredSlotItemHandler(handler, PressBlockEntity.SLOT_STAMP, 80, 17,
            (slot, stack) -> stack.getItem() instanceof StampItem || stack.getItem() instanceof StampBookItem));
        this.addSlot(new FilteredSlotItemHandler(handler, PressBlockEntity.SLOT_INPUT, 80, 53,
            (slot, stack) -> press == null || press.isItemValid(slot, stack)));
        this.addSlot(new OutputSlotItemHandler(handler, PressBlockEntity.SLOT_OUTPUT, 140, 35));
        for (int i = 0; i < 9; i++) {
            this.addSlot(new net.minecraftforge.items.SlotItemHandler(handler, PressBlockEntity.SLOT_STORAGE_START + i, 8 + i * 18, 84));
        }
        this.addPlayerInventory(inventory, 8, 120);
        this.data = press == null
            ? new SimpleContainerData(3)
            : new MachineDataSlots(
                java.util.List.of(press::getSpeed, press::getBurnTime, press::getPressTicks),
                java.util.List.of(press::setClientSpeed, press::setClientBurnTime, press::setClientPressTicks));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0) {
            return this.moveItemStackTo(stack, PressBlockEntity.SLOT_FUEL, PressBlockEntity.SLOT_FUEL + 1, false)
                || this.moveItemStackTo(stack, PressBlockEntity.SLOT_STORAGE_START, PressBlockEntity.SLOT_STORAGE_START + 9, false);
        }
        if (stack.getItem() instanceof StampItem || stack.getItem() instanceof StampBookItem) {
            return this.moveItemStackTo(stack, PressBlockEntity.SLOT_STAMP, PressBlockEntity.SLOT_STAMP + 1, false)
                || this.moveItemStackTo(stack, PressBlockEntity.SLOT_STORAGE_START, PressBlockEntity.SLOT_STORAGE_START + 9, false);
        }
        return this.moveItemStackTo(stack, PressBlockEntity.SLOT_INPUT, PressBlockEntity.SLOT_INPUT + 1, false)
            || this.moveItemStackTo(stack, PressBlockEntity.SLOT_STORAGE_START, PressBlockEntity.SLOT_STORAGE_START + 9, false);
    }

    @Override
    public boolean stillValid(final @NotNull Player player) {
        return this.machine != null && this.machine.canPlayerControl(player);
    }

    public int speed() {
        return this.data.get(0);
    }

    public int burnTime() {
        return this.data.get(1);
    }

    public int pressTicks() {
        return this.data.get(2);
    }

    public int maxSpeed() {
        return PressBlockEntity.MAX_SPEED;
    }

    public int maxPressTicks() {
        return PressBlockEntity.MAX_PRESS;
    }
}

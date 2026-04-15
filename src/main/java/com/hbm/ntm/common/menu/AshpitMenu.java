package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.AshpitBlockEntity;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("null")
public class AshpitMenu extends MachineMenuBase<AshpitBlockEntity> {
    public AshpitMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final AshpitBlockEntity ashpit ? ashpit : null);
    }

    public AshpitMenu(final int containerId, final Inventory inventory, final AshpitBlockEntity ashpit) {
        super(HbmMenuTypes.MACHINE_ASHPIT.get(), containerId, inventory, ashpit, AshpitBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = ashpit == null ? new ItemStackHandler(AshpitBlockEntity.SLOT_COUNT) : ashpit.getInternalItemHandler();

        this.addOutputGridSlots(handler, 0, 44, 27, 1, 5);
        this.addPlayerInventory(inventory, 8, 86);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        return false;
    }
}

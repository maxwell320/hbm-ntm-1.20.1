package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.IcfBlockEntity;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.registration.HbmItems;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("null")
public class IcfMenu extends MachineMenuBase<IcfBlockEntity> {
    private long clientLaser;
    private long clientMaxLaser;
    private long clientHeat;
    private long clientMaxHeat = 1L;
    private long clientHeatup;
    private int clientConsumption;
    private int clientOutput;

    private final int[] fluidAmounts = new int[3];
    private final int[] fluidCapacities = new int[3];
    private final String[] fluidNames = new String[]{"", "", ""};

    public IcfMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final IcfBlockEntity icf ? icf : null);
    }

    public IcfMenu(final int containerId, final Inventory inventory, final IcfBlockEntity icf) {
        super(HbmMenuTypes.MACHINE_ICF.get(), containerId, inventory, icf, IcfBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = icf == null ? new ItemStackHandler(IcfBlockEntity.SLOT_COUNT) : icf.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, IcfBlockEntity.SLOT_INPUT_1, 80, 18,
            (slot, stack) -> stack.getItem() == HbmItems.ICF_PELLET.get()));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfBlockEntity.SLOT_INPUT_2, 98, 18,
            (slot, stack) -> stack.getItem() == HbmItems.ICF_PELLET.get()));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfBlockEntity.SLOT_INPUT_3, 116, 18,
            (slot, stack) -> stack.getItem() == HbmItems.ICF_PELLET.get()));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfBlockEntity.SLOT_INPUT_4, 134, 18,
            (slot, stack) -> stack.getItem() == HbmItems.ICF_PELLET.get()));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfBlockEntity.SLOT_INPUT_5, 152, 18,
            (slot, stack) -> stack.getItem() == HbmItems.ICF_PELLET.get()));

        this.addSlot(new OutputSlotItemHandler(handler, IcfBlockEntity.SLOT_ACTIVE_PELLET, 116, 54));

        this.addSlot(new OutputSlotItemHandler(handler, IcfBlockEntity.SLOT_OUTPUT_1, 80, 90));
        this.addSlot(new OutputSlotItemHandler(handler, IcfBlockEntity.SLOT_OUTPUT_2, 98, 90));
        this.addSlot(new OutputSlotItemHandler(handler, IcfBlockEntity.SLOT_OUTPUT_3, 116, 90));
        this.addSlot(new OutputSlotItemHandler(handler, IcfBlockEntity.SLOT_OUTPUT_4, 134, 90));
        this.addSlot(new OutputSlotItemHandler(handler, IcfBlockEntity.SLOT_OUTPUT_5, 152, 90));

        this.addSlot(new FilteredSlotItemHandler(handler, IcfBlockEntity.SLOT_FLUID_ID, 44, 90,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));

        this.addPlayerInventory(inventory, 44, 140);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() == HbmItems.ICF_PELLET.get()) {
            return this.moveItemStackTo(stack, IcfBlockEntity.SLOT_INPUT_1, IcfBlockEntity.SLOT_INPUT_5 + 1, false);
        }
        if (stack.getItem() instanceof IItemFluidIdentifier) {
            return this.moveItemStackTo(stack, IcfBlockEntity.SLOT_FLUID_ID, IcfBlockEntity.SLOT_FLUID_ID + 1, false);
        }
        return false;
    }

    public long laser() {
        return this.clientLaser;
    }

    public long maxLaser() {
        return this.clientMaxLaser;
    }

    public long heat() {
        return this.clientHeat;
    }

    public long maxHeat() {
        return this.clientMaxHeat;
    }

    public long heatup() {
        return this.clientHeatup;
    }

    public int consumption() {
        return this.clientConsumption;
    }

    public int output() {
        return this.clientOutput;
    }

    public int fluidAmount(final int tank) {
        return tank < 0 || tank >= this.fluidAmounts.length ? 0 : this.fluidAmounts[tank];
    }

    public int fluidCapacity(final int tank) {
        return tank < 0 || tank >= this.fluidCapacities.length ? 0 : this.fluidCapacities[tank];
    }

    public String fluidName(final int tank) {
        return tank < 0 || tank >= this.fluidNames.length ? "" : this.fluidNames[tank];
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientLaser = Math.max(0L, data.getLong("laser"));
        this.clientMaxLaser = Math.max(1L, data.getLong("maxLaser"));
        this.clientHeat = Math.max(0L, data.getLong("heat"));
        this.clientMaxHeat = Math.max(1L, data.getLong("maxHeat"));
        this.clientHeatup = Math.max(0L, data.getLong("heatup"));
        this.clientConsumption = Math.max(0, data.getInt("consumption"));
        this.clientOutput = Math.max(0, data.getInt("output"));

        for (int tank = 0; tank < 3; tank++) {
            this.fluidAmounts[tank] = Math.max(0, data.getInt("fluid" + tank + "Amount"));
            this.fluidCapacities[tank] = Math.max(0, data.getInt("fluid" + tank + "Capacity"));
            this.fluidNames[tank] = data.getString("fluid" + tank + "Name");
        }
    }
}
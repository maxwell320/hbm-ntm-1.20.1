package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.IcfPressBlockEntity;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.item.IcfPelletItem;
import com.hbm.ntm.common.registration.HbmItems;
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
public class IcfPressMenu extends MachineMenuBase<IcfPressBlockEntity> {
    private static final int DATA_MUON = 0;
    private static final int DATA_COUNT = 1;

    private final ContainerData data;
    private int clientMuon;
    private int clientMaxMuon = IcfPressBlockEntity.MAX_MUON;
    private final int[] fluidAmounts = new int[2];
    private final int[] fluidCapacities = new int[2];
    private final String[] fluidNames = new String[]{"", ""};

    public IcfPressMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final IcfPressBlockEntity icf ? icf : null);
    }

    public IcfPressMenu(final int containerId, final Inventory inventory, final IcfPressBlockEntity icf) {
        super(HbmMenuTypes.MACHINE_ICF_PRESS.get(), containerId, inventory, icf, IcfPressBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = icf == null ? new ItemStackHandler(IcfPressBlockEntity.SLOT_COUNT) : icf.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, IcfPressBlockEntity.SLOT_PELLET_EMPTY, 98, 18,
            (slot, stack) -> stack.getItem() == HbmItems.ICF_PELLET_EMPTY.get()));
        this.addSlot(new OutputSlotItemHandler(handler, IcfPressBlockEntity.SLOT_PELLET_FILLED, 98, 54));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfPressBlockEntity.SLOT_MUON_INPUT, 8, 18,
            (slot, stack) -> stack.getItem() == HbmItems.PARTICLE_MUON.get()));
        this.addSlot(new OutputSlotItemHandler(handler, IcfPressBlockEntity.SLOT_MUON_OUTPUT, 8, 54));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfPressBlockEntity.SLOT_SOLID_FUEL_A, 62, 54,
            (slot, stack) -> this.machine == null ? IcfPelletItem.isMaterialFuel(stack) : this.machine.isValidSolidFuelStack(stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfPressBlockEntity.SLOT_SOLID_FUEL_B, 134, 54,
            (slot, stack) -> this.machine == null ? IcfPelletItem.isMaterialFuel(stack) : this.machine.isValidSolidFuelStack(stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfPressBlockEntity.SLOT_FLUID_ID_A, 62, 18,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));
        this.addSlot(new FilteredSlotItemHandler(handler, IcfPressBlockEntity.SLOT_FLUID_ID_B, 134, 18,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));

        this.addPlayerInventory(inventory, 8, 97);

        this.data = icf == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(icf::getMuonCharge),
                List.of(value -> this.clientMuon = Math.max(0, value)));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() == HbmItems.ICF_PELLET_EMPTY.get()) {
            return this.moveItemStackTo(stack, IcfPressBlockEntity.SLOT_PELLET_EMPTY, IcfPressBlockEntity.SLOT_PELLET_EMPTY + 1, false);
        }
        if (stack.getItem() == HbmItems.PARTICLE_MUON.get()) {
            return this.moveItemStackTo(stack, IcfPressBlockEntity.SLOT_MUON_INPUT, IcfPressBlockEntity.SLOT_MUON_INPUT + 1, false);
        }
        if (stack.getItem() instanceof IItemFluidIdentifier) {
            return this.moveItemStackTo(stack, IcfPressBlockEntity.SLOT_FLUID_ID_A, IcfPressBlockEntity.SLOT_FLUID_ID_B + 1, false);
        }
        if (IcfPelletItem.isMaterialFuel(stack)) {
            return this.moveItemStackTo(stack, IcfPressBlockEntity.SLOT_SOLID_FUEL_A, IcfPressBlockEntity.SLOT_SOLID_FUEL_A + 1, false)
                || this.moveItemStackTo(stack, IcfPressBlockEntity.SLOT_SOLID_FUEL_B, IcfPressBlockEntity.SLOT_SOLID_FUEL_B + 1, false);
        }
        return false;
    }

    public int muon() {
        return this.clientMuon > 0 ? this.clientMuon : this.data.get(DATA_MUON);
    }

    public int maxMuon() {
        return this.clientMaxMuon;
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
        this.clientMuon = Math.max(0, data.getInt("muon"));
        this.clientMaxMuon = Math.max(1, data.getInt("maxMuon"));
        for (int tank = 0; tank < 2; tank++) {
            this.fluidAmounts[tank] = Math.max(0, data.getInt("fluid" + tank + "Amount"));
            this.fluidCapacities[tank] = Math.max(0, data.getInt("fluid" + tank + "Capacity"));
            this.fluidNames[tank] = data.getString("fluid" + tank + "Name");
        }
    }
}

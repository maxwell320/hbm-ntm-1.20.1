package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.RtgGeneratorBlockEntity;
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
public class RtgGeneratorMenu extends MachineMenuBase<RtgGeneratorBlockEntity> {
    private static final int DATA_HEAT = 0;
    private static final int DATA_ENERGY = 1;
    private static final int DATA_COUNT = 2;

    private final ContainerData data;
    private int clientHeat;
    private int clientEnergy;
    private int clientMaxEnergy;

    public RtgGeneratorMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final RtgGeneratorBlockEntity rtg ? rtg : null);
    }

    public RtgGeneratorMenu(final int containerId, final Inventory inventory, final RtgGeneratorBlockEntity rtg) {
        super(HbmMenuTypes.MACHINE_RTG_GREY.get(), containerId, inventory, rtg, RtgGeneratorBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = rtg == null ? new ItemStackHandler(RtgGeneratorBlockEntity.SLOT_COUNT) : rtg.getInternalItemHandler();

        this.addFilteredGridSlots(handler,
            0,
            16,
            18,
            3,
            5,
            (slot, stack) -> stack.getItem() instanceof RtgPelletItem);
        this.addPlayerInventory(inventory, 8, 106);

        this.data = rtg == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    rtg::getHeat,
                    rtg::getStoredEnergy),
                List.of(
                    value -> this.clientHeat = Math.max(0, value),
                    value -> this.clientEnergy = Math.max(0, value)));
        this.addMachineDataSlots(this.data);
        this.clientMaxEnergy = rtg == null ? 100_000 : Math.max(1, rtg.getMaxStoredEnergy());
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof RtgPelletItem) {
            return this.moveToMachineRange(stack, 0, RtgGeneratorBlockEntity.SLOT_COUNT);
        }
        return false;
    }

    public int heat() {
        return this.clientHeat > 0 ? this.clientHeat : this.data.get(DATA_HEAT);
    }

    public int maxHeat() {
        return RtgGeneratorBlockEntity.HEAT_CAPACITY;
    }

    public int energy() {
        return this.clientEnergy > 0 ? this.clientEnergy : this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        return Math.max(1, this.clientMaxEnergy);
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientHeat = Math.max(0, data.getInt("heat"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(1, data.getInt("maxEnergy"));
    }
}

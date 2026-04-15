package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.EnergyNetworkDistributor;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.item.RtgPelletItem;
import com.hbm.ntm.common.menu.RtgGeneratorMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class RtgGeneratorBlockEntity extends MachineBlockEntity {
    public static final int SLOT_COUNT = 15;
    public static final int HEAT_CAPACITY = 600;
    private static final int POWER_CAPACITY = 100_000;
    private static final int ENERGY_PER_HEAT = 5;
    private static final int[] ACCESSIBLE_SLOTS = new int[SLOT_COUNT];

    static {
        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            ACCESSIBLE_SLOTS[slot] = slot;
        }
    }

    private int heat;

    public RtgGeneratorBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_RTG_GREY.get(), pos, state, SLOT_COUNT);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final RtgGeneratorBlockEntity rtg) {
        boolean dirty = false;

        final PelletTick pelletTick = rtg.tickPellets();
        dirty |= pelletTick.inventoryChanged();

        final int nextHeat = Math.min(HEAT_CAPACITY, pelletTick.totalHeat());
        if (nextHeat != rtg.heat) {
            rtg.heat = nextHeat;
            dirty = true;
        }

        if (nextHeat > 0) {
            final IEnergyStorage storage = rtg.getEnergyStorage(null);
            if (storage != null && storage.receiveEnergy(nextHeat * ENERGY_PER_HEAT, false) > 0) {
                dirty = true;
            }
        }

        if (rtg.pushEnergy(level, pos) > 0) {
            dirty = true;
        }

        if (dirty) {
            rtg.markChangedAndSync();
        }
        rtg.tickMachineStateSync();
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        return this.createSimpleEnergyStorage(POWER_CAPACITY, 0, POWER_CAPACITY);
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.EXTRACT;
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        return !stack.isEmpty() && slot >= 0 && slot < SLOT_COUNT && stack.getItem() instanceof RtgPelletItem;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return false;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return ACCESSIBLE_SLOTS;
    }

    @Override
    public @NotNull net.minecraft.network.chat.Component getDisplayName() {
        return net.minecraft.network.chat.Component.translatable(HbmBlocks.MACHINE_RTG_GREY.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new RtgGeneratorMenu(containerId, inventory, this);
    }

    public int getHeat() {
        return this.heat;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("heat", this.heat);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.heat = Math.max(0, tag.getInt("heat"));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("heat", this.heat);
        tag.putInt("energy", this.getStoredEnergy());
        tag.putInt("maxEnergy", this.getMaxStoredEnergy());
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.heat = Math.max(0, tag.getInt("heat"));
    }

    private PelletTick tickPellets() {
        int totalHeat = 0;
        boolean inventoryChanged = false;

        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            final ItemStack stack = this.getInternalItemHandler().getStackInSlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof final RtgPelletItem pellet)) {
                continue;
            }

            final long beforeLifespan = pellet.doesDecay() ? pellet.getLifespan(stack) : 0L;
            totalHeat += Math.max(0, pellet.getCurrentPower(stack, true));

            final ItemStack decayed = RtgPelletItem.handleDecay(stack);
            if (decayed != stack || !ItemStack.matches(decayed, stack)) {
                this.getInternalItemHandler().setStackInSlot(slot, decayed);
                inventoryChanged = true;
                continue;
            }

            if (pellet.doesDecay() && pellet.getLifespan(stack) != beforeLifespan) {
                inventoryChanged = true;
            }
        }

        return new PelletTick(totalHeat, inventoryChanged);
    }

    private int pushEnergy(final Level level, final BlockPos pos) {
        final IEnergyStorage storage = this.getEnergyStorage(null);
        if (storage == null) {
            return 0;
        }

        final int available = Math.min(storage.getEnergyStored(), storage.extractEnergy(Integer.MAX_VALUE, true));
        if (available <= 0) {
            return 0;
        }

        final int transferred = EnergyNetworkDistributor.distribute(level, pos, available, available, null);
        if (transferred > 0) {
            storage.extractEnergy(transferred, false);
        }
        return transferred;
    }

    private record PelletTick(int totalHeat, boolean inventoryChanged) {
    }
}

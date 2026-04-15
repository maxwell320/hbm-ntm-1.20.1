package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.menu.AshpitMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class AshpitBlockEntity extends MachineBlockEntity {
    public static final int SLOT_COUNT = 5;

    private static final int[] ACCESSIBLE_SLOTS = {0, 1, 2, 3, 4};

    public static final int THRESHOLD_WOOD = 2_000;
    public static final int THRESHOLD_COAL = 2_000;
    public static final int THRESHOLD_MISC = 2_000;
    public static final int THRESHOLD_FLY = 2_000;
    public static final int THRESHOLD_SOOT = 8_000;

    private long ashLevelWood;
    private long ashLevelCoal;
    private long ashLevelMisc;
    private long ashLevelFly;
    private long ashLevelSoot;

    public AshpitBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_ASHPIT.get(), pos, state, SLOT_COUNT);
    }

    public static void serverTick(final Level level,
                                  final BlockPos pos,
                                  final BlockState state,
                                  final AshpitBlockEntity ashpit) {
        ashpit.tickServer();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new AshpitMenu(containerId, inventory, this);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return false;
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return true;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return ACCESSIBLE_SLOTS;
    }

    @Override
    protected Set<String> allowedControlKeys() {
        return Set.of("repair");
    }

    public void addWoodAsh(final long amount) {
        if (amount <= 0L) {
            return;
        }
        this.ashLevelWood = this.safeAdd(this.ashLevelWood, amount);
        this.markChangedAndSync();
    }

    public void addCoalAsh(final long amount) {
        if (amount <= 0L) {
            return;
        }
        this.ashLevelCoal = this.safeAdd(this.ashLevelCoal, amount);
        this.markChangedAndSync();
    }

    public void addMiscAsh(final long amount) {
        if (amount <= 0L) {
            return;
        }
        this.ashLevelMisc = this.safeAdd(this.ashLevelMisc, amount);
        this.markChangedAndSync();
    }

    public void addFlyAsh(final long amount) {
        if (amount <= 0L) {
            return;
        }
        this.ashLevelFly = this.safeAdd(this.ashLevelFly, amount);
        this.markChangedAndSync();
    }

    public void addSootAsh(final long amount) {
        if (amount <= 0L) {
            return;
        }
        this.ashLevelSoot = this.safeAdd(this.ashLevelSoot, amount);
        this.markChangedAndSync();
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putLong("ashLevelWood", this.ashLevelWood);
        tag.putLong("ashLevelCoal", this.ashLevelCoal);
        tag.putLong("ashLevelMisc", this.ashLevelMisc);
        tag.putLong("ashLevelFly", this.ashLevelFly);
        tag.putLong("ashLevelSoot", this.ashLevelSoot);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.ashLevelWood = Math.max(0L, tag.getLong("ashLevelWood"));
        this.ashLevelCoal = Math.max(0L, tag.getLong("ashLevelCoal"));
        this.ashLevelMisc = Math.max(0L, tag.getLong("ashLevelMisc"));
        this.ashLevelFly = Math.max(0L, tag.getLong("ashLevelFly"));
        this.ashLevelSoot = Math.max(0L, tag.getLong("ashLevelSoot"));
    }

    private void tickServer() {
        this.ashLevelWood = this.processAsh(this.ashLevelWood, THRESHOLD_WOOD) ? this.ashLevelWood - THRESHOLD_WOOD : this.ashLevelWood;
        this.ashLevelCoal = this.processAsh(this.ashLevelCoal, THRESHOLD_COAL) ? this.ashLevelCoal - THRESHOLD_COAL : this.ashLevelCoal;
        this.ashLevelMisc = this.processAsh(this.ashLevelMisc, THRESHOLD_MISC) ? this.ashLevelMisc - THRESHOLD_MISC : this.ashLevelMisc;
        this.ashLevelFly = this.processAsh(this.ashLevelFly, THRESHOLD_FLY) ? this.ashLevelFly - THRESHOLD_FLY : this.ashLevelFly;
        this.ashLevelSoot = this.processAsh(this.ashLevelSoot, THRESHOLD_SOOT) ? this.ashLevelSoot - THRESHOLD_SOOT : this.ashLevelSoot;

        this.tickMachineStateSync();
    }

    private boolean processAsh(final long level, final int threshold) {
        if (threshold <= 0 || level < threshold) {
            return false;
        }

        final ItemStack ashStack = new ItemStack(HbmItems.POWDER_ASH.get());
        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            final ItemStack existing = this.getInternalItemHandler().getStackInSlot(slot);
            if (existing.isEmpty()) {
                this.getInternalItemHandler().setStackInSlot(slot, ashStack.copy());
                return true;
            }

            if (ItemStack.isSameItemSameTags(existing, ashStack) && existing.getCount() < existing.getMaxStackSize()) {
                final ItemStack grown = existing.copy();
                grown.grow(1);
                this.getInternalItemHandler().setStackInSlot(slot, grown);
                return true;
            }
        }

        return false;
    }

    private long safeAdd(final long current, final long delta) {
        if (delta <= 0L) {
            return current;
        }
        if (Long.MAX_VALUE - current < delta) {
            return Long.MAX_VALUE;
        }
        return current + delta;
    }
}

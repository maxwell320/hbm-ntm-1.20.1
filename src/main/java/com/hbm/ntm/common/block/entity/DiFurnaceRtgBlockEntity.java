package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.blastfurnace.HbmBlastFurnaceRecipes;
import com.hbm.ntm.common.block.DiFurnaceRtgBlock;
import com.hbm.ntm.common.item.RtgPelletItem;
import com.hbm.ntm.common.menu.DiFurnaceRtgMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
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
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class DiFurnaceRtgBlockEntity extends MachineBlockEntity {
    public static final int SLOT_INPUT_LEFT = 0;
    public static final int SLOT_INPUT_RIGHT = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_RTG_START = 3;
    public static final int SLOT_RTG_END = 8;
    public static final int SLOT_COUNT = 9;

    private static final int[] SLOT_ACCESS = new int[]{
        SLOT_INPUT_LEFT, SLOT_INPUT_RIGHT, SLOT_OUTPUT,
        SLOT_RTG_START, SLOT_RTG_START + 1, SLOT_RTG_START + 2,
        SLOT_RTG_START + 3, SLOT_RTG_START + 4, SLOT_RTG_END
    };
    private static final int POWER_THRESHOLD = 15;
    private static final int PROCESSING_TIME = 1_200;
    private static final int DECAY_INTERVAL = 20;

    private int progress;
    private int power;
    private int decayTimer;

    public DiFurnaceRtgBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_DI_FURNACE_RTG.get(), pos, state, SLOT_COUNT);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final DiFurnaceRtgBlockEntity furnace) {
        HbmBlastFurnaceRecipes.ensureInitialized();
        boolean dirty = false;

        final ItemStackHandler handler = furnace.getInternalItemHandler();

        furnace.decayTimer++;
        if (furnace.decayTimer >= DECAY_INTERVAL) {
            furnace.decayTimer = 0;
            dirty |= furnace.tickPelletDecay(handler);
        }

        furnace.power = furnace.computePower(handler);

        final ItemStack inputLeft = handler.getStackInSlot(SLOT_INPUT_LEFT);
        final ItemStack inputRight = handler.getStackInSlot(SLOT_INPUT_RIGHT);
        final @Nullable HbmBlastFurnaceRecipes.BlastRecipe recipe = HbmBlastFurnaceRecipes.findRecipe(inputLeft, inputRight).orElse(null);
        final boolean canProcess = recipe != null && furnace.canProcess(recipe);
        final boolean hasPower = furnace.power >= POWER_THRESHOLD;
        final boolean active = canProcess && hasPower;

        if (active) {
            furnace.progress += furnace.power;
            dirty = true;
            while (furnace.progress >= PROCESSING_TIME && canProcess && furnace.canProcess(recipe)) {
                furnace.progress -= PROCESSING_TIME;
                furnace.processRecipe(recipe);
            }
        } else if (furnace.progress > 0) {
            furnace.progress = 0;
            dirty = true;
        }

        furnace.updateLitState(active);

        if (dirty) {
            furnace.markChangedAndSync();
        }
        furnace.tickMachineStateSync();
    }

    private boolean tickPelletDecay(final ItemStackHandler handler) {
        boolean changed = false;
        for (int slot = SLOT_RTG_START; slot <= SLOT_RTG_END; slot++) {
            final ItemStack stack = handler.getStackInSlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof RtgPelletItem)) {
                continue;
            }
            final ItemStack decayed = RtgPelletItem.handleDecay(stack);
            if (decayed != stack || !ItemStack.matches(decayed, stack)) {
                handler.setStackInSlot(slot, decayed);
                changed = true;
            }
        }
        return changed;
    }

    private int computePower(final ItemStackHandler handler) {
        int total = 0;
        for (int slot = SLOT_RTG_START; slot <= SLOT_RTG_END; slot++) {
            final ItemStack stack = handler.getStackInSlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof final RtgPelletItem pellet)) {
                continue;
            }
            total += Math.max(0, pellet.getCurrentPower(stack, true));
        }
        return total;
    }

    private boolean canProcess(final HbmBlastFurnaceRecipes.BlastRecipe recipe) {
        final ItemStack output = recipe.result();
        if (output.isEmpty()) {
            return false;
        }
        final ItemStack existing = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (existing.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameTags(existing, output)) {
            return false;
        }
        return existing.getCount() + output.getCount() <= existing.getMaxStackSize();
    }

    private void processRecipe(final HbmBlastFurnaceRecipes.BlastRecipe recipe) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack output = recipe.result();

        final ItemStack existing = handler.getStackInSlot(SLOT_OUTPUT);
        if (existing.isEmpty()) {
            handler.setStackInSlot(SLOT_OUTPUT, output.copy());
        } else {
            final ItemStack merged = existing.copy();
            merged.grow(output.getCount());
            handler.setStackInSlot(SLOT_OUTPUT, merged);
        }

        shrinkInput(SLOT_INPUT_LEFT);
        shrinkInput(SLOT_INPUT_RIGHT);
    }

    private void shrinkInput(final int slot) {
        final ItemStack current = this.getInternalItemHandler().getStackInSlot(slot).copy();
        current.shrink(1);
        this.getInternalItemHandler().setStackInSlot(slot, current.isEmpty() ? ItemStack.EMPTY : current);
    }

    private void updateLitState(final boolean active) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        final BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof DiFurnaceRtgBlock) || !state.hasProperty(DiFurnaceRtgBlock.LIT)) {
            return;
        }
        if (state.getValue(DiFurnaceRtgBlock.LIT) == active) {
            return;
        }
        this.level.setBlock(this.worldPosition, state.setValue(DiFurnaceRtgBlock.LIT, active), 3);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (slot == SLOT_OUTPUT) {
            return false;
        }

        if (slot >= SLOT_RTG_START && slot <= SLOT_RTG_END) {
            return stack.getItem() instanceof RtgPelletItem;
        }

        if (slot == SLOT_INPUT_LEFT || slot == SLOT_INPUT_RIGHT) {
            final ItemStack other = this.getInternalItemHandler().getStackInSlot(slot == SLOT_INPUT_LEFT ? SLOT_INPUT_RIGHT : SLOT_INPUT_LEFT);
            return other.isEmpty() || HbmBlastFurnaceRecipes.findRecipe(stack, other).isPresent();
        }

        return false;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        if (slot == SLOT_OUTPUT) {
            return true;
        }
        if (slot >= SLOT_RTG_START && slot <= SLOT_RTG_END) {
            final ItemStack stack = this.getInternalItemHandler().getStackInSlot(slot);
            return !stack.isEmpty() && !(stack.getItem() instanceof RtgPelletItem);
        }
        return false;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return SLOT_ACCESS;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_DI_FURNACE_RTG.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new DiFurnaceRtgMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getPower() {
        return this.power;
    }

    public int getPowerThreshold() {
        return POWER_THRESHOLD;
    }

    public int getProcessingTime() {
        return PROCESSING_TIME;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("power", this.power);
        tag.putInt("decayTimer", this.decayTimer);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.power = Math.max(0, tag.getInt("power"));
        this.decayTimer = Math.max(0, tag.getInt("decayTimer"));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("power", this.power);
        tag.putInt("powerThreshold", POWER_THRESHOLD);
        tag.putInt("processingTime", PROCESSING_TIME);
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.power = Math.max(0, tag.getInt("power"));
    }
}

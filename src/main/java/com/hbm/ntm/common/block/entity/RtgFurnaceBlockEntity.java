package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.block.RtgFurnaceBlock;
import com.hbm.ntm.common.item.RtgPelletItem;
import com.hbm.ntm.common.menu.RtgFurnaceMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class RtgFurnaceBlockEntity extends MachineBlockEntity {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_RTG_1 = 1;
    public static final int SLOT_RTG_2 = 2;
    public static final int SLOT_RTG_3 = 3;
    public static final int SLOT_OUTPUT = 4;
    public static final int SLOT_COUNT = 5;

    private static final int[] SLOTS_TOP = new int[]{SLOT_INPUT};
    private static final int[] SLOTS_BOTTOM = new int[]{SLOT_OUTPUT};
    private static final int[] SLOTS_SIDE = new int[]{SLOT_RTG_1, SLOT_RTG_2, SLOT_RTG_3};
    private static final int PROCESSING_SPEED = 1_000;

    private int progress;
    private int heat;

    public RtgFurnaceBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_RTG_FURNACE.get(), pos, state, SLOT_COUNT);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final RtgFurnaceBlockEntity furnace) {
        boolean dirty = false;

        final RtgTick rtgTick = furnace.tickRtgs();
        dirty |= rtgTick.dirty();
        furnace.heat = rtgTick.heat();

        final @Nullable SmeltingRecipe recipe = furnace.findRecipe(furnace.getInternalItemHandler().getStackInSlot(SLOT_INPUT));
        final boolean canProcess = recipe != null && furnace.canProcess(recipe);
        final boolean hasPower = furnace.heat > 0;

        if (hasPower && canProcess) {
            furnace.progress += furnace.heat;
            dirty = true;

            while (furnace.progress >= PROCESSING_SPEED && furnace.canProcess(recipe)) {
                furnace.progress -= PROCESSING_SPEED;
                furnace.processRecipe(recipe);
            }
        } else if (furnace.progress > 0) {
            furnace.progress = 0;
            dirty = true;
        }

        furnace.updateLitState(furnace.progress > 0);

        if (dirty) {
            furnace.markChangedAndSync();
        }
        furnace.tickMachineStateSync();
    }

    private RtgTick tickRtgs() {
        boolean dirty = false;
        int totalHeat = 0;

        final ItemStack[] slots = new ItemStack[]{
            this.getInternalItemHandler().getStackInSlot(SLOT_RTG_1),
            this.getInternalItemHandler().getStackInSlot(SLOT_RTG_2),
            this.getInternalItemHandler().getStackInSlot(SLOT_RTG_3)
        };

        for (int i = 0; i < slots.length; i++) {
            final ItemStack stack = slots[i];
            if (stack.isEmpty() || !(stack.getItem() instanceof final RtgPelletItem pellet)) {
                continue;
            }

            totalHeat += Math.max(0, pellet.getCurrentPower(stack, true));
            final ItemStack decayed = RtgPelletItem.handleDecay(stack);
            if (decayed != stack || !ItemStack.matches(decayed, stack)) {
                this.getInternalItemHandler().setStackInSlot(SLOT_RTG_1 + i, decayed);
                dirty = true;
            }
        }

        return new RtgTick(totalHeat, dirty);
    }

    private @Nullable SmeltingRecipe findRecipe(final ItemStack input) {
        if (this.level == null || input.isEmpty()) {
            return null;
        }
        return this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(input.copyWithCount(1)), this.level).orElse(null);
    }

    private boolean canProcess(final SmeltingRecipe recipe) {
        if (this.level == null) {
            return false;
        }

        if (this.getInternalItemHandler().getStackInSlot(SLOT_INPUT).isEmpty()) {
            return false;
        }

        final ItemStack result = recipe.getResultItem(this.level.registryAccess());
        if (result.isEmpty()) {
            return false;
        }

        final ItemStack existing = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (existing.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameTags(existing, result)) {
            return false;
        }
        return existing.getCount() + result.getCount() <= existing.getMaxStackSize();
    }

    private void processRecipe(final SmeltingRecipe recipe) {
        if (this.level == null) {
            return;
        }

        final ItemStack result = recipe.getResultItem(this.level.registryAccess()).copy();
        if (result.isEmpty()) {
            return;
        }

        final ItemStack existing = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (existing.isEmpty()) {
            this.getInternalItemHandler().setStackInSlot(SLOT_OUTPUT, result);
        } else {
            final ItemStack merged = existing.copy();
            merged.grow(result.getCount());
            this.getInternalItemHandler().setStackInSlot(SLOT_OUTPUT, merged);
        }

        final ItemStack reducedInput = this.getInternalItemHandler().getStackInSlot(SLOT_INPUT).copy();
        reducedInput.shrink(1);
        this.getInternalItemHandler().setStackInSlot(SLOT_INPUT, reducedInput.isEmpty() ? ItemStack.EMPTY : reducedInput);
    }

    private void updateLitState(final boolean active) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        final BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof RtgFurnaceBlock) || !state.hasProperty(RtgFurnaceBlock.LIT)) {
            return;
        }
        if (state.getValue(RtgFurnaceBlock.LIT) == active) {
            return;
        }
        this.level.setBlock(this.worldPosition, state.setValue(RtgFurnaceBlock.LIT, active), 3);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot == SLOT_INPUT) {
            return this.findRecipe(stack) != null;
        }
        if (slot >= SLOT_RTG_1 && slot <= SLOT_RTG_3) {
            return stack.getItem() instanceof RtgPelletItem;
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
        if (slot >= SLOT_RTG_1 && slot <= SLOT_RTG_3) {
            final ItemStack stack = this.getInternalItemHandler().getStackInSlot(slot);
            return !stack.isEmpty() && !(stack.getItem() instanceof RtgPelletItem);
        }
        return false;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_BOTTOM;
        }
        if (side == Direction.UP) {
            return SLOTS_TOP;
        }
        return SLOTS_SIDE;
    }

    @Override
    public @NotNull net.minecraft.network.chat.Component getDisplayName() {
        return net.minecraft.network.chat.Component.translatable(HbmBlocks.MACHINE_RTG_FURNACE.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new RtgFurnaceMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getHeat() {
        return this.heat;
    }

    public int getProcessingSpeed() {
        return PROCESSING_SPEED;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("heat", this.heat);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.heat = Math.max(0, tag.getInt("heat"));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("heat", this.heat);
        tag.putInt("processingSpeed", PROCESSING_SPEED);
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.heat = Math.max(0, tag.getInt("heat"));
    }

    private record RtgTick(int heat, boolean dirty) {
    }
}
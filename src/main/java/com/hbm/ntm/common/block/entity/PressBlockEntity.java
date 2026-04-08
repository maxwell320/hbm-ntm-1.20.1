package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.menu.PressMenu;
import com.hbm.ntm.common.item.StampBookItem;
import com.hbm.ntm.common.item.StampItem;
import com.hbm.ntm.common.press.HbmPressRecipes;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class PressBlockEntity extends MachineBlockEntity {
    public static final int SLOT_FUEL = 0;
    public static final int SLOT_STAMP = 1;
    public static final int SLOT_INPUT = 2;
    public static final int SLOT_OUTPUT = 3;
    public static final int SLOT_STORAGE_START = 4;
    public static final int STORAGE_SLOTS = 9;
    public static final int SLOT_COUNT = 13;
    public static final int MAX_SPEED = 400;
    public static final int PROGRESS_AT_MAX = 25;
    public static final int MAX_PRESS = 200;
    public static final int BURN_PER_OPERATION = 200;

    private int speed;
    private int burnTime;
    private int pressTicks;
    private boolean retracting;
    private int delay;

    public PressBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_PRESS.get(), pos, state, SLOT_COUNT);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final PressBlockEntity press) {
        boolean dirty = false;
        final boolean preheated = press.isPreheated();
        final boolean canProcess = press.canProcess();

        if ((canProcess || press.retracting) && press.burnTime >= BURN_PER_OPERATION) {
            final int increase = preheated ? 4 : 1;
            if (press.speed < MAX_SPEED) {
                press.speed = Math.min(MAX_SPEED, press.speed + increase);
                dirty = true;
            }
        } else if (press.speed > 0) {
            press.speed = Math.max(0, press.speed - 1);
            dirty = true;
        }

        if (press.delay <= 0) {
            final int stampSpeed = press.speed * PROGRESS_AT_MAX / MAX_SPEED;
            if (press.retracting) {
                if (press.pressTicks > 0) {
                    press.pressTicks -= stampSpeed;
                    dirty = true;
                }
                if (press.pressTicks <= 0) {
                    press.pressTicks = 0;
                    press.retracting = false;
                    press.delay = 5;
                    dirty = true;
                }
            } else if (canProcess) {
                press.pressTicks += stampSpeed;
                dirty = true;
                if (press.pressTicks >= MAX_PRESS) {
                    press.finishOperation();
                    dirty = true;
                }
            } else if (press.pressTicks > 0) {
                press.retracting = true;
                dirty = true;
            }
        } else {
            press.delay--;
            dirty = true;
        }

        if (press.tryConsumeFuel()) {
            dirty = true;
        }

        if (dirty) {
            press.setChanged();
            press.syncToClient();
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_PRESS.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final Inventory inventory, final Player player) {
        return new PressMenu(containerId, inventory, this);
    }

    @Override
    public boolean isItemValid(final int slot, final ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return switch (slot) {
            case SLOT_FUEL -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
            case SLOT_STAMP -> stack.getItem() instanceof StampItem || stack.getItem() instanceof StampBookItem;
            case SLOT_INPUT -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) <= 0
                && !(stack.getItem() instanceof StampItem)
                && !(stack.getItem() instanceof StampBookItem);
            case SLOT_OUTPUT -> false;
            default -> true;
        };
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final ItemStack stack, final Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final Direction side) {
        return slot == SLOT_OUTPUT;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setClientSpeed(final int speed) {
        this.speed = speed;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public void setClientBurnTime(final int burnTime) {
        this.burnTime = burnTime;
    }

    public int getPressTicks() {
        return this.pressTicks;
    }

    public void setClientPressTicks(final int pressTicks) {
        this.pressTicks = pressTicks;
    }

    private boolean isPreheated() {
        if (this.level == null) {
            return false;
        }
        for (final Direction direction : Direction.values()) {
            if (this.level.getBlockState(this.worldPosition.relative(direction)).is(HbmBlocks.PRESS_PREHEATER.get())) {
                return true;
            }
        }
        return false;
    }

    private boolean tryConsumeFuel() {
        final ItemStack fuelStack = this.getInternalItemHandler().getStackInSlot(SLOT_FUEL);
        if (fuelStack.isEmpty() || this.burnTime >= BURN_PER_OPERATION) {
            return false;
        }
        final int fuelValue = ForgeHooks.getBurnTime(fuelStack, RecipeType.SMELTING);
        if (fuelValue <= 0) {
            return false;
        }
        this.burnTime += fuelValue;
        final ItemStack replacement = fuelStack.hasCraftingRemainingItem() && fuelStack.getCount() == 1
            ? fuelStack.getCraftingRemainingItem()
            : ItemStack.EMPTY;
        final ItemStack remaining = fuelStack.copy();
        remaining.shrink(1);
        if (!replacement.isEmpty()) {
            this.getInternalItemHandler().setStackInSlot(SLOT_FUEL, replacement);
        } else {
            this.getInternalItemHandler().setStackInSlot(SLOT_FUEL, remaining);
        }
        return true;
    }

    private boolean canProcess() {
        if (this.burnTime < BURN_PER_OPERATION) {
            return false;
        }
        final ItemStack stampStack = this.getInternalItemHandler().getStackInSlot(SLOT_STAMP);
        final ItemStack inputStack = this.getInternalItemHandler().getStackInSlot(SLOT_INPUT);
        if (stampStack.isEmpty() || inputStack.isEmpty()) {
            return false;
        }
        final ItemStack output = HbmPressRecipes.getOutput(inputStack, stampStack);
        if (output.isEmpty()) {
            return false;
        }
        final ItemStack existingOutput = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (existingOutput.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameTags(existingOutput, output)
            && existingOutput.getCount() + output.getCount() <= existingOutput.getMaxStackSize();
    }

    private void finishOperation() {
        final ItemStack stampStack = this.getInternalItemHandler().getStackInSlot(SLOT_STAMP);
        final ItemStack inputStack = this.getInternalItemHandler().getStackInSlot(SLOT_INPUT);
        final ItemStack output = HbmPressRecipes.getOutput(inputStack, stampStack);
        if (output.isEmpty()) {
            return;
        }

        final ItemStack existingOutput = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (existingOutput.isEmpty()) {
            this.getInternalItemHandler().setStackInSlot(SLOT_OUTPUT, output.copy());
        } else {
            final ItemStack combined = existingOutput.copy();
            combined.grow(output.getCount());
            this.getInternalItemHandler().setStackInSlot(SLOT_OUTPUT, combined);
        }

        final ItemStack reducedInput = inputStack.copy();
        reducedInput.shrink(1);
        this.getInternalItemHandler().setStackInSlot(SLOT_INPUT, reducedInput);
        this.damageStamp();
        this.retracting = true;
        this.delay = 5;
        this.burnTime -= BURN_PER_OPERATION;
        this.pressTicks = MAX_PRESS;
    }

    private void damageStamp() {
        final ItemStack stampStack = this.getInternalItemHandler().getStackInSlot(SLOT_STAMP);
        if (!stampStack.isDamageableItem()) {
            return;
        }
        final ItemStack updatedStamp = stampStack.copy();
        updatedStamp.setDamageValue(updatedStamp.getDamageValue() + 1);
        if (updatedStamp.getDamageValue() >= updatedStamp.getMaxDamage()) {
            this.getInternalItemHandler().setStackInSlot(SLOT_STAMP, ItemStack.EMPTY);
        } else {
            this.getInternalItemHandler().setStackInSlot(SLOT_STAMP, updatedStamp);
        }
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("speed", this.speed);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("pressTicks", this.pressTicks);
        tag.putBoolean("retracting", this.retracting);
        tag.putInt("delay", this.delay);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.speed = tag.getInt("speed");
        this.burnTime = tag.getInt("burnTime");
        this.pressTicks = tag.getInt("pressTicks");
        this.retracting = tag.getBoolean("retracting");
        this.delay = tag.getInt("delay");
    }
}

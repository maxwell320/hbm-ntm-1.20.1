package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.blastfurnace.HbmBlastFurnaceRecipes;
import com.hbm.ntm.common.block.DiFurnaceBlock;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.BriquetteItemType;
import com.hbm.ntm.common.item.CokeItemType;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.menu.DiFurnaceMenu;
import com.hbm.ntm.common.pollution.PollutionType;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class DiFurnaceBlockEntity extends MachineBlockEntity {
    public static final int SLOT_INPUT_LEFT = 0;
    public static final int SLOT_INPUT_RIGHT = 1;
    public static final int SLOT_FUEL = 2;
    public static final int SLOT_OUTPUT = 3;
    public static final int SLOT_COUNT = 4;
    public static final int TANK_SMOKE = 0;
    public static final int TANK_SMOKE_LEADED = 1;
    public static final int TANK_SMOKE_POISON = 2;

    private static final int[] SLOT_ACCESS = new int[]{SLOT_INPUT_LEFT, SLOT_INPUT_RIGHT, SLOT_FUEL, SLOT_OUTPUT};
    private static final int MAX_FUEL = 20_000;
    private static final int PROCESSING_SPEED = 400;
    private static final int SMOKE_BUFFER_CAPACITY = 50;

    private int progress;
    private int fuel;

    public DiFurnaceBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_DI_FURNACE.get(), pos, state, SLOT_COUNT);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final DiFurnaceBlockEntity furnace) {
        HbmBlastFurnaceRecipes.ensureInitialized();
        boolean dirty = false;

        final boolean extension = furnace.hasExtension();
        furnace.transferSmokeToNeighbors(extension);

        final ItemStackHandler handler = furnace.getInternalItemHandler();
        final ItemStack inputLeft = handler.getStackInSlot(SLOT_INPUT_LEFT);
        final ItemStack inputRight = handler.getStackInSlot(SLOT_INPUT_RIGHT);
        final @Nullable HbmBlastFurnaceRecipes.BlastRecipe recipe = HbmBlastFurnaceRecipes.findRecipe(inputLeft, inputRight).orElse(null);
        final boolean canProcess = recipe != null && furnace.canProcess(recipe);

        if (furnace.fuel < MAX_FUEL) {
            final ItemStack fuelStack = handler.getStackInSlot(SLOT_FUEL);
            final int burn = getFuelPower(fuelStack);
            if (burn > 0) {
                furnace.consumeFuelItem(fuelStack);
                furnace.fuel = Math.min(MAX_FUEL, furnace.fuel + burn);
                dirty = true;
            }
        }

        final boolean hasHeat = furnace.fuel > 0;
        final boolean active = hasHeat && canProcess;

        if (active) {
            furnace.fuel = Math.max(0, furnace.fuel - 1);
            final int speedMultiplier = extension ? 3 : 1;
            furnace.progress += speedMultiplier;
            if (level.getGameTime() % 20L == 0L) {
                furnace.bufferPollutionIntoSmokeTank(TANK_SMOKE, PollutionType.SOOT, HbmFluids.SMOKE.getStillFluid(), MACHINE_SOOT_PER_SECOND * speedMultiplier);
            }
            dirty = true;
            while (furnace.progress >= PROCESSING_SPEED && recipe != null && furnace.canProcess(recipe)) {
                furnace.progress -= PROCESSING_SPEED;
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

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        return new HbmFluidTank[] {
            this.createFluidTank(SMOKE_BUFFER_CAPACITY, this::isSmokeFluid),
            this.createFluidTank(SMOKE_BUFFER_CAPACITY, this::isLeadedSmokeFluid),
            this.createFluidTank(SMOKE_BUFFER_CAPACITY, this::isPoisonSmokeFluid)
        };
    }

    private boolean hasExtension() {
        if (this.level == null) {
            return false;
        }
        return this.level.getBlockState(this.worldPosition.above()).is(HbmBlocks.MACHINE_DI_FURNACE_EXTENSION.get());
    }

    private void transferSmokeToNeighbors(final boolean extension) {
        this.exportTankToNeighbors(TANK_SMOKE);
        this.exportTankToNeighbors(TANK_SMOKE_LEADED);
        this.exportTankToNeighbors(TANK_SMOKE_POISON);

        if (!extension) {
            return;
        }

        this.exportTankToExtensionTop(TANK_SMOKE);
        this.exportTankToExtensionTop(TANK_SMOKE_LEADED);
        this.exportTankToExtensionTop(TANK_SMOKE_POISON);
    }

    private void exportTankToExtensionTop(final int tankIndex) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }

        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        if (tank == null || tank.isEmpty()) {
            return;
        }

        final BlockEntity top = this.level.getBlockEntity(this.worldPosition.above(2));
        if (top == null) {
            return;
        }

        final IFluidHandler handler = top.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN).orElse(null);
        if (handler == null) {
            return;
        }

        final FluidStack stored = tank.getFluid();
        if (stored.isEmpty()) {
            return;
        }

        final int accepted = handler.fill(new FluidStack(stored, stored.getAmount()), IFluidHandler.FluidAction.EXECUTE);
        if (accepted <= 0) {
            return;
        }

        tank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
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

    private void consumeFuelItem(final ItemStack fuelStack) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        if (fuelStack.is(Items.LAVA_BUCKET)) {
            handler.setStackInSlot(SLOT_FUEL, new ItemStack(Items.BUCKET));
            return;
        }

        final Item remainder = fuelStack.hasCraftingRemainingItem() ? fuelStack.getCraftingRemainingItem().getItem() : null;
        fuelStack.shrink(1);

        if (fuelStack.isEmpty()) {
            if (remainder != null) {
                handler.setStackInSlot(SLOT_FUEL, new ItemStack(remainder));
            } else {
                handler.setStackInSlot(SLOT_FUEL, ItemStack.EMPTY);
            }
            return;
        }

        handler.setStackInSlot(SLOT_FUEL, fuelStack);
    }

    private void updateLitState(final boolean active) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        final BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof DiFurnaceBlock) || !state.hasProperty(DiFurnaceBlock.LIT)) {
            return;
        }
        if (state.getValue(DiFurnaceBlock.LIT) == active) {
            return;
        }
        this.level.setBlock(this.worldPosition, state.setValue(DiFurnaceBlock.LIT, active), 3);
    }

    private boolean isSmokeFluid(final net.minecraftforge.fluids.FluidStack stack) {
        return stack.isEmpty() || stack.getFluid() == HbmFluids.SMOKE.getStillFluid();
    }

    private boolean isLeadedSmokeFluid(final net.minecraftforge.fluids.FluidStack stack) {
        return stack.isEmpty() || stack.getFluid() == HbmFluids.SMOKE_LEADED.getStillFluid();
    }

    private boolean isPoisonSmokeFluid(final net.minecraftforge.fluids.FluidStack stack) {
        return stack.isEmpty() || stack.getFluid() == HbmFluids.SMOKE_POISON.getStillFluid();
    }

    public static int getFuelPower(final ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        if (stack.is(Items.LAVA_BUCKET)) {
            return 20_000;
        }

        if (stack.is(Items.COAL_BLOCK)) {
            return 2_000;
        }

        if (stack.is(Items.BLAZE_ROD)) {
            return 150;
        }

        if (stack.is(ItemTags.COALS)) {
            return stack.is(Items.CHARCOAL) ? 100 : 200;
        }

        if (stack.is(HbmItems.getMaterialPart(HbmMaterials.COAL, HbmMaterialShape.DUST).get())) {
            return 100;
        }

        if (stack.is(HbmItems.getMaterialPart(HbmMaterials.LIGNITE, HbmMaterialShape.GEM).get())) {
            return 50;
        }

        if (stack.is(HbmItems.getMaterialPart(HbmMaterials.LIGNITE, HbmMaterialShape.DUST).get())) {
            return 100;
        }

        if (stack.is(HbmItems.getBriquette(BriquetteItemType.LIGNITE).get())) {
            return 200;
        }

        if (stack.is(HbmItems.getBriquette(BriquetteItemType.COAL).get())) {
            return 400;
        }

        if (stack.is(HbmItems.getCoke(CokeItemType.COAL).get())) {
            return 400;
        }

        if (stack.is(HbmItems.getCoke(CokeItemType.LIGNITE).get())) {
            return 200;
        }

        if (stack.is(HbmItems.getCoke(CokeItemType.PETROLEUM).get())) {
            return 400;
        }

        return 0;
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot == SLOT_FUEL) {
            return getFuelPower(stack) > 0;
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
        return slot == SLOT_OUTPUT;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return SLOT_ACCESS;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_DI_FURNACE.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new DiFurnaceMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getFuel() {
        return this.fuel;
    }

    public int getMaxFuel() {
        return MAX_FUEL;
    }

    public int getProcessingSpeed() {
        return PROCESSING_SPEED;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("fuel", this.fuel);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.fuel = Math.max(0, Math.min(MAX_FUEL, tag.getInt("fuel")));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("fuel", this.fuel);
        tag.putInt("maxFuel", MAX_FUEL);
        tag.putInt("processingSpeed", PROCESSING_SPEED);
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.fuel = Math.max(0, Math.min(MAX_FUEL, tag.getInt("fuel")));
    }
}

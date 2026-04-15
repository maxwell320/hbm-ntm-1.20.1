package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.assembly.AssemblyMachineStructure;
import com.hbm.ntm.common.assembly.HbmAssemblyRecipes;
import com.hbm.ntm.common.config.AssemblyMachineConfig;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.BlueprintItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.menu.AssemblyMachineMenu;
import com.hbm.ntm.common.multiblock.MultiblockCoreBE;
import com.hbm.ntm.common.multiblock.MultiblockStructure;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class AssemblyMachineBlockEntity extends MultiblockCoreBE {
    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_BLUEPRINT = 1;
    public static final int SLOT_UPGRADE_1 = 2;
    public static final int SLOT_UPGRADE_2 = 3;
    public static final int SLOT_INPUT_START = 4;
    public static final int SLOT_INPUT_END = 16;
    public static final int INPUT_COUNT = 12;
    public static final int SLOT_OUTPUT = 16;
    public static final int SLOT_COUNT = 17;
    public static final int TANK_INPUT = 0;
    public static final int TANK_OUTPUT = 1;
    public static final int MAX_POWER = 100_000;
    public static final int MAX_TANK = 4_000;

    private int progress;
    private String selectedRecipeId = "";

    public AssemblyMachineBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_ASSEMBLY_MACHINE.get(), pos, state, SLOT_COUNT);
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        final int capacity = Math.max(1, AssemblyMachineConfig.INSTANCE.maxPower());
        return this.createSimpleEnergyStorage(capacity, capacity, 0);
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        final int capacity = Math.max(1, AssemblyMachineConfig.INSTANCE.tankCapacity());
        return new HbmFluidTank[]{
            this.createFluidTank(capacity),
            this.createFluidTank(capacity)
        };
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.RECEIVE;
    }

    @Override
    protected boolean isUpgradeSlot(final int slot) {
        return slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2;
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final AssemblyMachineBlockEntity machine) {
        HbmAssemblyRecipes.ensureInitialized();
        boolean dirty = false;

        if (machine.tryChargeFromBattery()) {
            dirty = true;
        }

        if (machine.syncSelectedRecipe()) {
            dirty = true;
        }
        if (machine.tryAutoSwitchRecipe()) {
            machine.progress = 0;
            machine.markChangedAndSync();
            machine.tickMachineStateSync();
            return;
        }

        final Optional<HbmAssemblyRecipes.AssemblyRecipe> selectedRecipe = machine.getSelectedRecipe();
        if (selectedRecipe.isEmpty() || !machine.canProcess(selectedRecipe.get())) {
            if (machine.progress > 0) {
                machine.progress = 0;
                dirty = true;
            }
            if (dirty) {
                machine.markChangedAndSync();
            }
            machine.tickMachineStateSync();
            return;
        }

        machine.progress++;
        machine.consumeEnergy((int) Math.min(Integer.MAX_VALUE, selectedRecipe.get().consumption()));
        dirty = true;

        if (machine.progress >= selectedRecipe.get().duration()) {
            machine.finishRecipe(selectedRecipe.get());
            machine.progress = 0;
            dirty = true;
            if (machine.syncSelectedRecipe()) {
                dirty = true;
            }
        }

        if (dirty) {
            machine.markChangedAndSync();
        }
        machine.tickMachineStateSync();
    }

    public List<HbmAssemblyRecipes.AssemblyRecipe> visibleRecipes() {
        return HbmAssemblyRecipes.visibleForPool(this.getBlueprintPool());
    }

    public Optional<HbmAssemblyRecipes.AssemblyRecipe> getSelectedRecipe() {
        return HbmAssemblyRecipes.findById(this.selectedRecipeId)
            .filter(recipe -> recipe.matchesBlueprintPool(this.getBlueprintPool()));
    }

    public int getSelectedRecipeIndex() {
        final List<HbmAssemblyRecipes.AssemblyRecipe> visible = this.visibleRecipes();
        for (int i = 0; i < visible.size(); i++) {
            if (visible.get(i).id().equals(this.selectedRecipeId)) {
                return i;
            }
        }
        return visible.isEmpty() ? -1 : 0;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setClientProgress(final int progress) {
        this.progress = progress;
    }

    public @Nullable HbmFluidTank getInputTank() {
        return this.getFluidTank(TANK_INPUT);
    }

    public @Nullable HbmFluidTank getOutputTank() {
        return this.getFluidTank(TANK_OUTPUT);
    }

    public String getSelectedRecipeId() {
        return this.selectedRecipeId;
    }

    public void cycleRecipe(final int delta) {
        final List<HbmAssemblyRecipes.AssemblyRecipe> visible = this.visibleRecipes();
        if (visible.isEmpty()) {
            if (!this.selectedRecipeId.isEmpty()) {
                this.selectedRecipeId = "";
                this.progress = 0;
                this.markChangedAndSync();
            }
            return;
        }
        int index = this.getSelectedRecipeIndex();
        if (index < 0) {
            index = 0;
        }
        final int nextIndex = Math.floorMod(index + delta, visible.size());
        final String nextId = visible.get(nextIndex).id();
        if (!nextId.equals(this.selectedRecipeId)) {
            this.selectedRecipeId = nextId;
            this.progress = 0;
            this.markChangedAndSync();
        }
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (slot == SLOT_BATTERY) {
            return stack.getItem() instanceof BatteryItem;
        }
        if (slot == SLOT_BLUEPRINT) {
            return stack.getItem() instanceof BlueprintItem;
        }
        if (slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2) {
            return stack.getItem() instanceof MachineUpgradeItem;
        }
        if (slot >= SLOT_INPUT_START && slot < SLOT_INPUT_END) {
            return this.acceptsInputSlot(slot, stack);
        }
        return false;
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        if (slot == SLOT_OUTPUT) {
            return true;
        }
        if (slot >= SLOT_INPUT_START && slot < SLOT_INPUT_END) {
            final ItemStack stack = this.getInternalItemHandler().getStackInSlot(slot);
            if (stack.isEmpty()) {
                return false;
            }
            return !this.acceptsInputSlot(slot, stack);
        }
        return false;
    }

    @Override
    protected void onInventoryChanged(final int slot) {
        super.onInventoryChanged(slot);
        if (slot == SLOT_BLUEPRINT || (slot >= SLOT_INPUT_START && slot < SLOT_INPUT_END)) {
            this.progress = 0;
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_ASSEMBLY_MACHINE.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new AssemblyMachineMenu(containerId, inventory, this);
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        super.saveMachineData(tag);
        tag.putInt("progress", this.progress);
        tag.putString("selectedRecipeId", this.selectedRecipeId);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        super.loadMachineData(tag);
        this.progress = tag.getInt("progress");
        this.selectedRecipeId = tag.getString("selectedRecipeId");
    }

    @Override
    protected void applyControlData(final CompoundTag data) {
        super.applyControlData(data);
        if (data.contains("cycleRecipe")) {
            this.cycleRecipe(data.getInt("cycleRecipe"));
        }
    }

    @Override
    protected Set<String> allowedControlKeys() {
        return Set.of("repair", "cycleRecipe");
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putString("selectedRecipeId", this.selectedRecipeId);
        tag.putInt("selectedRecipeIndex", this.getSelectedRecipeIndex());
        tag.putInt("visibleRecipeCount", this.visibleRecipes().size());
        tag.putInt("energy", this.getStoredEnergy());
        tag.putInt("maxEnergy", this.getMaxStoredEnergy());

        final HbmFluidTank inputTank = this.getInputTank();
        tag.putInt("inputAmount", inputTank == null ? 0 : inputTank.getFluidAmount());
        tag.putInt("inputCapacity", inputTank == null ? 0 : inputTank.getCapacity());
        tag.putString("inputFluid", this.getFluidDisplayName(inputTank));

        final HbmFluidTank outputTank = this.getOutputTank();
        tag.putInt("outputAmount", outputTank == null ? 0 : outputTank.getFluidAmount());
        tag.putInt("outputCapacity", outputTank == null ? 0 : outputTank.getCapacity());
        tag.putString("outputFluid", this.getFluidDisplayName(outputTank));
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.selectedRecipeId = tag.getString("selectedRecipeId");
    }

    @Override
    public MultiblockStructure getStructure() {
        return AssemblyMachineStructure.INSTANCE;
    }

    private Optional<HbmAssemblyRecipes.AssemblyRequirement> getRequirementForSlot(final int slot) {
        final int index = slot - SLOT_INPUT_START;
        final Optional<HbmAssemblyRecipes.AssemblyRecipe> selectedRecipe = this.getSelectedRecipe();
        if (selectedRecipe.isEmpty() || index < 0) {
            return Optional.empty();
        }
        final HbmAssemblyRecipes.AssemblyRequirement requirement = selectedRecipe.get().requirementForSlot(index, this.getInputStacks());
        return Optional.ofNullable(requirement);
    }

    private boolean syncSelectedRecipe() {
        final List<HbmAssemblyRecipes.AssemblyRecipe> visible = this.visibleRecipes();
        final String nextId = visible.isEmpty() ? "" : visible.stream()
            .filter(recipe -> recipe.id().equals(this.selectedRecipeId))
            .findFirst()
            .map(HbmAssemblyRecipes.AssemblyRecipe::id)
            .orElseGet(() -> visible.get(0).id());
        if (nextId.equals(this.selectedRecipeId)) {
            return false;
        }
        this.selectedRecipeId = nextId;
        this.progress = 0;
        return true;
    }

    private @Nullable String getBlueprintPool() {
        return BlueprintItem.getPool(this.getInternalItemHandler().getStackInSlot(SLOT_BLUEPRINT));
    }

    private boolean canProcess(final HbmAssemblyRecipes.AssemblyRecipe recipe) {
        if (this.getStoredEnergy() < recipe.consumption()) {
            return false;
        }
        if (this.findMatchedRequirements(recipe).isEmpty()) {
            return false;
        }
        if (!this.canConsumeInputFluid(recipe)) {
            return false;
        }
        if (!this.canAcceptOutput(recipe.outputCopy())) {
            return false;
        }
        return this.canAcceptOutputFluid(recipe.fluidOutputCopy());
    }

    private boolean canConsumeInputFluid(final HbmAssemblyRecipes.AssemblyRecipe recipe) {
        final HbmFluidTank inputTank = this.getInputTank();
        if (recipe.fluidInput().isEmpty()) {
            return true;
        }
        return inputTank != null && !inputTank.isEmpty() && inputTank.getFluid().isFluidEqual(recipe.fluidInput())
            && inputTank.getFluidAmount() >= recipe.fluidInput().getAmount();
    }

    private boolean canAcceptOutput(final ItemStack result) {
        final ItemStack output = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (output.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameTags(output, result)
            && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private boolean canAcceptOutputFluid(final net.minecraftforge.fluids.FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return true;
        }
        final HbmFluidTank outputTank = this.getOutputTank();
        if (outputTank == null) {
            return false;
        }
        return outputTank.fill(fluidStack.copy(), net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE) == fluidStack.getAmount();
    }

    private void finishRecipe(final HbmAssemblyRecipes.AssemblyRecipe recipe) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        final List<HbmAssemblyRecipes.AssemblyRequirement> requirements = this.findMatchedRequirements(recipe).orElse(recipe.itemInputs());
        for (int i = 0; i < requirements.size(); i++) {
            final ItemStack stack = handler.getStackInSlot(SLOT_INPUT_START + i).copy();
            stack.shrink(requirements.get(i).count());
            handler.setStackInSlot(SLOT_INPUT_START + i, stack.isEmpty() ? ItemStack.EMPTY : stack);
        }
        if (!recipe.fluidInput().isEmpty()) {
            final HbmFluidTank inputTank = this.getInputTank();
            if (inputTank != null) {
                inputTank.drain(recipe.fluidInput().getAmount(), net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            }
        }
        final ItemStack output = handler.getStackInSlot(SLOT_OUTPUT);
        final ItemStack result = recipe.outputCopy();
        if (output.isEmpty()) {
            handler.setStackInSlot(SLOT_OUTPUT, result);
        } else {
            final ItemStack merged = output.copy();
            merged.grow(result.getCount());
            handler.setStackInSlot(SLOT_OUTPUT, merged);
        }
        if (!recipe.fluidOutput().isEmpty()) {
            final HbmFluidTank outputTank = this.getOutputTank();
            if (outputTank != null) {
                outputTank.fill(recipe.fluidOutputCopy(), net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    private boolean tryChargeFromBattery() {
        final ItemStack battery = this.getInternalItemHandler().getStackInSlot(SLOT_BATTERY);
        if (battery.isEmpty()) {
            return false;
        }
        if (battery.getItem() instanceof final BatteryItem batteryItem) {
            final int stored = this.getStoredEnergy();
            final int space = this.getMaxStoredEnergy() - stored;
            if (space <= 0) {
                return false;
            }
            final int rate = batteryItem.getDischargeRate();
            final int charge = batteryItem.getStoredEnergy(battery);
            final int toDischarge = Math.min(Math.min(space, rate), charge);
            if (toDischarge <= 0) {
                return false;
            }
            batteryItem.withStoredEnergy(battery, charge - toDischarge);
            this.receiveEnergy(toDischarge);
            return true;
        }
        return false;
    }

    private void consumeEnergy(final int amount) {
        final var storage = this.getEnergyStorage(null);
        if (storage != null) {
            storage.extractEnergy(amount, false);
        }
    }

    private void receiveEnergy(final int amount) {
        final var storage = this.getEnergyStorage(null);
        if (storage != null) {
            storage.receiveEnergy(amount, false);
        }
    }

    private List<ItemStack> getInputStacks() {
        final List<ItemStack> stacks = new java.util.ArrayList<>(INPUT_COUNT);
        final ItemStackHandler handler = this.getInternalItemHandler();
        for (int i = 0; i < INPUT_COUNT; i++) {
            stacks.add(handler.getStackInSlot(SLOT_INPUT_START + i));
        }
        return stacks;
    }

    private Optional<List<HbmAssemblyRecipes.AssemblyRequirement>> findMatchedRequirements(final HbmAssemblyRecipes.AssemblyRecipe recipe) {
        return recipe.findMatchingRequirements(this.getInputStacks());
    }

    private boolean acceptsInputSlot(final int slot, final ItemStack stack) {
        final int index = slot - SLOT_INPUT_START;
        if (index < 0 || index >= INPUT_COUNT || stack.isEmpty()) {
            return false;
        }
        final Optional<HbmAssemblyRecipes.AssemblyRecipe> selected = this.getSelectedRecipe();
        if (selected.isEmpty()) {
            return false;
        }
        if (selected.get().acceptsInAnyVariant(index, stack)) {
            return true;
        }
        if (index != 0 || selected.get().autoSwitchGroup() == null) {
            return false;
        }
        return HbmAssemblyRecipes.findAutoSwitchRecipe(selected.get().id(), selected.get().autoSwitchGroup(), this.getBlueprintPool(), stack).isPresent();
    }

    private boolean tryAutoSwitchRecipe() {
        final Optional<HbmAssemblyRecipes.AssemblyRecipe> selected = this.getSelectedRecipe();
        if (selected.isEmpty() || selected.get().autoSwitchGroup() == null) {
            return false;
        }
        final ItemStack firstInput = this.getInternalItemHandler().getStackInSlot(SLOT_INPUT_START);
        if (firstInput.isEmpty()) {
            return false;
        }
        final Optional<HbmAssemblyRecipes.AssemblyRecipe> switched = HbmAssemblyRecipes.findAutoSwitchRecipe(
            selected.get().id(), selected.get().autoSwitchGroup(), this.getBlueprintPool(), firstInput);
        if (switched.isEmpty() || switched.get().id().equals(this.selectedRecipeId)) {
            return false;
        }
        this.selectedRecipeId = switched.get().id();
        return true;
    }

    private String getFluidDisplayName(final @Nullable HbmFluidTank tank) {
        if (tank == null || tank.isEmpty()) {
            return "";
        }
        final ResourceLocation fluidId = ForgeRegistries.FLUIDS.getKey(tank.getFluid().getFluid());
        if (fluidId == null) {
            return "";
        }
        return tank.getFluid().getDisplayName().getString();
    }
}

package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.config.SolderingStationMachineConfig;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.menu.SolderingStationMenu;
import com.hbm.ntm.common.multiblock.MultiblockCoreBE;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.soldering.HbmSolderingRecipes;
import com.hbm.ntm.common.soldering.SolderingStationStructure;
import java.util.List;
import java.util.Map;
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
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class SolderingStationBlockEntity extends MultiblockCoreBE {
    public static final int SLOT_TOPPING_START = 0;
    public static final int SLOT_TOPPING_END = 3;
    public static final int SLOT_PCB_START = 3;
    public static final int SLOT_PCB_END = 5;
    public static final int SLOT_SOLDER = 5;
    public static final int SLOT_OUTPUT = 6;
    public static final int SLOT_BATTERY = 7;
    public static final int SLOT_FLUID_ID = 8;
    public static final int SLOT_UPGRADE_1 = 9;
    public static final int SLOT_UPGRADE_2 = 10;
    public static final int SLOT_COUNT = 11;
    public static final int BASE_MAX_POWER = 2_000;
    private static final int INTERNAL_ENERGY_CAPACITY = 2_000_000;

    private int progress;
    private int processTime = 1;
    private long consumption = 100;
    private long maxPower = BASE_MAX_POWER;
    private boolean collisionPrevention;

    public SolderingStationBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_SOLDERING_STATION.get(), pos, state, SLOT_COUNT);
    }

    @Override
    public SolderingStationStructure getStructure() {
        return SolderingStationStructure.INSTANCE;
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        final int capacity = Math.max(1, SolderingStationMachineConfig.INSTANCE.internalEnergyCapacity());
        return this.createSimpleEnergyStorage(capacity, capacity, 0);
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        return new HbmFluidTank[]{this.createFluidTank(Math.max(1, SolderingStationMachineConfig.INSTANCE.fluidTankCapacity()), this::isFluidAllowed)};
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.RECEIVE;
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final SolderingStationBlockEntity station) {
        boolean dirty = false;

        if (station.tryChargeFromBattery()) {
            dirty = true;
        }

        final var recipe = station.findRecipe();
        long intendedMaxPower = Math.max(1, SolderingStationMachineConfig.INSTANCE.baseMaxPower());

        if (recipe != null) {
            final int speedLevel = station.countUpgrades(MachineUpgradeItem.UpgradeType.SPEED);
            final int powerLevel = station.countUpgrades(MachineUpgradeItem.UpgradeType.POWER);
            final int overdriveLevel = station.countUpgrades(MachineUpgradeItem.UpgradeType.OVERDRIVE);
            final int duration = recipe.duration();
            final long baseConsumption = recipe.consumption();

            station.processTime = Math.max(1, duration - (duration * speedLevel / 6) + (duration * powerLevel / 3));
            long consumption = baseConsumption + (baseConsumption * speedLevel) - (baseConsumption * powerLevel / 6);
            consumption *= 1L << Math.max(0, overdriveLevel);
            station.consumption = Math.max(0L, consumption);
            intendedMaxPower = station.consumption * 20L;

            if (station.canProcess(recipe)) {
                station.progress += 1 + Math.max(0, overdriveLevel);
                station.consumeEnergy(station.consumption);
                dirty = true;

                if (station.progress >= station.processTime) {
                    station.progress = 0;
                    if (station.consumeItems(recipe)) {
                        station.insertOutput(recipe.outputCopy());
                    }
                    dirty = true;
                }
            } else if (station.progress > 0) {
                station.progress = 0;
                dirty = true;
            }
        } else {
            if (station.progress > 0) {
                station.progress = 0;
                dirty = true;
            }
            station.consumption = 100;
            station.processTime = 1;
        }

        station.maxPower = Math.max(intendedMaxPower, station.getStoredEnergy());
        if (dirty) {
            station.markChangedAndSync();
        }
        station.tickMachineStateSync();
    }

    private @Nullable HbmSolderingRecipes.SolderingRecipe findRecipe() {
        final HbmFluidTank tank = this.getFluidTank(0);
        final FluidStack fluid = tank == null || tank.isEmpty() ? FluidStack.EMPTY : tank.getFluid().copy();
        final ItemStackHandler handler = this.getInternalItemHandler();
        return HbmSolderingRecipes.findRecipe(fluid,
            handler.getStackInSlot(0),
            handler.getStackInSlot(1),
            handler.getStackInSlot(2),
            handler.getStackInSlot(3),
            handler.getStackInSlot(4),
            handler.getStackInSlot(5)).orElse(null);
    }

    private boolean canProcess(final HbmSolderingRecipes.SolderingRecipe recipe) {
        if (this.getStoredEnergy() < this.consumption) {
            return false;
        }

        final HbmFluidTank tank = this.getFluidTank(0);
        final FluidStack requiredFluid = recipe.fluidCopy();
        if (!requiredFluid.isEmpty()) {
            if (tank == null || tank.isEmpty() || !tank.getFluid().isFluidEqual(requiredFluid) || tank.getFluidAmount() < requiredFluid.getAmount()) {
                return false;
            }
        }

        if (this.collisionPrevention && requiredFluid.isEmpty() && tank != null && tank.getFluidAmount() > 0) {
            return false;
        }

        final ItemStack output = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        final ItemStack produced = recipe.outputCopy();
        if (output.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameTags(output, produced)
            && output.getCount() + produced.getCount() <= output.getMaxStackSize();
    }

    private boolean consumeItems(final HbmSolderingRecipes.SolderingRecipe recipe) {
        if (!this.consumeCategory(recipe.toppings(), SLOT_TOPPING_START, SLOT_TOPPING_END)) {
            return false;
        }
        if (!this.consumeCategory(recipe.pcb(), SLOT_PCB_START, SLOT_PCB_END)) {
            return false;
        }
        if (!this.consumeCategory(recipe.solder(), SLOT_SOLDER, SLOT_SOLDER + 1)) {
            return false;
        }

        final FluidStack requiredFluid = recipe.fluidCopy();
        if (!requiredFluid.isEmpty()) {
            final HbmFluidTank tank = this.getFluidTank(0);
            if (tank == null) {
                return false;
            }
            tank.drain(requiredFluid.getAmount(), IFluidHandler.FluidAction.EXECUTE);
        }

        return true;
    }

    private boolean consumeCategory(final List<com.hbm.ntm.common.recipe.CountedIngredient> requirements, final int start, final int end) {
        for (final var requirement : requirements) {
            boolean consumed = false;
            for (int slot = start; slot < end; slot++) {
                final ItemStack stack = this.getInternalItemHandler().getStackInSlot(slot);
                if (!requirement.matches(stack)) {
                    continue;
                }
                final ItemStack reduced = stack.copy();
                reduced.shrink(requirement.count());
                this.getInternalItemHandler().setStackInSlot(slot, reduced.isEmpty() ? ItemStack.EMPTY : reduced);
                consumed = true;
                break;
            }
            if (!consumed) {
                return false;
            }
        }
        return true;
    }

    private void insertOutput(final ItemStack produced) {
        final ItemStack existing = this.getInternalItemHandler().getStackInSlot(SLOT_OUTPUT);
        if (existing.isEmpty()) {
            this.getInternalItemHandler().setStackInSlot(SLOT_OUTPUT, produced);
            return;
        }
        final ItemStack merged = existing.copy();
        merged.grow(produced.getCount());
        this.getInternalItemHandler().setStackInSlot(SLOT_OUTPUT, merged);
    }

    private boolean tryChargeFromBattery() {
        final ItemStack battery = this.getInternalItemHandler().getStackInSlot(SLOT_BATTERY);
        if (battery.isEmpty()) {
            return false;
        }
        if (battery.getItem() instanceof final BatteryItem batteryItem) {
            final int space = this.getMaxStoredEnergy() - this.getStoredEnergy();
            if (space <= 0) {
                return false;
            }
            final int charge = batteryItem.getStoredEnergy(battery);
            final int rate = batteryItem.getDischargeRate();
            final int toDischarge = Math.min(Math.min(space, rate), charge);
            if (toDischarge <= 0) {
                return false;
            }
            batteryItem.withStoredEnergy(battery, charge - toDischarge);
            final IEnergyStorage storage = this.getEnergyStorage(null);
            if (storage == null) {
                return false;
            }
            storage.receiveEnergy(toDischarge, false);
            return true;
        }
        return false;
    }

    private void consumeEnergy(final long amount) {
        final IEnergyStorage storage = this.getEnergyStorage(null);
        if (storage == null || amount <= 0) {
            return;
        }
        storage.extractEnergy((int) Math.min(Integer.MAX_VALUE, amount), false);
    }

    private boolean isFluidAllowed(final FluidStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        final ItemStack idStack = this.getInternalItemHandler().getStackInSlot(SLOT_FLUID_ID);
        if (!(idStack.getItem() instanceof final IItemFluidIdentifier identifier)) {
            return true;
        }
        final ResourceLocation fluidId = identifier.getFluidId(idStack);
        if (fluidId == null) {
            return true;
        }
        final ResourceLocation stackId = ForgeRegistries.FLUIDS.getKey(stack.getFluid());
        return fluidId.equals(stackId);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot >= SLOT_TOPPING_START && slot < SLOT_TOPPING_END) {
            return this.isUniqueInRange(slot, stack, SLOT_TOPPING_START, SLOT_TOPPING_END) && this.matchesTopping(stack);
        }
        if (slot >= SLOT_PCB_START && slot < SLOT_PCB_END) {
            return this.isUniqueInRange(slot, stack, SLOT_PCB_START, SLOT_PCB_END) && this.matchesPcb(stack);
        }
        if (slot == SLOT_SOLDER) {
            return this.matchesSolder(stack);
        }
        if (slot == SLOT_BATTERY) {
            return stack.getItem() instanceof BatteryItem;
        }
        if (slot == SLOT_FLUID_ID) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        }
        if (slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2) {
            return stack.getItem() instanceof MachineUpgradeItem;
        }
        return false;
    }

    private boolean isUniqueInRange(final int slot, final ItemStack stack, final int start, final int end) {
        for (int i = start; i < end; i++) {
            if (i == slot) {
                continue;
            }
            final ItemStack existing = this.getInternalItemHandler().getStackInSlot(i);
            if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, stack)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesTopping(final ItemStack stack) {
        return HbmSolderingRecipes.all().stream()
            .flatMap(recipe -> recipe.toppings().stream())
            .anyMatch(ingredient -> ingredient.matches(stack));
    }

    private boolean matchesPcb(final ItemStack stack) {
        return HbmSolderingRecipes.all().stream()
            .flatMap(recipe -> recipe.pcb().stream())
            .anyMatch(ingredient -> ingredient.matches(stack));
    }

    private boolean matchesSolder(final ItemStack stack) {
        return HbmSolderingRecipes.all().stream()
            .flatMap(recipe -> recipe.solder().stream())
            .anyMatch(ingredient -> ingredient.matches(stack));
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
        return new int[]{0, 1, 2, 3, 4, 5, 6};
    }

    @Override
    protected boolean isUpgradeSlot(final int slot) {
        return slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_SOLDERING_STATION.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new SolderingStationMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getProcessTime() {
        return this.processTime;
    }

    public long getConsumption() {
        return this.consumption;
    }

    public long getDisplayMaxPower() {
        return this.maxPower;
    }

    public boolean isCollisionPrevention() {
        return this.collisionPrevention;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        super.saveMachineData(tag);
        tag.putInt("progress", this.progress);
        tag.putInt("processTime", this.processTime);
        tag.putLong("consumption", this.consumption);
        tag.putLong("maxPower", this.maxPower);
        tag.putBoolean("collisionPrevention", this.collisionPrevention);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        super.loadMachineData(tag);
        this.progress = tag.getInt("progress");
        this.processTime = Math.max(1, tag.getInt("processTime"));
        this.consumption = Math.max(0L, tag.getLong("consumption"));
        this.maxPower = Math.max(Math.max(1, SolderingStationMachineConfig.INSTANCE.baseMaxPower()), tag.getLong("maxPower"));
        this.collisionPrevention = tag.getBoolean("collisionPrevention");
    }

    @Override
    protected void applyControlData(final CompoundTag data) {
        if (data.contains("collision")) {
            this.collisionPrevention = !this.collisionPrevention;
        }
    }

    @Override
    protected Set<String> allowedControlKeys() {
        return Set.of("repair", "collision");
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("processTime", this.processTime);
        tag.putLong("consumption", this.consumption);
        tag.putLong("maxPower", this.maxPower);
        tag.putInt("energy", this.getStoredEnergy());
        tag.putBoolean("collisionPrevention", this.collisionPrevention);

        final HbmFluidTank tank = this.getFluidTank(0);
        tag.putInt("fluidAmount", tank == null ? 0 : tank.getFluidAmount());
        tag.putInt("fluidCapacity", tank == null ? 0 : tank.getCapacity());
        tag.putString("fluidName", this.getFluidDisplayName(tank));
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.processTime = Math.max(1, tag.getInt("processTime"));
        this.consumption = Math.max(0L, tag.getLong("consumption"));
        this.maxPower = Math.max(1L, tag.getLong("maxPower"));
        this.collisionPrevention = tag.getBoolean("collisionPrevention");
    }

    @Override
    public Map<MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of(
            MachineUpgradeItem.UpgradeType.SPEED, 3,
            MachineUpgradeItem.UpgradeType.POWER, 3,
            MachineUpgradeItem.UpgradeType.OVERDRIVE, 3
        );
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


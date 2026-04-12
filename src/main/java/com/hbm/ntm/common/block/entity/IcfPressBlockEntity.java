package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.block.IcfPressBlock;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.item.IcfPelletItem;
import com.hbm.ntm.common.item.IcfPelletItem.EnumICFFuel;
import com.hbm.ntm.common.menu.IcfPressMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.Map;
import java.util.Objects;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class IcfPressBlockEntity extends MachineBlockEntity {
    public static final int SLOT_PELLET_EMPTY = 0;
    public static final int SLOT_PELLET_FILLED = 1;
    public static final int SLOT_MUON_INPUT = 2;
    public static final int SLOT_MUON_OUTPUT = 3;
    public static final int SLOT_SOLID_FUEL_A = 4;
    public static final int SLOT_SOLID_FUEL_B = 5;
    public static final int SLOT_FLUID_ID_A = 6;
    public static final int SLOT_FLUID_ID_B = 7;
    public static final int SLOT_COUNT = 8;

    public static final int TANK_FUEL_A = 0;
    public static final int TANK_FUEL_B = 1;
    public static final int MAX_MUON = 16;
    private static final int FUEL_FLUID_CONSUMPTION = 1_000;
    private static final int TANK_CAPACITY = 16_000;
    private static final int[] TOP_BOTTOM_SLOTS = new int[]{
        SLOT_PELLET_EMPTY,
        SLOT_PELLET_FILLED,
        SLOT_MUON_INPUT,
        SLOT_MUON_OUTPUT,
        SLOT_SOLID_FUEL_A
    };
    private static final int[] SIDE_SLOTS = new int[]{
        SLOT_PELLET_EMPTY,
        SLOT_PELLET_FILLED,
        SLOT_MUON_INPUT,
        SLOT_MUON_OUTPUT,
        SLOT_SOLID_FUEL_B
    };

    private int muonCharge;
    private final ResourceLocation[] selectedFuelFluids = new ResourceLocation[2];

    public IcfPressBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_ICF_PRESS.get(), pos, state, SLOT_COUNT);
        this.selectedFuelFluids[TANK_FUEL_A] = defaultFuelSelection(HbmFluids.DEUTERIUM);
        this.selectedFuelFluids[TANK_FUEL_B] = defaultFuelSelection(HbmFluids.TRITIUM);
    }

    private static @Nullable ResourceLocation defaultFuelSelection(final HbmFluids.FluidEntry entry) {
        return ForgeRegistries.FLUIDS.getKey(entry.getStillFluid());
    }

    @Override
    protected @Nullable com.hbm.ntm.common.energy.HbmEnergyStorage createEnergyStorage() {
        return null;
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        return new HbmFluidTank[]{
            this.createFluidTank(TANK_CAPACITY, stack -> this.isAcceptedFuelFluid(TANK_FUEL_A, stack)),
            this.createFluidTank(TANK_CAPACITY, stack -> this.isAcceptedFuelFluid(TANK_FUEL_B, stack))
        };
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.NONE;
    }

    public static void serverTick(final Level level,
                                  final BlockPos pos,
                                  final BlockState state,
                                  final IcfPressBlockEntity press) {
        boolean dirty = false;

        if (press.updateFluidSelection()) {
            dirty = true;
        }
        if (press.tryLoadMuonCatalyst()) {
            dirty = true;
        }
        if (press.tryFillPellet()) {
            dirty = true;
        }

        if (dirty) {
            press.markChangedAndSync();
        }
        press.tickMachineStateSync();
    }

    private boolean updateFluidSelection() {
        boolean changed = false;
        changed |= this.updateFluidSelectionForSlot(TANK_FUEL_A, SLOT_FLUID_ID_A);
        changed |= this.updateFluidSelectionForSlot(TANK_FUEL_B, SLOT_FLUID_ID_B);
        return changed;
    }

    private boolean updateFluidSelectionForSlot(final int tankIndex, final int slotIndex) {
        final ItemStack stack = this.getInternalItemHandler().getStackInSlot(slotIndex);
        if (!(stack.getItem() instanceof IItemFluidIdentifier identifier)) {
            return false;
        }

        final ResourceLocation fluidId = identifier.getFluidId(stack);
        if (fluidId == null) {
            return false;
        }
        final Fluid selected = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (selected == null || IcfPelletItem.fuelForFluid(selected) == null) {
            return false;
        }

        if (Objects.equals(this.selectedFuelFluids[tankIndex], fluidId)) {
            return false;
        }
        this.selectedFuelFluids[tankIndex] = fluidId;
        return true;
    }

    private boolean tryLoadMuonCatalyst() {
        if (this.muonCharge > 0) {
            return false;
        }

        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack muonStack = handler.getStackInSlot(SLOT_MUON_INPUT);
        if (muonStack.isEmpty() || muonStack.getItem() != HbmItems.PARTICLE_MUON.get()) {
            return false;
        }

        final ItemStack container = muonStack.getCraftingRemainingItem();
        if (!this.canStoreMuonContainer(container)) {
            return false;
        }

        this.consumeSlot(SLOT_MUON_INPUT, 1);
        this.storeMuonContainer(container);
        this.muonCharge = MAX_MUON;
        return true;
    }

    private boolean canStoreMuonContainer(final ItemStack container) {
        if (container.isEmpty()) {
            return true;
        }
        final ItemStack existing = this.getInternalItemHandler().getStackInSlot(SLOT_MUON_OUTPUT);
        if (existing.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameTags(existing, container) && existing.getCount() < existing.getMaxStackSize();
    }

    private void storeMuonContainer(final ItemStack container) {
        if (container.isEmpty()) {
            return;
        }
        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack existing = handler.getStackInSlot(SLOT_MUON_OUTPUT);
        if (existing.isEmpty()) {
            handler.setStackInSlot(SLOT_MUON_OUTPUT, container.copy());
            return;
        }
        final ItemStack grown = existing.copy();
        grown.grow(1);
        handler.setStackInSlot(SLOT_MUON_OUTPUT, grown);
    }

    private boolean tryFillPellet() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack pelletEmpty = handler.getStackInSlot(SLOT_PELLET_EMPTY);
        final ItemStack pelletFilled = handler.getStackInSlot(SLOT_PELLET_FILLED);

        if (pelletEmpty.isEmpty() || pelletEmpty.getItem() != HbmItems.ICF_PELLET_EMPTY.get() || !pelletFilled.isEmpty()) {
            return false;
        }

        IcfPelletItem.ensureFuelMappings();
        final boolean[] usedFluid = new boolean[]{false, false};
        final EnumICFFuel fuelA = this.resolveFuel(this.getFluidTank(TANK_FUEL_A), handler.getStackInSlot(SLOT_SOLID_FUEL_A), TANK_FUEL_A, usedFluid);
        final EnumICFFuel fuelB = this.resolveFuel(this.getFluidTank(TANK_FUEL_B), handler.getStackInSlot(SLOT_SOLID_FUEL_B), TANK_FUEL_B, usedFluid);
        if (fuelA == null || fuelB == null || fuelA == fuelB) {
            return false;
        }

        handler.setStackInSlot(SLOT_PELLET_FILLED, IcfPelletItem.setup(fuelA, fuelB, this.muonCharge > 0));
        this.consumeSlot(SLOT_PELLET_EMPTY, 1);

        if (this.muonCharge > 0) {
            this.muonCharge--;
        }

        if (usedFluid[TANK_FUEL_A]) {
            final HbmFluidTank tank = this.getFluidTank(TANK_FUEL_A);
            if (tank != null) {
                tank.drain(FUEL_FLUID_CONSUMPTION, IFluidHandler.FluidAction.EXECUTE);
            }
        } else {
            this.consumeSlot(SLOT_SOLID_FUEL_A, 1);
        }

        if (usedFluid[TANK_FUEL_B]) {
            final HbmFluidTank tank = this.getFluidTank(TANK_FUEL_B);
            if (tank != null) {
                tank.drain(FUEL_FLUID_CONSUMPTION, IFluidHandler.FluidAction.EXECUTE);
            }
        } else {
            this.consumeSlot(SLOT_SOLID_FUEL_B, 1);
        }

        return true;
    }

    private @Nullable EnumICFFuel resolveFuel(final @Nullable HbmFluidTank tank,
                                              final ItemStack solidFuel,
                                              final int tankIndex,
                                              final boolean[] usedFluid) {
        usedFluid[tankIndex] = false;

        if (tank != null && tank.getFluidAmount() >= FUEL_FLUID_CONSUMPTION) {
            final EnumICFFuel fluidFuel = IcfPelletItem.fuelForFluid(tank.getFluid());
            if (fluidFuel != null) {
                usedFluid[tankIndex] = true;
                return fluidFuel;
            }
        }

        return IcfPelletItem.fuelForMaterial(solidFuel);
    }

    private boolean isAcceptedFuelFluid(final int tankIndex, final FluidStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        if (IcfPelletItem.fuelForFluid(stack) == null) {
            return false;
        }

        final ResourceLocation selectedFluidId = this.selectedFuelFluids[tankIndex];
        if (selectedFluidId == null) {
            return true;
        }
        final Fluid selectedFluid = ForgeRegistries.FLUIDS.getValue(selectedFluidId);
        return selectedFluid != null && stack.getFluid().isSame(selectedFluid);
    }

    private void consumeSlot(final int slot, final int amount) {
        if (amount <= 0) {
            return;
        }
        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack stack = handler.getStackInSlot(slot);
        if (stack.isEmpty()) {
            return;
        }
        final ItemStack remaining = stack.copy();
        remaining.shrink(amount);
        handler.setStackInSlot(slot, remaining.isEmpty() ? ItemStack.EMPTY : remaining);
    }

    public boolean isValidSolidFuelStack(final ItemStack stack) {
        return IcfPelletItem.isMaterialFuel(stack);
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return switch (slot) {
            case SLOT_PELLET_EMPTY -> stack.getItem() == HbmItems.ICF_PELLET_EMPTY.get();
            case SLOT_MUON_INPUT -> stack.getItem() == HbmItems.PARTICLE_MUON.get();
            case SLOT_SOLID_FUEL_A, SLOT_SOLID_FUEL_B -> this.isValidSolidFuelStack(stack);
            case SLOT_FLUID_ID_A, SLOT_FLUID_ID_B -> stack.getItem() instanceof IItemFluidIdentifier;
            default -> false;
        };
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        if (!this.isItemValid(slot, stack)) {
            return false;
        }

        if (slot == SLOT_SOLID_FUEL_A) {
            return side == null || side == Direction.UP || side == Direction.DOWN;
        }
        if (slot == SLOT_SOLID_FUEL_B) {
            return side != Direction.UP && side != Direction.DOWN;
        }
        return true;
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return slot == SLOT_PELLET_FILLED || slot == SLOT_MUON_OUTPUT;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        if (side == null || side == Direction.UP || side == Direction.DOWN) {
            return TOP_BOTTOM_SLOTS;
        }
        return SIDE_SLOTS;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_ICF_PRESS.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new IcfPressMenu(containerId, inventory, this);
    }

    public int getMuonCharge() {
        return this.muonCharge;
    }

    public int getTankAmount(final int tankIndex) {
        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        return tank == null ? 0 : tank.getFluidAmount();
    }

    public int getTankCapacity(final int tankIndex) {
        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        return tank == null ? 0 : tank.getCapacity();
    }

    public String getTankFluidName(final int tankIndex) {
        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        if (tank == null || tank.isEmpty()) {
            return "";
        }
        return tank.getFluid().getDisplayName().getString();
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("muon", this.muonCharge);
        if (this.selectedFuelFluids[TANK_FUEL_A] != null) {
            tag.putString("selectedFuelA", this.selectedFuelFluids[TANK_FUEL_A].toString());
        }
        if (this.selectedFuelFluids[TANK_FUEL_B] != null) {
            tag.putString("selectedFuelB", this.selectedFuelFluids[TANK_FUEL_B].toString());
        }
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.muonCharge = Math.max(0, Math.min(MAX_MUON, tag.getInt("muon")));
        this.selectedFuelFluids[TANK_FUEL_A] = readFluidId(tag, "selectedFuelA", defaultFuelSelection(HbmFluids.DEUTERIUM));
        this.selectedFuelFluids[TANK_FUEL_B] = readFluidId(tag, "selectedFuelB", defaultFuelSelection(HbmFluids.TRITIUM));
    }

    private static @Nullable ResourceLocation readFluidId(final CompoundTag tag,
                                                          final String key,
                                                          final @Nullable ResourceLocation fallback) {
        if (!tag.contains(key)) {
            return fallback;
        }
        final ResourceLocation id = ResourceLocation.tryParse(tag.getString(key));
        return id == null ? fallback : id;
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("muon", this.muonCharge);
        tag.putInt("maxMuon", MAX_MUON);
        for (int tank = TANK_FUEL_A; tank <= TANK_FUEL_B; tank++) {
            tag.putInt("fluid" + tank + "Amount", this.getTankAmount(tank));
            tag.putInt("fluid" + tank + "Capacity", this.getTankCapacity(tank));
            tag.putString("fluid" + tank + "Name", this.getTankFluidName(tank));
        }
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.muonCharge = Math.max(0, Math.min(MAX_MUON, tag.getInt("muon")));
    }

    @Override
    public Map<com.hbm.ntm.common.item.MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of();
    }
}

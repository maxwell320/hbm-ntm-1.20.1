package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.config.CyclotronMachineConfig;
import com.hbm.ntm.common.cyclotron.HbmCyclotronRecipes;
import com.hbm.ntm.common.cyclotron.HbmCyclotronRecipes.CyclotronRecipe;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.fluid.HbmFluidTank;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.menu.CyclotronMenu;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmSoundEvents;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class CyclotronBlockEntity extends MachineBlockEntity {
    public static final int SLOT_PART_1 = 0;
    public static final int SLOT_PART_2 = 1;
    public static final int SLOT_PART_3 = 2;
    public static final int SLOT_TARGET_1 = 3;
    public static final int SLOT_TARGET_2 = 4;
    public static final int SLOT_TARGET_3 = 5;
    public static final int SLOT_OUTPUT_1 = 6;
    public static final int SLOT_OUTPUT_2 = 7;
    public static final int SLOT_OUTPUT_3 = 8;
    public static final int SLOT_BATTERY = 9;
    public static final int SLOT_UPGRADE_1 = 10;
    public static final int SLOT_UPGRADE_2 = 11;
    public static final int SLOT_COUNT = 12;

    public static final int TANK_WATER = 0;
    public static final int TANK_SPENT_STEAM = 1;
    public static final int TANK_ANTIMATTER = 2;

    private static final int[] SLOT_ACCESS = new int[]{
        SLOT_PART_1,
        SLOT_PART_2,
        SLOT_PART_3,
        SLOT_TARGET_1,
        SLOT_TARGET_2,
        SLOT_TARGET_3,
        SLOT_OUTPUT_1,
        SLOT_OUTPUT_2,
        SLOT_OUTPUT_3
    };
    private static final int OPERATE_SOUND_INTERVAL = 40;

    private int progress;
    private int lastConsumption;
    private int lastCoolantUse;
    private int operateSoundTimer;
    private boolean hasRecipe;
    private boolean canProcess;

    public CyclotronBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_CYCLOTRON.get(), pos, state, SLOT_COUNT);
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        final int capacity = Math.max(1, CyclotronMachineConfig.INSTANCE.maxPower());
        return this.createSimpleEnergyStorage(capacity, capacity, 0);
    }

    @Override
    protected HbmFluidTank[] createFluidTanks() {
        return new HbmFluidTank[]{
            this.createFluidTank(Math.max(1, CyclotronMachineConfig.INSTANCE.waterTankCapacity()), this::isAcceptedWater),
            this.createFluidTank(Math.max(1, CyclotronMachineConfig.INSTANCE.steamTankCapacity()), this::isAcceptedSpentSteam),
            this.createFluidTank(Math.max(1, CyclotronMachineConfig.INSTANCE.antimatterTankCapacity()), this::isAcceptedAntimatter)
        };
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.RECEIVE;
    }

    @Override
    protected boolean canFillFromSide(final Direction side) {
        return true;
    }

    @Override
    protected boolean canDrainFromSide(final Direction side) {
        return false;
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final CyclotronBlockEntity cyclotron) {
        boolean dirty = false;

        if (cyclotron.tryChargeFromBattery()) {
            dirty = true;
        }

        final int speed = Math.max(1, 1 + cyclotron.countUpgrades(MachineUpgradeItem.UpgradeType.SPEED));
        final int powerLevel = Math.max(0, cyclotron.countUpgrades(MachineUpgradeItem.UpgradeType.POWER));
        final int effectLevel = Math.max(0, cyclotron.countUpgrades(MachineUpgradeItem.UpgradeType.EFFECT));

        cyclotron.lastConsumption = Math.max(1,
            CyclotronMachineConfig.INSTANCE.basePowerPerTick() - CyclotronMachineConfig.INSTANCE.powerReductionPerLevel() * powerLevel);
        cyclotron.lastCoolantUse = Math.max(1,
            CyclotronMachineConfig.INSTANCE.baseCoolantUsePerTick() / (effectLevel + 1) * speed);

        final HbmFluidTank waterTank = cyclotron.getFluidTank(TANK_WATER);
        final HbmFluidTank steamTank = cyclotron.getFluidTank(TANK_SPENT_STEAM);
        final boolean hasPower = cyclotron.getStoredEnergy() >= cyclotron.lastConsumption;
        final boolean hasCoolant = waterTank != null && waterTank.getFluidAmount() >= cyclotron.lastCoolantUse;
        final boolean hasSteamSpace = steamTank != null && steamTank.getFluidAmount() + cyclotron.lastCoolantUse <= steamTank.getCapacity();

        cyclotron.hasRecipe = cyclotron.hasAnyRecipe();
        final boolean laneProcessable = cyclotron.hasProcessableLane();
        cyclotron.canProcess = laneProcessable && hasPower && hasCoolant && hasSteamSpace;

        if (cyclotron.canProcess) {
            cyclotron.progress += speed;
            cyclotron.consumeEnergy(cyclotron.lastConsumption);

            if (waterTank != null) {
                waterTank.drain(cyclotron.lastCoolantUse, IFluidHandler.FluidAction.EXECUTE);
            }
            if (steamTank != null) {
                steamTank.fill(new FluidStack(HbmFluids.SPENTSTEAM.getStillFluid(), cyclotron.lastCoolantUse), IFluidHandler.FluidAction.EXECUTE);
            }

            cyclotron.tickOperateSound(level, pos);
            dirty = true;

            if (cyclotron.progress >= cyclotron.getProcessDuration()) {
                cyclotron.progress = 0;
                if (cyclotron.processLanes()) {
                    dirty = true;
                }
            }
        } else if (cyclotron.progress > 0) {
            cyclotron.progress = 0;
            cyclotron.operateSoundTimer = 0;
            dirty = true;
        } else {
            cyclotron.operateSoundTimer = 0;
        }

        final int transferInterval = Math.max(1, CyclotronMachineConfig.INSTANCE.transferIntervalTicks());
        if (level.getGameTime() % transferInterval == 0 && cyclotron.transferOutputFluids()) {
            dirty = true;
        }

        if (dirty) {
            cyclotron.markChangedAndSync();
        }
        cyclotron.tickMachineStateSync();
    }

    private boolean hasAnyRecipe() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        for (int lane = 0; lane < 3; lane++) {
            final int partSlot = SLOT_PART_1 + lane;
            final int targetSlot = SLOT_TARGET_1 + lane;
            if (HbmCyclotronRecipes.findRecipe(handler.getStackInSlot(targetSlot), handler.getStackInSlot(partSlot)).isPresent()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasProcessableLane() {
        final ItemStackHandler handler = this.getInternalItemHandler();
        for (int lane = 0; lane < 3; lane++) {
            final int partSlot = SLOT_PART_1 + lane;
            final int targetSlot = SLOT_TARGET_1 + lane;
            final int outputSlot = SLOT_OUTPUT_1 + lane;
            final Optional<CyclotronRecipe> recipe = HbmCyclotronRecipes.findRecipe(handler.getStackInSlot(targetSlot), handler.getStackInSlot(partSlot));
            if (recipe.isEmpty()) {
                continue;
            }
            if (this.canAcceptOutput(outputSlot, recipe.get().outputCopy())) {
                return true;
            }
        }
        return false;
    }

    private boolean processLanes() {
        boolean processedAny = false;
        final ItemStackHandler handler = this.getInternalItemHandler();
        final HbmFluidTank antimatterTank = this.getFluidTank(TANK_ANTIMATTER);

        for (int lane = 0; lane < 3; lane++) {
            final int partSlot = SLOT_PART_1 + lane;
            final int targetSlot = SLOT_TARGET_1 + lane;
            final int outputSlot = SLOT_OUTPUT_1 + lane;
            final Optional<CyclotronRecipe> recipe = HbmCyclotronRecipes.findRecipe(handler.getStackInSlot(targetSlot), handler.getStackInSlot(partSlot));
            if (recipe.isEmpty()) {
                continue;
            }

            final ItemStack output = recipe.get().outputCopy();
            if (!this.canAcceptOutput(outputSlot, output)) {
                continue;
            }

            final ItemStack part = handler.getStackInSlot(partSlot).copy();
            final ItemStack target = handler.getStackInSlot(targetSlot).copy();
            part.shrink(1);
            target.shrink(1);
            handler.setStackInSlot(partSlot, part.isEmpty() ? ItemStack.EMPTY : part);
            handler.setStackInSlot(targetSlot, target.isEmpty() ? ItemStack.EMPTY : target);

            this.insertOutput(outputSlot, output);

            if (antimatterTank != null && recipe.get().antimatterYield() > 0) {
                antimatterTank.fill(new FluidStack(HbmFluids.AMAT.getStillFluid(), recipe.get().antimatterYield()), IFluidHandler.FluidAction.EXECUTE);
            }

            processedAny = true;
        }

        return processedAny;
    }

    private boolean canAcceptOutput(final int outputSlot, final ItemStack output) {
        if (output.isEmpty()) {
            return false;
        }
        final ItemStack existing = this.getInternalItemHandler().getStackInSlot(outputSlot);
        if (existing.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameTags(existing, output)) {
            return false;
        }
        return existing.getCount() + output.getCount() <= existing.getMaxStackSize();
    }

    private void insertOutput(final int outputSlot, final ItemStack output) {
        final ItemStackHandler handler = this.getInternalItemHandler();
        final ItemStack existing = handler.getStackInSlot(outputSlot);
        if (existing.isEmpty()) {
            handler.setStackInSlot(outputSlot, output.copy());
            return;
        }
        final ItemStack grown = existing.copy();
        grown.grow(output.getCount());
        handler.setStackInSlot(outputSlot, grown);
    }

    private boolean transferOutputFluids() {
        boolean moved = false;
        moved |= this.transferTankToNeighbors(TANK_SPENT_STEAM);
        moved |= this.transferTankToNeighbors(TANK_ANTIMATTER);
        return moved;
    }

    private boolean transferTankToNeighbors(final int tankIndex) {
        if (this.level == null) {
            return false;
        }

        final HbmFluidTank tank = this.getFluidTank(tankIndex);
        if (tank == null || tank.isEmpty()) {
            return false;
        }

        final int maxTransfer = Math.max(1, CyclotronMachineConfig.INSTANCE.fluidTransferPerTick());
        boolean moved = false;

        for (final Direction direction : Direction.values()) {
            if (tank.isEmpty()) {
                break;
            }

            final BlockEntity neighbor = this.level.getBlockEntity(this.worldPosition.relative(direction));
            if (neighbor == null) {
                continue;
            }

            final IFluidHandler neighborHandler = neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).orElse(null);
            if (neighborHandler == null) {
                continue;
            }

            final FluidStack stored = tank.getFluid();
            if (stored.isEmpty()) {
                continue;
            }

            final int offerAmount = Math.min(maxTransfer, stored.getAmount());
            final FluidStack offer = new FluidStack(stored, offerAmount);
            final int accepted = neighborHandler.fill(offer, IFluidHandler.FluidAction.EXECUTE);
            if (accepted <= 0) {
                continue;
            }

            tank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
            moved = true;
        }

        return moved;
    }

    private boolean tryChargeFromBattery() {
        final ItemStack battery = this.getInternalItemHandler().getStackInSlot(SLOT_BATTERY);
        if (battery.isEmpty() || !(battery.getItem() instanceof final BatteryItem batteryItem)) {
            return false;
        }

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
        final IEnergyStorage storage = this.getEnergyStorage(null);
        if (storage == null) {
            return false;
        }
        storage.receiveEnergy(toDischarge, false);
        return true;
    }

    private void consumeEnergy(final int amount) {
        final IEnergyStorage storage = this.getEnergyStorage(null);
        if (storage == null || amount <= 0) {
            return;
        }
        storage.extractEnergy(Math.min(amount, storage.getEnergyStored()), false);
    }

    private void tickOperateSound(final Level level, final BlockPos pos) {
        if (this.operateSoundTimer <= 0) {
            level.playSound(null, pos, HbmSoundEvents.BLOCK_CENTRIFUGE_OPERATE.get(), SoundSource.BLOCKS, this.getVolume(1.0F), 1.0F);
            this.operateSoundTimer = OPERATE_SOUND_INTERVAL;
        } else {
            this.operateSoundTimer--;
        }
    }

    private boolean isAcceptedWater(final FluidStack stack) {
        return stack.isEmpty() || stack.getFluid().isSame(Fluids.WATER);
    }

    private boolean isAcceptedSpentSteam(final FluidStack stack) {
        return stack.isEmpty() || stack.getFluid().isSame(HbmFluids.SPENTSTEAM.getStillFluid());
    }

    private boolean isAcceptedAntimatter(final FluidStack stack) {
        return stack.isEmpty() || stack.getFluid().isSame(HbmFluids.AMAT.getStillFluid());
    }

    @Override
    public boolean isItemValid(final int slot, final @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (slot >= SLOT_PART_1 && slot <= SLOT_PART_3) {
            return HbmCyclotronRecipes.isCyclotronPart(stack);
        }
        if (slot >= SLOT_TARGET_1 && slot <= SLOT_TARGET_3) {
            return HbmCyclotronRecipes.hasTargetRecipe(stack);
        }
        if (slot == SLOT_BATTERY) {
            return stack.getItem() instanceof BatteryItem;
        }
        if (slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2) {
            if (!(stack.getItem() instanceof MachineUpgradeItem upgrade)) {
                return false;
            }
            return upgrade.type() == MachineUpgradeItem.UpgradeType.SPEED
                || upgrade.type() == MachineUpgradeItem.UpgradeType.POWER
                || upgrade.type() == MachineUpgradeItem.UpgradeType.EFFECT;
        }

        return false;
    }

    @Override
    public boolean canInsertIntoSlot(final int slot, final @NotNull ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return slot >= SLOT_OUTPUT_1 && slot <= SLOT_OUTPUT_3;
    }

    @Override
    public int[] getAccessibleSlots(final @Nullable Direction side) {
        return SLOT_ACCESS;
    }

    @Override
    protected boolean isUpgradeSlot(final int slot) {
        return slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_CYCLOTRON.get().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return new CyclotronMenu(containerId, inventory, this);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getProcessDuration() {
        return Math.max(1, CyclotronMachineConfig.INSTANCE.processDuration());
    }

    public int getLastConsumption() {
        return this.lastConsumption;
    }

    public int getLastCoolantUse() {
        return this.lastCoolantUse;
    }

    public boolean hasRecipe() {
        return this.hasRecipe;
    }

    public boolean canProcessRecipe() {
        return this.canProcess;
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("consumption", this.lastConsumption);
        tag.putInt("coolantUse", this.lastCoolantUse);
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.lastConsumption = Math.max(0, tag.getInt("consumption"));
        this.lastCoolantUse = Math.max(0, tag.getInt("coolantUse"));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag.putInt("processDuration", this.getProcessDuration());
        tag.putInt("energy", this.getStoredEnergy());
        tag.putInt("maxEnergy", this.getMaxStoredEnergy());
        tag.putInt("consumption", this.lastConsumption);
        tag.putInt("coolantUse", this.lastCoolantUse);
        tag.putBoolean("hasRecipe", this.hasRecipe);
        tag.putBoolean("canProcess", this.canProcess);

        for (int tank = 0; tank < 3; tank++) {
            final HbmFluidTank fluidTank = this.getFluidTank(tank);
            final int amount = fluidTank == null ? 0 : fluidTank.getFluidAmount();
            final int capacity = fluidTank == null ? 0 : fluidTank.getCapacity();
            final String name = fluidTank == null || fluidTank.isEmpty() ? "Empty" : fluidTank.getFluid().getDisplayName().getString();
            tag.putInt("fluid" + tank + "Amount", amount);
            tag.putInt("fluid" + tank + "Capacity", capacity);
            tag.putString("fluid" + tank + "Name", name);
        }
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.progress = Math.max(0, tag.getInt("progress"));
        this.lastConsumption = Math.max(0, tag.getInt("consumption"));
        this.lastCoolantUse = Math.max(0, tag.getInt("coolantUse"));
        this.hasRecipe = tag.getBoolean("hasRecipe");
        this.canProcess = tag.getBoolean("canProcess");
    }

    @Override
    public Map<MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of(
            MachineUpgradeItem.UpgradeType.SPEED, 6,
            MachineUpgradeItem.UpgradeType.POWER, 6,
            MachineUpgradeItem.UpgradeType.EFFECT, 6
        );
    }
}

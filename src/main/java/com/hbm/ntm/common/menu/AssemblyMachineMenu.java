package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.AssemblyMachineBlockEntity;
import com.hbm.ntm.common.item.BlueprintItem;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("null")
public class AssemblyMachineMenu extends MachineMenuBase<AssemblyMachineBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_ENERGY_LO = 1;
    private static final int DATA_ENERGY_HI = 2;
    private static final int DATA_MAX_ENERGY_LO = 3;
    private static final int DATA_MAX_ENERGY_HI = 4;
    private static final int DATA_COUNT = 5;

    private final ContainerData data;
    private int clientProgress;
    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientRecipeIndex = -1;
    private int clientRecipeCount;
    private int clientInputAmount;
    private int clientInputCapacity;
    private String clientInputFluid = "";
    private int clientOutputAmount;
    private int clientOutputCapacity;
    private String clientOutputFluid = "";
    private boolean clientHasRecipe;
    private boolean clientHasPower;
    private boolean clientCanProcess;

    public AssemblyMachineMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final AssemblyMachineBlockEntity machine ? machine : null);
    }

    public AssemblyMachineMenu(final int containerId, final Inventory inventory, final AssemblyMachineBlockEntity machine) {
        super(HbmMenuTypes.MACHINE_ASSEMBLY_MACHINE.get(), containerId, inventory, machine, AssemblyMachineBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = machine == null ? new ItemStackHandler(AssemblyMachineBlockEntity.SLOT_COUNT) : machine.getInternalItemHandler();
        this.addSlot(new FilteredSlotItemHandler(handler, AssemblyMachineBlockEntity.SLOT_BATTERY, 152, 81,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addSlot(new FilteredSlotItemHandler(handler, AssemblyMachineBlockEntity.SLOT_BLUEPRINT, 35, 126,
            (slot, stack) -> stack.getItem() instanceof BlueprintItem));
        this.addUpgradeSlot(handler, AssemblyMachineBlockEntity.SLOT_UPGRADE_1, 152, 108);
        this.addUpgradeSlot(handler, AssemblyMachineBlockEntity.SLOT_UPGRADE_2, 152, 126);
        this.addFilteredGridSlots(handler, AssemblyMachineBlockEntity.SLOT_INPUT_START, 8, 18, 4, 3,
            (slot, stack) -> machine == null || machine.isItemValid(slot, stack));
        this.addSlot(new OutputSlotItemHandler(handler, AssemblyMachineBlockEntity.SLOT_OUTPUT, 98, 45));
        this.addPlayerInventory(inventory, 8, 174);
        this.data = machine == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    machine::getProgress,
                    MachineDataSlots.lowWord(machine::getStoredEnergy),
                    MachineDataSlots.highWord(machine::getStoredEnergy),
                    MachineDataSlots.lowWord(machine::getMaxStoredEnergy),
                    MachineDataSlots.highWord(machine::getMaxStoredEnergy)
                ),
                List.of(
                    this::setClientProgress,
                    value -> this.clientEnergy = MachineDataSlots.withLowWord(this.clientEnergy, value),
                    value -> this.clientEnergy = MachineDataSlots.withHighWord(this.clientEnergy, value),
                    value -> this.clientMaxEnergy = MachineDataSlots.withLowWord(this.clientMaxEnergy, value),
                    value -> this.clientMaxEnergy = MachineDataSlots.withHighWord(this.clientMaxEnergy, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, AssemblyMachineBlockEntity.SLOT_BATTERY, AssemblyMachineBlockEntity.SLOT_BATTERY + 1, false);
        }
        if (stack.getItem() instanceof BlueprintItem) {
            return this.moveItemStackTo(stack, AssemblyMachineBlockEntity.SLOT_BLUEPRINT, AssemblyMachineBlockEntity.SLOT_BLUEPRINT + 1, false);
        }
        if (this.isUpgradeItem(stack)) {
            return this.moveToMachineRange(stack, AssemblyMachineBlockEntity.SLOT_UPGRADE_1, AssemblyMachineBlockEntity.SLOT_UPGRADE_2 + 1);
        }
        return this.moveToMachineRange(stack, AssemblyMachineBlockEntity.SLOT_INPUT_START, AssemblyMachineBlockEntity.SLOT_INPUT_END);
    }

    public int progress() {
        return this.clientProgress;
    }

    public int energy() {
        return this.clientEnergy;
    }

    public int maxEnergy() {
        return Math.max(0, this.clientMaxEnergy);
    }

    public int recipeIndex() {
        return this.clientRecipeIndex;
    }

    public int recipeCount() {
        return this.clientRecipeCount;
    }

    public int inputAmount() {
        return this.clientInputAmount;
    }

    public int inputCapacity() {
        return this.clientInputCapacity;
    }

    public String inputFluid() {
        return this.clientInputFluid;
    }

    public int outputAmount() {
        return this.clientOutputAmount;
    }

    public int outputCapacity() {
        return this.clientOutputCapacity;
    }

    public String outputFluid() {
        return this.clientOutputFluid;
    }

    public boolean hasRecipe() {
        return this.clientHasRecipe;
    }

    public boolean hasPower() {
        return this.clientHasPower;
    }

    public boolean canProcess() {
        return this.clientCanProcess;
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(0, data.getInt("maxEnergy"));
        this.clientRecipeIndex = data.getInt("selectedRecipeIndex");
        this.clientRecipeCount = Math.max(0, data.getInt("visibleRecipeCount"));
        this.clientInputAmount = Math.max(0, data.getInt("inputAmount"));
        this.clientInputCapacity = Math.max(0, data.getInt("inputCapacity"));
        this.clientInputFluid = data.getString("inputFluid");
        this.clientOutputAmount = Math.max(0, data.getInt("outputAmount"));
        this.clientOutputCapacity = Math.max(0, data.getInt("outputCapacity"));
        this.clientOutputFluid = data.getString("outputFluid");
        this.clientHasRecipe = data.getBoolean("hasRecipe");
        this.clientHasPower = data.getBoolean("hasPower");
        this.clientCanProcess = data.getBoolean("canProcess");
    }

    private void setClientProgress(final int value) {
        this.clientProgress = value;
    }
}

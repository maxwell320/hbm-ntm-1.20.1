package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.PurexBlockEntity;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.BlueprintItem;
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
public class PurexMenu extends MachineMenuBase<PurexBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_PROCESS_TIME = 1;
    private static final int DATA_ENERGY = 2;
    private static final int DATA_MAX_ENERGY = 3;
    private static final int DATA_CONSUMPTION = 4;
    private static final int DATA_COUNT = 5;

    private final ContainerData data;
    private int clientProgress;
    private int clientProcessTime = 1;
    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientConsumption;
    private boolean clientHasRecipe;
    private boolean clientCanProcess;
    private boolean clientHasPower;
    private String clientRecipeId = "";
    private String clientSelectedRecipeId = "";
    private int clientRecipeIndex = -1;
    private int clientRecipeCount;
    private final int[] fluidAmounts = new int[4];
    private final int[] fluidCapacities = new int[4];
    private final String[] fluidNames = new String[]{"", "", "", ""};

    public PurexMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final PurexBlockEntity purex ? purex : null);
    }

    public PurexMenu(final int containerId, final Inventory inventory, final PurexBlockEntity purex) {
        super(HbmMenuTypes.MACHINE_PUREX.get(), containerId, inventory, purex, PurexBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = purex == null ? new ItemStackHandler(PurexBlockEntity.SLOT_COUNT) : purex.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, PurexBlockEntity.SLOT_BATTERY, 152, 81,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addSlot(new FilteredSlotItemHandler(handler, PurexBlockEntity.SLOT_BLUEPRINT, 35, 126,
            (slot, stack) -> stack.getItem() instanceof BlueprintItem));
        this.addUpgradeSlot(handler, PurexBlockEntity.SLOT_UPGRADE_1, 152, 108);
        this.addUpgradeSlot(handler, PurexBlockEntity.SLOT_UPGRADE_2, 170, 108);

        this.addSlot(new FilteredSlotItemHandler(handler, PurexBlockEntity.SLOT_INPUT_1, 8, 90,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, PurexBlockEntity.SLOT_INPUT_2, 8, 108,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, PurexBlockEntity.SLOT_INPUT_3, 8, 126,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));

        this.addSlot(new OutputSlotItemHandler(handler, PurexBlockEntity.SLOT_OUTPUT_1, 80, 36));
        this.addSlot(new OutputSlotItemHandler(handler, PurexBlockEntity.SLOT_OUTPUT_2, 98, 36));
        this.addSlot(new OutputSlotItemHandler(handler, PurexBlockEntity.SLOT_OUTPUT_3, 116, 36));
        this.addSlot(new OutputSlotItemHandler(handler, PurexBlockEntity.SLOT_OUTPUT_4, 80, 54));
        this.addSlot(new OutputSlotItemHandler(handler, PurexBlockEntity.SLOT_OUTPUT_5, 98, 54));
        this.addSlot(new OutputSlotItemHandler(handler, PurexBlockEntity.SLOT_OUTPUT_6, 116, 54));

        this.addPlayerInventory(inventory, 8, 174);

        this.data = purex == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    purex::getProgress,
                    purex::getProcessTime,
                    purex::getStoredEnergy,
                    purex::getDisplayMaxPower,
                    purex::getLastConsumption
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientProcessTime = Math.max(1, value),
                    value -> this.clientEnergy = Math.max(0, value),
                    value -> this.clientMaxEnergy = Math.max(1, value),
                    value -> this.clientConsumption = Math.max(0, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, PurexBlockEntity.SLOT_BATTERY, PurexBlockEntity.SLOT_BATTERY + 1, false);
        }

        if (stack.getItem() instanceof BlueprintItem) {
            return this.moveItemStackTo(stack, PurexBlockEntity.SLOT_BLUEPRINT, PurexBlockEntity.SLOT_BLUEPRINT + 1, false);
        }

        if (this.isUpgradeItem(stack)) {
            return this.moveToMachineRange(stack, PurexBlockEntity.SLOT_UPGRADE_1, PurexBlockEntity.SLOT_UPGRADE_2 + 1);
        }

        if (this.machine != null) {
            for (int slot = PurexBlockEntity.SLOT_INPUT_1; slot <= PurexBlockEntity.SLOT_INPUT_3; slot++) {
                if (this.machine.isItemValid(slot, stack) && this.moveItemStackTo(stack, slot, slot + 1, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int processTime() {
        if (this.clientProcessTime > 0) {
            return this.clientProcessTime;
        }
        return Math.max(1, this.data.get(DATA_PROCESS_TIME));
    }

    public int energy() {
        return this.clientEnergy > 0 ? this.clientEnergy : this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        return this.clientMaxEnergy > 0 ? this.clientMaxEnergy : this.data.get(DATA_MAX_ENERGY);
    }

    public int consumption() {
        return Math.max(0, this.clientConsumption > 0 ? this.clientConsumption : this.data.get(DATA_CONSUMPTION));
    }

    public boolean hasRecipe() {
        return this.clientHasRecipe;
    }

    public boolean canProcess() {
        return this.clientCanProcess;
    }

    public boolean hasPower() {
        return this.clientHasPower;
    }

    public String recipeId() {
        return this.clientSelectedRecipeId.isBlank() ? this.clientRecipeId : this.clientSelectedRecipeId;
    }

    public int recipeIndex() {
        return this.clientRecipeIndex;
    }

    public int recipeCount() {
        return this.clientRecipeCount;
    }

    public int fluidAmount(final int tank) {
        return tank < 0 || tank >= this.fluidAmounts.length ? 0 : this.fluidAmounts[tank];
    }

    public int fluidCapacity(final int tank) {
        return tank < 0 || tank >= this.fluidCapacities.length ? 0 : this.fluidCapacities[tank];
    }

    public String fluidName(final int tank) {
        return tank < 0 || tank >= this.fluidNames.length ? "" : this.fluidNames[tank];
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientProcessTime = Math.max(1, data.getInt("processTime"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(1, data.getInt("maxEnergy"));
        this.clientConsumption = Math.max(0, data.getInt("consumption"));
        this.clientHasRecipe = data.getBoolean("hasRecipe");
        this.clientCanProcess = data.getBoolean("canProcess");
        this.clientHasPower = data.getBoolean("hasPower");
        this.clientSelectedRecipeId = data.getString("selectedRecipeId");
        this.clientRecipeIndex = data.getInt("selectedRecipeIndex");
        this.clientRecipeCount = Math.max(0, data.getInt("visibleRecipeCount"));
        this.clientRecipeId = data.getString("recipeId");

        for (int tank = 0; tank < 4; tank++) {
            this.fluidAmounts[tank] = Math.max(0, data.getInt("fluid" + tank + "Amount"));
            this.fluidCapacities[tank] = Math.max(0, data.getInt("fluid" + tank + "Capacity"));
            this.fluidNames[tank] = data.getString("fluid" + tank + "Name");
        }
    }
}

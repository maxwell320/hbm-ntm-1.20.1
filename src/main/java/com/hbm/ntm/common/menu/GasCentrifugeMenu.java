package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.GasCentrifugeBlockEntity;
import com.hbm.ntm.common.config.GasCentrifugeMachineConfig;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.item.MachineUpgradeItem;
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
public class GasCentrifugeMenu extends MachineMenuBase<GasCentrifugeBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_ENERGY = 1;
    private static final int DATA_MAX_ENERGY = 2;
    private static final int DATA_INPUT = 3;
    private static final int DATA_OUTPUT = 4;
    private static final int DATA_CAPACITY = 5;
    private static final int DATA_PROCESSING_SPEED = 6;
    private static final int DATA_POWER_PER_TICK = 7;
    private static final int DATA_COUNT = 8;

    private final ContainerData data;
    private int clientProgress;
    private int clientEnergy;
    private int clientMaxEnergy;
    private int clientInput;
    private int clientOutput;
    private int clientCapacity;
    private int clientProcessingSpeed;
    private int clientPowerPerTick;
    private String clientInputTypeKey = "hbmpseudofluid.none";
    private String clientOutputTypeKey = "hbmpseudofluid.none";
    private boolean clientInputNeedsSpeed;
    private boolean clientHasSpeedUpgrade;

    public GasCentrifugeMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final GasCentrifugeBlockEntity gas ? gas : null);
    }

    public GasCentrifugeMenu(final int containerId, final Inventory inventory, final GasCentrifugeBlockEntity gas) {
        super(HbmMenuTypes.MACHINE_GAS_CENTRIFUGE.get(), containerId, inventory, gas, GasCentrifugeBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = gas == null ? new ItemStackHandler(GasCentrifugeBlockEntity.SLOT_COUNT) : gas.getInternalItemHandler();

        this.addSlot(new OutputSlotItemHandler(handler, GasCentrifugeBlockEntity.SLOT_OUTPUT_1, 71, 53));
        this.addSlot(new OutputSlotItemHandler(handler, GasCentrifugeBlockEntity.SLOT_OUTPUT_2, 89, 53));
        this.addSlot(new OutputSlotItemHandler(handler, GasCentrifugeBlockEntity.SLOT_OUTPUT_3, 71, 71));
        this.addSlot(new OutputSlotItemHandler(handler, GasCentrifugeBlockEntity.SLOT_OUTPUT_4, 89, 71));

        this.addSlot(new FilteredSlotItemHandler(handler, GasCentrifugeBlockEntity.SLOT_BATTERY, 182, 71,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));
        this.addSlot(new FilteredSlotItemHandler(handler, GasCentrifugeBlockEntity.SLOT_FLUID_ID, 91, 15,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));
        this.addUpgradeSlot(handler, GasCentrifugeBlockEntity.SLOT_UPGRADE, 69, 15,
            MachineUpgradeItem.UpgradeType.GC_SPEED);

        this.addPlayerInventory(inventory, 8, 122);

        this.data = gas == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    gas::getProgress,
                    gas::getStoredEnergy,
                    gas::getMaxStoredEnergy,
                    gas::getPseudoInputAmount,
                    gas::getPseudoOutputAmount,
                    gas::getPseudoCapacity,
                    () -> gas.hasSpeedUpgradeInstalled()
                        ? Math.max(1, GasCentrifugeMachineConfig.INSTANCE.overclockProcessingSpeed())
                        : Math.max(1, GasCentrifugeMachineConfig.INSTANCE.processingSpeed()),
                    () -> gas.hasSpeedUpgradeInstalled()
                        ? Math.max(1, GasCentrifugeMachineConfig.INSTANCE.overclockPowerPerTick())
                        : Math.max(1, GasCentrifugeMachineConfig.INSTANCE.powerPerTick())
                ),
                List.of(
                    value -> this.clientProgress = value,
                    value -> this.clientEnergy = value,
                    value -> this.clientMaxEnergy = value,
                    value -> this.clientInput = value,
                    value -> this.clientOutput = value,
                    value -> this.clientCapacity = value,
                    value -> this.clientProcessingSpeed = Math.max(1, value),
                    value -> this.clientPowerPerTick = Math.max(1, value)
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, GasCentrifugeBlockEntity.SLOT_BATTERY, GasCentrifugeBlockEntity.SLOT_BATTERY + 1, false);
        }
        if (stack.getItem() instanceof IItemFluidIdentifier) {
            return this.moveItemStackTo(stack, GasCentrifugeBlockEntity.SLOT_FLUID_ID, GasCentrifugeBlockEntity.SLOT_FLUID_ID + 1, false);
        }
        if (this.isUpgradeItem(stack, MachineUpgradeItem.UpgradeType.GC_SPEED)) {
            return this.moveToMachineRange(stack, GasCentrifugeBlockEntity.SLOT_UPGRADE, GasCentrifugeBlockEntity.SLOT_UPGRADE + 1);
        }
        return false;
    }

    public int progress() {
        return this.clientProgress > 0 ? this.clientProgress : this.data.get(DATA_PROGRESS);
    }

    public int energy() {
        return this.clientEnergy > 0 ? this.clientEnergy : this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        return this.clientMaxEnergy > 0 ? this.clientMaxEnergy : this.data.get(DATA_MAX_ENERGY);
    }

    public int inputAmount() {
        return Math.max(0, this.clientInput > 0 ? this.clientInput : this.data.get(DATA_INPUT));
    }

    public int outputAmount() {
        return Math.max(0, this.clientOutput > 0 ? this.clientOutput : this.data.get(DATA_OUTPUT));
    }

    public int tankCapacity() {
        return Math.max(1, this.clientCapacity > 0 ? this.clientCapacity : this.data.get(DATA_CAPACITY));
    }

    public int processingSpeed() {
        return Math.max(1, this.clientProcessingSpeed > 0 ? this.clientProcessingSpeed : this.data.get(DATA_PROCESSING_SPEED));
    }

    public int powerPerTick() {
        return Math.max(1, this.clientPowerPerTick > 0 ? this.clientPowerPerTick : this.data.get(DATA_POWER_PER_TICK));
    }

    public String inputTypeKey() {
        return this.clientInputTypeKey;
    }

    public String outputTypeKey() {
        return this.clientOutputTypeKey;
    }

    public boolean inputNeedsSpeedUpgrade() {
        return this.clientInputNeedsSpeed;
    }

    public boolean hasSpeedUpgrade() {
        return this.clientHasSpeedUpgrade;
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgress = Math.max(0, data.getInt("progress"));
        this.clientEnergy = Math.max(0, data.getInt("energy"));
        this.clientMaxEnergy = Math.max(1, data.getInt("maxEnergy"));
        this.clientInput = Math.max(0, data.getInt("pseudoInputAmount"));
        this.clientOutput = Math.max(0, data.getInt("pseudoOutputAmount"));
        this.clientCapacity = Math.max(1, data.getInt("pseudoCapacity"));
        this.clientProcessingSpeed = Math.max(1, data.getInt("processingSpeed"));
        this.clientPowerPerTick = Math.max(1, data.getInt("powerPerTick"));
        this.clientInputTypeKey = data.getString("pseudoInputTypeKey");
        this.clientOutputTypeKey = data.getString("pseudoOutputTypeKey");
        this.clientInputNeedsSpeed = data.getBoolean("pseudoInputNeedsSpeed");
        this.clientHasSpeedUpgrade = data.getBoolean("hasSpeedUpgrade");
    }
}

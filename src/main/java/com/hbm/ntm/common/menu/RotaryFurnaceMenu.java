package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.RotaryFurnaceBlockEntity;
import com.hbm.ntm.common.item.IItemFluidIdentifier;
import com.hbm.ntm.common.module.ModuleBurnTime;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import com.hbm.ntm.common.rotary.HbmRotaryFurnaceRecipes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("null")
public class RotaryFurnaceMenu extends MachineMenuBase<RotaryFurnaceBlockEntity> {
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_BURN_TIME = 1;
    private static final int DATA_MAX_BURN_TIME = 2;
    private static final int DATA_BURN_HEAT = 3;
    private static final int DATA_STEAM_USED = 4;
    private static final int DATA_STEAM_AMOUNT = 5;
    private static final int DATA_STEAM_CAPACITY = 6;
    private static final int DATA_SPENT_STEAM_AMOUNT = 7;
    private static final int DATA_SPENT_STEAM_CAPACITY = 8;
    private static final int DATA_INPUT_FLUID_AMOUNT = 9;
    private static final int DATA_INPUT_FLUID_CAPACITY = 10;
    private static final int DATA_OUTPUT_AMOUNT = 11;
    private static final int DATA_MAX_OUTPUT = 12;
    private static final int DATA_COUNT = 13;
    private static final List<Component> LEGACY_BURN_BONUSES = List.copyOf(new ModuleBurnTime()
        .setCokeTimeMod(1.25)
        .setRocketTimeMod(1.5)
        .setSolidTimeMod(1.5)
        .setBalefireTimeMod(1.5)
        .setSolidHeatMod(1.5)
        .setRocketHeatMod(3.0)
        .setBalefireHeatMod(10.0)
        .getDesc());

    private final ContainerData data;
    private final List<Component> burnBonuses;

    private int clientProgressScaled;
    private int clientBurnTime;
    private int clientMaxBurnTime;
    private int clientBurnHeatScaled = 150;
    private int clientSteamUsed;
    private int clientSteamAmount;
    private int clientSteamCapacity = 1;
    private int clientSpentSteamAmount;
    private int clientSpentSteamCapacity = 1;
    private int clientInputFluidAmount;
    private int clientInputFluidCapacity = 1;
    private int clientOutputAmount;
    private int clientMaxOutput = HbmRotaryFurnaceRecipes.QUANTA_PER_INGOT;
    private int clientOutputColor = 0xFFFFFFFF;
    private String clientInputFluidName = "Empty";
    private String clientOutputMaterialName = "";
    private boolean clientHasOutputAcceptor;

    public RotaryFurnaceMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final RotaryFurnaceBlockEntity furnace ? furnace : null);
    }

    public RotaryFurnaceMenu(final int containerId, final Inventory inventory, final RotaryFurnaceBlockEntity furnace) {
        super(HbmMenuTypes.MACHINE_ROTARY_FURNACE.get(), containerId, inventory, furnace, RotaryFurnaceBlockEntity.SLOT_COUNT);

        final ItemStackHandler handler = furnace == null ? new ItemStackHandler(RotaryFurnaceBlockEntity.SLOT_COUNT) : furnace.getInternalItemHandler();

        this.addSlot(new FilteredSlotItemHandler(handler, RotaryFurnaceBlockEntity.SLOT_INPUT_1, 8, 18,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, RotaryFurnaceBlockEntity.SLOT_INPUT_2, 26, 18,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, RotaryFurnaceBlockEntity.SLOT_INPUT_3, 44, 18,
            (slot, stack) -> this.machine == null || this.machine.isItemValid(slot, stack)));
        this.addSlot(new FilteredSlotItemHandler(handler, RotaryFurnaceBlockEntity.SLOT_FLUID_ID, 8, 54,
            (slot, stack) -> stack.getItem() instanceof IItemFluidIdentifier));
        this.addSlot(new FilteredSlotItemHandler(handler, RotaryFurnaceBlockEntity.SLOT_FUEL, 44, 54,
            (slot, stack) -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0));

        this.addPlayerInventory(inventory, 8, 104);
        this.burnBonuses = LEGACY_BURN_BONUSES;

        this.data = furnace == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    () -> Math.max(0, Math.min(10_000, Math.round(furnace.getProgress() * 10_000.0F))),
                    furnace::getBurnTime,
                    furnace::getMaxBurnTime,
                    () -> Math.max(0, Math.round(furnace.getBurnHeat() * 100.0F)),
                    furnace::getSteamUsed,
                    furnace::getSteamAmount,
                    furnace::getSteamCapacity,
                    furnace::getSpentSteamAmount,
                    furnace::getSpentSteamCapacity,
                    furnace::getInputFluidAmount,
                    furnace::getInputFluidCapacity,
                    furnace::getOutputAmount,
                    furnace::getMaxOutputAmount
                ),
                List.of(
                    value -> this.clientProgressScaled = Math.max(0, value),
                    value -> this.clientBurnTime = Math.max(0, value),
                    value -> this.clientMaxBurnTime = Math.max(0, value),
                    value -> this.clientBurnHeatScaled = Math.max(0, value),
                    value -> this.clientSteamUsed = Math.max(0, value),
                    value -> this.clientSteamAmount = Math.max(0, value),
                    value -> this.clientSteamCapacity = Math.max(1, value),
                    value -> this.clientSpentSteamAmount = Math.max(0, value),
                    value -> this.clientSpentSteamCapacity = Math.max(1, value),
                    value -> this.clientInputFluidAmount = Math.max(0, value),
                    value -> this.clientInputFluidCapacity = Math.max(1, value),
                    value -> this.clientOutputAmount = Math.max(0, value),
                    value -> this.clientMaxOutput = Math.max(1, value)
                ));

        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        if (stack.getItem() instanceof IItemFluidIdentifier) {
            return this.moveItemStackTo(stack, RotaryFurnaceBlockEntity.SLOT_FLUID_ID, RotaryFurnaceBlockEntity.SLOT_FLUID_ID + 1, false);
        }

        if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0) {
            if (this.moveItemStackTo(stack, RotaryFurnaceBlockEntity.SLOT_FUEL, RotaryFurnaceBlockEntity.SLOT_FUEL + 1, false)) {
                return true;
            }
        }

        if (this.machine != null) {
            for (final int slot : new int[]{RotaryFurnaceBlockEntity.SLOT_INPUT_1, RotaryFurnaceBlockEntity.SLOT_INPUT_2, RotaryFurnaceBlockEntity.SLOT_INPUT_3}) {
                if (this.machine.isItemValid(slot, stack) && this.moveItemStackTo(stack, slot, slot + 1, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int progressScaled() {
        return this.clientProgressScaled;
    }

    public int burnTime() {
        return this.clientBurnTime;
    }

    public int maxBurnTime() {
        return Math.max(1, this.clientMaxBurnTime);
    }

    public int burnHeatScaled() {
        return this.clientBurnHeatScaled;
    }

    public int steamUsed() {
        return this.clientSteamUsed;
    }

    public int steamAmount() {
        return this.clientSteamAmount;
    }

    public int steamCapacity() {
        return Math.max(1, this.clientSteamCapacity);
    }

    public int spentSteamAmount() {
        return this.clientSpentSteamAmount;
    }

    public int spentSteamCapacity() {
        return Math.max(1, this.clientSpentSteamCapacity);
    }

    public int inputFluidAmount() {
        return this.clientInputFluidAmount;
    }

    public int inputFluidCapacity() {
        return Math.max(1, this.clientInputFluidCapacity);
    }

    public String inputFluidName() {
        return this.clientInputFluidName;
    }

    public int outputAmount() {
        return this.clientOutputAmount;
    }

    public int maxOutput() {
        return Math.max(1, this.clientMaxOutput);
    }

    public int outputColor() {
        return this.clientOutputColor;
    }

    public String outputMaterialName() {
        return this.clientOutputMaterialName;
    }

    public boolean hasOutputAcceptor() {
        return this.clientHasOutputAcceptor;
    }

    public List<Component> burnBonuses() {
        return this.burnBonuses;
    }

    public String formattedOutputAmount() {
        final int amount = Math.max(0, this.outputAmount());
        if (amount <= 0) {
            return "";
        }

        final int ingots = amount / HbmRotaryFurnaceRecipes.QUANTA_PER_INGOT;
        final int remainder = amount % HbmRotaryFurnaceRecipes.QUANTA_PER_INGOT;
        final int nuggets = remainder / 8;

        if (ingots > 0 && nuggets > 0) {
            return ingots + " ingot" + (ingots == 1 ? "" : "s") + " " + nuggets + " nugget" + (nuggets == 1 ? "" : "s");
        }
        if (ingots > 0) {
            return ingots + " ingot" + (ingots == 1 ? "" : "s");
        }
        return nuggets + " nugget" + (nuggets == 1 ? "" : "s");
    }

    @Override
    protected void readMachineStateSync(final CompoundTag data) {
        this.clientProgressScaled = Math.max(0, data.getInt("progressScaled"));
        this.clientBurnTime = Math.max(0, data.getInt("burnTime"));
        this.clientMaxBurnTime = Math.max(0, data.getInt("maxBurnTime"));
        this.clientBurnHeatScaled = Math.max(0, data.getInt("burnHeatScaled"));
        this.clientSteamUsed = Math.max(0, data.getInt("steamUsed"));

        this.clientSteamAmount = Math.max(0, data.getInt("steamAmount"));
        this.clientSteamCapacity = Math.max(1, data.getInt("steamCapacity"));
        this.clientSpentSteamAmount = Math.max(0, data.getInt("spentSteamAmount"));
        this.clientSpentSteamCapacity = Math.max(1, data.getInt("spentSteamCapacity"));

        this.clientInputFluidAmount = Math.max(0, data.getInt("inputFluidAmount"));
        this.clientInputFluidCapacity = Math.max(1, data.getInt("inputFluidCapacity"));
        this.clientInputFluidName = data.getString("inputFluidName");

        this.clientOutputMaterialName = data.getString("outputMaterialName");
        this.clientOutputAmount = Math.max(0, data.getInt("outputAmount"));
        this.clientMaxOutput = Math.max(1, data.getInt("maxOutput"));
        this.clientOutputColor = data.getInt("outputColor");
        this.clientHasOutputAcceptor = data.getBoolean("hasOutputAcceptor");
    }
}

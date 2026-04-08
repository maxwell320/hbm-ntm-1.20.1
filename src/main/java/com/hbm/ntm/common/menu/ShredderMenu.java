package com.hbm.ntm.common.menu;

import com.hbm.ntm.common.block.entity.ShredderBlockEntity;
import com.hbm.ntm.common.item.BatteryItem;
import com.hbm.ntm.common.item.ShredderBladesItem;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Shredder container menu — direct port of legacy {@code ContainerMachineShredder}.
 * <p>
 * Slot layout (legacy-verified coordinates from {@code ContainerMachineShredder.java}):
 * <ul>
 *   <li>Slots 0-8: 3×3 input grid at (44,18)→(80,54)</li>
 *   <li>Slots 9-26: 3×6 output grid at (116,18)→(152,108)</li>
 *   <li>Slot 27: left blade at (44,108)</li>
 *   <li>Slot 28: right blade at (80,108)</li>
 *   <li>Slot 29: battery at (8,108)</li>
 *   <li>Player inventory at y=151, hotbar at y=209</li>
 * </ul>
 */
@SuppressWarnings("null")
public class ShredderMenu extends MachineMenuBase<ShredderBlockEntity> {

    private static final int DATA_PROGRESS = 0;
    private static final int DATA_ENERGY = 1;
    private static final int DATA_MAX_ENERGY = 2;
    private static final int DATA_GEAR_LEFT = 3;
    private static final int DATA_GEAR_RIGHT = 4;
    private static final int DATA_COUNT = 5;

    private final ContainerData data;

    // Client-side constructor (from network buffer)
    public ShredderMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId,
            inventory,
            inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof final ShredderBlockEntity shredder ? shredder : null);
    }

    // Server-side constructor
    public ShredderMenu(final int containerId, final Inventory inventory, final ShredderBlockEntity shredder) {
        super(HbmMenuTypes.MACHINE_SHREDDER.get(), containerId, inventory, shredder, ShredderBlockEntity.SLOT_COUNT);
        final ItemStackHandler handler = shredder == null ? new ItemStackHandler(ShredderBlockEntity.SLOT_COUNT) : shredder.getInternalItemHandler();

        // Input 3×3 grid — slots 0-8 (legacy: 44,18 → 80,54 in 18px steps)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new SlotItemHandler(handler, row * 3 + col, 44 + col * 18, 18 + row * 18));
            }
        }

        // Output 3×6 grid — slots 9-26 (legacy: 116,18 → 152,108 in 18px steps)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new OutputSlotItemHandler(handler, 9 + row * 3 + col, 116 + col * 18, 18 + row * 18));
            }
        }

        // Left blade — slot 27 at (44,108)
        this.addSlot(new FilteredSlotItemHandler(handler, ShredderBlockEntity.SLOT_BLADE_LEFT, 44, 108,
            (slot, stack) -> stack.getItem() instanceof ShredderBladesItem));
        // Right blade — slot 28 at (80,108)
        this.addSlot(new FilteredSlotItemHandler(handler, ShredderBlockEntity.SLOT_BLADE_RIGHT, 80, 108,
            (slot, stack) -> stack.getItem() instanceof ShredderBladesItem));
        // Battery — slot 29 at (8,108)
        this.addSlot(new FilteredSlotItemHandler(handler, ShredderBlockEntity.SLOT_BATTERY, 8, 108,
            (slot, stack) -> stack.getItem() instanceof BatteryItem));

        // Player inventory — legacy: y=151 for main, y=209 for hotbar → addPlayerInventory(inv, 8, 151)
        this.addPlayerInventory(inventory, 8, 151);

        // Data slots for client sync
        this.data = shredder == null
            ? new SimpleContainerData(DATA_COUNT)
            : new MachineDataSlots(
                List.of(
                    shredder::getProgress,
                    shredder::getStoredEnergy,
                    shredder::getMaxStoredEnergy,
                    shredder::getGearLeft,
                    shredder::getGearRight
                ),
                List.of(
                    shredder::setClientProgress,
                    v -> {}, // energy is display-only on client
                    v -> {}, // max energy is display-only on client
                    v -> {}, // gear left is display-only on client
                    v -> {}  // gear right is display-only on client
                ));
        this.addMachineDataSlots(this.data);
    }

    @Override
    protected boolean moveToMachineSlots(final ItemStack stack) {
        // Legacy shift-click logic from ContainerMachineShredder.transferStackInSlot:
        // 1. Batteries → slot 29
        // 2. Blades → slots 27-28
        // 3. Everything else → input slots 0-8
        if (stack.getItem() instanceof BatteryItem) {
            return this.moveItemStackTo(stack, ShredderBlockEntity.SLOT_BATTERY, ShredderBlockEntity.SLOT_BATTERY + 1, false);
        }
        if (stack.getItem() instanceof ShredderBladesItem) {
            return this.moveItemStackTo(stack, ShredderBlockEntity.SLOT_BLADE_LEFT, ShredderBlockEntity.SLOT_BLADE_RIGHT + 1, false);
        }
        return this.moveItemStackTo(stack, ShredderBlockEntity.SLOT_INPUT_START, ShredderBlockEntity.SLOT_INPUT_END, false);
    }

    // ===== Data accessors for the screen =====

    public int progress() {
        return this.data.get(DATA_PROGRESS);
    }

    public int energy() {
        return this.data.get(DATA_ENERGY);
    }

    public int maxEnergy() {
        return this.data.get(DATA_MAX_ENERGY);
    }

    public int gearLeft() {
        return this.data.get(DATA_GEAR_LEFT);
    }

    public int gearRight() {
        return this.data.get(DATA_GEAR_RIGHT);
    }
}

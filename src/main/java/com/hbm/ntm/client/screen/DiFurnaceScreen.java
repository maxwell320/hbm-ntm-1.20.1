package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.DiFurnaceBlockEntity;
import com.hbm.ntm.common.menu.DiFurnaceMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

@SuppressWarnings("null")
public class DiFurnaceScreen extends MachineScreenBase<DiFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/GUIDiFurnace.png");

    public DiFurnaceScreen(final DiFurnaceMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int fuel = this.menu.fuel();
        final int maxFuel = this.menu.maxFuel();
        if (fuel > 0 && maxFuel > 0) {
            final int fuelHeight = Math.max(1, Math.min(52, fuel * 52 / maxFuel));
            guiGraphics.blit(TEXTURE, this.leftPos + 44, this.topPos + 70 - fuelHeight, 201, 53 - fuelHeight, 16, fuelHeight);
            guiGraphics.blit(TEXTURE, this.leftPos + 63, this.topPos + 37, 176, 0, 14, 14);
        }

        final int progress = this.menu.progress();
        final int speed = this.menu.processingSpeed();
        if (progress > 0 && speed > 0) {
            final int arrowWidth = Math.max(1, Math.min(24, progress * 24 / speed));
            guiGraphics.blit(TEXTURE, this.leftPos + 101, this.topPos + 35, 176, 14, arrowWidth + 1, 17);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (this.minecraft == null || this.minecraft.player == null || !this.minecraft.player.containerMenu.getCarried().isEmpty()) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            final Slot slot = this.menu.getSlot(i);
            final int slotX = this.leftPos + slot.x;
            final int slotY = this.topPos + slot.y;
            if (!this.inside(mouseX, mouseY, slotX, slotY, 16, 16)) {
                continue;
            }

            final int raw = switch (i) {
                case 0 -> this.menu.sideUpper();
                case 1 -> this.menu.sideLower();
                default -> this.menu.sideFuel();
            };
            final int tooltipY = mouseY - (slot.hasItem() ? 15 : 0);
            this.renderMachineTooltip(guiGraphics,
                List.of(Component.literal("Accepts items from: " + this.directionName(raw)).withStyle(ChatFormatting.YELLOW)),
                mouseX,
                tooltipY);
            return;
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (button == 1
            && this.minecraft != null
            && this.minecraft.player != null
            && this.minecraft.player.containerMenu.getCarried().isEmpty()) {
            if (this.handleSideCycleClick(mouseX, mouseY, DiFurnaceBlockEntity.SLOT_INPUT_LEFT, "cycleUpper")) {
                return true;
            }
            if (this.handleSideCycleClick(mouseX, mouseY, DiFurnaceBlockEntity.SLOT_INPUT_RIGHT, "cycleLower")) {
                return true;
            }
            if (this.handleSideCycleClick(mouseX, mouseY, DiFurnaceBlockEntity.SLOT_FUEL, "cycleFuel")) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleSideCycleClick(final double mouseX, final double mouseY, final int slotIndex, final String controlKey) {
        if (slotIndex < 0 || slotIndex >= this.menu.slots.size()) {
            return false;
        }
        final Slot slot = this.menu.getSlot(slotIndex);
        if (slot.hasItem()) {
            return false;
        }
        if (!this.inside(mouseX, mouseY, this.leftPos + slot.x, this.topPos + slot.y, 16, 16)) {
            return false;
        }

        this.playButtonClick();
        this.sendToggleControl(controlKey);
        return true;
    }

    private String directionName(final int raw) {
        final Direction direction = Direction.from3DDataValue(Math.floorMod(raw, Direction.values().length));
        return switch (direction) {
            case DOWN -> "Down";
            case UP -> "Up";
            case NORTH -> "North";
            case SOUTH -> "South";
            case WEST -> "West";
            case EAST -> "East";
        };
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

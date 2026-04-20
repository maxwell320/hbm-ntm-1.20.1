package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.DiFurnaceRtgBlockEntity;
import com.hbm.ntm.common.item.RtgPelletItem;
import com.hbm.ntm.common.menu.DiFurnaceRtgMenu;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("null")
public class DiFurnaceRtgScreen extends MachineScreenBase<DiFurnaceRtgMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_rtg_difurnace.png");

    public DiFurnaceRtgScreen(final DiFurnaceRtgMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int power = this.menu.power();
        if (power >= this.menu.powerThreshold()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 58, this.topPos + 36, 176, 31, 18, 16);
        }

        final int progress = this.menu.progress();
        final int processingTime = this.menu.processingTime();
        if (progress > 0 && processingTime > 0) {
            final int arrowWidth = Math.max(1, Math.min(24, progress * 24 / processingTime));
            guiGraphics.blit(TEXTURE, this.leftPos + 101, this.topPos + 35, 176, 14, arrowWidth + 1, 17);
        }

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 15, this.topPos + 36, 2);
        this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 15, this.topPos + 52, 3);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderLegacyInfoPanelTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos - 15, this.topPos + 52, 3,
            List.of(Component.translatable("desc.gui.rtgBFurnace.desc")));

        this.renderLegacyInfoPanelTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos - 15, this.topPos + 36, 2,
            this.rtgPelletHeatTooltip());

        if (this.inside(mouseX, mouseY, this.leftPos + 58, this.topPos + 36, 18, 16)) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.translatable("desc.gui.rtg.heat", this.menu.power())), mouseX, mouseY);
        }

        if (this.minecraft == null || this.minecraft.player == null || !this.minecraft.player.containerMenu.getCarried().isEmpty()) {
            return;
        }

        for (int i = 0; i < 2; i++) {
            final Slot slot = this.menu.getSlot(i);
            final int slotX = this.leftPos + slot.x;
            final int slotY = this.topPos + slot.y;
            if (!this.inside(mouseX, mouseY, slotX, slotY, 16, 16)) {
                continue;
            }

            final int dir = i == 0 ? this.menu.sideUpper() : this.menu.sideLower();
            final int tooltipY = mouseY - (slot.hasItem() ? 15 : 0);
            this.renderMachineTooltip(guiGraphics,
                List.of(Component.literal("Accepts items from: " + this.directionName(dir)).withStyle(ChatFormatting.YELLOW)),
                mouseX,
                tooltipY);
            return;
        }
    }

    private List<Component> rtgPelletHeatTooltip() {
        final List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("desc.gui.rtg.pellets"));

        ForgeRegistries.ITEMS.getValues().stream()
            .filter(item -> item instanceof RtgPelletItem)
            .map(item -> (RtgPelletItem) item)
            .sorted(Comparator.comparing(item -> item.getDescriptionId(ItemStack.EMPTY)))
            .forEach(pellet -> {
                final Component name = Component.translatable(((Item) pellet).getDescriptionId());
                tooltip.add(Component.translatable("desc.gui.rtg.pelletHeat", name, pellet.getHeat()));
            });

        return tooltip;
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (button == 1
            && this.minecraft != null
            && this.minecraft.player != null
            && this.minecraft.player.containerMenu.getCarried().isEmpty()) {
            if (this.handleSideCycleClick(mouseX, mouseY, DiFurnaceRtgBlockEntity.SLOT_INPUT_LEFT, "cycleUpper")) {
                return true;
            }
            if (this.handleSideCycleClick(mouseX, mouseY, DiFurnaceRtgBlockEntity.SLOT_INPUT_RIGHT, "cycleLower")) {
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


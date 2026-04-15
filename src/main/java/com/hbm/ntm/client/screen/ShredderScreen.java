package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.ShredderMenu;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class ShredderScreen extends MachineScreenBase<ShredderMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/machine/gui_shredder.png");
    private static final int WARNING_X = -16;
    private static final int WARNING_Y = 36;

    public ShredderScreen(final ShredderMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 233);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int maxEnergy = this.menu.maxEnergy();
        if (maxEnergy > 0 && this.menu.energy() > 0) {
            final int powerHeight = (int) ((long) this.menu.energy() * 88 / maxEnergy);
            guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 106 - powerHeight, 176, 160 - powerHeight, 16, powerHeight);
        }

        final int progressPixels = this.menu.progress() * 34 / Math.max(1, this.menu.processingSpeed());
        guiGraphics.blit(TEXTURE, this.leftPos + 63, this.topPos + 89, 176, 54, progressPixels + 1, 18);

        final int gearLeft = this.menu.gearLeft();
        if (gearLeft != 0) {
            final int gearLeftV = switch (gearLeft) {
                case 1 -> 0;
                case 2 -> 18;
                case 3 -> 36;
                default -> -1;
            };
            if (gearLeftV >= 0) {
                guiGraphics.blit(TEXTURE, this.leftPos + 43, this.topPos + 71, 176, gearLeftV, 18, 18);
            }
        }

        final int gearRight = this.menu.gearRight();
        if (gearRight != 0) {
            final int gearRightV = switch (gearRight) {
                case 1 -> 0;
                case 2 -> 18;
                case 3 -> 36;
                default -> -1;
            };
            if (gearRightV >= 0) {
                guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 71, 194, gearRightV, 18, 18);
            }
        }

        if (this.hasBladeError()) {
            this.renderLegacyInfoPanel(guiGraphics, this.leftPos + WARNING_X, this.topPos + WARNING_Y, 6);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 8,
            this.topPos + 18,
            16,
            88,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderLegacyInfoPanelTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + WARNING_X,
            this.topPos + WARNING_Y,
            6,
            this.hasBladeError()
                ? List.of(Component.literal("Error: Shredder blades are broken or missing!"))
                : List.of());
    }

    private boolean hasBladeError() {
        return this.menu.gearLeft() == 0 || this.menu.gearLeft() == 3 || this.menu.gearRight() == 0 || this.menu.gearRight() == 3;
    }

    @Override
    protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 0x404040, false);
        this.renderMachineLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

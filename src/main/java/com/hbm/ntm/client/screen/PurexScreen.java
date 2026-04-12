package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.PurexMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class PurexScreen extends MachineScreenBase<PurexMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_purex.png");

    public PurexScreen(final PurexMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 256);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics, TEXTURE,
            this.leftPos + 152,
            this.topPos + 18,
            16,
            61,
            176,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        final int progressPixels = this.menu.processTime() <= 0 ? 0 : this.menu.progress() * 70 / this.menu.processTime();
        if (progressPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 126, 176, 61, progressPixels, 16);
        }

        if (this.menu.canProcess()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 195, 0, 3, 6);
        } else if (this.menu.hasRecipe()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 192, 0, 3, 6);
        }

        if (this.menu.canProcess()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 195, 0, 3, 6);
        } else if (this.menu.hasRecipe() && this.menu.hasPower()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 192, 0, 3, 6);
        }

        this.renderVerticalFluidBar(guiGraphics, this.leftPos + 8, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(0), this.menu.fluidCapacity(0), 0xFF4E8FD6);
        this.renderVerticalFluidBar(guiGraphics, this.leftPos + 26, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(1), this.menu.fluidCapacity(1), 0xFF4E8FD6);
        this.renderVerticalFluidBar(guiGraphics, this.leftPos + 44, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(2), this.menu.fluidCapacity(2), 0xFF4E8FD6);
        this.renderVerticalFluidBar(guiGraphics, this.leftPos + 116, this.topPos + 36, 16, 52,
            this.menu.fluidAmount(3), this.menu.fluidCapacity(3), 0xFFE6C85C);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 152,
            this.topPos + 18,
            16,
            61,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 8, this.topPos + 18, 16, 52,
            "Input Tank A", this.menu.fluidName(0), this.menu.fluidAmount(0), this.menu.fluidCapacity(0));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 26, this.topPos + 18, 16, 52,
            "Input Tank B", this.menu.fluidName(1), this.menu.fluidAmount(1), this.menu.fluidCapacity(1));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 44, this.topPos + 18, 16, 52,
            "Input Tank C", this.menu.fluidName(2), this.menu.fluidAmount(2), this.menu.fluidCapacity(2));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 116, this.topPos + 36, 16, 52,
            "Output Tank", this.menu.fluidName(3), this.menu.fluidAmount(3), this.menu.fluidCapacity(3));

        if (this.inside(mouseX, mouseY, this.leftPos + 62, this.topPos + 126, 70, 16)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("Consumption: " + this.menu.consumption() + " HE/t")),
                Optional.empty(),
                mouseX,
                mouseY);
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }

    private void renderVerticalFluidBar(final GuiGraphics guiGraphics,
                                        final int x,
                                        final int y,
                                        final int width,
                                        final int height,
                                        final int amount,
                                        final int capacity,
                                        final int color) {
        if (amount <= 0 || capacity <= 0) {
            return;
        }
        final int fill = Math.max(1, Math.min(height, amount * height / capacity));
        guiGraphics.fill(x, y + height - fill, x + width, y + height, color);
    }
}

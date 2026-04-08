package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.PressMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class PressScreen extends MachineScreenBase<PressMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/machine/gui_press.png");

    public PressScreen(final PressMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 202);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        if (this.menu.burnTime() >= 20) {
            guiGraphics.blit(TEXTURE, this.leftPos + 27, this.topPos + 36, 0, 202, 14, 14);
        }
        final int pressPixels = this.menu.maxPressTicks() <= 0 ? 0 : this.menu.pressTicks() * 16 / this.menu.maxPressTicks();
        if (pressPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 35, 14, 202, 18, pressPixels);
        }
        final int speedPixels = this.menu.maxSpeed() <= 0 ? 0 : Math.max(0, Math.min(18, this.menu.speed() * 18 / this.menu.maxSpeed()));
        if (speedPixels > 0) {
            guiGraphics.fill(this.leftPos + 25, this.topPos + 16 + (18 - speedPixels), this.leftPos + 43, this.topPos + 34, 0xFF7F0000);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (mouseX >= 25 && mouseX < 43 && mouseY >= 16 && mouseY < 34) {
            guiGraphics.renderTooltip(this.font, java.util.List.of(Component.literal((this.menu.speed() * 100 / Math.max(1, this.menu.maxSpeed())) + "%")), java.util.Optional.empty(), mouseX, mouseY);
        }
        if (mouseX >= 25 && mouseX < 43 && mouseY >= 34 && mouseY < 52) {
            guiGraphics.renderTooltip(this.font, java.util.List.of(Component.literal((this.menu.burnTime() / 200) + " operations left")), java.util.Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

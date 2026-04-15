package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.CombustionEngineMenu;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class CombustionEngineScreen extends MachineScreenBase<CombustionEngineMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/generators/gui_combustion.png");

    private boolean draggingSetting;

    public CombustionEngineScreen(final CombustionEngineMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 203);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalFluidGaugeBar(guiGraphics,
            this.leftPos + 35,
            this.topPos + 17,
            16,
            52,
            this.menu.fuel(),
            this.menu.fuelCapacity(),
            0xFF9D885A);

        this.renderVerticalEnergyBar(guiGraphics,
            TEXTURE,
            this.leftPos + 143,
            this.topPos + 17,
            16,
            52,
            176,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        if (this.menu.engineOn()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 13, 192, 0, 35, 15);
        }

        final int sliderX = this.leftPos + 79 + this.menu.setting() * 32 / 30;
        guiGraphics.blit(TEXTURE, sliderX, this.topPos + 38, 192, 15, 4, 8);

        if (this.menu.pistonTier() >= 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 80, this.topPos + 51, 176, 52 + this.menu.pistonTier() * 12, 25, 12);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderFluidTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 35,
            this.topPos + 17,
            16,
            52,
            "Fuel",
            this.menu.fuelName(),
            this.menu.fuel(),
            this.menu.fuelCapacity());

        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 143,
            this.topPos + 17,
            16,
            52,
            this.menu.energy(),
            this.menu.maxEnergy());

        if (this.inside(mouseX, mouseY, this.leftPos + 79, this.topPos + 13, 35, 15)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal(this.menu.engineOn() ? "Ignition: ON" : "Ignition: OFF"),
                    Component.literal("Click to toggle")),
                java.util.Optional.empty(),
                mouseX,
                mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 79, this.topPos + 35, 36, 12)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("Throttle: " + this.menu.setting() + " / 30"),
                    Component.literal(String.format(java.util.Locale.ROOT, "Fuel usage: %.1f mB/t", this.menu.setting() * 0.2F))),
                java.util.Optional.empty(),
                mouseX,
                mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 79, this.topPos + 51, 35, 12)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("Generation: " + this.menu.generation() + " HE/t")),
                java.util.Optional.empty(),
                mouseX,
                mouseY);
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (button == 0 && this.handleToggleControlClick(mouseX, mouseY, this.leftPos + 79, this.topPos + 13, 35, 15, "turnOn")) {
            return true;
        }
        if (button == 0 && this.inside(mouseX, mouseY, this.leftPos + 79, this.topPos + 35, 36, 12)) {
            this.draggingSetting = true;
            this.updateSettingFromMouse(mouseX);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double dragX, final double dragY) {
        if (this.draggingSetting && button == 0) {
            this.updateSettingFromMouse(mouseX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.draggingSetting = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateSettingFromMouse(final double mouseX) {
        final int relative = Mth.clamp((int) Math.round(mouseX - (this.leftPos + 79)), 0, 32);
        final int value = Mth.clamp(relative * 30 / 32, 0, 30);
        if (value != this.menu.setting()) {
            this.sendIntControl("setting", value);
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

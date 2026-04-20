package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.CombustionEngineMenu;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
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
    protected boolean shouldRenderTitle() {
        return false;
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

        if (this.inside(mouseX, mouseY, this.leftPos + 89, this.topPos + 13, 16, 14)) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.literal("Ignition")), mouseX, mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 79, this.topPos + 38, 36, 8)) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.literal(String.format(Locale.ROOT, "%.1fmB/t", this.menu.setting() * 0.2D))), mouseX, mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 79, this.topPos + 50, 35, 14)) {
            this.renderMachineTooltip(guiGraphics, List.of(
                    Component.literal(String.format(Locale.ROOT, "%d HE/t", this.menu.generation())).withStyle(ChatFormatting.YELLOW),
                    Component.literal(String.format(Locale.ROOT, "%d HE/s", this.menu.generation() * 20)).withStyle(ChatFormatting.YELLOW)), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (button == 0 && this.handleToggleControlClick(mouseX, mouseY, this.leftPos + 89, this.topPos + 13, 16, 14, "turnOn")) {
            return true;
        }
        if (button == 0 && this.inside(mouseX, mouseY, this.leftPos + 79, this.topPos + 38, 36, 8)) {
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
        final int relative = Mth.clamp((int) Math.round(mouseX - (this.leftPos + 81)), 0, 32);
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


package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.DieselGeneratorMenu;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class DieselGeneratorScreen extends MachineScreenBase<DieselGeneratorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/gui_diesel.png");

    public DieselGeneratorScreen(final DieselGeneratorMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalFluidGaugeBar(guiGraphics,
            this.leftPos + 80,
            this.topPos + 17,
            16,
            52,
            this.menu.fuel(),
            this.menu.fuelCapacity(),
            0xFF9D885A);

        this.renderVerticalEnergyBar(guiGraphics,
            TEXTURE,
            this.leftPos + 152,
            this.topPos + 17,
            16,
            52,
            176,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 12, this.topPos + 26, 2);

        if (this.menu.fuel() > 0 && !this.menu.acceptableFuel()) {
            this.renderLegacyInfoPanel(guiGraphics, this.leftPos + 30, this.topPos + 45, 6);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderFluidTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 80,
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
            this.leftPos + 152,
            this.topPos + 17,
            16,
            52,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderLegacyInfoPanelTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos - 12,
            this.topPos + 26,
            2,
            List.of(
                Component.literal("Consumption: 1 mB/t"),
                Component.literal("Output: " + this.menu.hePerMb() + " HE/mB"),
                Component.literal("Requires medium, high or aero fuels")));

        if (this.menu.fuel() > 0 && !this.menu.acceptableFuel()) {
            this.renderLegacyInfoPanelTooltip(guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 30,
                this.topPos + 45,
                6,
                List.of(Component.literal("Selected fuel is not valid for diesel generation")));
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

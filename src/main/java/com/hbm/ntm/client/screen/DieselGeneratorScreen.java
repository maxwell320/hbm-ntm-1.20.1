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
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/GUIDiesel.png");

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

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 36, 2);

        if (this.menu.fuel() > 0 && this.menu.acceptableFuel()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 115, this.topPos + 34, 208, 0, 18, 18);
        }

        if (!this.menu.acceptableFuel()) {
            this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 68, 6);
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
            this.leftPos - 16,
            this.topPos + 36,
            2,
            List.of(
                Component.literal("Fuel consumption rate:"),
                Component.literal("  1 mB/t"),
                Component.literal("  20 mB/s"),
                Component.literal("(Consumption rate is constant)")));

        if (!this.menu.acceptableFuel()) {
            this.renderLegacyInfoPanelTooltip(guiGraphics,
                mouseX,
                mouseY,
                this.leftPos - 16,
                this.topPos + 68,
                6,
                List.of(
                    Component.literal("Error: The currently set fuel type"),
                    Component.literal("is not supported by this engine!")));
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

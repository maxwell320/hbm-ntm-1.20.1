package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.ElectricFurnaceMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class ElectricFurnaceScreen extends MachineScreenBase<ElectricFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/GUIElectricFurnace.png");

    public ElectricFurnaceScreen(final ElectricFurnaceMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics,
            TEXTURE,
            this.leftPos + 20,
            this.topPos + 17,
            16,
            52,
            200,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        if (this.menu.hasPower()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 35, 176, 0, 16, 16);
        }

        final int progress = this.menu.progress();
        final int maxProgress = this.menu.maxProgress();
        if (progress > 0 && maxProgress > 0) {
            final int arrowWidth = Math.max(1, Math.min(24, progress * 24 / maxProgress));
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 34, 176, 17, arrowWidth, 17);
        }

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos + 151, this.topPos + 19, 8);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 20,
            this.topPos + 17,
            16,
            52,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderLegacyInfoPanelTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 151,
            this.topPos + 19,
            8,
            List.of(
                Component.literal("Upgrades"),
                Component.literal("Speed"),
                Component.literal("Power")
            ));

        this.renderUpgradeInfoTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 151, this.topPos + 19, 8, 8);

        if (this.inside(mouseX, mouseY, this.leftPos + 56, this.topPos + 35, 16, 16)) {
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
}
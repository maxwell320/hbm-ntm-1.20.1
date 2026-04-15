package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.SolderingStationMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class SolderingStationScreen extends MachineScreenBase<SolderingStationMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/machine/gui_soldering_station.png");

    public SolderingStationScreen(final SolderingStationMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 204);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics, TEXTURE,
            this.leftPos + 152,
            this.topPos + 18,
            16,
            52,
            176,
            0,
            this.menu.energy(),
            this.menu.maxPower());

        final int progressPixels = this.menu.progress() * 33 / Math.max(1, this.menu.processTime());
        if (progressPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 72, this.topPos + 28, 192, 0, progressPixels, 14);
        }

        if (this.menu.collisionPrevention()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 5, this.topPos + 66, 192, 14, 10, 10);
        }
        this.renderConfigToggleIndicator(guiGraphics, this.leftPos + 5, this.topPos + 66, 10, 10, this.menu.collisionPrevention());

        if (this.menu.energy() >= this.menu.consumption()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 156, this.topPos + 4, 176, 52, 9, 12);
        }

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos + 78, this.topPos + 67, 8);

        this.renderHorizontalFluidBar(guiGraphics, this.leftPos + 35, this.topPos + 79, 34, 16,
            this.menu.fluidAmount(), this.menu.fluidCapacity(), 0xFF2090D0);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 152, this.topPos + 18, 16, 52,
            this.menu.energy(), this.menu.maxPower());

        this.renderFluidTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 35, this.topPos + 79, 34, 16,
            "Fluid Tank", this.menu.fluidName(), this.menu.fluidAmount(), this.menu.fluidCapacity());

        if (this.inside(mouseX, mouseY, this.leftPos + 5, this.topPos + 66, 10, 10)) {
            guiGraphics.renderTooltip(this.font,
                List.of(
                    Component.literal("Recipe Collision Prevention: ")
                        .append(Component.literal(this.menu.collisionPrevention() ? "ON" : "OFF")
                            .withStyle(this.menu.collisionPrevention() ? ChatFormatting.GREEN : ChatFormatting.RED)),
                    Component.literal("Prevents no-fluid recipes while fluid is present.")),
                Optional.empty(), mouseX, mouseY);
        }

        this.renderUpgradeInfoTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 78, this.topPos + 67, 8, 8);

        this.renderUpgradeInfoTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 89, this.topPos + 63, 36, 18);
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (this.handleToggleControlClick(mouseX, mouseY, this.leftPos + 5, this.topPos + 66, 10, 10, "collision")) {
            return true;
        }

        return false;
    }


    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}


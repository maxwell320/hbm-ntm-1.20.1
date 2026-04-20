package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.CyclotronMenu;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class CyclotronScreen extends MachineScreenBase<CyclotronMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/machine/gui_cyclotron.png");

    public CyclotronScreen(final CyclotronMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 190, 215);
        this.inventoryLabelX = 15;
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics, TEXTURE,
            this.leftPos + 168,
            this.topPos + 18,
            16,
            63,
            190,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        final int progressPixels = this.menu.processDuration() <= 0 ? 0 : this.menu.progress() * 34 / this.menu.processDuration();
        if (progressPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 48, this.topPos + 27, 206, 0, progressPixels, 34);
            guiGraphics.blit(TEXTURE, this.leftPos + 172, this.topPos + 4, 190, 63, 9, 12);
        }

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos + 49, this.topPos + 85, 8);

        this.renderHorizontalFluidBar(guiGraphics, this.leftPos + 11, this.topPos + 88, 34, 7,
            this.menu.fluidAmount(0), this.menu.fluidCapacity(0), 0xFF4E8FD6);
        this.renderHorizontalFluidBar(guiGraphics, this.leftPos + 11, this.topPos + 97, 34, 7,
            this.menu.fluidAmount(1), this.menu.fluidCapacity(1), 0xFFB8B8B8);
        this.renderHorizontalFluidBar(guiGraphics, this.leftPos + 107, this.topPos + 97, 34, 16,
            this.menu.fluidAmount(2), this.menu.fluidCapacity(2), 0xFF111111);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 168,
            this.topPos + 18,
            16,
            63,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderFluidTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 11,
            this.topPos + 81,
            34,
            7,
            this.menu.fluidName(0),
            this.menu.fluidAmount(0),
            this.menu.fluidCapacity(0));

        this.renderFluidTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 11,
            this.topPos + 90,
            34,
            7,
            this.menu.fluidName(1),
            this.menu.fluidAmount(1),
            this.menu.fluidCapacity(1));

        this.renderFluidTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 107,
            this.topPos + 81,
            34,
            16,
            this.menu.fluidName(2),
            this.menu.fluidAmount(2),
            this.menu.fluidCapacity(2));

        this.renderLegacyInfoPanelTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 49, this.topPos + 85, 8,
            List.of(
                Component.translatable("desc.gui.upgrade"),
                Component.translatable("desc.gui.upgrade.speed"),
                Component.translatable("desc.gui.upgrade.effectiveness"),
                Component.translatable("desc.gui.upgrade.power")));
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

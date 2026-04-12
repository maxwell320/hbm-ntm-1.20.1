package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.CentrifugeMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class CentrifugeScreen extends MachineScreenBase<CentrifugeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_centrifuge.png");

    public CentrifugeScreen(final CentrifugeMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 186);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics, TEXTURE,
            this.leftPos + 9,
            this.topPos + 13,
            16,
            35,
            176,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        int progress = this.menu.processingSpeed() <= 0 ? 0 : this.menu.progress() * 145 / this.menu.processingSpeed();
        for (int i = 0; i < 4; i++) {
            final int barHeight = Math.min(progress, 36);
            if (barHeight > 0) {
                guiGraphics.blit(TEXTURE,
                    this.leftPos + 65 + i * 20,
                    this.topPos + 50 - barHeight,
                    176,
                    71 - barHeight,
                    12,
                    barHeight);
            }
            progress -= barHeight;
            if (progress <= 0) {
                break;
            }
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 9,
            this.topPos + 13,
            16,
            35,
            this.menu.energy(),
            this.menu.maxEnergy());

        if (this.inside(mouseX, mouseY, this.leftPos + 152, this.topPos + 17, 16, 36)) {
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

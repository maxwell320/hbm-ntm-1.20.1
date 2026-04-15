package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.DiFurnaceRtgMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class DiFurnaceRtgScreen extends MachineScreenBase<DiFurnaceRtgMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_rtg_difurnace.png");

    public DiFurnaceRtgScreen(final DiFurnaceRtgMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int power = this.menu.power();
        if (power >= this.menu.powerThreshold()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 12, this.topPos + 34, 176, 0, 18, 18);
        }

        final int progress = this.menu.progress();
        final int processingTime = this.menu.processingTime();
        if (progress > 0 && processingTime > 0) {
            final int arrowWidth = Math.max(1, Math.min(24, progress * 24 / processingTime));
            guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 35, 176, 31, arrowWidth, 17);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (this.inside(mouseX, mouseY, this.leftPos + 12, this.topPos + 34, 18, 18)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("RTG Heat: " + this.menu.power() + " / " + this.menu.powerThreshold())),
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

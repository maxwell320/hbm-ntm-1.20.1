package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.RtgFurnaceMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class RtgFurnaceScreen extends MachineScreenBase<RtgFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/RTGfurnace.png");

    public RtgFurnaceScreen(final RtgFurnaceMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        if (this.menu.heat() > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 55, this.topPos + 35, 176, 0, 18, 16);
        }

        final int progress = this.menu.progress();
        final int processingSpeed = this.menu.processingSpeed();
        if (progress > 0 && processingSpeed > 0) {
            final int arrowWidth = Math.max(1, Math.min(24, progress * 24 / processingSpeed));
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 34, 176, 16, arrowWidth, 17);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (this.inside(mouseX, mouseY, this.leftPos + 55, this.topPos + 35, 18, 16)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("RTG Heat: " + this.menu.heat())),
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
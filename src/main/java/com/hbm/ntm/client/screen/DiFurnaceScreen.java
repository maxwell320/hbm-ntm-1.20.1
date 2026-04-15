package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.DiFurnaceMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class DiFurnaceScreen extends MachineScreenBase<DiFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/GUIDiFurnace.png");

    public DiFurnaceScreen(final DiFurnaceMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int fuel = this.menu.fuel();
        final int maxFuel = this.menu.maxFuel();
        if (fuel > 0 && maxFuel > 0) {
            final int fuelWidth = Math.max(1, Math.min(52, fuel * 52 / maxFuel));
            guiGraphics.blit(TEXTURE, this.leftPos + 13, this.topPos + 53, 176, 14, fuelWidth, 14);
            guiGraphics.blit(TEXTURE, this.leftPos + 12, this.topPos + 34, 176, 0, 18, 18);
        }

        final int progress = this.menu.progress();
        final int speed = this.menu.processingSpeed();
        if (progress > 0 && speed > 0) {
            final int arrowWidth = Math.max(1, Math.min(24, progress * 24 / speed));
            guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 35, 176, 31, arrowWidth, 17);
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (this.inside(mouseX, mouseY, this.leftPos + 13, this.topPos + 53, 52, 14)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal(this.menu.fuel() + " / " + this.menu.maxFuel() + " Heat")),
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

package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.FurnaceSteelBlockEntity;
import com.hbm.ntm.common.menu.FurnaceSteelMenu;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class FurnaceSteelScreen extends MachineScreenBase<FurnaceSteelMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_furnace_steel.png");

    public FurnaceSteelScreen(final FurnaceSteelMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int heat = this.menu.heat();
        final int maxHeat = this.menu.maxHeat();
        if (heat > 0 && maxHeat > 0) {
            final int heatHeight = heat * 48 / maxHeat;
            guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 67 - heatHeight, 176, 76 - heatHeight, 7, heatHeight);
        }

        for (int lane = 0; lane < 3; lane++) {
            final int progress = this.menu.progress(lane);
            if (progress > 0) {
                final int progressWidth = progress * 69 / Math.max(1, this.menu.processTime());
                guiGraphics.blit(TEXTURE, this.leftPos + 54, this.topPos + 18 + 18 * lane, 176, 18, progressWidth, 5);
            }

            final int bonus = this.menu.bonus(lane);
            if (bonus > 0) {
                final int bonusWidth = bonus * 69 / 100;
                guiGraphics.blit(TEXTURE, this.leftPos + 54, this.topPos + 27 + 18 * lane, 176, 23, bonusWidth, 5);
            }

            if (this.menu.wasOn()) {
                guiGraphics.blit(TEXTURE, this.leftPos + 16, this.topPos + 16 + 18 * lane, 176, 0, 18, 18);
            }
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        for (int lane = 0; lane < 3; lane++) {
            final int pX = this.leftPos + 53;
            final int pY = this.topPos + 17 + 18 * lane;
            if (this.inside(mouseX, mouseY, pX, pY, 70, 7)) {
                this.renderMachineTooltip(guiGraphics, List.of(Component.literal(String.format(Locale.US, "%,d / %,dTU", this.menu.progress(lane), this.menu.processTime()))), mouseX, mouseY);
            }

            final int bY = this.topPos + 26 + 18 * lane;
            if (this.inside(mouseX, mouseY, pX, bY, 70, 7)) {
                this.renderMachineTooltip(guiGraphics, List.of(Component.literal("Bonus: " + this.menu.bonus(lane) + "%")), mouseX, mouseY);
            }
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 151, this.topPos + 18, 9, 50)) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.literal(String.format(Locale.US, "%,d / %,dTU", this.menu.heat(), this.menu.maxHeat()))), mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}


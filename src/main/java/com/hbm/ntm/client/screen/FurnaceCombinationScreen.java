package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.FurnaceCombinationMenu;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class FurnaceCombinationScreen extends MachineScreenBase<FurnaceCombinationMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_furnace_combination.png");

    public FurnaceCombinationScreen(final FurnaceCombinationMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 186);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int progress = this.menu.progress() * 38 / this.menu.processTime();
        if (progress > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 45, this.topPos + 37, 176, 0, progress, 5);
        }

        final int heat = this.menu.heat() * 37 / this.menu.maxHeat();
        if (heat > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 45, this.topPos + 46, 176, 5, heat, 5);
        }

        this.renderVerticalFluidGaugeBar(guiGraphics,
            this.leftPos + 118,
            this.topPos + 18,
            16,
            52,
            this.menu.tankAmount(),
            this.menu.tankCapacity(),
            0xFF6A4B2D);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (this.inside(mouseX, mouseY, this.leftPos + 44, this.topPos + 36, 39, 7)) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.literal(String.format(Locale.US, "%,d / %,dTU", this.menu.progress(), this.menu.processTime()))), mouseX, mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 44, this.topPos + 45, 39, 7)) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.literal(String.format(Locale.US, "%,d / %,dTU", this.menu.heat(), this.menu.maxHeat()))), mouseX, mouseY);
        }

        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 118, this.topPos + 18, 16, 52,
            this.menu.tankName(), this.menu.tankAmount(), this.menu.tankCapacity());
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}


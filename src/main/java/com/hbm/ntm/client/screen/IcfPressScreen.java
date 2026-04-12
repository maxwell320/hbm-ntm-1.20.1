package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.IcfPressBlockEntity;
import com.hbm.ntm.common.menu.IcfPressMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class IcfPressScreen extends MachineScreenBase<IcfPressMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_icf_press.png");

    public IcfPressScreen(final IcfPressMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 179);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final int maxMuon = Math.max(1, this.menu.maxMuon());
        final int muonPixels = Math.max(0, Math.min(52, this.menu.muon() * 52 / maxMuon));
        if (muonPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 28, this.topPos + 70 - muonPixels, 176, 52 - muonPixels, 4, muonPixels);
        }

        this.renderVerticalFluidBar(guiGraphics, this.leftPos + 44, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(0), this.menu.fluidCapacity(0), 0xFF47B9E8);
        this.renderVerticalFluidBar(guiGraphics, this.leftPos + 152, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(1), this.menu.fluidCapacity(1), 0xFFE85A6A);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 44, this.topPos + 18, 16, 52,
            "Fuel Tank A", this.menu.fluidName(0), this.menu.fluidAmount(0), this.menu.fluidCapacity(0));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 152, this.topPos + 18, 16, 52,
            "Fuel Tank B", this.menu.fluidName(1), this.menu.fluidAmount(1), this.menu.fluidCapacity(1));

        if (this.inside(mouseX, mouseY, this.leftPos + 28, this.topPos + 18, 4, 52)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("Muon charge: " + this.menu.muon() + " / " + this.menu.maxMuon())),
                Optional.empty(), mouseX, mouseY);
        }

        if (this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot.index == IcfPressBlockEntity.SLOT_SOLID_FUEL_A) {
                guiGraphics.renderTooltip(this.font,
                    List.of(Component.literal("Item input: Top/Bottom")),
                    Optional.empty(), mouseX, mouseY);
            } else if (this.hoveredSlot.index == IcfPressBlockEntity.SLOT_SOLID_FUEL_B) {
                guiGraphics.renderTooltip(this.font,
                    List.of(Component.literal("Item input: Sides")),
                    Optional.empty(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }

    private void renderVerticalFluidBar(final GuiGraphics guiGraphics,
                                        final int x,
                                        final int y,
                                        final int width,
                                        final int height,
                                        final int amount,
                                        final int capacity,
                                        final int color) {
        if (amount <= 0 || capacity <= 0) {
            return;
        }
        final int fill = Math.max(1, Math.min(height, amount * height / capacity));
        guiGraphics.fill(x, y + height - fill, x + width, y + height, color);
    }
}

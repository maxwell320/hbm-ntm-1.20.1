package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.IcfMenu;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class IcfScreen extends MachineScreenBase<IcfMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/reactors/gui_icf.png");

    public IcfScreen(final IcfMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 248, 222);
    }

    @Override
    protected void renderBg(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, 248, 114);
        guiGraphics.blit(TEXTURE, this.leftPos + 36, this.topPos + 122, 36, 122, 176, 108);
        this.renderMachineContents(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final long maxLaser = this.menu.maxLaser();
        if (maxLaser > 0L && this.menu.laser() > 0L) {
            final int laserPixels = (int) Math.max(0L, Math.min(70L, this.menu.laser() * 70L / maxLaser));
            if (laserPixels > 0) {
                guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 88 - laserPixels, 212, 192 - laserPixels, 16, laserPixels);
            }
        }

        final long maxHeat = this.menu.maxHeat();
        if (maxHeat > 0L) {
            this.renderSmoothNeedleGauge(
                guiGraphics,
                this.leftPos + 196,
                this.topPos + 98,
                (double) this.menu.heat() / (double) maxHeat,
                5.0D,
                2.0D,
                1.0D,
                0xFFFF00AF,
                0xFFFF00AF);
        }

        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 44, this.topPos + 18, 16, 70,
            this.menu.fluidAmount(0), this.menu.fluidCapacity(0), 0xFF4E8FD6);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 188, this.topPos + 18, 16, 70,
            this.menu.fluidAmount(1), this.menu.fluidCapacity(1), 0xFFE39BB9);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 224, this.topPos + 18, 16, 70,
            this.menu.fluidAmount(2), this.menu.fluidCapacity(2), 0xFFE300FF);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 44, this.topPos + 18, 16, 70,
            this.menu.fluidName(0), this.menu.fluidAmount(0), this.menu.fluidCapacity(0));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 188, this.topPos + 18, 16, 70,
            this.menu.fluidName(1), this.menu.fluidAmount(1), this.menu.fluidCapacity(1));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 224, this.topPos + 18, 16, 70,
            this.menu.fluidName(2), this.menu.fluidAmount(2), this.menu.fluidCapacity(2));

        if (this.inside(mouseX, mouseY, this.leftPos + 8, this.topPos + 18, 16, 70)) {
            final List<Component> tooltip = new ArrayList<>();
            final long maxLaser = this.menu.maxLaser();
            if (maxLaser <= 0L || this.menu.laser() <= 0L) {
                tooltip.add(Component.literal("OFFLINE"));
            } else {
                final double percent = (this.menu.laser() * 1000L / maxLaser) / 10D;
                tooltip.add(Component.literal(this.legacyShortNumber(this.menu.laser()) + "TU/t - " + percent + "%"));
            }
            this.renderMachineTooltip(guiGraphics, tooltip, mouseX, mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 187, this.topPos + 89, 18, 18)) {
            final List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal(this.legacyShortNumber(this.menu.heat()) + " / " + this.legacyShortNumber(this.menu.maxHeat()) + "TU"));
            this.renderMachineTooltip(guiGraphics, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 44, this.imageHeight - 93, 0x404040, false);
        this.renderMachineLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }

    private String legacyShortNumber(final long value) {
        if (value >= 1_000_000_000_000_000_000L) {
            return this.scaleShort(value, 1_000_000_000_000_000_000D, "E");
        }
        if (value >= 1_000_000_000_000_000L) {
            return this.scaleShort(value, 1_000_000_000_000_000D, "P");
        }
        if (value >= 1_000_000_000_000L) {
            return this.scaleShort(value, 1_000_000_000_000D, "T");
        }
        if (value >= 1_000_000_000L) {
            return this.scaleShort(value, 1_000_000_000D, "G");
        }
        if (value >= 1_000_000L) {
            return this.scaleShort(value, 1_000_000D, "M");
        }
        if (value >= 1_000L) {
            return this.scaleShort(value, 1_000D, "k");
        }
        return Long.toString(value);
    }

    private String scaleShort(final long value, final double divisor, final String suffix) {
        final double scaled = Math.round((value / divisor) * 100.0D) / 100.0D;
        return scaled + suffix;
    }
}

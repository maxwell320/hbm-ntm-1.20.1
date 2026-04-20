package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.RotaryFurnaceMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class RotaryFurnaceScreen extends MachineScreenBase<RotaryFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_rotary_furnace.png");

    public RotaryFurnaceScreen(final RotaryFurnaceMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 186);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        if (this.menu.burnTime() > 0) {
            final int burnPixels = this.menu.burnTime() * 14 / this.menu.maxBurnTime();
            guiGraphics.blit(TEXTURE, this.leftPos + 26, this.topPos + 69 - burnPixels, 176, 24 - burnPixels, 14, burnPixels);
        }

        final int progressPixels = (int) Math.ceil(this.menu.progressScaled() * 33.0D / 10_000.0D);
        if (progressPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 63, this.topPos + 30, 176, 0, progressPixels, 10);
        }

        this.renderHorizontalFluidBar(guiGraphics,
            this.leftPos + 8,
            this.topPos + 36,
            52,
            16,
            this.menu.inputFluidAmount(),
            this.menu.inputFluidCapacity(),
            0xFF7AA2C4);
        this.renderVerticalFluidGaugeBar(guiGraphics,
            this.leftPos + 134,
            this.topPos + 18,
            16,
            52,
            this.menu.steamAmount(),
            this.menu.steamCapacity(),
            0xFFE5E5E5);
        this.renderVerticalFluidGaugeBar(guiGraphics,
            this.leftPos + 152,
            this.topPos + 18,
            16,
            52,
            this.menu.spentSteamAmount(),
            this.menu.spentSteamCapacity(),
            0xFF445772);
        this.renderVerticalFluidGaugeBar(guiGraphics,
            this.leftPos + 98,
            this.topPos + 18,
            16,
            52,
            this.menu.outputAmount(),
            this.menu.maxOutput(),
            this.menu.outputColor());

        final int heatPixels = Math.min(52, this.menu.burnHeatScaled() * 52 / 1_000);
        if (heatPixels > 0) {
            guiGraphics.fill(this.leftPos + 96, this.topPos + 70 - heatPixels, this.leftPos + 97, this.topPos + 70, 0x80FFB347);
        }

    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderSimpleTankTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 8,
            this.topPos + 36,
            52,
            16,
            this.menu.inputFluidName(),
            this.menu.inputFluidAmount(),
            this.menu.inputFluidCapacity());

        if (this.inside(mouseX, mouseY, this.leftPos + 134, this.topPos + 18, 16, 52)) {
            this.renderSimpleTankTooltip(guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 134,
                this.topPos + 18,
                16,
                52,
                this.menu.steamAmount() > 0 ? "Steam" : "",
                this.menu.steamAmount(),
                this.menu.steamCapacity());
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 52)) {
            this.renderSimpleTankTooltip(guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 152,
                this.topPos + 18,
                16,
                52,
                this.menu.spentSteamAmount() > 0 ? "Low-pressure Steam" : "",
                this.menu.spentSteamAmount(),
                this.menu.spentSteamCapacity());
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 98, this.topPos + 18, 16, 52)) {
            if (this.menu.outputMaterialName().isBlank() || this.menu.outputAmount() <= 0) {
                this.renderMachineTooltip(guiGraphics, List.of(Component.literal("Empty").withStyle(ChatFormatting.RED)), mouseX, mouseY);
            } else {
                this.renderMachineTooltip(guiGraphics, List.of(Component.literal(this.menu.outputMaterialName() + ": " + this.menu.formattedOutputAmount()).withStyle(ChatFormatting.YELLOW)), mouseX, mouseY);
            }
        }

        // Legacy shows burn-module description on fuel slot hover; modern menu sync does not currently expose this text payload.
    }

    @Override
    protected int titleLabelX() {
        return (this.imageWidth - 54) / 2 - this.font.width(this.title) / 2;
    }

    private void renderSimpleTankTooltip(final GuiGraphics guiGraphics,
                                         final int mouseX,
                                         final int mouseY,
                                         final int x,
                                         final int y,
                                         final int width,
                                         final int height,
                                         final String fluidName,
                                         final int amount,
                                         final int capacity) {
        if (!this.inside(mouseX, mouseY, x, y, width, height)) {
            return;
        }
        if (amount <= 0) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.translatable("hbmfluid.none")), mouseX, mouseY);
            return;
        }
        this.renderMachineTooltip(guiGraphics, List.of(
                Component.literal(fluidName),
                Component.literal(amount + "/" + capacity + "mB")), mouseX, mouseY);
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}


package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.GasCentrifugeMenu;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class GasCentrifugeScreen extends MachineScreenBase<GasCentrifugeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_centrifuge_gas.png");

    public GasCentrifugeScreen(final GasCentrifugeMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 206, 204);
    }

    @Override
    protected boolean shouldRenderTitle() {
        return false;
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics, TEXTURE,
            this.leftPos + 182,
            this.topPos + 17,
            16,
            52,
            206,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        final int progressPixels = this.menu.processingSpeed() <= 0 ? 0 : this.menu.progress() * 36 / this.menu.processingSpeed();
        if (progressPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 70, this.topPos + 35, 206, 52, progressPixels, 13);
        }

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 12, this.topPos + 16, 3);
        this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 12, this.topPos + 32, 2);

        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 16, this.topPos + 16, 6, 52,
            this.menu.inputAmount(), this.menu.tankCapacity(), 0xFFD1CEBE);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 32, this.topPos + 16, 6, 52,
            this.menu.inputAmount(), this.menu.tankCapacity(), 0xFFD1CEBE);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 138, this.topPos + 16, 6, 52,
            this.menu.outputAmount(), this.menu.tankCapacity(), 0xFFB8C77A);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 154, this.topPos + 16, 6, 52,
            this.menu.outputAmount(), this.menu.tankCapacity(), 0xFFB8C77A);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 182,
            this.topPos + 17,
            16,
            52,
            this.menu.energy(),
            this.menu.maxEnergy());

        if (this.inside(mouseX, mouseY, this.leftPos + 15, this.topPos + 15, 24, 55)) {
            final Component inputName = Component.translatable(this.menu.inputTypeKey())
                .withStyle(this.menu.inputNeedsSpeedUpgrade()
                    ? (this.menu.hasSpeedUpgrade() ? ChatFormatting.GOLD : ChatFormatting.DARK_RED)
                    : ChatFormatting.WHITE);
            final List<Component> tooltip = new ArrayList<>();
            tooltip.add(inputName);
            tooltip.add(Component.literal(this.menu.inputAmount() + " / " + this.menu.tankCapacity() + " mB"));
            if (this.menu.inputNeedsSpeedUpgrade() && !this.menu.hasSpeedUpgrade()) {
                tooltip.add(Component.translatable("screen.hbmntm.machine_gascent.warning").withStyle(ChatFormatting.DARK_RED));
            }
            this.renderMachineTooltip(guiGraphics, tooltip, mouseX, mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 137, this.topPos + 15, 25, 55)) {
            this.renderMachineTooltip(guiGraphics, List.of(
                    Component.translatable(this.menu.outputTypeKey()),
                    Component.literal(this.menu.outputAmount() + " / " + this.menu.tankCapacity() + " mB")), mouseX, mouseY);
        }

        this.renderLegacyInfoPanelTooltip(guiGraphics, mouseX, mouseY, this.leftPos - 12, this.topPos + 16, 3,
            this.legacyKeyArrayTooltip("desc.gui.gasCent.enrichment"));

        this.renderLegacyInfoPanelTooltip(guiGraphics, mouseX, mouseY, this.leftPos - 12, this.topPos + 32, 2,
            this.legacyKeyArrayTooltip("desc.gui.gasCent.output"));
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }

    private List<Component> legacyKeyArrayTooltip(final String key) {
        final String translated = I18n.get(key);
        final String[] lines = translated.split("\\$");
        final List<Component> tooltip = new ArrayList<>(lines.length);

        for (int i = 0; i < lines.length; i++) {
            final String raw = lines[i];
            final boolean green = raw.contains("\u00A72");
            final boolean gold = raw.contains("\u00A76");
            final String text = raw.replace("\u00A72", "").replace("\u00A76", "").replace("\u00A7r", "");

            if (i == 0 && green) {
                tooltip.add(Component.literal(text).withStyle(ChatFormatting.GREEN));
            } else if (i == 0 && gold) {
                tooltip.add(Component.literal(text).withStyle(ChatFormatting.GOLD));
            } else {
                tooltip.add(Component.literal(text));
            }
        }

        return tooltip;
    }

}


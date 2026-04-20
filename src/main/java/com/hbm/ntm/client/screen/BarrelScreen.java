package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.BarrelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class BarrelScreen extends AbstractContainerScreen<BarrelMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/storage/gui_barrel.png");

    public BarrelScreen(final BarrelMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        final int capacity = this.menu.capacity();
        final int amount = this.menu.fluidAmount();
        if (capacity > 0 && amount > 0) {
            final int filled = Math.max(1, (int) Math.ceil(amount * 52.0D / capacity));
            guiGraphics.fill(this.leftPos + 71, this.topPos + 69 - filled, this.leftPos + 105, this.topPos + 69, this.menu.fluidColor());
        }
        guiGraphics.blit(TEXTURE, this.leftPos + 151, this.topPos + 34, 176, this.menu.mode() * 18, 18, 18);
    }

    @Override
    protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        final String titleString = this.title.getString();
        guiGraphics.drawString(this.font, titleString, this.imageWidth / 2 - this.font.width(titleString) / 2, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0x404040, false);
    }

    @Override
    public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX >= this.leftPos + 71 && mouseX < this.leftPos + 105 && mouseY >= this.topPos + 17 && mouseY < this.topPos + 69) {
            final String fluidName = this.menu.fluidName();
            final Component displayName = fluidName == null || fluidName.isBlank()
                ? Component.translatable("hbmfluid.none")
                : Component.literal(fluidName);
            guiGraphics.renderTooltip(this.font,
                java.util.List.of(displayName, Component.literal(this.menu.fluidAmount() + "/" + this.menu.capacity() + "mB")),
                java.util.Optional.empty(),
                mouseX,
                mouseY);
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (mouseX >= this.leftPos + 151 && mouseX < this.leftPos + 169 && mouseY > this.topPos + 35 && mouseY <= this.topPos + 53) {
            if (this.minecraft == null || this.minecraft.gameMode == null) {
                return true;
            }
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}


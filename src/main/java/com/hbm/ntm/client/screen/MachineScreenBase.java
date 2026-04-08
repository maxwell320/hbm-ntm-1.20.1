package com.hbm.ntm.client.screen;

import com.hbm.ntm.common.menu.MachineMenuBase;
import com.hbm.ntm.common.network.HbmPacketHandler;
import com.hbm.ntm.common.network.MachineControlPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public abstract class MachineScreenBase<T extends MachineMenuBase<?>> extends AbstractContainerScreen<T> {
    protected MachineScreenBase(final T menu, final Inventory inventory, final Component title, final int imageWidth, final int imageHeight) {
        super(menu, inventory, title);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public void render(final @NotNull GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(final @NotNull GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(this.texture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.renderMachineContents(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(final @NotNull GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 0x404040, false);
        this.renderMachineLabels(guiGraphics, mouseX, mouseY);
    }

    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
    }

    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
    }

    protected final void sendControl(final CompoundTag data) {
        if (this.menu.machine() == null || this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        HbmPacketHandler.CHANNEL.sendToServer(new MachineControlPacket(this.menu.machine().getBlockPos(), data));
    }

    protected final boolean inside(final double mouseX, final double mouseY, final int x, final int y, final int width, final int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    protected abstract ResourceLocation texture();
}

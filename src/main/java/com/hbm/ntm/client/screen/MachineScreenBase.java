package com.hbm.ntm.client.screen;

import com.hbm.ntm.common.menu.MachineMenuBase;
import com.hbm.ntm.common.network.HbmPacketHandler;
import com.hbm.ntm.common.network.MachineControlPacket;
import com.hbm.ntm.common.network.MachineStateRequestPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public abstract class MachineScreenBase<T extends MachineMenuBase<?>> extends AbstractContainerScreen<T> {
    private static final int REPAIR_ICON_SIZE = 8;

    protected MachineScreenBase(final T menu, final Inventory inventory, final Component title, final int imageWidth, final int imageHeight) {
        super(menu, inventory, title);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    protected void init() {
        super.init();
        this.requestMachineStateFromServer();
    }

    @Override
    public void render(final @NotNull GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.renderRepairTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(final @NotNull GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(this.texture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.renderMachineContents(guiGraphics, partialTick, mouseX, mouseY);
        this.renderRepairIcon(guiGraphics);
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

    protected final void requestMachineStateFromServer() {
        if (this.menu.machine() == null || this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        HbmPacketHandler.CHANNEL.sendToServer(new MachineStateRequestPacket(this.menu.machine().getBlockPos()));
    }

    protected final boolean inside(final double mouseX, final double mouseY, final int x, final int y, final int width, final int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private void renderRepairIcon(final GuiGraphics guiGraphics) {
        if (!this.menu.isMaintenanceBlocked()) {
            return;
        }
        final int x = this.repairIconX();
        final int y = this.repairIconY();
        guiGraphics.fill(x, y, x + REPAIR_ICON_SIZE, y + REPAIR_ICON_SIZE, 0xFFC02020);
        guiGraphics.drawString(this.font, "!", x + 2, y, 0xFFFFFFFF, false);
    }

    private void renderRepairTooltip(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (!this.menu.isMaintenanceBlocked()) {
            return;
        }
        final int x = this.repairIconX();
        final int y = this.repairIconY();
        if (!this.inside(mouseX, mouseY, x, y, REPAIR_ICON_SIZE, REPAIR_ICON_SIZE)) {
            return;
        }
        final List<Component> tooltip = new java.util.ArrayList<>();
        tooltip.add(Component.literal("Machine damaged").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Use a blowtorch to repair."));
        final List<net.minecraft.world.item.ItemStack> materials = this.menu.repairMaterials();
        if (!materials.isEmpty()) {
            tooltip.add(Component.literal("Required materials:").withStyle(ChatFormatting.GOLD));
            for (final net.minecraft.world.item.ItemStack stack : materials) {
                tooltip.add(Component.literal("- " + stack.getHoverName().getString() + " x" + stack.getCount()));
            }
        }
        guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
    }

    private int repairIconX() {
        return this.leftPos + this.imageWidth - 11;
    }

    private int repairIconY() {
        return this.topPos + 6;
    }

    protected final void renderVerticalEnergyBar(final GuiGraphics guiGraphics,
                                                 final ResourceLocation texture,
                                                 final int x,
                                                 final int y,
                                                 final int width,
                                                 final int height,
                                                 final int textureU,
                                                 final int textureV,
                                                 final long energy,
                                                 final long maxEnergy) {
        if (maxEnergy <= 0 || energy <= 0) {
            return;
        }
        final int clamped = (int) Math.max(0L, Math.min(height, energy * height / Math.max(1L, maxEnergy)));
        if (clamped <= 0) {
            return;
        }
        guiGraphics.blit(texture, x, y + height - clamped, textureU, textureV + height - clamped, width, clamped);
    }

    protected final void renderEnergyTooltip(final GuiGraphics guiGraphics,
                                             final int mouseX,
                                             final int mouseY,
                                             final int x,
                                             final int y,
                                             final int width,
                                             final int height,
                                             final long energy,
                                             final long maxEnergy) {
        if (this.inside(mouseX, mouseY, x, y, width, height)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal(energy + " / " + Math.max(0L, maxEnergy) + " HE")),
                Optional.empty(), mouseX, mouseY);
        }
    }

    protected final void renderHorizontalFluidBar(final GuiGraphics guiGraphics,
                                                  final int x,
                                                  final int y,
                                                  final int width,
                                                  final int height,
                                                  final FluidTank tank,
                                                  final int color) {
        if (tank == null || tank.getFluidAmount() <= 0 || tank.getCapacity() <= 0) {
            return;
        }
        final int fill = Math.max(1, Math.min(width, tank.getFluidAmount() * width / tank.getCapacity()));
        guiGraphics.fill(x, y, x + fill, y + height, color);
    }

    protected final void renderHorizontalFluidBar(final GuiGraphics guiGraphics,
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
        final int fill = Math.max(1, Math.min(width, amount * width / capacity));
        guiGraphics.fill(x, y, x + fill, y + height, color);
    }

    protected final void renderFluidTooltip(final GuiGraphics guiGraphics,
                                            final int mouseX,
                                            final int mouseY,
                                            final int x,
                                            final int y,
                                            final int width,
                                            final int height,
                                            final String title,
                                            final FluidTank tank) {
        if (!this.inside(mouseX, mouseY, x, y, width, height)) {
            return;
        }

        final List<Component> tooltip = new java.util.ArrayList<>();
        tooltip.add(Component.literal(title));
        if (tank == null || tank.getFluidAmount() <= 0) {
            tooltip.add(Component.literal("Empty"));
        } else {
            final FluidStack fluid = tank.getFluid();
            tooltip.add(fluid.getDisplayName());
            tooltip.add(Component.literal(fluid.getAmount() + " / " + tank.getCapacity() + " mB"));
        }
        guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
    }

    protected final void renderFluidTooltip(final GuiGraphics guiGraphics,
                                            final int mouseX,
                                            final int mouseY,
                                            final int x,
                                            final int y,
                                            final int width,
                                            final int height,
                                            final String title,
                                            final String fluidName,
                                            final int amount,
                                            final int capacity) {
        if (!this.inside(mouseX, mouseY, x, y, width, height)) {
            return;
        }
        final List<Component> tooltip = new java.util.ArrayList<>();
        tooltip.add(Component.literal(title));
        if (amount <= 0) {
            tooltip.add(Component.literal("Empty"));
        } else {
            tooltip.add(Component.literal(fluidName == null || fluidName.isBlank() ? "Unknown" : fluidName));
            tooltip.add(Component.literal(amount + " / " + Math.max(0, capacity) + " mB"));
        }
        guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
    }

    protected abstract ResourceLocation texture();
}

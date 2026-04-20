package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.client.debug.HbmDebugHitboxTracker;
import com.hbm.ntm.common.menu.MachineMenuBase;
import com.hbm.ntm.common.item.MachineUpgradeItem;
import com.hbm.ntm.common.network.HbmPacketHandler;
import com.hbm.ntm.common.network.MachineControlPacket;
import com.hbm.ntm.common.network.MachineStateRequestPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@SuppressWarnings("null")
public abstract class MachineScreenBase<T extends MachineMenuBase<?>> extends AbstractContainerScreen<T> {
    private static final ResourceLocation GUI_UTILITY_TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/gui_utility.png");
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
    }

    @Override
    protected void renderBg(final @NotNull GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(this.texture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.renderMachineContents(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(final @NotNull GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (this.shouldRenderTitle()) {
            guiGraphics.drawString(this.font, this.title, this.titleLabelX(), this.titleLabelY(), this.titleLabelColor(), false);
        }
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX(), this.inventoryLabelY(), this.inventoryLabelColor(), false);
        this.renderRepairIcon(guiGraphics);
        this.renderMachineLabels(guiGraphics, mouseX, mouseY);
    }

    private void renderRepairIcon(final GuiGraphics guiGraphics) {
        if (!this.menu.isMaintenanceBlocked()) {
            return;
        }
        final int x = this.imageWidth - REPAIR_ICON_SIZE - 4;
        final int y = 4;
        guiGraphics.fill(x, y, x + REPAIR_ICON_SIZE, y + REPAIR_ICON_SIZE, 0xFFC02020);
        guiGraphics.drawString(this.font, "!", x + 2, y, 0xFFFFFFFF, false);
    }

    protected boolean shouldRenderTitle() {
        return true;
    }

    protected int titleLabelX() {
        if (this.titleLabelX != 8) {
            return this.titleLabelX;
        }
        return Math.max(8, (this.imageWidth - this.font.width(this.title)) / 2);
    }

    protected int titleLabelY() {
        return this.titleLabelY;
    }

    protected int titleLabelColor() {
        return 0x404040;
    }

    protected int inventoryLabelX() {
        return this.inventoryLabelX;
    }

    protected int inventoryLabelY() {
        return this.inventoryLabelY;
    }

    protected int inventoryLabelColor() {
        return 0x404040;
    }

    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
    }

    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
    }

    protected final void renderMachineTooltip(final GuiGraphics guiGraphics,
                                              final List<Component> tooltip,
                                              final int mouseX,
                                              final int mouseY) {
        if (tooltip == null || tooltip.isEmpty()) {
            return;
        }
        guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
    }

    protected final void renderMachineTooltip(final GuiGraphics guiGraphics,
                                              final ItemStack stack,
                                              final int mouseX,
                                              final int mouseY) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
    }

    protected final void sendControl(final CompoundTag data) {
        if (this.menu.machine() == null || this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        HbmPacketHandler.CHANNEL.sendToServer(new MachineControlPacket(this.menu.machine().getBlockPos(), data));
    }

    protected final void sendToggleControl(final String key) {
        if (key == null || key.isBlank()) {
            return;
        }
        final CompoundTag data = new CompoundTag();
        data.putBoolean(key, true);
        this.sendControl(data);
    }

    protected final void sendIntControl(final String key, final int value) {
        if (key == null || key.isBlank()) {
            return;
        }
        final CompoundTag data = new CompoundTag();
        data.putInt(key, value);
        this.sendControl(data);
    }

    protected final void sendPatternSlotControl(final int slot, final ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        final CompoundTag data = new CompoundTag();
        data.putInt("slot", slot);
        data.put("stack", stack.copy().save(new CompoundTag()));
        this.sendControl(data);
    }

    protected final boolean handleToggleControlClick(final double mouseX,
                                                     final double mouseY,
                                                     final int x,
                                                     final int y,
                                                     final int width,
                                                     final int height,
                                                     final String key) {
        if (!this.inside(mouseX, mouseY, x, y, width, height)) {
            return false;
        }
        this.playButtonClick();
        this.sendToggleControl(key);
        return true;
    }

    protected final boolean handleLegacyInfoPanelControlClick(final double mouseX,
                                                              final double mouseY,
                                                              final int x,
                                                              final int y,
                                                              final int type,
                                                              final String key) {
        final LegacyInfoPanelSprite sprite = this.infoPanelSprite(type);
        if (!this.inside(mouseX, mouseY, x, y, sprite.width(), sprite.height())) {
            return false;
        }
        this.playButtonClick();
        this.sendToggleControl(key);
        return true;
    }

    protected final void playButtonClick() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    protected final void renderConfigToggleIndicator(final GuiGraphics guiGraphics,
                                                     final int x,
                                                     final int y,
                                                     final int width,
                                                     final int height,
                                                     final boolean enabled) {
        guiGraphics.fill(x, y, x + width, y + height, 0xFF202020);
        final int fillColor = enabled ? 0xFF2D8F42 : 0xFF8A2E2E;
        guiGraphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, fillColor);
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
        HbmDebugHitboxTracker.record("energy", x, y, width, height, HbmDebugHitboxTracker.COLOR_ENERGY);
        if (this.inside(mouseX, mouseY, x, y, width, height)) {
            this.renderMachineTooltip(guiGraphics,
                List.of(Component.literal(this.formatShortNumber(energy) + " / " + this.formatShortNumber(Math.max(0L, maxEnergy)) + " HE")),
                mouseX,
                mouseY);
        }
    }

    protected final void renderLegacyInfoPanel(final GuiGraphics guiGraphics,
                                               final int x,
                                               final int y,
                                               final int type) {
        final LegacyInfoPanelSprite sprite = this.infoPanelSprite(type);
        guiGraphics.blit(GUI_UTILITY_TEXTURE, x, y, sprite.u(), sprite.v(), sprite.width(), sprite.height(), 64, 64);
    }

    protected final void renderLegacyInfoPanelTooltip(final GuiGraphics guiGraphics,
                                                      final int mouseX,
                                                      final int mouseY,
                                                      final int x,
                                                      final int y,
                                                      final int type,
                                                      final List<Component> tooltip) {
        if (tooltip == null || tooltip.isEmpty()) {
            return;
        }

        final LegacyInfoPanelSprite sprite = this.infoPanelSprite(type);
        HbmDebugHitboxTracker.record("info-panel-" + type, x, y, sprite.width(), sprite.height(), HbmDebugHitboxTracker.COLOR_INFO_PANEL);
        if (!this.inside(mouseX, mouseY, x, y, sprite.width(), sprite.height())) {
            return;
        }

        this.renderMachineTooltip(guiGraphics, tooltip, mouseX, mouseY);
    }

    protected final void renderGhostSlotItem(final GuiGraphics guiGraphics,
                                             final @Nullable Slot slot,
                                             final ItemStack stack) {
        if (slot == null || slot.hasItem() || stack == null || stack.isEmpty()) {
            return;
        }

        final int x = this.leftPos + slot.x;
        final int y = this.topPos + slot.y;
        guiGraphics.renderFakeItem(stack, x, y);
        guiGraphics.fill(x, y, x + 16, y + 16, 0x55FFFFFF);
    }

    protected final void renderUpgradeInfoTooltip(final GuiGraphics guiGraphics,
                                                  final int mouseX,
                                                  final int mouseY,
                                                  final int x,
                                                  final int y,
                                                  final int width,
                                                  final int height) {
        HbmDebugHitboxTracker.record("upgrade", x, y, width, height, HbmDebugHitboxTracker.COLOR_UPGRADE);
        if (!this.inside(mouseX, mouseY, x, y, width, height) || this.menu.machine() == null) {
            return;
        }

        final Map<MachineUpgradeItem.UpgradeType, Integer> valid = this.menu.machine().getValidUpgrades();
        if (valid == null || valid.isEmpty()) {
            return;
        }

        final List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("Upgrades"));
        for (final MachineUpgradeItem.UpgradeType type : MachineUpgradeItem.UpgradeType.values()) {
            final int max = Math.max(0, valid.getOrDefault(type, 0));
            if (max <= 0) {
                continue;
            }
            tooltip.add(Component.literal(String.format(Locale.ROOT, "%s: %d", this.upgradeTypeLabel(type), max)));
        }

        this.renderMachineTooltip(guiGraphics, tooltip.size() > 1 ? tooltip : List.of(), mouseX, mouseY);
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

    protected final void renderVerticalFluidGaugeBar(final GuiGraphics guiGraphics,
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

    protected final void renderSmoothNeedleGauge(final GuiGraphics guiGraphics,
                                                 final int centerX,
                                                 final int centerY,
                                                 final double progress,
                                                 final double tipLength,
                                                 final double backLength,
                                                 final double backSide,
                                                 final int innerColor,
                                                 final int outerColor) {
        final double clamped = Math.max(0.0D, Math.min(1.0D, progress));
        final double angle = Math.toRadians(-clamped * 270.0D - 45.0D);

        final double tipX = -tipLength * Math.sin(angle);
        final double tipY = tipLength * Math.cos(angle);

        final double leftX = backSide * Math.cos(angle) + backLength * Math.sin(angle);
        final double leftY = backSide * Math.sin(angle) - backLength * Math.cos(angle);

        final double rightX = -backSide * Math.cos(angle) + backLength * Math.sin(angle);
        final double rightY = -backSide * Math.sin(angle) - backLength * Math.cos(angle);

        final Matrix4f matrix = guiGraphics.pose().last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        this.drawGaugeTriangle(matrix,
            centerX + tipX * 1.5D,
            centerY + tipY * 1.5D,
            centerX + leftX * 1.5D,
            centerY + leftY * 1.5D,
            centerX + rightX * 1.5D,
            centerY + rightY * 1.5D,
            outerColor);

        this.drawGaugeTriangle(matrix,
            centerX + tipX,
            centerY + tipY,
            centerX + leftX,
            centerY + leftY,
            centerX + rightX,
            centerY + rightY,
            innerColor);

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    protected final String formatShortNumber(final long value) {
        if (value >= 1_000_000_000_000_000L) {
            return this.scaledShortNumber(value, 1_000_000_000_000_000D, "Q");
        }
        if (value >= 1_000_000_000_000L) {
            return this.scaledShortNumber(value, 1_000_000_000_000D, "T");
        }
        if (value >= 1_000_000_000L) {
            return this.scaledShortNumber(value, 1_000_000_000D, "B");
        }
        if (value >= 1_000_000L) {
            return this.scaledShortNumber(value, 1_000_000D, "M");
        }
        if (value >= 1_000L) {
            return this.scaledShortNumber(value, 1_000D, "K");
        }
        return Long.toString(value);
    }

    protected final void renderFluidTooltip(final GuiGraphics guiGraphics,
                                            final int mouseX,
                                            final int mouseY,
                                            final int x,
                                            final int y,
                                            final int width,
                                            final int height,
                                            final String fluidName,
                                            final int amount,
                                            final int capacity) {
        HbmDebugHitboxTracker.record("fluid:" + (fluidName == null || fluidName.isBlank() ? "empty" : fluidName),
            x, y, width, height, HbmDebugHitboxTracker.COLOR_FLUID);
        if (!this.inside(mouseX, mouseY, x, y, width, height)) {
            return;
        }
        final List<Component> tooltip = new java.util.ArrayList<>();
        final Component name = fluidName == null || fluidName.isBlank()
            ? Component.translatable("hbmfluid.none")
            : Component.literal(fluidName);
        tooltip.add(name);
        tooltip.add(Component.literal(amount + "/" + Math.max(0, capacity) + "mB"));
        this.renderMachineTooltip(guiGraphics, tooltip, mouseX, mouseY);
    }

    protected final boolean renderBurnBonusTooltipOverEmptyFuelSlot(final GuiGraphics guiGraphics,
                                                                    final int mouseX,
                                                                    final int mouseY,
                                                                    final int firstSlotIndex,
                                                                    final int lastSlotIndex,
                                                                    final List<Component> bonuses) {
        if (bonuses == null || bonuses.isEmpty()) {
            return false;
        }
        if (this.minecraft == null || this.minecraft.player == null) {
            return false;
        }
        if (!this.minecraft.player.containerMenu.getCarried().isEmpty()) {
            return false;
        }
        for (int i = firstSlotIndex; i <= lastSlotIndex; i++) {
            final Slot slot = this.menu.getSlot(i);
            if (slot.hasItem()) {
                continue;
            }
            if (this.inside(mouseX, mouseY, this.leftPos + slot.x, this.topPos + slot.y, 16, 16)) {
                this.renderMachineTooltip(guiGraphics, bonuses, mouseX, mouseY);
                return true;
            }
        }
        return false;
    }

    protected abstract ResourceLocation texture();

    private LegacyInfoPanelSprite infoPanelSprite(final int type) {
        return switch (type) {
            case 1 -> new LegacyInfoPanelSprite(0, 8, 8, 8);
            case 2 -> new LegacyInfoPanelSprite(8, 0, 16, 16);
            case 3 -> new LegacyInfoPanelSprite(24, 0, 16, 16);
            case 4 -> new LegacyInfoPanelSprite(0, 16, 8, 8);
            case 5 -> new LegacyInfoPanelSprite(0, 24, 8, 8);
            case 6 -> new LegacyInfoPanelSprite(8, 16, 16, 16);
            case 7 -> new LegacyInfoPanelSprite(24, 16, 16, 16);
            case 8 -> new LegacyInfoPanelSprite(0, 32, 8, 8);
            case 9 -> new LegacyInfoPanelSprite(0, 40, 8, 8);
            case 10 -> new LegacyInfoPanelSprite(8, 32, 16, 16);
            case 11 -> new LegacyInfoPanelSprite(24, 32, 16, 16);
            default -> new LegacyInfoPanelSprite(0, 0, 8, 8);
        };
    }

    private String scaledShortNumber(final long value, final double divisor, final String suffix) {
        final double result = Math.round(value / divisor * 100.0D) / 100.0D;
        return result + suffix;
    }

    private void drawGaugeTriangle(final Matrix4f matrix,
                                   final double x1,
                                   final double y1,
                                   final double x2,
                                   final double y2,
                                   final double x3,
                                   final double y3,
                                   final int argbColor) {
        final int a = (argbColor >>> 24) & 0xFF;
        final int r = (argbColor >>> 16) & 0xFF;
        final int g = (argbColor >>> 8) & 0xFF;
        final int b = argbColor & 0xFF;

        final Tesselator tesselator = Tesselator.getInstance();
        final BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix, (float) x1, (float) y1, 0.0F).color(r, g, b, a).endVertex();
        builder.vertex(matrix, (float) x2, (float) y2, 0.0F).color(r, g, b, a).endVertex();
        builder.vertex(matrix, (float) x3, (float) y3, 0.0F).color(r, g, b, a).endVertex();
        BufferUploader.drawWithShader(builder.end());
    }

    private record LegacyInfoPanelSprite(int u, int v, int width, int height) {
    }

    private String upgradeTypeLabel(final MachineUpgradeItem.UpgradeType type) {
        return switch (type) {
            case SPEED -> "Speed";
            case POWER -> "Power";
            case EFFECT -> "Effect";
            case FORTUNE -> "Fortune";
            case OVERDRIVE -> "Overdrive";
            case AFTERBURN -> "Afterburn";
            case SPECIAL -> "Special";
            case RADIUS -> "Radius";
            case HEALTH -> "Health";
            case SMELTER -> "Smelter";
            case SHREDDER -> "Shredder";
            case CENTRIFUGE -> "Centrifuge";
            case CRYSTALLIZER -> "Crystallizer";
            case NULLIFIER -> "Nullifier";
            case SCREM -> "Screm";
            case GC_SPEED -> "Gas Speed";
        };
    }
}

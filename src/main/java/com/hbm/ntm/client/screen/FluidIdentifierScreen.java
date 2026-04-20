package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.fluid.HbmFluidType;
import com.hbm.ntm.common.item.FluidIdentifierItem;
import com.hbm.ntm.common.menu.FluidIdentifierMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("null")
public class FluidIdentifierScreen extends AbstractContainerScreen<FluidIdentifierMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/machine/gui_fluid.png");
    private EditBox searchBox;
    private List<ResourceLocation> matches = List.of();

    public FluidIdentifierScreen(final FluidIdentifierMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 54;
    }

    @Override
    protected void init() {
        super.init();
        this.searchBox = new EditBox(this.font, this.leftPos + 46, this.topPos + 11, 86, 12, Component.empty());
        this.searchBox.setResponder(ignored -> this.updateMatches());
        this.searchBox.setMaxLength(64);
        this.searchBox.setBordered(false);
        this.searchBox.setTextColor(0xFFFFFF);
        this.addRenderableWidget(this.searchBox);
        this.setInitialFocus(this.searchBox);
        this.updateMatches();
    }

    @Override
    protected void renderBg(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.searchBox != null && this.searchBox.isFocused()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 43, this.topPos + 7, 166, 54, 90, 18);
        }

        final ItemStack stack = this.menu.getIdentifierStack();
        ResourceLocation primary = null;
        ResourceLocation secondary = null;
        if (stack.getItem() instanceof final FluidIdentifierItem item) {
            primary = item.getPrimaryFluidId(stack);
            secondary = item.getSecondaryFluidId(stack);
        }

        for (int i = 0; i < Math.min(9, this.matches.size()); i++) {
            final ResourceLocation fluidId = this.matches.get(i);
                final int color = fluidColor(fluidId);
                RenderSystem.setShaderColor(
                    ((color >> 16) & 0xFF) / 255.0F,
                    ((color >> 8) & 0xFF) / 255.0F,
                    (color & 0xFF) / 255.0F,
                    ((color >> 24) & 0xFF) / 255.0F);
                guiGraphics.blit(TEXTURE, this.leftPos + 12 + i * 18, this.topPos + 31, 12 + i * 18, 56, 8, 14);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (fluidId.equals(primary) && fluidId.equals(secondary)) {
                guiGraphics.blit(TEXTURE, this.leftPos + 7 + i * 18, this.topPos + 29, 176, 36, 18, 18);
            } else if (fluidId.equals(primary)) {
                guiGraphics.blit(TEXTURE, this.leftPos + 7 + i * 18, this.topPos + 29, 176, 0, 18, 18);
            } else if (fluidId.equals(secondary)) {
                guiGraphics.blit(TEXTURE, this.leftPos + 7 + i * 18, this.topPos + 29, 176, 18, 18, 18);
            }
        }
    }

    @Override
    public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        for (int i = 0; i < Math.min(9, this.matches.size()); i++) {
            final ResourceLocation fluidId = this.matches.get(i);
            final int x = this.leftPos + 7 + i * 18;
            final int y = this.topPos + 29;
            if (mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18) {
                guiGraphics.renderTooltip(this.font, fluidTooltip(fluidId), java.util.Optional.empty(),
                    mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.searchBox.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        for (int i = 0; i < Math.min(9, this.matches.size()); i++) {
            if (mouseX >= this.leftPos + 7 + i * 18 && mouseX < this.leftPos + 25 + i * 18 && mouseY >= this.topPos + 29 && mouseY < this.topPos + 47) {
                final int fluidIndex = FluidIdentifierMenu.orderedSelectableFluids().indexOf(this.matches.get(i));
                if (fluidIndex >= 0) {
                    if (this.minecraft == null || this.minecraft.gameMode == null) {
                        return true;
                    }
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, fluidIndex * 2 + (button == 1 ? 1 : 0));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers) || this.searchBox.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(final char codePoint, final int modifiers) {
        if (this.searchBox.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    private void updateMatches() {
        final String query = this.searchBox == null ? "" : this.searchBox.getValue().toLowerCase(Locale.ROOT);
        final List<ResourceLocation> result = new ArrayList<>();
        for (final ResourceLocation fluidId : FluidIdentifierMenu.orderedSelectableFluids()) {
            if (FluidIdentifierMenu.displayName(fluidId).toLowerCase(Locale.ROOT).contains(query)) {
                result.add(fluidId);
                if (result.size() >= 9) {
                    break;
                }
            }
        }
        this.matches = result;
    }

    private static int fluidColor(final ResourceLocation fluidId) {
        if (!ForgeRegistries.FLUIDS.containsKey(fluidId)) {
            return 0xFFFFFFFF;
        }
        final var fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null) {
            return 0xFFFFFFFF;
        }
        final var fluidType = fluid.getFluidType();
        final int tint = fluidType instanceof HbmFluidType hbmFluidType ? hbmFluidType.getTintColor() : 0xFFFFFFFF;
        return tint | 0xFF000000;
    }

    private static List<Component> fluidTooltip(final ResourceLocation fluidId) {
        final List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(FluidIdentifierMenu.displayName(fluidId)));

        final var fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null) {
            return tooltip;
        }

        final var fluidType = fluid.getFluidType();
        if (fluidType instanceof final HbmFluidType hbmFluidType) {
            if (hbmFluidType.isHot()) {
                tooltip.add(Component.literal("Hot").withStyle(ChatFormatting.RED));
            }
            final int corrosive = hbmFluidType.getCorrosiveRating();
            if (corrosive > 0) {
                final ChatFormatting color = corrosive > 50 ? ChatFormatting.DARK_RED : ChatFormatting.GOLD;
                tooltip.add(Component.literal("Corrosive " + corrosive).withStyle(color));
            }
            if (hbmFluidType.isAntimatter()) {
                tooltip.add(Component.literal("Antimatter").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }

        return tooltip;
    }
}


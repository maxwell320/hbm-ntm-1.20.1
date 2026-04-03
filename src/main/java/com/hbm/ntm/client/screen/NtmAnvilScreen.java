package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.anvil.HbmAnvilRecipes;
import com.hbm.ntm.common.anvil.NtmAnvilMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class NtmAnvilScreen extends AbstractContainerScreen<NtmAnvilMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_anvil.png");
    private static final int RECIPES_PER_PAGE = 10;
    private final List<Integer> filteredRecipeIndexes = new ArrayList<>();
    private int page;
    private int selectedRecipe = -1;
    private EditBox searchBox;

    public NtmAnvilScreen(final NtmAnvilMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = 128;
    }

    @Override
    protected void init() {
        super.init();
        this.searchBox = new EditBox(this.font, this.leftPos + 10, this.topPos + 111, 84, 12, Component.empty());
        this.searchBox.setTextColor(0xFFFFFF);
        this.searchBox.setTextColorUneditable(0xFFFFFF);
        this.searchBox.setBordered(false);
        this.searchBox.setMaxLength(25);
        addWidget(this.searchBox);
        rebuildRecipeFilter();
        clampPage();
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.searchBox != null) {
            this.searchBox.tick();
        }
    }

    @Override
    public void render(final @NotNull GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderRecipeTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(final @NotNull GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.searchBox != null && this.searchBox.isFocused()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 108, 168, 222, 88, 16);
        }

        if (inside(mouseX, mouseY, this.leftPos + 7, this.topPos + 71, 9, 36)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 7, this.topPos + 71, 176, 186, 9, 36);
        }
        if (inside(mouseX, mouseY, this.leftPos + 106, this.topPos + 71, 9, 36)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 71, 185, 186, 9, 36);
        }
        if (inside(mouseX, mouseY, this.leftPos + 52, this.topPos + 53, 18, 18)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 53, 176, 150, 18, 18);
        }

        final int start = this.page * 2;
        for (int i = start; i < start + RECIPES_PER_PAGE; i++) {
            if (i >= this.filteredRecipeIndexes.size()) {
                break;
            }
            final int recipeIndex = this.filteredRecipeIndexes.get(i);
            final HbmAnvilRecipes.ConstructionRecipe recipe = this.menu.constructionRecipes().get(recipeIndex);
            final int relative = i - start;
            final int x = this.leftPos + 16 + 18 * (relative / 2);
            final int y = this.topPos + 71 + 18 * (relative % 2);
            guiGraphics.renderItem(recipe.outputStack(), x + 1, y + 1);
            if (recipeIndex == this.selectedRecipe) {
                guiGraphics.blit(TEXTURE, x, y, 0, 222, 18, 18);
            }
        }
        if (this.searchBox != null) {
            this.searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    protected void renderLabels(final @NotNull GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        guiGraphics.drawString(this.font, this.title, 61 - this.font.width(this.title) / 2, 8, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);

        if (this.selectedRecipe >= 0 && this.selectedRecipe < this.menu.constructionRecipes().size()) {
            final HbmAnvilRecipes.ConstructionRecipe recipe = this.menu.constructionRecipes().get(this.selectedRecipe);
            final boolean hasInput = this.menu.countPlayerItem(recipe.inputStack()) >= recipe.inputStack().getCount();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            guiGraphics.drawString(this.font, Component.literal("Inputs:").withStyle(ChatFormatting.YELLOW), 260, 50, 0xFFFFFF, false);
            guiGraphics.drawString(this.font,
                Component.literal(">1x " + recipe.inputStack().getHoverName().getString()).withStyle(hasInput ? ChatFormatting.WHITE : ChatFormatting.RED),
                260, 59, 0xFFFFFF, false);
            guiGraphics.drawString(this.font, Component.literal("Output:").withStyle(ChatFormatting.YELLOW), 260, 77, 0xFFFFFF, false);
            guiGraphics.drawString(this.font, Component.literal(">1x " + recipe.outputStack().getHoverName().getString()), 260, 86, 0xFFFFFF, false);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.searchBox != null && this.searchBox.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (inside(mouseX, mouseY, this.leftPos + 7, this.topPos + 71, 9, 36)) {
            if (this.page > 0) {
                this.page--;
            }
            return true;
        }

        if (inside(mouseX, mouseY, this.leftPos + 106, this.topPos + 71, 9, 36)) {
            if (this.page < maxPage()) {
                this.page++;
            }
            return true;
        }

        if (inside(mouseX, mouseY, this.leftPos + 52, this.topPos + 53, 18, 18)) {
            if (this.selectedRecipe >= 0 && this.minecraft != null && this.minecraft.gameMode != null) {
                final int buttonId = hasShiftDown() ? this.selectedRecipe + 1000 : this.selectedRecipe;
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
            }
            return true;
        }

        final int start = this.page * 2;
        for (int i = start; i < start + RECIPES_PER_PAGE; i++) {
            if (i >= this.filteredRecipeIndexes.size()) {
                break;
            }
            final int recipeIndex = this.filteredRecipeIndexes.get(i);
            final int relative = i - start;
            final int x = this.leftPos + 16 + 18 * (relative / 2);
            final int y = this.topPos + 71 + 18 * (relative % 2);
            if (inside(mouseX, mouseY, x, y, 18, 18)) {
                this.selectedRecipe = this.selectedRecipe == recipeIndex ? -1 : recipeIndex;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double scrollDelta) {
        if (scrollDelta > 0.0D && this.page > 0) {
            this.page--;
            return true;
        }
        if (scrollDelta < 0.0D && this.page < maxPage()) {
            this.page++;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (this.searchBox != null && this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            rebuildRecipeFilter();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(final char codePoint, final int modifiers) {
        if (this.searchBox != null && this.searchBox.charTyped(codePoint, modifiers)) {
            rebuildRecipeFilter();
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    private void renderRecipeTooltip(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        final int start = this.page * 2;
        for (int i = start; i < start + RECIPES_PER_PAGE; i++) {
            if (i >= this.filteredRecipeIndexes.size()) {
                break;
            }
            final int recipeIndex = this.filteredRecipeIndexes.get(i);
            final int relative = i - start;
            final int x = this.leftPos + 16 + 18 * (relative / 2);
            final int y = this.topPos + 71 + 18 * (relative % 2);
            if (inside(mouseX, mouseY, x, y, 18, 18)) {
                final HbmAnvilRecipes.ConstructionRecipe recipe = this.menu.constructionRecipes().get(recipeIndex);
                guiGraphics.renderTooltip(this.font, recipe.outputStack(), mouseX, mouseY);
                return;
            }
        }
    }

    private void clampPage() {
        this.page = Mth.clamp(this.page, 0, maxPage());
        if (this.selectedRecipe >= this.menu.constructionRecipes().size() || (this.selectedRecipe >= 0 && !this.filteredRecipeIndexes.contains(this.selectedRecipe))) {
            this.selectedRecipe = -1;
        }
    }

    private int maxPage() {
        return Math.max(0, Mth.ceil((this.filteredRecipeIndexes.size() - RECIPES_PER_PAGE) / 2.0D));
    }

    private void rebuildRecipeFilter() {
        this.filteredRecipeIndexes.clear();
        final String query = this.searchBox == null ? "" : this.searchBox.getValue().toLowerCase(Locale.US);

        for (int i = 0; i < this.menu.constructionRecipes().size(); i++) {
            final HbmAnvilRecipes.ConstructionRecipe recipe = this.menu.constructionRecipes().get(i);
            if (query.isEmpty() || recipeMatchesQuery(recipe, query)) {
                this.filteredRecipeIndexes.add(i);
            }
        }

        clampPage();
    }

    private static boolean recipeMatchesQuery(final HbmAnvilRecipes.ConstructionRecipe recipe, final String query) {
        return recipe.inputStack().getHoverName().getString().toLowerCase(Locale.US).contains(query)
            || recipe.outputStack().getHoverName().getString().toLowerCase(Locale.US).contains(query);
    }

    private static boolean inside(final double mouseX, final double mouseY, final int x, final int y, final int width, final int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}

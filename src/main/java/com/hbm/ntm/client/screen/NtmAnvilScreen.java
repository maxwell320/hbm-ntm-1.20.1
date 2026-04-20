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
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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
    private int lastSize;

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

        final int slide = Mth.clamp(this.lastSize - 42, 0, 1000);
        int mul = 1;
        while (slide >= 51 * mul) {
            guiGraphics.blit(TEXTURE, this.leftPos + 125 + 51 * mul, this.topPos + 17, 125, 17, 54, 108);
            mul++;
        }
        guiGraphics.blit(TEXTURE, this.leftPos + 125 + slide, this.topPos + 17, 125, 17, 54, 108);

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
            guiGraphics.blit(TEXTURE, x, y, 18, 222, 18, 18);
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
            final List<Component> detailLines = this.recipeDetailLines(recipe);
            int longest = 0;
            for (final Component line : detailLines) {
                longest = Math.max(longest, this.font.width(line));
            }

            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            int lineY = 50;
            for (final Component line : detailLines) {
                guiGraphics.drawString(this.font, line, 260, lineY, 0xFFFFFF, false);
                lineY += 9;
            }
            guiGraphics.pose().popPose();

            this.lastSize = (int) (longest * 0.5D);
        } else {
            this.lastSize = 0;
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
            this.playButtonClick();
            if (this.page > 0) {
                this.page--;
            }
            return true;
        }

        if (inside(mouseX, mouseY, this.leftPos + 106, this.topPos + 71, 9, 36)) {
            this.playButtonClick();
            if (this.page < maxPage()) {
                this.page++;
            }
            return true;
        }

        if (inside(mouseX, mouseY, this.leftPos + 52, this.topPos + 53, 18, 18)) {
            if (this.selectedRecipe >= 0 && this.minecraft != null && this.minecraft.gameMode != null) {
                this.playButtonClick();
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
                this.playButtonClick();
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
        if (recipe.outputStack().getHoverName().getString().toLowerCase(Locale.US).contains(query)) {
            return true;
        }
        for (final HbmAnvilRecipes.ConstructionIngredient ingredient : recipe.ingredients()) {
            final var display = ingredient.displayStack();
            if (!display.isEmpty() && display.getHoverName().getString().toLowerCase(Locale.US).contains(query)) {
                return true;
            }
        }
        return false;
    }

    private static boolean inside(final double mouseX, final double mouseY, final int x, final int y, final int width, final int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private int infoPanelExtensionCount() {
        if (this.selectedRecipe < 0 || this.selectedRecipe >= this.menu.constructionRecipes().size()) {
            return 0;
        }
        final HbmAnvilRecipes.ConstructionRecipe recipe = this.menu.constructionRecipes().get(this.selectedRecipe);
        final int lineCount = recipe.ingredients().size() + 2;
        final int lastSize = 42 + lineCount * 9;
        int count = 0;
        while (lastSize - 42 >= 51 * (count + 1)) {
            count++;
        }
        return count;
    }

    private List<Component> recipeDetailLines(final HbmAnvilRecipes.ConstructionRecipe recipe) {
        final List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("Inputs:").withStyle(ChatFormatting.YELLOW));

        final long tick = this.minecraft == null ? 0L : this.minecraft.level == null ? 0L : this.minecraft.level.getGameTime();
        int offset = 0;
        for (final HbmAnvilRecipes.ConstructionIngredient ingredient : recipe.ingredients()) {
            final int available = this.menu.countPlayerItems(ingredient.ingredient());
            final boolean hasInput = available >= ingredient.count();
            final var display = this.cyclingDisplayStack(ingredient, tick + offset);
            final String name = display.isEmpty() ? "Unknown" : display.getHoverName().getString();
            lines.add(Component.literal(">" + ingredient.count() + "x " + name).withStyle(hasInput ? ChatFormatting.WHITE : ChatFormatting.RED));
            offset += 9;
        }

        lines.add(Component.literal(""));
        lines.add(Component.literal("Outputs:").withStyle(ChatFormatting.YELLOW));
        lines.add(Component.literal(">1x " + recipe.outputStack().getHoverName().getString()));
        return lines;
    }

    private net.minecraft.world.item.ItemStack cyclingDisplayStack(final HbmAnvilRecipes.ConstructionIngredient ingredient, final long tickSeed) {
        final net.minecraft.world.item.ItemStack[] options = ingredient.ingredient().getItems();
        if (options.length <= 0) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        final int index = (int) Math.floorMod(tickSeed / 20L, options.length);
        final net.minecraft.world.item.ItemStack display = options[index].copy();
        display.setCount(Math.max(1, ingredient.count()));
        return display;
    }

    private void playButtonClick() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}


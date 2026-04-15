package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.PurexBlockEntity;
import com.hbm.ntm.common.menu.PurexMenu;
import com.hbm.ntm.common.purex.HbmPurexRecipes;
import com.hbm.ntm.common.purex.HbmPurexRecipes.PurexRecipe;
import com.hbm.ntm.common.recipe.CountedIngredient;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("null")
public class PurexScreen extends MachineScreenBase<PurexMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_purex.png");

    public PurexScreen(final PurexMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 256);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            this.playButtonClick();
            this.cycleRecipe(-1);
        }).bounds(this.leftPos + 26, this.topPos + 125, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            this.playButtonClick();
            this.cycleRecipe(1);
        }).bounds(this.leftPos + 44, this.topPos + 125, 16, 16).build());
    }

    private void cycleRecipe(final int delta) {
        this.sendIntControl("cycleRecipe", delta);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics, TEXTURE,
            this.leftPos + 152,
            this.topPos + 18,
            16,
            61,
            176,
            0,
            this.menu.energy(),
            this.menu.maxEnergy());

        final int progressPixels = this.menu.processTime() <= 0 ? 0 : this.menu.progress() * 70 / this.menu.processTime();
        if (progressPixels > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 126, 176, 61, progressPixels, 16);
        }

        if (this.menu.canProcess()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 195, 0, 3, 6);
        } else if (this.menu.hasRecipe()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 192, 0, 3, 6);
        }

        if (this.menu.canProcess()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 195, 0, 3, 6);
        } else if (this.menu.hasRecipe() && this.menu.hasPower()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 192, 0, 3, 6);
        }

        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 8, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(0), this.menu.fluidCapacity(0), 0xFF4E8FD6);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 26, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(1), this.menu.fluidCapacity(1), 0xFF4E8FD6);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 44, this.topPos + 18, 16, 52,
            this.menu.fluidAmount(2), this.menu.fluidCapacity(2), 0xFF4E8FD6);
        this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 116, this.topPos + 36, 16, 52,
            this.menu.fluidAmount(3), this.menu.fluidCapacity(3), 0xFFE6C85C);

        this.resolveGhostRecipe().ifPresent(recipe -> {
            final int maxSlots = Math.min(3, recipe.itemInputs().size());
            for (int i = 0; i < maxSlots; i++) {
                final ItemStack display = displayStackFor(recipe.itemInputs().get(i));
                final int slotIndex = PurexBlockEntity.SLOT_INPUT_1 + i;
                final Slot slot = slotIndex < this.menu.slots.size() ? this.menu.slots.get(slotIndex) : null;
                this.renderGhostSlotItem(guiGraphics, slot, display);
            }
        });
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 152,
            this.topPos + 18,
            16,
            61,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 8, this.topPos + 18, 16, 52,
            "Input Tank A", this.menu.fluidName(0), this.menu.fluidAmount(0), this.menu.fluidCapacity(0));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 26, this.topPos + 18, 16, 52,
            "Input Tank B", this.menu.fluidName(1), this.menu.fluidAmount(1), this.menu.fluidCapacity(1));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 44, this.topPos + 18, 16, 52,
            "Input Tank C", this.menu.fluidName(2), this.menu.fluidAmount(2), this.menu.fluidCapacity(2));
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 116, this.topPos + 36, 16, 52,
            "Output Tank", this.menu.fluidName(3), this.menu.fluidAmount(3), this.menu.fluidCapacity(3));

        final int recipeCount = this.menu.recipeCount();
        final int recipeIndex = this.menu.recipeIndex();
        final String recipeCounter = recipeCount <= 0 ? "0/0" : (Math.max(0, recipeIndex) + 1) + "/" + recipeCount;
        guiGraphics.drawString(this.font, recipeCounter, 62, 131, 0x404040, false);

        if (this.inside(mouseX, mouseY, this.leftPos + 62, this.topPos + 126, 70, 16)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("Consumption: " + this.menu.consumption() + " HE/t")),
                Optional.empty(),
                mouseX,
                mouseY);
        }

        if (this.inside(mouseX, mouseY, this.leftPos + 7, this.topPos + 125, 18, 18)) {
            final Optional<PurexRecipe> recipe = this.resolveGhostRecipe();
            if (recipe.isPresent()) {
                guiGraphics.renderTooltip(this.font, this.recipeTooltip(recipe.get()), Optional.empty(), mouseX, mouseY);
            } else {
                guiGraphics.renderTooltip(this.font,
                    List.of(Component.literal("Set recipe").withStyle(ChatFormatting.YELLOW)),
                    Optional.empty(),
                    mouseX,
                    mouseY);
            }
        }

        this.renderUpgradeInfoTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 152, this.topPos + 108, 36, 18);
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }

    private List<Component> recipeTooltip(final PurexRecipe recipe) {
        final List<Component> tooltip = new java.util.ArrayList<>();
        tooltip.add(Component.literal(recipe.id()));
        tooltip.add(Component.literal(recipe.duration() + " t"));
        tooltip.add(Component.literal(recipe.powerPerTick() + " HE/t"));

        for (final CountedIngredient requirement : recipe.itemInputs()) {
            final ItemStack display = displayStackFor(requirement);
            if (!display.isEmpty()) {
                tooltip.add(Component.literal(requirement.count() + "x " + display.getHoverName().getString()));
            }
        }

        for (final FluidStack input : recipe.fluidInputs()) {
            if (!input.isEmpty()) {
                tooltip.add(Component.literal("In: " + input.getAmount() + " mB " + input.getDisplayName().getString()));
            }
        }

        for (final ItemStack output : recipe.itemOutputs()) {
            if (!output.isEmpty()) {
                tooltip.add(Component.literal("Out: " + output.getCount() + "x " + output.getHoverName().getString()));
            }
        }

        for (final FluidStack output : recipe.fluidOutputs()) {
            if (!output.isEmpty()) {
                tooltip.add(Component.literal("Out: " + output.getAmount() + " mB " + output.getDisplayName().getString()));
            }
        }

        return tooltip;
    }

    private Optional<PurexRecipe> resolveGhostRecipe() {
        final String recipeId = this.menu.recipeId();
        if (!recipeId.isBlank()) {
            return HbmPurexRecipes.findById(recipeId);
        }

        final List<PurexRecipe> allRecipes = HbmPurexRecipes.all();
        if (allRecipes.size() == 1) {
            return Optional.of(allRecipes.get(0));
        }

        return Optional.empty();
    }

    private static ItemStack displayStackFor(final CountedIngredient ingredient) {
        final ItemStack[] options = ingredient.ingredient().getItems();
        if (options.length <= 0) {
            return ItemStack.EMPTY;
        }

        final ItemStack display = options[0].copy();
        display.setCount(Math.max(1, ingredient.count()));
        return display;
    }

}

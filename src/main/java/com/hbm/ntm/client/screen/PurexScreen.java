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

        this.renderTankInfo(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 18, 16, 52, 0);
        this.renderTankInfo(guiGraphics, mouseX, mouseY, this.leftPos + 26, this.topPos + 18, 16, 52, 1);
        this.renderTankInfo(guiGraphics, mouseX, mouseY, this.leftPos + 44, this.topPos + 18, 16, 52, 2);
        this.renderTankInfo(guiGraphics, mouseX, mouseY, this.leftPos + 116, this.topPos + 36, 16, 52, 3);

        if (this.inside(mouseX, mouseY, this.leftPos + 7, this.topPos + 125, 18, 18)) {
            final Optional<PurexRecipe> recipe = this.resolveGhostRecipe();
            if (recipe.isPresent()) {
                this.renderMachineTooltip(guiGraphics, this.recipeTooltip(recipe.get()), mouseX, mouseY);
            } else {
                this.renderMachineTooltip(guiGraphics, List.of(Component.literal("Set recipe").withStyle(ChatFormatting.YELLOW)), mouseX, mouseY);
            }
        }

    }

    private void renderTankInfo(final GuiGraphics guiGraphics,
                                final int mouseX,
                                final int mouseY,
                                final int x,
                                final int y,
                                final int width,
                                final int height,
                                final int tank) {
        if (!this.inside(mouseX, mouseY, x, y, width, height)) {
            return;
        }
        final List<Component> tooltip = new java.util.ArrayList<>();
        if (this.menu.fluidAmount(tank) <= 0) {
            tooltip.add(Component.translatable("hbmfluid.none"));
        } else {
            tooltip.add(Component.literal(this.menu.fluidName(tank)));
            tooltip.add(Component.literal(this.menu.fluidAmount(tank) + "/" + this.menu.fluidCapacity(tank) + "mB"));
        }
        this.renderMachineTooltip(guiGraphics, tooltip, mouseX, mouseY);
    }

    @Override
    protected int titleLabelX() {
        return 70 - this.font.width(this.title) / 2;
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (button == 1 && this.inside(mouseX, mouseY, this.leftPos + 7, this.topPos + 125, 18, 18)) {
            this.playButtonClick();
            this.cycleRecipe(1);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
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


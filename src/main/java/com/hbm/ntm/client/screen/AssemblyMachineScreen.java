package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.assembly.HbmAssemblyRecipes;
import com.hbm.ntm.common.block.entity.AssemblyMachineBlockEntity;
import com.hbm.ntm.common.menu.AssemblyMachineMenu;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("null")
public class AssemblyMachineScreen extends MachineScreenBase<AssemblyMachineMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/processing/gui_assembler.png");

    public AssemblyMachineScreen(final AssemblyMachineMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 256);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
                this.playButtonClick();
                this.cycleRecipe(-1);
            })
            .bounds(this.leftPos + 26, this.topPos + 125, 16, 16)
            .build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
                this.playButtonClick();
                this.cycleRecipe(1);
            })
            .bounds(this.leftPos + 44, this.topPos + 125, 16, 16)
            .build());
    }

    private void cycleRecipe(final int delta) {
        this.sendIntControl("cycleRecipe", delta);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        final AssemblyMachineBlockEntity machine = this.menu.machine();
        if (machine == null) {
            return;
        }
        this.renderVerticalEnergyBar(guiGraphics, TEXTURE, this.leftPos + 152, this.topPos + 18, 16, 61, 176, 0,
            this.menu.energy(), this.menu.maxEnergy());
        final int progressWidth = machine.getSelectedRecipe().map(recipe -> this.menu.progress() * 70 / recipe.duration()).orElse(0);
        if (progressWidth > 0) {
            guiGraphics.fill(this.leftPos + 62, this.topPos + 126, this.leftPos + 62 + progressWidth, this.topPos + 142, 0xFFB08020);
        }
        this.renderHorizontalFluidBar(guiGraphics, this.leftPos + 8, this.topPos + 115, 52, 16,
            this.menu.inputAmount(), this.menu.inputCapacity(), 0xFF2050C0);
        this.renderHorizontalFluidBar(guiGraphics, this.leftPos + 80, this.topPos + 115, 52, 16,
            this.menu.outputAmount(), this.menu.outputCapacity(), 0xFF20A0D0);
        machine.getSelectedRecipe().ifPresent(recipe -> {
            guiGraphics.renderFakeItem(recipe.outputCopy(), this.leftPos + 8, this.topPos + 126);
            final List<HbmAssemblyRecipes.AssemblyRequirement> requirements = recipe.itemInputs();
            for (int i = 0; i < requirements.size() && i < AssemblyMachineBlockEntity.INPUT_COUNT; i++) {
                final int slotIndex = AssemblyMachineBlockEntity.SLOT_INPUT_START + i;
                final Slot slot = slotIndex < this.menu.slots.size() ? this.menu.slots.get(slotIndex) : null;
                this.renderGhostSlotItem(guiGraphics, slot, requirements.get(i).displayStack());
            }
        });
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        final AssemblyMachineBlockEntity machine = this.menu.machine();
        if (machine == null) {
            return;
        }
        final Optional<HbmAssemblyRecipes.AssemblyRecipe> selectedRecipe = machine.getSelectedRecipe();
        final int recipeIndex = this.menu.recipeIndex();
        final int recipeCount = this.menu.recipeCount();
        guiGraphics.drawString(this.font, recipeCount <= 0 ? "0/0" : (recipeIndex + 1) + "/" + recipeCount, 62, 131, 0x404040, false);
        this.renderEnergyTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 61,
            this.menu.energy(), this.menu.maxEnergy());
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 115, 52, 16,
            "Input Tank", this.menu.inputFluid(), this.menu.inputAmount(), this.menu.inputCapacity());
        this.renderFluidTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 80, this.topPos + 115, 52, 16,
            "Output Tank", this.menu.outputFluid(), this.menu.outputAmount(), this.menu.outputCapacity());
        this.renderUpgradeInfoTooltip(guiGraphics, mouseX, mouseY,
            this.leftPos + 152, this.topPos + 108, 36, 18);
        if (this.inside(mouseX, mouseY, this.leftPos + 7, this.topPos + 125, 18, 18)) {
            if (selectedRecipe.isPresent()) {
                guiGraphics.renderTooltip(this.font, this.recipeTooltip(selectedRecipe.get()), Optional.empty(), mouseX, mouseY);
            } else {
                guiGraphics.renderTooltip(this.font,
                    List.of(Component.literal("Set recipe").withStyle(ChatFormatting.YELLOW)),
                    Optional.empty(),
                    mouseX,
                    mouseY);
            }
        }
    }


    private List<Component> recipeTooltip(final HbmAssemblyRecipes.AssemblyRecipe recipe) {
        final List<Component> tooltip = new ArrayList<>();
        tooltip.add(recipe.outputCopy().getHoverName());
        tooltip.add(Component.literal(recipe.duration() + " t"));
        tooltip.add(Component.literal(recipe.consumption() + " HE/t"));
        for (final HbmAssemblyRecipes.AssemblyRequirement requirement : recipe.itemInputs()) {
            tooltip.add(Component.literal(requirement.count() + "x " + requirement.displayStack().getHoverName().getString()));
        }
        final FluidStack fluidInput = recipe.fluidInputCopy();
        if (!fluidInput.isEmpty()) {
            tooltip.add(Component.literal("In: " + fluidInput.getAmount() + " mB " + fluidInput.getDisplayName().getString()));
        }
        final FluidStack fluidOutput = recipe.fluidOutputCopy();
        if (!fluidOutput.isEmpty()) {
            tooltip.add(Component.literal("Out: " + fluidOutput.getAmount() + " mB " + fluidOutput.getDisplayName().getString()));
        }
        return tooltip;
    }


    @Override
    protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 0x404040, false);
        this.renderMachineLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

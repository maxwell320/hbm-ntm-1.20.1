package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.item.RtgPelletItem;
import com.hbm.ntm.common.menu.RtgGeneratorMenu;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("null")
public class RtgGeneratorScreen extends MachineScreenBase<RtgGeneratorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/gui_rtg.png");

    public RtgGeneratorScreen(final RtgGeneratorMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 188);
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        this.renderVerticalEnergyBar(guiGraphics,
            TEXTURE,
            this.leftPos + 124,
            this.topPos + 9,
            16,
            51,
            176,
            10,
            this.menu.heat(),
            this.menu.maxHeat());

        this.renderVerticalEnergyBar(guiGraphics,
            TEXTURE,
            this.leftPos + 146,
            this.topPos + 9,
            16,
            51,
            192,
            10,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderLegacyInfoPanel(guiGraphics, this.leftPos - 12, this.topPos + 25, 2);
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        if (this.inside(mouseX, mouseY, this.leftPos + 124, this.topPos + 9, 16, 51)) {
            this.renderMachineTooltip(guiGraphics, List.of(Component.translatable("desc.gui.rtg.heat", this.menu.heat())), mouseX, mouseY);
        }

        this.renderEnergyTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos + 146,
            this.topPos + 9,
            16,
            51,
            this.menu.energy(),
            this.menu.maxEnergy());

        this.renderLegacyInfoPanelTooltip(guiGraphics,
            mouseX,
            mouseY,
            this.leftPos - 12,
            this.topPos + 25,
            2,
            this.rtgPelletPowerTooltip());
    }

    @Override
    protected int titleLabelX() {
        return 13;
    }

    @Override
    protected int titleLabelY() {
        return 7;
    }

    @Override
    protected int titleLabelColor() {
        return 10925486;
    }

    private List<Component> rtgPelletPowerTooltip() {
        final List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("desc.gui.rtg.pellets"));
        ForgeRegistries.ITEMS.getValues().stream()
            .filter(item -> item instanceof RtgPelletItem)
            .map(item -> (RtgPelletItem) item)
            .sorted(Comparator.comparing(item -> item.getDescriptionId(ItemStack.EMPTY)))
            .forEach(pellet -> {
                final Component name = Component.translatable(((Item) pellet).getDescriptionId());
                tooltip.add(Component.translatable("desc.gui.rtg.pelletPower", name, pellet.getHeat() * 5));
            });
        return tooltip;
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}


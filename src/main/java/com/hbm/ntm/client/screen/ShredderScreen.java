package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.ShredderMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Shredder machine GUI — direct port of legacy {@code GUIMachineShredder}.
 * <p>
 * Legacy GUI dimensions: 176×233. Texture: {@code textures/gui/gui_shredder.png}.
 * <p>
 * Legacy UV layout (verified from {@code GUIMachineShredder.java}):
 * <ul>
 *   <li>Power bar: 16×88, source at (176, 160-i) to (192, 160), drawn at (8, 106-i)</li>
 *   <li>Progress bar: 34×18, source at (176, 54), drawn at (63, 89)</li>
 *   <li>Gear left good: (176, 0), worn: (176, 18), broken: (176, 36) — each 18×18 at (43, 71)</li>
 *   <li>Gear right good: (194, 0), worn: (194, 18), broken: (194, 36) — each 18×18 at (79, 71)</li>
 *   <li>Warning icon: drawn via {@code drawInfoPanel} at (guiLeft-16, guiTop+36, 16, 16) when blades missing/broken</li>
 * </ul>
 */
@SuppressWarnings("null")
public class ShredderScreen extends MachineScreenBase<ShredderMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/machine/gui_shredder.png");

    public ShredderScreen(final ShredderMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 233);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderMachineContents(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
        // Power bar — legacy: 16×88 bar, drawn from bottom up at (8, 106-i)
        final int maxEnergy = this.menu.maxEnergy();
        if (maxEnergy > 0 && this.menu.energy() > 0) {
            final int powerHeight = (int) ((long) this.menu.energy() * 88 / maxEnergy);
            guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 106 - powerHeight, 176, 160 - powerHeight, 16, powerHeight);
        }

        // Progress bar — legacy: 34×18, progress/processingSpeed * 34
        final int progressPixels = this.menu.progress() * 34 / Math.max(1, 60); // legacy processingSpeed = 60
        guiGraphics.blit(TEXTURE, this.leftPos + 63, this.topPos + 89, 176, 54, progressPixels + 1, 18);

        // Gear left indicator at (43, 71) — 18×18
        final int gearLeft = this.menu.gearLeft();
        if (gearLeft != 0) {
            final int gearLeftV = switch (gearLeft) {
                case 1 -> 0;   // good
                case 2 -> 18;  // worn
                case 3 -> 36;  // broken
                default -> -1;
            };
            if (gearLeftV >= 0) {
                guiGraphics.blit(TEXTURE, this.leftPos + 43, this.topPos + 71, 176, gearLeftV, 18, 18);
            }
        }

        // Gear right indicator at (79, 71) — 18×18
        final int gearRight = this.menu.gearRight();
        if (gearRight != 0) {
            final int gearRightV = switch (gearRight) {
                case 1 -> 0;   // good
                case 2 -> 18;  // worn
                case 3 -> 36;  // broken
                default -> -1;
            };
            if (gearRightV >= 0) {
                guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 71, 194, gearRightV, 18, 18);
            }
        }
    }

    @Override
    protected void renderMachineLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        // Power tooltip — legacy: drawElectricityInfo at (8, 18) size 16×88
        if (this.inside(mouseX + this.leftPos, mouseY + this.topPos, this.leftPos + 8, this.topPos + 18, 16, 88)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal(this.menu.energy() + " / " + this.menu.maxEnergy() + " HE")),
                Optional.empty(), mouseX, mouseY);
        }

        // Blade warning tooltip — legacy: "Error: Shredder blades are broken or missing!"
        final boolean bladeError = this.menu.gearLeft() == 0 || this.menu.gearLeft() == 3
            || this.menu.gearRight() == 0 || this.menu.gearRight() == 3;
        if (bladeError && this.inside(mouseX + this.leftPos, mouseY + this.topPos, this.leftPos + 43, this.topPos + 71, 54, 18)) {
            guiGraphics.renderTooltip(this.font,
                List.of(Component.literal("Error: Shredder blades are broken or missing!")),
                Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}

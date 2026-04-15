package com.hbm.ntm.parity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MachineRenderParityHarnessTest {
    private static final Path FIXTURE_PATH = Path.of("src", "test", "resources", "parity", "machine-render-baseline.csv");

    private static final Map<String, FileExpectation> EXPECTATIONS = new LinkedHashMap<>();

    static {
        EXPECTATIONS.put("client_render_bindings", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "HbmClientSetup.java"),
            List.of(
                "BlockEntityRenderers.register(HbmBlockEntityTypes.MACHINE_PRESS.get(), PressBlockEntityRenderer::new);",
                "MenuScreens.register(HbmMenuTypes.MACHINE_PRESS.get(), PressScreen::new);",
                "MenuScreens.register(HbmMenuTypes.MACHINE_SHREDDER.get(), ShredderScreen::new);",
                "MenuScreens.register(HbmMenuTypes.MACHINE_SOLDERING_STATION.get(), SolderingStationScreen::new);",
                "MenuScreens.register(HbmMenuTypes.MACHINE_CENTRIFUGE.get(), CentrifugeScreen::new);",
                "MenuScreens.register(HbmMenuTypes.MACHINE_GAS_CENTRIFUGE.get(), GasCentrifugeScreen::new);"
            )
        ));

        EXPECTATIONS.put("press_renderer_progress_curve", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "renderer", "blockentity", "PressBlockEntityRenderer.java"),
            List.of(
                "final float progress = Mth.clamp((float) press.getPressTicks() / (float) Math.max(1, press.configuredMaxPress()), 0.0F, 1.0F);",
                "this.renderPressHead(poseStack, bufferSource, packedLight, packedOverlay, progress);",
                "this.renderInputItem(press, poseStack, bufferSource, packedLight, packedOverlay);"
            )
        ));

        EXPECTATIONS.put("press_renderer_head_transform", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "renderer", "blockentity", "PressBlockEntityRenderer.java"),
            List.of(
                "final float yOffset = (1.0F - progress) * 0.875F;",
                "poseStack.translate(0.5D, 1.1D + yOffset, 0.5D);",
                "poseStack.scale(0.8F, 0.25F, 0.8F);",
                "this.blockRenderer.renderSingleBlock(HbmBlocks.MACHINE_PRESS.get().defaultBlockState(), poseStack, bufferSource, packedLight, packedOverlay);"
            )
        ));

        EXPECTATIONS.put("press_renderer_item_projection", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "renderer", "blockentity", "PressBlockEntityRenderer.java"),
            List.of(
                "final ItemStack input = press.getInternalItemHandler().getStackInSlot(PressBlockEntity.SLOT_INPUT);",
                "if (input.isEmpty()) {",
                "poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));",
                "this.itemRenderer.renderStatic(input, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, press.getLevel(), 0);"
            )
        ));

        EXPECTATIONS.put("press_screen_texture_metrics", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "PressScreen.java"),
            List.of(
                "private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, \"textures/gui/machine/gui_press.png\");",
                "super(menu, inventory, title, 176, 202);",
                "protected ResourceLocation texture() {",
                "return TEXTURE;"
            )
        ));

        EXPECTATIONS.put("press_screen_gauges", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "PressScreen.java"),
            List.of(
                "if (this.menu.burnTime() >= 20) {",
                "final int pressPixels = this.menu.maxPressTicks() <= 0 ? 0 : this.menu.pressTicks() * 16 / this.menu.maxPressTicks();",
                "if (this.menu.maxSpeed() > 0) {",
                "this.renderSmoothNeedleGauge("
            )
        ));

        EXPECTATIONS.put("shredder_screen_warning_overlay", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "ShredderScreen.java"),
            List.of(
                "final int gearLeftV = switch (gearLeft) {",
                "final int gearRightV = switch (gearRight) {",
                "if (this.hasBladeError()) {",
                "this.renderLegacyInfoPanel(guiGraphics, this.leftPos + WARNING_X, this.topPos + WARNING_Y, 6);"
            )
        ));

        EXPECTATIONS.put("soldering_collision_toggle_render", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "SolderingStationScreen.java"),
            List.of(
                "if (this.menu.collisionPrevention()) {",
                "this.renderConfigToggleIndicator(guiGraphics, this.leftPos + 5, this.topPos + 66, 10, 10, this.menu.collisionPrevention());",
                "this.renderHorizontalFluidBar(guiGraphics, this.leftPos + 35, this.topPos + 79, 34, 16,",
                "if (this.handleToggleControlClick(mouseX, mouseY, this.leftPos + 5, this.topPos + 66, 10, 10, \"collision\")) {"
            )
        ));

        EXPECTATIONS.put("centrifuge_stacked_progress_columns", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "CentrifugeScreen.java"),
            List.of(
                "int progress = this.menu.processingSpeed() <= 0 ? 0 : this.menu.progress() * 145 / this.menu.processingSpeed();",
                "for (int i = 0; i < 4; i++) {",
                "progress -= barHeight;",
                "if (progress <= 0) {"
            )
        ));

        EXPECTATIONS.put("gas_centrifuge_pseudo_tanks_and_warning", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "GasCentrifugeScreen.java"),
            List.of(
                "this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 16, this.topPos + 16, 6, 52,",
                "this.renderVerticalFluidGaugeBar(guiGraphics, this.leftPos + 154, this.topPos + 16, 6, 52,",
                "if (this.menu.inputNeedsSpeedUpgrade() && !this.menu.hasSpeedUpgrade()) {",
                "Component.translatable(\"screen.hbmntm.machine_gascent.warning\")"
            )
        ));

        EXPECTATIONS.put("machine_screen_repair_overlay", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "MachineScreenBase.java"),
            List.of(
                "private static final int REPAIR_ICON_SIZE = 8;",
                "private void renderRepairIcon(final GuiGraphics guiGraphics) {",
                "if (!this.menu.isMaintenanceBlocked()) {",
                "guiGraphics.fill(x, y, x + REPAIR_ICON_SIZE, y + REPAIR_ICON_SIZE, 0xFFC02020);",
                "guiGraphics.drawString(this.font, \"!\", x + 2, y, 0xFFFFFFFF, false);"
            )
        ));
    }

    @Test
    void fixtureExists() {
        assertTrue(Files.exists(FIXTURE_PATH), "Machine render baseline fixture must exist: " + FIXTURE_PATH);
    }

    @Test
    void fixtureHasExpectedSchemaAndRows() throws IOException {
        final var lines = Files.readAllLines(FIXTURE_PATH);
        assertFalse(lines.isEmpty(), "Fixture must contain a header row");
        assertEquals("check_id,file_ref,notes", lines.get(0), "Fixture header must match schema");

        final Set<String> fixtureIds = new LinkedHashSet<>();
        int dataRows = 0;
        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            final String[] cells = line.split(",", -1);
            assertEquals(3, cells.length, "Each fixture row must have 3 columns: " + line);
            assertFalse(cells[0].isBlank(), "check_id must be non-blank");
            assertFalse(cells[1].isBlank(), "file_ref must be non-blank");
            assertTrue(cells[2].contains("_"), "notes should preserve explicit check context");
            fixtureIds.add(cells[0]);
            dataRows++;
        }

        assertTrue(dataRows >= 10, "Fixture should include at least 10 seeded render checks");
        assertEquals(EXPECTATIONS.keySet(), fixtureIds, "Fixture IDs must align with seeded render expectations");
    }

    @Test
    void renderParityAnchorsExistAcrossCoreFiles() throws IOException {
        final Map<Path, String> sourceCache = new LinkedHashMap<>();

        for (final var entry : EXPECTATIONS.entrySet()) {
            final String checkId = entry.getKey();
            final FileExpectation expectation = entry.getValue();
            sourceCache.computeIfAbsent(expectation.filePath(), path -> {
                try {
                    return Files.readString(path);
                } catch (final IOException exception) {
                    throw new RuntimeException(exception);
                }
            });
            final String source = sourceCache.get(expectation.filePath());

            int cursor = 0;
            for (final String token : expectation.orderedTokens()) {
                final int foundAt = source.indexOf(token, cursor);
                assertTrue(foundAt >= 0, "Check " + checkId + " missing expected token: " + token);
                cursor = foundAt + token.length();
            }
        }
    }

    private static FileExpectation expectation(final Path filePath, final List<String> orderedTokens) {
        return new FileExpectation(filePath, orderedTokens);
    }

    private record FileExpectation(Path filePath, List<String> orderedTokens) {
    }
}
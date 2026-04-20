package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@SuppressWarnings("null")
public final class HbmDebugEventHandler {

    private static int lastMouseX;
    private static int lastMouseY;
    private static int lastScreenWidth;
    private static int lastScreenHeight;

    private HbmDebugEventHandler() {
    }

    public static int lastMouseX() {
        return lastMouseX;
    }

    public static int lastMouseY() {
        return lastMouseY;
    }

    public static int lastScreenWidth() {
        return lastScreenWidth;
    }

    public static int lastScreenHeight() {
        return lastScreenHeight;
    }

    @SubscribeEvent
    public static void onPre(final ScreenEvent.Render.Pre event) {
        HbmDebugHitboxTracker.beginFrame();
    }

    @SubscribeEvent
    public static void onKeyPressed(final ScreenEvent.KeyPressed.Pre event) {
        if (Screen.hasShiftDown() || Screen.hasControlDown() || Screen.hasAltDown()) {
            return;
        }
        switch (event.getKeyCode()) {
            case GLFW.GLFW_KEY_F6 -> {
                if (runHotkeyCapture()) {
                    event.setCanceled(true);
                }
            }
            case GLFW.GLFW_KEY_F7 -> {
                if (HbmDebugOverlay.anyEnabled()) {
                    HbmDebugOverlay.disableAll();
                    toast("HBM overlays", "All overlays OFF");
                } else {
                    HbmDebugOverlay.enableAll();
                    toast("HBM overlays", "All overlays ON: hitbox, slots, grid, coords, labels");
                }
                event.setCanceled(true);
            }
            case GLFW.GLFW_KEY_F8 -> {
                if (runHotkeyBundle()) {
                    event.setCanceled(true);
                }
            }
            case GLFW.GLFW_KEY_F9 -> {
                if (runHotkeyScreenshot()) {
                    event.setCanceled(true);
                }
            }
            default -> { /* fall through to screen */ }
        }
    }

    private static boolean runHotkeyCapture() {
        try {
            HbmDebugScreenInspector.captureProbe(lastMouseX, lastMouseY);
            final Path uiDump = HbmDebugScreenInspector.dumpCurrentScreen();
            final Path probeOut = HbmDebugWriter.write("ui-probe",
                HbmDebugScreenInspector.probeToString(HbmDebugScreenInspector.lastProbe()));
            toast("HBM capture", "Saved ui-dump + ui-probe");
            HbmNtmMod.LOGGER.info("[hbm-debug] F6 capture -> {} + {}", uiDump, probeOut);
            tryCaptureModelLook();
            tryCaptureMachine();
            return true;
        } catch (final IOException ex) {
            toast("HBM capture FAILED", ex.toString());
            HbmNtmMod.LOGGER.error("[hbm-debug] F6 capture failed", ex);
            return true;
        }
    }

    private static void tryCaptureModelLook() {
        try {
            HbmDebugModelInspector.dumpLookedAtBlock();
        } catch (final IOException ex) {
            HbmNtmMod.LOGGER.warn("[hbm-debug] F6 model look failed: {}", ex.toString());
        }
    }

    private static void tryCaptureMachine() {
        try {
            HbmDebugMachineInspector.dumpLookedAtMachine();
        } catch (final IOException ex) {
            HbmNtmMod.LOGGER.warn("[hbm-debug] F6 machine dump failed: {}", ex.toString());
        }
    }

    private static boolean runHotkeyBundle() {
        try {
            final Path out = HbmDebugBundle.runFull();
            toast("HBM bundle", "Saved " + out.getFileName());
            HbmNtmMod.LOGGER.info("[hbm-debug] F8 bundle -> {}", out);
            return true;
        } catch (final IOException ex) {
            toast("HBM bundle FAILED", ex.toString());
            HbmNtmMod.LOGGER.error("[hbm-debug] F8 bundle failed", ex);
            return true;
        }
    }

    private static boolean runHotkeyScreenshot() {
        try {
            final Path out = HbmDebugBundle.screenshotOnly();
            toast("HBM screenshot", "Saved " + out.getFileName());
            HbmNtmMod.LOGGER.info("[hbm-debug] F9 screenshot -> {}", out);
            return true;
        } catch (final IOException ex) {
            toast("HBM screenshot FAILED", ex.toString());
            HbmNtmMod.LOGGER.error("[hbm-debug] F9 screenshot failed", ex);
            return true;
        }
    }

    private static void toast(final String title, final String body) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getToasts() == null) {
            return;
        }
        SystemToast.add(mc.getToasts(), SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
            Component.literal(title), Component.literal(body));
    }

    @SubscribeEvent
    public static void onPost(final ScreenEvent.Render.Post event) {
        final Screen screen = event.getScreen();
        lastMouseX = event.getMouseX();
        lastMouseY = event.getMouseY();
        lastScreenWidth = screen.width;
        lastScreenHeight = screen.height;

        if (!HbmDebugOverlay.anyEnabled()) {
            return;
        }

        final GuiGraphics g = event.getGuiGraphics();
        final Font font = Minecraft.getInstance().font;

        if (HbmDebugOverlay.isEnabled(HbmDebugOverlay.OverlayMode.GRID) && screen instanceof AbstractContainerScreen<?> acs) {
            drawGrid(g, acs);
        }

        if (HbmDebugOverlay.isEnabled(HbmDebugOverlay.OverlayMode.SLOTS) && screen instanceof AbstractContainerScreen<?> acs) {
            drawSlots(g, font, acs);
        }

        if (HbmDebugOverlay.isEnabled(HbmDebugOverlay.OverlayMode.HITBOX)) {
            drawHitboxes(g, font);
        }

        if (HbmDebugOverlay.isEnabled(HbmDebugOverlay.OverlayMode.LABELS) && screen instanceof AbstractContainerScreen<?> acs) {
            drawLabels(g, font, acs);
        }

        if (HbmDebugOverlay.isEnabled(HbmDebugOverlay.OverlayMode.COORDS)) {
            drawCoords(g, font, screen);
        }

        if (HbmDebugOverlay.consumeProbeRequest()) {
            HbmDebugScreenInspector.captureProbe(lastMouseX, lastMouseY);
        }
    }

    private static void drawGrid(final GuiGraphics g, final AbstractContainerScreen<?> acs) {
        final int left = acs.getGuiLeft();
        final int top = acs.getGuiTop();
        final int right = left + acs.getXSize();
        final int bottom = top + acs.getYSize();
        final int color = 0x40FFFFFF;
        for (int x = left; x <= right; x += 16) {
            g.fill(x, top, x + 1, bottom, color);
        }
        for (int y = top; y <= bottom; y += 16) {
            g.fill(left, y, right, y + 1, color);
        }
        final int accent = 0x80FFD040;
        g.fill(left, top, right, top + 1, accent);
        g.fill(left, bottom - 1, right, bottom, accent);
        g.fill(left, top, left + 1, bottom, accent);
        g.fill(right - 1, top, right, bottom, accent);
    }

    private static void drawSlots(final GuiGraphics g, final Font font, final AbstractContainerScreen<?> acs) {
        final int left = acs.getGuiLeft();
        final int top = acs.getGuiTop();
        final List<Slot> slots = acs.getMenu().slots;
        for (int i = 0; i < slots.size(); i++) {
            final Slot slot = slots.get(i);
            final int sx = left + slot.x;
            final int sy = top + slot.y;
            outline(g, sx, sy, 16, 16, 0xFF00E0FF);
            g.drawString(font, Integer.toString(i), sx + 1, sy + 1, 0xFFFFFFFF, true);
        }
    }

    private static void drawHitboxes(final GuiGraphics g, final Font font) {
        final List<HbmDebugHitboxTracker.Hitbox> boxes = HbmDebugHitboxTracker.snapshot();
        for (final HbmDebugHitboxTracker.Hitbox box : boxes) {
            outline(g, box.x(), box.y(), box.width(), box.height(), box.color());
            g.drawString(font, box.label(), box.x() + 1, box.y() - 9, box.color(), true);
        }
    }

    private static void drawLabels(final GuiGraphics g, final Font font, final AbstractContainerScreen<?> acs) {
        final int left = acs.getGuiLeft();
        final int top = acs.getGuiTop();
        outline(g, left, top, acs.getXSize(), 12, 0xFF40D040);
        outline(g, left, top + acs.getYSize() - 24, acs.getXSize(), 10, 0xFF40D040);
    }

    private static void drawCoords(final GuiGraphics g, final Font font, final Screen screen) {
        final int x = 4;
        int y = 4;
        g.fill(x - 2, y - 2, x + 220, y + 48, 0xA0101010);
        g.drawString(font, Component.literal("HBM debug overlay"), x, y, 0xFFFFD040, true);
        y += 10;
        g.drawString(font, Component.literal("mouse abs: " + lastMouseX + "," + lastMouseY), x, y, 0xFFFFFFFF, true);
        y += 10;
        if (screen instanceof AbstractContainerScreen<?> acs) {
            g.drawString(font, Component.literal("mouse gui: "
                + (lastMouseX - acs.getGuiLeft()) + "," + (lastMouseY - acs.getGuiTop())), x, y, 0xFFFFFFFF, true);
            y += 10;
            g.drawString(font, Component.literal("gui: " + acs.getClass().getSimpleName()
                + " " + acs.getXSize() + "x" + acs.getYSize()
                + " @ " + acs.getGuiLeft() + "," + acs.getGuiTop()), x, y, 0xFFA0FFA0, true);
        } else {
            g.drawString(font, Component.literal("screen: " + screen.getClass().getSimpleName()), x, y, 0xFFA0FFA0, true);
        }
    }

    private static void outline(final GuiGraphics g, final int x, final int y, final int width, final int height, final int color) {
        g.fill(x, y, x + width, y + 1, color);
        g.fill(x, y + height - 1, x + width, y + height, color);
        g.fill(x, y, x + 1, y + height, color);
        g.fill(x + width - 1, y, x + width, y + height, color);
    }
}

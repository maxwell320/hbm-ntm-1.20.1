package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.client.screen.MachineScreenBase;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("null")
public final class HbmDebugScreenInspector {

    private static volatile ProbeResult lastProbe;

    public record ProbeResult(int mouseAbsX, int mouseAbsY, int mouseGuiX, int mouseGuiY,
                              String screenClass, int leftPos, int topPos, int imageWidth, int imageHeight,
                              int hoveredSlotIndex, String hoveredSlotItem,
                              List<HbmDebugHitboxTracker.Hitbox> hitHitboxes) {
    }

    private HbmDebugScreenInspector() {
    }

    public static ProbeResult lastProbe() {
        return lastProbe;
    }

    public static Path dumpCurrentScreen() throws IOException {
        final Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            return HbmDebugWriter.write("ui-dump-none", "No screen open at " + HbmDebugWriter.stamp() + "\n");
        }
        final StringBuilder sb = new StringBuilder(4096);
        sb.append("=== HBM debug ui dump ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("modId: ").append(HbmNtmMod.MOD_ID).append('\n');
        sb.append("screenClass: ").append(screen.getClass().getName()).append('\n');
        sb.append("screenSize: ").append(screen.width).append('x').append(screen.height).append('\n');

        appendSuperclasses(sb, screen.getClass());

        if (screen instanceof AbstractContainerScreen<?> acs) {
            sb.append("\n-- geometry --\n");
            sb.append("leftPos: ").append(acs.getGuiLeft()).append('\n');
            sb.append("topPos: ").append(acs.getGuiTop()).append('\n');
            sb.append("imageWidth: ").append(acs.getXSize()).append('\n');
            sb.append("imageHeight: ").append(acs.getYSize()).append('\n');

            sb.append("\n-- labels --\n");
            appendIntField(sb, "titleLabelX", acs, "titleLabelX");
            appendIntField(sb, "titleLabelY", acs, "titleLabelY");
            appendIntField(sb, "inventoryLabelX", acs, "inventoryLabelX");
            appendIntField(sb, "inventoryLabelY", acs, "inventoryLabelY");
            sb.append("title: ").append(acs.getTitle().getString()).append('\n');

            sb.append("\n-- menu --\n");
            final AbstractContainerMenu menu = acs.getMenu();
            sb.append("menuClass: ").append(menu.getClass().getName()).append('\n');
            sb.append("menuType: ").append(menu.getType() == null ? "null" : menu.getType().toString()).append('\n');
            sb.append("containerId: ").append(menu.containerId).append('\n');

            sb.append("\n-- slots (").append(menu.slots.size()).append(") --\n");
            sb.append("idx\tx\ty\tabsX\tabsY\titem\tslotClass\n");
            for (int i = 0; i < menu.slots.size(); i++) {
                final Slot slot = menu.slots.get(i);
                final ItemStack stack = slot.getItem();
                sb.append(i).append('\t')
                  .append(slot.x).append('\t')
                  .append(slot.y).append('\t')
                  .append(acs.getGuiLeft() + slot.x).append('\t')
                  .append(acs.getGuiTop() + slot.y).append('\t')
                  .append(stack.isEmpty() ? "empty" : (stack.getItem() + " x" + stack.getCount())).append('\t')
                  .append(slot.getClass().getSimpleName())
                  .append('\n');
            }

            sb.append("\n-- hitboxes recorded last frame (").append(HbmDebugHitboxTracker.snapshot().size()).append(") --\n");
            sb.append("label\tx\ty\twidth\theight\tcolor\n");
            for (final HbmDebugHitboxTracker.Hitbox box : HbmDebugHitboxTracker.snapshot()) {
                sb.append(box.label()).append('\t')
                  .append(box.x()).append('\t')
                  .append(box.y()).append('\t')
                  .append(box.width()).append('\t')
                  .append(box.height()).append('\t')
                  .append(String.format("%08X", box.color()))
                  .append('\n');
            }

            if (screen instanceof MachineScreenBase<?> msb) {
                sb.append("\n-- machine screen reflection --\n");
                appendAllFields(sb, msb);
            }
        } else {
            sb.append("\n(non-container screen)\n");
        }

        sb.append("\n-- last mouse position --\n");
        sb.append("mouseX: ").append(HbmDebugEventHandler.lastMouseX()).append('\n');
        sb.append("mouseY: ").append(HbmDebugEventHandler.lastMouseY()).append('\n');

        return HbmDebugWriter.write("ui-dump", sb.toString());
    }

    public static void captureProbe(final int mouseX, final int mouseY) {
        final Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            lastProbe = null;
            return;
        }
        int leftPos = 0;
        int topPos = 0;
        int imageWidth = screen.width;
        int imageHeight = screen.height;
        int hoveredSlotIndex = -1;
        String hoveredSlotItem = "n/a";
        if (screen instanceof AbstractContainerScreen<?> acs) {
            leftPos = acs.getGuiLeft();
            topPos = acs.getGuiTop();
            imageWidth = acs.getXSize();
            imageHeight = acs.getYSize();
            final Slot hovered = acs.getSlotUnderMouse();
            if (hovered != null) {
                hoveredSlotIndex = hovered.index;
                final ItemStack stack = hovered.getItem();
                hoveredSlotItem = stack.isEmpty() ? "empty" : (stack.getItem() + " x" + stack.getCount());
            }
        }
        final List<HbmDebugHitboxTracker.Hitbox> hit = HbmDebugHitboxTracker.snapshot().stream()
            .filter(h -> mouseX >= h.x() && mouseX < h.x() + h.width()
                && mouseY >= h.y() && mouseY < h.y() + h.height())
            .toList();
        lastProbe = new ProbeResult(mouseX, mouseY,
            mouseX - leftPos, mouseY - topPos,
            screen.getClass().getSimpleName(), leftPos, topPos, imageWidth, imageHeight,
            hoveredSlotIndex, hoveredSlotItem, hit);
    }

    public static String probeToString(final ProbeResult p) {
        if (p == null) {
            return "no probe captured — open a screen and run /hbm ui probe again";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("probe @ ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("  screen: ").append(p.screenClass()).append('\n');
        sb.append("  mouse abs: ").append(p.mouseAbsX()).append(',').append(p.mouseAbsY()).append('\n');
        sb.append("  mouse gui: ").append(p.mouseGuiX()).append(',').append(p.mouseGuiY()).append('\n');
        sb.append("  gui: ").append(p.imageWidth()).append('x').append(p.imageHeight())
          .append(" @ ").append(p.leftPos()).append(',').append(p.topPos()).append('\n');
        sb.append("  hovered slot: ")
          .append(p.hoveredSlotIndex() < 0 ? "none" : (p.hoveredSlotIndex() + " (" + p.hoveredSlotItem() + ")"))
          .append('\n');
        sb.append("  hitboxes matching (").append(p.hitHitboxes().size()).append("):\n");
        for (final HbmDebugHitboxTracker.Hitbox box : p.hitHitboxes()) {
            sb.append("    - ").append(box.label())
              .append(" @ ").append(box.x()).append(',').append(box.y())
              .append(" size ").append(box.width()).append('x').append(box.height()).append('\n');
        }
        return sb.toString();
    }

    private static void appendSuperclasses(final StringBuilder sb, final Class<?> cls) {
        sb.append("inheritance:\n");
        Class<?> c = cls;
        int depth = 0;
        while (c != null) {
            for (int i = 0; i < depth; i++) sb.append("  ");
            sb.append("- ").append(c.getName()).append('\n');
            c = c.getSuperclass();
            depth++;
        }
    }

    private static void appendIntField(final StringBuilder sb, final String label, final Object target, final String fieldName) {
        try {
            final Field f = findField(target.getClass(), fieldName);
            if (f == null) {
                sb.append(label).append(": <not found>\n");
                return;
            }
            f.setAccessible(true);
            sb.append(label).append(": ").append(f.getInt(target)).append('\n');
        } catch (final ReflectiveOperationException ex) {
            sb.append(label).append(": <error ").append(ex.getClass().getSimpleName()).append(">\n");
        }
    }

    private static Field findField(final Class<?> cls, final String name) {
        Class<?> c = cls;
        while (c != null) {
            try {
                return c.getDeclaredField(name);
            } catch (final NoSuchFieldException ignored) {
                c = c.getSuperclass();
            }
        }
        return null;
    }

    private static void appendAllFields(final StringBuilder sb, final Object target) {
        Class<?> c = target.getClass();
        while (c != null && c != Object.class) {
            for (final Field f : c.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                try {
                    f.setAccessible(true);
                    final Object v = f.get(target);
                    sb.append(c.getSimpleName()).append('.').append(f.getName())
                      .append(" = ")
                      .append(v == null ? "null" : shortRender(v))
                      .append('\n');
                } catch (final ReflectiveOperationException ex) {
                    sb.append(c.getSimpleName()).append('.').append(f.getName())
                      .append(" = <error ").append(ex.getClass().getSimpleName()).append(">\n");
                }
            }
            c = c.getSuperclass();
        }
    }

    private static String shortRender(final Object v) {
        final String raw = String.valueOf(v);
        if (raw.length() <= 200) {
            return raw;
        }
        return raw.substring(0, 200) + "... (truncated " + (raw.length() - 200) + " chars)";
    }
}

package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

@SuppressWarnings("null")
public final class HbmDebugBundle {

    private HbmDebugBundle() {
    }

    public static Path run() throws IOException {
        final StringBuilder sb = new StringBuilder(16_384);
        sb.append("=== HBM debug bundle ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');

        appendVersions(sb);
        appendScreenSummary(sb);
        appendLookSummary(sb);
        appendLogTail(sb);

        return HbmDebugWriter.write("bundle", sb.toString());
    }

    public static Path runFull() throws IOException {
        final Path bundleTxt = HbmDebugWriter.write("bundle", buildBundleContent());
        tryDumpUi();
        tryDumpModelLook();
        tryDumpMachine();
        captureScreenshot();
        return bundleTxt;
    }

    public static Path screenshotOnly() throws IOException {
        return captureScreenshot();
    }

    private static String buildBundleContent() throws IOException {
        final StringBuilder sb = new StringBuilder(16_384);
        sb.append("=== HBM debug bundle (full) ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        appendVersions(sb);
        appendScreenSummary(sb);
        appendLookSummary(sb);
        appendLogTail(sb);
        return sb.toString();
    }

    private static void tryDumpUi() {
        try {
            HbmDebugScreenInspector.dumpCurrentScreen();
        } catch (final IOException ex) {
            HbmNtmMod.LOGGER.warn("[hbm-debug] bundle: ui dump failed: {}", ex.toString());
        }
    }

    private static void tryDumpModelLook() {
        try {
            HbmDebugModelInspector.dumpLookedAtBlock();
        } catch (final IOException ex) {
            HbmNtmMod.LOGGER.warn("[hbm-debug] bundle: model look failed: {}", ex.toString());
        }
    }

    private static void tryDumpMachine() {
        try {
            HbmDebugMachineInspector.dumpLookedAtMachine();
        } catch (final IOException ex) {
            HbmNtmMod.LOGGER.warn("[hbm-debug] bundle: machine dump failed: {}", ex.toString());
        }
    }

    private static Path captureScreenshot() throws IOException {
        final Minecraft mc = Minecraft.getInstance();
        final Path debugDir = HbmDebugWriter.debugDir();
        final String filename = "hbm-screenshot-" + HbmDebugWriter.stamp() + ".png";
        Screenshot.grab(debugDir.toFile(), filename, mc.getMainRenderTarget(),
            (Component msg) -> HbmNtmMod.LOGGER.info("[hbm-debug] screenshot: {}", msg.getString()));
        return debugDir.resolve(filename);
    }

    private static void appendVersions(final StringBuilder sb) {
        sb.append("\n-- versions --\n");
        sb.append("mcVersion: ").append(SharedConstants.getCurrentVersion().getName()).append('\n');
        sb.append("forgeVersion: ").append(FMLLoader.versionInfo().forgeVersion()).append('\n');
        sb.append("mcpVersion: ").append(FMLLoader.versionInfo().mcpVersion()).append('\n');
        sb.append("javaVersion: ").append(System.getProperty("java.version")).append('\n');
        sb.append("os: ").append(System.getProperty("os.name")).append(' ')
          .append(System.getProperty("os.version")).append('\n');
        ModList.get().getMods().stream()
            .filter(m -> HbmNtmMod.MOD_ID.equals(m.getModId()))
            .findFirst()
            .ifPresent(mod -> sb.append("hbmntm: ").append(mod.getVersion()).append('\n'));
    }

    private static void appendScreenSummary(final StringBuilder sb) {
        sb.append("\n-- current screen --\n");
        final Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            sb.append("(no screen open)\n");
            return;
        }
        sb.append("class: ").append(mc.screen.getClass().getName()).append('\n');
        if (mc.screen instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<?> acs) {
            sb.append("geometry: ").append(acs.getXSize()).append('x').append(acs.getYSize())
              .append(" @ ").append(acs.getGuiLeft()).append(',').append(acs.getGuiTop()).append('\n');
            sb.append("slotCount: ").append(acs.getMenu().slots.size()).append('\n');
        }
    }

    private static void appendLookSummary(final StringBuilder sb) {
        sb.append("\n-- looking at --\n");
        final Minecraft mc = Minecraft.getInstance();
        final LocalPlayer player = mc.player;
        final ClientLevel level = mc.level;
        if (player == null || level == null) {
            sb.append("(no player/level)\n");
            return;
        }
        final HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult bhr) || bhr.getType() != HitResult.Type.BLOCK) {
            sb.append("(not looking at a block)\n");
            return;
        }
        final BlockPos pos = bhr.getBlockPos();
        sb.append("pos: ").append(pos.getX()).append(',').append(pos.getY()).append(',').append(pos.getZ()).append('\n');
        sb.append("block: ").append(level.getBlockState(pos)).append('\n');
    }

    private static void appendLogTail(final StringBuilder sb) {
        sb.append("\n-- latest.log tail (last 100 lines) --\n");
        try {
            final Path src = HbmDebugLogTail.resolveLatestLog();
            if (!Files.exists(src)) {
                sb.append("(latest.log missing)\n");
                return;
            }
            final java.util.List<String> all = Files.readAllLines(src, Charset.forName("ISO-8859-1"));
            final int start = Math.max(0, all.size() - 100);
            for (int i = start; i < all.size(); i++) {
                sb.append(all.get(i)).append('\n');
            }
        } catch (final IOException ex) {
            sb.append("(log tail error: ").append(ex.toString()).append(")\n");
        }
    }
}

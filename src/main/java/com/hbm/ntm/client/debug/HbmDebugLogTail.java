package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;

@SuppressWarnings("null")
public final class HbmDebugLogTail {

    private static final Charset LOG_CHARSET = lenientCharset();

    private HbmDebugLogTail() {
    }

    private static Charset lenientCharset() {
        try {
            return Charset.forName("ISO-8859-1");
        } catch (final Exception ex) {
            return StandardCharsets.UTF_8;
        }
    }

    public static Path resolveLatestLog() {
        return Minecraft.getInstance().gameDirectory.toPath()
            .resolve("logs").resolve("latest.log");
    }

    public static Path tail(final int lines) throws IOException {
        final Path src = resolveLatestLog();
        final StringBuilder sb = new StringBuilder(8192);
        sb.append("=== HBM log tail ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("source: ").append(src.toAbsolutePath()).append('\n');
        sb.append("requested lines: ").append(lines).append('\n');
        if (!Files.exists(src)) {
            sb.append("(latest.log does not exist)\n");
            return HbmDebugWriter.write("log-tail", sb.toString());
        }
        final int clamped = Math.max(1, Math.min(5000, lines));
        final Deque<String> ring = new ArrayDeque<>(clamped);
        try (Stream<String> stream = Files.lines(src, LOG_CHARSET)) {
            stream.forEach(line -> {
                if (ring.size() >= clamped) {
                    ring.pollFirst();
                }
                ring.addLast(line);
            });
        }
        sb.append("\n-- last ").append(ring.size()).append(" lines --\n");
        for (final String line : ring) {
            sb.append(line).append('\n');
        }
        return HbmDebugWriter.write("log-tail", sb.toString());
    }

    public static Path saveCopy() throws IOException {
        final Path src = resolveLatestLog();
        if (!Files.exists(src)) {
            return HbmDebugWriter.write("log-save", "latest.log does not exist at " + src + "\n");
        }
        final Path target = HbmDebugWriter.debugDir().resolve("latest-" + HbmDebugWriter.stamp() + ".log");
        Files.copy(src, target);
        HbmNtmMod.LOGGER.info("[hbm-debug] Copied latest.log -> {}", target);
        return target;
    }

    public static List<String> grep(final String needle, final int maxLines) throws IOException {
        final Path src = resolveLatestLog();
        if (!Files.exists(src)) {
            return List.of("(latest.log does not exist)");
        }
        final int clamped = Math.max(1, Math.min(2000, maxLines));
        try (Stream<String> stream = Files.lines(src, LOG_CHARSET)) {
            return stream
                .filter(line -> line.contains(needle))
                .limit(clamped)
                .toList();
        }
    }
}

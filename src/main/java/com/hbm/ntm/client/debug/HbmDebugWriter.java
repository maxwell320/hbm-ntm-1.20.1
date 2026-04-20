package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.minecraft.client.Minecraft;

public final class HbmDebugWriter {

    public static final String DEBUG_DIR = "hbm-debug";

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private HbmDebugWriter() {
    }

    public static Path debugDir() throws IOException {
        final Path gameDir = Minecraft.getInstance().gameDirectory.toPath();
        final Path dir = gameDir.resolve(DEBUG_DIR);
        if (!Files.isDirectory(dir)) {
            Files.createDirectories(dir);
        }
        return dir;
    }

    public static String stamp() {
        return LocalDateTime.now().format(TIMESTAMP);
    }

    public static Path write(final String kind, final String content) throws IOException {
        final Path file = debugDir().resolve(kind + "-" + stamp() + ".txt");
        Files.writeString(file, content, StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        HbmNtmMod.LOGGER.info("[hbm-debug] Wrote {}", file.toAbsolutePath());
        return file;
    }

    public static Path writeSafe(final String kind, final String content) {
        try {
            return write(kind, content);
        } catch (final IOException ex) {
            HbmNtmMod.LOGGER.error("[hbm-debug] Failed to write {}: {}", kind, ex.toString());
            return null;
        }
    }

    public static Path append(final Path target, final String content) throws IOException {
        Files.writeString(target, content, StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return target;
    }
}

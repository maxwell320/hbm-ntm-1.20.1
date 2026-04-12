package com.hbm.ntm.parity;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class VerificationChecklistPresenceTest {
    private static final Path CHECKLIST_PATH = Path.of("docs", "verification", "verification-evidence-checklist.md");

    @Test
    void checklistFileExists() {
        assertTrue(Files.exists(CHECKLIST_PATH), "Verification checklist file must exist: " + CHECKLIST_PATH);
    }

    @Test
    void checklistContainsRequiredSections() throws IOException {
        final String content = Files.readString(CHECKLIST_PATH);

        assertTrue(content.contains("## Automated Gates"), "Checklist missing Automated Gates section");
        assertTrue(content.contains("## Sync Stability Evidence"), "Checklist missing Sync Stability Evidence section");
        assertTrue(content.contains("## Render Parity Evidence"), "Checklist missing Render Parity Evidence section");
        assertTrue(content.contains("## Batch Exit Gate"), "Checklist missing Batch Exit Gate section");
    }
}

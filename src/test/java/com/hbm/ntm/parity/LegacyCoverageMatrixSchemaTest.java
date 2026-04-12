package com.hbm.ntm.parity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class LegacyCoverageMatrixSchemaTest {
    private static final Path MATRIX_PATH = Path.of("docs", "verification", "legacy-coverage-matrix.csv");
    private static final Set<String> REQUIRED_HEADERS = Set.of(
        "subsystem_id",
        "subsystem_name",
        "legacy_scope",
        "status",
        "modern_anchor",
        "verification_gate",
        "blocker",
        "next_batch"
    );
    private static final Set<String> VALID_STATUSES = Set.of("done", "partial", "not_started", "blocked");

    @Test
    void matrixFileExists() {
        assertTrue(Files.exists(MATRIX_PATH), "Coverage matrix file must exist: " + MATRIX_PATH);
    }

    @Test
    void matrixHeaderMatchesRequiredColumns() throws IOException {
        final var lines = Files.readAllLines(MATRIX_PATH);
        assertFalse(lines.isEmpty(), "Coverage matrix must include a header row");

        final var headerSet = Arrays.stream(lines.get(0).split(",", -1))
            .map(String::trim)
            .collect(Collectors.toSet());

        assertEquals(REQUIRED_HEADERS, headerSet, "Coverage matrix header must match required schema");
    }

    @Test
    void matrixRowsHaveValidStatusAndMinimumSeedCoverage() throws IOException {
        final var lines = Files.readAllLines(MATRIX_PATH);
        int dataRows = 0;

        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            final String[] cells = line.split(",", -1);
            assertEquals(8, cells.length, "Each matrix row must have exactly 8 columns: " + line);

            assertFalse(cells[0].isBlank(), "subsystem_id must be non-blank");
            assertTrue(VALID_STATUSES.contains(cells[3].trim()), "Invalid status value: " + cells[3]);
            dataRows++;
        }

        assertTrue(dataRows >= 10, "Coverage matrix should be seeded with at least 10 subsystem rows");
    }
}

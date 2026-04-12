package com.hbm.ntm.parity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class CentrifugeParityHarnessTest {
    private static final Path FIXTURE_PATH = Path.of("src", "test", "resources", "parity", "centrifuge-legacy-baseline.csv");
    private static final Path MODERN_SOURCE_PATH = Path.of(
        "src", "main", "java", "com", "hbm", "ntm", "common", "centrifuge", "HbmCentrifugeRecipes.java"
    );

    private static final Map<String, RouteExpectation> ROUTE_EXPECTATIONS = new LinkedHashMap<>();

    static {
        ROUTE_EXPECTATIONS.put("coal_ore", route(
            "addRecipe\\(Ingredient\\.of\\(Blocks\\.COAL_ORE[\\s\\S]*?new ItemStack\\(Blocks\\.GRAVEL\\)\\);",
            Map.of(
                "material(HbmMaterials.COAL, HbmMaterialShape.DUST, 2)", 3,
                "new ItemStack(Blocks.GRAVEL)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("lignite_ore", route(
            "addRecipe\\(Ingredient\\.of\\(HbmBlocks\\.getOverworldOre\\(OverworldOreType\\.LIGNITE\\)\\.get\\(\\)\\),[\\s\\S]*?new ItemStack\\(Blocks\\.GRAVEL\\)\\);",
            Map.of(
                "material(HbmMaterials.LIGNITE, HbmMaterialShape.DUST, 2)", 3,
                "new ItemStack(Blocks.GRAVEL)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("uranium_ore", route(
            "addRecipe\\(Ingredient\\.of\\(\\s*HbmBlocks\\.getOverworldOre\\(OverworldOreType\\.URANIUM\\)\\.get\\(\\),[\\s\\S]*?new ItemStack\\(Blocks\\.GRAVEL\\)\\);",
            Map.of(
                "material(HbmMaterials.URANIUM, HbmMaterialShape.DUST, 1)", 2,
                "material(HbmMaterials.RA226, HbmMaterialShape.NUGGET, 1)", 1,
                "new ItemStack(Blocks.GRAVEL)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("plutonium_ore", route(
            "addRecipe\\(Ingredient\\.of\\(HbmBlocks\\.getNetherOre\\(NetherOreType\\.PLUTONIUM\\)\\.get\\(\\)\\),[\\s\\S]*?new ItemStack\\(Blocks\\.GRAVEL\\)\\);",
            Map.of(
                "material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 1)", 2,
                "material(HbmMaterials.POLONIUM, HbmMaterialShape.NUGGET, 3)", 1,
                "new ItemStack(Blocks.GRAVEL)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("nether_fire_ore", route(
            "addRecipe\\(Ingredient\\.of\\(HbmBlocks\\.getNetherOre\\(NetherOreType\\.FIRE\\)\\.get\\(\\)\\),[\\s\\S]*?new ItemStack\\(Blocks\\.NETHERRACK, 1\\)\\);",
            Map.of(
                "new ItemStack(Items.BLAZE_POWDER, 2)", 1,
                "material(HbmMaterials.RED_PHOSPHORUS, HbmMaterialShape.DUST, 2)", 1,
                "material(HbmMaterials.WHITE_PHOSPHORUS, HbmMaterialShape.INGOT, 1)", 1,
                "new ItemStack(Blocks.NETHERRACK, 1)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("rare_ore", route(
            "addRecipe\\(Ingredient\\.of\\(HbmBlocks\\.getOverworldOre\\(OverworldOreType\\.RARE\\)\\.get\\(\\)\\),[\\s\\S]*?new ItemStack\\(Blocks\\.GRAVEL\\)\\);",
            Map.of(
                "hbmItem(\"powder_desh_mix\", 1)", 1,
                "material(HbmMaterials.ZIRCONIUM, HbmMaterialShape.NUGGET, 1)", 2,
                "new ItemStack(Blocks.GRAVEL)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("crystal_trixite", route(
            "addRecipe\\(Ingredient\\.of\\(HbmItems\\.getMaterialPart\\(HbmMaterials\\.TRIXITE, HbmMaterialShape\\.CRYSTAL\\)\\.get\\(\\)\\),[\\s\\S]*?hbmItem\\(\"powder_nitan_mix\", 1\\)\\);",
            Map.of(
                "material(HbmMaterials.PLUTONIUM, HbmMaterialShape.DUST, 2)", 1,
                "material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 3)", 1,
                "material(HbmMaterials.NIOBIUM, HbmMaterialShape.DUST, 2)", 1,
                "hbmItem(\"powder_nitan_mix\", 1)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("crystal_starmetal", route(
            "addRecipe\\(Ingredient\\.of\\(HbmItems\\.getMaterialPart\\(HbmMaterials\\.STARMETAL, HbmMaterialShape\\.CRYSTAL\\)\\.get\\(\\)\\),[\\s\\S]*?new ItemStack\\(Objects\\.requireNonNull\\(HbmItems\\.INGOT_MERCURY\\.get\\(\\)\\), 5\\)\\);",
            Map.of(
                "material(HbmMaterials.DURA_STEEL, HbmMaterialShape.DUST, 3)", 1,
                "material(HbmMaterials.COBALT, HbmMaterialShape.DUST, 3)", 1,
                "material(HbmMaterials.ASTATINE, HbmMaterialShape.DUST, 2)", 1,
                "new ItemStack(Objects.requireNonNull(HbmItems.INGOT_MERCURY.get()), 5)", 1
            )
        ));
    }

    @Test
    void fixtureExists() {
        assertTrue(Files.exists(FIXTURE_PATH), "Centrifuge legacy baseline fixture must exist: " + FIXTURE_PATH);
    }

    @Test
    void fixtureHasExpectedSchemaAndRows() throws IOException {
        final var lines = Files.readAllLines(FIXTURE_PATH);
        assertFalse(lines.isEmpty(), "Fixture must contain a header row");
        assertEquals("legacy_id,input_ref,expected_output_count,notes", lines.get(0), "Fixture header must match schema");

        final Set<String> fixtureIds = new LinkedHashSet<>();
        int dataRows = 0;
        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            final String[] cells = line.split(",", -1);
            assertEquals(4, cells.length, "Each fixture row must have 4 columns: " + line);
            assertFalse(cells[0].isBlank(), "legacy_id must be non-blank");
            assertFalse(cells[1].isBlank(), "input_ref must be non-blank");
            final int outputCount = Integer.parseInt(cells[2]);
            assertTrue(outputCount > 0, "expected_output_count must be > 0");
            assertTrue(cells[3].startsWith("legacy_"), "notes column should preserve explicit legacy context");
            fixtureIds.add(cells[0]);
            dataRows++;
        }

        assertTrue(dataRows >= 6, "Fixture should include at least 6 seeded legacy entries");
        assertEquals(ROUTE_EXPECTATIONS.keySet(), fixtureIds, "Fixture IDs must align with seeded parity expectations");
    }

    @Test
    void modernCentrifugeRegistryContainsSeededLegacyRoutes() throws IOException {
        assertTrue(Files.exists(MODERN_SOURCE_PATH), "Modern centrifuge registry source must exist: " + MODERN_SOURCE_PATH);
        final String source = Files.readString(MODERN_SOURCE_PATH);

        for (final var entry : ROUTE_EXPECTATIONS.entrySet()) {
            final String routeId = entry.getKey();
            final RouteExpectation expectation = entry.getValue();

            final Matcher matcher = expectation.modernSnippetPattern().matcher(source);
            assertTrue(matcher.find(), "Missing modern route snippet for legacy route: " + routeId);
            final String snippet = matcher.group();

            for (final var token : expectation.requiredTokenCounts().entrySet()) {
                final int actual = countOccurrences(snippet, token.getKey());
                assertEquals(
                    token.getValue().intValue(),
                    actual,
                    "Route " + routeId + " expected token count mismatch for: " + token.getKey()
                );
            }
        }
    }

    private static int countOccurrences(final String haystack, final String needle) {
        int count = 0;
        int index = 0;
        while (true) {
            index = haystack.indexOf(needle, index);
            if (index < 0) {
                return count;
            }
            count++;
            index += needle.length();
        }
    }

    private static RouteExpectation route(final String regex, final Map<String, Integer> requiredTokenCounts) {
        return new RouteExpectation(Pattern.compile(regex, Pattern.DOTALL), requiredTokenCounts);
    }

    private record RouteExpectation(Pattern modernSnippetPattern, Map<String, Integer> requiredTokenCounts) {
    }
}

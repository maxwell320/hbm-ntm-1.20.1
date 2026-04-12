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
import org.junit.jupiter.api.Test;

class ShredderParityHarnessTest {
    private static final Path FIXTURE_PATH = Path.of("src", "test", "resources", "parity", "shredder-legacy-baseline.csv");
    private static final Path MODERN_SOURCE_PATH = Path.of(
        "src", "main", "java", "com", "hbm", "ntm", "common", "shredder", "HbmShredderRecipes.java"
    );

    private static final Map<String, RouteExpectation> ROUTE_EXPECTATIONS = new LinkedHashMap<>();

    static {
        ROUTE_EXPECTATIONS.put("nether_fire_ore", route(
            "setRecipe(HbmBlocks.getNetherOre(NetherOreType.FIRE).get().asItem(), new ItemStack(redPhosphorusDust, 6));",
            "setRecipe(HbmBlocks.getNetherOre(NetherOreType.FIRE).get().asItem(), new ItemStack(redPhosphorusDust, 6));",
            Map.of(
                "HbmBlocks.getNetherOre(NetherOreType.FIRE)", 1,
                "new ItemStack(redPhosphorusDust, 6)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("aluminium_ore_special", route(
            "setRecipe(HbmBlocks.getOverworldOre(OverworldOreType.ALUMINIUM).get().asItem(),",
            "new ItemStack(HbmItems.getChunkOre(ChunkOreItemType.CRYOLITE).get(), 2));",
            Map.of(
                "HbmBlocks.getOverworldOre(OverworldOreType.ALUMINIUM)", 1,
                "HbmItems.getChunkOre(ChunkOreItemType.CRYOLITE)", 1,
                ", 2))", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("canister_empty", route(
            "setRecipe(HbmItems.CANISTER_EMPTY.get(), new ItemStack(aluminiumDust, 2));",
            "setRecipe(HbmItems.CANISTER_EMPTY.get(), new ItemStack(aluminiumDust, 2));",
            Map.of(
                "HbmItems.CANISTER_EMPTY", 1,
                "new ItemStack(aluminiumDust, 2)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("skull_biomass", route(
            "for (final Item skull : new Item[] {",
            "setRecipe(skull, new ItemStack(HbmItems.BIOMASS.get(), 4));",
            Map.of(
                "Items.SKELETON_SKULL", 1,
                "Items.WITHER_SKELETON_SKULL", 1,
                "Items.PLAYER_HEAD", 1,
                "Items.ZOMBIE_HEAD", 1,
                "Items.CREEPER_HEAD", 1,
                "new ItemStack(HbmItems.BIOMASS.get(), 4)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("crystal_starmetal", route(
            "setMaterialRecipe(HbmMaterials.STARMETAL, HbmMaterialShape.CRYSTAL, HbmMaterials.STARMETAL, HbmMaterialShape.DUST, 6);",
            "setMaterialRecipe(HbmMaterials.STARMETAL, HbmMaterialShape.CRYSTAL, HbmMaterials.STARMETAL, HbmMaterialShape.DUST, 6);",
            Map.of(
                "HbmMaterials.STARMETAL", 2,
                "HbmMaterialShape.CRYSTAL", 1,
                "HbmMaterialShape.DUST", 1,
                ", 6);", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("debris_shrapnel", route(
            "setRecipe(HbmItems.DEBRIS_SHRAPNEL.get(), new ItemStack(steelTinyDust, 5));",
            "setRecipe(HbmItems.DEBRIS_SHRAPNEL.get(), new ItemStack(steelTinyDust, 5));",
            Map.of(
                "HbmItems.DEBRIS_SHRAPNEL", 1,
                "new ItemStack(steelTinyDust, 5)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("sellafield_level5", route(
            "if (input.is(HbmItems.SELLAFIELD.get())) {",
            "default -> 1;",
            Map.of(
                "case 1 -> 2;", 1,
                "case 2 -> 3;", 1,
                "case 3 -> 5;", 1,
                "case 4 -> 7;", 1,
                "case 5 -> 15;", 1,
                "default -> 1;", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("fallback_scrap", route(
            "return HbmItems.SCRAP.get().getDefaultInstance();",
            "return HbmItems.SCRAP.get().getDefaultInstance();",
            Map.of(
                "return HbmItems.SCRAP.get().getDefaultInstance();", 1
            )
        ));
    }

    @Test
    void fixtureExists() {
        assertTrue(Files.exists(FIXTURE_PATH), "Shredder legacy baseline fixture must exist: " + FIXTURE_PATH);
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

        assertTrue(dataRows >= 7, "Fixture should include at least 7 seeded legacy entries");
        assertEquals(ROUTE_EXPECTATIONS.keySet(), fixtureIds, "Fixture IDs must align with seeded parity expectations");
    }

    @Test
    void modernShredderRegistryContainsSeededLegacyRoutes() throws IOException {
        assertTrue(Files.exists(MODERN_SOURCE_PATH), "Modern shredder registry source must exist: " + MODERN_SOURCE_PATH);
        final String source = Files.readString(MODERN_SOURCE_PATH);

        for (final var entry : ROUTE_EXPECTATIONS.entrySet()) {
            final String routeId = entry.getKey();
            final RouteExpectation expectation = entry.getValue();

            final int start = source.indexOf(expectation.startFragment());
            assertTrue(start >= 0, "Missing modern route snippet for legacy route: " + routeId);

            final int end = source.indexOf(expectation.endFragment(), start);
            assertTrue(end >= start, "Could not isolate route snippet for legacy route: " + routeId);
            final String snippet = source.substring(start, end + expectation.endFragment().length());

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

    private static RouteExpectation route(final String startFragment,
                                          final String endFragment,
                                          final Map<String, Integer> requiredTokenCounts) {
        return new RouteExpectation(startFragment, endFragment, requiredTokenCounts);
    }

    private record RouteExpectation(String startFragment, String endFragment, Map<String, Integer> requiredTokenCounts) {
    }
}

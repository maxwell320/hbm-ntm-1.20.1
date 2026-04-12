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

class PressParityHarnessTest {
    private static final Path FIXTURE_PATH = Path.of("src", "test", "resources", "parity", "press-legacy-baseline.csv");
    private static final Path MODERN_SOURCE_PATH = Path.of(
        "src", "main", "java", "com", "hbm", "ntm", "common", "press", "HbmPressRecipes.java"
    );

    private static final Map<String, RouteExpectation> ROUTE_EXPECTATIONS = new LinkedHashMap<>();

    static {
        ROUTE_EXPECTATIONS.put("flat_quartz_dust", route(
            "registry.add(PressStampType.FLAT, Ingredient.of(item(HbmMaterials.QUARTZ, HbmMaterialShape.DUST)), new ItemStack(Items.QUARTZ));",
            "new ItemStack(Items.QUARTZ));",
            Map.of(
                "PressStampType.FLAT", 1,
                "item(HbmMaterials.QUARTZ, HbmMaterialShape.DUST)", 1,
                "new ItemStack(Items.QUARTZ)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("flat_any_coke", route(
            "Objects.requireNonNull(HbmItems.getCoke(CokeItemType.COAL).get())",
            "new ItemStack(item(HbmMaterials.GRAPHITE, HbmMaterialShape.INGOT)));",
            Map.of(
                "HbmItems.getCoke(CokeItemType.COAL)", 1,
                "HbmItems.getCoke(CokeItemType.LIGNITE)", 1,
                "HbmItems.getCoke(CokeItemType.PETROLEUM)", 1,
                "item(HbmMaterials.GRAPHITE, HbmMaterialShape.INGOT)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("flat_coal_dust_briquette", route(
            "Ingredient.of(item(HbmMaterials.COAL, HbmMaterialShape.DUST))",
            "HbmItems.getBriquette(BriquetteItemType.COAL).get())));",
            Map.of(
                "item(HbmMaterials.COAL, HbmMaterialShape.DUST)", 1,
                "HbmItems.getBriquette(BriquetteItemType.COAL)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("plate_saturnite", route(
            "addPlateRecipe(registry, HbmMaterials.SATURNITE);",
            "addPlateRecipe(registry, HbmMaterials.SATURNITE);",
            Map.of(
                "addPlateRecipe(registry, HbmMaterials.SATURNITE);", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("c9_gunmetal", route(
            "registry.add(PressStampType.C9,",
            "HbmItems.getCasing(CasingItemType.SMALL).get()), 4));",
            Map.of(
                "PressStampType.C9", 1,
                "item(HbmMaterials.GUNMETAL, HbmMaterialShape.PLATE)", 1,
                "HbmItems.getCasing(CasingItemType.SMALL)", 1,
                "), 4))", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("c50_weaponsteel", route(
            "registry.add(PressStampType.C50,\n            Ingredient.of(item(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.PLATE)),",
            "HbmItems.getCasing(CasingItemType.LARGE_STEEL).get()), 2));",
            Map.of(
                "PressStampType.C50", 1,
                "item(HbmMaterials.WEAPONSTEEL, HbmMaterialShape.PLATE)", 1,
                "HbmItems.getCasing(CasingItemType.LARGE_STEEL)", 1,
                "), 2))", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("wire_ingot_loop", route(
            "for (final HbmMaterialDefinition material : HbmMaterials.ordered()) {",
            "new ItemStack(item(material, HbmMaterialShape.WIRE), 8));",
            Map.of(
                "material.hasShape(HbmMaterialShape.INGOT)", 1,
                "material.hasShape(HbmMaterialShape.WIRE)", 1,
                "PressStampType.WIRE", 1,
                "new ItemStack(item(material, HbmMaterialShape.WIRE), 8)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("silicon_circuit", route(
            "registry.add(PressStampType.CIRCUIT,",
            "HbmItems.getCircuit(CircuitItemType.SILICON).get())));",
            Map.of(
                "PressStampType.CIRCUIT", 1,
                "item(HbmMaterials.SILICON, HbmMaterialShape.BILLET)", 1,
                "HbmItems.getCircuit(CircuitItemType.SILICON)", 1
            )
        ));

        ROUTE_EXPECTATIONS.put("printing_page8", route(
            "addPrintingRecipe(registry, PrintingStampType.PRINTING8, PageItemType.PAGE8);",
            "addPrintingRecipe(registry, PrintingStampType.PRINTING8, PageItemType.PAGE8);",
            Map.of(
                "addPrintingRecipe(registry, PrintingStampType.PRINTING8, PageItemType.PAGE8);", 1
            )
        ));
    }

    @Test
    void fixtureExists() {
        assertTrue(Files.exists(FIXTURE_PATH), "Press legacy baseline fixture must exist: " + FIXTURE_PATH);
    }

    @Test
    void fixtureHasExpectedSchemaAndRows() throws IOException {
        final var lines = Files.readAllLines(FIXTURE_PATH);
        assertFalse(lines.isEmpty(), "Fixture must contain a header row");
        assertEquals("legacy_id,stamp,input_ref,expected_output_ref,notes", lines.get(0), "Fixture header must match schema");

        final Set<String> fixtureIds = new LinkedHashSet<>();
        int dataRows = 0;
        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            final String[] cells = line.split(",", -1);
            assertEquals(5, cells.length, "Each fixture row must have 5 columns: " + line);
            assertFalse(cells[0].isBlank(), "legacy_id must be non-blank");
            assertFalse(cells[1].isBlank(), "stamp must be non-blank");
            assertFalse(cells[2].isBlank(), "input_ref must be non-blank");
            assertFalse(cells[3].isBlank(), "expected_output_ref must be non-blank");
            assertTrue(cells[4].startsWith("legacy_"), "notes column should preserve explicit legacy context");
            fixtureIds.add(cells[0]);
            dataRows++;
        }

        assertTrue(dataRows >= 8, "Fixture should include at least 8 seeded legacy entries");
        assertEquals(ROUTE_EXPECTATIONS.keySet(), fixtureIds, "Fixture IDs must align with seeded parity expectations");
    }

    @Test
    void modernPressRegistryContainsSeededLegacyRoutes() throws IOException {
        assertTrue(Files.exists(MODERN_SOURCE_PATH), "Modern press registry source must exist: " + MODERN_SOURCE_PATH);
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

    @Test
    void plateHelperPreservesLegacyIngotToPlateContract() throws IOException {
        final String source = Files.readString(MODERN_SOURCE_PATH);

        assertTrue(source.contains("registry.add(PressStampType.PLATE,"), "Plate helper must register against plate stamp type");
        assertTrue(source.contains("Ingredient.of(item(material, HbmMaterialShape.INGOT))"), "Plate helper must consume ingots");
        assertTrue(source.contains("new ItemStack(item(material, HbmMaterialShape.PLATE))"), "Plate helper must produce plates");
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

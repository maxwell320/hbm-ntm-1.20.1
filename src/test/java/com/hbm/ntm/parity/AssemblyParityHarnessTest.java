package com.hbm.ntm.parity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AssemblyParityHarnessTest {
    private static final Path FIXTURE_PATH = Path.of("src", "test", "resources", "parity", "assembly-legacy-baseline.csv");
    private static final Path MODERN_SOURCE_PATH = Path.of(
        "src", "main", "java", "com", "hbm", "ntm", "common", "assembly", "HbmAssemblyRecipes.java"
    );

    private static final Map<String, RouteExpectation> ROUTE_EXPECTATIONS = new LinkedHashMap<>();

    static {
        ROUTE_EXPECTATIONS.put("plate_saturnite", route(
            List.of(
                "registerPlateRecipe(registry, \"ass.platesaturnite\",",
                "materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.PLATE)",
                "materialItem(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT));"
            )
        ));

        ROUTE_EXPECTATIONS.put("fluid_pack_empty", route(
            List.of(
                "\"ass.fluid_pack_empty\",",
                "new ItemStack(item(HbmItems.FLUID_PACK_EMPTY))",
                "materialItem(HbmMaterials.TITANIUM, HbmMaterialShape.PLATE), 4",
                "materialItem(HbmMaterials.POLYMER, HbmMaterialShape.INGOT)",
                "2))));"
            )
        ));

        ROUTE_EXPECTATIONS.put("machine_shredder", route(
            List.of(
                "\"ass.shredder\",",
                "new ItemStack(item(HbmItems.MACHINE_SHREDDER))",
                "materialItem(HbmMaterials.STEEL, HbmMaterialShape.PLATE), 8",
                "materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 4",
                "Ingredient.of(item(HbmItems.MOTOR)), item(HbmItems.MOTOR), 2"
            )
        ));

        ROUTE_EXPECTATIONS.put("machine_assembler", route(
            List.of(
                "\"ass.assembler\",",
                "new ItemStack(item(HbmItems.MACHINE_ASSEMBLY_MACHINE))",
                "materialItem(HbmMaterials.STEEL, HbmMaterialShape.INGOT), 4",
                "materialItem(HbmMaterials.COPPER, HbmMaterialShape.PLATE), 4",
                "item(HbmItems.getCircuit(CircuitItemType.ANALOG)), 1"
            )
        ));

        ROUTE_EXPECTATIONS.put("machine_gascent", route(
            List.of(
                "\"ass.gascent\",",
                "new ItemStack(item(HbmItems.MACHINE_GAS_CENTRIFUGE))",
                "materialItem(HbmMaterials.DESH, HbmMaterialShape.INGOT), 2",
                "item(HbmItems.getCircuit(CircuitItemType.ADVANCED)), 1)),",
                "List.of(POOL_PREFIX_528 + \"gascent\")));"
            )
        ));

        ROUTE_EXPECTATIONS.put("filter_coal", route(
            List.of(
                "\"ass.filtercoal\",",
                "new ItemStack(item(HbmItems.FILTER_COAL))",
                "materialItem(HbmMaterials.COAL, HbmMaterialShape.DUST), 4",
                "Ingredient.of(Items.STRING), Items.STRING, 2",
                "Ingredient.of(Items.PAPER), Items.PAPER, 1"
            )
        ));

        ROUTE_EXPECTATIONS.put("drill_ferro_diamond", route(
            List.of(
                "\"ass.drillferrodiamond\",",
                "new ItemStack(item(HbmItems.DRILLBIT_FERRO_DIAMOND))",
                "Ingredient.of(item(HbmItems.DRILLBIT_FERRO)), item(HbmItems.DRILLBIT_FERRO), 1",
                "materialItem(HbmMaterials.DIAMOND, HbmMaterialShape.DUST), 56"
            )
        ));

        ROUTE_EXPECTATIONS.put("overdrive_tier3", route(
            List.of(
                "\"ass.overdrive3\",",
                "new ItemStack(item(HbmItems.UPGRADE_OVERDRIVE_3))",
                "item(HbmItems.UPGRADE_OVERDRIVE_2)), item(HbmItems.UPGRADE_OVERDRIVE_2), 1",
                "materialItem(HbmMaterials.BISMUTH_BRONZE, HbmMaterialShape.INGOT), 16",
                "Ingredient.of(item(HbmItems.INGOT_CFT)), item(HbmItems.INGOT_CFT), 16",
                "item(HbmItems.getCircuit(CircuitItemType.BISMOID)), 16"
            )
        ));
    }

    @Test
    void fixtureExists() {
        assertTrue(Files.exists(FIXTURE_PATH), "Assembly legacy baseline fixture must exist: " + FIXTURE_PATH);
    }

    @Test
    void fixtureHasExpectedSchemaAndRows() throws IOException {
        final var lines = Files.readAllLines(FIXTURE_PATH);
        assertFalse(lines.isEmpty(), "Fixture must contain a header row");
        assertEquals("legacy_id,recipe_id,expected_key_inputs,notes", lines.get(0), "Fixture header must match schema");

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
            assertFalse(cells[1].isBlank(), "recipe_id must be non-blank");
            assertTrue(Integer.parseInt(cells[2]) > 0, "expected_key_inputs must be > 0");
            assertTrue(cells[3].startsWith("legacy_"), "notes column should preserve explicit legacy context");
            fixtureIds.add(cells[0]);
            dataRows++;
        }

        assertTrue(dataRows >= 7, "Fixture should include at least 7 seeded legacy entries");
        assertEquals(ROUTE_EXPECTATIONS.keySet(), fixtureIds, "Fixture IDs must align with seeded parity expectations");
    }

    @Test
    void modernAssemblyRegistryContainsSeededLegacyRoutes() throws IOException {
        assertTrue(Files.exists(MODERN_SOURCE_PATH), "Modern assembly registry source must exist: " + MODERN_SOURCE_PATH);
        final String source = Files.readString(MODERN_SOURCE_PATH);

        for (final var entry : ROUTE_EXPECTATIONS.entrySet()) {
            final String routeId = entry.getKey();
            final RouteExpectation expectation = entry.getValue();
            int cursor = 0;
            for (final String token : expectation.orderedTokens()) {
                final int foundAt = source.indexOf(token, cursor);
                assertTrue(foundAt >= 0, "Route " + routeId + " missing expected token: " + token);
                cursor = foundAt + token.length();
            }
        }
    }

    @Test
    void plateRecipeHelperKeepsAltPoolAndAutoswitchGroup() throws IOException {
        final String source = Files.readString(MODERN_SOURCE_PATH);
        assertTrue(source.contains("List.of(POOL_ALT_PLATES),"), "Plate helper must assign alt plate pool");
        assertTrue(source.contains("GROUP_AUTOSWITCH_PLATES));"), "Plate helper must keep autoswitch plate grouping");
    }

    private static RouteExpectation route(final List<String> orderedTokens) {
        return new RouteExpectation(orderedTokens);
    }

    private record RouteExpectation(List<String> orderedTokens) {
    }
}

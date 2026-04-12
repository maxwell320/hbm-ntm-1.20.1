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

class SolderingParityHarnessTest {
    private static final Path FIXTURE_PATH = Path.of("src", "test", "resources", "parity", "soldering-legacy-baseline.csv");
    private static final Path MODERN_SOURCE_PATH = Path.of(
        "src", "main", "java", "com", "hbm", "ntm", "common", "soldering", "HbmSolderingRecipes.java"
    );

    private static final Map<String, RouteExpectation> ROUTE_EXPECTATIONS = new LinkedHashMap<>();

    static {
        ROUTE_EXPECTATIONS.put("analog_circuit", route(List.of(
            "circuit(CircuitItemType.ANALOG, 100, 100,",
            "circuitIngredient(CircuitItemType.VACUUM_TUBE, 3)",
            "circuitIngredient(CircuitItemType.CAPACITOR, 2)",
            "circuitIngredient(CircuitItemType.PCB, 4)",
            "materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 4)"
        )));

        ROUTE_EXPECTATIONS.put("advanced_circuit", route(List.of(
            "circuit(CircuitItemType.ADVANCED, new FluidStack(HbmFluids.SULFURIC_ACID.getStillFluid(), 1_000), 300, 1_000,",
            "circuitIngredient(CircuitItemType.CHIP, 16)",
            "materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 2)",
            "materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 8)"
        )));

        ROUTE_EXPECTATIONS.put("bismoid_circuit", route(List.of(
            "circuit(CircuitItemType.BISMOID, new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 1_000), 400, 10_000,",
            "circuitIngredient(CircuitItemType.CHIP_BISMOID, 4)",
            "hardPlasticIngredient(2)",
            "materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 12)"
        )));

        ROUTE_EXPECTATIONS.put("quantum_circuit", route(List.of(
            "circuit(CircuitItemType.QUANTUM, new FluidStack(HbmFluids.HELIUM4.getStillFluid(), 1_000), 400, 100_000,",
            "circuitIngredient(CircuitItemType.CHIP_QUANTUM, 4)",
            "circuitIngredient(CircuitItemType.CHIP_BISMOID, 16)",
            "circuitIngredient(CircuitItemType.ATOMIC_CLOCK, 4)",
            "hardPlasticIngredient(4)",
            "materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 16)"
        )));

        ROUTE_EXPECTATIONS.put("upgrade_speed_1", route(List.of(
            "simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_1.get()), 200, 1_000,",
            "circuitIngredient(CircuitItemType.VACUUM_TUBE, 4)",
            "simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_TEMPLATE.get()), 1)",
            "materialIngredient(HbmMaterials.RED_COPPER, HbmMaterialShape.DUST, 4)"
        )));

        ROUTE_EXPECTATIONS.put("upgrade_speed_3", route(List.of(
            "simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_3.get()), new FluidStack(HbmFluids.SOLVENT.getStillFluid(), 500), 400, 25_000,",
            "simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_2.get()), 1)",
            "materialIngredient(HbmMaterials.RUBBER, HbmMaterialShape.INGOT, 4)"
        )));

        ROUTE_EXPECTATIONS.put("controller_circuit", route(List.of(
            "circuit(CircuitItemType.CONTROLLER, new FluidStack(HbmFluids.PERFLUOROMETHYL.getStillFluid(), 1_000), 400, 15_000,",
            "circuitIngredient(CircuitItemType.CHIP, 32)",
            "circuitIngredient(CircuitItemType.CAPACITOR_TANTALIUM, 16)",
            "simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_1.get()), 1)",
            "materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 16)"
        )));

        ROUTE_EXPECTATIONS.put("controller_advanced_circuit", route(List.of(
            "circuit(CircuitItemType.CONTROLLER_ADVANCED, new FluidStack(HbmFluids.PERFLUOROMETHYL.getStillFluid(), 4_000), 600, 25_000,",
            "circuitIngredient(CircuitItemType.CHIP_BISMOID, 16)",
            "circuitIngredient(CircuitItemType.CAPACITOR_TANTALIUM, 48)",
            "circuitIngredient(CircuitItemType.ATOMIC_CLOCK, 1)",
            "simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_SPEED_3.get()), 1)",
            "materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 24)"
        )));

        ROUTE_EXPECTATIONS.put("controller_quantum_circuit", route(List.of(
            "circuit(CircuitItemType.CONTROLLER_QUANTUM, new FluidStack(HbmFluids.PERFLUOROMETHYL_COLD.getStillFluid(), 6_000), 600, 250_000,",
            "circuitIngredient(CircuitItemType.CHIP_QUANTUM, 16)",
            "circuitIngredient(CircuitItemType.CHIP_BISMOID, 48)",
            "circuitIngredient(CircuitItemType.ATOMIC_CLOCK, 8)",
            "circuitIngredient(CircuitItemType.CONTROLLER_ADVANCED, 2)",
            "simpleItemIngredient(Objects.requireNonNull(HbmItems.UPGRADE_OVERDRIVE_1.get()), 1)",
            "materialIngredient(HbmMaterials.LEAD, HbmMaterialShape.WIRE, 32)"
        )));

        ROUTE_EXPECTATIONS.put("upgrade_radius", route(List.of(
            "simpleItem(Objects.requireNonNull(HbmItems.UPGRADE_RADIUS.get()), 200, 1_000,",
            "circuitIngredient(CircuitItemType.CHIP, 4)",
            "circuitIngredient(CircuitItemType.CAPACITOR, 4)",
            "simpleItemIngredient(Items.GLOWSTONE_DUST, 4)"
        )));
    }

    @Test
    void fixtureExists() {
        assertTrue(Files.exists(FIXTURE_PATH), "Soldering legacy baseline fixture must exist: " + FIXTURE_PATH);
    }

    @Test
    void fixtureHasExpectedSchemaAndRows() throws IOException {
        final var lines = Files.readAllLines(FIXTURE_PATH);
        assertFalse(lines.isEmpty(), "Fixture must contain a header row");
        assertEquals("legacy_id,output_ref,expected_signal,notes", lines.get(0), "Fixture header must match schema");

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
            assertFalse(cells[1].isBlank(), "output_ref must be non-blank");
            assertFalse(cells[2].isBlank(), "expected_signal must be non-blank");
            assertTrue(cells[3].startsWith("legacy_"), "notes column should preserve explicit legacy context");
            fixtureIds.add(cells[0]);
            dataRows++;
        }

        assertTrue(dataRows >= 9, "Fixture should include at least 9 seeded legacy entries");
        assertEquals(ROUTE_EXPECTATIONS.keySet(), fixtureIds, "Fixture IDs must align with seeded parity expectations");
    }

    @Test
    void modernSolderingRegistryContainsSeededLegacyRoutes() throws IOException {
        assertTrue(Files.exists(MODERN_SOURCE_PATH), "Modern soldering registry source must exist: " + MODERN_SOURCE_PATH);
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
    void defaultRecipeRegistryUsesStaticSeedList() throws IOException {
        final String source = Files.readString(MODERN_SOURCE_PATH);
        assertTrue(source.contains("private static final List<SolderingRecipe> DEFAULT_RECIPES = List.of("), "Static DEFAULT_RECIPES seed list must exist");
        assertTrue(source.contains("this.addAllRecipes(DEFAULT_RECIPES);"), "Registry defaults must be populated from DEFAULT_RECIPES");
    }

    private static RouteExpectation route(final List<String> orderedTokens) {
        return new RouteExpectation(orderedTokens);
    }

    private record RouteExpectation(List<String> orderedTokens) {
    }
}

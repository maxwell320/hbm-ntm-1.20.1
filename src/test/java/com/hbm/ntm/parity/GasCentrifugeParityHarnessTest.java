package com.hbm.ntm.parity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GasCentrifugeParityHarnessTest {
    private static final Path MODERN_SOURCE_PATH = Path.of(
        "src", "main", "java", "com", "hbm", "ntm", "common", "centrifuge", "HbmGasCentrifugeRecipes.java"
    );

    private static final Map<String, PseudoFluidExpectation> EXPECTATIONS = new LinkedHashMap<>();

    static {
        EXPECTATIONS.put("HEUF6", pseudo(
            "HEUF6(\"heuf6\", 300, 0, NONE, true, 0xFFD1CEBE,",
            "MEUF6(\"meuf6\",",
            Map.of(
                "material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 2)", 1,
                "material(HbmMaterials.U235, HbmMaterialShape.NUGGET, 1)", 1,
                "material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 1)", 1
            )
        ));

        EXPECTATIONS.put("MEUF6", pseudo(
            "MEUF6(\"meuf6\", 200, 100, HEUF6, false, 0xFFD1CEBE,",
            "LEUF6(\"leuf6\",",
            Map.of(
                "material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 1)", 1
            )
        ));

        EXPECTATIONS.put("LEUF6", pseudo(
            "LEUF6(\"leuf6\", 300, 200, MEUF6, false, 0xFFD1CEBE,",
            "NUF6(\"nuf6\",",
            Map.of(
                "material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 1)", 1,
                "material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 1)", 1
            )
        ));

        EXPECTATIONS.put("NUF6", pseudo(
            "NUF6(\"nuf6\", 400, 300, LEUF6, false, 0xFFD1CEBE,",
            "PF6(\"pf6\",",
            Map.of(
                "material(HbmMaterials.U238, HbmMaterialShape.NUGGET, 1)", 1
            )
        ));

        EXPECTATIONS.put("PF6", pseudo(
            "PF6(\"pf6\", 300, 0, NONE, false, 0xFF4C4C4C,",
            "MUD_HEAVY(\"mud_heavy\",",
            Map.of(
                "material(HbmMaterials.PU238, HbmMaterialShape.NUGGET, 1)", 1,
                "material(HbmMaterials.PU_MIX, HbmMaterialShape.NUGGET, 2)", 1,
                "material(HbmMaterials.FLUORITE, HbmMaterialShape.DUST, 1)", 1
            )
        ));

        EXPECTATIONS.put("MUD_HEAVY", pseudo(
            "MUD_HEAVY(\"mud_heavy\", 500, 0, NONE, false, 0xFF86653E,",
            "MUD(\"mud\",",
            Map.of(
                "material(HbmMaterials.IRON, HbmMaterialShape.DUST, 1)", 1,
                "hbmItem(\"dust\", 1)", 1,
                "item(HbmItems.NUCLEAR_WASTE_TINY, 1)", 1
            )
        ));

        EXPECTATIONS.put("MUD", pseudo(
            "MUD(\"mud\", 1000, 500, MUD_HEAVY, false, 0xFF86653E,",
            null,
            Map.of(
                "material(HbmMaterials.LEAD, HbmMaterialShape.DUST, 1)", 1,
                "hbmItem(\"dust\", 1)", 1
            )
        ));
    }

    @Test
    void pseudoFluidDefinitionsMatchLegacyCriticalValues() throws IOException {
        assertTrue(Files.exists(MODERN_SOURCE_PATH), "Modern gas centrifuge registry source must exist: " + MODERN_SOURCE_PATH);
        final String source = Files.readString(MODERN_SOURCE_PATH);

        for (final var entry : EXPECTATIONS.entrySet()) {
            final String pseudoType = entry.getKey();
            final PseudoFluidExpectation expectation = entry.getValue();

            final int start = source.indexOf(expectation.startFragment());
            assertTrue(start >= 0, "Missing pseudo-fluid definition for " + pseudoType);

            final int end = expectation.nextStartFragment() == null
                ? source.indexOf(";", start)
                : source.indexOf(expectation.nextStartFragment(), start);
            assertTrue(end > start, "Could not isolate pseudo-fluid block for " + pseudoType);

            final String snippet = source.substring(start, end);
            for (final var token : expectation.requiredTokenCounts().entrySet()) {
                final int actual = countOccurrences(snippet, token.getKey());
                assertEquals(
                    token.getValue().intValue(),
                    actual,
                    "Pseudo-fluid " + pseudoType + " expected token count mismatch for: " + token.getKey()
                );
            }
        }
    }

    @Test
    void feedFluidMappingsMatchLegacyRouting() throws IOException {
        final String source = Files.readString(MODERN_SOURCE_PATH);

        assertTrue(source.contains("ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, \"uf6\")"), "UF6 ID constant missing");
        assertTrue(source.contains("ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, \"puf6\")"), "PUF6 ID constant missing");
        assertTrue(source.contains("ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, \"watz\")"), "WATZ ID constant missing");

        assertTrue(source.contains("if (fluidId.equals(UF6_ID))"), "UF6 mapping branch missing");
        assertTrue(source.contains("return Optional.of(PseudoFluidType.NUF6);"), "UF6 should map to NUF6");

        assertTrue(source.contains("if (fluidId.equals(PUF6_ID))"), "PUF6 mapping branch missing");
        assertTrue(source.contains("return Optional.of(PseudoFluidType.PF6);"), "PUF6 should map to PF6");

        assertTrue(source.contains("if (fluidId.equals(WATZ_ID))"), "WATZ mapping branch missing");
        assertTrue(source.contains("return Optional.of(PseudoFluidType.MUD);"), "WATZ should map to MUD");
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

    private static PseudoFluidExpectation pseudo(final String startFragment,
                                                 final String nextStartFragment,
                                                 final Map<String, Integer> requiredTokenCounts) {
        return new PseudoFluidExpectation(startFragment, nextStartFragment, requiredTokenCounts);
    }

    private record PseudoFluidExpectation(String startFragment,
                                          String nextStartFragment,
                                          Map<String, Integer> requiredTokenCounts) {
    }
}

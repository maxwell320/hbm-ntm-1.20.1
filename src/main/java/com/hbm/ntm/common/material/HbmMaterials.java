package com.hbm.ntm.common.material;

import java.util.List;

public final class HbmMaterials {
    public static final HbmMaterialDefinition URANIUM = HbmMaterialDefinition.of("uranium", "Uranium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST);
    public static final HbmMaterialDefinition THORIUM = HbmMaterialDefinition.of("thorium", "Thorium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST);
    public static final HbmMaterialDefinition PLUTONIUM = HbmMaterialDefinition.of("plutonium", "Plutonium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.BILLET, HbmMaterialShape.DUST);
    public static final HbmMaterialDefinition TITANIUM = HbmMaterialDefinition.of("titanium", "Titanium", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.PLATE);
    public static final HbmMaterialDefinition STEEL = HbmMaterialDefinition.of("steel", "Steel", HbmMaterialShape.INGOT, HbmMaterialShape.DUST, HbmMaterialShape.PLATE);
    public static final HbmMaterialDefinition SCHRABIDIUM = HbmMaterialDefinition.of("schrabidium", "Schrabidium", HbmMaterialShape.INGOT, HbmMaterialShape.NUGGET, HbmMaterialShape.DUST, HbmMaterialShape.PLATE);

    private static final List<HbmMaterialDefinition> ORDERED = List.of(
        URANIUM,
        THORIUM,
        PLUTONIUM,
        TITANIUM,
        STEEL,
        SCHRABIDIUM
    );

    private HbmMaterials() {
    }

    public static List<HbmMaterialDefinition> ordered() {
        return ORDERED;
    }
}

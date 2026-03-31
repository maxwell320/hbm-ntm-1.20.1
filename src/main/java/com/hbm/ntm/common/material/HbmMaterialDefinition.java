package com.hbm.ntm.common.material;

import java.util.List;

public record HbmMaterialDefinition(String id, String displayName, List<HbmMaterialShape> shapes) {
    public HbmMaterialDefinition {
        shapes = List.copyOf(shapes);
    }

    public static HbmMaterialDefinition of(final String id, final String displayName, final HbmMaterialShape... shapes) {
        return new HbmMaterialDefinition(id, displayName, List.of(shapes));
    }

    public String itemId(final HbmMaterialShape shape) {
        return shape.registryPrefix() + "_" + this.id;
    }

    public String itemTranslation(final HbmMaterialShape shape) {
        return this.displayName + " " + shape.displaySuffix();
    }
}

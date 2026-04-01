package com.hbm.ntm.common.material;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record HbmMaterialDefinition(String id, String displayName, List<HbmMaterialShape> shapes, Map<HbmMaterialShape, String> itemIds, Map<HbmMaterialShape, String> itemDisplayNames, boolean autoDustSmelting) {
    public HbmMaterialDefinition {
        shapes = List.copyOf(shapes);
        itemIds = Map.copyOf(itemIds);
        itemDisplayNames = Map.copyOf(itemDisplayNames);
    }

    public static HbmMaterialDefinition of(final String id, final String displayName, final HbmMaterialShape... shapes) {
        return new HbmMaterialDefinition(id, displayName, List.of(shapes), Map.of(), Map.of(), true);
    }

    public boolean hasShape(final HbmMaterialShape shape) {
        return this.shapes.contains(shape);
    }

    public HbmMaterialDefinition withItemId(final HbmMaterialShape shape, final String itemId) {
        final Map<HbmMaterialShape, String> updatedItemIds = new LinkedHashMap<>(this.itemIds);
        updatedItemIds.put(shape, itemId);
        return new HbmMaterialDefinition(this.id, this.displayName, this.shapes, updatedItemIds, this.itemDisplayNames, this.autoDustSmelting);
    }

    public HbmMaterialDefinition withItemDisplayName(final HbmMaterialShape shape, final String itemDisplayName) {
        final Map<HbmMaterialShape, String> updatedItemDisplayNames = new LinkedHashMap<>(this.itemDisplayNames);
        updatedItemDisplayNames.put(shape, itemDisplayName);
        return new HbmMaterialDefinition(this.id, this.displayName, this.shapes, this.itemIds, updatedItemDisplayNames, this.autoDustSmelting);
    }

    public HbmMaterialDefinition withoutDustSmelting() {
        return new HbmMaterialDefinition(this.id, this.displayName, this.shapes, this.itemIds, this.itemDisplayNames, false);
    }

    public String itemId(final HbmMaterialShape shape) {
        return this.itemIds.getOrDefault(shape, shape.registryPrefix() + "_" + this.id);
    }

    public String itemTranslation(final HbmMaterialShape shape) {
        return this.itemDisplayNames.getOrDefault(shape, shape.formatDisplayName(this.displayName));
    }
}

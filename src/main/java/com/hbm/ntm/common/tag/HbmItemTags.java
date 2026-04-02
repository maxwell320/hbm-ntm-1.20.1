package com.hbm.ntm.common.tag;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class HbmItemTags {
    private HbmItemTags() {
    }

    public static TagKey<Item> shape(final HbmMaterialShape shape) {
        return create(HbmNtmMod.MOD_ID, shape.tagFolder());
    }

    public static TagKey<Item> material(final HbmMaterialDefinition material) {
        return create(HbmNtmMod.MOD_ID, "materials/" + material.id());
    }

    public static TagKey<Item> materialShape(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        return create(HbmNtmMod.MOD_ID, shape.tagFolder() + "/" + material.id());
    }

    public static TagKey<Item> forgeMaterialShape(final HbmMaterialDefinition material, final HbmMaterialShape shape) {
        return create("forge", shape.tagFolder() + "/" + material.id());
    }

    public static TagKey<Item> named(final String namespace, final String path) {
        return create(namespace, path);
    }

    @SuppressWarnings("null")
    private static TagKey<Item> create(final String namespace, final String path) {
        return TagKey.create(Objects.requireNonNull(Registries.ITEM), Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(namespace, path)));
    }
}

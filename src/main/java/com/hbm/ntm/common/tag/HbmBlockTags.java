package com.hbm.ntm.common.tag;

import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class HbmBlockTags {
    private HbmBlockTags() {
    }

    public static TagKey<Block> named(final String namespace, final String path) {
        return TagKey.create(Objects.requireNonNull(Registries.BLOCK), Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(namespace, path)));
    }
}

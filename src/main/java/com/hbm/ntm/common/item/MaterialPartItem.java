package com.hbm.ntm.common.item;

import com.hbm.ntm.common.material.HbmMaterialDefinition;
import com.hbm.ntm.common.material.HbmMaterialShape;
import net.minecraft.world.item.Item;

public class MaterialPartItem extends Item {
    private final HbmMaterialDefinition material;
    private final HbmMaterialShape shape;

    public MaterialPartItem(final HbmMaterialDefinition material, final HbmMaterialShape shape, final Properties properties) {
        super(properties);
        this.material = material;
        this.shape = shape;
    }

    public HbmMaterialDefinition material() {
        return this.material;
    }

    public HbmMaterialShape shape() {
        return this.shape;
    }
}

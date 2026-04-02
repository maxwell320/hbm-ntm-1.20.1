package com.hbm.ntm.common.block;

import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

public class BasaltOreBlock extends Block {
    private final BasaltOreType type;

    public BasaltOreBlock(final BasaltOreType type, final BlockBehaviour.Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(final BlockState state, final LootParams.Builder builder) {
        return List.of(switch (this.type) {
            case SULFUR -> new ItemStack(HbmItems.getMaterialPart(HbmMaterials.SULFUR, HbmMaterialShape.DUST).get());
            case FLUORITE -> new ItemStack(HbmItems.getMaterialPart(HbmMaterials.FLUORITE, HbmMaterialShape.DUST).get());
            case ASBESTOS -> new ItemStack(HbmItems.getMaterialPart(HbmMaterials.ASBESTOS, HbmMaterialShape.INGOT).get());
            case GEM -> new ItemStack(HbmItems.getMaterialPart(HbmMaterials.VOLCANIC, HbmMaterialShape.GEM).get());
            case MOLYSITE -> new ItemStack(HbmItems.getMaterialPart(HbmMaterials.MOLYSITE, HbmMaterialShape.DUST).get());
        });
    }

    @Override
    public void stepOn(final Level level, final BlockPos pos, final BlockState state, final Entity entity) {
        if (!level.isClientSide() && this.type == BasaltOreType.ASBESTOS && level.getBlockState(pos.above()).isAir() && level.getRandom().nextInt(10) == 0) {
            level.setBlock(pos.above(), HbmBlocks.GAS_ASBESTOS.get().defaultBlockState(), Block.UPDATE_ALL);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void playerDestroy(final Level level, final Player player, final BlockPos pos, final BlockState state, final BlockEntity blockEntity, final ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        replaceWithAsbestosGas(level, pos);
    }

    @Override
    public void wasExploded(final Level level, final BlockPos pos, final Explosion explosion) {
        super.wasExploded(level, pos, explosion);
        replaceWithAsbestosGas(level, pos);
    }

    private void replaceWithAsbestosGas(final Level level, final BlockPos pos) {
        if (!level.isClientSide() && this.type == BasaltOreType.ASBESTOS) {
            level.setBlock(pos, HbmBlocks.GAS_ASBESTOS.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}

package com.hbm.ntm.common.block;

import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import java.util.Objects;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class BasaltOreBlock extends Block {
    private final BasaltOreType type;

    public BasaltOreBlock(final BasaltOreType type, final BlockBehaviour.Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    @Deprecated
    public @NotNull List<ItemStack> getDrops(final @NotNull BlockState state, final @NotNull LootParams.Builder builder) {
        return List.of(switch (this.type) {
            case SULFUR -> new ItemStack(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.SULFUR, HbmMaterialShape.DUST).get()));
            case FLUORITE -> new ItemStack(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.FLUORITE, HbmMaterialShape.DUST).get()));
            case ASBESTOS -> new ItemStack(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.ASBESTOS, HbmMaterialShape.INGOT).get()));
            case GEM -> new ItemStack(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.VOLCANIC, HbmMaterialShape.GEM).get()));
            case MOLYSITE -> new ItemStack(Objects.requireNonNull(HbmItems.getMaterialPart(HbmMaterials.MOLYSITE, HbmMaterialShape.DUST).get()));
        });
    }

    @Override
    public void stepOn(final @NotNull Level level, final @NotNull BlockPos pos, final @NotNull BlockState state, final @NotNull Entity entity) {
        final BlockPos abovePos = Objects.requireNonNull(pos.above());

        if (!level.isClientSide() && this.type == BasaltOreType.ASBESTOS && level.getBlockState(abovePos).isAir() && level.getRandom().nextInt(10) == 0) {
            level.setBlock(abovePos, Objects.requireNonNull(HbmBlocks.GAS_ASBESTOS.get().defaultBlockState()), Block.UPDATE_ALL);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void playerDestroy(final @NotNull Level level, final @NotNull Player player, final @NotNull BlockPos pos, final @NotNull BlockState state,
                              final @Nullable BlockEntity blockEntity, final @NotNull ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        replaceWithAsbestosGas(level, pos);
    }

    @Override
    public void wasExploded(final @NotNull Level level, final @NotNull BlockPos pos, final @NotNull Explosion explosion) {
        super.wasExploded(level, pos, explosion);
        replaceWithAsbestosGas(level, pos);
    }

    private void replaceWithAsbestosGas(final @NotNull Level level, final @NotNull BlockPos pos) {
        if (!level.isClientSide() && this.type == BasaltOreType.ASBESTOS) {
            level.setBlock(pos, Objects.requireNonNull(HbmBlocks.GAS_ASBESTOS.get().defaultBlockState()), Block.UPDATE_ALL);
        }
    }
}

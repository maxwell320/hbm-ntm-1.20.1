package com.hbm.ntm.common.block;

import com.hbm.ntm.common.block.entity.ChimneyBrickBlockEntity;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class ChimneyBrickBlock extends BaseEntityBlock {
    public ChimneyBrickBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull RenderShape getRenderShape(final @NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull InteractionResult use(final @NotNull BlockState state,
                                          final @NotNull Level level,
                                          final @NotNull BlockPos pos,
                                          final @NotNull Player player,
                                          final @NotNull InteractionHand hand,
                                          final @NotNull BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        return new ChimneyBrickBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(final @NotNull Level level,
                                                                             final @NotNull BlockState state,
                                                                             final @NotNull BlockEntityType<T> type) {
        return level.isClientSide()
            ? null
            : createTickerHelper(type, HbmBlockEntityTypes.CHIMNEY_BRICK.get(), ChimneyBrickBlockEntity::serverTick);
    }

    @Override
    public int getLightBlock(final @NotNull BlockState state,
                             final @NotNull BlockGetter level,
                             final @NotNull BlockPos pos) {
        return 0;
    }
}

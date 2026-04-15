package com.hbm.ntm.common.block;

import com.hbm.ntm.common.block.entity.MiniRtgBlockEntity;
import com.hbm.ntm.common.block.entity.PowerRtgBlockEntity;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class ConstantRtgBlock extends BaseEntityBlock {
    private final boolean powerVariant;

    public ConstantRtgBlock(final boolean powerVariant, final Properties properties) {
        super(properties);
        this.powerVariant = powerVariant;
    }

    @Override
    public @NotNull RenderShape getRenderShape(final @NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(final @NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos pos) {
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof final MiniRtgBlockEntity miniRtg) {
            return miniRtg.getComparatorOutput();
        }
        if (blockEntity instanceof final PowerRtgBlockEntity powerRtg) {
            return powerRtg.getComparatorOutput();
        }
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        return this.powerVariant ? new PowerRtgBlockEntity(pos, state) : new MiniRtgBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(final @NotNull Level level,
                                                                             final @NotNull BlockState state,
                                                                             final @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        if (this.powerVariant) {
            return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_POWERRTG.get(), PowerRtgBlockEntity::serverTick);
        }
        return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_MINIRTG.get(), MiniRtgBlockEntity::serverTick);
    }

    @Override
    public float getShadeBrightness(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos) {
        return 0.9F;
    }
}

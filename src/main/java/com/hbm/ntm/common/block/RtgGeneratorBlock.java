package com.hbm.ntm.common.block;

import com.hbm.ntm.common.block.entity.RtgGeneratorBlockEntity;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class RtgGeneratorBlock extends MachineBlock {
    public RtgGeneratorBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        return new RtgGeneratorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(final @NotNull Level level,
                                                                             final @NotNull BlockState state,
                                                                             final @NotNull BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, HbmBlockEntityTypes.MACHINE_RTG_GREY.get(), RtgGeneratorBlockEntity::serverTick);
    }
}

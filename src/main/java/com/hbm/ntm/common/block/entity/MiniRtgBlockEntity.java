package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("null")
public class MiniRtgBlockEntity extends AbstractConstantRtgBlockEntity {
    private static final int OUTPUT_PER_TICK = 700;
    private static final int CAPACITY = 1_400;

    public MiniRtgBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_MINIRTG.get(), pos, state, OUTPUT_PER_TICK, CAPACITY);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final MiniRtgBlockEntity blockEntity) {
        AbstractConstantRtgBlockEntity.serverTick(level, pos, state, blockEntity);
    }
}

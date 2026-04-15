package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("null")
public class PowerRtgBlockEntity extends AbstractConstantRtgBlockEntity {
    private static final int OUTPUT_PER_TICK = 2_500;
    private static final int CAPACITY = 50_000;

    public PowerRtgBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_POWERRTG.get(), pos, state, OUTPUT_PER_TICK, CAPACITY);
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final PowerRtgBlockEntity blockEntity) {
        AbstractConstantRtgBlockEntity.serverTick(level, pos, state, blockEntity);
    }
}

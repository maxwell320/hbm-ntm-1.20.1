package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("null")
public class ChimneyIndustrialBlockEntity extends ChimneyBlockEntity {
    private static final float POLLUTION_MODIFIER = 0.10F;

    public ChimneyIndustrialBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.CHIMNEY_INDUSTRIAL.get(), pos, state);
    }

    public static void serverTick(final Level level,
                                  final BlockPos pos,
                                  final BlockState state,
                                  final ChimneyIndustrialBlockEntity chimney) {
        ChimneyBlockEntity.serverTick(level, pos, state, chimney);
    }

    @Override
    protected float pollutionModifier() {
        return POLLUTION_MODIFIER;
    }

    @Override
    protected boolean capturesSootAsh() {
        return true;
    }
}

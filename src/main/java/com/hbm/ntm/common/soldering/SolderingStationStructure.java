package com.hbm.ntm.common.soldering;

import com.hbm.ntm.common.block.SolderingStationBlock;
import com.hbm.ntm.common.block.entity.SolderingStationProxyBlockEntity;
import com.hbm.ntm.common.multiblock.SimpleMultiblockStructure;
import com.hbm.ntm.common.registration.HbmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("null")
public final class SolderingStationStructure extends SimpleMultiblockStructure {
    private static final int[] DIMENSIONS = new int[]{0, 0, 1, 0, 1, 0};
    public static final SolderingStationStructure INSTANCE = new SolderingStationStructure();

    private SolderingStationStructure() {
        super(DIMENSIONS, Blocks.AIR);
    }

    public static Direction normalizeFacing(final Direction direction) {
        return direction.getAxis().isHorizontal() ? direction : Direction.NORTH;
    }

    public boolean canPlaceAt(final Level level, final BlockPos corePos, final Direction facing) {
        final int y = corePos.getY();
        if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) {
            return false;
        }
        return this.canPlace(level, corePos, normalizeFacing(facing));
    }

    @Override
    protected BlockState stateForPosition(final BlockPos corePos, final BlockPos pos, final Direction direction) {
        return HbmBlocks.MACHINE_SOLDERING_STATION.get().defaultBlockState()
            .setValue(SolderingStationBlock.FACING, normalizeFacing(direction))
            .setValue(SolderingStationBlock.PART, pos.equals(corePos) ? SolderingStationPart.CORE : SolderingStationPart.PROXY);
    }

    @Override
    protected boolean isExpectedState(final Level level, final BlockPos corePos, final BlockPos pos, final Direction direction) {
        final BlockState actual = level.getBlockState(pos);
        if (!actual.is(HbmBlocks.MACHINE_SOLDERING_STATION.get())) {
            return false;
        }
        return actual.getValue(SolderingStationBlock.FACING) == normalizeFacing(direction)
            && actual.getValue(SolderingStationBlock.PART) == (pos.equals(corePos) ? SolderingStationPart.CORE : SolderingStationPart.PROXY);
    }

    @Override
    protected void afterPlace(final Level level, final BlockPos corePos, final BlockPos pos, final Direction direction) {
        if (pos.equals(corePos)) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof final SolderingStationProxyBlockEntity proxy) {
            proxy.setCorePos(corePos);
            proxy.setDirection(normalizeFacing(direction));
        }
    }
}


package com.hbm.ntm.common.press;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("null")
public final class PressStructure {
    public static final int HEIGHT = 3;

    private PressStructure() {
    }

    public static List<BlockPos> positions(final BlockPos corePos) {
        return List.of(corePos, corePos.above(), corePos.above(2));
    }

    public static boolean canPlaceAt(final Level level, final BlockPos corePos) {
        if (corePos.getY() < level.getMinBuildHeight() || corePos.getY() + 2 >= level.getMaxBuildHeight()) {
            return false;
        }
        for (final BlockPos pos : positions(corePos)) {
            final BlockState state = level.getBlockState(pos);
            if (!pos.equals(corePos) && !state.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    public static BlockPos partPos(final BlockPos corePos, final PressPart part) {
        return switch (part) {
            case CORE -> corePos;
            case MIDDLE -> corePos.above();
            case TOP -> corePos.above(2);
        };
    }

    public static BlockPos corePos(final BlockPos anyPartPos, final PressPart part) {
        return switch (part) {
            case CORE -> anyPartPos;
            case MIDDLE -> anyPartPos.below();
            case TOP -> anyPartPos.below(2);
        };
    }

    public static Direction normalizeFacing(final Direction direction) {
        return direction.getAxis().isHorizontal() ? direction : Direction.NORTH;
    }
}

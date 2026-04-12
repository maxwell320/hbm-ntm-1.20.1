package com.hbm.ntm.common.press;

import com.hbm.ntm.common.block.PressBlock;
import com.hbm.ntm.common.block.entity.PressProxyBlockEntity;
import com.hbm.ntm.common.multiblock.SimpleMultiblockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public final class PressStructure extends SimpleMultiblockStructure {
    private static final int[] DIMENSIONS = new int[]{2, 0, 0, 0, 0, 0};
    public static final PressStructure INSTANCE = new PressStructure();
    public static final int HEIGHT = 3;

    private PressStructure() {
        super(DIMENSIONS, Blocks.AIR);
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

    public boolean canPlaceAt(final Level level, final BlockPos corePos, final Direction facing) {
        if (corePos.getY() < level.getMinBuildHeight() || corePos.getY() + 2 >= level.getMaxBuildHeight()) {
            return false;
        }
        return this.canPlace(level, corePos, facing);
    }

    @Override
    protected BlockState stateForPosition(final BlockPos corePos, final BlockPos pos, final Direction direction) {
        return com.hbm.ntm.common.registration.HbmBlocks.MACHINE_PRESS.get().defaultBlockState()
            .setValue(PressBlock.FACING, normalizeFacing(direction))
            .setValue(PressBlock.PART, this.partForPosition(corePos, pos));
    }

    @Override
    protected boolean isExpectedState(final Level level, final BlockPos corePos, final BlockPos pos, final Direction direction) {
        final BlockState actual = level.getBlockState(pos);
        if (!actual.is(com.hbm.ntm.common.registration.HbmBlocks.MACHINE_PRESS.get())) {
            return false;
        }
        return actual.getValue(PressBlock.FACING) == normalizeFacing(direction)
            && actual.getValue(PressBlock.PART) == this.partForPosition(corePos, pos);
    }

    @Override
    protected void afterPlace(final Level level, final BlockPos corePos, final BlockPos pos, final Direction direction) {
        if (pos.equals(corePos)) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof PressProxyBlockEntity proxy) {
            proxy.setCorePos(corePos);
            proxy.setDirection(normalizeFacing(direction));
        }
    }

    private @NotNull PressPart partForPosition(final BlockPos corePos, final BlockPos pos) {
        final int yOffset = pos.getY() - corePos.getY();
        return switch (yOffset) {
            case 0 -> PressPart.CORE;
            case 1 -> PressPart.MIDDLE;
            case 2 -> PressPart.TOP;
            default -> throw new IllegalArgumentException("Invalid press part position: " + pos + " for core " + corePos);
        };
    }
}

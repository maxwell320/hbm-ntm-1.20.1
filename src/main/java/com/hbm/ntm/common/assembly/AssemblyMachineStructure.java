package com.hbm.ntm.common.assembly;

import com.hbm.ntm.common.block.AssemblyMachineBlock;
import com.hbm.ntm.common.block.entity.AssemblyMachineProxyBlockEntity;
import com.hbm.ntm.common.registration.HbmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("null")
public final class AssemblyMachineStructure extends com.hbm.ntm.common.multiblock.SimpleMultiblockStructure {
    private static final int[] DIMENSIONS = new int[]{2, 0, 1, 1, 1, 1};
    public static final AssemblyMachineStructure INSTANCE = new AssemblyMachineStructure();

    private AssemblyMachineStructure() {
        super(DIMENSIONS, Blocks.AIR);
    }

    public static Direction normalizeFacing(final Direction direction) {
        return direction.getAxis().isHorizontal() ? direction : Direction.NORTH;
    }

    public boolean canPlaceAt(final Level level, final BlockPos placedPos, final Direction facing) {
        final int y = placedPos.getY();
        if (y < level.getMinBuildHeight() || y + 2 >= level.getMaxBuildHeight()) {
            return false;
        }
        return this.canPlace(level, placedPos, normalizeFacing(facing));
    }

    @Override
    public BlockPos getCorePosition(final BlockPos placedPos, final Direction direction) {
        return placedPos.relative(normalizeFacing(direction).getOpposite());
    }

    @Override
    protected BlockState stateForPosition(final BlockPos corePos, final BlockPos pos, final Direction direction) {
        return HbmBlocks.MACHINE_ASSEMBLY_MACHINE.get().defaultBlockState()
            .setValue(AssemblyMachineBlock.FACING, normalizeFacing(direction))
            .setValue(AssemblyMachineBlock.PART, pos.equals(corePos) ? AssemblyMachinePart.CORE : AssemblyMachinePart.PROXY);
    }

    @Override
    protected boolean isExpectedState(final Level level, final BlockPos corePos, final BlockPos pos, final Direction direction) {
        final BlockState actual = level.getBlockState(pos);
        if (!actual.is(HbmBlocks.MACHINE_ASSEMBLY_MACHINE.get())) {
            return false;
        }
        return actual.getValue(AssemblyMachineBlock.FACING) == normalizeFacing(direction)
            && actual.getValue(AssemblyMachineBlock.PART) == (pos.equals(corePos) ? AssemblyMachinePart.CORE : AssemblyMachinePart.PROXY);
    }

    @Override
    protected void afterPlace(final Level level, final BlockPos corePos, final BlockPos pos, final Direction direction) {
        if (pos.equals(corePos)) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof AssemblyMachineProxyBlockEntity proxy) {
            proxy.setCorePos(corePos);
            proxy.setDirection(normalizeFacing(direction));
        }
    }
}

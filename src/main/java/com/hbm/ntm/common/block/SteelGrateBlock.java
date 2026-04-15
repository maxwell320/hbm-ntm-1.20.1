package com.hbm.ntm.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class SteelGrateBlock extends Block {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 9);
    private static final double EPSILON = 1.0E-6D;

    private final boolean wide;
    private final VoxelShape[] shapes;

    public SteelGrateBlock(final boolean wide, final Properties properties) {
        super(properties);
        this.wide = wide;
        this.shapes = createShapes(wide);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        int level = placementLevel(context);
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            if (level == 0 && hasLowerAnchor(context.getLevel(), context.getClickedPos())) {
                level = 9;
            } else if (level == 7 && hasUpperAnchor(context.getLevel(), context.getClickedPos())) {
                level = 8;
            }
        }
        return this.defaultBlockState().setValue(LEVEL, level);
    }

    @Override
    public @NotNull BlockState updateShape(final @NotNull BlockState state,
                                           final @NotNull Direction direction,
                                           final @NotNull BlockState neighborState,
                                           final @NotNull LevelAccessor level,
                                           final @NotNull BlockPos pos,
                                           final @NotNull BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public boolean canSurvive(final @NotNull BlockState state, final @NotNull LevelReader level, final @NotNull BlockPos pos) {
        final int levelIndex = state.getValue(LEVEL);
        if (levelIndex == 8) {
            return hasUpperAnchor(level, pos);
        }
        if (levelIndex == 9) {
            return hasLowerAnchor(level, pos);
        }
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(final @NotNull BlockState state,
                                        final @NotNull BlockGetter level,
                                        final @NotNull BlockPos pos,
                                        final @NotNull CollisionContext context) {
        return this.shapes[state.getValue(LEVEL)];
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(final @NotNull BlockState state,
                                                 final @NotNull BlockGetter level,
                                                 final @NotNull BlockPos pos,
                                                 final @NotNull CollisionContext context) {
        return this.shapes[state.getValue(LEVEL)];
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public void entityInside(final @NotNull BlockState state,
                             final @NotNull Level level,
                             final @NotNull BlockPos pos,
                             final @NotNull Entity entity) {
        if (!this.wide) {
            return;
        }
        if (!(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrb)) {
            return;
        }

        final double y = entity.getY();
        final double threshold = pos.getY() + yOffset(state.getValue(LEVEL)) + 0.375D;
        if (y >= threshold) {
            return;
        }

        entity.setDeltaMovement(0.0D, -0.25D, 0.0D);
        entity.setPos(entity.getX(), y - 0.125D, entity.getZ());
    }

    private static int placementLevel(final BlockPlaceContext context) {
        final Direction side = context.getClickedFace();
        if (side == Direction.DOWN) {
            return 7;
        }
        if (side == Direction.UP) {
            return 0;
        }

        final double y = context.getClickLocation().y - context.getClickedPos().getY();
        return Math.max(0, Math.min(7, (int) Math.floor(y * 8.0D)));
    }

    private static boolean hasLowerAnchor(final BlockGetter level, final BlockPos pos) {
        final BlockPos belowPos = pos.below();
        final VoxelShape shape = level.getBlockState(belowPos).getCollisionShape(level, belowPos);
        if (shape.isEmpty()) {
            return false;
        }

        return shape.bounds().minY >= 0.125D - EPSILON;
    }

    private static boolean hasUpperAnchor(final BlockGetter level, final BlockPos pos) {
        final BlockPos abovePos = pos.above();
        final VoxelShape shape = level.getBlockState(abovePos).getCollisionShape(level, abovePos);
        if (shape.isEmpty()) {
            return false;
        }

        return shape.bounds().maxY <= 0.875D + EPSILON;
    }

    private static VoxelShape[] createShapes(final boolean wide) {
        final VoxelShape[] result = new VoxelShape[10];
        final double thickness = wide ? 1.984D : 2.0D;

        for (int level = 0; level < result.length; level++) {
            final double minY = level == 9 ? -2.0D : level * 2.0D;
            result[level] = Block.box(0.0D, minY, 0.0D, 16.0D, minY + thickness, 16.0D);
        }

        return result;
    }

    private static double yOffset(final int level) {
        return level == 9 ? -0.125D : level * 0.125D;
    }
}

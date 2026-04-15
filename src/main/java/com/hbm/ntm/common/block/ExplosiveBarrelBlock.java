package com.hbm.ntm.common.block;

import com.hbm.ntm.common.registration.HbmBlocks;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class ExplosiveBarrelBlock extends Block {
    private static final VoxelShape SHAPE = box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private final ExplosiveBarrelType type;

    public ExplosiveBarrelBlock(final ExplosiveBarrelType type, final BlockBehaviour.Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public @NotNull VoxelShape getShape(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos,
                                        final @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos,
                                                 final @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public boolean useShapeForLightOcclusion(final @NotNull BlockState state) {
        return true;
    }

    @Override
    public void wasExploded(final @NotNull Level level, final @NotNull BlockPos pos, final @NotNull Explosion explosion) {
        if (level.isClientSide()) {
            super.wasExploded(level, pos, explosion);
            return;
        }
        detonate(level, pos);
    }

    @Override
    public void onProjectileHit(final @NotNull Level level, final @NotNull BlockState state, final @NotNull BlockHitResult hit,
                                final @NotNull Projectile projectile) {
        if (!this.type.projectileSensitive() || level.isClientSide()) {
            return;
        }
        detonate(level, hit.getBlockPos());
    }

    private void detonate(final Level level, final BlockPos pos) {
        if (level.getBlockState(pos).getBlock() != this) {
            return;
        }

        level.removeBlock(pos, false);
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D;
        final double z = pos.getZ() + 0.5D;

        switch (this.type) {
            case RED, PINK -> level.explode(null, x, y, z, 2.5F, true, Level.ExplosionInteraction.BLOCK);
            case LOX -> {
                level.explode(null, x, y, z, 1.0F, false, Level.ExplosionInteraction.NONE);
                applyCryogenicBurst(level, pos, 7, level.random);
            }
            case TAINT -> {
                level.explode(null, x, y, z, 1.0F, false, Level.ExplosionInteraction.NONE);
                applyTaintResidue(level, pos, level.random);
            }
        }
    }

    private static void applyCryogenicBurst(final Level level, final BlockPos center, final int radius, final RandomSource random) {
        final MutableBlockPos cursor = new MutableBlockPos();
        final BlockState snow = Blocks.SNOW.defaultBlockState();
        final int radiusSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    cursor.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (center.distSqr(cursor) > radiusSq) {
                        continue;
                    }

                    final BlockState state = level.getBlockState(cursor);
                    if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) {
                        level.setBlock(cursor, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                        continue;
                    }

                    final FluidState fluidState = state.getFluidState();
                    if (fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
                        level.setBlock(cursor, Blocks.ICE.defaultBlockState(), Block.UPDATE_ALL);
                        continue;
                    }

                    if (fluidState.is(FluidTags.LAVA)) {
                        final BlockState frozen = fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState();
                        level.setBlock(cursor, frozen, Block.UPDATE_ALL);
                        continue;
                    }

                    if (!level.isEmptyBlock(cursor) || random.nextInt(8) != 0) {
                        continue;
                    }

                    if (snow.canSurvive(level, cursor)) {
                        level.setBlock(cursor, snow, Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    private static void applyTaintResidue(final Level level, final BlockPos center, final RandomSource random) {
        final BlockState fallout = Objects.requireNonNull(HbmBlocks.FALLOUT.get().defaultBlockState());

        for (int i = 0; i < 100; i++) {
            final int x = random.nextInt(9) - 4;
            final int y = random.nextInt(9) - 4;
            final int z = random.nextInt(9) - 4;
            final BlockPos target = center.offset(x, y, z);

            if (level.isEmptyBlock(target)) {
                continue;
            }

            final BlockPos residuePos = target.above();
            if (!level.isEmptyBlock(residuePos)) {
                continue;
            }

            if (fallout.canSurvive(level, residuePos)) {
                level.setBlock(residuePos, fallout, Block.UPDATE_ALL);
            }
        }
    }

    public enum ExplosiveBarrelType {
        RED(true),
        PINK(true),
        LOX(false),
        TAINT(false);

        private final boolean projectileSensitive;

        ExplosiveBarrelType(final boolean projectileSensitive) {
            this.projectileSensitive = projectileSensitive;
        }

        public boolean projectileSensitive() {
            return this.projectileSensitive;
        }
    }
}
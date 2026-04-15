package com.hbm.ntm.common.block;

import com.hbm.ntm.common.radiation.ChunkRadiationManager;
import com.hbm.ntm.common.registration.HbmBlocks;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class WasteBarrelBlock extends Block {
    private static final VoxelShape SHAPE = box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final int TICK_RATE = 20;
    private final WasteBarrelType type;

    public WasteBarrelBlock(final WasteBarrelType type, final BlockBehaviour.Properties properties) {
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
    public void onPlace(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos pos,
                        final @NotNull BlockState oldState, final boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.is(state.getBlock())) {
            level.scheduleTick(pos, this, TICK_RATE);
        }
    }

    @Override
    public void tick(final @NotNull BlockState state, final @NotNull ServerLevel level, final @NotNull BlockPos pos,
                     final @NotNull RandomSource random) {
        ChunkRadiationManager.incrementRad(level, pos.getX(), pos.getY(), pos.getZ(), this.type.passiveRadiation());
        level.scheduleTick(pos, this, TICK_RATE);
    }

    @Override
    public void animateTick(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos pos,
                            final @NotNull RandomSource random) {
        if (random.nextInt(4) != 0) {
            return;
        }
        final double x = pos.getX() + 0.25D + random.nextDouble() * 0.5D;
        final double y = pos.getY() + 1.1D;
        final double z = pos.getZ() + 0.25D + random.nextDouble() * 0.5D;
        level.addParticle(ParticleTypes.ASH, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void wasExploded(final @NotNull Level level, final @NotNull BlockPos pos, final @NotNull Explosion explosion) {
        if (!level.isClientSide() && this.type.volatileOnExplosion()) {
            this.detonate(level, pos);
            return;
        }
        super.wasExploded(level, pos, explosion);
    }

    private void detonate(final Level level, final BlockPos pos) {
        final RandomSource random = level.random;
        level.removeBlock(pos, false);

        if (random.nextInt(3) == 0) {
            placeFallout(level, pos);
        } else {
            level.explode(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 12.0F, Level.ExplosionInteraction.BLOCK);
        }

        ChunkRadiationManager.incrementRad(level, pos.getX(), pos.getY(), pos.getZ(), 35.0F);
        final BlockState falloutState = Objects.requireNonNull(HbmBlocks.FALLOUT.get().defaultBlockState());

        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    if (random.nextInt(5) != 0) {
                        continue;
                    }
                    final BlockPos targetPos = pos.offset(x, y, z);
                    if (!level.isEmptyBlock(targetPos)) {
                        continue;
                    }
                    if (falloutState.canSurvive(level, targetPos)) {
                        level.setBlock(targetPos, falloutState, Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    private static void placeFallout(final Level level, final BlockPos pos) {
        final BlockState falloutState = Objects.requireNonNull(HbmBlocks.FALLOUT.get().defaultBlockState());
        if (level.isEmptyBlock(pos) && falloutState.canSurvive(level, pos)) {
            level.setBlock(pos, falloutState, Block.UPDATE_ALL);
        }
    }

    public enum WasteBarrelType {
        YELLOW(5.0F, true),
        VITRIFIED(0.5F, false);

        private final float passiveRadiation;
        private final boolean volatileOnExplosion;

        WasteBarrelType(final float passiveRadiation, final boolean volatileOnExplosion) {
            this.passiveRadiation = passiveRadiation;
            this.volatileOnExplosion = volatileOnExplosion;
        }

        public float passiveRadiation() {
            return this.passiveRadiation;
        }

        public boolean volatileOnExplosion() {
            return this.volatileOnExplosion;
        }
    }
}
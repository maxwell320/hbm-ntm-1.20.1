package com.hbm.ntm.common.block;

import com.hbm.ntm.common.radiation.RadiationUtil;
import com.hbm.ntm.common.registration.HbmMobEffects;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class FalloutBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public FalloutBlock() {
        super(Objects.requireNonNull(BlockBehaviour.Properties.copy(Blocks.SNOW).strength(0.1F).sound(SoundType.GRAVEL).noOcclusion().replaceable()));
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
    public boolean canSurvive(final @NotNull BlockState state, final @NotNull LevelReader level, final @NotNull BlockPos pos) {
        final BlockPos belowPos = pos.below();
        final BlockState belowState = level.getBlockState(belowPos);
        if (belowState.is(Blocks.ICE) || belowState.is(Blocks.PACKED_ICE)) {
            return false;
        }
        if (belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
            return true;
        }
        return belowState.is(BlockTags.LEAVES);
    }

    @Override
    public @NotNull BlockState updateShape(final @NotNull BlockState state, final @NotNull Direction direction, final @NotNull BlockState neighborState,
                                           final @NotNull LevelAccessor level, final @NotNull BlockPos pos, final @NotNull BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void stepOn(final @NotNull Level level, final @NotNull BlockPos pos, final @NotNull BlockState state, final @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof final LivingEntity livingEntity) {
            if (!(livingEntity instanceof Player player) || !player.isCreative()) {
                final MobEffectInstance effect = new MobEffectInstance(HbmMobEffects.RADIATION.get(), 10 * 60 * 20, 0);
                effect.setCurativeItems(List.of());
                livingEntity.addEffect(effect);
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void attack(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos pos, final @NotNull Player player) {
        if (!level.isClientSide()) {
            RadiationUtil.addContaminationEffect(player, new RadiationUtil.ContaminationEffect(1.0F, 200, false));
        }
        super.attack(state, level, pos, player);
    }
}

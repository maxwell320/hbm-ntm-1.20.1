package com.hbm.ntm.common.block;

import com.hbm.ntm.common.block.entity.PressBlockEntity;
import com.hbm.ntm.common.block.entity.PressProxyBlockEntity;
import com.hbm.ntm.common.press.PressPart;
import com.hbm.ntm.common.press.PressStructure;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class PressBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<PressPart> PART = EnumProperty.create("part", PressPart.class);
    private static boolean removingStructure;

    public PressBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(PART, PressPart.CORE));
    }

    @Override
    public RenderShape getRenderShape(final BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public PushReaction getPistonPushReaction(final BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        if (!PressStructure.canPlaceAt(level, pos)) {
            return null;
        }
        return this.defaultBlockState()
            .setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(PART, PressPart.CORE);
    }

    @Override
    public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, final @Nullable LivingEntity placer, final ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }
        final Direction facing = state.getValue(FACING);
        level.setBlock(pos.above(), state.setValue(PART, PressPart.MIDDLE), Block.UPDATE_ALL);
        level.setBlock(pos.above(2), state.setValue(PART, PressPart.TOP), Block.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof final PressBlockEntity core) {
            core.setChanged();
            core.syncToClient();
        }
        this.configureProxy(level, pos.above(), pos, facing);
        this.configureProxy(level, pos.above(2), pos, facing);
    }

    @Override
    public @NotNull InteractionResult use(final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        final BlockPos corePos = this.resolveCorePos(level, pos, state);
        if (corePos == null || !(level.getBlockEntity(corePos) instanceof final PressBlockEntity press) || !(player instanceof final ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }
        NetworkHooks.openScreen(serverPlayer, press, corePos);
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(final BlockState state, final Level level, final BlockPos pos, final BlockState newState, final boolean movedByPiston) {
        if (state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, movedByPiston);
            return;
        }
        if (!level.isClientSide() && !removingStructure) {
            final BlockPos corePos = this.resolveCorePos(level, pos, state);
            if (corePos != null) {
                final BlockEntity coreEntity = level.getBlockEntity(corePos);
                if (coreEntity instanceof final PressBlockEntity press) {
                    press.dropContents();
                    if (!corePos.equals(pos)) {
                        popResource(level, corePos, new ItemStack(this));
                    }
                }
                removingStructure = true;
                for (final BlockPos partPos : PressStructure.positions(corePos)) {
                    if (!partPos.equals(pos) && level.getBlockState(partPos).is(this)) {
                        level.removeBlock(partPos, false);
                    }
                }
                removingStructure = false;
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return state.getValue(PART) == PressPart.CORE ? new PressBlockEntity(pos, state) : new PressProxyBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(final Level level, final BlockState state, final BlockEntityType<T> type) {
        if (level.isClientSide() || state.getValue(PART) != PressPart.CORE) {
            return null;
        }
        return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_PRESS.get(), PressBlockEntity::serverTick);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean hasAnalogOutputSignal(final BlockState state) {
        return state.getValue(PART) == PressPart.CORE;
    }

    @Override
    public int getAnalogOutputSignal(final BlockState state, final Level level, final BlockPos pos) {
        final BlockPos corePos = this.resolveCorePos(level, pos, state);
        if (corePos != null && level.getBlockEntity(corePos) instanceof final PressBlockEntity press) {
            return press.getComparatorOutput();
        }
        return 0;
    }

    private void configureProxy(final Level level, final BlockPos pos, final BlockPos corePos, final Direction facing) {
        if (level.getBlockEntity(pos) instanceof final PressProxyBlockEntity proxy) {
            proxy.setCorePos(corePos);
            proxy.setDirection(facing);
        }
    }

    private @Nullable BlockPos resolveCorePos(final BlockGetter level, final BlockPos pos, final BlockState state) {
        final PressPart part = state.getValue(PART);
        if (part == PressPart.CORE) {
            return pos;
        }
        if (level.getBlockEntity(pos) instanceof final PressProxyBlockEntity proxy && proxy.getCorePos() != null) {
            return proxy.getCorePos();
        }
        final BlockPos guessed = PressStructure.corePos(pos, part);
        return level.getBlockState(guessed).is(this) ? guessed : null;
    }
}

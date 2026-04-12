package com.hbm.ntm.common.block;

import com.hbm.ntm.common.block.entity.IcfLaserComponentBlockEntity;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class IcfLaserComponentBlock extends BaseEntityBlock {
    public static final EnumProperty<IcfLaserComponentPart> PART = EnumProperty.create("part", IcfLaserComponentPart.class);

    public IcfLaserComponentBlock(final Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, IcfLaserComponentPart.CASING));
    }

    @Override
    public @NotNull RenderShape getRenderShape(final @NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        return new IcfLaserComponentBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(final @NotNull Level level,
                                                                             final @NotNull BlockState state,
                                                                             final @NotNull BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, HbmBlockEntityTypes.ICF_LASER_COMPONENT.get(), IcfLaserComponentBlockEntity::serverTick);
    }
}
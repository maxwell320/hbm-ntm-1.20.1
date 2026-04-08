package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class PressProxyBlockEntity extends BlockEntity {
    private BlockPos corePos = BlockPos.ZERO;
    private Direction direction = Direction.NORTH;

    public PressProxyBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_PRESS_PROXY.get(), pos, state);
    }

    public @Nullable BlockPos getCorePos() {
        return this.corePos.equals(BlockPos.ZERO) ? null : this.corePos;
    }

    public void setCorePos(final BlockPos corePos) {
        this.corePos = corePos.immutable();
        this.setChanged();
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(final CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("coreX", this.corePos.getX());
        tag.putInt("coreY", this.corePos.getY());
        tag.putInt("coreZ", this.corePos.getZ());
        tag.putInt("direction", this.direction.ordinal());
    }

    @Override
    public void load(final CompoundTag tag) {
        super.load(tag);
        this.corePos = new BlockPos(tag.getInt("coreX"), tag.getInt("coreY"), tag.getInt("coreZ"));
        this.direction = Direction.from3DDataValue(tag.getInt("direction"));
    }
}

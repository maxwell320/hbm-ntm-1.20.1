package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.machine.IMachineControlReceiver;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public abstract class MachineBlockEntity extends BlockEntity implements MenuProvider, IMachineControlReceiver {
    private final ItemStackHandler items;
    private final Map<Direction, LazyOptional<IItemHandler>> sidedItemCapabilities = new EnumMap<>(Direction.class);
    private LazyOptional<IItemHandler> itemCapability = LazyOptional.empty();
    private boolean muffled;

    protected MachineBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState state, final int slotCount) {
        super(type, pos, state);
        this.items = new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(final int slot) {
                MachineBlockEntity.this.onInventoryChanged(slot);
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.createCapabilities();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemCapability.invalidate();
        this.sidedItemCapabilities.values().forEach(LazyOptional::invalidate);
        this.sidedItemCapabilities.clear();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.createCapabilities();
    }

    @Override
    protected void saveAdditional(final CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("items", this.items.serializeNBT());
        tag.putBoolean("muffled", this.muffled);
        this.saveMachineData(tag);
    }

    @Override
    public void load(final CompoundTag tag) {
        super.load(tag);
        if (tag.contains("items", CompoundTag.TAG_COMPOUND)) {
            this.items.deserializeNBT(tag.getCompound("items"));
        }
        this.muffled = tag.getBoolean("muffled");
        this.loadMachineData(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(final CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void onDataPacket(final Connection connection, final ClientboundBlockEntityDataPacket packet) {
        if (packet.getTag() != null) {
            this.load(packet.getTag());
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return side == null ? this.itemCapability.cast() : this.sidedItemCapabilities.getOrDefault(side, LazyOptional.empty()).cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean canPlayerControl(final Player player) {
        return !this.isRemoved() && player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void receiveControl(final CompoundTag data) {
        this.applyControlData(data);
        this.setChanged();
        this.syncToClient();
    }

    public ItemStackHandler getInternalItemHandler() {
        return this.items;
    }

    public boolean isMuffled() {
        return this.muffled;
    }

    public void setMuffled(final boolean muffled) {
        this.muffled = muffled;
        this.setChanged();
        this.syncToClient();
    }

    public void syncToClient() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void dropContents() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        for (int slot = 0; slot < this.items.getSlots(); slot++) {
            Containers.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), this.items.getStackInSlot(slot));
        }
    }

    public int getComparatorOutput() {
        int filled = 0;
        int nonEmpty = 0;
        for (int slot = 0; slot < this.items.getSlots(); slot++) {
            final net.minecraft.world.item.ItemStack stack = this.items.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            filled += Math.max(1, stack.getCount() * 64 / Math.max(1, stack.getMaxStackSize()));
            nonEmpty++;
        }
        if (nonEmpty <= 0) {
            return 0;
        }
        return Math.min(15, Math.max(1, filled / Math.max(1, this.items.getSlots())));
    }

    public boolean canInsertIntoSlot(final int slot, final net.minecraft.world.item.ItemStack stack, final @Nullable Direction side) {
        return this.isItemValid(slot, stack);
    }

    public boolean canExtractFromSlot(final int slot, final @Nullable Direction side) {
        return true;
    }

    public boolean isItemValid(final int slot, final net.minecraft.world.item.ItemStack stack) {
        return true;
    }

    protected void onInventoryChanged(final int slot) {
        this.setChanged();
    }

    protected void createCapabilities() {
        this.itemCapability = LazyOptional.of(() -> this.items);
        this.sidedItemCapabilities.clear();
        for (final Direction direction : Direction.values()) {
            this.sidedItemCapabilities.put(direction, LazyOptional.of(() -> new MachineSidedItemHandler(this, direction)));
        }
    }

    protected void saveMachineData(final CompoundTag tag) {
    }

    protected void loadMachineData(final CompoundTag tag) {
    }

    protected void applyControlData(final CompoundTag data) {
    }
}

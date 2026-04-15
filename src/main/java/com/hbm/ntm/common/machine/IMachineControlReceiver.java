package com.hbm.ntm.common.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface IMachineControlReceiver {
    boolean canPlayerControl(Player player);

    default boolean isControlDataAllowed(final Player player, final CompoundTag data) {
        return data != null && !data.isEmpty();
    }

    void receiveControl(CompoundTag data);

    default void receiveControl(final Player player, final CompoundTag data) {
        this.receiveControl(data);
    }
}

package com.hbm.ntm.common.block;

import com.hbm.ntm.common.block.entity.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class DiFurnaceExtensionBlock extends Block {
    public DiFurnaceExtensionBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos pos,
                                          final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        final BlockPos corePos = pos.below();
        final BlockEntity core = level.getBlockEntity(corePos);
        if (core instanceof final MachineBlockEntity machine && machine.tryRepairInteraction(player, player.getItemInHand(hand))) {
            return InteractionResult.CONSUME;
        }
        if (!(core instanceof final MenuProvider menuProvider) || !(player instanceof final ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        NetworkHooks.openScreen(serverPlayer, menuProvider, corePos);
        return InteractionResult.CONSUME;
    }
}
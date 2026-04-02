package com.hbm.ntm.common.block;

import com.hbm.ntm.common.item.ChunkOreItemType;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class StoneResourceBlock extends Block {
    private final StoneResourceType type;

    public StoneResourceBlock(final StoneResourceType type, final BlockBehaviour.Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(final BlockState state, final LootParams.Builder builder) {
        if (this.type == StoneResourceType.MALACHITE) {
            final ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);
            final int fortune = tool == null ? 0 : EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
            final int count = 3 + fortune + builder.getLevel().getRandom().nextInt(fortune + 2);
            return List.of(new ItemStack(HbmItems.getChunkOre(ChunkOreItemType.MALACHITE).get(), count));
        }

        return List.of(new ItemStack(this));
    }

    @Override
    public void playerDestroy(final Level level, final Player player, final BlockPos pos, final BlockState state, final BlockEntity blockEntity, final ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        replaceWithAsbestosGas(level, pos);
    }

    @Override
    public void wasExploded(final Level level, final BlockPos pos, final Explosion explosion) {
        super.wasExploded(level, pos, explosion);
        replaceWithAsbestosGas(level, pos);
    }

    private void replaceWithAsbestosGas(final Level level, final BlockPos pos) {
        if (!level.isClientSide() && this.type == StoneResourceType.ASBESTOS) {
            level.setBlock(pos, HbmBlocks.GAS_ASBESTOS.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}

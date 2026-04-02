package com.hbm.ntm.common.item;

import com.hbm.ntm.common.registration.HbmItems;
import com.hbm.ntm.common.registration.HbmMobEffects;
import com.hbm.ntm.common.registration.HbmSoundEvents;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RadawayItem extends Item {
    private final int durationTicks;

    public RadawayItem(final int durationTicks, final Properties properties) {
        super(properties);
        this.durationTicks = durationTicks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            final int existingDuration = player.hasEffect(HbmMobEffects.RADAWAY.get()) ? Objects.requireNonNull(player.getEffect(HbmMobEffects.RADAWAY.get())).getDuration() : 0;
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(HbmMobEffects.RADAWAY.get(), existingDuration + durationTicks, 0));
            level.playSound(null, player.blockPosition(), HbmSoundEvents.ITEM_RADAWAY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                final ItemStack emptyBag = new ItemStack(Objects.requireNonNull(HbmItems.IV_EMPTY.get()));
                if (stack.isEmpty()) {
                    return InteractionResultHolder.sidedSuccess(emptyBag, level.isClientSide());
                }
                if (!player.getInventory().add(emptyBag)) {
                    player.drop(emptyBag, false);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(final ItemStack stack, final @Nullable Level level, final List<Component> tooltip, final TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId() + ".desc"));
    }
}

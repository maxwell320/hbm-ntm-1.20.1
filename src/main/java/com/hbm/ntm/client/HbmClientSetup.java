package com.hbm.ntm.client;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.client.screen.NtmAnvilScreen;
import com.hbm.ntm.common.block.SellafieldBlock;
import com.hbm.ntm.common.block.SellafieldOreType;
import com.hbm.ntm.common.item.MaterialPartItem;
import com.hbm.ntm.common.item.SellafieldBlockItem;
import com.hbm.ntm.common.material.LegacyMaterialColors;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.hbm.ntm.common.registration.HbmFluids;
import com.hbm.ntm.common.registration.HbmItems;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import java.util.Objects;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@SuppressWarnings("null")
public final class HbmClientSetup {
    private HbmClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            final RenderType translucent = Objects.requireNonNull(RenderType.translucent());
            MenuScreens.register(HbmMenuTypes.NTM_ANVIL.get(), NtmAnvilScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Objects.requireNonNull(HbmFluids.VOLCANIC_LAVA.getStillFluid()), translucent);
            ItemBlockRenderTypes.setRenderLayer(Objects.requireNonNull(HbmFluids.VOLCANIC_LAVA.getFlowingFluid()), translucent);
            ItemBlockRenderTypes.setRenderLayer(Objects.requireNonNull(HbmFluids.RAD_LAVA.getStillFluid()), translucent);
            ItemBlockRenderTypes.setRenderLayer(Objects.requireNonNull(HbmFluids.RAD_LAVA.getFlowingFluid()), translucent);
        });
        HbmNtmMod.LOGGER.debug("Client setup event received for {}", HbmNtmMod.MOD_ID);
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? SellafieldBlock.colorFromState(state) : -1,
            HbmBlocks.SELLAFIELD.get());
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? com.hbm.ntm.common.block.SellafieldSlakedBlock.colorFromState(state) : -1,
            HbmBlocks.SELLAFIELD_SLAKED.get());

        for (final SellafieldOreType type : SellafieldOreType.values()) {
            event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? com.hbm.ntm.common.block.SellafieldSlakedBlock.colorFromState(state) : -1,
                HbmBlocks.getSellafieldOre(type).get());
        }
    }

    @SubscribeEvent
    public static void onRegisterItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex != 0 || !(stack.getItem() instanceof MaterialPartItem materialPartItem) || !materialPartItem.shape().usesLegacySharedTexture()) {
                return 0xFFFFFF;
            }
            return LegacyMaterialColors.sharedPartTint(materialPartItem.material());
        }, HbmItems.creativeTabEntries().stream().map(registryObject -> (ItemLike) registryObject.get()).toArray(ItemLike[]::new));
        event.register((stack, tintIndex) -> tintIndex == 0 ? SellafieldBlock.colorFromLevel(SellafieldBlockItem.getLevel(stack)) : -1,
            HbmItems.SELLAFIELD.get());
    }
}

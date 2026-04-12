package com.hbm.ntm.client.renderer.blockentity;

import com.hbm.ntm.common.block.entity.PressBlockEntity;
import com.hbm.ntm.common.registration.HbmBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("null")
public class PressBlockEntityRenderer implements BlockEntityRenderer<PressBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;
    private final ItemRenderer itemRenderer;

    public PressBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(final PressBlockEntity press, final float partialTick, final PoseStack poseStack, final MultiBufferSource bufferSource,
                       final int packedLight, final int packedOverlay) {
        final float progress = Mth.clamp((float) press.getPressTicks() / (float) Math.max(1, press.configuredMaxPress()), 0.0F, 1.0F);
        this.renderPressHead(poseStack, bufferSource, packedLight, packedOverlay, progress);
        this.renderInputItem(press, poseStack, bufferSource, packedLight, packedOverlay);
    }

    private void renderPressHead(final PoseStack poseStack, final MultiBufferSource bufferSource,
                                 final int packedLight, final int packedOverlay, final float progress) {
        final float yOffset = (1.0F - progress) * 0.875F;

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.1D + yOffset, 0.5D);
        poseStack.scale(0.8F, 0.25F, 0.8F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        this.blockRenderer.renderSingleBlock(HbmBlocks.MACHINE_PRESS.get().defaultBlockState(), poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }

    private void renderInputItem(final PressBlockEntity press, final PoseStack poseStack, final MultiBufferSource bufferSource,
                                 final int packedLight, final int packedOverlay) {
        final ItemStack input = press.getInternalItemHandler().getStackInSlot(PressBlockEntity.SLOT_INPUT);
        if (input.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.02D, 0.5D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.65F, 0.65F, 0.65F);
        this.itemRenderer.renderStatic(input, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, press.getLevel(), 0);
        poseStack.popPose();
    }
}

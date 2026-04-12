package com.hbm.ntm.client.renderer.blockentity;

import com.hbm.ntm.common.block.IcfControllerBlock;
import com.hbm.ntm.common.block.entity.IcfControllerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@SuppressWarnings("null")
public class IcfControllerBlockEntityRenderer implements BlockEntityRenderer<IcfControllerBlockEntity> {
    public IcfControllerBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(final IcfControllerBlockEntity controller, final float partialTick, final PoseStack poseStack, final MultiBufferSource bufferSource,
                       final int packedLight, final int packedOverlay) {
        final int beamLength = controller.getBeamLength();
        if (beamLength <= 0) {
            return;
        }

        final long maxPower = Math.max(1L, controller.getMaxBeamPower());
        final float intensity = Math.max(0.1F, Math.min(1.0F, (float) controller.getBeamPower() / (float) maxPower));
        final int alpha = Math.max(80, Math.min(255, (int) (intensity * 255.0F)));
        final Direction facing = controller.getBlockState().getValue(IcfControllerBlock.FACING);

        final float startX = 0.5F;
        final float startY = 0.5F;
        final float startZ = 0.5F;
        final float endX = startX + facing.getStepX() * beamLength;
        final float endY = startY + facing.getStepY() * beamLength;
        final float endZ = startZ + facing.getStepZ() * beamLength;
        final float nx = facing.getStepX();
        final float ny = facing.getStepY();
        final float nz = facing.getStepZ();

        final VertexConsumer lineConsumer = bufferSource.getBuffer(RenderType.lines());
        final Matrix4f pose = poseStack.last().pose();
        final Matrix3f normal = poseStack.last().normal();

        this.addLine(lineConsumer, pose, normal, startX, startY, startZ, endX, endY, endZ, nx, ny, nz, 32, 32, 32, alpha);
        this.addLine(lineConsumer, pose, normal, startX, startY + 0.04F, startZ, endX, endY + 0.04F, endZ, nx, ny, nz, 16, 0, 0, alpha);
        this.addLine(lineConsumer, pose, normal, startX, startY - 0.04F, startZ, endX, endY - 0.04F, endZ, nx, ny, nz, 16, 0, 0, alpha);
        this.addLine(lineConsumer, pose, normal, startX + 0.04F * nz, startY, startZ + 0.04F * nx,
            endX + 0.04F * nz, endY, endZ + 0.04F * nx, nx, ny, nz, 48, 8, 8, alpha);
        this.addLine(lineConsumer, pose, normal, startX - 0.04F * nz, startY, startZ - 0.04F * nx,
            endX - 0.04F * nz, endY, endZ - 0.04F * nx, nx, ny, nz, 48, 8, 8, alpha);
    }

    private void addLine(final VertexConsumer consumer,
                         final Matrix4f pose,
                         final Matrix3f normal,
                         final float startX,
                         final float startY,
                         final float startZ,
                         final float endX,
                         final float endY,
                         final float endZ,
                         final float nx,
                         final float ny,
                         final float nz,
                         final int red,
                         final int green,
                         final int blue,
                         final int alpha) {
        consumer.vertex(pose, startX, startY, startZ)
            .color(red, green, blue, alpha)
            .normal(normal, nx, ny, nz)
            .endVertex();
        consumer.vertex(pose, endX, endY, endZ)
            .color(red, green, blue, alpha)
            .normal(normal, nx, ny, nz)
            .endVertex();
    }
}
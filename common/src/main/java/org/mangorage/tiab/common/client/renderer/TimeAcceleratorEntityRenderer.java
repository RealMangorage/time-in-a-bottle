package org.mangorage.tiab.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;

public class TimeAcceleratorEntityRenderer extends EntityRenderer<TimeAcceleratorEntity> {
    private static final BlockFaceTextRenderer textRenderer = BlockFaceTextRenderer.create();

    public TimeAcceleratorEntityRenderer(EntityRendererProvider.Context erp) {
        super(erp);
    }

    @Override
    public void render(TimeAcceleratorEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
        String timeRate = "x" + 2 * entity.getTimeRate();
        float paddingLeftRight = 2 * entity.getTimeRate() < 10 ? 0.11F : 0.19F;

        int remainingTimeSeconds = entity.getRemainingTime() / 20;
        String timeRemaining = remainingTimeSeconds + "s";

        var rendererText = textRenderer.of(poseStack, bufferSource);

        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRate, packedLightIn, 16777215, paddingLeftRight, 0.064F, 0.51F); // Render Time Rate
        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRemaining, packedLightIn, remainingTimeSeconds > 10 ? 16777215 : 16711680, paddingLeftRight, 0.264F, 0.51F); // Render Time Remaining, goes reed when < 10 seconds, otherwise white text.
    }

    @Override
    public ResourceLocation getTextureLocation(TimeAcceleratorEntity entity) {
        return new ResourceLocation("");
    }
}

package org.mangorage.tiab.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;

public final class TimeAcceleratorEntityRenderer extends EntityRenderer<TimeAcceleratorEntity> {
    private static final BlockFaceTextRenderer textRenderer = BlockFaceTextRenderer.create();
	
	private static final float TEXT_1PX_BELOW_MIDDLE = 0.4375F;
	private static final float TEXT_1PX_ABOVE_MIDDLE = 0.7063F;
	
	private static final float PADDING_2CHAR = 0.11F;
	private static final float PADDING_3CHAR = 0.19F;

    public TimeAcceleratorEntityRenderer(EntityRendererProvider.Context erp) {
        super(erp);
    }

    @Override
    public void render(TimeAcceleratorEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
        String timeRate = "x" + 2 * entity.getTimeRate();
        float paddingLeftRightMult = 2 * entity.getTimeRate() < 10 ? PADDING_2CHAR : PADDING_3CHAR;

        int remainingTimeSeconds = entity.getRemainingTime() / 20;
        String timeRemaining = remainingTimeSeconds + "s";
        float paddingLeftRightTime = remainingTimeSeconds < 10 ? PADDING_2CHAR : PADDING_3CHAR;

        var rendererText = textRenderer.of(poseStack, bufferSource);

        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRate, packedLightIn, ChatFormatting.WHITE.getColor(), paddingLeftRightMult, TEXT_1PX_BELOW_MIDDLE, 0.51F); // Render Time Rate
        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRemaining, packedLightIn, remainingTimeSeconds > 10 ? ChatFormatting.WHITE.getColor() : ChatFormatting.RED.getColor(), paddingLeftRightTime, TEXT_1PX_ABOVE_MIDDLE, 0.51F); // Render Time Remaining, goes reed when < 10 seconds, otherwise white text.
    }

    @Override
    public ResourceLocation getTextureLocation(TimeAcceleratorEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("tiab", "accelerate");
    }
}

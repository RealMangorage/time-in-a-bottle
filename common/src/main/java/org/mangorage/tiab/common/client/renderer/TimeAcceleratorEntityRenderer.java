package org.mangorage.tiab.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;

public final class TimeAcceleratorEntityRenderer extends EntityRenderer<TimeAcceleratorEntity, TiabRenderState> {
    private static final BlockFaceTextRenderer textRenderer = BlockFaceTextRenderer.create();
	
	private static final float TEXT_1PX_BELOW_MIDDLE = 0.4375F;
	private static final float TEXT_1PX_ABOVE_MIDDLE = 0.7063F;
	
	private static final float PADDING_2CHAR = 0.11F;
	private static final float PADDING_3CHAR = 0.19F;

    public TimeAcceleratorEntityRenderer(EntityRendererProvider.Context erp) {
        super(erp);
    }

    @Override
    public TiabRenderState createRenderState() {
        return new TiabRenderState();
    }


    @Override
    public void submit(TiabRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
//        String timeRate = "x" + 2 * state.timeRate;
//        float paddingLeftRightMult = 2 * state.timeRate < 10 ? PADDING_2CHAR : PADDING_3CHAR;
//
//        int remainingTimeSeconds = state.timeRemaining / 20;
//        String timeRemaining = remainingTimeSeconds + "s";
//        float paddingLeftRightTime = remainingTimeSeconds < 10 ? PADDING_2CHAR : PADDING_3CHAR;
//
//
//        var packedLightIn = state.lightCoords;
//        var rendererText = textRenderer.of(stack, null);
//
//        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRate,  packedLightIn, ChatFormatting.WHITE.getColor(), paddingLeftRightMult, TEXT_1PX_BELOW_MIDDLE, 0.51F); // Render Time Rate
//        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRemaining, packedLightIn, remainingTimeSeconds > 10 ? ChatFormatting.WHITE.getColor() : ChatFormatting.RED.getColor(), paddingLeftRightTime, TEXT_1PX_ABOVE_MIDDLE, 0.51F); // Render Time Remaining, goes reed when < 10 seconds, otherwise white text.
        super.submit(state, stack, collector, cameraRenderState);
    }

    @Override
    public void extractRenderState(TimeAcceleratorEntity entity, TiabRenderState state, float randomFloat) {
        super.extractRenderState(entity, state, randomFloat);
        state.timeRate = entity.getTimeRate();
        state.timeRemaining = entity.getRemainingTime();
    }

    @Override
    protected void finalizeRenderState(TimeAcceleratorEntity entity, TiabRenderState state) {
        super.finalizeRenderState(entity, state);
    }






    //    @Override
//    public void render(TimeAcceleratorEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
//        String timeRate = "x" + 2 * entity.getTimeRate();
//        float paddingLeftRightMult = 2 * entity.getTimeRate() < 10 ? PADDING_2CHAR : PADDING_3CHAR;
//
//        int remainingTimeSeconds = entity.getRemainingTime() / 20;
//        String timeRemaining = remainingTimeSeconds + "s";
//        float paddingLeftRightTime = remainingTimeSeconds < 10 ? PADDING_2CHAR : PADDING_3CHAR;
//
//        var rendererText = textRenderer.of(poseStack, bufferSource);
//
//        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRate, packedLightIn, ChatFormatting.WHITE.getColor(), paddingLeftRightMult, TEXT_1PX_BELOW_MIDDLE, 0.51F); // Render Time Rate
//        rendererText.render(BlockFaceTextRenderer.Face.valuesList(), timeRemaining, packedLightIn, remainingTimeSeconds > 10 ? ChatFormatting.WHITE.getColor() : ChatFormatting.RED.getColor(), paddingLeftRightTime, TEXT_1PX_ABOVE_MIDDLE, 0.51F); // Render Time Remaining, goes reed when < 10 seconds, otherwise white text.
//    }
//
//    @Override
//    public ResourceLocation getTextureLocation(TimeAcceleratorEntity entity) {
//        return ResourceLocation.fromNamespaceAndPath("tiab", "accelerate");
//    }
}

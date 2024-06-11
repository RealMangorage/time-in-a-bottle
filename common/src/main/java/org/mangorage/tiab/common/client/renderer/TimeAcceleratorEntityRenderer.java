package org.mangorage.tiab.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;

public class TimeAcceleratorEntityRenderer extends EntityRenderer<TimeAcceleratorEntity> {

    enum Face {
        FRONT((v, x, y, z) -> {
            return v.set(-x, y, z);
        }, Axis.YP.rotationDegrees(0)),
        BACK((v, x, y, z) -> {
            return v.set(x, y, -z);
        }, Axis.YP.rotationDegrees(180F)),
        RIGHT((v, x, y, z) -> {
            return v.set(z, y, x);
        }, Axis.YP.rotationDegrees(90F)),
        LEFT((v, x, y, z) -> {
            return v.set(-z, y, -x);
        }, Axis.YP.rotationDegrees(-90F)),
        TOP((v, x, y, z) -> {
            return v.set(-x, z, -y);
        }, Axis.XP.rotationDegrees(90F)),
        BOTTON((v, x, y, z) -> {
            return v.set(-x, -z, y);
        }, Axis.XP.rotationDegrees(-90F));

        private final QuadFunction<Vector3f, Float, Float, Float, Vector3f> function;
        private final Quaternionf rotate;

        Face(QuadFunction<Vector3f, Float, Float, Float, Vector3f> function, Quaternionf rotate) {
            this.function = function;
            this.rotate = rotate;
        }

        public Vector3f of(Vector3f vector3f, float x, float y, float z) {
            return function.apply(vector3f, x, y, z);
        }

        public Quaternionf axis() {
            return rotate;
        }
    }

    @FunctionalInterface
    public interface QuadFunction<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);
    }


    private final Vector3f vector3f = new Vector3f();
    public TimeAcceleratorEntityRenderer(EntityRendererProvider.Context erp) {
        super(erp);
    }

    @Override
    public void render(TimeAcceleratorEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn) {
        // Show The Rate
        String timeRate = "x" + 2 * entity.getTimeRate();
        float paddingLeftRight = 2 * entity.getTimeRate() < 10 ? 0.11F : 0.19F;

        String timeRemaining = entity.getRemainingTime() / 20 + "s";

        for (Face value : Face.values()) {

            // Display time rate
            drawText(
                    matrixStack,
                    bufferIn,
                    timeRate,
                    value.of(vector3f, paddingLeftRight, 0.064F, 0.51F),
                    value.axis(),
                    ChatFormatting.WHITE.getColor()
            );

            // Display Time Remaining
            drawText(
                    matrixStack,
                    bufferIn,
                    timeRemaining,
                    value.of(vector3f, paddingLeftRight, 0.264F, 0.51F),
                    value.axis(),
                    ChatFormatting.WHITE.getColor()
            );
        }
    }

    @Override
    public ResourceLocation getTextureLocation(TimeAcceleratorEntity entity) {
        return new ResourceLocation("");
    }

    private void drawText(PoseStack matrixStack, MultiBufferSource source, String text, Vector3f translateVector, Quaternionf rotate, int color) {
        matrixStack.pushPose();
        matrixStack.translate(translateVector.x(), translateVector.y(), translateVector.z());
        matrixStack.scale(0.02F, -0.02F, 0.02F);
        matrixStack.mulPose(rotate);
        getFont().drawInBatch(
                text,
                0,
                0,
                -1,
                false,
                matrixStack.last().pose(),
                source,
                Font.DisplayMode.NORMAL,
                0,
                color
        );
        matrixStack.popPose();
    }
}

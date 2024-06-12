package org.mangorage.tiab.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public record BlockFaceTextRenderer(Font font, Vector3f vector3f) {
    public static BlockFaceTextRenderer create(Font font) {
        return new BlockFaceTextRenderer(font, new Vector3f());
    }

    public static BlockFaceTextRenderer create() {
        return create(Minecraft.getInstance().font);
    }

    public IDrawText of(PoseStack poseStack, MultiBufferSource multiBufferSource) {
        return (face, text, packed, color, x, y, z) -> {
            drawText(font, poseStack, multiBufferSource, text, face.of(vector3f, x, y, z), face.axis(), packed, color);
        };
    }

    private static void drawText(Font font, PoseStack matrixStack, MultiBufferSource source, String text, Vector3f translateVector, Quaternionf rotate, int pPackedLightCoords, int color) {
        matrixStack.pushPose();
        matrixStack.translate(translateVector.x(), translateVector.y(), translateVector.z());
        matrixStack.scale(0.02F, -0.02F, 0.02F);
        matrixStack.mulPose(rotate);
        font.drawInBatch(
                text, // Text
                0, // pX
                0, // pY
                color, // pColor
                false, // pDropShadow
                matrixStack.last().pose(), // matrix4f
                source, // MultiBufferSource
                Font.DisplayMode.NORMAL, // DisplayMode
                0, // Background Color
                pPackedLightCoords // pPackedLightsCoords
        );
        matrixStack.popPose();
    }

    public enum Face {
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

        private static final List<Face> FACES = List.of(values());

        public static List<Face> valuesList() {
            return FACES;
        }

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

    @FunctionalInterface
    public interface IDrawText {
        void render(Face face, String text, int packedLightCoords, int color, float x, float y, float z);

        default void render(List<Face> faces, String text, int packedLightCoords, int color, float x, float y, float z) {
            faces.forEach(face -> render(face, text, packedLightCoords, color, x, y, z));
        }
    }
}

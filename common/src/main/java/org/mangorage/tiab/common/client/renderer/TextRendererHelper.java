package org.mangorage.tiab.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public record TextRendererHelper() {
    private static final Vector3f vector3f = new Vector3f();

    public static void submitText(PoseStack poseStack, SubmitNodeCollector collector, Face face, String text, boolean dropShadows, int packedLightCoords, int color, int backgroundcolor, float x, float y, float z, float offsetX, float offsetY) {
        submitText(poseStack, collector, text, face.of(vector3f, x, y, z), face.axis(), packedLightCoords, dropShadows, color, backgroundcolor, offsetX, offsetY);
    }

    public static void submitText(PoseStack poseStack, SubmitNodeCollector collector, List<Face> faces, String text, boolean dropShadows, int packedLightCoords, int color, int backgroundcolor, float x, float y, float z) {
        faces.forEach(face -> submitText(poseStack, collector, face, text, dropShadows, packedLightCoords, color, backgroundcolor, x, y, z, 0, 0));
    }

    private static void submitText(PoseStack stack, SubmitNodeCollector collector, String text, Vector3f translateVector, Quaternionf rotate, int pPackedLightCoords, boolean dropShadows, int color, int backgroundColor, float oX, float oY) {
        stack.pushPose();
        stack.translate(translateVector.x(), translateVector.y(), translateVector.z());
        stack.scale(0.02F, -0.02F, 0.02F);
        stack.mulPose(rotate);

        collector.submitText(
                stack,
                oY,
                oX,
                FormattedCharSequence.forward(text, Style.EMPTY),
                dropShadows,
                Font.DisplayMode.NORMAL,
                backgroundColor,
                color,
                pPackedLightCoords,
                0
        );

        stack.popPose();
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
            return v.set(-x, z + 0.5F, -y + 0.5F);
        }, Axis.XP.rotationDegrees(90F)),
        BOTTOM((v, x, y, z) -> {
            return v.set(-x, -z + 0.5F, y - 0.5F);
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
}

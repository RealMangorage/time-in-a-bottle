package org.mangorage.tiab.common.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.lang.Styles;
import org.mangorage.tiab.common.lang.Translation;

import java.util.function.Function;

public final class CommonHelper {

    public static Component getTotalTimeTranslated(int totalAccumulatedTime) {
        int totalAccumulatedTimeSeconds = totalAccumulatedTime / 20;
        int totalAccumulatedHours = totalAccumulatedTimeSeconds / 3600;
        int totalAccumulatedMinutes = (totalAccumulatedTimeSeconds % 3600) / 60;
        int totalAccumulatedSeconds = totalAccumulatedTimeSeconds % 60;

        return Translation.TOOLTIP_TOTAL_ACCUMULATED_TIME.componentTranslation(String.format("%02d", totalAccumulatedHours), String.format("%02d", totalAccumulatedMinutes), String.format("%02d", totalAccumulatedSeconds)).setStyle(Styles.GRAY);
    }

    public static Component getStoredTimeTranslated(int storedTime) {
        int storedSeconds = storedTime / 20;
        int hours = storedSeconds / 3600;
        int minutes = (storedSeconds % 3600) / 60;
        int seconds = storedSeconds % 60;

        return Translation.TOOLTIP_STORED_TIME.componentTranslation(String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds)).setStyle(Styles.GREEN);
    }

    public static <T> T modify(ItemStack stack, DataComponentType<T> dataComponentType, T defaultComponent, Function<T, T> function) {
        var comp = stack.getOrDefault(dataComponentType, defaultComponent);
        stack.set(dataComponentType, function.apply(comp));
        return stack.get(dataComponentType);
    }

    public static boolean isPositive(int number) {
        return number >= 0;
    }

    public static void sendStatusMessage(ServerPlayer serverPlayer, String message) {
        serverPlayer.displayClientMessage(Component.literal(message), true);
    }

    public static <T> ResourceKey<T> getResourceKey(ResourceKey<? extends Registry<T>> registry, String id) {
        return ResourceKey.create(
                registry,
                Identifier.fromNamespaceAndPath(
                        CommonConstants.MODID,
                        id
                )
        );
    }

    CommonHelper() {}
}

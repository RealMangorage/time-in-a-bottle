package org.mangorage.tiab.common.misc;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.lang.Styles;
import org.mangorage.tiab.common.lang.Translation;

import java.util.function.Function;
import java.util.function.Supplier;

public final class CommonHelper {

    public static Component getTotalTimeTranslated(ItemStack stack) {
        int totalAccumulatedTime = stack.getOrDefault(ICommonTimeInABottleAPI.COMMON_API.getDirect().getRegistration().getStoredTime(), new StoredTimeComponent(0, 0)).total();
        int totalAccumulatedTimeSeconds = totalAccumulatedTime / 20;
        int totalAccumulatedHours = totalAccumulatedTimeSeconds / 3600;
        int totalAccumulatedMinutes = (totalAccumulatedTimeSeconds % 3600) / 60;
        int totalAccumulatedSeconds = totalAccumulatedTimeSeconds % 60;

        return Translation.TOOLTIP_TOTAL_ACCUMULATED_TIME.componentTranslation(String.format("%02d", totalAccumulatedHours), String.format("%02d", totalAccumulatedMinutes), String.format("%02d", totalAccumulatedSeconds)).setStyle(Styles.GRAY);
    }

    public static Component getStoredTimeTranslated(ItemStack stack) {
        int storedTime = stack.getOrDefault(ICommonTimeInABottleAPI.COMMON_API.getDirect().getRegistration().getStoredTime(), new StoredTimeComponent(0, 0)).stored();
        int storedSeconds = storedTime / 20;
        int hours = storedSeconds / 3600;
        int minutes = (storedSeconds % 3600) / 60;
        int seconds = storedSeconds % 60;

        return Translation.TOOLTIP_STORED_TIME.componentTranslation(String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds)).setStyle(Styles.GREEN);
    }

    public static <T> void modify(ItemStack stack, DataComponentType<T> dataComponentType, Supplier<T> defaultComponent, Function<T, T> function) {
        var comp = stack.getOrDefault(dataComponentType, defaultComponent.get());
        stack.set(dataComponentType, function.apply(comp));
    }

    public static boolean isPositive(int number) {
        return !(number < 0);
    }

    public static void sendStatusMessage(ServerPlayer serverPlayer, String message) {
        serverPlayer.displayClientMessage(Component.literal(message), true);
    }
}

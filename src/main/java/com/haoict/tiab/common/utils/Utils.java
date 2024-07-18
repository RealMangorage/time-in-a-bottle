package com.haoict.tiab.common.utils;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.utils.lang.Styles;
import com.haoict.tiab.common.utils.lang.Translation;
import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class Utils {
    private static ITimeInABottleAPI API;

    public static void setAPI(ITimeInABottleAPI api) {
        if (API != null) return;
        API = api;
    }

    public static Component getTotalTimeTranslated(ItemStack stack) {
        int totalAccumulatedTime = API.getTotalTime(stack);
        int totalAccumulatedTimeSeconds = totalAccumulatedTime / Constants.TICK_CONST;
        int totalAccumulatedHours = totalAccumulatedTimeSeconds / 3600;
        int totalAccumulatedMinutes = (totalAccumulatedTimeSeconds % 3600) / 60;
        int totalAccumulatedSeconds = totalAccumulatedTimeSeconds % 60;

        return Translation.TOOLTIP_TOTAL_ACCUMULATED_TIME.componentTranslation(String.format("%02d", totalAccumulatedHours), String.format("%02d", totalAccumulatedMinutes), String.format("%02d", totalAccumulatedSeconds)).setStyle(Styles.GRAY);
    }

    public static Component getStoredTimeTranslated(ItemStack stack) {
        int storedTime = API.getStoredTime(stack);
        int storedSeconds = storedTime / Constants.TICK_CONST;
        int hours = storedSeconds / 3600;
        int minutes = (storedSeconds % 3600) / 60;
        int seconds = storedSeconds % 60;

        return Translation.TOOLTIP_STORED_TIME.componentTranslation(String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds)).setStyle(Styles.GREEN);
    }
}

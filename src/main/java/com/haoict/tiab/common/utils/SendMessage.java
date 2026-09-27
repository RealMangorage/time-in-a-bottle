package com.haoict.tiab.common.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SendMessage {
    public static void sendStatusMessage(ServerPlayer serverPlayer, String message) {
        if (serverPlayer == null) return;
        serverPlayer.displayClientMessage(Component.literal(message), true);
    }
}

package org.mangorage.tiab.common.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SendMessage {
    public static void sendStatusMessage(ServerPlayer serverPlayer, String message) {
        serverPlayer.displayClientMessage(Component.literal(message), true);
    }
}

package com.magorage.tiab.api.events;

import com.magorage.tiab.api.HandledState;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface ITimeCommandEvent {
    boolean accept(ServerPlayer player, int time, boolean isAdd, HandledState handledState);
}

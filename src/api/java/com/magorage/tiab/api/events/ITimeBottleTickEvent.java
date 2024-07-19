package com.magorage.tiab.api.events;

import com.magorage.tiab.api.HandledState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ITimeBottleTickEvent {
    boolean accept(ServerPlayer player, ItemStack bottle, HandledState handledState);
}

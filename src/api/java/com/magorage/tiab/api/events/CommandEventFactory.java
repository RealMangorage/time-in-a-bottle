package com.magorage.tiab.api.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public sealed class CommandEventFactory permits TimeCommandEvent.APIProvider {
    public TimeCommandEvent createEvent(ItemStack stack, ServerPlayer player, int time, boolean isAdd) {
        return null;
    }
}

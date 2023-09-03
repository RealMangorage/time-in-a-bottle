package com.haoict.tiab.common.core.api.interfaces;

import com.magorage.tiab.api.events.TimeCommandEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface ITimeInABottleCommandEventAPI {
    TimeCommandEvent createEvent(ItemStack stack, ServerPlayer player, int time, boolean isAdd);
}

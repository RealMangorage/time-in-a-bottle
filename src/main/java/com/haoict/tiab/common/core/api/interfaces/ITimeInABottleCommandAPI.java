package com.haoict.tiab.common.core.api.interfaces;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public interface ITimeInABottleCommandAPI {
    int processCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, String messageValue, boolean isAdd);
}

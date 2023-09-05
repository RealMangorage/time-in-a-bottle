package com.magorage.tiab.api.events;

import com.magorage.tiab.api.HandledState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ITimeBottleUseEvent {
    boolean accept(ItemStack bottle, Player player, Level level, BlockPos pos, HandledState handledState);
}

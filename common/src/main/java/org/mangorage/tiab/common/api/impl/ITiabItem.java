package org.mangorage.tiab.common.api.impl;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ITiabItem {
    IStoredTimeComponent getStoredComponent(ItemStack stack);
    Item asItem();

    /**
     Find the bottle for the player and tick it once.
     */
    void tickPlayer(Player player);

    /**
     Find the bottle for the player and add ticks to it.
      */
    void tickPlayer(Player player, int ticks);

    /**
     Tick the bottle once.
      */
    void tickBottle(ItemStack stack);

    /**
     Add ticks to the bottle.
      */
    void tickBottle(ItemStack stack, int ticks);
}

package org.mangorage.tiab.common.api.impl;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.api.annotations.API;

@API(since = "7.0.0")
public interface ITiabItem {
    @API(since = "7.0.0")
    IStoredTimeComponent getStoredComponent(ItemStack stack);

    @API(since = "7.0.0")
    Item asItem();

    /**
     Find the bottle for the player and tick it once.
     */
    @API(since = "7.0.0")
    void tickPlayer(Player player);

    /**
     Find the bottle for the player and add ticks to it.
      */
    @API(since = "7.0.0")
    void tickPlayer(Player player, int ticks);

    /**
     Tick the bottle once.
      */
    @API(since = "7.0.0")
    void tickBottle(ItemStack stack);

    /**
     Add ticks to the bottle.
      */
    @API(since = "7.0.0")
    void tickBottle(ItemStack stack, int ticks);

    /**
     * @return true if Creative variant.
     */
    @API(since = "7.0.0")
    boolean isCreative();
}

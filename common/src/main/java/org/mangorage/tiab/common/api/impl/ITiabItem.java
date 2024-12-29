package org.mangorage.tiab.common.api.impl;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ITiabItem {
    IStoredTimeComponent getStoredComponent(ItemStack stack);
    Item asItem();

    void tickPlayer(Player player);
    void tickBottle(ItemStack stack);
}

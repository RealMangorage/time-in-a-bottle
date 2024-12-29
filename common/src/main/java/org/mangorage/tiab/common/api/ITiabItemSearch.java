package org.mangorage.tiab.common.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ITiabItemSearch {
    ItemStack findItem(Player player);
}

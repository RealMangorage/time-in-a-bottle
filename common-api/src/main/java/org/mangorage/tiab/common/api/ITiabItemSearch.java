package org.mangorage.tiab.common.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mangorage.tiab.common.api.annotations.API;

@API(since = "7.0.0")
public interface ITiabItemSearch {
    /**
     * @return Time In A Bottle {@link ItemStack} or {@link ItemStack#EMPTY}
     */
    @API(since = "7.0.0")
    @NotNull ItemStack findItem(Player player);
}

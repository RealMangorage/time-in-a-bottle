package org.mangorage.tiab.common.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ITiabItemSearch {
    /**
     * @return Time In A Bottle {@link ItemStack} or {@link ItemStack#EMPTY}
     */
    @NotNull ItemStack findItem(Player player);
}

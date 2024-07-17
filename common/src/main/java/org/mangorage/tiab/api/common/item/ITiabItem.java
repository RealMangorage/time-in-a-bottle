package org.mangorage.tiab.api.common.item;

import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.api.common.components.IStoredTimeComponent;

public interface ITiabItem {
    IStoredTimeComponent getStoredTime(ItemStack itemStack);
    void setStoredTime(ItemStack itemStack, IStoredTimeComponent iStoredTimeComponent);
}

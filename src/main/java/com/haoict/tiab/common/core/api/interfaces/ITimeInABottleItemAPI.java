package com.haoict.tiab.common.core.api.interfaces;

import net.minecraft.world.item.ItemStack;

public interface ITimeInABottleItemAPI {
    int getStoredEnergy(ItemStack stack);
    void setStoredEnergy(ItemStack stack, int energy);
    void applyDamage(ItemStack stack, int damage);
    int getTotalAccumulatedTime(ItemStack stack);
    void setTotalAccumulatedTime(ItemStack stack, int value);
}

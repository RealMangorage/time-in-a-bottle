package com.haoict.tiab.common.core.api.interfaces;

import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ITimeInABottleItemAPI {
    int getStoredEnergy(ItemStack stack);
    void setStoredEnergy(ItemStack stack, int energy);
    void applyDamage(ItemStack stack, int damage);
    int getTotalAccumulatedTime(ItemStack stack);
    void setTotalAccumulatedTime(ItemStack stack, int value);
    int getEnergyCost(int timeRate);
    void playSound(Level level, BlockPos pos, int nextRate);
    InteractionResult accelerateBlock(ITimeInABottleAPI API, ItemStack stack, Player player, Level level, BlockPos pos);
}

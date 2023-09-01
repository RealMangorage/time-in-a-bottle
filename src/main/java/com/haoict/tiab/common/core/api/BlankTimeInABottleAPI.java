package com.haoict.tiab.common.core.api;

import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public final class BlankTimeInABottleAPI implements ITimeInABottleAPI {
    @Override
    public RegistryObject<Item> getRegistryObject() {
        return null;
    }

    @Override
    public int getTotalTime(ItemStack bottle) {
        return 0;
    }

    @Override
    public int getStoredTime(ItemStack bottle) {
        return 0;
    }

    @Override
    public String getModID() {
        return "error no mod id";
    }

    @Override
    public boolean canSetTime() {
        return false;
    }

    @Override
    public void setStoredTime(ItemStack bottle, int time) {

    }

    @Override
    public void setTotalTime(ItemStack bottle, int time) {

    }

    @Override
    public int processCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, Component messageValue, boolean isAdd) {
        return 0;
    }

}

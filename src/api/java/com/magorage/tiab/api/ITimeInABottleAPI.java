package com.magorage.tiab.api;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Function;

public interface ITimeInABottleAPI {
    class IMC {
        public static final String GET_API = "get_api";
        public static final String MOD_ID = "tiab";
    }

    RegistryObject<Item> getRegistryObject();
    int getTotalTime(ItemStack bottle);
    int getStoredTime(ItemStack bottle);
    String getModID();
    boolean canSetTime(); // can you set Time?
    void setStoredTime(ItemStack bottle, int time);
    void setTotalTime(ItemStack bottle, int time);
    int processCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, Component messageValue, boolean isAdd);
}

package com.magorage.tiab.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public interface ITimeInABottleAPI {
    class IMC {
        public static final String GET_API = "get_api";
    }

    RegistryObject<Item> getRegistryObject();
    int getTotalTime(ItemStack bottle);
    int getStoredTime(ItemStack bottle);
    String getModID();
    boolean canSetTime(); // can you set Time?
    void setStoredTime(ItemStack bottle, int time);
    void setTotalTime(ItemStack bottle, int time);
}

package com.magorage.tiab.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

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
}

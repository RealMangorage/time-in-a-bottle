package com.haoict.tiab.common.items;

import com.haoict.tiab.Tiab;
import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.core.ItemRegistry;
import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

/**
  * Used to get The Registry Object for TIAB without needing to try to get it
  * This is for mods wanting it without worrying about anything changing
  *  And for anything useful mods may need.
 */
public final class TimeInABottleAPI implements ITimeInABottleAPI {
    private final String API_MOD_ID;

    /**
     * @param API_MOD_ID -> The mod which requested to get an API
     */
    public TimeInABottleAPI(String API_MOD_ID) {
        this.API_MOD_ID = API_MOD_ID;
    }


    @Override
    public RegistryObject<Item> getRegistryObject() {
        return ItemRegistry.timeInABottleItem;
    }

    public String getModID() {
        return Constants.MOD_ID;
    }


    @Override
    public int getTotalTime(ItemStack bottle) {
        if (bottle.getItem() instanceof TimeInABottleItem tiab)
            return tiab.getTotalAccumulatedTime(bottle);
        return 0;
    }

    @Override
    public int getStoredTime(ItemStack bottle) {
        if (bottle.getItem() instanceof TimeInABottleItem tiab)
            return tiab.getStoredEnergy(bottle);
        return 0;
    }

    @Override
    public boolean canSetTime() {
        return false;
    }

    @Override
    public void setStoredTime(ItemStack bottle, int time) {
        if (bottle.getItem() instanceof TimeInABottleItem tiab)
            tiab.setStoredEnergy(bottle, time);
    }

    @Override
    public void setTotalTime(ItemStack bottle, int time) {
        if (bottle.getItem() instanceof TimeInABottleItem tiab)
            tiab.setTotalAccumulatedTime(bottle, time);
    }

}

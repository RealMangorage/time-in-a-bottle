package com.haoict.tiab.common.core;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.items.TimeInABottleItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<Item> timeInABottleItem = ITEMS.register("time_in_a_bottle", TimeInABottleItem::new);
}

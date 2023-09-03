package com.magorage.tiab.api.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

// Cant cancel this event
public class TimeBottleUseEvent extends Event implements IModBusEvent {
    private final ItemStack BOTTLE_ITEMSTACK;
    private final BlockPos USED_ON_BLOCK;
    private final Level LEVEL;

    public TimeBottleUseEvent(ItemStack bottle, Level level, BlockPos pos) {
        this.BOTTLE_ITEMSTACK = bottle;
        this.LEVEL = level;
        this.USED_ON_BLOCK = pos;
    }

    public ItemStack getBottleItemStack() {
        return BOTTLE_ITEMSTACK;
    }

    public Level getLevel() {
        return LEVEL;
    }

    public BlockPos getClickedOn() {
        return USED_ON_BLOCK;
    }
}

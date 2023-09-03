package com.magorage.tiab.api.events;

import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;
import java.util.function.Function;

// Cant cancel this event
// Cannot extend this event.
public final class TimeCommandEvent extends Event implements IModBusEvent {
    protected non-sealed static class APIProvider extends CommandEventFactory {
        @Override
        public TimeCommandEvent createEvent(ItemStack stack, ServerPlayer player, int time, boolean isAdd) {
            return new TimeCommandEvent(stack, player, time, isAdd);
        }
    }
    private static boolean registered = false;
    public static CommandEventFactory registerAPIProvider() {
        if (registered) return null;
        registered = true;
        return new APIProvider();
    }

    private final ItemStack stack;
    private final ServerPlayer player;
    private final int time;
    private final boolean isAdd;
    private boolean handled = false;

    private TimeCommandEvent(ItemStack itemStack, ServerPlayer player, int time, boolean isAdd) {
        this.stack = itemStack;
        this.player = player;
        this.time = time;
        this.isAdd = isAdd;
    }

    public ItemStack getStack() {
        return stack;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public int getTime() {
        return time;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setHandled() {
        this.handled = true;
    }

    public boolean isHandled() {
        return handled;
    }
}

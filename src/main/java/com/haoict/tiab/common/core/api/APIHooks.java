package com.haoict.tiab.common.core.api;

import com.magorage.tiab.api.HandledState;
import com.magorage.tiab.api.TiabAPIHooks;
import com.magorage.tiab.api.events.ITimeBottleTickEvent;
import com.magorage.tiab.api.events.ITimeBottleUseEvent;
import com.magorage.tiab.api.events.ITimeCommandEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class APIHooks {
    private static final APIHooks API = new APIHooks();

    protected static APIHooks getHooks() {
        return API;
    }

    public static void freezeAll() {
        API.freeze();
    }


    private final ArrayList<ITimeCommandEvent> COMMAND_HOOKS = new ArrayList<>();
    private final ArrayList<ITimeBottleUseEvent> USE_HOOKS = new ArrayList<>();
    private final ArrayList<ITimeBottleTickEvent> TICK_HOOKS = new ArrayList<>();
    private boolean frozen = false;

    protected void addEventCallbacks(TiabAPIHooks hooks) {
        if (hooks.getCommandEvent() != null)
            COMMAND_HOOKS.add(hooks.getCommandEvent());
        if (hooks.getUseEvent() != null)
            USE_HOOKS.add(hooks.getUseEvent());
        if (hooks.getTickEvent() != null)
            TICK_HOOKS.add(hooks.getTickEvent());
    }

    protected boolean fireCommandEvent(ServerPlayer player, int time, boolean isAdd) {
        var handledState = new HandledState();
        var success = false;
        for (ITimeCommandEvent hook : COMMAND_HOOKS)
            if (hook.accept(player, time, isAdd, handledState))
                success = true;

        return success;
    }

    protected boolean fireUseEvent(ItemStack bottle, Player player, Level level, BlockPos pos) {
        var handledState = new HandledState();
        var success = false;
        for (ITimeBottleUseEvent hook : USE_HOOKS)
            if (hook.accept(bottle, player, level, pos, handledState))
                success = true;

        return success;
    }

    protected boolean fireTickEvent(ServerPlayer player, ItemStack stack) {
        var handledState = new HandledState();
        var success = false;
        for (ITimeBottleTickEvent hook : TICK_HOOKS)
            if (hook.accept(player, stack, handledState))
                success = true;

        return success;
    }
    private void freeze() {
        this.frozen = true;
    }
}

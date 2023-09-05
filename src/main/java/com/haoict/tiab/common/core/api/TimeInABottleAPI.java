package com.haoict.tiab.common.core.api;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.config.TiabConfig;
import com.haoict.tiab.common.core.ItemRegistry;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleCommandAPI;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleItemAPI;
import com.haoict.tiab.common.items.TimeInABottleItem;
import com.haoict.tiab.common.utils.Utils;
import com.magorage.tiab.api.ITimeInABottleAPI;
import com.magorage.tiab.api.TiabProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
    Distributed to any mods wanting access to the API
    APIRegistry is persistant/never changing on all
    Instances of this class. Should allow for secure
    enough api. Should stop mods from screwing around
    recklessly.
 */
public final class TimeInABottleAPI implements ITimeInABottleAPI {
    public static class IMCPublic {
        public static void load(IEventBus bus) {
            bus.addListener(IMC::imcProcess);
        }
    }
    private static class IMC {
        private static void imcProcess(InterModProcessEvent event) {
            List<String> PROCESSED_MODS = new ArrayList<>();
            event.getIMCStream(ITimeInABottleAPI.IMC.GET_API::equals).forEach(e -> {
                if (e.messageSupplier().get() instanceof TiabProvider provider) {
                    if (PROCESSED_MODS.contains(provider.getModID()))
                        throw new IllegalStateException("Some mod tried to get API for TIAB when it already has been given one! Mods can only get one API! %s mod tried to do this!".formatted(e.senderModId()));

                    var api = new TimeInABottleAPI(provider.getModID());
                    provider.setAPI(api);
                    APIHooks.getHooks().addEventCallbacks(provider.getHooks());
                    provider.setFinalized();
                    PROCESSED_MODS.add(provider.getModID());
                }
            });
        }
    }

    private final String API_MOD_ID;
    private final APIRegistry REGISTRY = APIRegistry.getAPI();

    /**
     * @param API_MOD_ID -> The mod which requested to get an API
     */
    private TimeInABottleAPI(String API_MOD_ID) {
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
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                return api.getTotalAccumulatedTime(bottle);
        }
        return 0;
    }

    @Override
    public int getStoredTime(ItemStack bottle) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                return api.getStoredEnergy(bottle);
        }
        return 0;
    }

    @Override
    public void setStoredTime(ItemStack bottle, int time) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null && canUse()) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                api.setStoredEnergy(bottle, time);
        }
    }

    @Override
    public void setTotalTime(ItemStack bottle, int time) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null && canUse()) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                api.setTotalAccumulatedTime(bottle, time);
        }
    }

    @Override
    public int processCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, String messageValue, boolean isAdd) {
        var api = REGISTRY.getRegistered(ITimeInABottleCommandAPI.class);
        if (api != null && canUse()) {
            return api.processCommand(itemStackFunction, player, messageValue, isAdd);
        }
        return 0;
    }

    @Override
    public Component getTotalTimeTranslated(ItemStack stack) {
        return Utils.getTotalTimeTranslated(stack);
    }

    @Override
    public Component getStoredTimeTranslated(ItemStack stack) {
        return Utils.getStoredTimeTranslated(stack);
    }

    @Override
    public void playSound(Level level, BlockPos pos, int nextRate) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null && canUse()) {
            api.playSound(level, pos, nextRate);
        }
    }

    @Override
    public void applyDamage(ItemStack stack, int damage) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null && canUse()) {
            api.applyDamage(stack, damage);
        }
    }

    @Override
    public int getEnergyCost(int timeRate) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            return api.getEnergyCost(timeRate);
        }
        return -1;
    }

    // All setters should call this first
    // Getters are fine as they provide info
    // Check if Mod has api Levels revoked!
    @Override // Do we have access to setters? for API_MOD_ID?
    public boolean canUse() {
        if (TiabConfig.COMMON.MODS_API.get().contains(API_MOD_ID))
            return false;
        return true; // API level was not revoked
    }

    @Override
    public boolean callCommandEvent(ServerPlayer player, int messageValue, boolean isAdd) {
        if (API_MOD_ID.equals("tiab"))
            return APIHooks.getHooks().fireCommandEvent(player, messageValue, isAdd);
        return false;
    }

    @Override
    public boolean callTickEvent(ServerPlayer player, ItemStack stack) {
        if (API_MOD_ID.equals("tiab"))
            return APIHooks.getHooks().fireTickEvent(player, stack);
        return false;
    }

    @Override
    public boolean callUseEvent(ItemStack bottle, Player player, Level level, BlockPos pos) {
        if (API_MOD_ID.equals("tiab"))
            return APIHooks.getHooks().fireUseEvent(bottle, player, level, pos);
        return false;
    }

    @Override
    public InteractionResult accelerateBlock(ITimeInABottleAPI API, ItemStack stack, Player player, Level level, BlockPos pos) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            return api.accelerateBlock(API, stack, player, level, pos);
        }
        return InteractionResult.FAIL;
    }
}

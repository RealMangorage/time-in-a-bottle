package com.haoict.tiab.common.core.api;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleItemAPI;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.HashMap;

// For internal use only! Mods can't access this
public final class ApiRegistry {
    private static final ApiRegistry API = new ApiRegistry();

    protected static ApiRegistry getApi() {
        return API;
    }

    public static void freeze() {
        API.freezeApi();
    }

    public static <T> void registerAccess(Class<T> tClass, T object) {
        if (ModLoadingContext.get().getActiveContainer().getModId().equals(Constants.MOD_ID))
            API.register(tClass, object);
        else
            throw new IllegalArgumentException("Mod %s tried to register to Time in a bottle's API Registry. Only used internally".formatted(ModLoadingContext.get().getActiveContainer().getModId()));
    }

    private final HashMap<Class<?>, Object> REGISTERED = new HashMap<>();
    private boolean frozen = false;

    @SuppressWarnings("unchecked")
    <T> T getRegistered(Class<T> tClass) {
        if (REGISTERED.containsKey(tClass))
            return (T) REGISTERED.get(tClass);
        return null;
    }

    void freezeApi() {
        this.frozen = true;
    }

    <T> void register(Class<T> tClass, T value) {
        if (frozen) return;
        REGISTERED.computeIfAbsent(tClass, (i) -> value);
    }
}

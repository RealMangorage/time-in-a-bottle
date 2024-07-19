package com.haoict.tiab.common.core.api;

import com.haoict.tiab.common.config.Constants;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.HashMap;

// For internal use only! Mods can't access this
public final class APIRegistry {
    private static final APIRegistry API = new APIRegistry();

    static APIRegistry getAPI() {
        return API;
    }

    public static void freezeAll() {
        API.freeze();
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

    void freeze() {
        this.frozen = true;
    }

    <T> void register(Class<T> tClass, T value) {
        if (frozen) return;
        REGISTERED.computeIfAbsent(tClass, (i) -> value);
    }
}

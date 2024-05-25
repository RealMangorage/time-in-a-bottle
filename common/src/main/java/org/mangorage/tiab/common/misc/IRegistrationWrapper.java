package org.mangorage.tiab.common.misc;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface IRegistrationWrapper {
    <T> void register(ResourceKey<? extends Registry<T>> resourceKey, ResourceLocation resourceLocation, T value);

    default <T> void register(Registry<T> registry, ResourceLocation resourceLocation, T value) {
        register(registry.key(), resourceLocation, value);
    }
}

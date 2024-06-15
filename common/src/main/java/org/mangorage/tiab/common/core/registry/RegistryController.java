package org.mangorage.tiab.common.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("all")
public final class RegistryController {
    public static RegistryController create(String modID) {
        return new RegistryController(modID);
    }

    private final Map<ResourceKey<Registry<?>>, List<RegistryHolder<?>>> REGISTRIES = new HashMap<>();
    private final String modID;
    private boolean frozen = false;

    private RegistryController(String modID) {
        this.modID = modID;
    }


    public <T extends E, E> RegistryHolder<T> register(String id, ResourceKey<Registry<E>> resourceKey, Registry<E> defaultRegistry, Supplier<T> supplier) {
        if (frozen)
            throw new IllegalStateException("Cannot register, RegistryController is frozen.");
        RegistryHolder<T> holder = new RegistryHolder<>(new ResourceLocation(modID, id), defaultRegistry, supplier);
        REGISTRIES.computeIfAbsent((ResourceKey<Registry<?>>) (Object) resourceKey, o -> new ArrayList<>()).add(holder);
        return holder;
    }

    public void register(ResourceKey<? extends Registry<?>> resourceKey, RegistryWrapper registry) {
        frozen = true;
        var list = REGISTRIES.get(resourceKey);
        if (list == null) return;
        list.forEach(holder -> {
            holder.handleRegister(registry);
        });
        REGISTRIES.remove(resourceKey);
    }

    public void registerUsingDefault() {
        frozen = true;
        REGISTRIES.forEach((k, v) -> {
            v.forEach(holder -> {
                holder.handleDefaultRegister();
            });
        });
        REGISTRIES.clear();
    }
}

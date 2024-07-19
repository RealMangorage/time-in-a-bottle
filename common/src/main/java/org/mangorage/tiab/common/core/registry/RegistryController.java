package org.mangorage.tiab.common.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("all")
public final class RegistryController {
    private static final Set<ResourceKey<? extends Registry<?>>> RESOURCE_KEYS = new LinkedHashSet<>();

    static {
        RESOURCE_KEYS.add(Registries.DATA_COMPONENT_TYPE);
        RESOURCE_KEYS.add(Registries.ITEM);
        RESOURCE_KEYS.add(Registries.ENTITY_TYPE);
        RESOURCE_KEYS.add(Registries.CREATIVE_MODE_TAB);
    }


    public static RegistryController create(String modID) {
        return new RegistryController(modID);
    }

    private final Map<ResourceKey<Registry<?>>, List<RegistryHolder<?>>> REGISTRIES = new LinkedHashMap<>();
    private final String modID;
    private boolean frozen = false;

    private RegistryController(String modID) {
        this.modID = modID;
    }


    public <T extends E, E> RegistryHolder<T> register(String id, ResourceKey<Registry<E>> resourceKey, Registry<E> defaultRegistry, Supplier<T> supplier) {
        if (frozen)
            throw new IllegalStateException("Cannot register, RegistryController is frozen.");
        RegistryHolder<T> holder = new RegistryHolder<>(ResourceLocation.fromNamespaceAndPath(modID, id), defaultRegistry, supplier);
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
        RESOURCE_KEYS.forEach(key -> {
            register(key, new RegistryWrapper() {
                @Override
                public <T> Holder<T> registerForHolder(ResourceLocation id, T object) {
                    return Registry.registerForHolder((Registry<T>) BuiltInRegistries.REGISTRY.get(key.location()), id, object);
                }
            });
        });
    }
}

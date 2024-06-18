package org.mangorage.tiab.common.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegistryHolder<T> implements Supplier<T> {
    private final ResourceLocation id;
    private final Registry<?> defaultRegistry;

    private Supplier<T> supplier;
    private Holder<T> minecraftHolder;

    RegistryHolder(ResourceLocation id, Registry<?> defaultRegistry, Supplier<T> supplier) {
        this.id = id;
        this.defaultRegistry = defaultRegistry;
        this.supplier = supplier;
    }

    T getUnSafe() {
        var instance = supplier.get();
        supplier = null;
        return instance;
    }

    @Override
    public T get() {
        if (minecraftHolder == null)
            throw new IllegalStateException("RegistryHolder not yet populated...");
        return minecraftHolder.value();
    }

    public Supplier<T> asSupplier() {
        return this;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Holder<T> getHolder() {
        return minecraftHolder;
    }

    void handleRegister(RegistryWrapper registry) {
        this.minecraftHolder = registry.registerForHolder(id, getUnSafe());
    }

    @SuppressWarnings("unchecked")
    void handleDefaultRegister() {
        this.minecraftHolder = Registry.registerForHolder((Registry<T>) defaultRegistry, id, getUnSafe());
    }
}

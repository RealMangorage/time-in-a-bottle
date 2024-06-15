package org.mangorage.tiab.common.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface RegistryWrapper {
    <T> Holder<T> registerForHolder(ResourceLocation id, T object);
}

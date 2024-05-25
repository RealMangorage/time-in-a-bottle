package org.mangorage.tiab.common.core;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.common.misc.IRegistrationWrapper;
import org.mangorage.tiab.common.misc.Lazy;

import java.util.function.Supplier;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public class CommonRegistration {
    public static final Supplier<TiabItem> TIAB_ITEM = Lazy.of(() -> new TiabItem(new Item.Properties()));
    public static final Supplier<Block> CUSTOM_BLOCK = Lazy.of(() -> new Block(BlockBehaviour.Properties.of()));
    public static final Supplier<DataComponentType<Integer>> STORED_TIME_COMPONENT = Lazy.of(() -> {
        return new DataComponentType.Builder<Integer>()
                .persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.INT)
                .build();
    });

    // For Forge
    public static void init(IRegistrationWrapper wrapper) {
        wrapper.register(BuiltInRegistries.ITEM, new ResourceLocation(MODID, "tiab"), TIAB_ITEM.get());
        wrapper.register(BuiltInRegistries.BLOCK, new ResourceLocation(MODID, "tiab_block"), CUSTOM_BLOCK.get());
        wrapper.register(BuiltInRegistries.DATA_COMPONENT_TYPE, new ResourceLocation("tiab", "stored_time"), STORED_TIME_COMPONENT.get());
    }

    public static void init() {
        init(new IRegistrationWrapper() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> resourceKey, ResourceLocation resourceLocation, T value) {
                throw new IllegalStateException("Cannot register, use the one that takes the Registry Directly!");
            }

            @Override
            public <T> void register(Registry<T> registry, ResourceLocation resourceLocation, T value) {
                Registry.register(registry, resourceLocation, value);
            }
        });
    }
}

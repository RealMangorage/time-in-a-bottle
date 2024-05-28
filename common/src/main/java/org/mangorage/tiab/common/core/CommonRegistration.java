package org.mangorage.tiab.common.core;

import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.common.misc.IRegistrationWrapper;
import org.mangorage.tiab.common.misc.Lazy;

import java.util.function.Supplier;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public class CommonRegistration {
    public static final Supplier<DataComponentType<Integer>> STORED_TIME_COMPONENT = Lazy.of(() -> {
        return new DataComponentType.Builder<Integer>()
                .persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.INT)
                .build();
    });
    public static final Supplier<TiabItem> TIAB_ITEM = Lazy.of(
            () -> new TiabItem(
                    new Item.Properties()
                            .component(STORED_TIME_COMPONENT.get(), 0)
                            .component(DataComponents.MAX_STACK_SIZE, 1)
            )
    );
    public static final Supplier<EntityType<TimeAcceleratorEntity>> ACCELERATOR_ENTITY = Lazy.of(() -> {
       return EntityType.Builder.<TimeAcceleratorEntity>of(TimeAcceleratorEntity::new, MobCategory.MISC).build("accelerator");
    });

    public static ResourceLocation create(String id) {
        return new ResourceLocation(MODID, id);
    }

    // For Forge
    public static void init(IRegistrationWrapper wrapper) {
        wrapper.register(BuiltInRegistries.ITEM,create("time_in_a_bottle"), TIAB_ITEM.get());
        wrapper.register(BuiltInRegistries.DATA_COMPONENT_TYPE, create("stored_time"), STORED_TIME_COMPONENT.get());
        wrapper.register(BuiltInRegistries.ENTITY_TYPE, create("accelerator"), ACCELERATOR_ENTITY.get());
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

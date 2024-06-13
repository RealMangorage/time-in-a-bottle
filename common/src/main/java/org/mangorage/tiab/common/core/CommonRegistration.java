package org.mangorage.tiab.common.core;

import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.mangorage.tiab.common.commands.TiabCommands;
import org.mangorage.tiab.common.config.ConfigHolder;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.common.misc.IRegistrationWrapper;
import org.mangorage.tiab.common.misc.LazySupplier;

import java.util.function.Supplier;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public final class CommonRegistration {
    public static final Supplier<DataComponentType<StoredTimeComponent>> STORED_TIME_COMPONENT = LazySupplier.of(() -> {
        return new DataComponentType.Builder<StoredTimeComponent>()
                .persistent(StoredTimeComponent.DIRECT_CODEC)
                .networkSynchronized(StoredTimeComponent.DIRECT_STREAM_CODEC)
                .build();
    });
    public static final Supplier<TiabItem> TIAB_ITEM = LazySupplier.of(
            () -> new TiabItem(
                    new Item.Properties()
                            .component(STORED_TIME_COMPONENT.get(), new StoredTimeComponent(0, 0))
                            .component(DataComponents.MAX_STACK_SIZE, 1)
            )
    );
    public static final Supplier<EntityType<TimeAcceleratorEntity>> ACCELERATOR_ENTITY = LazySupplier.of(() -> {
       return EntityType.Builder.<TimeAcceleratorEntity>of(TimeAcceleratorEntity::new, MobCategory.MISC).build("accelerator");
    });
    public static final Supplier<CreativeModeTab> TIAB_CREATIVE_TAB = LazySupplier.of(() -> {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .icon(() -> TIAB_ITEM.get().getDefaultInstance())
                .title(Component.literal("Time in a bottle"))
                .displayItems((parameters, output) -> {
                    output.accept(TIAB_ITEM.get());
                })
                .build();
    });
    public static final ConfigHolder SERVER_CONFIG = ConfigHolder.create();

    public static final TagKey<Block> TIAB_UN_ACCELERATABLE = TagKey.create(Registries.BLOCK, create("un_acceleratable"));

    private static ResourceLocation create(String id) {
        return new ResourceLocation(MODID, id);
    }

    public static void init(IRegistrationWrapper wrapper) {
        wrapper.register(BuiltInRegistries.ITEM, create("time_in_a_bottle"), TIAB_ITEM.get());
        wrapper.register(BuiltInRegistries.DATA_COMPONENT_TYPE, create("stored_time"), STORED_TIME_COMPONENT.get());
        wrapper.register(BuiltInRegistries.ENTITY_TYPE, create("accelerator"), ACCELERATOR_ENTITY.get());
        wrapper.register(BuiltInRegistries.CREATIVE_MODE_TAB, create("tiab"), TIAB_CREATIVE_TAB.get());
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

    public static void initServer(MinecraftServer server) {
        server.getCommands().getDispatcher().register(Commands.literal(MODID).then(TiabCommands.addTimeCommand).then(TiabCommands.removeTimeCommand));
    }
}

package org.mangorage.tiab.common.core;

import static org.mangorage.tiab.common.CommonConstants.MODID;

import org.mangorage.tiab.common.commands.TiabCommands;
import org.mangorage.tiab.common.config.ConfigHolder;
import org.mangorage.tiab.common.core.registry.RegistryController;
import org.mangorage.tiab.common.core.registry.RegistryHolder;
import org.mangorage.tiab.common.core.registry.RegistryWrapper;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;


// Where we register everything....
public final class CommonRegistration {
    private static final RegistryController registry = RegistryController.create(MODID);

//    public static final RegistryHolder<DataComponentType<StoredTimeComponent>> STORED_TIME_COMPONENT = registry.register(
//            "stored_time",
//            Registries.DATA_COMPONENT_TYPE,
//            BuiltInRegistries.DATA_COMPONENT_TYPE,
//            () -> new DataComponentType.Builder<StoredTimeComponent>()
//                    .persistent(StoredTimeComponent.DIRECT_CODEC)
//                    .networkSynchronized(StoredTimeComponent.DIRECT_STREAM_CODEC)
//                    .build());

    public static final RegistryHolder<TiabItem> TIAB_ITEM = registry.register(
            "time_in_a_bottle",
            Registries.ITEM,
            BuiltInRegistries.ITEM,
            () -> new TiabItem(
                    new Item.Properties()
     //                       .component(STORED_TIME_COMPONENT.get(), new StoredTimeComponent(0, 0))
     //                       .component(DataComponents.MAX_STACK_SIZE, 1)
            ));

    public static final RegistryHolder<EntityType<TimeAcceleratorEntity>> ACCELERATOR_ENTITY = registry.register(
            "accelerator",
            Registries.ENTITY_TYPE,
            BuiltInRegistries.ENTITY_TYPE,
            () -> EntityType.Builder.<TimeAcceleratorEntity>of((entityType, level) -> new TimeAcceleratorEntity(level), MobCategory.MISC).build("accelerator"));

    public static final RegistryHolder<CreativeModeTab> TIAB_CREATIVE_TAB = registry.register(
            "tiab",
            Registries.CREATIVE_MODE_TAB,
            BuiltInRegistries.CREATIVE_MODE_TAB,
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> TIAB_ITEM.get().getDefaultInstance())
                    .title(Component.literal("Time in a bottle"))
                    .displayItems((parameters, output) -> {
                        output.accept(TIAB_ITEM.get());
                    })
                    .build());

    public static final ConfigHolder SERVER_CONFIG = ConfigHolder.create();

    public static final TagKey<Block> TIAB_UN_ACCELERATABLE = TagKey.create(Registries.BLOCK, create("un_acceleratable"));

    private static ResourceLocation create(String id) {
        return new ResourceLocation(MODID, id);
    }

    public static <T> void register(ResourceKey<? extends Registry<T>> resourceKey, RegistryWrapper registryWrapper) {
        registry.register(resourceKey, registryWrapper);
    }

    public static void register() {
        registry.registerUsingDefault();
    }

    public static void initServer(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(MODID).then(TiabCommands.addTimeCommand).then(TiabCommands.removeTimeCommand));
    }
}

package org.mangorage.tiab.fabric.core;

import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public final class Registration {

    private static <B, T extends B> T register(Registry<B> registry, String id, T value) {
        return Registry.register(registry, ResourceLocation.fromNamespaceAndPath(MODID, id), value);
    }

    public static final DataComponentType<StoredTimeComponent> STORED_TIME_COMPONENT = register(BuiltInRegistries.DATA_COMPONENT_TYPE, "stored_time", new DataComponentType.Builder<StoredTimeComponent>()
            .persistent(StoredTimeComponent.DIRECT_CODEC)
            .networkSynchronized(StoredTimeComponent.DIRECT_STREAM_CODEC)
            .build());

    public static final TiabItem TIAB_ITEM = register(BuiltInRegistries.ITEM, "time_in_a_bottle", new TiabItem(
            new Item.Properties()
                    .component(STORED_TIME_COMPONENT, new StoredTimeComponent(0, 0))
                    .component(DataComponents.MAX_STACK_SIZE, 1)
    ));

    public static final EntityType<TimeAcceleratorEntity> ACCELERATOR_ENTITY = register(BuiltInRegistries.ENTITY_TYPE, "accelerator", EntityType.Builder.<TimeAcceleratorEntity>of(
            (entityType, level) -> new TimeAcceleratorEntity(level),
            MobCategory.MISC
    ).build("accelerator"));

    public static final CreativeModeTab TIAB_CREATIVE_TAB = register(BuiltInRegistries.CREATIVE_MODE_TAB, "tiab", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(TIAB_ITEM::getDefaultInstance)
            .title(Component.literal("Time in a bottle"))
            .displayItems((parameters, output) -> {
                output.accept(TIAB_ITEM);
            })
            .build());

    public static void register() {
    }

    public interface FabricRegistration extends ITiabRegistration {
        @Override
        default DataComponentType<StoredTimeComponent> getStoredTime() {
            return STORED_TIME_COMPONENT;
        }

        @Override
        default EntityType<TimeAcceleratorEntity> getAcceleratorEntity() {
            return ACCELERATOR_ENTITY;
        }

        @Override
        default TiabItem getTiabItem() {
            return TIAB_ITEM;
        }

        @Override
        default CreativeModeTab getCreativeTab() {
            return TIAB_CREATIVE_TAB;
        }
    }
}

package org.mangorage.tiab.neoforge.core;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public final class Registration {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    private static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(MODID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<StoredTimeComponent>> STORED_TIME_COMPONENT = DATA_COMPONENT_TYPES.register("stored_time",
            () -> new DataComponentType.Builder<StoredTimeComponent>()
                    .persistent(StoredTimeComponent.DIRECT_CODEC)
                    .networkSynchronized(StoredTimeComponent.DIRECT_STREAM_CODEC)
                    .build());

    public static final DeferredItem<TiabItem> TIAB_ITEM = ITEMS.register("time_in_a_bottle",
            () -> new TiabItem(
                    new Item.Properties()
                            .component(STORED_TIME_COMPONENT.get(), new StoredTimeComponent(0, 0))
                            .component(DataComponents.MAX_STACK_SIZE, 1)
            ));

    public static final DeferredHolder<EntityType<?>, EntityType<TimeAcceleratorEntity>> ACCELERATOR_ENTITY = ENTITY_TYPES.register("accelerator",
            () -> EntityType.Builder.<TimeAcceleratorEntity>of(
                    (entityType, level) -> new TimeAcceleratorEntity(level),
                    MobCategory.MISC
            ).build("accelerator"));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TIAB_CREATIVE_TAB = TABS.register("tiab", () -> CreativeModeTab.builder()
            .icon(() -> TIAB_ITEM.get().getDefaultInstance())
            .title(Component.literal("Time in a bottle"))
            .displayItems((parameters, output) -> {
                output.accept(TIAB_ITEM.get());
            })
            .build());

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        ENTITY_TYPES.register(modBus);
        DATA_COMPONENT_TYPES.register(modBus);
        TABS.register(modBus);
    }

    public interface NeoForgeRegistration extends ITiabRegistration {
        @Override
        default CreativeModeTab getCreativeTab() {
            return Registration.TIAB_CREATIVE_TAB.get();
        }

        @Override
        default DataComponentType<StoredTimeComponent> getStoredTime() {
            return Registration.STORED_TIME_COMPONENT.get();
        }

        @Override
        default TiabItem getTiabItem() {
            return Registration.TIAB_ITEM.get();
        }

        @Override
        default EntityType<TimeAcceleratorEntity> getAcceleratorEntity() {
            return Registration.ACCELERATOR_ENTITY.get();
        }
    }
}

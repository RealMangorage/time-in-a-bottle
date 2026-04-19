package org.mangorage.tiab.neoforge.core;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITiabItem;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.common.lang.Styles;
import org.mangorage.tiab.common.lang.Translation;
import org.mangorage.tiab.common.misc.CommonHelper;

import java.util.List;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public final class Registration {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    private static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<IStoredTimeComponent>> STORED_TIME_COMPONENT = DATA_COMPONENT_TYPES.register("stored_time",
            () -> new DataComponentType.Builder<IStoredTimeComponent>()
                    .persistent(StoredTimeComponent.DIRECT_CODEC)
                    .networkSynchronized(StoredTimeComponent.DIRECT_STREAM_CODEC)
                    .build());

    public static final DeferredItem<TiabItem> TIAB_ITEM = ITEMS.register("time_in_a_bottle",
            () -> new TiabItem(false,
                    new Item.Properties()
                            .component(STORED_TIME_COMPONENT.get(), new StoredTimeComponent(0, 0))
                            .component(DataComponents.MAX_STACK_SIZE, 1)
                            .setId(CommonHelper.getResourceKey(Registries.ITEM, "time_in_a_bottle"))
            ));

    public static final DeferredItem<TiabItem> CREATIVE_TIAB_ITEM = ITEMS.register("creative_time_in_a_bottle",
            () -> new TiabItem(true,
                    new Item.Properties()
                            .component(STORED_TIME_COMPONENT.get(), new StoredTimeComponent(Integer.MAX_VALUE, Integer.MAX_VALUE))
                            .component(DataComponents.MAX_STACK_SIZE, 1)
                            .rarity(Rarity.EPIC)
                            .component(
                                    DataComponents.LORE,
                                    new ItemLore(
                                            List.of(
                                                    Component.translatable("item.tiab.time_in_a_bottle.tooltip.creative")
                                                            .withStyle(Styles.AQUA.withBold(true).withItalic(true))
                                            )
                                    )
                            )
                            .setId(CommonHelper.getResourceKey(Registries.ITEM, "creative_time_in_a_bottle"))
            ));

    public static final DeferredHolder<EntityType<?>, EntityType<TimeAcceleratorEntity>> ACCELERATOR_ENTITY = ENTITY_TYPES.register("accelerator",
            () -> EntityType.Builder.<TimeAcceleratorEntity>of(
                    (entityType, level) -> new TimeAcceleratorEntity(level),
                    MobCategory.MISC
            ).sized(1.0f, 1.0f).build(CommonHelper.getResourceKey(Registries.ENTITY_TYPE, "accelerator")));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TIAB_CREATIVE_TAB = TABS.register("tiab", () -> CreativeModeTab.builder()
            .icon(() -> TIAB_ITEM.get().getDefaultInstance())
            .title(Translation.ITEM.componentTranslation())
            .displayItems((parameters, output) -> {
                output.accept(TIAB_ITEM.get());
                output.accept(CREATIVE_TIAB_ITEM.get());
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
        default ITiabItem getTiabItem() {
            return TIAB_ITEM.get();
        }

        @Override
        default ITiabItem getCreativeTiabItem() {
            return CREATIVE_TIAB_ITEM.get();
        }

        @Override
        default CreativeModeTab getCreativeTab() {
            return TIAB_CREATIVE_TAB.get();
        }

        @Override
        default DataComponentType<IStoredTimeComponent> getStoredTime() {
            return STORED_TIME_COMPONENT.get();
        }

        @Override
        default EntityType<? extends Entity> getAcceleratorEntityType() {
            return ACCELERATOR_ENTITY.get();
        }
    }

    Registration() {}
}

package org.mangorage.tiab.fabric.core;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
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

    private static <B, T extends B> T register(Registry<B> registry, String id, T value) {
        return Registry.register(registry, Identifier.fromNamespaceAndPath(MODID, id), value);
    }

    public static final DataComponentType<IStoredTimeComponent> STORED_TIME_COMPONENT = register(BuiltInRegistries.DATA_COMPONENT_TYPE, "stored_time", new DataComponentType.Builder<IStoredTimeComponent>()
            .persistent(StoredTimeComponent.DIRECT_CODEC)
            .networkSynchronized(StoredTimeComponent.DIRECT_STREAM_CODEC)
            .build());

    public static final TiabItem TIAB_ITEM = register(BuiltInRegistries.ITEM, "time_in_a_bottle", new TiabItem(false,
            new Item.Properties()
                    .component(STORED_TIME_COMPONENT, new StoredTimeComponent(0, 0))
                    .component(DataComponents.MAX_STACK_SIZE, 1)
                    .setId(CommonHelper.getResourceKey(Registries.ITEM, "time_in_a_bottle"))
    ));

    public static final TiabItem CREATIVE_TIAB_ITEM = register(BuiltInRegistries.ITEM, "creative_time_in_a_bottle", new TiabItem(true,
            new Item.Properties()
                    .component(STORED_TIME_COMPONENT, new StoredTimeComponent(Integer.MAX_VALUE, Integer.MAX_VALUE))
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


    public static final EntityType<TimeAcceleratorEntity> ACCELERATOR_ENTITY = register(
            BuiltInRegistries.ENTITY_TYPE, "accelerator",
            EntityType.Builder.<TimeAcceleratorEntity>of(
                    (entityType, level) ->
                            new TimeAcceleratorEntity(level),
                            MobCategory.MISC
                    )
                    .sized(1.0f, 1.0f)
                    .build(CommonHelper.getResourceKey(Registries.ENTITY_TYPE, "accelerator"))
    );

    public static final CreativeModeTab TIAB_CREATIVE_TAB = register(BuiltInRegistries.CREATIVE_MODE_TAB, "tiab", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(TIAB_ITEM::getDefaultInstance)
            .title(Translation.ITEM.componentTranslation())
            .displayItems((parameters, output) -> {
                output.accept(TIAB_ITEM);
                output.accept(CREATIVE_TIAB_ITEM);
            })
            .build());

    public static void register() {}

    public interface FabricRegistration extends ITiabRegistration {
        @Override
        default ITiabItem getTiabItem() {
            return TIAB_ITEM;
        }

        @Override
        default ITiabItem getCreativeTiabItem() {
            return CREATIVE_TIAB_ITEM;
        }

        @Override
        default CreativeModeTab getCreativeTab() {
            return TIAB_CREATIVE_TAB;
        }

        @Override
        default DataComponentType<IStoredTimeComponent> getStoredTime() {
            return STORED_TIME_COMPONENT;
        }

        @Override
        default EntityType<? extends Entity> getAcceleratorEntityType() {
            return ACCELERATOR_ENTITY;
        }
    }

    Registration() {}
}

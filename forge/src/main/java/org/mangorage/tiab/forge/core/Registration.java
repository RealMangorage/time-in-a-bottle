package org.mangorage.tiab.forge.core;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITiabItem;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.common.lang.Translation;
import org.mangorage.tiab.common.misc.CommonHelper;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public final class Registration {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<DataComponentType<IStoredTimeComponent>> STORED_TIME_COMPONENT = DATA_COMPONENT_TYPES.register("stored_time",
            () -> new DataComponentType.Builder<IStoredTimeComponent>()
                    .persistent(StoredTimeComponent.DIRECT_CODEC)
                    .networkSynchronized(StoredTimeComponent.DIRECT_STREAM_CODEC)
                    .build()
    );

    public static RegistryObject<DataComponentType<Boolean>> CREATIVE_COMPONENT = DATA_COMPONENT_TYPES.register("creative",
            () -> new DataComponentType.Builder<Boolean>()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );

    public static final RegistryObject<TiabItem> TIAB_ITEM = ITEMS.register("time_in_a_bottle",
            () -> new TiabItem(
                    new Item.Properties()
                            .component(STORED_TIME_COMPONENT.get(), new StoredTimeComponent(0, 0))
                            .component(DataComponents.MAX_STACK_SIZE, 1)
                            .setId(CommonHelper.getResourceKey(Registries.ITEM, "time_in_a_bottle"))
            )
    );

    public static final RegistryObject<EntityType<TimeAcceleratorEntity>> ACCELERATOR_ENTITY = ENTITY_TYPES.register("accelerator",
            () -> EntityType.Builder.<TimeAcceleratorEntity>of(
                    (entityType, level) -> new TimeAcceleratorEntity(level),
                    MobCategory.MISC
            ).sized(1.0f, 1.0f).build(CommonHelper.getResourceKey(Registries.ENTITY_TYPE, "accelerator"))
    );

    public static final RegistryObject<CreativeModeTab> TIAB_CREATIVE_TAB = TABS.register("tiab", () -> CreativeModeTab.builder()
            .icon(() -> TIAB_ITEM.get().getDefaultInstance())
            .title(Translation.ITEM.componentTranslation())
            .displayItems((parameters, output) -> {
                var regular = TIAB_ITEM.get().getDefaultInstance();
                var creative = TIAB_ITEM.get().getDefaultInstance();

                creative.set(CREATIVE_COMPONENT.get(), true);

                output.accept(regular);
                output.accept(creative);
            })
            .build()
    );

    public static void register(BusGroup modGroup) {
        ITEMS.register(modGroup);
        ENTITY_TYPES.register(modGroup);
        DATA_COMPONENT_TYPES.register(modGroup);
        TABS.register(modGroup);
    }

    public interface ForgeRegistration extends ITiabRegistration {
        @Override
        default ITiabItem getTiabItem() {
            return TIAB_ITEM.get();
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
        default DataComponentType<Boolean> getCreativeComponent() {
            return CREATIVE_COMPONENT.get();
        }

        @Override
        default EntityType<? extends Entity> getAcceleratorEntityType() {
            return ACCELERATOR_ENTITY.get();
        }
    }

    Registration() {}
}

package org.mangorage.tiab.common.api;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import org.mangorage.tiab.common.api.annotations.API;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITiabItem;

/**
 * Where you get registred objects from!
 */
@API(since = "7.0.0")
public interface ITiabRegistration {
    @API(since = "7.0.0")
    ITiabItem getTiabItem();

    @API(since = "7.0.0")
    ITiabItem getCreativeTiabItem();

    @API(since = "7.0.0")
    CreativeModeTab getCreativeTab();

    @API(since = "7.0.0")
    DataComponentType<IStoredTimeComponent> getStoredTime();

    @API(since = "7.0.0")
    EntityType<? extends Entity> getAcceleratorEntityType();
}

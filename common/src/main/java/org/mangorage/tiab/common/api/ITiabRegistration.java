package org.mangorage.tiab.common.api;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITiabItem;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.items.TiabItem;

public interface ITiabRegistration {
    ITiabItem getTiabItem();
    CreativeModeTab getCreativeTab();
    DataComponentType<IStoredTimeComponent> getStoredTime();
    EntityType<TimeAcceleratorEntity> getAcceleratorEntity();
}

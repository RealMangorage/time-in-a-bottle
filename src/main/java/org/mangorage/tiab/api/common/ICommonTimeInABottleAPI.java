package org.mangorage.tiab.api.common;

import net.minecraft.world.level.Level;
import org.mangorage.tiab.api.common.components.IStoredTimeComponent;
import org.mangorage.tiab.api.common.entity.IAcceleratorEntity;
import org.mangorage.tiab.api.common.item.ITiabItem;

public interface ICommonTimeInABottleAPI {
    APIHolder<ICommonTimeInABottleAPI> API = new APIHolder<>("Time in a bottle");

    ITiabItem getTiabItem();
    IStoredTimeComponent createStoredTimeComponent(int stored, int total);

    IAcceleratorEntity createAcceleratorEntity(Level level);

    String getModID();

    <T> T getPlatformAPI(Class<T> tClass);
}

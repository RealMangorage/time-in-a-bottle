package org.mangorage.tiab.api.common;

import org.mangorage.tiab.api.common.components.IStoredTimeComponent;
import org.mangorage.tiab.api.common.item.ITiabItem;

import java.util.function.Supplier;

public interface ICommonTimeInABottleAPI {
    Supplier<ICommonTimeInABottleAPI> API = new APIHolder<>("Time in a bottle");

    ITiabItem getTiabItem();
    IStoredTimeComponent createStoredTimeComponent(int stored, int total);
}

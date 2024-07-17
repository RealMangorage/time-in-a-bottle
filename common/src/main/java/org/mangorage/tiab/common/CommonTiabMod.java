package org.mangorage.tiab.common;

import org.mangorage.tiab.api.common.ICommonTimeInABottleAPI;
import org.mangorage.tiab.api.common.APIHolder;
import org.mangorage.tiab.api.common.components.IStoredTimeComponent;
import org.mangorage.tiab.api.common.item.ITiabItem;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.common.core.StoredTimeComponent;

import java.util.function.Function;

public class CommonTiabMod implements ICommonTimeInABottleAPI {

    private final LoaderSide loaderSide;
    private final Function<String, Boolean> isModLoaded;

    public CommonTiabMod(LoaderSide loaderSide, Function<String, Boolean> isModLoaded) {
        this.loaderSide = loaderSide;
        this.isModLoaded = isModLoaded;
        APIHolder<ICommonTimeInABottleAPI> API = (APIHolder<ICommonTimeInABottleAPI>) ICommonTimeInABottleAPI.API;
        API.setValue(this);
    }

    public LoaderSide getLoaderSide() {
        return loaderSide;
    }

    @Override
    public ITiabItem getTiabItem() {
        return CommonRegistration.TIAB_ITEM.get();
    }

    @Override
    public IStoredTimeComponent createStoredTimeComponent(int stored, int total) {
        return new StoredTimeComponent(stored, total);
    }
}

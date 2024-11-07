package org.mangorage.tiab.common.api;

import org.mangorage.tiab.common.core.LoaderSide;

public interface ICommonTimeInABottleAPI {
    APIHolder<ICommonTimeInABottleAPI> COMMON_API = new APIHolder<>("Time in a bottle");
    LoaderSide getLoaderSide();
    boolean isModLoaded(String modId);
    ITiabRegistration getRegistration();
}

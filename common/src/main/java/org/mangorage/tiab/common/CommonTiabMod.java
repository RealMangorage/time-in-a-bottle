package org.mangorage.tiab.common;

import org.mangorage.tiab.api.TiabAPI;
import org.mangorage.tiab.common.core.LoaderSide;

import java.util.function.Function;

public class CommonTiabMod extends TiabAPI {
    private final LoaderSide loaderSide;
    private final Function<String, Boolean> isModLoaded;

    public CommonTiabMod(LoaderSide loaderSide, Function<String, Boolean> isModLoaded) {
        this.loaderSide = loaderSide;
        this.isModLoaded = isModLoaded;
    }

    public LoaderSide getLoaderSide() {
        return loaderSide;
    }
}

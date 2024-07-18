package org.mangorage.tiab.common;

import net.minecraft.world.level.Level;
import org.mangorage.tiab.api.common.ICommonTimeInABottleAPI;
import org.mangorage.tiab.api.common.components.IStoredTimeComponent;
import org.mangorage.tiab.api.common.entity.IAcceleratorEntity;
import org.mangorage.tiab.api.common.item.ITiabItem;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import java.util.function.Function;

public class TiabMod implements ICommonTimeInABottleAPI {

    private final LoaderSide loaderSide;
    private final Function<String, Boolean> isModLoaded;

    public TiabMod(LoaderSide loaderSide, Function<String, Boolean> isModLoaded) {
        this.loaderSide = loaderSide;
        this.isModLoaded = isModLoaded;

        // TODO: FIX THIS, NOT A GOOD TIME TO DO IT. SHOULD BE DONE WHEN EVERYTHING IS REGISTERED AND LOADED....
        ICommonTimeInABottleAPI.API.setValue(this);
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

    @Override
    public IAcceleratorEntity createAcceleratorEntity(Level level) {
        return new TimeAcceleratorEntity(level);
    }

    @Override
    public String getModID() {
        return CommonConstants.MODID;
    }

    @Override
    public <T> T getPlatformAPI(Class<T> tClass) {
        return null;
    }
}

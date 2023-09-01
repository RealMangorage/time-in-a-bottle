package com.magorage.tiab.api;

import net.minecraftforge.fml.ModLoadingContext;
import java.util.function.Consumer;

public final class TiabProvider {
    private final String MODID;
    private final Consumer<ITimeInABottleAPI> apiConsumer;
    private boolean recieved = false;

    public TiabProvider(Consumer<ITimeInABottleAPI> apiConsumer) {
        this.MODID = ModLoadingContext.get().getActiveContainer().getModId();
        this.apiConsumer = apiConsumer;
    }

    public String getModID() {
        return MODID;
    }

    public void setAPI(ITimeInABottleAPI api) {
        if (recieved) return;
        this.recieved = true;
        apiConsumer.accept(api);
    }
}

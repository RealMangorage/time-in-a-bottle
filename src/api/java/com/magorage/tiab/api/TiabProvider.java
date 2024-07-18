package com.magorage.tiab.api;

import net.minecraftforge.fml.ModLoadingContext;
import java.util.function.Consumer;

public final class TiabProvider {
    private final String MODID;
    private final Consumer<ITimeInABottleAPI> apiConsumer;
    private final TiabAPIHooks hooks;
    private boolean recieved = false;

    public TiabProvider(Consumer<ITimeInABottleAPI> apiConsumer) {
        this(apiConsumer, TiabAPIHooks.create().build());
    }

    public TiabProvider(Consumer<ITimeInABottleAPI> apiConsumer, TiabAPIHooks hooks) {
        this.MODID = ModLoadingContext.get().getActiveContainer().getModId();
        this.apiConsumer = apiConsumer;
        this.hooks = hooks;
    }

    public String getModID() {
        return MODID;
    }

    public TiabAPIHooks getHooks() {
        return hooks;
    }

    public void setAPI(ITimeInABottleAPI api) {
        if (recieved) return;
        apiConsumer.accept(api);
    }

    public void setFinalized() {
        this.recieved = true;
    }
}

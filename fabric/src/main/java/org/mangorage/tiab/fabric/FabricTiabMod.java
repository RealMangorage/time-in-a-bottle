package org.mangorage.tiab.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.mangorage.tiab.common.core.CommonRegistration;

public class FabricTiabMod implements ModInitializer {
    public FabricTiabMod() {
        ServerLifecycleEvents.SERVER_STARTING.register(CommonRegistration::initServer);
    }

    @Override
    public void onInitialize() {
        CommonRegistration.init();
    }
}

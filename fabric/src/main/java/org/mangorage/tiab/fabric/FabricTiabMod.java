package org.mangorage.tiab.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.mangorage.tiab.common.CommonTiabMod;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;

public class FabricTiabMod extends CommonTiabMod implements ModInitializer {
    public FabricTiabMod() {
        super(LoaderSide.FABRIC, modid -> {
            return false;
        });
        ServerLifecycleEvents.SERVER_STARTING.register(CommonRegistration::initServer);
    }

    @Override
    public void onInitialize() {
        CommonRegistration.init();
    }
}

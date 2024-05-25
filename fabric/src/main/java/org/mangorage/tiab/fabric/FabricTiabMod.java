package org.mangorage.tiab.fabric;

import net.fabricmc.api.ModInitializer;
import org.mangorage.tiab.common.core.CommonRegistration;

public class FabricTiabMod implements ModInitializer {
    public FabricTiabMod() {

    }

    @Override
    public void onInitialize() {
        CommonRegistration.init();
    }
}

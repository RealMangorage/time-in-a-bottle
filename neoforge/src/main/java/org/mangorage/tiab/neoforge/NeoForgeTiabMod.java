package org.mangorage.tiab.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mangorage.tiab.common.core.CommonRegistration;

import static org.mangorage.tiab.common.CommonConstants.MODID;

@Mod(MODID)
public class NeoForgeTiabMod {

    public NeoForgeTiabMod(IEventBus bus) {
        bus.addListener(this::onRegisterEvent);
    }

    public void onRegisterEvent(RegisterEvent event) {
        CommonRegistration.init();
    }
}

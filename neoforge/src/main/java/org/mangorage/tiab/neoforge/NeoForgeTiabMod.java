package org.mangorage.tiab.neoforge;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.api.ITiabConfig;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.neoforge.core.Registration;

import static org.mangorage.tiab.common.CommonConstants.MODID;

@Mod(MODID)
public class NeoForgeTiabMod extends TiabMod {
    private final ITiabRegistration registration = new Registration.NeoForgeRegistration() {};
    private final ITiabConfig config;

    public NeoForgeTiabMod(IEventBus bus) {
        super(LoaderSide.NEOFORGE);

        Registration.register(bus);
        bus.addListener(this::onClient);
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);

        Pair<NeoForgeTiabConfig, ModConfigSpec> cfg = new ModConfigSpec.Builder()
                .configure(NeoForgeTiabConfig::new);

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, cfg.getRight());
        this.config = cfg.getKey();
    }


    public void onClient(FMLClientSetupEvent event) {
        EntityRenderers.register(Registration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        registerCommand(event.getDispatcher());
    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        TiabItem.tickPlayer(event.getEntity());
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public ITiabRegistration getRegistration() {
        return registration;
    }

    @Override
    public ITiabConfig getConfig() {
        return config;
    }
}

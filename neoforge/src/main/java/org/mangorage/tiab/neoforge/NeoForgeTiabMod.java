package org.mangorage.tiab.neoforge;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.mangorage.tiab.common.CommonTiabMod;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.common.core.registry.RegistryWrapper;
import org.mangorage.tiab.common.items.TiabItem;

import static org.mangorage.tiab.common.CommonConstants.MODID;

@Mod(MODID)
public class NeoForgeTiabMod extends CommonTiabMod {

    public NeoForgeTiabMod(IEventBus bus) {
        super(LoaderSide.NEOFORGE, modid -> ModList.get().isLoaded(modid));
        bus.addListener(this::onRegisterEvent);
        bus.addListener(this::onClient);
        NeoForge.EVENT_BUS.addListener(this::onServer);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);


        Pair<NeoForgeTiabConfig, ModConfigSpec> cfg = new ModConfigSpec.Builder()
                .configure(NeoForgeTiabConfig::new);

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, cfg.getRight());
        CommonRegistration.SERVER_CONFIG.setConfig(cfg.getKey());
    }

    public void onRegisterEvent(RegisterEvent event) {
        CommonRegistration.register(event.getRegistry().key(), new RegistryWrapper() {
            @Override
            public <T> Holder<T> registerForHolder(ResourceLocation id, T object) {
                return Registry.registerForHolder((Registry<T>) event.getRegistry(), id, object);
            }
        });
    }

    public void onClient(FMLClientSetupEvent event) {
        EntityRenderers.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }

    public void onServer(ServerStartingEvent event) {
        CommonRegistration.initServer(event.getServer());
    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        TiabItem.tickPlayer(event.getEntity());
    }
}

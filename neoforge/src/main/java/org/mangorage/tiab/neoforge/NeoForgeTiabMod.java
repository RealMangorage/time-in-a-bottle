package org.mangorage.tiab.neoforge;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.misc.IRegistrationWrapper;

import static org.mangorage.tiab.common.CommonConstants.MODID;

@Mod(MODID)
public class NeoForgeTiabMod {

    public NeoForgeTiabMod(IEventBus bus) {
        bus.addListener(this::onRegisterEvent);
        bus.addListener(this::onClient);
        NeoForge.EVENT_BUS.addListener(this::onServer);


        Pair<NeoForgeTiabConfig, ModConfigSpec> cfg = new ModConfigSpec.Builder()
                .configure(NeoForgeTiabConfig::new);

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, cfg.getRight());
        CommonRegistration.SERVER_CONFIG.setConfig(cfg.getKey());
    }

    @SuppressWarnings("unchecked")
    public void onRegisterEvent(RegisterEvent event) {
        CommonRegistration.init(new IRegistrationWrapper() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> resourceKey, ResourceLocation resourceLocation, T value) {
                if (event.getRegistryKey() == resourceKey)
                    event.register((ResourceKey<Registry<T>>) event.getRegistry().key(), resourceLocation, () -> value);
            }
        });
    }

    public void onClient(FMLClientSetupEvent event) {
        EntityRenderers.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }

    public void onServer(ServerStartingEvent event) {
        CommonRegistration.initServer(event.getServer());
    }
}

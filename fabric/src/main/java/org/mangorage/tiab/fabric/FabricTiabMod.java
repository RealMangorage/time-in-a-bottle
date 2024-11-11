package org.mangorage.tiab.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.ITiabConfig;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.fabric.core.Registration;

public final class FabricTiabMod extends TiabMod implements ModInitializer {
    private final ITiabRegistration registration = new Registration.FabricRegistration() {};
    private final ITiabConfig config;

    public FabricTiabMod() {
        super(LoaderSide.FABRIC);
        CommandRegistrationCallback.EVENT.register((dispatcher, context, commandSelection) -> registerCommand(dispatcher));
        Registration.register();
        this.config = FabricTiabConfig.get();
    }

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            EntityRendererRegistry.register(ICommonTimeInABottleAPI.COMMON_API.getDirect().getRegistration().getAcceleratorEntity(), TimeAcceleratorEntityRenderer::new);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
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

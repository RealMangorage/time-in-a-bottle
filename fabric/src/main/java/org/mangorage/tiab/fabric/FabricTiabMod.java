package org.mangorage.tiab.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.mangorage.tiab.common.CommonTiabMod;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;

public class FabricTiabMod extends CommonTiabMod implements ModInitializer {
    public FabricTiabMod() {
        super(LoaderSide.FABRIC, modid -> FabricLoader.getInstance().isModLoaded(modid));
        CommandRegistrationCallback.EVENT.register((dispatcher, context, commandSelection) -> CommonRegistration.initServer(dispatcher));
    }

    @Override
    public void onInitialize() {
        CommonRegistration.SERVER_CONFIG.setConfig(FabricTiabConfig.get());
        CommonRegistration.register();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            EntityRendererRegistry.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }
}

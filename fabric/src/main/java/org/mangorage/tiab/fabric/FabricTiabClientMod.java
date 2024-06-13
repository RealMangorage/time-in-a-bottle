package org.mangorage.tiab.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.mangorage.tiab.common.CommonTiabMod;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;

public class FabricTiabClientMod extends CommonTiabMod implements ClientModInitializer {
    public FabricTiabClientMod() {
        super(LoaderSide.FABRIC, modid -> FabricLoader.getInstance().isModLoaded(modid));
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }
}

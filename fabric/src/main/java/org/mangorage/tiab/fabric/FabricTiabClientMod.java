package org.mangorage.tiab.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;

public class FabricTiabClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }
}

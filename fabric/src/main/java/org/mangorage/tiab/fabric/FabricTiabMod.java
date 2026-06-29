package org.mangorage.tiab.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.component.TooltipDisplay;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.ITiabConfig;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.api.LoaderSide;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.fabric.core.Registration;

public final class FabricTiabMod extends TiabMod implements ModInitializer {
    private final ITiabRegistration registration = new Registration.FabricRegistration() {};
    private final ITiabConfig config;

    public FabricTiabMod() {
        super(LoaderSide.FABRIC);

        CommandRegistrationCallback.EVENT.register((dispatcher, context, commandSelection) -> registerCommand(dispatcher));
        ServerTickEvents.START_SERVER_TICK.register(e -> e.getPlayerList().getPlayers().forEach(this::tickPlayer));
        ItemTooltipCallback.EVENT.register((stack, context, flag, list) -> {
            stack.addToTooltip(
                    ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getStoredTime(),
                    context,
                    TooltipDisplay.DEFAULT,
                    list::add,
                    flag
            );
        });

        Registration.register();
        this.config = FabricTiabConfig.get();

        FabricLoader.getInstance().getObjectShare().put(CommonConstants.MODID + ":api", this);
    }

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            EntityRendererRegistry.register(Registration.ACCELERATOR_ENTITY, TimeAcceleratorEntityRenderer::new);
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
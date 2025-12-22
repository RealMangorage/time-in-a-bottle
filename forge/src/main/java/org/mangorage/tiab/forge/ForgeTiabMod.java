package org.mangorage.tiab.forge;

import net.minecraft.client.renderer.entity.EntityRenderers;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.ITiabConfig;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.api.LoaderSide;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.forge.core.Registration;

@Mod(CommonConstants.MODID)
public final class ForgeTiabMod extends TiabMod {
    private final ITiabRegistration registration = new Registration.ForgeRegistration() {};
    private final ITiabConfig config;

    public ForgeTiabMod(FMLJavaModLoadingContext context) {
        super(LoaderSide.FORGE);
        var modGroup = context.getModBusGroup();

        Registration.register(modGroup);

        FMLClientSetupEvent.getBus(modGroup).addListener(this::onClient);

        TickEvent.PlayerTickEvent.Post.BUS.addListener(this::onPlayerTick);
        RegisterCommandsEvent.BUS.addListener(this::onRegisterCommands);

        Pair<ForgeTiabConfig, ForgeConfigSpec> cfg = new ForgeConfigSpec.Builder()
                .configure(ForgeTiabConfig::new);

        context.registerConfig(ModConfig.Type.SERVER, cfg.getRight());

        this.config = cfg.getKey();
    }

    public void onClient(FMLClientSetupEvent event) {
        EntityRenderers.register(Registration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        registerCommand(event.getDispatcher());
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent.Post event) {
        tickPlayer(event.player());
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

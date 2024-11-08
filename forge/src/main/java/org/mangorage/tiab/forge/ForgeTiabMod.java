package org.mangorage.tiab.forge;

import net.minecraft.client.renderer.entity.EntityRenderers;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.mangorage.tiab.common.api.ITiabRegistration;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.forge.core.Registration;


@Mod(CommonConstants.MODID)
public class ForgeTiabMod extends TiabMod {

    public ForgeTiabMod(FMLJavaModLoadingContext context) {
        super(LoaderSide.FORGE);
        var modBus = context.getModEventBus();

        Registration.register(modBus);
        modBus.addListener(this::onClient);

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);

        Pair<ForgeTiabConfig, ForgeConfigSpec> cfg = new ForgeConfigSpec.Builder()
                .configure(ForgeTiabConfig::new);

        context.registerConfig(ModConfig.Type.SERVER, cfg.getRight());


        CommonRegistration.SERVER_CONFIG.setConfig(cfg.getKey());
    }

    public void onClient(FMLClientSetupEvent event) {
        EntityRenderers.register(Registration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        registerCommand(event.getDispatcher());
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        TiabItem.tickPlayer(event.player);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public ITiabRegistration getRegistration() {
        return new Registration.ForgeRegistration() {};
    }
}

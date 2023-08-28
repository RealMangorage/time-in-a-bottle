package com.haoict.tiab;

import com.haoict.tiab.client.renderer.TimeAcceleratorEntityRenderer;
import com.haoict.tiab.commands.TiabCommands;
import com.haoict.tiab.common.config.TiabConfig;
import com.haoict.tiab.common.core.EntityTypeRegistry;
import com.haoict.tiab.common.core.ItemRegistry;
import com.haoict.tiab.common.items.TimeInABottleAPI;
import com.magorage.tiab.api.ITimeInABottleAPI;
import com.magorage.tiab.api.ITimeInABottleAPIRequester;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.haoict.tiab.common.config.Constants.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class Tiab {

    public static class TiabProvider implements ITimeInABottleAPIRequester {
        private boolean recieved = false;
        @Override
        public void setAPI(ITimeInABottleAPI api) {
            if (recieved) return;
            this.recieved = true;
            TiabCommands.setAPI(api);
        }
    }

    public static final TiabProvider API_PROVIDER = new TiabProvider();


    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Tiab()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, TiabConfig.COMMON_CONFIG);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        InterModComms.sendTo(MOD_ID, ITimeInABottleAPI.IMC.GET_API, () -> API_PROVIDER);
        modEventBus.addListener(this::imcProcess);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::CreativeTab);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ItemRegistry.ITEMS.register(modEventBus);
        EntityTypeRegistry.TILE_ENTITIES.register(modEventBus);
    }

    @SuppressWarnings("unchecked")
    private void imcProcess(InterModProcessEvent event) {
        List<String> PROCESSED_MODS = new ArrayList<>();
        event.getIMCStream(ITimeInABottleAPI.IMC.GET_API::equals).forEach(e -> {
            if (PROCESSED_MODS.contains(e.senderModId()))
                throw new IllegalStateException("Some mod tried to get API for TIAB when it already has been given one! Mods can only get one API! %s mod tried to do this!".formatted(e.senderModId()));
            PROCESSED_MODS.add(e.senderModId());
            if (e.messageSupplier().get() instanceof ITimeInABottleAPIRequester requester)
                requester.setAPI(new TimeInABottleAPI(e.senderModId()));
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(EntityTypeRegistry.timeAcceleratorEntityType.get(), TimeAcceleratorEntityRenderer::new);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        event.getServer().getCommands().getDispatcher().register(Commands.literal(MOD_ID).then(TiabCommands.addTimeCommand).then(TiabCommands.removeTimeCommand));
    }

    public void CreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES) return;
        event.accept(ItemRegistry.timeInABottleItem);
    }
}

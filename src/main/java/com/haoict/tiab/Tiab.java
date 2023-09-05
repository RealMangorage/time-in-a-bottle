package com.haoict.tiab;

import com.haoict.tiab.client.renderer.TimeAcceleratorEntityRenderer;
import com.haoict.tiab.commands.TiabCommands;
import com.haoict.tiab.common.config.TiabConfig;
import com.haoict.tiab.common.core.EntityTypeRegistry;
import com.haoict.tiab.common.core.ItemRegistry;
import com.haoict.tiab.common.core.api.APIHooks;
import com.haoict.tiab.common.core.api.APIRegistry;
import com.haoict.tiab.common.core.api.TimeInABottleAPI;
import com.haoict.tiab.common.items.AbstractTiabItem;
import com.haoict.tiab.common.items.TimeInABottleItem;
import com.haoict.tiab.common.utils.Utils;
import com.magorage.tiab.api.TiabAPIHooks;
import com.magorage.tiab.api.TiabProvider;
import com.magorage.tiab.api.ITimeInABottleAPI;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static com.haoict.tiab.common.config.Constants.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class Tiab {
    private static ITimeInABottleAPI API;
    private static final TiabProvider API_PROVIDER = new TiabProvider((api) -> {
        TiabCommands.setAPI(api);
        Utils.setAPI(api);
        AbstractTiabItem.setAPI(api);
        TimeInABottleItem.setAPI(api);
        API = api;
    });

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Tiab()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, TiabConfig.COMMON_CONFIG);
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        InterModComms.sendTo(MOD_ID, ITimeInABottleAPI.IMC.GET_API, () -> API_PROVIDER);
        TimeInABottleAPI.IMCPublic.load(modEventBus);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::CreativeTab);
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ItemRegistry.ITEMS.register(modEventBus);
        EntityTypeRegistry.TILE_ENTITIES.register(modEventBus);

        TiabCommands.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        APIRegistry.freezeAll(); // Freeze our API registry!
        APIHooks.freezeAll();
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

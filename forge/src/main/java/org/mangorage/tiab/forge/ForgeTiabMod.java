package org.mangorage.tiab.forge;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.CommonTiabMod;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.common.core.registry.RegistryWrapper;
import org.mangorage.tiab.common.items.TiabItem;


@Mod(CommonConstants.MODID)
public class ForgeTiabMod extends CommonTiabMod {

    public ForgeTiabMod() {
        super(LoaderSide.FORGE, modid -> ModList.get().isLoaded(modid));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClient);
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);

        Pair<ForgeTiabConfig, ForgeConfigSpec> cfg = new ForgeConfigSpec.Builder()
                .configure(ForgeTiabConfig::new);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, cfg.getRight());

        CommonRegistration.SERVER_CONFIG.setConfig(cfg.getKey());
    }

    @SuppressWarnings("unchecked")
    public <T> ResourceKey<Registry<T>> castRegistry(ResourceKey<? extends Registry<?>> resourceKey) {
        return (ResourceKey<Registry<T>>) resourceKey;
    }

    @SuppressWarnings({"unchecked", ""})
    public void onRegister(RegisterEvent event) {
        CommonRegistration.register(castRegistry(event.getRegistryKey()), new RegistryWrapper() {
            @Override
            public <T> Holder<T> registerForHolder(ResourceLocation id, T object) {
                if (event.getForgeRegistry() == null) {
                    return Registry.registerForHolder(event.getVanillaRegistry(), id, object);
                } else {
                    event.getForgeRegistry().register(id, object);
                    return (Holder<T>) event.getForgeRegistry().getHolder(id).get();
                }
            }
        });
    }

    public void onClient(FMLClientSetupEvent event) {
        EntityRenderers.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommonRegistration.initServer(event.getDispatcher());
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        TiabItem.tickPlayer(event.player);
    }
}

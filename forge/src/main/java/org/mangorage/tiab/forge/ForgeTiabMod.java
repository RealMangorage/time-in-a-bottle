package org.mangorage.tiab.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.misc.IRegistrationWrapper;

@Mod(CommonConstants.MODID)
public class ForgeTiabMod {
    public ForgeTiabMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::register);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::Data);
    }

    public void register(RegisterEvent event) {
        CommonRegistration.init(new IRegistrationWrapper() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> resourceKey, ResourceLocation resourceLocation, T value) {
                if (resourceKey == event.getRegistryKey())
                    event.register(resourceKey, h -> h.register(resourceLocation, value));
            }
        });
    }

    public void Data(GatherDataEvent event) {
        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        var efh = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new ModelGen(output, efh));
    }
}

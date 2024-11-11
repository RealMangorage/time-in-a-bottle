package org.mangorage.apitest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;

public class APITest implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((serverGamePacketListener, packetSender, minecraftServer) -> {
            var plr = serverGamePacketListener.player;
            plr.addItem(ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().asItem().getDefaultInstance());
        });
    }
}

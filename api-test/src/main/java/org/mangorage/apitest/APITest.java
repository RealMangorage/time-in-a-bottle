package org.mangorage.apitest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;

public class APITest implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register(new ServerPlayConnectionEvents.Join() {
            @Override
            public void onPlayReady(ServerGamePacketListenerImpl serverGamePacketListener, PacketSender packetSender, MinecraftServer minecraftServer) {
                var plr = serverGamePacketListener.player;
                plr.addItem(ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().asItem().getDefaultInstance());
            }
        });
    }
}

package org.mangorage.tiab.fabric.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public final class ServerPlayerMixin {
    @Unique
    private int tiab$ticks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo info) {
        if (tiab$ticks % 20 == 0) {
            ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().tickPlayer((Player) (Object) this, 20);
        }
        tiab$ticks++;
    }

}

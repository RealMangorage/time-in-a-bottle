package org.mangorage.tiab.fabric.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.mangorage.tiab.common.items.TiabItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo info) {
        TiabItem.tickPlayer((Player) (Object) this);
    }
}

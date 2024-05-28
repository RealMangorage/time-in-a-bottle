package org.mangorage.tiab.fabric.mixin;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.items.TiabItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TiabItem.class)
public class TiabItemMixin implements FabricItem {
    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}

package org.mangorage.tiab.forge.mixin;


import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.mangorage.tiab.common.items.TiabItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TiabItem.class)
public abstract class TiabItemMixin implements IForgeItem {
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}

package org.mangorage.tiab.neoforge.mixin;


import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.mangorage.tiab.common.items.TiabItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TiabItem.class)
public abstract class TiabItemMixin implements IItemExtension {
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}

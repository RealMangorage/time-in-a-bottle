package org.mangorage.tiab.neoforge.core;

import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.items.TiabItem;

public class NeoForgeTiabItem extends TiabItem {
    public NeoForgeTiabItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}

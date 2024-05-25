package org.mangorage.tiab.common.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import org.mangorage.tiab.common.core.CommonRegistration;

import java.util.List;

public class TiabItem extends Item {
    public TiabItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int $$3, boolean $$4) {
        var comp = CommonRegistration.STORED_TIME_COMPONENT.get();
        if (stack.has(comp)) {
            stack.set(comp, stack.get(comp) + 1);

            ItemLore lore = new ItemLore(
                    List.of(
                            Component.literal("Stored Time: %s ticks".formatted(stack.get(comp)))
                    )
            );
            stack.set(DataComponents.LORE, lore);
        }
    }
}

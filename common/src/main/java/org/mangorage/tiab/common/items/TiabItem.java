package org.mangorage.tiab.common.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.misc.CommonHelper;

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
                            CommonHelper.getStoredTimeTranslated(stack)
                    )
            );
            stack.set(DataComponents.LORE, lore);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var lvl = context.getLevel();
        var pos = context.getClickedPos();

        var entity = new TimeAcceleratorEntity(lvl, pos);
        entity.setTimeRate(16);
        entity.setRemainingTime(10000);

        lvl.addFreshEntity(entity);

        return super.useOn(context);
    }
}

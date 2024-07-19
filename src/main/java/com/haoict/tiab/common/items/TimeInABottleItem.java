package com.haoict.tiab.common.items;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.config.NBTKeys;
import com.haoict.tiab.common.config.TiabConfig;
import com.haoict.tiab.common.core.api.APIRegistry;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleItemAPI;
import com.haoict.tiab.common.utils.Utils;
import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class TimeInABottleItem extends AbstractTiabItem {
    private static ITimeInABottleAPI API;

    public static void setAPI(ITimeInABottleAPI api) {
        if (API != null) return;
        API = api;
    }

    public TimeInABottleItem() {
        super();
        class Provider implements ITimeInABottleItemAPI {
            final TimeInABottleItem item;
            private Provider(TimeInABottleItem item) {
                this.item = item;
            }

            @Override
            public int getStoredEnergy(ItemStack stack) {
                return item.getStoredEnergy(stack);
            }

            @Override
            public void setStoredEnergy(ItemStack stack, int energy) {
                item.setStoredEnergy(stack, energy);
            }

            @Override
            public void applyDamage(ItemStack stack, int damage) {
                item.applyDamage(stack, damage);
            }

            @Override
            public int getTotalAccumulatedTime(ItemStack stack) {
                return item.getTotalAccumulatedTime(stack);
            }

            @Override
            public void setTotalAccumulatedTime(ItemStack stack, int value) {
                item.setTotalAccumulatedTime(stack, value);
            }

            @Override
            public int getEnergyCost(int timeRate) {
                return item.getEnergyCost(timeRate);
            }

            @Override
            public void playSound(Level level, BlockPos pos, int nextRate) {
                item.playSound(level, pos, nextRate);
            }

            @Override
            public InteractionResult accelerateBlock(ITimeInABottleAPI API, ItemStack stack, Player player, Level level, BlockPos pos) {
                return item.accelerateBlock(API, stack, player, level, pos);
            }
        }
        APIRegistry.registerAccess(ITimeInABottleItemAPI.class, new Provider(this));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        // TODO: USE API HOOKS
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
        if (level.isClientSide) {
            return;
        }

        if (API.canUse()) {
            if (level.getGameTime() % Constants.TICK_CONST == 0) {
                int storedTime = API.getStoredTime(itemStack);
                if (storedTime < TiabConfig.COMMON.maxStoredTime.get()) {
                    API.setStoredTime(itemStack, storedTime + Constants.TICK_CONST);
                }

                int totalAccumulatedTime = API.getTotalTime(itemStack);
                if (totalAccumulatedTime < TiabConfig.COMMON.maxStoredTime.get()) {
                    API.setTotalTime(itemStack, totalAccumulatedTime + Constants.TICK_CONST);
                }
            }

            // remove time if player has other TIAB items in his inventory, check every 10 sec
            if (level.getGameTime() % (Constants.TICK_CONST * 10) == 0) {
                if (!(entity instanceof Player player)) {
                    return;
                }

                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack invStack = player.getInventory().getItem(i);
                    if (invStack.getItem() == this) {
                        if (invStack != itemStack) {
                            int otherTimeData = API.getStoredTime(invStack);
                            int myTimeData = API.getStoredTime(itemStack);

                            if (myTimeData < otherTimeData) {
                                API.setStoredTime(itemStack, 0);
                            }
                        }
                    }
                }


            }
        } else {
            ServerPlayer player = null;
            if (entity instanceof ServerPlayer serverPlayer)
                player = serverPlayer;
            API.callTickEvent(player, itemStack);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        tooltip.add(Utils.getStoredTimeTranslated(itemStack));
        tooltip.add(Utils.getTotalTimeTranslated(itemStack));
    }

    @Override
    protected int getStoredEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBTKeys.STORED_TIME);
    }

    @Override
    protected void setStoredEnergy(ItemStack stack, int energy) {
        int newStoredTime = Math.min(energy, TiabConfig.COMMON.maxStoredTime.get());
        stack.getOrCreateTag().putInt(NBTKeys.STORED_TIME, newStoredTime);
    }

    @Override
    protected void applyDamage(ItemStack stack, int damage) {
        setStoredEnergy(stack, getStoredEnergy(stack) - damage);
    }

    private int getTotalAccumulatedTime(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBTKeys.TOTAL_ACCUMULATED_TIME);
    }

    private void setTotalAccumulatedTime(ItemStack stack, int value) {
        int newValue = Math.min(value, TiabConfig.COMMON.maxStoredTime.get());
        stack.getOrCreateTag().putInt(NBTKeys.TOTAL_ACCUMULATED_TIME, newValue);
    }
}

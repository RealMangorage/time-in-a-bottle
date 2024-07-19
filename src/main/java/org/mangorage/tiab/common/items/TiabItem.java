package org.mangorage.tiab.common.items;

import java.util.Optional;

import org.mangorage.tiab.api.common.components.IStoredTimeComponent;
import org.mangorage.tiab.api.common.item.ITiabItem;
import org.mangorage.tiab.common.CommonConstants.NBTKeys;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.misc.CommonHelper;
import org.mangorage.tiab.common.misc.CommonSoundHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TiabItem extends Item implements ITiabItem {
    public TiabItem(Properties properties) {
        super(properties);
    }

    public static void tickPlayer(Player player) {
        for (ItemStack item : player.getInventory().items) {
            if (item.getItem() instanceof TiabItem tiabItem) {
                tiabItem.tickBottle(item);
                break;
            }
        }
    }

    public void tickBottle(ItemStack stack) {
        if (stack.getItem() != this) return;

        //var comp = CommonRegistration.STORED_TIME_COMPONENT.get();

        CommonHelper.modify(stack, () -> getStoredComponent(stack), old -> {
            if (CommonHelper.isPositive(old.stored() + 1) && CommonHelper.isPositive(old.total() + 1)) {
                return new StoredTimeComponent(Math.min(old.stored() + 1, CommonRegistration.SERVER_CONFIG.get().MAX_STORED_TIME()), old.total() + 1);
            } else {
                return old;
            }
        });

//        ItemLore lore = new ItemLore(
//                List.of(
//                        CommonHelper.getStoredTimeTranslated(stack),
//                        CommonHelper.getTotalTimeTranslated(stack)
//                )
//        );
//
//        stack.set(DataComponents.LORE, lore);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        BlockEntity targetTE = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if ((targetTE == null && !blockState.isRandomlyTicking()) || blockState.is(CommonRegistration.TIAB_UN_ACCELERATABLE)) {
            return InteractionResult.FAIL;
        }

        int nextRate = 1;
        int energyRequired = getEnergyCost(nextRate);
        boolean isCreativeMode = player != null && player.isCreative();

        Optional<TimeAcceleratorEntity> o = level.getEntitiesOfClass(TimeAcceleratorEntity.class, new AABB(pos)).stream().findFirst();

        if (o.isPresent()) {
            TimeAcceleratorEntity entityTA = o.get();
            int currentRate = entityTA.getTimeRate();
            int usedUpTime = getEachUseDuration() - entityTA.getRemainingTime();

            if (currentRate >= Math.pow(2, CommonRegistration.SERVER_CONFIG.get().MAX_RATE_MULTI() - 1)) {
                return InteractionResult.SUCCESS;
            }

            nextRate = currentRate * 2;
            int timeAdded = usedUpTime / 2;
            energyRequired = getEnergyCost(nextRate);

            if (!canUse(stack, isCreativeMode, energyRequired)) {
                return InteractionResult.SUCCESS;
            }

            entityTA.setTimeRate(nextRate);
            entityTA.setRemainingTime(entityTA.getRemainingTime() + timeAdded);
        } else {
            // First use
            if (!canUse(stack, isCreativeMode, energyRequired)) {
                return InteractionResult.SUCCESS;
            }

            TimeAcceleratorEntity entityTA = new TimeAcceleratorEntity(level);
            entityTA.setBlockPos(pos);
            entityTA.setRemainingTime(getEachUseDuration());
            level.addFreshEntity(entityTA);
        }

        if (!isCreativeMode) {
            final int required = energyRequired;
            CommonHelper.modify(stack, () -> getStoredComponent(stack), old -> {
                var newStoredTime = Math.min(old.stored() - required, CommonRegistration.SERVER_CONFIG.get().MAX_STORED_TIME());
                return new StoredTimeComponent(newStoredTime, old.total());
            });
        }

        CommonSoundHelper.playSound(level, pos, nextRate);

        return InteractionResult.SUCCESS;
    }


    public int getEachUseDuration() {
        // TICK CONST * EACH USE DURATION (in secs)
        return CommonRegistration.SERVER_CONFIG.get().TICKS_CONST() * CommonRegistration.SERVER_CONFIG.get().EACH_USE_DURATION();
    }

    public int getEnergyCost(int timeRate) {
        if (timeRate <= 1) return getEachUseDuration();
        return timeRate / 2 * getEachUseDuration();
    }

    public boolean canUse(ItemStack stack, boolean isCreativeMode, int energyRequired) {
        return getStoredComponent(stack).stored() >= energyRequired || isCreativeMode;
    }

    public StoredTimeComponent getStoredComponent(ItemStack stack) {
    	int stored = stack.getOrCreateTag().getInt(NBTKeys.STORED_TIME);
    	int accumulated = stack.getOrCreateTag().getInt(NBTKeys.TOTAL_ACCUMULATED_TIME);
    	StoredTimeComponent comp = new StoredTimeComponent(stored,accumulated);
    	return comp;	
    }


    // FABRIC
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    public boolean allowContinuingBlockBreaking(Player player, ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    // FORGE & NEOFORGE
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public IStoredTimeComponent getStoredTime(ItemStack itemStack) {
return getStoredComponent(itemStack);
    }

    @Override
    public void setStoredTime(ItemStack itemStack, IStoredTimeComponent iStoredTimeComponent) {
    	itemStack.getOrCreateTag().putInt(NBTKeys.STORED_TIME,iStoredTimeComponent.stored());
    	itemStack.getOrCreateTag().putInt(NBTKeys.TOTAL_ACCUMULATED_TIME,iStoredTimeComponent.total());
    	}
}

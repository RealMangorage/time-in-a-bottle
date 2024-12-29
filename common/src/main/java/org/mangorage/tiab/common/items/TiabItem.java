package org.mangorage.tiab.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITiabItem;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.mangorage.tiab.common.misc.CommonHelper;
import org.mangorage.tiab.common.misc.CommonSoundHelper;

import java.util.List;
import java.util.Optional;

public class TiabItem extends Item implements ITiabItem {
    public TiabItem(Properties properties) {
        super(properties);
    }

    public void tickPlayer(Player player) {
        for (ItemStack item : player.getInventory().items) {
            if (item.getItem() instanceof TiabItem tiabItem) {
                tiabItem.tickBottle(item);
                break;
            }
        }
    }

    public void tickBottle(ItemStack stack) {
        if (stack.getItem() != this) return;
        var cfg = ICommonTimeInABottleAPI.COMMON_API.get().getConfig();
        var comp = ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getStoredTime();

        CommonHelper.modify(stack, comp, () -> new StoredTimeComponent(0, 0), old -> {
            if (CommonHelper.isPositive(old.stored() + 1) && CommonHelper.isPositive(old.total() + 1)) {
                return new StoredTimeComponent(Math.min(old.stored() + 1, cfg.MAX_STORED_TIME()), old.total() + 1);
            } else {
                return old;
            }
        });

        ItemLore lore = new ItemLore(
                List.of(
                        CommonHelper.getStoredTimeTranslated(stack),
                        CommonHelper.getTotalTimeTranslated(stack)
                )
        );

        stack.set(DataComponents.LORE, lore);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        var cfg = ICommonTimeInABottleAPI.COMMON_API.get().getConfig();

        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        BlockEntity targetTE = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if ((targetTE == null && !blockState.isRandomlyTicking()) || blockState.is(ICommonTimeInABottleAPI.COMMON_API.get().getTagKey())) {
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

            if (currentRate >= Math.pow(2, cfg.MAX_RATE_MULTI() - 1)) {
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
            CommonHelper.modify(stack, ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getStoredTime(), () -> new StoredTimeComponent(0, 0), old -> {
                var newStoredTime = Math.min(old.stored() - required, cfg.MAX_STORED_TIME());
                return new StoredTimeComponent(newStoredTime, old.total());
            });
        }

        CommonSoundHelper.playSound(level, pos, nextRate);

        return InteractionResult.SUCCESS;
    }


    public int getEachUseDuration() {
        // TICK CONST * EACH USE DURATION (in secs)
        return ICommonTimeInABottleAPI.COMMON_API.get().getConfig().TICKS_CONST() * ICommonTimeInABottleAPI.COMMON_API.get().getConfig().EACH_USE_DURATION();
    }

    public int getEnergyCost(int timeRate) {
        if (timeRate <= 1) return getEachUseDuration();
        return timeRate / 2 * getEachUseDuration();
    }

    public boolean canUse(ItemStack stack, boolean isCreativeMode, int energyRequired) {
        return getStoredComponent(stack).stored() >= energyRequired || isCreativeMode;
    }

    public IStoredTimeComponent getStoredComponent(ItemStack stack) {
        return stack.getOrDefault(ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getStoredTime(), new StoredTimeComponent(0, 0));
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
}

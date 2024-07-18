package com.haoict.tiab.common.items;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.config.TiabConfig;
import com.haoict.tiab.common.entities.TimeAcceleratorEntity;
import com.haoict.tiab.common.utils.PlaySound;
import com.haoict.tiab.common.utils.SendMessage;
import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class AbstractTiabItem extends Item {
    private static final String[] NOTES = {"C", "D", "E", "F", "G2", "A2", "B2", "C2", "D2", "E2", "F2"};
    private static ITimeInABottleAPI API;

    public static void setAPI(ITimeInABottleAPI api) {
        if (API != null) return;
        API = api;
    }

    public AbstractTiabItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext context) { // TODO: API HOOK
        Level level = context.getLevel();

        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        BlockEntity targetTE = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (targetTE == null && !blockState.isRandomlyTicking()) {
            return InteractionResult.FAIL;
        }

        if (API.canUse()) {
            accelerateBlock(API, stack, player, level, pos);
        } else {
            if (!API.callUseEvent(stack, player, level, pos) && player instanceof ServerPlayer serverPlayer)
                SendMessage.sendStatusMessage(serverPlayer, "TIAB has had its API access revoked.");

        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult accelerateBlock(ITimeInABottleAPI API, ItemStack stack, Player player, Level level, BlockPos pos) {
        int nextRate = 1;
        int energyRequired = API.getEnergyCost(nextRate);
        boolean isCreativeMode = player != null && player.isCreative();

        if (API.canUse()) {
            Optional<TimeAcceleratorEntity> o = level.getEntitiesOfClass(TimeAcceleratorEntity.class, new AABB(pos)).stream().findFirst();

            if (o.isPresent()) {
                TimeAcceleratorEntity entityTA = o.get();
                int currentRate = entityTA.getTimeRate();
                int usedUpTime = getEachUseDuration() - entityTA.getRemainingTime();

                if (currentRate >= Math.pow(2, TiabConfig.COMMON.maxTimeRatePower.get() - 1)) {
                    return InteractionResult.SUCCESS;
                }

                nextRate = currentRate * 2;
                int timeAdded = usedUpTime / 2;
                energyRequired = API.getEnergyCost(nextRate);

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

                TimeAcceleratorEntity entityTA = new TimeAcceleratorEntity(level, pos, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                entityTA.setRemainingTime(getEachUseDuration());
                level.addFreshEntity(entityTA);
            }

            if (!isCreativeMode) {
                API.applyDamage(stack, energyRequired);
            }

            API.playSound(level, pos, nextRate);
        }
        return InteractionResult.SUCCESS;
    }

    protected int getEachUseDuration() {
        return Constants.TICK_CONST * TiabConfig.COMMON.eachUseDuration.get();
    }

    protected int getEnergyCost(int timeRate) {
        if (timeRate <= 1) return getEachUseDuration();
        return timeRate / 2 * getEachUseDuration();
    }

    protected boolean canUse(ItemStack stack, boolean isCreativeMode, int energyRequired) {
        return API.getStoredTime(stack) >= energyRequired || isCreativeMode;
    }

    protected abstract int getStoredEnergy(ItemStack stack);

    protected abstract void setStoredEnergy(ItemStack stack, int energy);

    protected abstract void applyDamage(ItemStack stack, int damage);

    protected void playSound(Level level, BlockPos pos, int nextRate) {
        switch (nextRate) {
            case 1 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[0]);
            case 2 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[1]);
            case 4 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[2]);
            case 8 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[3]);
            case 16 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[4]);
            case 32 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[5]);
            case 64 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[6]);
            case 128 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[7]);
            case 256 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[8]);
            case 512 -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[9]);
            default -> PlaySound.playNoteBlockHarpSound(level, pos, NOTES[10]);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }
}

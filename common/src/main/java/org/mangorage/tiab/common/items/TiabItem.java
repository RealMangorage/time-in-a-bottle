package org.mangorage.tiab.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITiabItem;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.lang.Styles;
import org.mangorage.tiab.common.misc.CommonHelper;
import org.mangorage.tiab.common.misc.CommonSoundHelper;
import java.util.List;
import java.util.Optional;

public class TiabItem extends Item implements ITiabItem {

    private final boolean creative;

    public TiabItem(boolean creative, Properties properties) {
        super(properties);
        this.creative = creative;
    }

    @Override
    public boolean isCreative() {
        return creative;
    }

    public void tickPlayer(Player player) {
        if (creative) return;
        tickPlayer(player, 1);
    }

    @Override
    public void tickPlayer(Player player, int ticks) {
        if (creative) return;
        final var item = ICommonTimeInABottleAPI.COMMON_API.get().findTiabItem(player);
        if (item.isEmpty()) return;
        tickBottle(item, ticks);
    }

    @Override
    public void tickBottle(ItemStack stack) {
        if (creative) return;
        tickBottle(stack, 1);
    }

    public void tickBottle(ItemStack stack, int ticks) {
        if (creative) return;
        if (stack.getItem() != this || ticks <= 0) return;

        var comp = ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getStoredTime();

        var compInst = CommonHelper.modify(stack, comp, new StoredTimeComponent(0, 0), old -> {
            if (CommonHelper.isPositive(old.stored() + ticks) && CommonHelper.isPositive(old.total() + ticks)) {
                var cfg = ICommonTimeInABottleAPI.COMMON_API.get().getConfig();
                return new StoredTimeComponent(Math.min(old.stored() + ticks, cfg.MAX_STORED_TIME()), old.total() + ticks);
            } else {
                return old;
            }
        });


        ItemLore lore = new ItemLore(
                List.of(
                        CommonHelper.getStoredTimeTranslated(compInst.stored()),
                        CommonHelper.getTotalTimeTranslated(compInst.total())
                )
        );

        stack.set(DataComponents.LORE, lore);
    }

    private boolean isValidBlockTarget(Level level, BlockPos pos, BlockState state, BlockEntity be) {
        return !((be == null && !state.isRandomlyTicking()) ||
                state.is(ICommonTimeInABottleAPI.COMMON_API.get().getTagKey()));
    }

    private boolean isValidEntityTarget(Entity entity) {
        return entity != null && entity.isAlive() && entity instanceof LivingEntity;
    }


    private InteractionResult accelerate(Level level, BlockPos pos, Entity targetEntity, ItemStack stack, Player player) {
        var cfg = ICommonTimeInABottleAPI.COMMON_API.get().getConfig();

        int nextRate = 1;
        int energyRequired = getEnergyCost(nextRate);
        boolean isCreativeMode = (player != null && player.isCreative()) || creative;

        // Find existing accelerator
        Optional<? extends ITimeAcceleratorEntity> o = ICommonTimeInABottleAPI.COMMON_API.get()
                .getEntities(level, new AABB(pos))
                .stream()
                .filter(entity -> entity instanceof ITimeAcceleratorEntity)
                .findAny();

        if (o.isPresent()) {
            ITimeAcceleratorEntity entityTA = o.get();

            int currentRate = entityTA.getTimeRate();
            int usedUpTime = getEachUseDuration() - entityTA.getRemainingTime();

            if (currentRate >= Math.pow(2, cfg.MAX_RATE_MULTI() - 1)) {
                return InteractionResult.SUCCESS_SERVER;
            }

            nextRate = currentRate * 2;
            int timeAdded = usedUpTime / 2;
            energyRequired = getEnergyCost(nextRate);

            if (!canUse(stack, isCreativeMode, energyRequired)) {
                return InteractionResult.SUCCESS_SERVER;
            }

            entityTA.setTimeRate(nextRate);
            entityTA.setRemainingTime(entityTA.getRemainingTime() + timeAdded);

        } else {
            if (!canUse(stack, isCreativeMode, energyRequired)) {
                return InteractionResult.SUCCESS_SERVER;
            }

            ITimeAcceleratorEntity entityTA = ICommonTimeInABottleAPI.COMMON_API.get().createEntity((ServerLevel) level);

            // --- THIS is the important part you kept dodging ---
            if (targetEntity != null) {
                entityTA.setTargetedEntityUUID(targetEntity.getUUID());
                entityTA.setTargetedBlockPos(null);
                entityTA.asEntity().setPos(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
            } else {
                entityTA.setTargetedBlockPos(pos);
                entityTA.setTargetedEntityUUID(null);
            }

            entityTA.setRemainingTime(getEachUseDuration());


            level.addFreshEntity(entityTA.asEntity());
        }

        if (!isCreativeMode) {
            final int required = energyRequired;
            CommonHelper.modify(
                    stack,
                    ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getStoredTime(),
                    new StoredTimeComponent(0, 0),
                    old -> new StoredTimeComponent(
                            Math.min(old.stored() - required, cfg.MAX_STORED_TIME()),
                            old.total()
                    )
            );
        }

        CommonSoundHelper.playSound(level, pos, nextRate);

        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        Level level = entity.level();

        if (level.isClientSide()) return InteractionResult.PASS;

        if (!isValidEntityTarget(entity)) {
            return InteractionResult.FAIL;
        }

        return accelerate(
                level,
                entity.blockPosition(),
                entity,
                stack,
                player
        );
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) return InteractionResult.PASS;

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        if (!isValidBlockTarget(level, pos, state, be)) {
            return InteractionResult.FAIL;
        }

        return accelerate(
                level,
                pos,
                null,
                context.getItemInHand(),
                context.getPlayer()
        );
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

    // Forge
    public boolean shouldCauseReequipAnimation(ItemStack itemStack, ItemStack itemStack1, boolean b) {
        return false;
    }

    public boolean shouldCauseBlockBreakReset(ItemStack itemStack, ItemStack itemStack1) {
        return true;
    }

}

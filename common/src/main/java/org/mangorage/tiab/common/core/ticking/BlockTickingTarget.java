package org.mangorage.tiab.common.core.ticking;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;

public final class BlockTickingTarget  {

    public static boolean tick(ServerLevel serverLevel, BlockPos blockPos, ITimeAcceleratorEntity timeEntity) {
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (blockState.is(ICommonTimeInABottleAPI.COMMON_API.get().getTagKey())) {
            timeEntity.asEntity().remove(Entity.RemovalReason.KILLED);
            timeEntity.setRemainingTime(0);
            timeEntity.setTimeRate(0);
            return false;
        }

        BlockEntity targetBlockEntity = serverLevel.getBlockEntity(blockPos);
        BlockEntityTicker<BlockEntity> targetTicker = null;
        if (targetBlockEntity != null)
            targetTicker = targetBlockEntity.getBlockState().getTicker(serverLevel, (BlockEntityType<BlockEntity>) targetBlockEntity.getType());

        for (int i = 0; i < timeEntity.getTimeRate(); i++) {
            if (targetTicker != null) {
                targetTicker.tick(serverLevel, blockPos, blockState, targetBlockEntity);
            } else if (blockState.isRandomlyTicking()) {
                // if is random ticket block (grass block, sugar cane, wheat or sapling, ...)
                blockState.randomTick(serverLevel, blockPos, serverLevel.getRandom());
            } else {
                return false;
            }
        }

        return true;
    }
}

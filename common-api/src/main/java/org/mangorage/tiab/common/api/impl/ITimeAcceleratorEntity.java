package org.mangorage.tiab.common.api.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface ITimeAcceleratorEntity {
    BlockPos getBlockPos();
    int getTimeRate();

    void setTimeRate(int rate);
    int getRemainingTime();

    void setRemainingTime(int remainingTime);
    void setBlockPos(BlockPos blockPos);

    Entity asEntity();
}

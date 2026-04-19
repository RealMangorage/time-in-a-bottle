package org.mangorage.tiab.common.api.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.mangorage.tiab.common.api.annotations.API;

@API(since = "7.0.2")
public interface ITimeAcceleratorEntity {
    @API(since = "7.0.2")
    BlockPos getBlockPos();

    @API(since = "7.0.2")
    int getTimeRate();

    @API(since = "7.0.2")
    void setTimeRate(int rate);

    @API(since = "7.0.2")
    int getRemainingTime();

    @API(since = "7.0.2")
    void setRemainingTime(int remainingTime);

    @API(since = "7.0.2")
    void setBlockPos(BlockPos blockPos);

    @API(since = "7.0.2")
    Entity asEntity();
}

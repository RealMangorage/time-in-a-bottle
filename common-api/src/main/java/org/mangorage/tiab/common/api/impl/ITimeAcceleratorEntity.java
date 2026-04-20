package org.mangorage.tiab.common.api.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.mangorage.tiab.common.api.annotations.API;
import org.mangorage.tiab.common.api.annotations.Use;

import java.util.UUID;

@API(since = "7.0.0")
public interface ITimeAcceleratorEntity {
    @API(since = "7.0.0")
    @Deprecated(forRemoval = true, since = "7.1..0")
    @Use("ITimeAcceleratorEntity#getTargetedBlockPos")
    default BlockPos getBlockPos() {
        return getTargetedBlockPos();
    }

    @API(since = "7.1.0")
    BlockPos getTargetedBlockPos();

    @API(since = "7.1.0")
    UUID getTargetedEntityUUID();

    @API(since = "7.0.0")
    int getTimeRate();

    @API(since = "7.0.0")
    void setTimeRate(int rate);

    @API(since = "7.0.0")
    int getRemainingTime();

    @API(since = "7.0.0")
    void setRemainingTime(int remainingTime);

    @API(since = "7.0.0")
    @Deprecated(forRemoval = true, since = "7.1..0")
    @Use("ITimeAcceleratorEntity#setTargetedBlockPos")
    default void setBlockPos(BlockPos blockPos) {
        setTargetedBlockPos(blockPos);
    }

    @API(since = "7.1.0")
    void setTargetedBlockPos(BlockPos blockPos);

    @API(since = "7.1..0")
    void setTargetedEntityUUID(UUID uuid);

    @API(since = "7.0.0")
    Entity asEntity();
}

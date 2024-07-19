package org.mangorage.tiab.api.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * The Accelerator Entity.
 */
public interface IAcceleratorEntity {
    void setBlockPos(BlockPos blockPos);
    BlockPos getBlockPos();
    Level level();

    void setTimeRate(int rate);
    int getTimeRate();

    void setRemainingTime(int rate);
    int getRemainingTime();

    /**
     * Use this to be able to do stuff with it, such as adding it to a level.
     * @return An Object that extends {@link Entity}
     */
    Entity getEntity();
}

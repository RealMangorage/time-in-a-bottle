package org.mangorage.tiab.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.mangorage.tiab.api.common.entity.IAcceleratorEntity;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.core.CommonRegistration;

public class TimeAcceleratorEntity extends Entity implements IAcceleratorEntity {
    private static final EntityDataAccessor<Integer> timeRate = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> timeRemaining = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);
    private BlockPos pos;

    public TimeAcceleratorEntity(Level worldIn) {
        super(CommonRegistration.ACCELERATOR_ENTITY.get(), worldIn);
        entityData.set(timeRate, 1);
    }

    @Override
    protected void defineSynchedData() {
    	entityData.define(timeRate, 1);
    	entityData.define(timeRemaining, 0);
    }

    @SuppressWarnings("unchecked") // It's fine!
    @Override
    public void tick() {
        if (level().isClientSide()) return;
        ServerLevel level = (ServerLevel) level();

        if (pos == null) {
            this.remove(RemovalReason.KILLED);
            return;
        }

        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(CommonRegistration.TIAB_UN_ACCELERATABLE)) {
            this.remove(RemovalReason.KILLED);
            setRemainingTime(0);
            setTimeRate(1);
            return;
        }
        BlockEntity targetBlockEntity = level.getBlockEntity(pos);
        BlockEntityTicker<BlockEntity> targetTicker = null;
        if (targetBlockEntity != null)
            targetTicker = targetBlockEntity.getBlockState().getTicker(level, (BlockEntityType<BlockEntity>) targetBlockEntity.getType());

        for (int i = 0; i < getTimeRate(); i++) {
            if (targetTicker != null) {
                targetTicker.tick(level, pos, blockState, targetBlockEntity);
            } else if (blockState.isRandomlyTicking()) {
                // if is random ticket block (grass block, sugar cane, wheat or sapling, ...)
                if (level.random.nextInt(1365) == 0) {
                    blockState.randomTick(level, pos, level.getRandom());
                }
            } else {
                this.remove(RemovalReason.KILLED);
                break;
            }
        }

        setRemainingTime(getRemainingTime() - 1);
        if (getRemainingTime() <= 0) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean isColliding(BlockPos $$0, BlockState $$1) {
        return false;
    }

    @Override
    protected Vec3 limitPistonMovement(Vec3 $$0) {
        return Vec3.ZERO;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        entityData.set(timeRate, compound.getInt(CommonConstants.NBTKeys.ENTITY_TIME_RATE));
        setRemainingTime(compound.getInt(CommonConstants.NBTKeys.ENTITY_REMAINING_TIME));
        this.pos = NbtUtils.readBlockPos(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt(CommonConstants.NBTKeys.ENTITY_TIME_RATE, getTimeRate());
        compound.putInt(CommonConstants.NBTKeys.ENTITY_REMAINING_TIME, getRemainingTime());
        compound.put(CommonConstants.NBTKeys.ENTITY_POS, NbtUtils.writeBlockPos(this.pos));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void setBlockPos(BlockPos blockPos) {
        this.pos = blockPos.immutable();
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }

    @Override
    public int getTimeRate() {
        return entityData.get(timeRate);
    }

    @Override
    public void setTimeRate(int rate) {
        entityData.set(timeRate, rate);
    }

    @Override
    public int getRemainingTime() {
        return entityData.get(timeRemaining);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public void setRemainingTime(int remainingTime) {
        entityData.set(timeRemaining, remainingTime);
    }
}

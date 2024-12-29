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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;

public class TimeAcceleratorEntity extends Entity {
    private static final EntityDataAccessor<Integer> timeRate = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> timeRemaining = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);
    private BlockPos pos;

    public TimeAcceleratorEntity(Level worldIn) {
        super(TiabMod.COMMON_API.get().getRegistration().getAcceleratorEntityType(), worldIn);
        entityData.set(timeRate, 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(timeRate, 1);
        builder.define(timeRemaining, 0);
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
        if (blockState.is(ICommonTimeInABottleAPI.COMMON_API.get().getTagKey())) {
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
        this.pos = NbtUtils.readBlockPos(compound, CommonConstants.NBTKeys.ENTITY_POS).orElse(new BlockPos(0,0,0));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt(CommonConstants.NBTKeys.ENTITY_TIME_RATE, getTimeRate());
        compound.putInt(CommonConstants.NBTKeys.ENTITY_REMAINING_TIME, getRemainingTime());
        compound.put(CommonConstants.NBTKeys.ENTITY_POS, NbtUtils.writeBlockPos(this.pos));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }

    public void setBlockPos(BlockPos blockPos) {
        this.pos = blockPos.immutable();
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public BlockPos getBlockPos() {
        return pos;
    }

    public int getTimeRate() {
        return entityData.get(timeRate);
    }

    public void setTimeRate(int rate) {
        entityData.set(timeRate, rate);
    }

    public int getRemainingTime() {
        return entityData.get(timeRemaining);
    }


    public void setRemainingTime(int remainingTime) {
        entityData.set(timeRemaining, remainingTime);
    }
}

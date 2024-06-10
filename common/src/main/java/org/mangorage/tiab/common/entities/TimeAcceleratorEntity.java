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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.core.CommonRegistration;

public class TimeAcceleratorEntity extends Entity {
    private static final EntityDataAccessor<Integer> timeRate = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> timeRemaining = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);
    private BlockPos pos;

    public TimeAcceleratorEntity(EntityType entityType, Level worldIn) {
        super(entityType, worldIn);
        entityData.set(timeRate, 1);
    }

    public TimeAcceleratorEntity(Level worldIn, BlockPos pos) {
        this(CommonRegistration.ACCELERATOR_ENTITY.get(), worldIn);
        this.pos = pos;
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(timeRate, 1);
        builder.define(timeRemaining, 0);
    }

    @Override
    public void tick() {
        super.tick();
        Level level = level();

        if (pos == null) {
            if (!level.isClientSide) {
                this.remove(RemovalReason.KILLED);
            }
            return;
        }

        BlockState blockState = level.getBlockState(pos);
        ServerLevel serverWorld = level.getServer().getLevel(level.dimension());
        BlockEntity targetTE = level.getBlockEntity(pos);

        for (int i = 0; i < getTimeRate(); i++) {
            if (targetTE != null) {
                // if is TileEntity (furnace, brewing stand, ...)
                BlockEntityTicker<BlockEntity> ticker = targetTE.getBlockState().getTicker(level, (BlockEntityType<BlockEntity>) targetTE.getType());
                if (ticker != null) {
                    ticker.tick(level, pos, targetTE.getBlockState(), targetTE);
                }
            } else if (serverWorld != null && blockState.isRandomlyTicking()) {
                // if is random ticket block (grass block, sugar cane, wheat or sapling, ...)
                if (level.random.nextInt(1365) == 0) {
                    blockState.randomTick(serverWorld, pos, level.random);
                }
            } else {
                // block entity broken
                this.remove(RemovalReason.KILLED);
                break;
            }
        }

        setRemainingTime(getRemainingTime() - 1);
        if (getRemainingTime() <= 0 && !level.isClientSide) {
            this.remove(RemovalReason.KILLED);
        }
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
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

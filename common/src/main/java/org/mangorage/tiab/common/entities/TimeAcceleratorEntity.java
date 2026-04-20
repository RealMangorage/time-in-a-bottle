package org.mangorage.tiab.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;
import org.mangorage.tiab.common.core.ticking.BlockTickingTarget;
import org.mangorage.tiab.common.core.ticking.EntityTickingTarget;

import java.util.UUID;

public final class TimeAcceleratorEntity extends Entity implements ITimeAcceleratorEntity {
    private static final EntityDataAccessor<Integer> timeRate = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> timeRemaining = SynchedEntityData.defineId(TimeAcceleratorEntity.class, EntityDataSerializers.INT);

    private BlockPos targetedBlockPos;
    private UUID targetedEntity;

    public TimeAcceleratorEntity(Level worldIn) {
        super(TiabMod.COMMON_API.get().getRegistration().getAcceleratorEntityType(), worldIn);
        entityData.set(timeRate, 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(timeRate, 1);
        builder.define(timeRemaining, 0);
    }

    @Override
    public void tick() {
        if (level().isClientSide()) return;
        ServerLevel level = (ServerLevel) level();

        if (targetedBlockPos == null && (targetedEntity == null || level.getEntity(targetedEntity) == null)) {
            remove(RemovalReason.KILLED);
            targetedEntity = null;
            targetedBlockPos = null;
            return;
        }

        boolean isValid = true;

        if (targetedEntity != null) {
            // Tick Entity
            isValid = EntityTickingTarget.tick(level, this);
        } else {
            // Tick Block!
            isValid = BlockTickingTarget.tick(level, targetedBlockPos, this);
        }

        if (!isValid) {
            remove(RemovalReason.KILLED);
            targetedEntity = null;
            targetedBlockPos = null;
            return;
        }

        setRemainingTime(getRemainingTime() - 1);

        if (getRemainingTime() <= 0) {
            remove(RemovalReason.KILLED);
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
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        entityData.set(timeRate, valueInput.getIntOr(CommonConstants.NBTKeys.ENTITY_TIME_RATE, 1));
        setRemainingTime(valueInput.getIntOr(CommonConstants.NBTKeys.ENTITY_REMAINING_TIME, 10));
        setTargetedBlockPos(valueInput.read(CommonConstants.NBTKeys.ENTITY_POS, BlockPos.CODEC).orElse(null));
        setTargetedEntityUUID(valueInput.read(CommonConstants.NBTKeys.ENTITY_TARGETED_ENTITY, UUIDUtil.CODEC).orElse(null));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putInt(CommonConstants.NBTKeys.ENTITY_TIME_RATE, getTimeRate());
        valueOutput.putInt(CommonConstants.NBTKeys.ENTITY_REMAINING_TIME, getRemainingTime());
        if (targetedBlockPos != null) {
            valueOutput.store(CommonConstants.NBTKeys.ENTITY_POS, BlockPos.CODEC, this.targetedBlockPos);
        }
        if (targetedEntity != null) {
            valueOutput.store(CommonConstants.NBTKeys.ENTITY_TARGETED_ENTITY, UUIDUtil.CODEC, this.targetedEntity);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }

    @Override
    public void setTargetedBlockPos(BlockPos blockPos) {
        if (blockPos == null) {
            this.targetedBlockPos = null;
            return;
        }
        this.targetedBlockPos = blockPos.immutable();
        this.setPos(this.targetedBlockPos.getX() + 0.5, this.targetedBlockPos.getY(), this.targetedBlockPos.getZ() + 0.5);
    }

    @Override
    public void setTargetedEntityUUID(UUID uuid) {
        if (uuid == null) {
            this.uuid = null;
            return;
        }
        this.targetedEntity = uuid;
    }

    @Override
    public Entity asEntity() {
        return this;
    }

    @Override
    public BlockPos getTargetedBlockPos() {
        return targetedBlockPos;
    }

    @Override
    public UUID getTargetedEntityUUID() {
        return targetedEntity;
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
    public void setRemainingTime(int remainingTime) {
        entityData.set(timeRemaining, remainingTime);
    }
}

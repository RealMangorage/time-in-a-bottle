package org.mangorage.tiab.common.core.ticking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;

import java.util.UUID;

public final class EntityTickingTarget {
    public static boolean tick(ServerLevel serverLevel, ITimeAcceleratorEntity timeEntity) {
        UUID uuid = timeEntity.getTargetedEntityUUID();

        if (uuid == null) {
            return false;
        }

        Entity target = serverLevel.getEntity(uuid);

        // Entity gone? Yeah, kill the accelerator instead of pretending life is okay.
        if (target == null || !target.isAlive()) {
            timeEntity.asEntity().remove(Entity.RemovalReason.KILLED);
            timeEntity.setRemainingTime(0);
            timeEntity.setTimeRate(0);
            return false;
        }

        timeEntity.asEntity().setPos(target.getX(), target.getY(), target.getZ());

        // Don’t tick yourself like an idiot
        if (target == timeEntity.asEntity()) {
            return false;
        }

        int rate = timeEntity.getTimeRate();

        for (int i = 0; i < rate; i++) {
            // This is the core: force extra ticks
            target.tick();

            // If it dies mid-acceleration, stop immediately
            if (!target.isAlive()) {
                return false;
            }
        }

        return true;
    }
}

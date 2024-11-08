package org.mangorage.tiab.common.client.renderer;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;

public class AcceleratorEntityRenderState extends EntityRenderState {
    private TimeAcceleratorEntity entity;

    void set(TimeAcceleratorEntity entity) {
        this.entity = entity;
    }

    TimeAcceleratorEntity get() {
        return entity;
    }
}

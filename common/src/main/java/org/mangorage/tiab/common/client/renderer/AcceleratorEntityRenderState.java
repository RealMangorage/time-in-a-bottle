package org.mangorage.tiab.common.client.renderer;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class AcceleratorEntityRenderState extends EntityRenderState {
    private int timeRate = 0;
    private int timeRemaining = 0;

    public void setTimeRate(int timeRate) {
        this.timeRate = timeRate;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public int getTimeRate() {
        return timeRate;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }
}

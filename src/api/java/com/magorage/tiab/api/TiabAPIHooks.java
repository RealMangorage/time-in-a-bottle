package com.magorage.tiab.api;

import com.magorage.tiab.api.events.ITimeBottleTickEvent;
import com.magorage.tiab.api.events.ITimeBottleUseEvent;
import com.magorage.tiab.api.events.ITimeCommandEvent;

public class TiabAPIHooks {
    public static Builder create() {
        return new Builder();
    }

    private final ITimeBottleTickEvent tickEvent;
    private final ITimeBottleUseEvent useEvent;
    private final ITimeCommandEvent commandEvent;

    private TiabAPIHooks(ITimeBottleTickEvent tickEvent, ITimeBottleUseEvent useEvent, ITimeCommandEvent commandEvent) {
        this.tickEvent = tickEvent;
        this.useEvent = useEvent;
        this.commandEvent = commandEvent;
    }

    public ITimeBottleTickEvent getTickEvent() {
        return tickEvent;
    }

    public ITimeBottleUseEvent getUseEvent() {
        return useEvent;
    }

    public ITimeCommandEvent getCommandEvent() {
        return commandEvent;
    }

    public static class Builder {
        private ITimeBottleTickEvent tickEvent;
        private ITimeBottleUseEvent useEvent;
        private ITimeCommandEvent commandEvent;

        public TiabAPIHooks.Builder setCommandEvent(ITimeCommandEvent commandEvent) {
            this.commandEvent = commandEvent;
            return this;
        }

        public TiabAPIHooks.Builder setTickEvent(ITimeBottleTickEvent tickEvent) {
            this.tickEvent = tickEvent;
            return this;
        }

        public TiabAPIHooks.Builder setUseEvent(ITimeBottleUseEvent useEvent) {
            this.useEvent = useEvent;
            return this;
        }

        public TiabAPIHooks build() {
            return new TiabAPIHooks(tickEvent, useEvent, commandEvent);
        }
    }
}

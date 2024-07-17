package org.mangorage.tiab.api.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;


public final class APIHolder<T> implements Supplier<T> {
    private static Logger LOGGER = LogManager.getLogger();

    private final String ID;
    private T value = null;

    public APIHolder(String id) {
        this.ID = id;
    }

    public void setValue(T value) {
        if (this.value != null)
            return;
        LOGGER.info("Set API for %s!".formatted(ID));
        this.value = value;
    }

    public T get() {
        return value;
    }
}

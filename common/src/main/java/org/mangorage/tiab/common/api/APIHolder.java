package org.mangorage.tiab.common.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public final class APIHolder<T> {
    private static Logger LOGGER = LogManager.getLogger();

    private final String ID;
    private T value = null;

    public APIHolder(String id) {
        this.ID = id;
    }

    public void setValue(T value) {
        if (this.value != null) return;
        LOGGER.info("Set API Holder for %s!".formatted(ID));
        this.value = value;
    }

    public Optional<T> get() {
        return Optional.of(value);
    }

    public T getDirect() {
        return value;
    }
}

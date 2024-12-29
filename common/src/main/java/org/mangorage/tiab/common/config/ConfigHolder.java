package org.mangorage.tiab.common.config;

import org.mangorage.tiab.common.api.ITiabConfig;

import java.util.function.Supplier;

public final class ConfigHolder implements Supplier<ITiabConfig> {
    public static ConfigHolder create() {
        return new ConfigHolder();
    }

    private final ITiabConfig defaultConfig = new ITiabConfig() {};

    private ITiabConfig config;

    private ConfigHolder() {}

    public void setConfig(ITiabConfig config) {
        if (this.config == null)
            this.config = config;
    }

    public ITiabConfig get() {
        return config == null ? defaultConfig : config;
    }
}

package org.mangorage.tiab.neoforge;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.mangorage.tiab.common.config.ITiabConfig;

public class NeoForgeTiabConfig implements ITiabConfig {
    private final ModConfigSpec.ConfigValue<Integer> MAX_STORED_TIME;
    private final ModConfigSpec.ConfigValue<Integer> TICKS_CONST;
    private final ModConfigSpec.ConfigValue<Integer> EACH_USE_DURATION;
    private final ModConfigSpec.ConfigValue<Integer> MAX_RATE_MULTI;

    public NeoForgeTiabConfig(ModConfigSpec.Builder builder) {
        this.MAX_STORED_TIME = builder
                .comment("Max Stored Time Possible in ticks")
                .define("max_stored_time", ITiabConfig.super.MAX_STORED_TIME());

        this.TICKS_CONST = builder
                .comment("Ticks constant in ticks")
                .define("ticks_const", ITiabConfig.super.TICKS_CONST());

        this.EACH_USE_DURATION = builder
                .comment("Each use Duration in seconds")
                .define("each_use_duration", ITiabConfig.super.EACH_USE_DURATION());

        this.MAX_RATE_MULTI = builder
                .comment("Define maximum time the items can be used continuously. Corresponding to maximum times faster: Eg. 2^8=256")
                .define("max_rate_multi", ITiabConfig.super.MAX_RATE_MULTI());
    }

    @Override
    public int MAX_STORED_TIME() {
        return MAX_STORED_TIME.get();
    }

    @Override
    public int TICKS_CONST() {
        return TICKS_CONST.get();
    }

    @Override
    public int EACH_USE_DURATION() {
        return EACH_USE_DURATION.get();
    }

    @Override
    public int MAX_RATE_MULTI() {
        return MAX_RATE_MULTI.get();
    }
}
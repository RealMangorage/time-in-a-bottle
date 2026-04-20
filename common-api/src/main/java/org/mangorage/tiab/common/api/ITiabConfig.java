package org.mangorage.tiab.common.api;

import org.mangorage.tiab.common.api.annotations.API;

@API(since = "7.0.0")
public interface ITiabConfig {

    // Max Stored Time Possible in ticks
    @API(since = "7.0.0")
    default int MAX_STORED_TIME() {
        return 622080000;
    }

    // Ticks constant in ticks
    @API(since = "7.0.0")
    default int TICKS_CONST() {
        return 20;
    }

    // Each use Duration in seconds
    @API(since = "7.0.0")
    default int EACH_USE_DURATION() {
        return 30;
    }

    // Define maximum time the items can be used continuously. Corresponding to maximum times faster: Eg. 2^8=256
    @API(since = "7.0.0")
    default int MAX_RATE_MULTI() {
        return 8;
    }
}

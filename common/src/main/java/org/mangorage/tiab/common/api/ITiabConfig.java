package org.mangorage.tiab.common.api;

public interface ITiabConfig {

    // Max Stored Time Possible in ticks
    default int MAX_STORED_TIME() {
        return 622080000;
    }

    // Ticks constant in ticks
    default int TICKS_CONST() {
        return 20;
    }

    // Each use Duration in seconds
    default int EACH_USE_DURATION() {
        return 30;
    }

    // Define maximum time the items can be used continuously. Corresponding to maximum times faster: Eg. 2^8=256
    default int MAX_RATE_MULTI() {
        return 8;
    }
}

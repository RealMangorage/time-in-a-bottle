package org.mangorage.tiab.common;

public class CommonConstants {
    public static final String MODID = "tiab";

    // Max Stored Time Possible in ticks
    @Deprecated
    public static final int MAX_STORED_TIME = 622080000;

    // Ticks constant in ticks
    @Deprecated
    public static final int TICKS_CONST = 20;

    // Each use Duration in seconds
    @Deprecated
    public static final int EACH_USE_DURATION = 30;

    // Define maximum time the items can be used continuously. Corresponding to maximum times faster: Eg. 2^8=256
    @Deprecated
    public static final int MAX_RATE_MULTI = 8;


    public static class NBTKeys {
        // for time accelerator entity
        public static final String ENTITY_TIME_RATE = "timeRate";
        public static final String ENTITY_REMAINING_TIME = "remainingTime";
        public static final String ENTITY_POS = "position";
    }
}

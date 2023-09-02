package com.haoict.tiab.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;


@EventBusSubscriber
public class TiabConfig {
    public static final Builder COMMON_BUILDER = new Builder();

    public static final Common COMMON = new Common();

    public static final class Common {
        public final IntValue maxTimeRatePower;
        public final IntValue eachUseDuration;
        public final IntValue maxStoredTime;
        public final IntValue averageUpdateRandomTick;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> MODS_API;

        private Common() {
            COMMON_BUILDER.push("Time In A Bottle");

            maxTimeRatePower = COMMON_BUILDER.comment("Define maximum time the items can be used continuously. Corresponding to maximum times faster: Eg. 2^8=256").defineInRange("Max Time Rate Power", 8, 1, 12);

            eachUseDuration = COMMON_BUILDER.comment("Define duration for each use - in second").defineInRange("Each Use Duration", 30, 1, 60);

            averageUpdateRandomTick = COMMON_BUILDER.comment("Define Average Update Random Tick on block in chunk (eg: sapling growth). On average, blocks are updated every 68.27 seconds (1365.33 game ticks)... https://minecraft.gamepedia.com/Tick#Random_tick").defineInRange("Average Update Random Tick", 1365, 600, 2100);

            maxStoredTime = COMMON_BUILDER.comment("Define max time the items can store - in tick (1 second = 20 ticks)").defineInRange("Max Stored Time", 622080000, 30 * 20, 622080000);

            MODS_API = COMMON_BUILDER.comment("Define which mods dont have access to the API. Can add Tiab aswell").defineListAllowEmpty("Blacklisted API Mods", new ArrayList<>(), (a) -> true);
            COMMON_BUILDER.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_CONFIG = COMMON_BUILDER.build();
}
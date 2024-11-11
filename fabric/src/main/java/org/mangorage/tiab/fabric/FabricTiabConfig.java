package org.mangorage.tiab.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.mangorage.tiab.common.api.ITiabConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public record FabricTiabConfig(int MAX_STORED_TIME, int TICKS_CONST, int EACH_USE_DURATION, int MAX_RATE_MULTI) implements ITiabConfig {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker TIAB_MOD = MarkerManager.getMarker("TIAB_MOD");

    private static void write(Path path, String data) {
        try {
            // Write data to the file, creating it if it doesn't exist and overwriting if it does
            Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            LOGGER.info(TIAB_MOD, "Data written to file successfully to " + path);
        } catch (IOException e) {
            LOGGER.warn(TIAB_MOD, "An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static String read(Path path) {
        try {
            // Read all lines from the file and join them into a single string
            return Files.readString(path);
        } catch (IOException e) {
            LOGGER.warn(TIAB_MOD, "An error occurred while reading the file: " + e.getMessage());
            return null;
        }
    }

    public static ITiabConfig get() {
        Path cfgPath = FabricLoader.getInstance().getConfigDir().toAbsolutePath();
        Path cfgFile = cfgPath.resolve("tiab-server.json").toAbsolutePath();

        if (!Files.exists(cfgFile)) {
            var cfg = new FabricTiabConfig(
                    622080000,
                    20,
                    30,
                    8
            );
            var data = GSON.toJson(cfg);
            write(cfgFile, data);
            return cfg;
        }

        var cfg = GSON.fromJson(read(cfgFile), FabricTiabConfig.class);
        if (cfg != null)
            return cfg;

        try {
            Files.deleteIfExists(cfgFile);
            LOGGER.warn("Detected Malformed Config, using default config.");
        } catch (IOException e) {
            LOGGER.info(TIAB_MOD, "Failed to delete malformed config");
        }

        return new ITiabConfig() {};
    }

}

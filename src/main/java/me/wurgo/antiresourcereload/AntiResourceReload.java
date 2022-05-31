package me.wurgo.antiresourcereload;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AntiResourceReload implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(FabricLoader.getInstance().getModContainer("antiresourcereload").get().getMetadata().getName());

    public static void log(String message) {
        LOGGER.info("[AntiResourceReload] " + message);
    }

    @Override
    public void onInitialize() {
        log("Initializing.");
    }
}

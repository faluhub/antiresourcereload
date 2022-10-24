package me.wurgo.antiresourcereload;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Unit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class AntiResourceReload implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(FabricLoader.getInstance().getModContainer("antiresourcereload").get().getMetadata().getName());

    public static CompletableFuture<Unit> managerProvider;

    public static void log(String message) {
        LOGGER.info("[" + LOGGER.getName() + "] " + message);
    }

    @Override
    public void onInitialize() {
        log("Initializing.");
    }
}

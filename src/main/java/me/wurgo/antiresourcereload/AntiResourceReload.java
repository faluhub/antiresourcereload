package me.wurgo.antiresourcereload;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ServerResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class AntiResourceReload {
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("antiresourcereload").get();
    public static final String LOGGER_NAME = MOD_CONTAINER.getMetadata().getName();
    public static Logger LOGGER = LogManager.getLogger(LOGGER_NAME);

    public static CompletableFuture<ServerResourceManager> manager;

    public static void log(String message) {
        LOGGER.info(message);
    }
}

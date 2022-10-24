package me.wurgo.antiresourcereload;

import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AntiResourceReload implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(FabricLoader.getInstance().getModContainer("antiresourcereload").get().getMetadata().getName());

    public static void log(String message) {
        LOGGER.info("[AntiResourceReload] " + message);
    }

    public static CompletableFuture<ServerResourceManager> cache;
    public static Map<Identifier, JsonElement> recipes;
    public static boolean hasSeenRecipes;

    @Override
    public void onInitialize() {
        log("Initializing.");
    }
}

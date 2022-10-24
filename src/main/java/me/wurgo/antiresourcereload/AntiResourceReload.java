package me.wurgo.antiresourcereload;

import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.achievement.class_3348;
import net.minecraft.class_4488;
import net.minecraft.loot.class_2787;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.server.function.FunctionTickable;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class AntiResourceReload implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(FabricLoader.getInstance().getModContainer("antiresourcereload").get().getMetadata().getName());

    public static void log(String message) {
        LOGGER.info("[AntiResourceReload] " + message);
    }

    public static ReloadableResourceManager dataManager;
    public static RecipeDispatcher recipeManager;
    public static class_4488 tagManager;
    public static class_2787 lootManager;
    public static class_3348 advancementManager;
    public static FunctionTickable commandFunctionManager;

    public static Map<Identifier, JsonElement> recipes;
    public static boolean hasSeenRecipes;

    @Override
    public void onInitialize() {
        log("Initializing.");
    }

}

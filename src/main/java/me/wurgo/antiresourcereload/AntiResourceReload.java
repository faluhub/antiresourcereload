package me.wurgo.antiresourcereload;

import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.tag.RegistryTagManager;
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
    public static RecipeManager recipeManager;
    public static RegistryTagManager tagManager;
    public static LootConditionManager predicateManager;
    public static LootManager lootManager;
    public static ServerAdvancementLoader advancementLoader;
    public static CommandFunctionManager commandFunctionManager;

    public static Map<Identifier, JsonElement> recipes;
    public static boolean hasSeenRecipes;

    @Override
    public void onInitialize() {
        log("Initializing.");
    }

}

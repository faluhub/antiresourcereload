package me.wurgo.antiresourcereload;

import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.tag.RegistryTagManager;

public class AntiResourceReload {

    public static ReloadableResourceManagerImpl dataManager;
    //public static ResourcePackManager<ResourcePackProfile> dataPackManager;
    public static RecipeManager recipeManager;
    public static RegistryTagManager tagManager;
    public static LootConditionManager predicateManager;
    public static LootManager lootManager;
    public static ServerAdvancementLoader advancementLoader;
    public static CommandFunctionManager commandFunctionManager;

}

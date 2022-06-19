package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.loot.LootManager;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.tag.RegistryTagManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow @Final private static Logger LOGGER;
    
    private static boolean register = true;

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/resource/ReloadableResourceManagerImpl"))
    private ReloadableResourceManagerImpl skipReloadingDataManager(ResourceType type, Thread mainThread) {
        if (AntiResourceReload.dataManager != null) {
            LOGGER.info("Using cached server resources.");
            register = false;
            return AntiResourceReload.dataManager;
        } else {
            LOGGER.info("Cached resources unavailable, reloading.");
            register = true;
            return (AntiResourceReload.dataManager = new ReloadableResourceManagerImpl(type, mainThread));
        }
    }

//    Not caching DataPackManager because it can cause issues when using/switching Datapacks

//    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/resource/ResourcePackManager"))
//    private ResourcePackManager skipReloadingDataPackManager(ResourcePackProfile.Factory factory) {
//        return AntiResourceReload.dataPackManager == null ? (AntiResourceReload.dataPackManager = new ResourcePackManager(factory)) : AntiResourceReload.dataPackManager;
//    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/recipe/RecipeManager"))
    private RecipeManager skipReloadingRecipeManager() {
        return AntiResourceReload.recipeManager == null ? (AntiResourceReload.recipeManager = new RecipeManager()) : AntiResourceReload.recipeManager;
    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/tag/RegistryTagManager"))
    private RegistryTagManager skipReloadingTagManager() {
        return AntiResourceReload.tagManager == null ? (AntiResourceReload.tagManager = new RegistryTagManager()) : AntiResourceReload.tagManager;
    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/loot/LootManager"))
    private LootManager skipReloadingLootManager() {
        return AntiResourceReload.lootManager == null ? (AntiResourceReload.lootManager = new LootManager()) : AntiResourceReload.lootManager;
    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/server/ServerAdvancementLoader"))
    private ServerAdvancementLoader skipReloadingAdvancementManager() {
        return AntiResourceReload.advancementLoader == null ? (AntiResourceReload.advancementLoader = new ServerAdvancementLoader()) : AntiResourceReload.advancementLoader;
    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/server/function/CommandFunctionManager"))
    private CommandFunctionManager skipReloadingCommandFunctionManager(MinecraftServer server) {
        return AntiResourceReload.commandFunctionManager == null ? (AntiResourceReload.commandFunctionManager = new CommandFunctionManager(server)) : AntiResourceReload.commandFunctionManager;
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;registerListener(Lnet/minecraft/resource/ResourceReloadListener;)V"))
    private void register(ReloadableResourceManager instance, ResourceReloadListener resourceReloadListener){
        if (register) {
            instance.registerListener(resourceReloadListener);
        }
    }
}

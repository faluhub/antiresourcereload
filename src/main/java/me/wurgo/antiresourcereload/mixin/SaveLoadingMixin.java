package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.SaveLoading;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Mixin(SaveLoading.class)
public class SaveLoadingMixin {
    @Redirect(
            method = "load",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/DataPackContents;reload(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/registry/DynamicRegistryManager$Immutable;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private static CompletableFuture<DataPackContents> antiresourcereload_cachedReload(ResourceManager manager, DynamicRegistryManager.Immutable dynamicRegistryManager, CommandManager.RegistrationEnvironment commandEnvironment, int functionPermissionLevel, Executor prepareExecutor, Executor applyExecutor, SaveLoading.ServerConfig serverConfig) throws ExecutionException, InterruptedException {
        int dataPacks = serverConfig.dataPacks().settings().getEnabled().size() + serverConfig.dataPacks().settings().getDisabled().size();
        if (dataPacks != 1) { AntiResourceReload.log("Using data-packs, reloading."); }
        else if (AntiResourceReload.cache == null) { AntiResourceReload.log("Cached resources unavailable, reloading & caching."); }
        else {
            AntiResourceReload.log("Using cached server resources.");
            if (AntiResourceReload.hasSeenRecipes) {
                ((RecipeManagerAccess) AntiResourceReload.cache.get().getRecipeManager()).invokeApply(AntiResourceReload.recipes, null, null);
            }
            AntiResourceReload.hasSeenRecipes = false;
            return AntiResourceReload.cache;
        }

        CompletableFuture<DataPackContents> reloaded = DataPackContents.reload(manager, dynamicRegistryManager, commandEnvironment, functionPermissionLevel, prepareExecutor, applyExecutor);

        if (dataPacks == 1) { AntiResourceReload.cache = reloaded; }

        return reloaded;
    }
}

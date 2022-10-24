package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.resource.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Redirect(
            method = "reloadDataPacks",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;beginReload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/List;Ljava/util/concurrent/CompletableFuture;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private CompletableFuture<Unit> antiresourcereload_cachedReload(ReloadableResourceManager instance, Executor executor, Executor executor2, List<ResourcePack> dataPacks, CompletableFuture<Unit> unitCompletableFuture) {
        if (dataPacks.size() != 1) { AntiResourceReload.log("Using data-packs, reloading."); }
        else if (AntiResourceReload.managerProvider == null) { AntiResourceReload.log("Cached resources unavailable, reloading & caching."); }
        else {
            AntiResourceReload.log("Using cached server resources.");
            return AntiResourceReload.managerProvider;
        }

        CompletableFuture<Unit> reloaded = instance.beginReload(executor, executor2, dataPacks, unitCompletableFuture);

        if (dataPacks.size() == 1) { AntiResourceReload.managerProvider = reloaded; }

        return reloaded;
    }
}

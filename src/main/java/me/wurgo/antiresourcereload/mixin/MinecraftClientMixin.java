package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Redirect(method = "method_29604", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ServerResourceManager;reload(Ljava/util/List;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<ServerResourceManager> removeReload(List<ResourcePack> list, CommandManager.RegistrationEnvironment registrationEnvironment, int i, Executor executor, Executor executor2) {
        CompletableFuture<ServerResourceManager> manager = AntiResourceReload.manager;

        if (manager != null) {
            AntiResourceReload.log("Using cached server resources.");
            return manager;
        } else {
            CompletableFuture<ServerResourceManager> future = ServerResourceManager.reload(list, registrationEnvironment, i, executor, executor2);
            AntiResourceReload.manager = future;

            return future;
        }
    }

    @Redirect(method = "method_29604", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;loadDataPacks(Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/resource/DataPackSettings;Z)Lnet/minecraft/resource/DataPackSettings;"))
    private DataPackSettings removeDataPackReload(ResourcePackManager<ResourcePackProfile> resourcePackManager, DataPackSettings dataPackSettings, boolean safeMode) {
        DataPackSettings dataPacks = AntiResourceReload.dataPacks;

        if (dataPacks != null) {
            AntiResourceReload.log("Using cached data packs.");
            return dataPacks;
        } else {
            DataPackSettings loaded = MinecraftServer.loadDataPacks(resourcePackManager, dataPackSettings, false);
            AntiResourceReload.dataPacks = loaded;

            return loaded;
        }
    }
}

package me.wurgo.antiresourcereload.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.*;
import net.minecraft.server.command.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    private CompletableFuture<ServerResourceManager> managerProvider;
    private final Logger LOGGER = LogManager.getLogger(FabricLoader.getInstance().getModContainer("antiresourcereload").get().getMetadata().getName());

    @Redirect(method = "method_29604", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ServerResourceManager;reload(Ljava/util/List;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<ServerResourceManager> cachedReload(List<ResourcePack> dataPacks, CommandManager.RegistrationEnvironment registrationEnvironment, int i, Executor executor, Executor executor2) {
        if (this.managerProvider == null) { LOGGER.info("Cached resources unavailable, reloading."); }
        else if (dataPacks.size() != 1) { LOGGER.info("Using data-packs, reloading."); }
        else {
            LOGGER.info("Using cached server resources.");
            return this.managerProvider;
        }

        this.managerProvider = ServerResourceManager.reload(dataPacks, registrationEnvironment, i, executor, executor2);
        return this.managerProvider;
    }
}

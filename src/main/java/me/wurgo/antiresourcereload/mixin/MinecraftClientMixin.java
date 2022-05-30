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

    private static CompletableFuture<ServerResourceManager> managerProvider;

    private static final Logger LOGGER = LogManager.getLogger(FabricLoader.getInstance().getModContainer("antiresourcereload").get().getMetadata().getName());
    private static void log(String message) {
        LOGGER.info(message);
    }

    @Redirect(method = "method_29604", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ServerResourceManager;reload(Ljava/util/List;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<ServerResourceManager> cachedReload(List<ResourcePack> dataPacks, CommandManager.RegistrationEnvironment registrationEnvironment, int i, Executor executor, Executor executor2) {

        if(managerProvider == null){
            log("loading server resources.");
        }
        else if(dataPacks.size() != 1){
            log("reloading server resources.");
        }
        else {
            log("using cached server resources.");
            return managerProvider;
        }

        managerProvider = ServerResourceManager.reload(dataPacks, registrationEnvironment, i, executor, executor2);

        return managerProvider;
    }

}

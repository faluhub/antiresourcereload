package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.*;
import net.minecraft.server.command.CommandManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Redirect(method = "method_29604", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ServerResourceManager;reload(Ljava/util/List;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<ServerResourceManager> removeReload(List<ResourcePack> dataPacks, CommandManager.RegistrationEnvironment registrationEnvironment, int i, Executor executor, Executor executor2) {
        CompletableFuture<ServerResourceManager> manager = AntiResourceReload.manager;

        if (manager != null && dataPacks.size() == 1) {
            AntiResourceReload.log("Using cached server resources.");
            return manager;
        }
        else if (dataPacks.size() > 1) { AntiResourceReload.log("Using data-packs, reloading."); }
        else { AntiResourceReload.log("Cached resources unavailable, reloading."); }

        CompletableFuture<ServerResourceManager> future = ServerResourceManager.reload(dataPacks, registrationEnvironment, i, executor, executor2);
        if (dataPacks.size() == 1) {
            AntiResourceReload.manager = future;
        }

        return future;
    }
}

package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.ExecutionException;

@Mixin(MinecraftServer.ResourceManagerHolder.class)
public class ResourceManagerHolderMixin {
    @Redirect(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/LifecycledResourceManager;close()V"))
    private void antiresourcereload_keepOpened(LifecycledResourceManager instance) throws ExecutionException, InterruptedException {
        //noinspection ConstantConditions - I think mixin is confusing for intellij here
        if (AntiResourceReload.cache == null || (Object) this != AntiResourceReload.cache.get()) {
            instance.close();
        }
    }
}

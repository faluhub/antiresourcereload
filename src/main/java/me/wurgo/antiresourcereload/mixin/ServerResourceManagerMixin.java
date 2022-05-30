package me.wurgo.antiresourcereload.mixin;

import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerResourceManager.class)
public abstract class ServerResourceManagerMixin {
    @Redirect(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;close()V"))
    private void keepOpened(ReloadableResourceManager instance) {}
}

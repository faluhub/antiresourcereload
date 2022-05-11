package me.wurgo.antiresourcereload.mixin;

import net.minecraft.resource.ServerResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourceManager.class)
public class ServerResourceManagerMixin {
    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    public void keepOpened(CallbackInfo ci){
        ci.cancel();
    }
}

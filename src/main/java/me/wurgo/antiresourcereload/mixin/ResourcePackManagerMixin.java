package me.wurgo.antiresourcereload.mixin;

import net.minecraft.resource.ResourcePackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {
    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    private void keepOpened(CallbackInfo ci) {
        ci.cancel();
    }
}

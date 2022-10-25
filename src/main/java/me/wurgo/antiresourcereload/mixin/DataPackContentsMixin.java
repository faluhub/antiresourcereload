package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.server.DataPackContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ExecutionException;

@Mixin(DataPackContents.class)
public class DataPackContentsMixin {
    private boolean refreshed;

    @Inject(method = "refresh", at = @At("HEAD"), cancellable = true)
    private void antiresourcereload_cancelRefresh(CallbackInfo ci) throws ExecutionException, InterruptedException {
        // noinspection ConstantConditions - I think mixin is confusing for intellij here
        if ((Object) this == AntiResourceReload.cache.get() && refreshed) {
            ci.cancel();
        }
        this.refreshed = true;
    }
}

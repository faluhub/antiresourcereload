package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.class_3282;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_3282.class)
public class RecipeBookWidgetMixin {
    @Inject(method = "method_18793",at = @At("HEAD"))
    public void updateHasSeenRecipes(CallbackInfo ci){
        AntiResourceReload.hasSeenRecipes=true;
    }
}

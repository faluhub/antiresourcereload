package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.achievement.class_3348;
import net.minecraft.class_4488;
import net.minecraft.loot.class_2787;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.function.FunctionTickable;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow protected abstract void method_20314(LevelProperties levelProperties);
    @Mutable @Shadow @Final private ReloadableResourceManager field_21597;
    @Mutable @Shadow @Final private class_4488 field_21602;
    @Mutable @Shadow @Final private RecipeDispatcher field_21601;
    @Mutable @Shadow @Final private class_2787 field_21605;
    @Mutable @Shadow @Final private FunctionTickable field_21607;
    @Mutable @Shadow @Final private class_3348 field_21606;

    @Redirect(
            method = "method_20319",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;method_20314(Lnet/minecraft/world/level/LevelProperties;)V"
            )
    )
    private void antiresourcereload_cachedReload(MinecraftServer instance, LevelProperties levelProperties) {
        if (levelProperties.method_17951().size() + levelProperties.method_17952().size() != 0) {
            AntiResourceReload.log("Using data-packs, reloading.");
            this.method_20314(levelProperties);
            return;
        }
        
        if (AntiResourceReload.dataManager == null) {
            AntiResourceReload.log("Cached resources unavailable, reloading & caching.");
            AntiResourceReload.dataManager = this.field_21597;
            this.method_20314(levelProperties);
            AntiResourceReload.tagManager = this.field_21602;
            AntiResourceReload.recipeManager = this.field_21601;
            AntiResourceReload.lootManager = this.field_21605;
            AntiResourceReload.commandFunctionManager = this.field_21607;
            AntiResourceReload.advancementManager = this.field_21606;
        } else {
            AntiResourceReload.log("Using cached server resources.");
            this.field_21597 = AntiResourceReload.dataManager;
            this.field_21602 = AntiResourceReload.tagManager;
            this.field_21601 = AntiResourceReload.recipeManager;
            this.field_21605 = AntiResourceReload.lootManager;
            this.field_21607 = AntiResourceReload.commandFunctionManager;
            this.field_21606 = AntiResourceReload.advancementManager;
            if (AntiResourceReload.hasSeenRecipes) {
                this.field_21601.reload(this.field_21597);
            }
        }
    }
}

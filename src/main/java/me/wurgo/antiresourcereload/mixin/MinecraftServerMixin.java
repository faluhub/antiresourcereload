package me.wurgo.antiresourcereload.mixin;

import me.wurgo.antiresourcereload.AntiResourceReload;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.tag.RegistryTagManager;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow
    protected abstract void reloadDataPacks(LevelProperties levelProperties);

    @Mutable
    @Shadow @Final
    private ReloadableResourceManager dataManager;

    @Mutable
    @Shadow @Final private RegistryTagManager tagManager;

    @Mutable
    @Shadow @Final private LootConditionManager predicateManager;

    @Mutable
    @Shadow @Final private RecipeManager recipeManager;

    @Mutable
    @Shadow @Final private LootManager lootManager;

    @Mutable
    @Shadow @Final private CommandFunctionManager commandFunctionManager;

    @Mutable
    @Shadow @Final private ServerAdvancementLoader advancementLoader;

    @Redirect(method = "loadWorldDataPacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;reloadDataPacks(Lnet/minecraft/world/level/LevelProperties;)V"))
    private void antiresourcereload_cachedReload(MinecraftServer instance, LevelProperties levelProperties) {
        if (levelProperties.getEnabledDataPacks().size() + levelProperties.getDisabledDataPacks().size() != 0) {
            AntiResourceReload.log("Using data-packs, reloading.");
            this.reloadDataPacks(levelProperties);
            return;
        }
        if (AntiResourceReload.dataManager == null) {
            AntiResourceReload.log("Cached resources unavailable, reloading & caching.");
            AntiResourceReload.dataManager = this.dataManager;
            this.reloadDataPacks(levelProperties);
            AntiResourceReload.tagManager = this.tagManager;
            AntiResourceReload.predicateManager = this.predicateManager;
            AntiResourceReload.recipeManager = this.recipeManager;
            AntiResourceReload.lootManager = this.lootManager;
            AntiResourceReload.commandFunctionManager = this.commandFunctionManager;
            AntiResourceReload.advancementLoader = this.advancementLoader;
        } else {
            AntiResourceReload.log("Using cached server resources.");
            this.dataManager = AntiResourceReload.dataManager;
            this.tagManager = AntiResourceReload.tagManager;
            this.predicateManager = AntiResourceReload.predicateManager;
            this.recipeManager = AntiResourceReload.recipeManager;
            this.lootManager = AntiResourceReload.lootManager;
            this.commandFunctionManager = AntiResourceReload.commandFunctionManager;
            this.advancementLoader = AntiResourceReload.advancementLoader;
            if (AntiResourceReload.hasSeenRecipes) {
                ((RecipeManagerAccess) this.recipeManager).invokeApply(AntiResourceReload.recipes, null, null);
            }
        }
    }
}

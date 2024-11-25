package org.figuramc.figura.mixin;

import net.minecraft.server.MinecraftServer;
import org.figuramc.figura.cosmetics.ServerSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "runServer", at = @At("HEAD"))
    private void runServer(CallbackInfo ci) {
        ServerSupplier.serverInstance = (MinecraftServer) (Object) this;
    }
}
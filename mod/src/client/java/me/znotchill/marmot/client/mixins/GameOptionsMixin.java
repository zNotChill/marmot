package me.znotchill.marmot.client.mixins;

import me.znotchill.marmot.client.Client;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Inject(method = "setPerspective", at = @At("HEAD"), cancellable = true)
    void setPerspective(Perspective perspective, CallbackInfo ci) {
        if (Client.perspectiveLocked) {
            ci.cancel();
        }
    }
}

package me.znotchill.marmot.client.mixins;

import me.znotchill.marmot.client.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    private void update(double timeDelta, CallbackInfo ci) {
        if (Client.cameraLocked) {
            ci.cancel();
        }
    }

    @Inject(method = "onMouseButton(JIII)V", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (Client.mouseButtonsLocked && !Client.emitMouseEvents) {
            if (mc.currentScreen == null) {
                ci.cancel();
            }
        }
    }
}
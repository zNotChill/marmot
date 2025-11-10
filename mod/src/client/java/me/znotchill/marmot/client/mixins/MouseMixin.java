package me.znotchill.marmot.client.mixins;

import me.znotchill.marmot.client.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "updateMouse(D)V", at = @At("HEAD"), cancellable = true)
    void update(double timeDelta, CallbackInfo ci) {
        if (Client.cameraLocked) {
            ci.cancel();
        }
    }

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    void onMouseButton(long window, MouseInput input, int action, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (Client.mouseButtonsLocked && !Client.emitMouseEvents) {
            if (mc.currentScreen == null) {
                ci.cancel();
            }
        }
    }
}
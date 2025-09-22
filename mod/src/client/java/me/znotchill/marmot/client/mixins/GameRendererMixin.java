package me.znotchill.marmot.client.mixins;

import me.znotchill.marmot.client.Client;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> ci) {
        float vanillaFov = ci.getReturnValue();

        if (Client.currentFov < 0f) {
            Client.currentFov = vanillaFov;
            Client.targetFov = Client.customFov > 0f ? Client.customFov : vanillaFov;
        }

        float speed = 0.2f;
        Client.currentFov += (Client.targetFov - Client.currentFov) * speed;

        ci.setReturnValue(Client.currentFov);
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void applyCustomCamera(MatrixStack matrices, float tickProgress, CallbackInfo ci) {
        if (Client.cameraLocked) {
            matrices.multiply(new Quaternionf().rotateXYZ(Client.customPitch, Client.customYaw, 0f));
            ci.cancel();
            return;
        }
        matrices.multiply(new Quaternionf().rotateXYZ(Client.customPitch, Client.customYaw, Client.customRoll));
    }
}

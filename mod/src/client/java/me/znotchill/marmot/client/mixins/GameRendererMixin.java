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
    void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> ci) {
        float vanillaFov = ci.getReturnValue();

        if (Client.targetFov > 0f) {
            if (Client.currentFov < 0f) {
                Client.currentFov = vanillaFov;
            }

            float speed = 0.2f;
            Client.currentFov += (Client.targetFov - Client.currentFov) * speed;

            ci.setReturnValue(Client.currentFov);
        } else {
            Client.currentFov = -1f;
            ci.setReturnValue(vanillaFov);
        }
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"))
    private void applyCustomCamera(MatrixStack matrices, float f, CallbackInfo ci) {
        if (Client.customPitch != 0f || Client.customYaw != 0f || Client.customRoll != 0f) {
            float pitchRad = (float) Math.toRadians(Client.customPitch);
            float yawRad   = (float) Math.toRadians(Client.customYaw);
            float rollRad  = (float) Math.toRadians(Client.customRoll);

            matrices.multiply(new Quaternionf().rotateXYZ(pitchRad, yawRad, rollRad));
        }

        // Y and Z are swapped intentionally
        matrices.translate(
                Client.cameraOffsetX * -1,
                Client.cameraOffsetZ * -1,
                Client.cameraOffsetY * -1
        );
    }
}
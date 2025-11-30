package me.znotchill.marmot.client.mixins;

import me.znotchill.marmot.client.Client;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> ci) {
        // run last so we get the return value from vanilla, zoomify/other zoom mods
        float vanillaFov = ci.getReturnValue();

        // first case - FOV is not locked
        if (!Client.lockFov) {
            // if not animating, fully restore vanilla/zoom mod control
            if (!Client.isInterpolatingFov) {
                Client.currentFov = vanillaFov;
                return;
            }

            // we are currently interpolating, but FOV is not locked
            float animated = animateFov(vanillaFov, tickDelta);

            // when animation finished, stop canceling slider/zoom
            if (!Client.isInterpolatingFov) {
                Client.currentFov = Client.targetFov;
            }

            ci.setReturnValue(animated);
            return;
        }

        // second case - FOV is LOCKED (ignore fov slider & zoom mods)
        if (!Client.animateFov) {
            ci.setReturnValue(Client.targetFov);
            return;
        }

        ci.setReturnValue(animateFov(vanillaFov, tickDelta));
    }

    @Unique
    private float animateFov(float fallbackFov, float tickDelta) {
        if (Client.currentFov < 0f)
            Client.currentFov = fallbackFov;

        if (Client.fovAnimTicks <= 0) {
            Client.currentFov = Client.targetFov;
            Client.isInterpolatingFov = false;
            return Client.currentFov;
        }

        float fractionPerTick = 1f / Client.fovAnimTicks;
        float delta = Client.targetFov - Client.currentFov;

        Client.currentFov += delta * fractionPerTick * tickDelta;

        if (Math.abs(Client.targetFov - Client.currentFov) < 0.01f) {
            Client.currentFov = Client.targetFov;
            Client.isInterpolatingFov = false;
            Client.fovAnimTicks = 0;

        }

        return Client.currentFov;
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
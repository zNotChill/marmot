package me.znotchill.marmot.client.mixins

import me.znotchill.marmot.client.Client
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import org.joml.Quaternionf
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(GameRenderer::class)
class GameRendererMixin {
    @Inject(method = ["getFov"], at = [At("RETURN")], cancellable = true)
    fun onGetFov(
        camera: net.minecraft.client.render.Camera,
        tickDelta: Float,
        changingFov: Boolean,
        ci: CallbackInfoReturnable<Float>
    ) {
        val vanillaFov = ci.returnValue

        if (Client.currentFov < 0f) {
            Client.currentFov = vanillaFov
            Client.targetFov = Client.customFov.takeIf { it > 0f } ?: vanillaFov
        }

        val speed = 0.2f
        Client.currentFov += (Client.targetFov - Client.currentFov) * speed

        ci.returnValue = Client.currentFov
    }

    @Inject(method = ["bobView"], at = [At("HEAD")], cancellable = true)
    fun applyCustomCamera(matrices: MatrixStack, tickProgress: Float, ci: CallbackInfo) {
        if (Client.cameraLocked) {
            matrices.multiply(
                Quaternionf()
                    .rotateXYZ(Client.pitch, Client.yaw, 0f)
            )
            ci.cancel()
        }
        matrices.multiply(
            Quaternionf()
                .rotateXYZ(Client.pitch, Client.yaw, Client.roll)
        )
    }
}
package me.znotchill.marmot.client.mixins

import me.znotchill.marmot.client.Client
import net.minecraft.client.Mouse
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Mouse::class)
class MouseMixin {
    @Inject(method = ["updateMouse"], at = [At("HEAD")], cancellable = true)
    fun update(ci: CallbackInfo) {
        if (Client.cameraLocked) {
            ci.cancel()
        }
    }

    @Inject(method = ["onMouseButton"], at = [At("HEAD")], cancellable = true)
    fun onMouseButton(ci: CallbackInfo) {
        if (Client.mouseButtonsLocked && !Client.emitMouseEvents) {
            ci.cancel()
        }
    }
}
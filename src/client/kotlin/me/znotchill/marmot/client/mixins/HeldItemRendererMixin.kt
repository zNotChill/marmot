package me.znotchill.marmot.client.mixins

import me.znotchill.marmot.client.Client
import net.minecraft.client.render.item.HeldItemRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(HeldItemRenderer::class)
abstract class HeldItemRendererMixin {
    @Shadow private var equipProgressMainHand: Float = 0f
    @Shadow private var lastEquipProgressMainHand: Float = 0f
    @Shadow private var equipProgressOffHand: Float = 0f
    @Shadow private var lastEquipProgressOffHand: Float = 0f

    @Inject(method = ["updateHeldItems"], at = [At("HEAD")], cancellable = true)
    private fun forceEquip(ci: CallbackInfo) {
        if (Client.mouseButtonsLocked) {
            equipProgressMainHand = 1f
            lastEquipProgressMainHand = 1f
            equipProgressOffHand = 1f
            lastEquipProgressOffHand = 1f
            ci.cancel()
        }
    }
}

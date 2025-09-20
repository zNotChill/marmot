package me.znotchill.marmot.client.mixins

import me.znotchill.marmot.client.Client
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.Hand
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientPlayerEntity::class)
abstract class ClientPlayerEntityMixin : AbstractClientPlayerEntity(null, null) {
    @Inject(
        method = ["swingHand(Lnet/minecraft/util/Hand;)V"],
        at = [At("HEAD")],
        cancellable = true
    )
    private fun cancelSwing(hand: Hand, ci: CallbackInfo) {
        if (Client.mouseButtonsLocked) {
            ci.cancel()
        }
    }
}
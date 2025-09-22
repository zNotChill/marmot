package me.znotchill.marmot.client.mixins;

import me.znotchill.marmot.client.Client;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Inject(method = "swingHand", at = @At("HEAD"), cancellable = true)
    private void cancelSwing(Hand hand, CallbackInfo ci) {
        if (Client.mouseButtonsLocked) {
            ci.cancel();
        }
    }
}
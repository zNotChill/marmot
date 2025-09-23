package me.znotchill.marmot.client.mixins;

import me.znotchill.marmot.client.Client;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow private float equipProgressMainHand;
    @Shadow private float lastEquipProgressMainHand;
    @Shadow private float equipProgressOffHand;
    @Shadow private float lastEquipProgressOffHand;

    @Inject(method = "updateHeldItems", at = @At("HEAD"), cancellable = true)
    void forceEquip(CallbackInfo ci) {
        if (Client.mouseButtonsLocked) {
            equipProgressMainHand = 1f;
            lastEquipProgressMainHand = 1f;
            equipProgressOffHand = 1f;
            lastEquipProgressOffHand = 1f;
            ci.cancel();
        }
    }
}

package me.znotchill.marmot.client.mixins

import me.znotchill.marmot.client.Client
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ClientPlayerInteractionManager::class)
abstract class ClientPlayerInteractionManagerMixin {
    @Inject(method = ["attackEntity"], at = [At("HEAD")], cancellable = true)
    private fun cancelAttackEntity(player: PlayerEntity, target: Entity, ci: CallbackInfo) {
        if (Client.mouseButtonsLocked) {
            ci.cancel()
        }
    }

    @Inject(method = ["attackBlock"], at = [At("HEAD")], cancellable = true)
    private fun cancelAttackBlock(pos: BlockPos, direction: Direction, ci: CallbackInfoReturnable<Boolean>) {
        if (Client.mouseButtonsLocked) {
            ci.cancel()
            ci.returnValue = false
        }
    }
}
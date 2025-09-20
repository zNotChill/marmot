package me.znotchill.marmot.client.mixins

import me.znotchill.marmot.client.ChatManager
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.DrawContext
import org.lwjgl.glfw.GLFW
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ChatScreen::class)
abstract class ChatScreenMixin(screen: Screen) : Screen(screen.title) {

    private var fadeTime = 0

    @Inject(method = ["keyPressed"], at = [At("HEAD")], cancellable = true)
    private fun onKeyPressed(
        keyCode: Int,
        scanCode: Int,
        modifiers: Int,
        cir: CallbackInfoReturnable<Boolean>
    ) {
        if (ChatManager.chatEnabled) {
            return
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            cir.returnValue = true
            if (ChatManager.fadeEnabled) {
                fadeTime = 20
            }
        } else {
            cir.returnValue = true
        }
    }

    @Inject(method = ["render"], at = [At("HEAD")])
    private fun onRender(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float, ci: CallbackInfo) {
        if (!ChatManager.chatEnabled && ChatManager.fadeEnabled && fadeTime > 0) {
            val fade = fadeTime / 20f
            val alpha = (fade * 50).toInt() shl 24
            context.fill(
                0, 0,
                this.width, this.height,
                alpha or 0xD8413A
            )
            fadeTime--
        }
    }
}
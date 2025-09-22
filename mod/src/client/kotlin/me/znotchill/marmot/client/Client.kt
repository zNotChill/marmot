package me.znotchill.marmot.client

import net.minecraft.client.MinecraftClient

object Client {
    @JvmField
    var customPitch = 0f
    @JvmField
    var customYaw = 0f
    @JvmField
    var customRoll = 0f
    @JvmField
    var customFov = 0f

    var fov: Float
        get() = customFov
        set(value) {
            this.customFov = value
        }

    @JvmField
    var targetFov: Float = -1f
    @JvmField
    var currentFov: Float = MinecraftClient.getInstance().options.fov.value.toFloat()

    @JvmField
    var cameraLocked: Boolean = false
    @JvmField
    var mouseButtonsLocked: Boolean = false
    @JvmField
    var emitMouseEvents: Boolean = false

    @JvmField
    var isLeftClicking: Boolean = false
    @JvmField
    var isRightClicking: Boolean = false
}
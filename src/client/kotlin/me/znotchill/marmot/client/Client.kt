package me.znotchill.marmot.client

import net.minecraft.client.MinecraftClient

object Client {
    private var customPitch = 0f
    private var customYaw = 0f
    private var customRoll = 0f
    var customFov = 0f

    var pitch: Float
        get() = this.customPitch
        set(value) {
            this.customPitch = value
        }

    var yaw: Float
        get() = this.customYaw
        set(value) {
            this.customYaw = value
        }

    var roll: Float
        get() = this.customRoll
        set(value) {
            this.customRoll = value
        }

    var fov: Float
        get() = customFov
        set(value) {
            this.customFov = value
        }

    var targetFov: Float = -1f
    var currentFov: Float = MinecraftClient.getInstance().options.fov.value.toFloat()

    var cameraLocked: Boolean = false
    var mouseButtonsLocked: Boolean = false
}
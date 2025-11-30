package me.znotchill.marmot.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.Perspective

object Client {
    @JvmField
    var customPitch = 0f
    @JvmField
    var customYaw = 0f
    @JvmField
    var customRoll = 0f

    @JvmField
    var targetFov: Float = -1f
    @JvmField
    var currentFov: Float = MinecraftClient.getInstance().options.fov.value.toFloat()
    @JvmField
    var lockFov: Boolean = false
    @JvmField
    var isInterpolatingFov: Boolean = false
    @JvmField
    var fovAnimTicks: Int = 5
    @JvmField
    var animateFov: Boolean = false

    @JvmField
    var cameraOffsetX  = 0f
    @JvmField
    var cameraOffsetY  = 0f
    @JvmField
    var cameraOffsetZ  = 0f

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

    @JvmField
    var perspectiveLocked: Boolean = false
    var currentPerspective: Perspective
        get() = MinecraftClient.getInstance().options.perspective
        set(value) {
            MinecraftClient.getInstance().options.perspective = value
        }
}
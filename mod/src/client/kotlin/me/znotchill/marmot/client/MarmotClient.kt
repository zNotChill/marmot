package me.znotchill.marmot.client

import me.znotchill.marmot.client.packets.clientbound.handlers.*
import me.znotchill.marmot.client.packets.clientbound.payloads.*
import me.znotchill.marmot.client.packets.serverbound.payloads.ClickUpdatePayload
import me.znotchill.marmot.client.packets.serverbound.payloads.IsMarmotServerPayload
import me.znotchill.marmot.client.ui.UIRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.MinecraftClient

class MarmotClient : ClientModInitializer {
    override fun onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(CameraPayload.ID, CameraPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(CameraLockPayload.ID, CameraLockPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(MousePayload.ID, MousePayload.CODEC)
        PayloadTypeRegistry.playS2C().register(ForceKeybindsPayload.ID, ForceKeybindsPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(IsMarmotClientPayload.ID, IsMarmotClientPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(UIPayload.ID, UIPayload.CODEC)

        PayloadTypeRegistry.playC2S().register(ClickUpdatePayload.ID, ClickUpdatePayload.CODEC)
        PayloadTypeRegistry.playC2S().register(IsMarmotServerPayload.ID, IsMarmotServerPayload.CODEC)

        CameraHandler().register()
        CameraLockHandler().register()
        ForceKeybindsHandler().register()
        MouseHandler().register()
        IsMarmotClientHandler().register()
        UIHandler().register()

        UIRenderer.register()

        ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
            val leftPressed = client.mouse.wasLeftButtonClicked()
            val rightPressed = client.mouse.wasRightButtonClicked()

            if (leftPressed != Client.isLeftClicking || rightPressed != Client.isRightClicking) {
                val payload = ClickUpdatePayload(
                    leftPressed,
                    rightPressed
                )
                ClientPlayNetworking.send(payload)
            }

            Client.isLeftClicking = leftPressed
            Client.isRightClicking = rightPressed
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            val client = MinecraftClient.getInstance()
            Client.pitch = 0f
            Client.yaw = 0f
            Client.roll = 0f
            Client.targetFov = client.options.fov.value.toFloat()
            Client.cameraLocked = false
            Client.isLeftClicking = false
            Client.isRightClicking = false
            Client.emitMouseEvents = true
            Client.cameraLocked = false
            UIRenderer.setWindow(null)

            KeybindManager.restoreAll()
        }
    }
}

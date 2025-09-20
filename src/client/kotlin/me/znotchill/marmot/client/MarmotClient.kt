package me.znotchill.marmot.client

import me.znotchill.marmot.client.packets.clientbound.handlers.CameraHandler
import me.znotchill.marmot.client.packets.clientbound.handlers.CameraLockHandler
import me.znotchill.marmot.client.packets.clientbound.handlers.ChatToggleHandler
import me.znotchill.marmot.client.packets.clientbound.handlers.ForceKeybindsHandler
import me.znotchill.marmot.client.packets.clientbound.handlers.IsMarmotClientHandler
import me.znotchill.marmot.client.packets.clientbound.handlers.MouseHandler
import me.znotchill.marmot.client.packets.clientbound.payloads.CameraLockPayload
import me.znotchill.marmot.client.packets.clientbound.payloads.CameraPayload
import me.znotchill.marmot.client.packets.clientbound.payloads.ForceKeybindsPayload
import me.znotchill.marmot.client.packets.clientbound.payloads.IsMarmotClientPayload
import me.znotchill.marmot.client.packets.clientbound.payloads.MousePayload
import me.znotchill.marmot.client.packets.serverbound.payloads.ClickUpdatePayload
import me.znotchill.marmot.client.packets.serverbound.payloads.IsMarmotServerPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.MinecraftClient
import me.znotchill.marmot.client.packets.clientbound.payloads.ChatTogglePayload

class MarmotClient : ClientModInitializer {
    override fun onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(CameraPayload.ID, CameraPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(CameraLockPayload.ID, CameraLockPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(MousePayload.ID, MousePayload.CODEC)
        PayloadTypeRegistry.playS2C().register(ForceKeybindsPayload.ID, ForceKeybindsPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(IsMarmotClientPayload.ID, IsMarmotClientPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(ChatTogglePayload.ID, ChatTogglePayload.CODEC)

        PayloadTypeRegistry.playC2S().register(ClickUpdatePayload.ID, ClickUpdatePayload.CODEC)
        PayloadTypeRegistry.playC2S().register(IsMarmotServerPayload.ID, IsMarmotServerPayload.CODEC)

        CameraHandler().register()
        CameraLockHandler().register()
        ForceKeybindsHandler().register()
        MouseHandler().register()
        IsMarmotClientHandler().register()
        ChatToggleHandler().register()

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

            ChatManager.chatEnabled = true
            ChatManager.fadeEnabled = true

            KeybindManager.restoreAll()
        }
    }
}

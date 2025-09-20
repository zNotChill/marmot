package me.znotchill.marmot.client

import me.znotchill.marmot.client.payloads.CameraLockPayload
import me.znotchill.marmot.client.payloads.CameraPayload
import me.znotchill.marmot.client.payloads.MouseLockPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.MinecraftClient

class MarmotClient : ClientModInitializer {
    override fun onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(CameraPayload.ID, CameraPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(CameraLockPayload.ID, CameraLockPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(MouseLockPayload.ID, MouseLockPayload.CODEC)

        ClientPlayNetworking.registerGlobalReceiver(CameraPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                Client.pitch = payload.x
                Client.yaw = payload.y
                Client.roll = payload.z
                Client.targetFov = payload.fov
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(MouseLockPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.mouseButtonsLocked = lock
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(CameraLockPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.cameraLocked = lock
            }
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            val client = MinecraftClient.getInstance()
            Client.pitch = 0f
            Client.yaw = 0f
            Client.roll = 0f
            Client.targetFov = client.options.fov.value.toFloat()
            Client.cameraLocked = false
        }
    }
}

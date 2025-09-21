package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.CameraLockPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class CameraLockHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(CameraLockPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.cameraLocked = lock
            }
        }
    }
}
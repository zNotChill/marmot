package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.CameraOffsetPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class CameraOffsetHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(CameraOffsetPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                Client.cameraOffsetX = payload.x
                Client.cameraOffsetY = payload.y
                Client.cameraOffsetZ = payload.z
            }
        }
    }
}
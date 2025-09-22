package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.CameraPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class CameraHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(CameraPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                Client.customPitch = payload.x
                Client.customYaw = payload.y
                Client.customRoll = payload.z
                Client.targetFov = payload.fov
            }
        }
    }
}
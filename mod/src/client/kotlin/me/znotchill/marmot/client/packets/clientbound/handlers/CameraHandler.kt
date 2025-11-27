package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.CameraPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class CameraHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(CameraPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                if (payload.x != -1f)
                    Client.customPitch = payload.x
                if (payload.y != -1f)
                    Client.customYaw = payload.y
                if (payload.z != -1f)
                    Client.customRoll = payload.z
                if (payload.fov != -1f)
                    Client.targetFov = payload.fov
                if (payload.lockFov.toInt() != -1)
                    Client.lockFov = payload.lockFov.toInt() != 0
                if (payload.animateFov.toInt() != -1)
                    Client.animateFov = payload.animateFov.toInt() != 0
            }
        }
    }
}
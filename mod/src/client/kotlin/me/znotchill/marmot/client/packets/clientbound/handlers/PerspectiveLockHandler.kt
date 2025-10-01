package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.PerspectiveLockPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class PerspectiveLockHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(PerspectiveLockPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.perspectiveLocked = lock
            }
        }
    }
}
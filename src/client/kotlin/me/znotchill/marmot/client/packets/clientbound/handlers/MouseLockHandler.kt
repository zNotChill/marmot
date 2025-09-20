package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.MouseLockPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class MouseLockHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(MouseLockPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.mouseButtonsLocked = lock
            }
        }
    }
}
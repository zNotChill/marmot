package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.MousePayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class MouseHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(MousePayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.mouseButtonsLocked = lock

                val emit = payload.emitEvents.toInt() != 0
                Client.emitMouseEvents = emit
            }
        }
    }
}
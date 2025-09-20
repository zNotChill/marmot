package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.packets.clientbound.payloads.IsMarmotClientPayload
import me.znotchill.marmot.client.packets.serverbound.payloads.IsMarmotServerPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class IsMarmotClientHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(IsMarmotClientPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val payload = IsMarmotServerPayload(1)
                ClientPlayNetworking.send(payload)
            }
        }
    }
}
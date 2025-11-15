package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.packets.clientbound.payloads.ClearUIPayload
import me.znotchill.marmot.client.ui.UIRenderer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class ClearUIHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ClearUIPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                UIRenderer.setWindow(null)
            }
        }
    }
}
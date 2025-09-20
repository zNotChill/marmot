package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.packets.clientbound.payloads.ChatTogglePayload
import me.znotchill.marmot.client.ChatManager
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class ChatToggleHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ChatTogglePayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                ChatManager.chatEnabled = payload.chatEnabled
                ChatManager.fadeEnabled = payload.fadeEnabled
            }
        }
    }
}
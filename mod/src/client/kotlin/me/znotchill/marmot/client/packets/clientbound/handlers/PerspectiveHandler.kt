package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.PerspectivePayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.option.Perspective

class PerspectiveHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(PerspectivePayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val perspective = runCatching {
                    Perspective.valueOf(payload.perspective)
                }.getOrNull() ?: return@execute

                Client.currentPerspective = perspective
            }
        }
    }
}
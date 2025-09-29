package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.packets.clientbound.payloads.UIPayload
import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.JsonUtil
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.UIEvent
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class UIHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(UIPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                try {
                    if (!payload.updated) {
                        val window = UIWindow.decode(payload.json)
                        UIRenderer.handleFreshRender(window)
                    } else {
                        val events: List<UIEvent> =
                            JsonUtil.json.decodeFromString(payload.json)
                        UIRenderer.handleUpdateRender(events)
                    }
                } catch (e: Exception) {
                    println("Server returned malformed or invalid JSON: ${payload.json}")
                    e.printStackTrace()
                }
            }
        }
    }
}
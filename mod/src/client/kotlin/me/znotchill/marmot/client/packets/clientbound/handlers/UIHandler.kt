package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.packets.clientbound.payloads.UIPayload
import me.znotchill.marmot.client.ui.UIRenderer
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
                        // This is a fresh render of this UI
                        val window = UIWindow.decode(payload.json)
                        UIRenderer.setWindow(window)
                    } else {
                        val json = kotlinx.serialization.json.Json {
                            ignoreUnknownKeys = true
                            classDiscriminator = "type"
                        }

                        val events: List<UIEvent> =
                            json.decodeFromString(payload.json)

                        events.forEach { event ->
                            UIRenderer.applyEvent(event)
                        }
                    }
                } catch (e: Exception) {
                    println("Server returned malformed or invalid JSON: ${payload.json}")
                    e.printStackTrace()
                }
            }
        }
    }
}
package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.packets.clientbound.payloads.UIPayload
import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.UIWindow
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class UIHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(UIPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                try {
                    val window = UIWindow.decode(payload.json)
                    UIRenderer.setWindow(window)
                } catch (e: Exception) {
                    println("Server returned malformed or invalid JSON: ${payload.json}")
                    e.printStackTrace()
                }
            }
        }
    }
}
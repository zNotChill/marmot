package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.KeybindManager
import me.znotchill.marmot.client.packets.clientbound.payloads.ForceKeybindsPayload
import me.znotchill.marmot.client.ui.screens.ForceKeybindsScreen
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import kotlin.collections.component1
import kotlin.collections.component2

class ForceKeybindsHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ForceKeybindsPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                client.setScreen(
                    ForceKeybindsScreen(
                        payload,
                        onAccept = {
                            onKeybindsAccept(payload, context)
                        },
                        onDeny = {
                            onKeybindsDeny(payload, context)
                        }
                    )
                )
            }
        }
    }

    fun onKeybindsAccept(payload: ForceKeybindsPayload, context: ClientPlayNetworking.Context) {
        val client = context.client()
        payload.binds.forEach { (bindName, forcedKeyTranslationKey) ->
            val keyBinding = client.options.allKeys.firstOrNull { it.translationKey == bindName }
                ?: return@forEach println("Unknown bind name: $bindName")

            val forcedKey = InputUtil.fromTranslationKey(forcedKeyTranslationKey)
                ?: return@forEach println("Unknown key translation: $forcedKeyTranslationKey")

            println("RECEIVED KEYBIND $keyBinding -> $forcedKey")
            KeybindManager.overrideKeybind(keyBinding, forcedKey)
            println("Set ${keyBinding.translationKey} to $forcedKeyTranslationKey")
        }

        KeyBinding.updateKeysByCode()
    }

    fun onKeybindsDeny(payload: ForceKeybindsPayload, context: ClientPlayNetworking.Context) {
        println("denied keybinds")
    }
}
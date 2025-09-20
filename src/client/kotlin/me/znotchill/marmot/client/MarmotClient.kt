package me.znotchill.marmot.client

import me.znotchill.marmot.client.payloads.CameraLockPayload
import me.znotchill.marmot.client.payloads.CameraPayload
import me.znotchill.marmot.client.payloads.ForceKeybindsPayload
import me.znotchill.marmot.client.payloads.MouseLockPayload
import me.znotchill.marmot.client.ui.screens.ForceKeybindsScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import java.lang.reflect.Field

class MarmotClient : ClientModInitializer {
    override fun onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(CameraPayload.ID, CameraPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(CameraLockPayload.ID, CameraLockPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(MouseLockPayload.ID, MouseLockPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(ForceKeybindsPayload.ID, ForceKeybindsPayload.CODEC)

        ClientPlayNetworking.registerGlobalReceiver(CameraPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                Client.pitch = payload.x
                Client.yaw = payload.y
                Client.roll = payload.z
                Client.targetFov = payload.fov
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(MouseLockPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.mouseButtonsLocked = lock
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(CameraLockPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                val lock = payload.locked.toInt() != 0
                Client.cameraLocked = lock
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(ForceKeybindsPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                client.setScreen(
                    ForceKeybindsScreen(
                        payload,
                        onAccept = {

                            payload.binds.forEach { (bindName, forcedKeyTranslationKey) ->
                                val keyBinding = client.options.allKeys.firstOrNull { it.translationKey == bindName }
                                    ?: return@forEach println("Unknown bind name: $bindName")

                                val forcedKey = InputUtil.fromTranslationKey(forcedKeyTranslationKey)
                                    ?: return@forEach println("Unknown key translation: $forcedKeyTranslationKey")

                                KeybindManager.overrideKeybind(keyBinding, forcedKey)
                                println("Set ${keyBinding.translationKey} to $forcedKeyTranslationKey")
                            }

                            KeyBinding.updateKeysByCode()
                        },
                        onDeny = {
                            println("denied keybinds")
                        }
                    )
                )
            }
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            val client = MinecraftClient.getInstance()
            Client.pitch = 0f
            Client.yaw = 0f
            Client.roll = 0f
            Client.targetFov = client.options.fov.value.toFloat()
            Client.cameraLocked = false

            KeybindManager.restoreAll()
        }
    }
}

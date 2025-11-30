package me.znotchill.marmot.client.packets.clientbound.handlers

import me.znotchill.marmot.client.Client
import me.znotchill.marmot.client.packets.clientbound.payloads.CameraPayload
import me.znotchill.marmot.common.classes.FovOp
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient

class CameraHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(CameraPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                if (payload.x != -1f)
                    Client.customPitch = payload.x
                if (payload.y != -1f)
                    Client.customYaw = payload.y
                if (payload.z != -1f)
                    Client.customRoll = payload.z
                if (payload.fov != -1f) {
                    val fovOp = FovOp.fromId(payload.fovOp)

                    Client.targetFov = when (fovOp) {
                        FovOp.SET -> payload.fov
                        FovOp.ADD -> Client.currentFov + payload.fov
                        FovOp.MUL -> Client.currentFov * payload.fov
                        FovOp.DIV -> if (payload.fov != 0f)
                            Client.currentFov / payload.fov else Client.targetFov
                        FovOp.SUB -> Client.currentFov - payload.fov
                        FovOp.RESET -> MinecraftClient.getInstance().options.fov.value.toFloat()
                    }

                    Client.fovAnimTicks = payload.fovAnimTicks
                    Client.fovTicksRemaining = payload.fovAnimTicks
                }
                if (payload.lockFov.toInt() != -1)
                    Client.lockFov = payload.lockFov.toInt() != 0
                if (payload.animateFov.toInt() != -1)
                    Client.animateFov = payload.animateFov.toInt() != 0
            }
        }
    }
}
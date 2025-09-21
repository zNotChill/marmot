package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class CameraLockPayload(
    val locked: Byte
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<CameraLockPayload>(ClientPackets.CAMERA_LOCK)

        val CODEC: PacketCodec<PacketByteBuf, CameraLockPayload> = PacketCodec.tuple(
            PacketCodecs.BYTE, CameraLockPayload::locked,
            ::CameraLockPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
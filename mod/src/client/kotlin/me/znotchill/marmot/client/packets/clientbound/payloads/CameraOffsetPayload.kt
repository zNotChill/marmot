package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class CameraOffsetPayload(
    val x: Float,
    val y: Float,
    val z: Float
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<CameraOffsetPayload>(ClientPackets.CAMERA_OFFSET)

        val CODEC: PacketCodec<PacketByteBuf, CameraOffsetPayload> = PacketCodec.tuple(
            PacketCodecs.FLOAT, CameraOffsetPayload::x,
            PacketCodecs.FLOAT, CameraOffsetPayload::y,
            PacketCodecs.FLOAT, CameraOffsetPayload::z,
            ::CameraOffsetPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
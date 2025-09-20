package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class CameraPayload(
    val x: Float,
    val y: Float,
    val z: Float,
    val fov: Float,
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<CameraPayload>(ClientPackets.CAMERA)

        val CODEC: PacketCodec<PacketByteBuf, CameraPayload> = PacketCodec.tuple(
            PacketCodecs.FLOAT, CameraPayload::x,
            PacketCodecs.FLOAT, CameraPayload::y,
            PacketCodecs.FLOAT, CameraPayload::z,
            PacketCodecs.FLOAT, CameraPayload::fov,
            ::CameraPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
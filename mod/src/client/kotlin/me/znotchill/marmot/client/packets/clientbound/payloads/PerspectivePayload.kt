package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload


class PerspectivePayload(
    val perspective: String,
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<PerspectivePayload>(ClientPackets.PERSPECTIVE)

        val CODEC: PacketCodec<PacketByteBuf, PerspectivePayload> = PacketCodec.tuple(
            PacketCodecs.STRING, PerspectivePayload::perspective,
            ::PerspectivePayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class IsMarmotClientPayload(
    val status: Int
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<IsMarmotClientPayload>(ClientPackets.IS_MARMOT)

        val CODEC: PacketCodec<PacketByteBuf, IsMarmotClientPayload> = PacketCodec.tuple(
            PacketCodecs.INTEGER, IsMarmotClientPayload::status,
            ::IsMarmotClientPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
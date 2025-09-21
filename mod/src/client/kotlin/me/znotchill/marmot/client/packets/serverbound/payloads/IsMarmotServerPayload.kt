package me.znotchill.marmot.client.packets.serverbound.payloads

import me.znotchill.marmot.client.packets.serverbound.ServerPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class IsMarmotServerPayload(
    val status: Int
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<IsMarmotServerPayload>(ServerPackets.IS_MARMOT)

        val CODEC: PacketCodec<PacketByteBuf, IsMarmotServerPayload> = PacketCodec.tuple(
            PacketCodecs.INTEGER, IsMarmotServerPayload::status,
            ::IsMarmotServerPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
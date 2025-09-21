package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class UIPayload(
    val json: String
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<UIPayload>(ClientPackets.UI)

        val CODEC: PacketCodec<PacketByteBuf, UIPayload> = PacketCodec.tuple(
            PacketCodecs.STRING, UIPayload::json,
            ::UIPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
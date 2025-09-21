package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload


class MousePayload(
    val locked: Byte,
    val emitEvents: Byte,
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<MousePayload>(ClientPackets.MOUSE)

        val CODEC: PacketCodec<PacketByteBuf, MousePayload> = PacketCodec.tuple(
            PacketCodecs.BYTE, MousePayload::locked,
            PacketCodecs.BYTE, MousePayload::emitEvents,
            ::MousePayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
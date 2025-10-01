package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class PerspectiveLockPayload(
    val locked: Byte
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<PerspectiveLockPayload>(ClientPackets.PERSPECTIVE_LOCK)

        val CODEC: PacketCodec<PacketByteBuf, PerspectiveLockPayload> = PacketCodec.tuple(
            PacketCodecs.BYTE, PerspectiveLockPayload::locked,
            ::PerspectiveLockPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload


class MouseLockPayload(
    val locked: Byte
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<MouseLockPayload>(ClientPackets.MOUSE_LOCK)

        val CODEC: PacketCodec<PacketByteBuf, MouseLockPayload> = PacketCodec.tuple(
            PacketCodecs.BYTE, MouseLockPayload::locked,
            ::MouseLockPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
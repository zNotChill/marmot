package me.znotchill.marmot.client.payloads

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier


class MouseLockPayload(
    val locked: Byte
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<MouseLockPayload>(Identifier.of("marmot", "mouse_lock"))

        val CODEC: PacketCodec<PacketByteBuf, MouseLockPayload> = PacketCodec.tuple(
            PacketCodecs.BYTE, MouseLockPayload::locked,
            ::MouseLockPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
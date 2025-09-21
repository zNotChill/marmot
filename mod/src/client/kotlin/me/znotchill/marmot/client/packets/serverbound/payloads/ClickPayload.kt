package me.znotchill.marmot.client.packets.serverbound.payloads

import me.znotchill.marmot.client.packets.serverbound.ServerPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class ClickUpdatePayload(val leftHeld: Boolean, val rightHeld: Boolean): CustomPayload {
    companion object {
        val ID = CustomPayload.Id<ClickUpdatePayload>(ServerPackets.CLICK_UPDATE)

        val CODEC: PacketCodec<PacketByteBuf, ClickUpdatePayload> = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, ClickUpdatePayload::leftHeld,
            PacketCodecs.BOOLEAN, ClickUpdatePayload::rightHeld,
            ::ClickUpdatePayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload?> {
        return ID
    }
}
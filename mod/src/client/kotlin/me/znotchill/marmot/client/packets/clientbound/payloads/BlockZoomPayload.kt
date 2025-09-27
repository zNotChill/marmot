package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class BlockZoomPayload(
    val locked: Byte
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<BlockZoomPayload>(ClientPackets.BLOCK_ZOOM)

        val CODEC: PacketCodec<PacketByteBuf, BlockZoomPayload> = PacketCodec.tuple(
            PacketCodecs.BYTE, BlockZoomPayload::locked,
            ::BlockZoomPayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
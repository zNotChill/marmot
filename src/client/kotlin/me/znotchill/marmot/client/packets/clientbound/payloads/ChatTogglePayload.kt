package me.znotchill.marmot.client.packets.clientbound.payloads

import me.znotchill.marmot.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class ChatTogglePayload(
    val chatEnabled: Boolean,
    val fadeEnabled: Boolean
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<ChatTogglePayload>(ClientPackets.CHAT_TOGGLE)

        val CODEC: PacketCodec<PacketByteBuf, ChatTogglePayload> = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, ChatTogglePayload::chatEnabled,
            PacketCodecs.BOOLEAN, ChatTogglePayload::fadeEnabled,
            ::ChatTogglePayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}

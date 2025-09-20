package me.znotchill.marmot.client.payloads

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier


class ForceKeybindsPayload(
    val binds: Map<String, String>
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<ForceKeybindsPayload>(Identifier.of("marmot", "force_keybinds"))

        val CODEC: PacketCodec<PacketByteBuf, ForceKeybindsPayload> =
            PacketCodec.of(
                { value, buf ->
                    buf.writeVarInt(value.binds.size)
                    for ((bindName, keyName) in value.binds) {
                        buf.writeString(bindName)
                        buf.writeString(keyName)
                    }
                },
                { buf ->
                    val size = buf.readVarInt()
                    val map = mutableMapOf<String, String>()
                    repeat(size) {
                        val bindName = buf.readString()
                        val keyName = buf.readString()
                        map[bindName] = keyName
                    }
                    ForceKeybindsPayload(map)
                }
            )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}
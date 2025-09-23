package me.znotchill.marmot.common.networking

import io.netty.buffer.ByteBuf
import java.nio.charset.StandardCharsets

object BufUtils {
    fun writeVarInt(buf: ByteBuf, value: Int) {
        var i = value
        while (true) {
            if ((i and 0x7F.inv()) == 0) {
                buf.writeByte(i)
                return
            }
            buf.writeByte((i and 0x7F) or 0x80)
            i = i ushr 7
        }
    }

    fun writeString(buf: ByteBuf, value: String) {
        val bytes = value.toByteArray(StandardCharsets.UTF_8)
        writeVarInt(buf, bytes.size)
        buf.writeBytes(bytes)
    }
}
package me.znotchill.marmot.minestom.api

import io.netty.buffer.ByteBufAllocator
import net.minestom.server.entity.Player
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import java.nio.ByteBuffer
import kotlin.collections.iterator

object MarmotAPI {
    fun sendKeybinds(player: Player, binds: Map<String, String>) {
        val buf = ByteBufAllocator.DEFAULT.buffer()

        BufUtils.writeVarInt(buf, binds.size)
        for ((bindName, keyName) in binds) {
            BufUtils.writeString(buf, bindName)
            BufUtils.writeString(buf, keyName)
        }

        val bytes = ByteArray(buf.readableBytes())
        buf.readBytes(bytes)
        buf.release()

        val packet = PluginMessagePacket("marmot:force_keybinds", bytes)
        player.sendPacket(packet)
    }

    fun sendCameraPacket(
        player: Player,
        pitch: Float,
        yaw: Float,
        roll: Float,
        fov: Float
    ) {
        val buffer = ByteBuffer.allocate(16)
        buffer.putFloat(pitch)
        buffer.putFloat(yaw)
        buffer.putFloat(roll)
        buffer.putFloat(fov)
        val packet = PluginMessagePacket("marmot:camera", buffer.array())
        player.sendPacket(packet)
    }

    fun lockCamera(player: Player) {
        setCameraLock(player, true)
    }
    fun unlockCamera(player: Player) {
        setCameraLock(player, false)
    }
    fun setCameraLock(player: Player, locked: Boolean) {
        val buffer = ByteBuffer.allocate(1)
        buffer.put(if (locked) 1 else 0)
        val packet = PluginMessagePacket("marmot:camera_lock", buffer.array())
        player.sendPacket(packet)
    }

    fun lockMouse(player: Player) {
        setMouseLock(player, true)
    }
    fun unlockMouse(player: Player) {
        setMouseLock(player, false)
    }
    fun setMouseLock(player: Player, locked: Boolean) {
        val buffer = ByteBuffer.allocate(1)
        buffer.put(if (locked) 1 else 0)
        val packet = PluginMessagePacket("marmot:mouse_lock", buffer.array())
        player.sendPacket(packet)
    }
}
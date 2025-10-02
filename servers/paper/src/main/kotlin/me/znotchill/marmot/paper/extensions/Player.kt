package me.znotchill.marmot.paper.extensions

import me.znotchill.marmot.common.ClientPerspective
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.UIEventSerializer
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.UIEvent
import me.znotchill.marmot.paper.api.MarmotAPI
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import kotlin.collections.iterator

fun buildPacket(block: DataOutputStream.() -> Unit): ByteArray {
    return ByteArrayOutputStream().use { out ->
        DataOutputStream(out).use { data ->
            data.block()
        }
        out.toByteArray()
    }
}

private fun DataOutputStream.writeVarInt(value: Int) {
    var v = value
    while (true) {
        if ((v and 0x7F.inv()) == 0) {
            this.writeByte(v)
            return
        }
        this.writeByte((v and 0x7F) or 0x80)
        v = v ushr 7
    }
}

private fun DataOutputStream.writeString(value: String) {
    val bytes = value.toByteArray(StandardCharsets.UTF_8)
    writeVarInt(bytes.size)
    write(bytes)
}

/**
 * Sends a basic health packet to the player's client.
 * If the client is using Marmot, the client will automatically
 * send back a confirmation health packet.
 */
fun Player.handshake() {
    val buffer = ByteBuffer.allocate(4)
    buffer.putInt(1)
    sendPluginMessage(MarmotAPI.plugin, "marmot:is_marmot", buffer.array())
}

fun Player.adjustCamera(
    pitch: Float,
    yaw: Float,
    roll: Float,
    fov: Float
) {
    val bytes = buildPacket {
        writeFloat(pitch)
        writeFloat(yaw)
        writeFloat(roll)
        writeFloat(fov)
    }
    sendPluginMessage(MarmotAPI.plugin, "marmot:camera", bytes)
}

fun Player.adjustCameraOffset(x: Float, y: Float, z: Float) {
    val bytes = buildPacket {
        writeFloat(x)
        writeFloat(y)
        writeFloat(z)
    }
    sendPluginMessage(MarmotAPI.plugin, "marmot:camera_offset", bytes)
}

fun Player.lockCamera(locked: Boolean) {
    val bytes = buildPacket { writeBoolean(locked) }
    sendPluginMessage(MarmotAPI.plugin, "marmot:camera_lock", bytes)
}

fun Player.configureMouse(locked: Boolean, emitEvents: Boolean) {
    val bytes = buildPacket {
        writeBoolean(locked)
        writeBoolean(emitEvents)
    }
    sendPluginMessage(MarmotAPI.plugin, "marmot:mouse", bytes)
}

fun Player.lockPerspective(locked: Boolean) {
    val bytes = buildPacket { writeBoolean(locked) }
    sendPluginMessage(MarmotAPI.plugin, "marmot:perspective_lock", bytes)
}

fun Player.setPerspective(perspective: ClientPerspective) {
    val bytes = buildPacket { writeString(perspective.toString()) }
    sendPluginMessage(MarmotAPI.plugin, "marmot:perspective", bytes)
}

fun Player.sendKeybinds(binds: Map<String, String>) {
    val bytes = buildPacket {
        writeVarInt(binds.size)
        for ((bindName, keyName) in binds) {
            writeString(bindName)
            writeString(keyName)
        }
    }
    sendPluginMessage(MarmotAPI.plugin, "marmot:force_keybinds", bytes)
}

fun Player.openUI(marmotUI: MarmotUI) {
    this.openUI(marmotUI.build())
}

fun Player.openUI(uiWindow: UIWindow) {
    val json = uiWindow.encode()
    val bytes = buildPacket {
        writeBoolean(false)
        writeString(json)
    }
    sendPluginMessage(MarmotAPI.plugin, "marmot:ui", bytes)
}

fun Player.updateUI(events: List<UIEvent>) {
    if (events.isEmpty()) return
    val json = UIEventSerializer.encode(events)
    val bytes = buildPacket {
        writeBoolean(true)
        writeString(json)
    }
    sendPluginMessage(MarmotAPI.plugin, "marmot:ui", bytes)
}
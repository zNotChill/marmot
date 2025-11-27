package me.znotchill.marmot.minestom.api.extensions

import io.netty.buffer.ByteBufAllocator
import me.znotchill.marmot.common.ClientPerspective
import me.znotchill.marmot.common.networking.BufUtils
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.UIEventSerializer
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.UIEvent
import me.znotchill.marmot.common.api.MarmotPlayer
import me.znotchill.marmot.minestom.api.marmot
import net.minestom.server.entity.Player
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import java.nio.ByteBuffer

/**
 * Sends a basic health packet to the player's client.
 * If the client is using Marmot, the client will automatically
 * send back a confirmation health packet.
 *
 * The returned packet is automatically acknowledged and served into
 * the [Player]'s [MarmotPlayer].
 */
fun Player.handshake() {
    val buf = ByteBufAllocator.DEFAULT.buffer()
    buf.writeInt(1)

    val bytes = ByteArray(buf.readableBytes())
    val packet = PluginMessagePacket("marmot:is_marmot", bytes)
    sendPacket(packet)
}

/**
 * Forcefully manipulate the player's camera.
 */
fun Player.adjustCamera(
    pitch: Float = -1f,
    yaw: Float = -1f,
    roll: Float = -1f,
    fov: Float = -1f,
    lockFov: Boolean? = null,
    animateFov: Boolean? = null
) {
    val buffer = ByteBuffer.allocate(18)
    buffer.putFloat(pitch)
    buffer.putFloat(yaw)
    buffer.putFloat(roll)
    buffer.putFloat(fov)

    if (lockFov == null)
        buffer.put(-1)
    else
        buffer.put(if (lockFov) 1 else 0)

    if (animateFov == null)
        buffer.put(-1)
    else
        buffer.put(if (animateFov) 1 else 0)

    val packet = PluginMessagePacket("marmot:camera", buffer.array())
    sendPacket(packet)
}

/**
 * Send forced keybinds to the client.
 * Opens a GUI on the client asking the user if they
 * want to override their keybinds temporarily.
 *
 * Must use Minecraft translation keys, for example:
 * ```kt
 * sendKeybinds(
 *  player,
 *  mapOf(
 *   "key.advancements" to "key.keyboard.y"
 *  )
 * )
 * ```
 */
fun Player.sendKeybinds(binds: Map<String, String>) {
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
    sendPacket(packet)
}

/**
 * Offset the player's camera.
 */
fun Player.adjustCameraOffset(
    x: Float,
    y: Float,
    z: Float,
) {
    val buffer = ByteBuffer.allocate(12)
    buffer.putFloat(x)
    buffer.putFloat(y)
    buffer.putFloat(z)
    val packet = PluginMessagePacket("marmot:camera_offset", buffer.array())
    sendPacket(packet)
}

/**
 * Decides whether the player's camera should be locked
 * from moving on both axes.
 */
fun Player.lockCamera(locked: Boolean) {
    val buffer = ByteBuffer.allocate(1)
    buffer.put(if (locked) 1 else 0)
    val packet = PluginMessagePacket("marmot:camera_lock", buffer.array())
    sendPacket(packet)
}

/**
 * Forcefully manipulate the player's mouse.
 *
 * @param locked Whether the player's mouse buttons should be locked. Disables the hand swing.
 * @param emitEvents Whether the player's mouse buttons should still emit click events
 * while the mouse buttons are locked.
 */
fun Player.configureMouse(
    locked: Boolean,
    emitEvents: Boolean
) {
    val buffer = ByteBuffer.allocate(2)
    buffer.put(if (locked) 1 else 0)
    buffer.put(if (emitEvents) 1 else 0)
    val packet = PluginMessagePacket("marmot:mouse", buffer.array())
    sendPacket(packet)
}

/**
 * Forcefully lock a player's perspective.
 */
fun Player.lockPerspective(locked: Boolean) {
    val buffer = ByteBuffer.allocate(1)

    buffer.put(if (locked) 1 else 0)
    val packet = PluginMessagePacket("marmot:perspective_lock", buffer.array())
    sendPacket(packet)
}

/**
 * Force a player's perspective.
 */
fun Player.setPerspective(perspective: ClientPerspective) {
    val buf = ByteBufAllocator.DEFAULT.buffer()

    BufUtils.writeString(buf, perspective.toString())

    val bytes = ByteArray(buf.readableBytes())
    buf.readBytes(bytes)
    buf.release()

    val packet = PluginMessagePacket("marmot:perspective", bytes)
    sendPacket(packet)
}

/**
 * Send a [MarmotUI] to display on the player's screen.
 */
fun Player.openUI(marmotUI: MarmotUI) {
    this.openUI(marmotUI.build())
}

/**
 * Send a [UIWindow] to display on the player's screen.
 */
fun Player.openUI(uiWindow: UIWindow) {
    val jsonString = uiWindow.encode()
    val buf = ByteBufAllocator.DEFAULT.buffer()
    buf.writeBoolean(false)
    BufUtils.writeString(buf, jsonString)

    val bytes = ByteArray(buf.readableBytes())
    buf.getBytes(buf.readerIndex(), bytes)

    val packet = PluginMessagePacket("marmot:ui", bytes)
    sendPacket(packet)
    buf.release()

    marmot?.currentWindow = uiWindow
    marmot?.previousWindow = uiWindow.deepCopy()
}

/**
 * Send a UI update to the player.
 * **Should not be used manually.**
 * Only use when acknowledging events and flushing them immediately.
 */
fun Player.updateUI(events: List<UIEvent>) {
    if (events.isEmpty()) return

    val jsonString = UIEventSerializer.encode(events)
    val buf = ByteBufAllocator.DEFAULT.buffer()
    buf.writeBoolean(true)
    BufUtils.writeString(buf, jsonString)

    val bytes = ByteArray(buf.readableBytes())
    buf.getBytes(buf.readerIndex(), bytes)

    val packet = PluginMessagePacket("marmot:ui", bytes)
    sendPacket(packet)
    buf.release()
}

/**
 * Clear a player's rendered UI.
 */
fun Player.clearUI(id: String = "active") {
    val buf = ByteBufAllocator.DEFAULT.buffer()
    BufUtils.writeString(buf, id)

    val bytes = ByteArray(buf.readableBytes())
    buf.readBytes(bytes)
    buf.release()

    val packet = PluginMessagePacket("marmot:clear_ui", bytes)
    sendPacket(packet)
}
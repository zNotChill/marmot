package me.znotchill.marmot.minestom.api

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.extensions.ticks
import me.znotchill.blossom.scheduler.task
import me.znotchill.marmot.common.networking.BufUtils
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.UIEventQueue
import me.znotchill.marmot.common.ui.UIEventSerializer
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.UIEvent
import net.minestom.server.entity.Player
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import net.minestom.server.timer.SchedulerManager
import java.nio.ByteBuffer
import java.util.*

enum class MarmotEvent {
    LEFT_CLICK_BEGIN,
    LEFT_CLICK_END,
    RIGHT_CLICK_BEGIN,
    RIGHT_CLICK_END
}

object MarmotAPI {
    private val players: MutableMap<UUID, MarmotPlayer> = mutableMapOf()

    private val events: MutableMap<MarmotEvent, (player: Player) -> Unit> = mutableMapOf()

    fun getEvent(event: MarmotEvent): (Player) -> Unit {
        return events[event] ?: {}
    }

    fun addEvent(event: MarmotEvent, run: (Player) -> Unit) {
        events[event] = run
    }

    fun registerEvents(eventHandler: GlobalEventHandler) {
        eventHandler.addListener<PlayerPluginMessageEvent> { event ->
            val player = event.player

            when (event.identifier) {
                "marmot:click_update" -> {
                    val playerMarmot = player.marmot ?: return@addListener
                    val buf: ByteBuf = Unpooled.wrappedBuffer(event.message)

                    val leftHeld = buf.readBoolean()
                    val rightHeld = buf.readBoolean()

                    if (leftHeld && !playerMarmot.holdingLeftClick) {
                        getEvent(MarmotEvent.LEFT_CLICK_BEGIN)(event.player)
                    } else if (!leftHeld && playerMarmot.holdingLeftClick) {
                        getEvent(MarmotEvent.LEFT_CLICK_END)(event.player)
                    }

                    if (rightHeld && !playerMarmot.holdingRightClick) {
                        getEvent(MarmotEvent.RIGHT_CLICK_BEGIN)(event.player)
                    } else if (!rightHeld && playerMarmot.holdingRightClick) {
                        getEvent(MarmotEvent.RIGHT_CLICK_END)(event.player)
                    }

                    playerMarmot.holdingLeftClick = leftHeld
                    playerMarmot.holdingRightClick = rightHeld

                    println("Player ${event.player.username} leftHeld=$leftHeld rightHeld=$rightHeld")
                }
                "marmot:is_marmot" -> {
                    println("received is_marmot health packet from client")
                    players[event.player.uuid] = MarmotPlayer.create(event.player)
                }
            }
        }

        eventHandler.addListener<PlayerLoadedEvent> { event ->
            println("sending is_marmot health packet to client")
            sendHealthPacket(event.player)
        }

        eventHandler.addListener<PlayerDisconnectEvent> { event ->
            players.remove(event.player.uuid)
        }
    }

    fun registerTasks(schedulerManager: SchedulerManager) {
        schedulerManager.task {
            repeat = 1.ticks
            run = { task ->
                val events = UIEventQueue.tick()

                if (events.isNotEmpty()) {
                    val byWindow = events.groupBy { it.window?.id }

                    for ((windowId, windowEvents) in byWindow) {
                        val audience = players.values.filter { it.currentWindow?.id == windowId }
                        if (audience.isEmpty()) continue

                        val byComponent = windowEvents.groupBy { it.targetId }

                        for ((componentId, compEvents) in byComponent) {
                            // Group components together by ID, and send them to the relevant players that have this UIWindow rendered
                            audience.forEach { marmotPlayer ->
                                val player = marmotPlayer.player
                                sendUIEvents(player, compEvents)
                                println("Sent ${compEvents.size} updates for $componentId to ${player.username}")
                            }
                        }
                    }
                }
            }
        }
    }

    fun isUsingMarmot(player: Player): Boolean {
        return players[player.uuid]?.isMarmot == true
    }

    fun getMarmotPlayer(player: Player): MarmotPlayer? {
        return players[player.uuid]
    }

    /**
     * Sends a basic health packet to the client.
     * If the client is using Marmot, the client will automatically
     * send back a confirmation health packet.
     */
    fun sendHealthPacket(player: Player) {
        val buf = ByteBufAllocator.DEFAULT.buffer()
        buf.writeInt(1)

        val bytes = ByteArray(buf.readableBytes())
        val packet = PluginMessagePacket("marmot:is_marmot", bytes)
        player.sendPacket(packet)
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

    /**
     * Forcefully manipulate the client's camera.
     */
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

    /**
     * Offset the player's camera.
     */
    fun sendCameraOffset(
        player: Player,
        x: Float,
        y: Float,
        z: Float,
    ) {
        val buffer = ByteBuffer.allocate(12)
        buffer.putFloat(x)
        buffer.putFloat(y)
        buffer.putFloat(z)
        val packet = PluginMessagePacket("marmot:camera_offset", buffer.array())
        player.sendPacket(packet)
    }

    /**
     * Decides whether the player's camera should be locked
     * from moving.
     */
    fun setCameraLock(player: Player, locked: Boolean) {
        val buffer = ByteBuffer.allocate(1)
        buffer.put(if (locked) 1 else 0)
        val packet = PluginMessagePacket("marmot:camera_lock", buffer.array())
        player.sendPacket(packet)
    }

    /**
     * Forcefully manipulate the player's mouse.
     * Can be locked from clicking left or right click.
     * Can be locked from emitting click events.
     */
    fun setMouse(player: Player, locked: Boolean, emitEvents: Boolean) {
        val buffer = ByteBuffer.allocate(2)
        buffer.put(if (locked) 1 else 0)
        buffer.put(if (emitEvents) 1 else 0)
        val packet = PluginMessagePacket("marmot:mouse", buffer.array())
        player.sendPacket(packet)
    }

    /**
     * Send a constructed UI to display on the player's screen.
     */
    fun sendUI(player: Player, marmotUI: MarmotUI) {
        sendUI(player, marmotUI.build())
    }

    /**
     * Send a constructed UI to display on the player's screen.
     */
    fun sendUI(player: Player, uiWindow: UIWindow) {
        val jsonString = uiWindow.encode()
        val buf = ByteBufAllocator.DEFAULT.buffer()
        buf.writeBoolean(false)
        BufUtils.writeString(buf, jsonString)

        val bytes = ByteArray(buf.readableBytes())
        buf.getBytes(buf.readerIndex(), bytes)

        val packet = PluginMessagePacket("marmot:ui", bytes)
        player.sendPacket(packet)
        buf.release()

        player.marmot?.currentWindow = uiWindow
        player.marmot?.previousWindow = uiWindow.deepCopy()
    }

    /**
     * Send a UI update to the player.
     * **Should not be used manually.**
     * Only use when acknowledging events and flushing them immediately.
     */
    fun sendUIEvents(player: Player, events: List<UIEvent>) {
        if (events.isEmpty()) return

        val jsonString = UIEventSerializer.encode(events)
        val buf = ByteBufAllocator.DEFAULT.buffer()
        buf.writeBoolean(true)
        BufUtils.writeString(buf, jsonString)

        val bytes = ByteArray(buf.readableBytes())
        buf.getBytes(buf.readerIndex(), bytes)

        val packet = PluginMessagePacket("marmot:ui", bytes)
        player.sendPacket(packet)
        buf.release()
    }
}
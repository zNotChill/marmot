package me.znotchill.marmot.minestom.api

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.extensions.ticks
import me.znotchill.blossom.scheduler.task
import me.znotchill.marmot.common.ClientPerspective
import me.znotchill.marmot.common.api.BaseMarmotAPI
import me.znotchill.marmot.common.api.MarmotEvent
import me.znotchill.marmot.common.api.MarmotPlayer
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.UIEventQueue
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.UIEvent
import me.znotchill.marmot.minestom.api.extensions.*
import net.kyori.adventure.audience.Audience
import net.minestom.server.entity.Player
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.timer.SchedulerManager
import org.slf4j.LoggerFactory
import java.util.*

object MarmotAPI : BaseMarmotAPI<Player, MarmotPlayer<Player>> {
    override val players: MutableMap<UUID, MarmotPlayer<Player>> = mutableMapOf()

    val logger: Logger = LoggerFactory.getLogger("Marmot") as Logger

    private val events: MutableMap<MarmotEvent, (player: Player) -> Unit> = mutableMapOf()

    /**
     * Whether Marmot should log debug statements to the console.
     */
    var debuggingEnabled: Boolean
        get() = logger.isDebugEnabled
        set(value) {
            logger.level = if (value) Level.DEBUG else Level.INFO
        }

    override fun getEvent(event: MarmotEvent): (Player) -> Unit {
        return events[event] ?: {}
    }

    override fun addEvent(event: MarmotEvent, run: (Player) -> Unit) {
        events[event] = run
    }

    /**
     * Registers Marmot's server-wide events.
     */
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
                }
                "marmot:is_marmot" -> {
                    logger.debug("Received Marmot health packet from {} ({})", event.player.username, event.player.uuid)
                    players[event.player.uuid] = MarmotPlayer.create(event.player)
                }
            }
        }

        eventHandler.addListener<PlayerLoadedEvent> { event ->
            event.player.handshake()
        }

        eventHandler.addListener<PlayerDisconnectEvent> { event ->
            players.remove(event.player.uuid)
        }
    }

    /**
     * Registers Marmot's server-wide tasks.
     */
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
                                updateUI(player, compEvents)
                                logger.debug("Sending {} UI updates to {} ({})", compEvents.size, player.username, player.uuid)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun isUsingMarmot(player: Player): Boolean {
        return players[player.uuid]?.isMarmot == true
    }

    override fun getMarmotPlayer(player: Player): MarmotPlayer<Player>? {
        return players[player.uuid]
    }

    /**
     * @see Player.handshake
     */
    override fun handshake(player: Player) = player.handshake()

    /**
     * @see Player.sendKeybinds
     */
    override fun sendKeybinds(audience: Audience, binds: Map<String, String>) {
        audience.players().forEach { player ->
            player.sendKeybinds(binds)
        }
    }

    /**
     * @see Player.sendKeybinds
     */
    fun sendKeybinds(players: List<Player>, binds: Map<String, String>) {
        players.forEach { player ->
            player.sendKeybinds(binds)
        }
    }

    /**
     * @see Player.adjustCamera
     */
    override fun adjustCamera(
        audience: Audience,
        pitch: Float,
        yaw: Float,
        roll: Float,
        fov: Float
    ) {
        audience.players().forEach { player ->
            player.adjustCamera(pitch, yaw, roll, fov)
        }
    }

    /**
     * @see Player.adjustCameraOffset
     */
    override fun adjustCameraOffset(
        audience: Audience,
        x: Float,
        y: Float,
        z: Float,
    ) {
        audience.players().forEach { player ->
            player.adjustCameraOffset(x, y, z)
        }
    }

    /**
     * @see Player.lockCamera
     */
    override fun lockCamera(audience: Audience, locked: Boolean) {
        audience.players().forEach { player ->
            player.lockCamera(locked)
        }
    }

    /**
     * @see Player.configureMouse
     */
    override fun configureMouse(
        audience: Audience,
        locked: Boolean,
        emitEvents: Boolean
    ) {
        audience.players().forEach { player ->
            player.configureMouse(locked, emitEvents)
        }
    }

    /**
     * @see Player.openUI
     */
    override fun openUI(
        audience: Audience,
        marmotUI: MarmotUI
    ) {
        audience.players().forEach { player ->
            player.openUI(marmotUI)
        }
    }

    /**
     * @see Player.openUI
     */
    override fun openUI(
        audience: Audience,
        uiWindow: UIWindow
    ) {
        audience.players().forEach { player ->
            player.openUI(uiWindow)
        }
    }

    /**
     * @see Player.updateUI
     */
    override fun updateUI(
        audience: Audience,
        events: List<UIEvent>
    ) {
        audience.players().forEach { player ->
            player.updateUI(events)
        }
    }

    /**
     * @see Player.lockPerspective
     */
    override fun lockPerspective(
        audience: Audience,
        locked: Boolean
    ) {
        audience.players().forEach { player ->
            player.lockPerspective(locked)
        }
    }
    /**
     * @see Player.setPerspective
     */
    override fun setPerspective(
        audience: Audience,
        perspective: ClientPerspective
    ) {
        audience.players().forEach { player ->
            player.setPerspective(perspective)
        }
    }

}

fun Audience.players(): List<Player> {
    val result = mutableListOf<Player>()
    forEachAudience {
        if (it is Player) result += it
    }
    return result
}

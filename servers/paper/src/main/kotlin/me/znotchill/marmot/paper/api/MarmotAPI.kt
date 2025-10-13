package me.znotchill.marmot.paper.api

import me.znotchill.marmot.common.ClientPerspective
import me.znotchill.marmot.common.api.BaseMarmotAPI
import me.znotchill.marmot.common.api.MarmotEvent
import me.znotchill.marmot.common.api.MarmotPlayer
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.UIEventQueue
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.UIEvent
import me.znotchill.marmot.paper.api.extensions.*
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.LoggerFactory
import java.util.*

object MarmotAPI : BaseMarmotAPI<Player, MarmotPlayer<Player>> {
    val logger = LoggerFactory.getLogger("Marmot")!!
    override val players: MutableMap<UUID, MarmotPlayer<Player>> = mutableMapOf()
    private val events: MutableMap<MarmotEvent, (player: Player) -> Unit> = mutableMapOf()
    lateinit var plugin: JavaPlugin

    override fun addEvent(event: MarmotEvent, run: (Player) -> Unit) {
        events[event] = run
    }
    override fun getEvent(event: MarmotEvent): (Player) -> Unit {
        return events[event] ?: {}
    }

    fun registerEvents(plugin: JavaPlugin) {
        this@MarmotAPI.plugin = plugin

        val messenger = plugin.server.messenger
        messenger.registerIncomingPluginChannel(plugin, "marmot:click_update", MarmotMessageListener)
        messenger.registerIncomingPluginChannel(plugin, "marmot:is_marmot", MarmotMessageListener)

        val outgoing = listOf(
            "marmot:is_marmot",
            "marmot:click_update",
            "marmot:camera",
            "marmot:camera_offset",
            "marmot:camera_lock",
            "marmot:mouse",
            "marmot:force_keybinds",
            "marmot:perspective",
            "marmot:ui",
            "marmot:perspective_lock"
        )
        outgoing.forEach { messenger.registerOutgoingPluginChannel(plugin, it) }

        plugin.server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onJoin(event: PlayerJoinEvent) {
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    handshake(event.player)
                }, 5L)
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    println("${event.player.name} listening channels: ${event.player.listeningPluginChannels}")
                }, 20L)
            }
            @EventHandler
            fun onQuit(event: PlayerQuitEvent) {
                players.remove(event.player.uniqueId)
            }
        }, plugin)
    }

    fun registerTasks(plugin: JavaPlugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            val events = UIEventQueue.tick()

            if (events.isNotEmpty()) {
                val byWindow = events.groupBy { it.window?.id }

                for ((windowId, windowEvents) in byWindow) {
                    val audiencePlayers = players.values.filter { it.currentWindow?.id == windowId }
                    if (audiencePlayers.isEmpty()) continue

                    val byComponent = windowEvents.groupBy { it.targetId }

                    for ((componentId, compEvents) in byComponent) {
                        audiencePlayers.forEach { marmotPlayer ->
                            val player = marmotPlayer.player
                            updateUI(player, compEvents)
                            logger.debug("Sending {} UI updates to {} ({})", compEvents.size, player.name, player.uniqueId)
                        }
                    }
                }
            }
        }, 1L, 1L)
    }

    override fun isUsingMarmot(player: Player): Boolean {
        return players[player.uniqueId]?.isMarmot == true
    }

    override fun getMarmotPlayer(player: Player): MarmotPlayer<Player>? {
        return players[player.uniqueId]
    }

    override fun handshake(player: Player) {
        try {
            println("sending handshake")
            val bytes = buildPacket {
                writeInt(1)
            }
            player.sendPluginMessage(plugin, "marmot:is_marmot", bytes)
        } catch (ex: IllegalArgumentException) {
            logger.debug("Failed to send handshake plugin message to ${player.name}: ${ex.message}")
        }
    }

    override fun sendKeybinds(audience: Audience, binds: Map<String, String>) {
        audience.players().forEach { player ->
            player.sendKeybinds(binds)
        }
    }

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

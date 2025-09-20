package me.znotchill.marmot.minestom

import me.znotchill.blossom.command.command
import me.znotchill.blossom.component.component
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.marmot.minestom.api.MarmotAPI
import me.znotchill.marmot.minestom.api.MarmotEvent
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.InstanceContainer

class Server : BlossomServer(
    "Marmot-Server",
    auth = false
) {
    lateinit var instanceContainer: InstanceContainer

    override fun preLoad() {
        MarmotAPI.registerEvents(eventHandler)

        MarmotAPI.addEvent(MarmotEvent.LEFT_CLICK_BEGIN) { player ->
            player.sendMessage("began left click")
        }
        MarmotAPI.addEvent(MarmotEvent.LEFT_CLICK_END) { player ->
            player.sendMessage("stopped left click")
        }
        MarmotAPI.addEvent(MarmotEvent.RIGHT_CLICK_BEGIN) { player ->
            player.sendMessage("began right click")
        }
        MarmotAPI.addEvent(MarmotEvent.RIGHT_CLICK_END) { player ->
            player.sendMessage("stopped right click")
        }

        instanceContainer = BaseInstance().createInstance(instanceManager)

        eventHandler.addListener<PlayerLoadedEvent> { event ->
            MarmotAPI.sendKeybinds(
                event.player,
                mapOf(
                    "key.advancements" to "key.keyboard.y",
                )
            )
        }

        eventHandler.addListener<AsyncPlayerConfigurationEvent> { event ->
            val player = event.player
            event.spawningInstance = instanceContainer
            player.gameMode = GameMode.ADVENTURE
            player.permissionLevel = 4
        }

        registerCommand(
            command("lockcamera") {
                val lock = argument<Boolean>("lock")
                syntax(lock) { lockBool ->
                    MarmotAPI.setCameraLock(this, lockBool)
                }
            }
        )

        registerCommand(
            command("lockmouse") {
                val lock = argument<Boolean>("lock")
                syntax(lock) { lockBool ->
                    MarmotAPI.setMouseLock(this, lockBool)
                }
            }
        )

        registerCommand(
            command("sendhealth") {
                syntax {
                    MarmotAPI.sendHealthPacket(this)
                }
            }
        )

        registerCommand(
            command("ismarmot") {
                val playerName = argument<String>("player")
                syntax(playerName) { nameStr ->
                    val player = players.find { it.username == nameStr }
                    if (player == null) return@syntax

                    val isMarmot = MarmotAPI.isUsingMarmot(player)

                    if (isMarmot) {
                        val message = component {
                            text("${player.username} is using Marmot.")
                        }
                        sendMessage(message)
                    } else {
                        val message = component {
                            text("${player.username} is not using Marmot.")
                        }
                        sendMessage(message)
                    }
                }
            }
        )
    }

}

fun main() {
    Server().start()
}
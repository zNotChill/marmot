package me.znotchill.marmot.minestom

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import me.znotchill.blossom.command.command
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.marmot.minestom.api.MarmotAPI
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.instance.InstanceContainer

class Server : BlossomServer(
    "Marmot-Server",
    auth = false
) {
    lateinit var instanceContainer: InstanceContainer

    override fun preLoad() {
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

        eventHandler.addListener<PlayerPluginMessageEvent> { event ->
            when (event.identifier) {
                "marmot:click_update" -> {
                    val buf: ByteBuf = Unpooled.wrappedBuffer(event.message)

                    val leftHeld = buf.readBoolean()
                    val rightHeld = buf.readBoolean()

                    println("Player ${event.player.username} leftHeld=$leftHeld rightHeld=$rightHeld")
                }
            }
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
    }

}

fun main() {
    Server().start()
}
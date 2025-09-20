package me.znotchill.marmot.minestom

import me.znotchill.blossom.command.command
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import java.nio.ByteBuffer

class Server : BlossomServer(
    "Marmot-Server",
    auth = false
) {
    lateinit var instanceContainer: InstanceContainer

    override fun preLoad() {
        instanceContainer = BaseInstance().createInstance(instanceManager)

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
                    setCameraLock(this, lockBool)
                }
            }
        )

        registerCommand(
            command("lockmouse") {
                val lock = argument<Boolean>("lock")
                syntax(lock) { lockBool ->
                    setMouseLock(this, lockBool)
                }
            }
        )
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

fun main() {
    Server().start()
}
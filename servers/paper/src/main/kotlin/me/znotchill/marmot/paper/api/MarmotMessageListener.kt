package me.znotchill.marmot.paper.api

import me.znotchill.marmot.common.api.MarmotEvent
import me.znotchill.marmot.common.api.MarmotPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.ByteArrayInputStream
import java.io.DataInputStream

object MarmotMessageListener : PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        when (channel) {
            "marmot:click_update" -> {
                val playerMarmot = MarmotAPI.players[player.uniqueId] ?: return
                val input = DataInputStream(ByteArrayInputStream(message))

                val leftHeld = input.readBoolean()
                val rightHeld = input.readBoolean()

                if (leftHeld && !playerMarmot.holdingLeftClick) {
                    MarmotAPI.getEvent(MarmotEvent.LEFT_CLICK_BEGIN)(player)
                } else if (!leftHeld && playerMarmot.holdingLeftClick) {
                    MarmotAPI.getEvent(MarmotEvent.LEFT_CLICK_END)(player)
                }

                if (rightHeld && !playerMarmot.holdingRightClick) {
                    MarmotAPI.getEvent(MarmotEvent.RIGHT_CLICK_BEGIN)(player)
                } else if (!rightHeld && playerMarmot.holdingRightClick) {
                    MarmotAPI.getEvent(MarmotEvent.RIGHT_CLICK_END)(player)
                }

                playerMarmot.holdingLeftClick = leftHeld
                playerMarmot.holdingRightClick = rightHeld
            }

            "marmot:is_marmot" -> {
                MarmotAPI.logger.debug("Received Marmot health packet from {} ({})", player.name, player.uniqueId)
                MarmotAPI.players[player.uniqueId] = MarmotPlayer.create(player)
            }
        }
    }
}

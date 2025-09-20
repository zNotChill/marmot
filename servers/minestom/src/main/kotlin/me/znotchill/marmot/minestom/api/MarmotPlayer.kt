package me.znotchill.marmot.minestom.api

import net.minestom.server.entity.Player

class MarmotPlayer(
    var player: Player
) {
    val isMarmot: Boolean = true
    var holdingLeftClick: Boolean = false
    var holdingRightClick: Boolean = false

    companion object {
        fun create(player: Player): MarmotPlayer {
            return MarmotPlayer(player)
        }
    }
}
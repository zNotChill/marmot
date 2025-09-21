package me.znotchill.marmot.minestom.api

import me.znotchill.marmot.common.ui.UIWindow
import net.minestom.server.entity.Player

class MarmotPlayer(
    var player: Player
) {
    val isMarmot: Boolean = true
    var holdingLeftClick: Boolean = false
    var holdingRightClick: Boolean = false
    var previousWindow: UIWindow? = null

    companion object {
        fun create(player: Player): MarmotPlayer {
            return MarmotPlayer(player)
        }
    }
}
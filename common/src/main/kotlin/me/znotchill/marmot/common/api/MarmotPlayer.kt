package me.znotchill.marmot.common.api

import me.znotchill.marmot.common.ui.UIWindow

class MarmotPlayer<P>(
    val player: P
) {
    val isMarmot: Boolean = true
    var holdingLeftClick: Boolean = false
    var holdingRightClick: Boolean = false
    var previousWindow: UIWindow? = null
    var currentWindow: UIWindow? = null

    companion object {
        fun <P> create(player: P): MarmotPlayer<P> {
            return MarmotPlayer(player)
        }
    }
}
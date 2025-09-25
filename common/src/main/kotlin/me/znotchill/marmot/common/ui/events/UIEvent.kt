package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.ui.UIWindow

@Serializable
sealed class UIEvent {
    abstract val targetId: String
    abstract var delay: Long

    @Transient
    var window: UIWindow? = null
}
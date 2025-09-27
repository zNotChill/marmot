package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.ui.components.Component

@Serializable
data class PropertyAnimation<T>(
    override val targetId: String,
    override var delay: Long = 0L,
    val getter: (Component) -> T,
    val setter: (Component, T) -> Unit,
    /**
     * If this value is null, the client will do the animation from
     * the current position of the component.
     */
    var from: T? = null,
    val to: T,
    val durationSeconds: Double,
    val easing: String = "linear"
) : UIEvent() {
    @Transient var elapsed = 0.0
}
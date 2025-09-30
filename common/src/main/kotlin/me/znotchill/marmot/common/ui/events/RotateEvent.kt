package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.ui.classes.Easing

@Serializable
data class RotateEvent(
    override var delay: Long,
    override val targetId: String,
    val rotation: Int,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent() {
    @Transient var elapsed = 0.0
    @Transient var start: Float? = null
}
package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.classes.Easing

@Serializable
data class MoveEvent(
    override var delay: Long,
    override val targetId: String,
    val position: Vec2,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent() {
    @Transient var elapsed = 0.0
    @Transient var start: Vec2? = null
}
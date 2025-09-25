package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.Vec2
import kotlinx.serialization.Transient

@Serializable
data class MoveEvent(
    override var delay: Long,
    override val targetId: String,
    val to: Vec2,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent() {
    @Transient var elapsed = 0.0
    @Transient var start: Vec2? = null
}
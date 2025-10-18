package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.classes.Spacing

@Serializable
data class PaddingEvent(
    override var delay: Long,
    override val targetId: String,
    val padding: Spacing,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent()
package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.Easing

@Serializable
data class ProgressEvent(
    override var delay: Long,
    override val targetId: String,
    val progress: Float,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent()
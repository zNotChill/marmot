package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.ui.components.Component

@Serializable
data class PropertyAnimation<C : Component, T>(
    override val targetId: String,
    override var delay: Long = 0L,
    val getter: (C) -> T,
    val setter: (C, T) -> Unit,
    var from: T? = null,
    val to: T,
    val durationSeconds: Double,
    val easing: String = "linear"
) : UIEvent() {
    @Transient var elapsed = 0.0
}
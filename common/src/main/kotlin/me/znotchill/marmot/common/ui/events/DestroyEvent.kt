package me.znotchill.marmot.common.ui.events

import kotlinx.serialization.Serializable

@Serializable
data class DestroyEvent(
    override var delay: Long,
    override val targetId: String
) : UIEvent()
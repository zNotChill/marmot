package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.FlowDirection

@Serializable
open class FlowProps(
    var gap: Float = 0f,
    var direction: FlowDirection = FlowDirection.HORIZONTAL
) : CollectionProps()
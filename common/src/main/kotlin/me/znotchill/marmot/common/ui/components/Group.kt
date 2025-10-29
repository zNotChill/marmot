package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.CollectionProps

@Serializable
@SerialName("group")
data class Group(
    override val props: CollectionProps,
) : Component() {
    override val compType: CompType = CompType.GROUP

    override fun width(): Float {
        val childWidths = props.components.map { it.width() + it.props.pos.x }
        return (childWidths.maxOrNull() ?: 0).toInt() + props.padding.left + props.padding.right
    }

    override fun height(): Float {
        val childHeights = props.components.map { it.height() + it.props.pos.y }
        return (childHeights.maxOrNull() ?: 0).toInt() + props.padding.top + props.padding.bottom
    }
}
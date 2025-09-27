package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.GroupProps

@Serializable
@SerialName("group")
data class GroupComponent(
    override val props: GroupProps,
) : Component() {
    override val compType: CompType = CompType.GROUP

    override fun width(): Int {
        val childWidths = props.components.map { it.width() + it.props.pos.x }
        return (childWidths.maxOrNull() ?: 0).toInt() + props.padding.left + props.padding.right
    }

    override fun height(): Int {
        val childHeights = props.components.map { it.height() + it.props.pos.y }
        return (childHeights.maxOrNull() ?: 0).toInt() + props.padding.top + props.padding.bottom
    }
}
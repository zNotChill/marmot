package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.classes.FlowDirection
import me.znotchill.marmot.common.ui.components.props.FlowProps

@Serializable
@SerialName("flow_container")
data class FlowContainer(
    override val props: FlowProps,
) : Component() {
    override val compType: CompType = CompType.FLOW_CONTAINER

    override fun width(): Float {
        val totalWidth = when (props.direction) {
            FlowDirection.HORIZONTAL -> {
                val contentWidth = props.components.sumOf { (it.width() + it.props.margin.left + it.props.margin.right).toDouble() } +
                        ((props.components.size - 1) * props.gap)
                (contentWidth + props.padding.left + props.padding.right).toFloat()
            }
            FlowDirection.VERTICAL -> {
                val contentWidth = props.components.maxOfOrNull {
                    it.width() + it.props.margin.left + it.props.margin.right
                } ?: 0f
                contentWidth + props.padding.left + props.padding.right
            }
        }
        return totalWidth
    }

    override fun height(): Float {
        val totalHeight = when (props.direction) {
            FlowDirection.VERTICAL -> {
                val contentHeight = props.components.sumOf { (it.height() + it.props.margin.top + it.props.margin.bottom).toDouble() } +
                        ((props.components.size - 1) * props.gap)
                (contentHeight + props.padding.top + props.padding.bottom).toFloat()
            }
            FlowDirection.HORIZONTAL -> {
                val contentHeight = props.components.maxOfOrNull {
                    it.height() + it.props.margin.top + it.props.margin.bottom
                } ?: 0f
                contentHeight + props.padding.top + props.padding.bottom
            }
        }
        return totalHeight
    }
}

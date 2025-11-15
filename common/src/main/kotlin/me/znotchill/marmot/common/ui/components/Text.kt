package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.TextProps
import me.znotchill.marmot.common.ui.mcWidth

@Serializable
@SerialName("text")
open class Text(
    override val props: TextProps
) : Component() {
    override val compType: CompType = CompType.TEXT

    override fun width(): Float {
        val computed = computedSize?.x ?: 0f
        if (computed > 0f) return computed

        val lines = props.text.split("\n")
        val widest = lines.maxOfOrNull { it.mcWidth() } ?: 0
        val baseWidth = widest + props.padding.left + props.padding.right
        return (baseWidth * props.scale.x)
    }

    override fun height(): Float {
        val computed = computedSize?.y ?: 0f
        if (computed > 0f) return computed

        val lineCount = props.text.count { it == '\n' } + 1
        val baseHeight = (7 * lineCount) + props.padding.top + props.padding.bottom
        return (baseHeight * props.scale.y)
    }
}
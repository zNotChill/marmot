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
        val baseWidth = props.text.mcWidth() + props.padding.left + props.padding.right
        return (baseWidth * props.scale.x)
    }

    override fun height(): Float {
        val baseHeight = 7 + props.padding.top + props.padding.bottom
        return (baseHeight * props.scale.y)
    }
}
package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.TextProps
import me.znotchill.marmot.common.ui.mcWidth

@Serializable
@SerialName("text")
open class TextComponent(
    override val props: TextProps,
) : Component() {
    override val compType: CompType = CompType.TEXT

    override fun width(): Int {
        return props.text.mcWidth() + props.padding.left + props.padding.right
    }

    override fun height(): Int {
        // todo: not hardcode this
        return props.padding.top + props.padding.bottom + 7
    }
}
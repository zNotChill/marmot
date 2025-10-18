package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.SpriteProps

@Serializable
@SerialName("sprite")
open class SpriteComponent(
    override val props: SpriteProps
) : Component() {
    override val compType: CompType
        get() = CompType.SPRITE

    override fun width(): Float {
        val drawWidth = if (props.size.x > 0) props.size.x else (computedSize?.x ?: 0f)
        val baseWidth = drawWidth + props.padding.left + props.padding.right
        return (baseWidth * props.scale.x)
    }

    override fun height(): Float {
        val drawHeight = if (props.size.y > 0) props.size.y else (computedSize?.y ?: 0f)
        val baseHeight = drawHeight + props.padding.top + props.padding.bottom
        return (baseHeight * props.scale.y)
    }
}
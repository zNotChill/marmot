package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.components.props.TextProps
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.OpacityEvent
import me.znotchill.marmot.common.ui.mcWidth

@Serializable
@SerialName("text")
open class TextComponent(
    override val props: TextProps
) : Component() {
    override val compType: CompType = CompType.TEXT

    override fun width(): Int {
        val baseWidth = props.text.mcWidth() + props.padding.left + props.padding.right
        return (baseWidth * props.scale.x).toInt()
    }

    override fun height(): Int {
        val baseHeight = 7 + props.padding.top + props.padding.bottom
        return (baseHeight * props.scale.y).toInt()
    }
}

fun TextComponent.opacity(
    /**
     * Opacity between 0-1
     */
    opacity: Float,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR
): OpacityEvent {
    val event = OpacityEvent(
        targetId = this.id,
        delay = 0L,
        opacity = opacity,
        durationSeconds = duration,
        easing = easing
    )
    event.window = this.window
    return event
}
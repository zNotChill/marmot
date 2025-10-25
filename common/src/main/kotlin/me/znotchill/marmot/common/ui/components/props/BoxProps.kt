package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.classes.UIColor

@Serializable
data class BoxProps(
    var color: UIColor = UIColor(255, 255, 255),
    var fillScreen: Boolean = false
) : BaseProps(
    size = Vec2(0f, 0f),
    pos = Vec2(0f, 0f)
)
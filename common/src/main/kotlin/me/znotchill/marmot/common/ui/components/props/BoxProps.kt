package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.classes.Vec2

@Serializable
data class BoxProps(
    var color: UIColor = UIColor(255, 255, 255),
    var fillScreen: Boolean = false
) : BaseProps(
    size = Vec2(0f, 0f),
    pos = Vec2(0f, 0f)
)
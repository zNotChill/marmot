package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.classes.UIColor

@Serializable
data class LineProps(
    var from: Vec2 = Vec2(0f, 0f),
    var to: Vec2 = Vec2(0f, 0f),
    var color: UIColor = UIColor(255, 255, 255),
    var pointSize: Vec2 = Vec2(2f, 2f)
) : BaseProps()
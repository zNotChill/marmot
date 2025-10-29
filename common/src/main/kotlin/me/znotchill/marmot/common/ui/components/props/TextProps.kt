package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.classes.UIColor

@Serializable
open class TextProps(
    var text: String = "",
    var color: UIColor = UIColor(255, 255, 255),
    var shadow: Boolean = false,
    var backgroundColor: UIColor? = null,
    var textScale: Vec2 = Vec2(1f, 1f),
    var backgroundScale: Vec2 = Vec2(1f, 1f)
) : BaseProps()
package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.UIColor

@Serializable
data class TextProps(
    var text: String = "",
    var color: UIColor = UIColor(255, 255, 255),
    var shadow: Boolean = false,
    var backgroundColor: UIColor? = null
) : BaseProps()
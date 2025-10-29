package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.UIColor

@Serializable
open class GradientProps(
    var from: UIColor = UIColor(0, 0, 0),
    var to: UIColor = UIColor(255, 255, 255),
    var fillScreen: Boolean = false
) : BaseProps()
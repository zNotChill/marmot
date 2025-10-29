package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.FillDirection
import me.znotchill.marmot.common.ui.classes.UIColor

@Serializable
open class ProgressBarProps(
    var progress: Float = 0f,
    var fillColor: UIColor? = null,
    var emptyColor: UIColor? = null,
    var fillDirection: FillDirection = FillDirection.RIGHT
) : BaseProps()
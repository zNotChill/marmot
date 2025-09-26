package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.UIColor

@Serializable
data class SpriteProps(
    var texturePath: String = ""
) : BaseProps()
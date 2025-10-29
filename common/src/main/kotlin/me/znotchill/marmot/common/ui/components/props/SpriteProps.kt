package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable

@Serializable
open class SpriteProps(
    var texturePath: String = "",
    var fillScreen: Boolean = false
) : BaseProps()
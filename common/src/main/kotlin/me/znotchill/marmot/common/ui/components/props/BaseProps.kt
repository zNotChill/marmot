package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.Vec2

@Serializable
open class BaseProps(
    var pos: Vec2 = Vec2(0f, 0f),
    var size: Vec2 = Vec2(0f, 0f),
    val anchor: Anchor = Anchor.CENTER_CENTER,
    val rotation: Float = 0f,
    var opacity: Float = 1f,
    val visible: Boolean = true,
    val zIndex: Int = 0,
    val padding: Spacing = Spacing(0),
    val margin: Spacing = Spacing(0),
    var scale: Vec2 = Vec2(1f, 1f)
)
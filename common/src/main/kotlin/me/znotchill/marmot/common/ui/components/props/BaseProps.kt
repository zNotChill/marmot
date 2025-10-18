package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.Vec2

@Serializable
open class BaseProps(
    var pos: Vec2 = Vec2(0f, 0f),
    var size: Vec2 = Vec2(0f, 0f),
    var anchor: Anchor = Anchor.CENTER_CENTER,
    var rotation: Int = 0,
    var opacity: Float = 1f,
    var visible: Boolean = true,
    var zIndex: Int = 0,
    var padding: Spacing = Spacing(0f),
    var margin: Spacing = Spacing(0f),
    var scale: Vec2 = Vec2(1f, 1f)
)
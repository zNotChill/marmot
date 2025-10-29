package me.znotchill.marmot.common.ui.classes

import kotlinx.serialization.Serializable

@Serializable
data class BorderRules(
    var width: Float = 0f,
    var color: UIColor = UIColor(0, 0, 0, 255),
    var position: BorderPosition = BorderPosition.INSIDE
)

@Serializable
enum class BorderPosition {
    INSIDE,
    OUTSIDE
}
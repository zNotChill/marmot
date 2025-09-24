package me.znotchill.marmot.common.ui.classes

import kotlinx.serialization.Serializable

@Serializable
data class UIColor(
    val r: Int,
    val g: Int,
    val b: Int,
    var a: Int = 255
) {
    fun toArgb(): Int =
        (a shl 24) or (r shl 16) or (g shl 8) or b
}
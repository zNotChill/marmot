package me.znotchill.marmot.common.ui.classes

import kotlinx.serialization.Serializable

@Serializable
data class Spacing(
    var left: Float = 0f,
    var top: Float = 0f,
    var right: Float = 0f,
    var bottom: Float = 0f,
    var x: Float = 0f,
    var y: Float = 0f
) {
    init {
        x.let { left = it; right = it }
        y.let { top = it; bottom = it }
    }

    companion object {
        fun uniform(all: Float) = Spacing(all, all, all, all)
        fun horizontal(vertical: Float, horizontal: Float) =
            Spacing(horizontal, vertical, horizontal, vertical)
    }
}
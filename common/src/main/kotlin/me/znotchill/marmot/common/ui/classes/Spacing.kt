package me.znotchill.marmot.common.ui.classes

import kotlinx.serialization.Serializable

@Serializable
data class Spacing(
    var left: Int = 0,
    var top: Int = 0,
    var right: Int = 0,
    var bottom: Int = 0,
    var x: Int? = null,
    var y: Int? = null
) {
    init {
        x?.let { left = it; right = it }
        y?.let { top = it; bottom = it }
    }

    companion object {
        fun uniform(all: Int) = Spacing(all, all, all, all)
        fun horizontal(vertical: Int, horizontal: Int) =
            Spacing(horizontal, vertical, horizontal, vertical)
    }
}
package me.znotchill.marmot.common.ui.classes

import kotlinx.serialization.Serializable

@Serializable
data class Vec2(var x: Float = 0f, var y: Float = 0f) {
    fun lineTo(to: Vec2): List<Vec2> {
        val points = mutableListOf<Vec2>()

        var x1 = x.toInt()
        var y1 = y.toInt()
        val x2 = to.x.toInt()
        val y2 = to.y.toInt()

        // absolute distance between from and to
        val dx = kotlin.math.abs(x2 - x1)
        val dy = kotlin.math.abs(y2 - y1)

        // step direction
        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1
        var err = dx - dy

        // todo: find out if this is dangerous
        while (true) {
            points.add(Vec2(x1.toFloat(), y1.toFloat()))
            if (x1 == x2 && y1 == y2) break
            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x1 += sx
            }
            if (e2 < dx) {
                err += dx
                y1 += sy
            }
        }

        return points
    }
}

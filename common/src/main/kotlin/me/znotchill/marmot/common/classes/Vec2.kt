package me.znotchill.marmot.common.classes

import kotlinx.serialization.Serializable
import org.joml.Vector2f
import org.joml.Vector2fc

@Serializable
data class Vec2(val x: Float = 0f, val y: Float = 0f) {
    fun toJoml(): Vector2f = Vector2f(x, y)
    companion object { fun fromJoml(v: Vector2fc) = Vec2(v.x(), v.y()) }

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
package me.znotchill.marmot.common.classes

import kotlinx.serialization.Serializable
import org.joml.Vector4f
import org.joml.Vector4fc

@Serializable
data class Vec4(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f, val w: Float = 0f) {
    fun toJoml(): Vector4f = Vector4f(x, y, z, w)
    companion object { fun fromJoml(v: Vector4fc) = Vec4(v.x(), v.y(), v.z(), v.w()) }
}
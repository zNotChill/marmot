package me.znotchill.marmot.common.classes

import kotlinx.serialization.Serializable
import org.joml.Vector3f
import org.joml.Vector3fc

@Serializable
data class Vec3(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f) {
    fun toJoml(): Vector3f = Vector3f(x, y, z)
    companion object { fun fromJoml(v: Vector3fc) = Vec3(v.x(), v.y(), v.z()) }
}
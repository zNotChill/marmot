package me.znotchill.marmot.client.payloads

import net.minecraft.util.Identifier

object PayloadList {
    val CAMERA_PACKET_ID: Identifier = Identifier.of("marmot", "camera")
    val CAMERA_LOCK_PACKET_ID: Identifier = Identifier.of("marmot", "camera_lock")
    val MOUSE_PACKET_ID: Identifier = Identifier.of("marmot", "mouse")
}
package me.znotchill.marmot.client.packets.clientbound

import net.minecraft.util.Identifier

object ClientPackets {
    val CAMERA: Identifier = Identifier.of("marmot", "camera")
    val CAMERA_LOCK: Identifier = Identifier.of("marmot", "camera_lock")
    val MOUSE_LOCK: Identifier = Identifier.of("marmot", "mouse_lock")
    val FORCE_KEYBINDS: Identifier = Identifier.of("marmot", "force_keybinds")
    val IS_MARMOT: Identifier = Identifier.of("marmot", "is_marmot")
}
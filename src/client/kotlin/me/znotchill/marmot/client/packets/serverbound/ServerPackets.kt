package me.znotchill.marmot.client.packets.serverbound

import net.minecraft.util.Identifier

object ServerPackets {
    val CLICK_UPDATE: Identifier = Identifier.of("marmot", "click_update")
    val IS_MARMOT: Identifier = Identifier.of("marmot", "is_marmot")
}
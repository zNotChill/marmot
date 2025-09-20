package me.znotchill.marmot.minestom.api

import net.minestom.server.entity.Player

val Player.marmot
    get() = MarmotAPI.getMarmotPlayer(this)
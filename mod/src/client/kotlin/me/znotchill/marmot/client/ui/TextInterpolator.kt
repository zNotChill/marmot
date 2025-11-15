package me.znotchill.marmot.client.ui

import net.minecraft.client.MinecraftClient


object TextInterpolator {
    private val mc get() = MinecraftClient.getInstance()

    private val providers = mutableMapOf<String, () -> String>()

    init {
        register("CLIENT_PING") {
            mc.player?.networkHandler!!.getPlayerListEntry(mc.player!!.uuid)?.latency?.toString() ?: "N/A"
        }
        register("CLIENT_FPS") {
            "${mc.currentFps}"
        }
        register("PLAYER_NAME") {
            mc.player?.name?.string ?: "Unknown"
        }

        register("CLIENT_FPS") { "${mc.currentFps}" }
        register("CLIENT_DIMENSION") { mc.world?.registryKey?.value?.path ?: "Unknown" }
        register("CLIENT_MEMORY") {
            val rt = Runtime.getRuntime()
            val used = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024
            val max = rt.maxMemory() / 1024 / 1024
            "${used}MB/${max}MB"
        }

        register("RANDOM_INT") { (0..9999).random().toString() }
        register("PLAYER_PING_COLORED") {
            val ping = mc.player?.networkHandler?.getPlayerListEntry(mc.player!!.uuid)?.latency ?: -1
            "${getPlayerPingColor()}${ping}"
        }
        register("PLAYER_PING_COLOR") {
            getPlayerPingColor()
        }
    }

    fun getPlayerPingColor(): String {
        val ping = mc.player?.networkHandler?.getPlayerListEntry(mc.player!!.uuid)?.latency ?: -1
        return when {
            ping < 0 -> "N/A"
            ping < 50 -> "§a"
            ping < 150 -> "§e"
            else -> "§c"
        }
    }

    fun register(name: String, provider: () -> String) {
        providers[name.uppercase()] = provider
    }

    fun interpolate(input: String): String {
        var output = input
        for ((key, value) in providers) {
            output = output.replace("{$key}", value())
        }
        return output
    }
}
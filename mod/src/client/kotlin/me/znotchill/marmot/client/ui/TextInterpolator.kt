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
        register("PLAYER_POS_X") {
            mc.player?.x?.toString() ?: "0"
        }
        register("PLAYER_POS_Y") {
            mc.player?.y?.toString() ?: "0"
        }
        register("PLAYER_POS_Z") {
            mc.player?.z?.toString() ?: "0"
        }
        register("PLAYER_POS_YAW") {
            mc.player?.yaw?.toString() ?: "0"
        }
        register("PLAYER_POS_PITCH") {
            mc.player?.pitch?.toString() ?: "0"
        }
        register("PLAYER_WORLD") {
            mc.world?.registryKey?.value?.path ?: "Unknown"
        }
        register("PLAYER_SPEED") {
            mc.player?.velocity?.horizontalLength()?.toString() ?: "0"
        }

        register("PLAYER_DIRECTION") {
            mc.player?.horizontalFacing?.name ?: "Unknown"
        }

        register("PLAYER_BIOME") {
            mc.world?.getBiome(mc.player?.blockPos ?: return@register "Unknown")
                ?.key?.get()?.value?.path ?: "Unknown"
        }

        register("WORLD_TIME_TICKS") {
            mc.world?.time?.toString() ?: "0"
        }

        register("WORLD_IS_RAINING") {
            if (mc.world?.isRaining == true) "true" else "false"
        }
        register("PLAYER_HEALTH") {
            (mc.player?.health?.toDouble() ?: 0.0).toString()
        }

        register("PLAYER_MAX_HEALTH") {
            (mc.player?.maxHealth?.toDouble() ?: 0.0).toString()
        }

        register("PLAYER_HEARTS") {
            val player = mc.player ?: return@register "?"
            val hearts = (player.health / 2).toInt().coerceAtLeast(0)
            "❤".repeat(hearts)
        }

        register("PLAYER_ABSORPTION") {
            mc.player?.absorptionAmount?.toString() ?: "0"
        }

        register("PLAYER_HUNGER") {
            mc.player?.hungerManager?.foodLevel?.toString() ?: "0"
        }

        register("PLAYER_SATURATION") {
            mc.player?.hungerManager?.saturationLevel?.toString() ?: "0"
        }

        register("PLAYER_ARMOR") {
            mc.player?.armor?.toString() ?: "0"
        }

        register("PLAYER_AIR") {
            mc.player?.air?.toString() ?: "0"
        }

        register("PLAYER_ON_GROUND") {
            mc.player?.isOnGround?.toString() ?: "false"
        }

        register("PLAYER_SNEAKING") {
            mc.player?.isSneaking?.toString() ?: "false"
        }

        register("PLAYER_SPRINTING") {
            mc.player?.isSprinting?.toString() ?: "false"
        }

        register("PLAYER_FLYING") {
            (mc.player?.abilities?.flying ?: false).toString()
        }

        register("PLAYER_GAMEMODE") {
            mc.interactionManager?.currentGameMode?.name ?: "Unknown"
        }

        register("PLAYER_ON_FIRE") {
            mc.player?.isOnFire?.toString() ?: "false"
        }

        register("PLAYER_IN_LAVA") {
            mc.player?.isInLava?.toString() ?: "false"
        }

        register("PLAYER_IN_WATER") {
            mc.player?.isTouchingWater?.toString() ?: "false"
        }

        register("TARGET_NAME") {
            val hit = mc.crosshairTarget ?: return@register "None"
            when (hit.type) {
                net.minecraft.util.hit.HitResult.Type.ENTITY -> {
                    (hit as net.minecraft.util.hit.EntityHitResult).entity.name?.string ?: "Unknown"
                }
                net.minecraft.util.hit.HitResult.Type.BLOCK -> "Block"
                else -> "None"
            }
        }

        register("TARGET_HEALTH") {
            val hit = mc.crosshairTarget ?: return@register "0"
            if (hit.type == net.minecraft.util.hit.HitResult.Type.ENTITY) {
                val e = (hit as net.minecraft.util.hit.EntityHitResult).entity
                if (e is net.minecraft.entity.LivingEntity) e.health.toString() else "0"
            } else "0"
        }

        register("TARGET_TYPE") {
            val hit = mc.crosshairTarget ?: return@register "None"
            when (hit.type) {
                net.minecraft.util.hit.HitResult.Type.ENTITY -> "Entity"
                net.minecraft.util.hit.HitResult.Type.BLOCK -> "Block"
                else -> "None"
            }
        }

        register("PLAYER_HELD_ITEM") {
            mc.player?.mainHandStack?.name?.string ?: "Empty"
        }

        register("PLAYER_HELD_ITEM_DURABILITY") {
            val item = mc.player?.mainHandStack ?: return@register "0"
            if (item.isDamageable) (item.maxDamage - item.damage).toString() else "0"
        }

        register("PLAYER_HELD_ITEM_MAX_DURABILITY") {
            mc.player?.mainHandStack?.maxDamage?.toString() ?: "0"
        }

        register("PLAYER_OFFHAND_ITEM") {
            mc.player?.offHandStack?.name?.string ?: "Empty"
        }

        register("PLAYER_OFFHAND_ITEM_DURABILITY") {
            val item = mc.player?.offHandStack ?: return@register "0"
            if (item.isDamageable) (item.maxDamage - item.damage).toString() else "0"
        }

        register("PLAYER_OFFHAND_ITEM_MAX_DURABILITY") {
            mc.player?.offHandStack?.maxDamage?.toString() ?: "0"
        }

        register("PLAYER_YAW_CARDINAL_8") {
            val yaw = mc.player?.yaw ?: return@register "?"
            val dir = (((yaw % 360) + 360) % 360)
            when {
                dir < 22.5 -> "S"
                dir < 67.5 -> "SW"
                dir < 112.5 -> "W"
                dir < 157.5 -> "NW"
                dir < 202.5 -> "N"
                dir < 247.5 -> "NE"
                dir < 292.5 -> "E"
                dir < 337.5 -> "SE"
                else -> "S"
            }
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

        // {NAME} or {NAME:R2} (R = round)
        val regex = Regex("\\{([A-Z_]+)(?::R(\\d+))?}")

        output = regex.replace(output) { match ->
            val key = match.groupValues[1]
            val rounding = match.groupValues[2]

            val provider = providers[key] ?: return@replace match.value

            val value = provider()

            // no rounding requested, return normal value
            if (rounding.isEmpty()) return@replace value

            val digits = rounding.toIntOrNull() ?: return@replace value
            val number = value.toDoubleOrNull() ?: return@replace value

            "%.${digits}f".format(number)
        }

        return output
    }

}
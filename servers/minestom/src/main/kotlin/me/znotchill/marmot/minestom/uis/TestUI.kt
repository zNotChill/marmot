package me.znotchill.marmot.minestom.uis

import me.znotchill.blossom.sound.sound
import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.components.bottomOf
import me.znotchill.marmot.common.ui.components.move
import me.znotchill.marmot.common.ui.components.opacity
import me.znotchill.marmot.common.ui.components.schedule
import me.znotchill.marmot.common.ui.components.topOf
import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.Player
import kotlin.collections.forEach


class MapChangeUI : MarmotUI("map_change") {
    fun new(
        /**
         * Load time in ticks
         */
        loadTime: Long = 60,
        /**
         * Lag time in ticks
         */
        lagTime: Long = 5,
        backgroundFadeTime: Long = 10,
        swipeInSound: Sound = sound("entity.breeze.shoot"),
        swipeOutSound: Sound = sound("entity.breeze.land"),
        completeSound: Sound = sound("entity.experience_orb.pickup"),

        players: List<Player>
    ) {
        group("group") {
            val background = box("background") {
                fillScreen = true
                anchor = Anchor.TOP_LEFT
                color = UIColor(0, 0, 0)
                opacity = 0f
            }
            val mapName = text("map_name") {
                text = "byue bye"
                scale = Vec2(1.5f, 1.5f)
                shadow = true
                anchor = Anchor.TOP_LEFT
                opacity = 1f
                zIndex = 1000
                margin = Spacing(x = 20f, y = 20f)
            }
            val mapChanging = text("map_changing") {
                text = "LOADING MAP..."
                color = UIColor(0, 0, 0)
                backgroundColor = UIColor(255, 255, 255)
                opacity = 1f
                zIndex = 1000
                padding = Spacing(
                    x = 3f, y = 3f
                )
            } topOf mapName
            val map = text("map") {
                text = "ID: hi"
                color = UIColor(230, 230, 230)
                shadow = true
                opacity = 0f
                zIndex = 100
            } bottomOf mapName

            background.schedule(0) {
                players.forEach { it.playSound(swipeInSound) }
                listOf(
                    opacity(1f, 0.25)
                )
            }

            map.schedule(backgroundFadeTime) {
                listOf(
                    opacity(1f, 0.0),
                    move(
                        Vec2(20f, 0f),
                        1.0,
                        easing = Easing.EASE_OUT_BACK
                    )
                )
            }
            mapName.schedule(0) {
                listOf(
                    opacity(1f, 1.5),
                    move(
                        Vec2(20f, 00f),
                        1.0,
                        easing = Easing.EASE_OUT_BACK
                    )
                )
            }

            mapName.schedule(loadTime) {
                players.forEach { it.playSound(swipeOutSound) }
                players.forEach { it.playSound(completeSound) }
                background.schedule(backgroundFadeTime) {
                    listOf(
                        opacity(0f, 0.25)
                    )
                }
                map.schedule(lagTime) {
                    listOf(
                        opacity(0f, 0.5),
                        move(
                            Vec2(-200f, 0f),
                            1.0,
                            easing = Easing.EASE_OUT_BACK
                        )
                    )
                }
                listOf(
                    opacity(0f, 0.0),
                    move(
                        Vec2(-200f, 0f),
                        1.0,
                        easing = Easing.EASE_OUT_BACK
                    )
                )
            }
        }
    }
}
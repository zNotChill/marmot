package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.rightOf
import me.znotchill.marmot.common.ui.components.rotate
import me.znotchill.marmot.common.ui.components.schedule

class TestUI : MarmotUI("kill_feed") {
    fun newKill(
        killer: String,
        victim: String,
        method: String
    ) {
        group("test_group") {
            val test1 = sprite("test_sprite") {
                texturePath = "textures/item/black_candle.png"
                size = Vec2(32f, 32f)
                pos = Vec2(20f, 20f)
            }
            val test2 = text("test_text") {
                text = "test test test"
            } rightOf test1

            test1.schedule(20) {
                listOf(
                    rotate(360, 0.5)
                )
            }
            test2.schedule(20) {
                listOf(
                    rotate(-360, 0.5)
                )
            }
        }
    }
}
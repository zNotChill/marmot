package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.move
import me.znotchill.marmot.common.ui.components.rightOf
import me.znotchill.marmot.common.ui.components.schedule

class TestUI : MarmotUI("kill_feed") {
    fun newKill(
        killer: String,
        victim: String,
        method: String
    ) {
        val group = group("die") {
            val test = text("hi") {
                text = "sigma"
                color = UIColor(134, 196, 207)
                shadow = true
                pos = Vec2(10f, 10f)
                backgroundColor = UIColor(255, 255, 255)
            }

            test.schedule(20) {
                println("HELLO")
                move(Vec2(0f, -100f), 0.5)
            }

            text("l") {
                text = "sigma2"
                color = UIColor(134, 196, 207)
                shadow = true
                backgroundColor = UIColor(255, 255, 255)
            } rightOf test

        }
    }
}
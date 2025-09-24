package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.rightOf

class TestUI : MarmotUI() {
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
            text("l") {
                text = "sigma2"
                color = UIColor(134, 196, 207)
                shadow = true
                backgroundColor = UIColor(255, 255, 255)
            } rightOf test
        }
    }
}
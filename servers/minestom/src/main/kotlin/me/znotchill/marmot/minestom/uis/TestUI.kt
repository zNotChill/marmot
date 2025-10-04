package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.leftOf

class TestUI : MarmotUI("kill_feed") {
    fun newKill(
        killer: String,
        victim: String,
        method: String
    ) {
//        group("test_group") {
//            backgroundColor = UIColor(150, 150, 150)
//            padding = Spacing(x = 5)
//
//            val victim = text("victim") {
//                text = victim
//                color = UIColor(132, 194, 205)
//                shadow = true
//                anchor = Anchor.TOP_RIGHT
//                pos = Vec2(10f, 10f)
//                padding = Spacing(
//                    y = 5
//                )
//            }
//
//            val method = text("method") {
//                text = method
//                shadow = false
//                padding = Spacing(
//                    x = 5, y = 5
//                )
//            } leftOf victim
//
//            text("killer") {
//                text = killer
//                color = UIColor(236, 125, 107)
//                shadow = true
//                padding = Spacing(
//                    y = 5
//                )
//            } leftOf method
//        }
        group("test") {
            gradient("test_gradient") {
                pos = Vec2(50f, 50f)
                size = Vec2(50f, 100f)
            }
            line("test_line") {
                from = Vec2(0f, 0f)
                to = Vec2(20f, 50f)
                pointSize = Vec2(1f, 1f)
                anchor = Anchor.CENTER_CENTER
            }
        }
    }
}
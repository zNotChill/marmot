package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.padding
import me.znotchill.marmot.common.ui.components.relative
import me.znotchill.marmot.common.ui.components.schedule


class NewTestUI : MarmotUI("test_ui_2") {
    fun new() {
        group("group") {
            val background = box("background") {
                anchor = Anchor.CENTER_CENTER
                color = UIColor(0, 0, 0)
                opacity = 1f
                size = Vec2(300f, 150f)
            }

            val test = text("test") {
                anchor = Anchor.BOTTOM_LEFT
                color = UIColor(255, 255, 255)
                text = "Test"
                padding = Spacing(
                    x = 2f, y = 2f
                )
                scale = Vec2(3f, 3f)
            } relative background

            test.schedule(60) {
                listOf(
                    padding(
                        padding = Spacing(
                            x = 10f,
                            y = 10f
                        ),
                        duration = 0.5,
                        easing = Easing.EASE_IN_EXPO
                    )
                )
            }
        }
    }
}
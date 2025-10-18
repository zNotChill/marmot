package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.relative


class NewTestUI : MarmotUI("test_ui_2") {
    fun new() {
        group("group") {
            val background = box("background") {
                anchor = Anchor.CENTER_CENTER
                color = UIColor(0, 0, 0)
                opacity = 1f
                size = Vec2(300f, 150f)
            }

            text("test") {
                anchor = Anchor.BOTTOM_LEFT
                color = UIColor(255, 255, 255)
                text = "Test"
            } relative background
        }
    }
}
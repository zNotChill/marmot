package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.components.bottomOf
import me.znotchill.marmot.common.ui.components.leftOf
import me.znotchill.marmot.common.ui.components.rightOf
import me.znotchill.marmot.common.ui.components.topOf


class NewTestUI : MarmotUI("test_ui_2") {
    fun new() {
        group() {
            val test = text() {
                anchor = Anchor.TOP_RIGHT
                text = "LOADING MAP..."
                color = UIColor(0, 0, 0)
                backgroundColor = UIColor(255, 255, 255)
                padding = Spacing(x = 5f, y = 5f)
                margin = Spacing(x = 20f, y = 20f)
            }

            text() {
                text = "noorfb"
            } bottomOf test

            text() {
                text = "noorfb"
            } rightOf test

            text() {
                text = "noorfb"
            } topOf test

            text() {
                text = "noorfb"
            } leftOf test
        }
    }
}
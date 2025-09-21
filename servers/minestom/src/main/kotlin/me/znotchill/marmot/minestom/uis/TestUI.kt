package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.Spacing
import me.znotchill.marmot.common.ui.UIColor
import me.znotchill.marmot.common.ui.leftOf

class TestUI : MarmotUI() {
    init {
        val victimLabel = text("znci") {
            x = 10f
            y = 10f
            id = "victim"
            color = UIColor(134, 196, 207)
            padding = Spacing(
                y = 3
            )
            shadow = true
            anchor = Anchor.TOP_RIGHT
        }

        val killLabel = text("killed") {
            id = "killed"
            color = UIColor(255, 255, 255)
            padding = Spacing(
                x = 5, y = 3
            )
            anchor = Anchor.TOP_RIGHT
        } leftOf victimLabel

        val attackerLabel = text("Xx_KolojaProNoob_xX") {
            id = "player"
            color = UIColor(239, 127, 108)
            shadow = true
            padding = Spacing(
                x = 5, y = 3
            )
            anchor = Anchor.TOP_RIGHT
        } leftOf killLabel

        val group = group {
            id = "killfeed_group"
            backgroundColor = UIColor(100, 100, 100)
            padding = Spacing(
                x = 5, y = 3
            )
            anchor = Anchor.TOP_RIGHT
        }
        group.add(victimLabel, killLabel, attackerLabel)
        add(group)
    }
}
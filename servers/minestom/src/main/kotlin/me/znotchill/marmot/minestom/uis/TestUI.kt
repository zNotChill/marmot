package me.znotchill.marmot.minestom.uis

import me.znotchill.marmot.common.ui.Anchor
import me.znotchill.marmot.common.ui.MarmotUI

class TestUI : MarmotUI() {
    fun newKill(
        killer: String,
        victim: String,
        method: String
    ) {
        widget(
            KillFeedWidget(
                "ZnCi",
                "Noob",
                "knifed"
            ),
            y = 20,
            anchor = Anchor.TOP_RIGHT
        )
    }
}
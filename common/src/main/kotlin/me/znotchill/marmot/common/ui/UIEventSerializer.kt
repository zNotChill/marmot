package me.znotchill.marmot.common.ui

import me.znotchill.marmot.common.ui.events.UIEvent

object UIEventSerializer {
    fun encode(events: List<UIEvent>): String {
        return JsonUtil.json.encodeToString(events)
    }
}
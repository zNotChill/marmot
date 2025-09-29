package me.znotchill.marmot.client.ui.events

import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.events.UIEvent

interface UIEventHandler<E : UIEvent> {
    fun handle(event: E, component: Component, context: UIEventContext)
}
package me.znotchill.marmot.client.ui.events

import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.events.DestroyEvent

class DestroyEventHandler : UIEventHandler<DestroyEvent> {
    override fun handle(event: DestroyEvent, component: Component, context: UIEventContext) {
        context.currentWindow()?.components?.remove(event.targetId)
    }
}
package me.znotchill.marmot.client.ui.events

import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.events.PaddingEvent
import me.znotchill.marmot.common.ui.events.PropertyAnimation

class PaddingEventHandler : UIEventHandler<PaddingEvent> {
    override fun handle(event: PaddingEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { component.props.padding },
            setter = { c, value -> c.props.padding = value },
            from = null,
            to = event.padding,
            durationSeconds = event.durationSeconds,
            easing = event.easing.toString()
        )
        anim.window = event.window
        context.enqueueAnimation(anim)
    }
}
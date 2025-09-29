package me.znotchill.marmot.client.ui.events

import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.events.OpacityEvent
import me.znotchill.marmot.common.ui.events.PropertyAnimation

class OpacityEventHandler : UIEventHandler<OpacityEvent> {
    override fun handle(event: OpacityEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { component.props.opacity },
            setter = { c, value -> c.props.opacity = value },
            from = null,
            to = event.opacity,
            durationSeconds = event.durationSeconds,
            easing = event.easing.toString()
        )
        anim.window = event.window
        context.enqueueAnimation(anim)
    }
}
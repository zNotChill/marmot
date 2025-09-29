package me.znotchill.marmot.client.ui.events

import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.PropertyAnimation

class MoveEventHandler : UIEventHandler<MoveEvent> {
    override fun handle(event: MoveEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { component.props.pos },
            setter = { c, value -> c.props.pos = value },
            from = null,
            to = event.to,
            durationSeconds = event.durationSeconds,
            easing = event.easing.toString()
        )
        anim.window = event.window
        context.enqueueAnimation(anim)
    }
}
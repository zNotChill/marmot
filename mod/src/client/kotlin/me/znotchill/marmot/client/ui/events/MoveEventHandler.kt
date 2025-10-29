package me.znotchill.marmot.client.ui.events

import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.PropertyAnimation

class MoveEventHandler : UIEventHandler<MoveEvent> {
    override fun handle(event: MoveEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.pos },
            setter = { c, value ->
                var x = value.x
                var y = value.y
                if (value.x == 0f) x = c.props.pos.x
                if (value.y == 0f) y = c.props.pos.y

                c.props.pos = Vec2(x, y)
            },
            from = null,
            to = event.position,
            durationSeconds = event.durationSeconds,
            easing = event.easing.toString()
        )
        anim.window = event.window
        context.enqueueAnimation(anim)
    }
}
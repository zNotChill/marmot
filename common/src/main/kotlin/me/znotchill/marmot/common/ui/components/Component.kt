package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.ui.UIEventQueue
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.props.BaseProps
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.UIEvent

@Serializable
sealed class Component {
    @Transient
    lateinit var window: UIWindow

    var id: String = ""
    var relativeTo: String? = null
    var relativePosition: RelativePosition? = null
    abstract val compType: CompType
    abstract val props: BaseProps

    abstract fun width(): Int
    abstract fun height(): Int

    @Transient
    var screenX: Int = 0
    @Transient
    var screenY: Int = 0
    @Transient
    var computedScale: Vec2 = Vec2(0f, 0f)
}

fun Component.move(to: Vec2, duration: Double, easing: String = "linear"): MoveEvent {
    val event = MoveEvent(
        targetId = this.id,
        delay = 0L,
        to = to,
        durationSeconds = duration,
        easing = easing
    )
    event.window = this.window
    return event
}

fun <T : Component> T.schedule(delay: Long, block: T.() -> UIEvent) {
    UIEventQueue.enqueueDelayed(delay) {
        this.block()
    }
}

infix fun Component.relative(component: Component): Component {
    this.relativeTo = component.id
    return this
}

infix fun Component.rightOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.RIGHT_OF
    return this
}

infix fun Component.leftOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.LEFT_OF
    return this
}

infix fun Component.topOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.ABOVE
    return this
}

infix fun Component.bottomOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.BELOW
    return this
}
package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.UIEventQueue
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.components.props.BaseProps
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.OpacityEvent
import me.znotchill.marmot.common.ui.events.PaddingEvent
import me.znotchill.marmot.common.ui.events.RotateEvent

@Serializable
sealed class Component {
    @Transient
    lateinit var window: UIWindow

    var id: String = ""
    var relativeTo: String? = null
    var relativePosition: RelativePosition? = null
    abstract val compType: CompType
    abstract val props: BaseProps

    abstract fun width(): Float
    abstract fun height(): Float

    @Transient
    var screenX: Int = 0
    @Transient
    var screenY: Int = 0
    @Transient
    var computedScale: Vec2 = Vec2(0f, 0f)
    @Transient
    var computedSize: Vec2? = null
}

fun Component.move(
    to: Vec2,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): MoveEvent {
    val event = MoveEvent(
        targetId = this.id,
        delay = 0L,
        position = to,
        durationSeconds = duration,
        easing = easing
    ).also { it.window = this.window }

    UIEventQueue.enqueueNow(event)
    return event
}

fun Component.rotate(
    rotation: Int,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): RotateEvent {
    val event = RotateEvent(
        targetId = this.id,
        delay = 0L,
        rotation = rotation,
        durationSeconds = duration,
        easing = easing
    ).also { it.window = this.window }

    UIEventQueue.enqueueNow(event)
    return event
}


fun Component.opacity(
    /**
     * Opacity between 0-1
     */
    opacity: Float,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): OpacityEvent {
    val event = OpacityEvent(
        targetId = this.id,
        delay = 0L,
        opacity = opacity,
        durationSeconds = duration,
        easing = easing
    ).also { it.window = this.window }

    UIEventQueue.enqueueNow(event)
    return event
}

fun Component.padding(
    padding: Spacing,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): PaddingEvent {
    val event = PaddingEvent(
        targetId = this.id,
        delay = 0L,
        padding = padding,
        durationSeconds = duration,
        easing = easing
    ).also { it.window = this.window }

    UIEventQueue.enqueueNow(event)
    return event
}

fun <T : Component> T.schedule(delay: Long, block: T.() -> Unit) {
    UIEventQueue.enqueueDelayed(delay) {
        this.block()
    }
}

infix fun Component.relative(component: Component): Component {
    this.relativeTo = component.id
    return this
}

infix fun <T : Component> T.rightOf(component: Component): T {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.RIGHT_OF
    return this
}

infix fun <T : Component> T.leftOf(component: Component): T {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.LEFT_OF
    return this
}

infix fun <T : Component> T.topOf(component: Component): T {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.ABOVE
    return this
}

infix fun <T : Component> T.bottomOf(component: Component): T {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.BELOW
    return this
}

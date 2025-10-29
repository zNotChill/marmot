package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.UIEventQueue
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.components.props.ProgressBarProps
import me.znotchill.marmot.common.ui.events.ProgressEvent

@Serializable
@SerialName("progress_bar")
open class ProgressBar(
    override val props: ProgressBarProps
) : Component() {
    override val compType: CompType = CompType.PROGRESS_BAR

    override fun width(): Float {
        return props.size.x
    }

    override fun height(): Float {
        return props.size.y
    }
}

fun ProgressBar.progress(
    progress: Float,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): ProgressEvent {
    val event = ProgressEvent(
        targetId = this.name,
        delay = delay,
        progress = progress,
        durationSeconds = duration,
        easing = easing
    ).also { it.window = this.window }

    UIEventQueue.enqueueNow(event)
    return event
}
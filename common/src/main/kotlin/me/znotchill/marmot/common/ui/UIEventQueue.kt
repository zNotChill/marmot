package me.znotchill.marmot.common.ui

import me.znotchill.marmot.common.ui.events.UIEvent

object UIEventQueue {
    private val tasks = mutableListOf<ScheduledTask>()
    private var tick = 0L
    private val readyEvents = mutableListOf<UIEvent>()

    fun enqueueDelayed(delay: Long, block: () -> Unit) {
        tasks += ScheduledTask(tick + delay, block)
    }

    fun enqueueNow(event: UIEvent) {
        readyEvents += event
    }

    fun tick(): List<UIEvent> {
        tick++
        val due = tasks.filter { it.dueTick <= tick }
        tasks.removeAll(due)

        due.forEach { it.block() }

        val events = readyEvents.toList()
        readyEvents.clear()
        return events
    }

    private data class ScheduledTask(
        val dueTick: Long,
        val block: () -> Unit
    )
}
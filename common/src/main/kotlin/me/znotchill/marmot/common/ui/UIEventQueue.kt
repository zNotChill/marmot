package me.znotchill.marmot.common.ui

import me.znotchill.marmot.common.ui.events.UIEvent

object UIEventQueue {
    private val tasks = mutableListOf<ScheduledTask>()
    private var tick = 0L
    private val readyEvents = mutableListOf<UIEvent>()

    fun enqueueDelayed(delay: Long, block: () -> UIEvent) {
        tasks += ScheduledTask(tick + delay, block)
    }

    fun tick(): List<UIEvent> {
        tick++
        val due = tasks.filter { it.dueTick <= tick }
        tasks.removeAll(due)

        // run tasks and enqueue results
        due.forEach { task ->
            readyEvents += task.block()
        }

        val events = readyEvents.toList()
        readyEvents.clear()
        return events
    }

    private data class ScheduledTask(
        val dueTick: Long,
        val block: () -> UIEvent
    )
}
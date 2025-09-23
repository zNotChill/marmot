package me.znotchill.marmot.common.ui

import kotlinx.serialization.Polymorphic

open class MarmotUI {
    private val components = mutableListOf<@Polymorphic UIComponent>()
    private val widgets = mutableListOf<@Polymorphic UIWidget>()

    private val queuedUpdates = mutableListOf<QueuedUIUpdate>()

    fun build(): UIWindow = UIWindow(components)

    fun widget(widget: UIWidget, x: Int = 0, y: Int = 0, anchor: Anchor): UIComponentManager {
        widget.x = x
        widget.y = y
        val builtWidget = widget.build()

        builtWidget.forEach { component ->
            components.add(component)
        }

        return UIComponentManager(widget)
    }

    /**
     * Add a vararg [UIComponent] to the window.
     */
    fun add(vararg componentList: UIComponent) {
        componentList.forEach {
            components.add(it)
        }
    }
}
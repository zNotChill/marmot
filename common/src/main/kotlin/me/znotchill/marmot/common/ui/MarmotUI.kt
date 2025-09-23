package me.znotchill.marmot.common.ui

import kotlinx.serialization.Polymorphic

open class MarmotUI {
    private val components = mutableListOf<@Polymorphic UIComponent>()
    private val widgets = mutableListOf<@Polymorphic UIWidget>()

    private val queuedUpdates = mutableListOf<QueuedUIUpdate>()

    fun build(): UIWindow = UIWindow(components)

    fun widget(widget: UIWidget, x: Int = 0, y: Int = 0, anchor: Anchor) {
        val builtWidget = widget.build()

        builtWidget.forEach { component ->
            if (component is UIGroup) {
                component.x = x
                component.y = y
                component.anchor = anchor
            }
            components.add(component)
        }
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
package me.znotchill.marmot.common.ui

import kotlinx.serialization.Polymorphic
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.GroupComponent
import me.znotchill.marmot.common.ui.dsl.GroupBuilder

open class MarmotUI(val id: String) {
    private val components = mutableListOf<@Polymorphic Component>()

    fun build(): UIWindow {
        val window = UIWindow(id)
        components.forEach {
            window.add(it)
        }
        return window
    }

    fun group(
        id: String,
        init: GroupBuilder.() -> Unit
    ) {
        val builder = GroupBuilder(build())
        builder.init()
        val group = builder.build()

        builder.groupProps.components = group
        components.add(
            GroupComponent(
                props = builder.groupProps
            )
        )
    }

//    fun widget(widget: UIWidget, x: Int = 0, y: Int = 0, anchor: Anchor): UIComponentManager {
//        widget.x = x
//        widget.y = y
//        val builtWidget = widget.build()
//
//        builtWidget.forEach { component ->
//            components.add(component)
//        }
//
//        return UIComponentManager(widget)
//    }

    /**
     * Add a vararg [Component] to the window.
     */
    fun add(vararg componentList: Component) {
        componentList.forEach {
            components.add(it)
        }
    }
}
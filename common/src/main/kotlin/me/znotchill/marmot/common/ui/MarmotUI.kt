package me.znotchill.marmot.common.ui

import kotlinx.serialization.Polymorphic
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.dsl.GroupBuilder

open class MarmotUI {
    private val components = mutableListOf<@Polymorphic Component>()

    fun build(): UIWindow {
        val window = UIWindow()
        components.forEach {
            window.add(it)
        }
        return window
    }

    fun tick() {
        components.forEach { component ->
        }
    }

    fun group(
        id: String,
        init: GroupBuilder.() -> Unit
    ) {
        val builder = GroupBuilder()
        builder.init()
        val built = builder.build()

        built.forEach { comp -> components.add(comp) }
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
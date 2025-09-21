package me.znotchill.marmot.common.ui

import kotlinx.serialization.Polymorphic

open class MarmotUI {
    private val components = mutableListOf<@Polymorphic UIComponent>()

    fun text(value: String, block: UIText.() -> Unit): UIText {
        val text = UIText(value)
        text.block()
        return text
    }

    fun box(block: UIBox.() -> Unit): UIBox {
        val box = UIBox(0, 0, UIColor(255, 255, 255))
        box.block()
        return box
    }

    fun group(block: UIGroup.() -> Unit): UIGroup {
        val group = UIGroup().apply(block)
        return group
    }

    fun build(): UIWindow = UIWindow(components)

    /**
     * Add a vararg [UIComponent] to the window.
     */
    fun add(vararg componentList: UIComponent) {
        componentList.forEach {
            components.add(it)
        }
    }
}
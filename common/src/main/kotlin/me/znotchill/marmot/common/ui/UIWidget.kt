package me.znotchill.marmot.common.ui

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
abstract class UIWidget(
    val name: String,
) {
    val id: String
        get() = "${name}_${UUID.randomUUID()}"

    abstract fun build(): List<UIComponent>

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
}
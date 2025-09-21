package me.znotchill.marmot.common.ui

import kotlinx.serialization.Serializable
import kotlinx.serialization.*
import java.util.UUID

@Serializable
enum class RelativePosition {
    RIGHT_OF, LEFT_OF, ABOVE, BELOW
}

@Serializable
data class UIColor(val r: Int, val g: Int, val b: Int, val a: Int = 255)

@Serializable
data class Spacing(
    var left: Int = 0,
    var top: Int = 0,
    var right: Int = 0,
    var bottom: Int = 0,
    var x: Int? = null,
    var y: Int? = null
) {
    init {
        x?.let { left = it; right = it }
        y?.let { top = it; bottom = it }
    }

    companion object {
        fun uniform(all: Int) = Spacing(all, all, all, all)
        fun horizontal(vertical: Int, horizontal: Int) =
            Spacing(horizontal, vertical, horizontal, vertical)
    }
}

@Serializable
@Polymorphic
sealed class UIComponent() {
    var x: Float = 0f
    var y: Float = 0f
    var anchor: Anchor = Anchor.CENTER_CENTER
    var id: String = UUID.randomUUID().toString()

    var relativeTo: String? = null
    var relativePosition: RelativePosition? = null
    var padding: Spacing = Spacing()
    var margin: Spacing = Spacing()

    var screenX: Int = 0
    var screenY: Int = 0

    abstract fun width(): Int
    abstract fun height(): Int
}

infix fun UIComponent.relative(component: UIComponent): UIComponent {
    this.relativeTo = component.id
    return this
}

infix fun UIComponent.rightOf(component: UIComponent): UIComponent {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.RIGHT_OF
    return this
}

infix fun UIComponent.leftOf(component: UIComponent): UIComponent {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.LEFT_OF
    return this
}

infix fun UIComponent.topOf(component: UIComponent): UIComponent {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.ABOVE
    return this
}

infix fun UIComponent.bottomOf(component: UIComponent): UIComponent {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.BELOW
    return this
}

@Serializable
@SerialName("text")
data class UIText(
    var text: String,
    var shadow: Boolean = false,
    var color: UIColor = UIColor(255, 255, 255),
    var backgroundColor: UIColor? = null,
) : UIComponent() {
    override fun width(): Int {
        return text.mcWidth() + padding.left + padding.right
    }

    override fun height(): Int {
        // todo: not hardcode this
        return padding.top + padding.bottom + 7
    }
}

@Serializable
@SerialName("box")
data class UIBox(
    var width: Int,
    var height: Int,
    var color: UIColor
) : UIComponent() {
    override fun width(): Int {
        return width + padding.left + padding.right
    }

    override fun height(): Int {
        return height + padding.top + padding.bottom
    }
}

@Serializable
@SerialName("group")
data class UIGroup(
    var backgroundColor: UIColor? = null,
    var width: Int = 0,
    var height: Int = 0,
) : UIComponent() {

    val children = mutableListOf<UIComponent>()

    fun add(vararg components: UIComponent): UIGroup {
        children.addAll(components)
        return this
    }

    override fun width(): Int {
        val childWidths = children.map { it.width() + it.x.toInt() }
        return (childWidths.maxOrNull() ?: 0) + padding.left + padding.right
    }

    override fun height(): Int {
        val childHeights = children.map { it.height() + it.y.toInt() }
        return (childHeights.maxOrNull() ?: 0) + padding.top + padding.bottom
    }
}

@Serializable
data class UIWindow(
    val components: MutableList<UIComponent> = mutableListOf()
) {
    fun encode(): String = JsonUtil.json.encodeToString(this)

    fun getComponentById(id: String?): UIComponent? {
        return components.find { it.id == id }
    }

    fun getComponentByIdDeep(id: String?): UIComponent? {
        fun search(list: List<UIComponent>): UIComponent? {
            for (c in list) {
                if (c.id == id) return c
                if (c is UIGroup) {
                    search(c.children)?.let { return it }
                }
            }
            return null
        }
        return id?.let { search(components) }
    }

    companion object {
        fun decode(json: String): UIWindow =
            JsonUtil.json.decodeFromString(json)
    }
}

package me.znotchill.marmot.common.ui

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import me.znotchill.marmot.common.ui.classes.UIOperation
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.Group

@Serializable
class UIWindow(val id: String) {
    var components = mutableMapOf<String, Component>()
    private val previousState = mutableMapOf<String, Component>()

    fun add(component: Component) {
        val key = componentKey(component)
        components[key] = component
    }

    fun remove(id: String) {
        components.remove(id)
    }

    fun update(id: String, props: Map<String, JsonElement>) {
        val comp = components[id] ?: return
        val newPropsJson = flattenProps(comp).toMutableMap()
        newPropsJson.putAll(props)
        components[id] = comp
    }

    fun diff(): List<UIOperation> {
        val ops = mutableListOf<UIOperation>()

        for ((id, comp) in components) {
            val old = previousState[id]
            if (old == null) {
                ops += UIOperation.Create(comp)
            } else {
                val oldProps = flattenProps(old)
                val newProps = flattenProps(comp)
                val changed = newProps.filter { (k, v) -> oldProps[k] != v }
                if (changed.isNotEmpty()) {
                    ops += UIOperation.Update(id, changed)
                }
            }
        }

        for (id in previousState.keys) {
            if (id !in components) {
                ops += UIOperation.Remove(id)
            }
        }

        previousState.clear()
        previousState.putAll(components)

        return ops
    }

    private fun flattenProps(comp: Component): Map<String, JsonElement> {
        val json = Json.encodeToJsonElement(comp.props) as JsonObject
        return json.toMap()
    }

    private fun componentKey(comp: Component): String = comp.internalId

    fun getComponentById(id: String?): Component? {
        return components[id]
    }

    /**
     * Perform a recursive search for a component by its ID.
     */
    fun getComponentByIdDeep(id: String): Component? {
        fun search(list: List<Component>): Component? {
            for (c in list) {
                if (c.internalId == id) return c
                if (c is Group) {
                    search(c.props.components)?.let { return it }
                }
            }
            return null
        }

        return search(components.values.toList())
    }

    /**
     * This is like the easiest solution I could come up with
     * Converts the window to a JSON string, then re-encodes it back to a new UIWindow instance
     */
    fun deepCopy(): UIWindow = JsonUtil.json.decodeFromString(this.encode())

    fun encode(): String = JsonUtil.json.encodeToString(this)
    companion object {
        fun decode(json: String): UIWindow =
            JsonUtil.json.decodeFromString(json)
    }
}

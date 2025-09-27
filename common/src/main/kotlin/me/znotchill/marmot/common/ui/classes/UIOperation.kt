package me.znotchill.marmot.common.ui.classes

import me.znotchill.marmot.common.ui.components.Component
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
sealed class UIOperation {
    @Serializable
    data class Create(val component: Component) : UIOperation()

    @Serializable
    data class Update(
        val id: String,
        val props: Map<String, JsonElement>
    ) : UIOperation()

    @Serializable
    data class Remove(val id: String) : UIOperation()
}
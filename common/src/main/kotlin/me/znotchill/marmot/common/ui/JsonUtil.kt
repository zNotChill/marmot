package me.znotchill.marmot.common.ui

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import me.znotchill.marmot.common.ui.components.Component

object JsonUtil {
    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(Component::class) {
            }
        }
        classDiscriminator = "type"
        encodeDefaults = true
    }
}

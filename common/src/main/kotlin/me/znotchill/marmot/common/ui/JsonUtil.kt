package me.znotchill.marmot.common.ui

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

object JsonUtil {
    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(UIComponent::class) {
                subclass(UIText::class)
                subclass(UIBox::class)
                subclass(UIGroup::class)
            }
        }
        classDiscriminator = "type"
        encodeDefaults = true
    }
}

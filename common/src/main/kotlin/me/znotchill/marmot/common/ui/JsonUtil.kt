package me.znotchill.marmot.common.ui

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

object JsonUtil {
    val json = Json {
        serializersModule = SerializersModule {
//            polymorphic(UIComponent::class) {
//                subclass(UIText::class)
//                subclass(UIBox::class)
//                subclass(UIGroup::class)
//                subclass(UIWidget::class)
//            }
        }
        classDiscriminator = "type"
        encodeDefaults = true
    }
}

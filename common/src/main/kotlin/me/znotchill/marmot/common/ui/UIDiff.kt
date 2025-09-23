package me.znotchill.marmot.common.ui

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class UIDiff {
    val id: String = UUID.randomUUID().toString()
    @Serializable data class Add(val component: UIComponent) : UIDiff()
    @Serializable data class Update(val component: UIComponent) : UIDiff()
    @Serializable data class Remove(val componentId: String) : UIDiff()
    @Serializable data class Full(val window: UIWindow) : UIDiff()
}
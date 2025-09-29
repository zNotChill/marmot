package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.common.ui.components.Component
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

interface UIComponent<C : Component> {
    fun draw(component: C, context: DrawContext, instance: MinecraftClient)
}
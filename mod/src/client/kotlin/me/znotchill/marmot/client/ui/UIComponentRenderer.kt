package me.znotchill.marmot.client.ui

import me.znotchill.marmot.client.MarmotClient
import me.znotchill.marmot.client.ui.components.UIComponent
import me.znotchill.marmot.common.ui.components.Component
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * Renders [Component]s to their corresponding [UIComponent] implementations
 * to be rendered on the screen.
 */
class UIComponentRenderer(
    private val handlers: Map<Class<out Component>, UIComponent<out Component>>
) {
    fun draw(component: Component, context: DrawContext, instance: MinecraftClient) {
        val handler = handlers[component::class.java]
        if (handler != null) {
            @Suppress("UNCHECKED_CAST")
            (handler as UIComponent<Component>).draw(component, context, instance)
        } else {
            MarmotClient.LOGGER.error("UI Component Renderer handler not found for ${component.compType}")
        }
    }
}
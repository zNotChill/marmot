package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.classes.BorderPosition
import me.znotchill.marmot.common.ui.components.Component
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

abstract class UIComponent<T : Component> {
    /**
     * Draw only the inner content of the component (e.g., box fill, gradient, sprite, etc.)
     */
    protected abstract fun drawContent(component: T, context: DrawContext, instance: MinecraftClient)

    /**
     * The final draw call in the process. Applies transforms, renders, and borders.
     */
    fun draw(component: T, context: DrawContext, instance: MinecraftClient) {
        UIRenderer.applyComponentMatrices(context, component)

        drawContent(component, context, instance)
        drawBorder(component, context)

        context.matrices.popMatrix()
    }

    /**
     * Default border rendering implementation, shared by all components
     * by default.
     */
    open fun drawBorder(component: Component, context: DrawContext) {
        val border = component.props.border
        if (border.width <= 0f || !component.props.visible) return

        val w = component.width()
        val h = component.height()
        val color = border.color.toArgb()
        val offset = if (border.position == BorderPosition.OUTSIDE) -border.width else 0f

        // top
        context.fill(
            (offset).toInt(),
            (offset).toInt(),
            (w - offset).toInt(),
            (offset + border.width).toInt(),
            color
        )
        // bottom
        context.fill(
            (offset).toInt(),
            (h - offset - border.width).toInt(),
            (w - offset).toInt(),
            (h - offset).toInt(),
            color
        )
        // left
        context.fill(
            (offset).toInt(),
            (offset).toInt(),
            (offset + border.width).toInt(),
            (h - offset).toInt(),
            color
        )
        // right
        context.fill(
            (w - offset - border.width).toInt(),
            (offset).toInt(),
            (w - offset).toInt(),
            (h - offset).toInt(),
            color
        )
    }
}

package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.components.BoxComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class BoxRenderer : UIComponent<BoxComponent> {
    override fun draw(component: BoxComponent, context: DrawContext, instance: MinecraftClient) {
        val props = component.props

        var drawWidth = props.size.x.toInt().takeIf { it > 0 } ?: 0
        var drawHeight = props.size.y.toInt().takeIf { it > 0 } ?: 0

        if (props.fillScreen) {
            val window = instance.window
            drawWidth = window.scaledWidth
            drawHeight = window.scaledHeight
        }

        UIRenderer.applyComponentMatrices(context, component)

        context.fill(
            0, 0,
            drawWidth, drawHeight,
            props.color.copy(
                a = (props.opacity * 255).toInt()
            ).toArgb()
        )

        context.matrices.popMatrix()
    }
}
package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.common.ui.components.Box
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class BoxRenderer : UIComponent<Box>() {
    override fun drawContent(component: Box, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        var drawWidth = props.size.x.toInt()
        var drawHeight = props.size.y.toInt()

        if (props.fillScreen) {
            val window = instance.window
            drawWidth = window.scaledWidth
            drawHeight = window.scaledHeight
        }

        context.fill(
            0, 0,
            drawWidth, drawHeight,
            props.color.copy(
                a = (props.color.a - props.opacity * 255).toInt()
            ).toArgb()
        )
    }
}
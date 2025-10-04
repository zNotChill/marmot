package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.components.GradientComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class GradientRenderer : UIComponent<GradientComponent> {
    override fun draw(component: GradientComponent, context: DrawContext, instance: MinecraftClient) {
        val props = component.props

        UIRenderer.applyComponentMatrices(context, component)

        val x = props.pos.x.toInt()
        val y = props.pos.y.toInt()
        var width = props.size.x.toInt()
        var height = props.size.y.toInt()

        if (props.fillScreen) {
            val window = instance.window
            width = window.scaledWidth
            height = window.scaledHeight
        }

        context.fillGradient(
            x, y,
            x + width,
            y + height,
            props.from.toArgb(),
            props.to.toArgb()
        )

        context.matrices.popMatrix()
    }
}

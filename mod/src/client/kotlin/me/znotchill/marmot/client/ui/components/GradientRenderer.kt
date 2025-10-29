package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.common.ui.components.Gradient
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class GradientRenderer : UIComponent<Gradient>() {
    override fun drawContent(component: Gradient, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        var width = props.size.x.toInt()
        var height = props.size.y.toInt()

        if (props.fillScreen) {
            val window = instance.window
            width = window.scaledWidth
            height = window.scaledHeight
        }

        context.fillGradient(0, 0, width, height, props.from.toArgb(), props.to.toArgb())
    }
}
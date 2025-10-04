package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.components.GradientComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class GradientRenderer : UIComponent<GradientComponent> {
    override fun draw(component: GradientComponent, context: DrawContext, instance: MinecraftClient) {
        val props = component.props

        UIRenderer.applyComponentMatrices(context, component)

        context.fillGradient(
            0, 0,
            (props.pos.x + props.size.x).toInt(),
            (props.pos.y + props.size.y).toInt(),
            props.from.toArgb(),
            props.to.toArgb()
        )

        context.matrices.popMatrix()
    }
}
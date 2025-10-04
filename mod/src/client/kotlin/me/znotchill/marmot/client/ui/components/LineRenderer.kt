package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.components.LineComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class LineRenderer : UIComponent<LineComponent> {
    override fun draw(component: LineComponent, context: DrawContext, instance: MinecraftClient) {
        val (from, to) = component.props
        val props = component.props
        val points = from.lineTo(to)

        UIRenderer.applyComponentMatrices(context, component)
        points.forEach { point ->
            context.fill(
                point.x.toInt(),
                point.y.toInt(),
                (point.x + props.pointSize.x).toInt(),
                (point.y + props.pointSize.y).toInt(),
                props.color.toArgb()
            )
        }
        context.matrices.popMatrix()
    }
}
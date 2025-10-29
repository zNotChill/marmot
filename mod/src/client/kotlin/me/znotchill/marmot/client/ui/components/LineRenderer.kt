package me.znotchill.marmot.client.ui.components

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import me.znotchill.marmot.common.ui.components.Line

class LineRenderer : UIComponent<Line>() {
    override fun drawContent(component: Line, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        val points = props.from.lineTo(props.to)

        points.forEach { point ->
            context.fill(
                point.x.toInt(),
                point.y.toInt(),
                (point.x + props.pointSize.x).toInt(),
                (point.y + props.pointSize.y).toInt(),
                props.color.toArgb()
            )
        }
    }
}

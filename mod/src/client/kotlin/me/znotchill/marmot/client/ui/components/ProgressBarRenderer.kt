package me.znotchill.marmot.client.ui.components

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import me.znotchill.marmot.common.ui.components.ProgressBar

class ProgressBarRenderer : UIComponent<ProgressBar>() {
    override fun drawContent(component: ProgressBar, context: DrawContext, instance: MinecraftClient) {
        val props = component.props

        val x = props.pos.x
        val y = props.pos.y
        val width = props.size.x
        val height = props.size.y

        // background (empty fill)
        props.emptyColor?.let {
            context.fill(
                x.toInt(),
                y.toInt(),
                (x + width).toInt(),
                (y + height).toInt(),
                it.toArgb()
            )
        }

        // progress fill
        val clampedProgress = props.progress.coerceIn(0f, 1f)
        val fillWidth = width * clampedProgress

        props.fillColor?.let {
            context.fill(
                x.toInt(),
                y.toInt(),
                (x + fillWidth).toInt(),
                (y + height).toInt(),
                it.toArgb()
            )
        }
    }
}

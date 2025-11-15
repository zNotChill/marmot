package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.TextInterpolator
import me.znotchill.marmot.common.ui.components.Text
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class TextRenderer : UIComponent<Text>() {
    override fun drawContent(component: Text, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        val renderer = MinecraftClient.getInstance().textRenderer

        val x = props.pos.x
        val y = props.pos.y

        // interpolate dynamic variables & split into lines
        val text = TextInterpolator.interpolate(props.text)
        val lines = text.split("\n")

        // measure and get widest line
        val textWidth = lines.maxOfOrNull { renderer.getWidth(it) }?.toFloat() ?: 0f
        val textHeight = (renderer.fontHeight * lines.size).toFloat()

        val scaleX = props.textScale.x
        val scaleY = props.textScale.y

        val paddingX = props.padding.x
        val paddingY = props.padding.y

        val scaledTextWidth = textWidth * scaleX
        val scaledTextHeight = textHeight * scaleY

        val bgWidth = scaledTextWidth + paddingX * 2
        val bgHeight = scaledTextHeight + paddingY * 2

        // Draw background
        props.backgroundColor?.let { bg ->
            context.fill(
                x.toInt(),
                y.toInt(),
                (x + bgWidth).toInt(),
                (y + bgHeight - 2).toInt(),
                bg.toArgb()
            )
        }

        val textStartX = x + paddingX
        val textStartY = y + paddingY

        context.matrices.pushMatrix()
        context.matrices.translate(textStartX, textStartY)
        context.matrices.scale(scaleX, scaleY)

        val color = props.color.copy(a = (props.opacity * 255).toInt()).toArgb()

        for (line in lines) {
            context.drawText(renderer, line, 0, 0, color, props.shadow)
            context.matrices.translate(0f, renderer.fontHeight.toFloat())
        }

        context.matrices.popMatrix()
    }
}

package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.common.ui.components.Text
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class TextRenderer : UIComponent<Text>() {
    override fun drawContent(component: Text, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        val renderer = MinecraftClient.getInstance().textRenderer

        val x = props.pos.x
        val y = props.pos.y

        val text = props.text
        val textWidth = renderer.getWidth(text).toFloat()
        val textHeight = renderer.fontHeight.toFloat()

        val scaleX = props.textScale.x
        val scaleY = props.textScale.y

        val paddingX = props.padding.x
        val paddingY = props.padding.y

        val scaledTextWidth = textWidth * scaleX
        val scaledTextHeight = textHeight * scaleY

        val bgWidth = scaledTextWidth + paddingX * 2
        val bgHeight = scaledTextHeight + paddingY * 2

        props.backgroundColor?.let { bg ->
            context.fill(
                x.toInt(),
                y.toInt(),
                (x + bgWidth).toInt(),

                // very arbitrary value (-2) but text backgrounds appear to go too deep without it
                (y + bgHeight - 2).toInt(),
                bg.toArgb()
            )
        }

        val textStartX = x + paddingX + (bgWidth - scaledTextWidth) / 2 - paddingX
        val textStartY = y + paddingY + (bgHeight - scaledTextHeight) / 2 - paddingY

        context.matrices.pushMatrix()
        context.matrices.translate(textStartX, textStartY)
        context.matrices.scale(props.textScale.x, props.textScale.y)

        context.drawText(
            renderer,
            text,
            0,
            0,
            props.color.copy(a = (props.opacity * 255).toInt()).toArgb(),
            props.shadow
        )

        context.matrices.popMatrix()
    }

}

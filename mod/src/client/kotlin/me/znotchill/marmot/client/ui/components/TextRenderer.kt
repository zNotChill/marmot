package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.common.ui.components.TextComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class TextRenderer : UIComponent<TextComponent> {
    override fun draw(component: TextComponent, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        val renderer = MinecraftClient.getInstance().textRenderer

        props.backgroundColor?.let { bg ->
            context.fill(
                component.screenX,
                component.screenY,
                component.screenX + component.width(),
                component.screenY + component.height(),
                bg.toArgb()
            )
        }

        context.matrices.pushMatrix()

        val scale = props.scale
        context.matrices.scale(scale.x, scale.y)
        context.matrices.translate(
            (component.screenX + props.padding.left) / scale.x,
            (component.screenY + props.padding.top) / scale.y
        )

        context.drawText(
            renderer,
            props.text,
            0,
            0,
            props.color.copy(
                a = (props.opacity * 255).toInt()
            ).toArgb(),
            props.shadow
        )

        context.matrices.popMatrix()
    }
}
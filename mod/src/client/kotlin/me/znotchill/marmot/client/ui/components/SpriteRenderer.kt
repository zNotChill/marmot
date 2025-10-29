package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.components.Sprite
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext

class SpriteRenderer : UIComponent<Sprite>() {
    override fun drawContent(component: Sprite, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        val texture = UIRenderer.getIdentifier(props.texturePath)
        val texWidth = component.computedSize?.x?.toInt() ?: 16
        val texHeight = component.computedSize?.y?.toInt() ?: 16

        var drawWidth = props.size.x.toInt().takeIf { it > 0 } ?: texWidth
        var drawHeight = props.size.y.toInt().takeIf { it > 0 } ?: texHeight

        if (props.fillScreen) {
            val window = instance.window
            drawWidth = window.scaledWidth
            drawHeight = window.scaledHeight
        }

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            texture,
            0,
            0,
            0f, 0f,
            drawWidth, drawHeight,
            drawWidth, drawHeight,
        )
    }
}

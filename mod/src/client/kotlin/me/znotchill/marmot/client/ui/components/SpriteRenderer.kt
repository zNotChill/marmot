package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.UIRenderer
import me.znotchill.marmot.common.ui.components.SpriteComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext

class SpriteRenderer : UIComponent<SpriteComponent> {
    override fun draw(component: SpriteComponent, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        val texture = UIRenderer.getIdentifier(props.texturePath)
        val texWidth = component.computedSize?.x?.toInt() ?: 16
        val texHeight = component.computedSize?.y?.toInt() ?: 16

        var drawWidth = props.size.x.toInt()
        if (drawWidth == 0)
            drawWidth = texWidth

        var drawHeight = props.size.y.toInt()
        if (drawHeight == 0)
            drawHeight = texHeight

        UIRenderer.applyComponentMatrices(context, component)

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            texture,
            0,
            0,
            0f, 0f,
            drawWidth, drawHeight,
            drawWidth, drawHeight,
        )

        context.matrices.popMatrix()
    }
}
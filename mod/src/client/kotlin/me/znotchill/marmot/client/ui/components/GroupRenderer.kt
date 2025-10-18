package me.znotchill.marmot.client.ui.components

import me.znotchill.marmot.client.ui.draw
import me.znotchill.marmot.common.ui.components.GroupComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class GroupRenderer : UIComponent<GroupComponent> {
    override fun draw(component: GroupComponent, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        props.backgroundColor?.let { bg ->
            val components = props.components
            val minX = components.minOfOrNull { it.screenX } ?: 0
            val maxX = components.maxOfOrNull { it.screenX + it.width() } ?: 0
            val minY = components.minOfOrNull { it.screenY } ?: 0
            val maxY = components.maxOfOrNull { it.screenY + it.height() } ?: 0

            context.fill(
                (minX - props.padding.left).toInt(),
                (minY - props.padding.top).toInt(),
                maxX.toInt() + props.padding.right.toInt(),
                maxY.toInt() + props.padding.bottom.toInt(),
                bg.toArgb()
            )
        }

        props.components.forEach { child -> child.draw(context) }
    }
}
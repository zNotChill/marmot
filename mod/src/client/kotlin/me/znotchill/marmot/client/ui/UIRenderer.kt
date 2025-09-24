package me.znotchill.marmot.client.ui

import me.znotchill.marmot.common.ui.*
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.GroupComponent
import me.znotchill.marmot.common.ui.components.TextComponent
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

object UIRenderer {
    private var currentWindow: UIWindow? = null

    fun setWindow(window: UIWindow?) {
        currentWindow = window
    }

//    fun applyDiff(diff: UIDiff) {
//        when (diff) {
//            is UIDiff.Full -> setWindow(diff.window)
//            is UIDiff.Update -> {
//                val comp = currentWindow?.getComponentByIdDeep(diff.id)
//                if (comp != null) {
//                    println("updating")
//                }
//            }
//            is UIDiff.Add -> {
//                currentWindow?.components?.add(diff.component)
//            }
//            is UIDiff.Remove -> {
//                currentWindow?.components?.removeIf { it.id == diff.id }
//            }
//        }
//    }

    fun register() {
        HudRenderCallback.EVENT.register { context, _ ->
            currentWindow?.let { window ->
                layout(window)
                window.components.values.forEach { component ->
                    component.draw(context)
                }
            }
        }
    }

    /**
     * Performs a layout pass, resolving every component's position.
     */
    private fun layout(window: UIWindow) {
        window.components.forEach { component ->
            layoutComponent(component.value, window)
        }
    }

    /**
     * Layout a component.
     */
    private fun layoutComponent(component: Component, window: UIWindow) {
        resolvePosition(component, window)

        if (component is GroupComponent) {
            component.props.components.forEach { child ->
                resolvePosition(child, window)
            }
        }
    }


    /**
     * Resolve absolute screen position for a component.
     */
    private fun resolvePosition(component: Component, window: UIWindow) {
        val screenWidth = MinecraftClient.getInstance().window.scaledWidth
        val screenHeight = MinecraftClient.getInstance().window.scaledHeight

        val width = component.width()
        val height = component.height()

        var resolvedX = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT -> component.props.pos.x
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER -> (screenWidth / 2f + component.props.pos.x - width / 2f)
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT -> (screenWidth - component.props.pos.x - width)
        }

        var resolvedY = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT -> component.props.pos.y
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT -> (screenHeight / 2f + component.props.pos.y - height / 2f)
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT -> (screenHeight - component.props.pos.y - height)
        }

        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId) ?: return@let
            when (component.relativePosition) {
                RelativePosition.RIGHT_OF -> {
                    resolvedX = relativeTo.screenX + relativeTo.width() + component.props.pos.x
                    resolvedY = relativeTo.screenY + component.props.pos.y
                }
                RelativePosition.LEFT_OF -> {
                    resolvedX = relativeTo.screenX - width + component.props.pos.x
                    resolvedY = relativeTo.screenY + component.props.pos.y
                }
                RelativePosition.BELOW -> {
                    resolvedX = relativeTo.screenX + component.props.pos.x
                    resolvedY = relativeTo.screenY + relativeTo.height() + component.props.pos.y
                }
                RelativePosition.ABOVE -> {
                    resolvedX = relativeTo.screenX + component.props.pos.x
                    resolvedY = relativeTo.screenY - height + component.props.pos.y
                }
                else -> {}
            }
        }

        component.screenX = resolvedX.toInt()
        component.screenY = resolvedY.toInt()
    }
}

private fun Component.draw(context: DrawContext) {
    when (this) {
        is TextComponent -> {
            val renderer = MinecraftClient.getInstance().textRenderer

            props.backgroundColor?.let { bg ->
                context.fill(screenX, screenY, screenX + this.width(), screenY + this.height(), bg.toArgb())
            }

            context.drawText(
                renderer,
                props.text,
                screenX + props.padding.left,
                screenY + props.padding.top,
                props.color.toArgb(),
                props.shadow
            )
        }

//        is BoxComponent -> {
//            context.fill(screenX, screenY, screenX + width, screenY + height, color.toArgb())
//        }

        is GroupComponent -> {
            props.backgroundColor?.let { bg ->
                val components = props.components
                val minX = components.minOfOrNull { it.screenX } ?: 0
                val maxX = components.maxOfOrNull { it.screenX + it.width() } ?: 0
                val minY = components.minOfOrNull { it.screenY } ?: 0
                val maxY = components.maxOfOrNull { it.screenY + it.height() } ?: 0

                context.fill(
                    minX - props.padding.left,
                    minY - props.padding.top,
                    maxX + props.padding.right,
                    maxY + props.padding.bottom,
                    bg.toArgb()
                )
            }

            props.components.forEach { child -> child.draw(context) }
        }

        else -> {}
    }
}

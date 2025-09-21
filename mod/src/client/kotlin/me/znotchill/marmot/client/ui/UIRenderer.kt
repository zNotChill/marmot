package me.znotchill.marmot.client.ui

import me.znotchill.marmot.common.ui.*
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

private fun UIColor.toArgb(): Int =
    (a shl 24) or (r shl 16) or (g shl 8) or b

object UIRenderer {
    private var currentWindow: UIWindow? = null

    fun setWindow(window: UIWindow?) {
        currentWindow = window
    }

    fun register() {
        HudRenderCallback.EVENT.register { context, _ ->
            currentWindow?.let { window ->
                layout(window)
                window.components.forEach { component ->
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
            layoutComponent(component, window)
        }
    }

    /**
     * Layout a component.
     */
    private fun layoutComponent(component: UIComponent, window: UIWindow) {
        resolvePosition(component, window)

        if (component is UIGroup) {
            component.children.forEach { child ->
                resolvePosition(child, window)
            }
        }
    }


    /**
     * Resolve absolute screen position for a component.
     */
    private fun resolvePosition(component: UIComponent, window: UIWindow) {
        val screenWidth = MinecraftClient.getInstance().window.scaledWidth
        val screenHeight = MinecraftClient.getInstance().window.scaledHeight

        val width = component.width()
        val height = component.height()

        var resolvedX = when (component.anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT -> component.x
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER -> (screenWidth / 2f + component.x - width / 2f)
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT -> (screenWidth - component.x - width)
        }

        var resolvedY = when (component.anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT -> component.y
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT -> (screenHeight / 2f + component.y - height / 2f)
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT -> (screenHeight - component.y - height)
        }

        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId) ?: return@let
            when (component.relativePosition) {
                RelativePosition.RIGHT_OF -> {
                    resolvedX = relativeTo.screenX + relativeTo.width() + component.x
                    resolvedY = relativeTo.screenY + component.y
                }
                RelativePosition.LEFT_OF -> {
                    resolvedX = relativeTo.screenX - width + component.x
                    resolvedY = relativeTo.screenY + component.y
                }
                RelativePosition.BELOW -> {
                    resolvedX = relativeTo.screenX + component.x
                    resolvedY = relativeTo.screenY + relativeTo.height() + component.y
                }
                RelativePosition.ABOVE -> {
                    resolvedX = relativeTo.screenX + component.x
                    resolvedY = relativeTo.screenY - height + component.y
                }
                else -> {}
            }
        }

        component.screenX = resolvedX.toInt()
        component.screenY = resolvedY.toInt()
    }
}

private fun UIComponent.draw(context: DrawContext) {
    when (this) {
        is UIText -> {
            val renderer = MinecraftClient.getInstance().textRenderer

            backgroundColor?.let { bg ->
                context.fill(screenX, screenY, screenX + this.width(), screenY + this.height(), bg.toArgb())
            }

            context.drawText(
                renderer,
                text,
                screenX + padding.left,
                screenY + padding.top,
                color.toArgb(),
                shadow
            )
        }

        is UIBox -> {
            context.fill(screenX, screenY, screenX + width, screenY + height, color.toArgb())
        }

        is UIGroup -> {
            backgroundColor?.let { bg ->
                val minX = children.minOfOrNull { it.screenX } ?: 0
                val maxX = children.maxOfOrNull { it.screenX + it.width() } ?: 0
                val minY = children.minOfOrNull { it.screenY } ?: 0
                val maxY = children.maxOfOrNull { it.screenY + it.height() } ?: 0

                context.fill(
                    minX - padding.left,
                    minY - padding.top,
                    maxX + padding.right,
                    maxY + padding.bottom,
                    bg.toArgb()
                )
            }

            children.forEach { child -> child.draw(context) }
        }

        else -> {
        }
    }
}

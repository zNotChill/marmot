package me.znotchill.marmot.client.ui

import me.znotchill.marmot.common.ui.*
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.GroupComponent
import me.znotchill.marmot.common.ui.components.TextComponent
import me.znotchill.marmot.common.ui.events.DestroyEvent
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.PropertyAnimation
import me.znotchill.marmot.common.ui.events.UIEvent
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Matrix3x2f

object UIRenderer {
    private var currentWindow: UIWindow? = null
    private val activeAnimations = mutableListOf<PropertyAnimation<Any>>()
    private var lastTime: Long = System.nanoTime()

    fun setWindow(window: UIWindow?) {
        currentWindow = window
    }

    fun register() {
        HudRenderCallback.EVENT.register { context, _ ->
            val now = System.nanoTime()
            val deltaSeconds = (now - lastTime) / 1_000_000_000.0
            lastTime = now

            tickAnimations(deltaSeconds)

            currentWindow?.let { window ->
                layout(window)
                window.components.values.forEach { component ->
                    component.draw(context)
                }
            }
        }
    }

    fun applyEvent(event: UIEvent) {
        val comp = currentWindow?.getComponentByIdDeep(event.targetId) ?: return

        when (event) {
            is MoveEvent -> {
                val anim = PropertyAnimation(
                    targetId = event.targetId,
                    getter = { comp.props.pos },
                    setter = { c, value -> c.props.pos = value },
                    from = null,
                    to = event.to,
                    durationSeconds = event.durationSeconds,
                    easing = event.easing
                )
                anim.window = event.window
                enqueueAnimation(anim)
            }

            is PropertyAnimation<*> -> {
                enqueueAnimation(event)
            }

            is DestroyEvent -> {
                currentWindow?.components?.remove(event.targetId)
            }

            else -> {
                println("Unknown event type: $event")
            }
        }
    }

    fun <T> enqueueAnimation(event: PropertyAnimation<T>) {
        val comp = currentWindow?.getComponentByIdDeep(event.targetId)
        if (comp != null && event.from == null) {
            @Suppress("UNCHECKED_CAST")
            event.from = event.getter(comp)
        }
        @Suppress("UNCHECKED_CAST")
        activeAnimations += event as PropertyAnimation<Any>
    }

    fun tickAnimations(deltaSeconds: Double) {
        val iterator = activeAnimations.iterator()
        while (iterator.hasNext()) {
            val anim = iterator.next()

            val comp = currentWindow?.getComponentByIdDeep(anim.targetId) ?: continue
            val start = anim.from ?: anim.getter(comp)

            anim.elapsed += deltaSeconds
            val t = (anim.elapsed / anim.durationSeconds).coerceIn(0.0, 1.0)
            val easedT = applyEasing(t, anim.easing)

            @Suppress("UNCHECKED_CAST")
            when (start) {
                is Float -> (anim as PropertyAnimation<Float>).setter(comp, lerp(start, anim.to, easedT))
                is Int -> (anim as PropertyAnimation<Int>).setter(comp, lerp(start.toFloat(), anim.to.toFloat(), easedT).toInt())
                is Vec2 -> {
                    val target = anim.to as Vec2
                    val result = Vec2(
                        lerp(start.x, target.x, easedT),
                        lerp(start.y, target.y, easedT)
                    )
                    (anim as PropertyAnimation<Vec2>).setter(comp, result)
                }
                else -> anim.setter(comp, anim.to)
            }

            if (t >= 1.0) iterator.remove()
        }
    }

    fun lerp(start: Float, end: Float, t: Double): Float {
        return start + ((end - start) * t).toFloat()
    }

    fun applyEasing(t: Double, easing: String): Double {
        return when (easing.lowercase()) {
            "linear" -> t
            "easein" -> t * t
            "easeout" -> 1 - (1 - t) * (1 - t)
            "easeinout" -> if (t < 0.5) 2 * t * t
                else 1 - 2 * (1 - t) * (1 - t)
            else -> t
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
    private fun layoutComponent(component: Component, window: UIWindow, parentScale: Vec2 = Vec2(1f, 1f)) {
        val effectiveScale = Vec2(
            component.props.scale.x * parentScale.x,
            component.props.scale.y * parentScale.y
        )

        component.computedScale = effectiveScale

        resolvePosition(component, window)

        if (component is GroupComponent) {
            component.props.components.forEach { child ->
                layoutComponent(child, window, effectiveScale)
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

            context.matrices.pushMatrix()

            val scale = props.scale

            context.matrices.scale(scale.x, scale.y)

            context.matrices.translate(
                (screenX + props.padding.left) / scale.x,
                (screenY + props.padding.top) / scale.y
            )

            context.drawText(
                renderer,
                props.text,
                0,
                0,
                props.color.toArgb(),
                props.shadow
            )

            context.matrices.popMatrix()
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
